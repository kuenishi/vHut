/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.task;

import static jp.co.ntts.vhut.entity.Names.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import jp.co.ntts.vhut.command.ICommand;
import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.factory.CommandFactory;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.chronos.core.TaskTrigger;
import org.seasar.chronos.core.annotation.task.Task;
import org.seasar.chronos.core.annotation.task.method.NextTask;
import org.seasar.config.core.container.ConfigContainer;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.OrderByItem;
import org.seasar.extension.jdbc.OrderByItem.OrderingSpec;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>
 * クラウド関連のバッチ処理を行うタスク <br>
 * <p>
 * 短い間隔で呼び出され以下の処理を実行する。
 * <ul>
 * <li>実行中のコマンドを取得する。
 * <li>ドライバを使ってコマンドの状況を取得する。
 * <li>コマンドの状態を更新する。
 * <li>実行可能なコマンドを取得する。
 * <li>優先順位に沿って並べ替える。
 * <li>ドライバを使ってコマンドを実行する。
 * <li>コマンドの状態を更新する。
 * </ul>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
@Task
public class CloudTask {

    /**
     * ロガー.
     */
    private static VhutLogger logger = VhutLogger.getLogger(CloudTask.class);

    /**
     * クラウドの識別子.
     * TODO : マネージャからの設定を想定
     */
    public long cloudId = 1L;

    /**
     * SeasarのJdbcManager.
     */
    public JdbcManager jdbcManager;

    /**
     * 外部ファイルcloud.propertiesを読み込んで設定値を提供します.
     */
    public CloudConfig cloudConfig;

    /**
     * コマンドを生成するファクトリ.
     */
    public CommandFactory commandFactory;

    /**
     * トランザクションを手動制御
     */
    public UserTransaction userTransaction;

    /**
     * トランザクションIDタスクの識別子.
     */
    private String transactionId;

    /**
     * 各処理で発生した例外.
     */
    private Exception exception;


    /**
     * DI登録後実行されます.
     */
    @InitMethod
    public void init() {
        ConfigContainer configContainer = SingletonS2Container.getComponent(ConfigContainer.class);
        configContainer.loadToBeans();
    }

    //    /**
    //     * 初期化処理.
    //     */
    //    public void initialize() {
    //        logger.debug("DCLDT1001");
    //    }

    /**
     * このタスクを呼び出すトリガを取得する.
     * Schedulerから呼び出されます。
     * @return 設定ファイルで定義したトリガー
     */
    public TaskTrigger getTrigger() {
        return cloudConfig.getCloudTaskTrigger();
    }

    /**
     * 開始処理.
     */
    @NextTask("updateExecutingCommands")
    public void start() {
        transactionId = VhutLogger.createTransactionId();
        logger.start(transactionId, "ICLDT0011", new Object[]{});
    }

