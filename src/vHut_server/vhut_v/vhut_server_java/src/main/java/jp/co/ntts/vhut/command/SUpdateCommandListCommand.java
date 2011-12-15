/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>コマンドの状態を更新するコマンド.
 * <br>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class SUpdateCommandListCommand extends AbstractCloudDriverCommand {

    private static final VhutLogger logger = VhutLogger.getLogger(SUpdateCommandListCommand.class);


    /** パラメータ保存用のキー */
    protected static enum Key {
        /** コマンドのリスト */
        COMMAND_LIST
    }


    /**
     * デフォルトコンストラクタ.
     */
    public SUpdateCommandListCommand() {
        operation = CommandOperation.UPDATE_COMMAND_LIST;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param commandList コマンドのリスト
     */
    public void setParameter(List<Command> commandList) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.COMMAND_LIST, commandList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.co.ntts.vhut.command.ICommand#execute()
     */
    @Override
    public CommandStatus execute() {
        command.status = CommandStatus.ERROR;
        try {
            if (isPrivate) {
                // プライベートクラウドの処理
                result = new ArrayList<Command>(prcDirver.updateCommandList((ArrayList<Command>) parameter.get(Key.COMMAND_LIST)));
            } else {
                // パブリッククラウドの処理
            }
            command.status = CommandStatus.SUCCESS;
        } catch (Exception e) {
            command.status = CommandStatus.ERROR;
            throw new CommandExecutionRuntimeException(operation, command.id, e);
        }
        return command.status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.co.ntts.vhut.command.ICommand#finish()
     */
    @Override
    public void finish() {
        try {
            updateCommand();
        } catch (Exception e) {
            throw new CommandFinishRuntimeException(operation, command.id, e);
        }
    }

    private void updateCommand() {
        jdbcManager.updateBatch((List<Command>) result)
            .execute();
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @Override
    public void setParameter(Serializable... args) {
        throw new UnsupportedOperationException();
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