    /**
     * 実行中のコマンドの状態を確認して更新します.
     * <ul>
     * <li>実行中のコマンドを取得する。
     * <li>ドライバを使ってコマンドの状態を更新する。
     * </ul>
     * @throws SystemException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws NotSupportedException
     */
    @NextTask("insertSyncCommands")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doUpdateExecutingCommands() throws IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException, SystemException, NotSupportedException {
        //実行中コマンド取得
        List<Command> executingCommands = jdbcManager.from(Command.class)
            .leftOuterJoin(command().dependingCommand())
            .where(new SimpleWhere().eq(command().cloudId(), cloudId)
                .eq(command().status(), CommandStatus.EXECUTING))
            .orderBy(command().id()
                .toString())
            .eager(command().cloudId(), command().parameter(), command().result(), command().startTime(), command().endTime(), command().errorMessage(), command().dependingCommand()
                .cloudId(), command().dependingCommand()
                .parameter(), command().dependingCommand()
                .result(), command().dependingCommand()
                .startTime(), command().dependingCommand()
                .endTime(), command().dependingCommand()
                .errorMessage())
            .getResultList();
        //ドライバを使ってコマンドの状態を更新する。
        for (Command executingCommand : executingCommands) {
            userTransaction.begin();
            try {
                updateCommand(executingCommand);
            } catch (Exception e) {
                logger.log("ECLDT5011", new Object[]{ executingCommand.id, executingCommand.operation }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 同期系のコマンドをDBに挿入します.
     * <ul>
     * <li>同期系コマンド種別をリスト化
     * <li>最終コマンドの開始時刻から指定時間過ぎているものをDBに登録
     * </ul>
     * @throws SystemException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws NotSupportedException
     */
    @NextTask("executeWaitingCommands")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doInsertSyncCommands() throws IllegalStateException, SecurityException, SystemException, HeuristicMixedException, HeuristicRollbackException, RollbackException, NotSupportedException {
        Map<CommandOperation, Integer> operationIntervalMap = cloudConfig.getOperationIntervalMap();
        for (CommandOperation operation : operationIntervalMap.keySet()) {
            userTransaction.begin();
            try {
                if (canInsertSyncCommand(operation, operationIntervalMap.get(operation))) {
                    Command command = createSyncCommand(operation);
                    jdbcManager.insert(command)
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ECLDT5012", new Object[]{ operation }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 待機中のコマンドを実行します.
     * <ul>
     * <li>待機中コマンドを取得する
     * <li>スタックに分類する
     * <li>実行可能なコマンドがなくなるまでスタックからコマンドを取得して実行、結果を記録する
     * </ul>
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doExecuteWaitingCommands() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        //待機中のコマンド取得
        List<Command> waitingCommands = jdbcManager.from(Command.class)
            .leftOuterJoin(command().dependingCommand())
            .where(new SimpleWhere().eq(command().cloudId(), cloudId)
                .eq(command().status(), CommandStatus.WAITING))
            .eager(command().cloudId(), command().parameter(), command().result(), command().startTime(), command().endTime(), command().errorMessage(), command().dependingCommand()
                .cloudId(), command().dependingCommand()
                .parameter(), command().dependingCommand()
                .result(), command().dependingCommand()
                .startTime(), command().dependingCommand()
                .endTime(), command().dependingCommand()
                .errorMessage())
            .getResultList();
        //スタックに分類する
        List<List<Command>> commandStacks = createCommandStacks(waitingCommands);
        List<Command> commands;
        do {
            //スタックから1回のターンで
            commands = getExecutableCommandsFromStacks(commandStacks);
            for (Command command : commands) {
                userTransaction.begin();
                try {
                    Command resultCommand = executeCommand(command);
                    command.startTime = resultCommand.startTime;
                    command.endTime = resultCommand.endTime;
                    command.status = resultCommand.status;
                } catch (Exception e) {
                    logger.log("ECLDT5013", new Object[]{ command.id, command.operation }, e);
                    command.status = CommandStatus.UNKNOWN;
                    userTransaction.rollback();
                    continue;
                }
                userTransaction.commit();
            }
        } while (commands.size() > 0);
    }

    /**
     * コマンドを実行し状態を更新します.
     * @param command コマンドエンティティ
     * @return 更新後のコマンドエンティティ
     */
    protected Command executeCommand(Command command) {
        //実行待ち or 依存コマンドが成功 or キャンセルでない場合は終了
        if (!command.status.equals(CommandStatus.WAITING)) {
            return command;
        } else if (command.dependingCommand != null) {
            switch (command.dependingCommand.status) {
                case SUCCESS:
                case CANCELED:
                    break;
                default:
                    return command;
            }
        }
        //上記以外のコマンドを実行
        command.startTime = TimestampUtil.getCurrentTimestamp();
        ICommand realCommand = commandFactory.newCommand(command);
        try {
            CommandStatus resultStatus = realCommand.execute(); //外部に接続し状態を取得
            if (resultStatus.equals(CommandStatus.SUCCESS)) {
                realCommand.finish(); //成功の場合は終了処理
            }
        } catch (CommandExecutionRuntimeException e) {
            logger.error("Command execution is faild.", e);
        }
        Command resultCommand = realCommand.getCommand(); //実行後のコマンドエンティティ
        //成功 or エラー の時は終了時刻を設定
        switch (resultCommand.status) {
            case SUCCESS:
            case ERROR:
                resultCommand.endTime = TimestampUtil.getCurrentTimestamp();
                break;
            default:
                break;
        }
        //コマンドをDBに記録
        jdbcManager.update(resultCommand)
            .includes(command().startTime(), command().endTime(), command().status(), command().result(), command().errorMessage())
            .execute();
        return resultCommand;
    }

    /**
     * 実行中コマンドの状態を更新します.
     * @param command コマンドエンティティ
     * @return 更新後のコマンドエンティティ
     */
    protected Command updateCommand(Command command) {
        //実行中でない場合は終了
        if (!command.status.equals(CommandStatus.EXECUTING)) {
            return command;
        }
        //依存関係があるコマンドが終了もしくはキャンセル済みでない場合はエラーとしてマーク
        if (command.dependingCommand != null) {
            switch (command.dependingCommand.status) {
                case SUCCESS:
                case CANCELED:
                    break;
                default:
                    command.status = CommandStatus.ERROR;
                    command.endTime = TimestampUtil.getCurrentTimestamp();
                    jdbcManager.update(command)
                        .includes(command().endTime(), command().status())
                        .execute();
                    return command;
            }
        }
        //上記以外のコマンドの状態を確認
        ICommand realCommand = commandFactory.newCommand(command);
        CommandStatus resultStatus = realCommand.updateStatus(); //外部に接続し状態を取得
        if (resultStatus.equals(CommandStatus.SUCCESS)) {
            realCommand.finish(); //成功の場合は終了処理
        }
        Command resultCommand = realCommand.getCommand(); //実行後のコマンドエンティティ
        //成功 or エラー の時は終了時刻を設定
        switch (resultCommand.status) {
            case SUCCESS:
            case ERROR:
                resultCommand.endTime = TimestampUtil.getCurrentTimestamp();
                break;
            default:
                break;
        }
        //タイムアウト時の処理

        //コマンドをDBに記録
        jdbcManager.update(resultCommand)
            .includes(command().startTime(), command().endTime(), command().status(), command().result(), command().errorMessage())
            .execute();
        return resultCommand;
    }

    /**
     * 同期系コマンドが追加可能か判断します.
     * 直前のコマンドのステータスで分類
     * <ul>
     * <li>実行待ち false
     * <li>実行中 false
     * <li>正常終了 -> 終了時刻比較
     * <li>異常終了 -> 終了時刻比較
     * <li>キャンセル済み 例外
     * <li>状態不明 例外
     * </ul>
     * @param operation オペレーション
     * @param minutes 前回終了時刻との間隔（分）
     * @return 追加可能の場合true
     */
    protected boolean canInsertSyncCommand(CommandOperation operation, Integer minutes) {
        Command lastCommand = null; //ひとつ前のコマンド
        List<Command> commands = jdbcManager.from(Command.class)
            .eager(command().endTime())
            .where(new SimpleWhere().eq(command().cloudId(), cloudId)
                .eq(command().operation(), operation)
                .ne(command().status(), CommandStatus.CANCELED)
                .ne(command().status(), CommandStatus.UNKNOWN))
            .orderBy(new OrderByItem(command().id(), OrderingSpec.DESC))
            .maxRows(1)
            .getResultList();
        if (commands.size() > 0) {
            lastCommand = commands.get(0);
        }
        // 前回実行したコマンドがない場合、追加してよい。
        if (lastCommand == null) {
            return true;
        }
        // 前回実行したコマンドのステータスで分類
        switch (lastCommand.status) {
            case WAITING:
            case EXECUTING:
                // 実行待ち, 実行中は終了を待つ
                return false;
            case SUCCESS:
            case ERROR:
                // 正常終了 or 異常終了 終了時期を判断
                break;
            default:
                //それ以外のステータスの場合 追加してよい。
                return true;
        }
        if (lastCommand.endTime == null) {
            // 終了時刻が設定されていない
            throw new DBStateRuntimeException(Command.class, lastCommand.id, "When status is SUCCESS or ERROR, endTime should be defined.");
        }
        // 終了時間を確認
        if ((TimestampUtil.getCurrentTimestamp()
            .getTime() - TimestampUtil.add(lastCommand.endTime, minutes, TimestampUtil.Unit.MIN)
            .getTime()) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 同期コマンドを作成します.
     * サポート対象のオペレーション
     * <ul>
     * <li>GET_ALL_CLUSTERS
     * <li>GET_HOSTS_BY_CLUSTER_ID
     * <li>GET_NETWORKS_BY_CLUSTER_ID
     * <li>GET_ALL_DATA_STORAGES
     * <li>GET_VMS_BY_CLUSTER_ID
     * <li>GET_TEMPLATES_BY_CLUSTER_ID
     * <li>GET_ALL_USERS
     * </ul>
     * @param operation オペレーション
     * @return コマンドエンティティ
     */
    protected Command createSyncCommand(CommandOperation operation) {
        if (!operation.isSync()) {
            throw new IllegalArgumentException("is not sync operation.");
        }
        ICommand realCommand = commandFactory.newCommand(operation, cloudId);
        switch (operation) {
            case GET_HOSTS_BY_CLUSTER_ID:
            case GET_NETWORKS_BY_CLUSTER_ID:
            case GET_VMS_BY_CLUSTER_ID:
            case GET_TEMPLATES_BY_CLUSTER_ID:
                realCommand.setParameter(cloudConfig.getRhevClusterId());
                break;
            default:
                break;
        }
        return realCommand.getCommand();
    }

    /**
     * 依存関係(dependingCommandId)を整理してコマンドのスタックを作成します.
     * @param commands コマンドの配列
     * @return コマンドスタックのリスト
     */
    protected List<List<Command>> createCommandStacks(List<Command> commands) {
        //WAITING以外のコマンドを除きます。
        List<Command> filteredCommands = new ArrayList<Command>();
        for (Command command : commands) {
            if (command.status.equals(CommandStatus.WAITING)) {
                filteredCommands.add(command);
            }
        }

        //コマンドIDをキーにマップに登録します
        Map<Long, Command> idCommandMap = new HashMap<Long, Command>(); //コマンドのIDとコマンド
        for (Command command : filteredCommands) {
            idCommandMap.put(command.id, command);
        }
        //dependingCommandを更新します
        Map<Long, Command> idDependedCommandMap = new HashMap<Long, Command>(); //コマンドのIDと依存されているコマンド
        for (Command command : filteredCommands) {
            if (command.dependingCommandId != null) {
                Command dependingCommand = idCommandMap.get(command.dependingCommandId);
                if (dependingCommand != null) {
                    command.dependingCommand = dependingCommand;
                    idDependedCommandMap.put(dependingCommand.id, command);
                }
            }
        }
        //スタックのリストを作ります
        List<List<Command>> stacks = new ArrayList<List<Command>>();
        for (Command command : filteredCommands) {
            //先頭のコマンドを探す
            if (command.dependingCommand == null || !command.dependingCommand.status.equals(CommandStatus.WAITING)) {
                List<Command> stack = new ArrayList<Command>();
                Command currentCommand = command;
                while (currentCommand != null) {
                    stack.add(currentCommand);
                    currentCommand = idDependedCommandMap.get(currentCommand.id);
                }
                stacks.add(stack);
            }
        }
        return stacks;
    }

    /**
     * コマンドスタックのリストから実行可能なコマンドのリストを返します.
     * @param commandStacks コマンドスタックのリスト
     * @return 実行可能なコマンドのリスト
     */
    protected List<Command> getExecutableCommandsFromStacks(List<List<Command>> commandStacks) {
        List<Command> executableCommands = new ArrayList<Command>();
        for (List<Command> stack : commandStacks) {
            STACK_LOOP: for (Command command : stack) {
                if (command.status.equals(CommandStatus.WAITING)) {
                    Command dependingCommand = command.dependingCommand;
                    if (dependingCommand != null) {
                        switch (dependingCommand.status) {
                            case SUCCESS:
                            case CANCELED:
                                break;
                            default:
                                break STACK_LOOP;
                        }

                    }
                    executableCommands.add(command);
                    break STACK_LOOP;
                }
            }
        }
        Collections.sort(executableCommands, new CommandIdComparator());
        return executableCommands;
    }

    /**
     * すべてのタスクメソッドが終了したら呼ばれます.
     */
    public void end() {
        if (exception != null) {
            logger.end(transactionId, "ECLDT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "ICLDT0012", new Object[]{});
        }
    }

    /**
     * 共通的な例外処理です.
     * @param ex 各処理で発生した例外.
     */
    public void catchException(Exception ex) {
        this.exception = ex;
    }

    /**
     * 処理が中断され、タスクオブジェクトが破棄される直前に実行されます.
     */
    public void destroy() {
        if (exception != null) {
            logger.end(transactionId, "ECLDT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "ECLDT0012", new Object[]{});
        }
    }


    class CommandIdComparator implements Comparator<Command> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Command o1, Command o2) {
            return (int) (o1.id - o2.id);
        }

    }

}


/**
 * =====================================================================
 * 
 *    Copyright 2011 NTT Sofware Corporation
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * =====================================================================
 */
