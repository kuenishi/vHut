/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.task;

import static jp.co.ntts.vhut.entity.Names.application;
import static jp.co.ntts.vhut.entity.Names.applicationInstance;
import static jp.co.ntts.vhut.entity.Names.releasedApplication;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceStatus;
import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
import jp.co.ntts.vhut.entity.ApplicationStatus;
import jp.co.ntts.vhut.entity.ApplicationVm;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.ReleasedApplication;
import jp.co.ntts.vhut.entity.ReleasedApplicationStatus;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.chronos.core.TaskTrigger;
import org.seasar.chronos.core.annotation.task.Task;
import org.seasar.chronos.core.annotation.task.method.NextTask;
import org.seasar.config.core.container.ConfigContainer;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>サービス関連の状態管理を行うタスク.
 * <br>
 * <p>短い間隔で呼び出され以下の処理を実行する。
 * <ul>
 * <li>Applicationの状態を確認して状態遷移させる。
 * <li>ApplicationInstanceの状態を確認して状態遷移させる。
 * <li>ReleasedApplicationの状態を確認して状態遷移させる。
 * </ul>
 */
@Task
public class ServiceCheckTask {

    /**
     * ロガー.
     */
    private static VhutLogger logger = VhutLogger.getLogger(ServiceCheckTask.class);

    /**
     * デフォルトトリガー.
     */
    //    private static final TaskTrigger DEFAULT_TRIGGER = new CCronTrigger("*/10 * * * * ?");

    /**
     * サービスの設定.
     */
    public ServiceConfig serviceConfig;

    /**
     * JDBCアクセス手段.
     */
    public JdbcManager jdbcManager;

    /**
     * クラウドロジックの作成クラス.
     */
    public CloudLogicFactory cloudLogicFactory;

    /**
     * 現在時刻（検索用）.
     */
    public Timestamp currentTime;

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


    @InitMethod
    public void init() {
        ConfigContainer configContainer = SingletonS2Container.getComponent(ConfigContainer.class);
        configContainer.loadToBeans();
    }

    //    /**
    //     * 初期化処理.
    //     */
    //    public void initialize() {
    //        logger.debug("DSVCT0011");
    //    }

    /**
     * このタスクを呼び出すトリガを取得する.
     * Schedulerから呼び出されます。
     * @return 設定ファイルで定義したトリガー
     */
    public TaskTrigger getTrigger() {
        return serviceConfig.getServiceCheckTaskTrigger();
    }

    /**
     * 開始処理.
     */
    @NextTask("checkCreatingApplication")
    public void start() {
        transactionId = VhutLogger.createTransactionId();
        currentTime = TimestampUtil.getCurrentTimestamp();
        logger.start(transactionId, "ISVCT0011", new Object[]{});
    }

    /**
     * Applicationの状態遷移（CREATING->DEACTIVE）を行います.
     * <p>
     * 対象:Application<br>
     * 状態遷移：CREATING->DEACTIVE
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws IllegalStateException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     */
    @NextTask("checkUpdatingApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckCreatingApplication() throws IllegalStateException, SystemException, NotSupportedException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<Application> apps = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .where(new SimpleWhere().eq(application().status(), ApplicationStatus.CREATING))
            .getResultList();
        for (Application app : apps) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationVm avm : app.applicationVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(avm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(avm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    app.status = ApplicationStatus.DEACTIVE;
                    jdbcManager.update(app)
                        .includes(application().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5011", new Object[]{ app.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * Applicationの状態遷移（UPDATING->DEACTIVE）を行います.
     * <p>
     * 対象:Application<br>
     * 状態遷移：UPDATING->DEACTIVE
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("checkReleasingApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckUpdatingApplication() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<Application> apps = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .where(new SimpleWhere().eq(application().status(), ApplicationStatus.UPDATING))
            .getResultList();
        for (Application app : apps) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationVm avm : app.applicationVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(avm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(avm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    app.status = ApplicationStatus.DEACTIVE;
                    jdbcManager.update(app)
                        .includes(application().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5012", new Object[]{ app.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * Applicationの状態遷移（RELEASING->DEACTIVE）を行います.
     * <p>
     * 対象:Application<br>
     * 状態遷移：RELEASING->DEACTIVE
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("checkDeletingApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckReleasingApplication() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<Application> apps = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .where(new SimpleWhere().eq(application().status(), ApplicationStatus.RELEASING))
            .getResultList();
        for (Application app : apps) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationVm avm : app.applicationVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(avm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(avm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    app.status = ApplicationStatus.DEACTIVE;
                    jdbcManager.update(app)
                        .includes(application().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5013", new Object[]{ app.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * Applicationの状態遷移（DELETING->DELETED）を行います.
     * <p>
     * 対象:Application<br>
     * 状態遷移：DELETING->DELETED
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("checkCreatingReleasedApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckDeletingApplication() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<Application> apps = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .where(new SimpleWhere().eq(application().status(), ApplicationStatus.DELETING))
            .getResultList();
        for (Application app : apps) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationVm avm : app.applicationVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(avm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(avm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    app.status = ApplicationStatus.DELETED;
                    jdbcManager.update(app)
                        .includes(application().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5014", new Object[]{ app.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * ReleasedApplicationの状態遷移（CREATING->READY）を行います.
     * <p>
     * 対象:ReleasedApplication<br>
     * 状態遷移：CREATING->READY
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("checkDeletingReleasedApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckCreatingReleasedApplication() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<ReleasedApplication> rapps = jdbcManager.from(ReleasedApplication.class)
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
            .where(new SimpleWhere().eq(releasedApplication().status(), ReleasedApplicationStatus.CREATING))
            .getResultList();
        for (ReleasedApplication rapp : rapps) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ReleasedApplicationTemplate rat : rapp.releasedApplicationTemplateList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(rat.cloudId);
                    List<Command> cmds = csl.getCommandListByTemplateId(rat.templateId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    rapp.status = ReleasedApplicationStatus.READY;
                    jdbcManager.update(rapp)
                        .includes(releasedApplication().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5015", new Object[]{ rapp.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * ReleasedApplicationの状態遷移（DELETING->DELETED）を行います.
     * <p>
     * 対象:ReleasedApplication<br>
     * 状態遷移：DELETING->DELETED
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("checkCreatingApplicationInstance")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckDeletingReleasedApplication() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<ReleasedApplication> rapps = jdbcManager.from(ReleasedApplication.class)
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
            .where(new SimpleWhere().eq(releasedApplication().status(), ReleasedApplicationStatus.DELETING))
            .getResultList();
        for (ReleasedApplication rapp : rapps) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ReleasedApplicationTemplate rat : rapp.releasedApplicationTemplateList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(rat.cloudId);
                    List<Command> cmds = csl.getCommandListByTemplateId(rat.templateId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    rapp.status = ReleasedApplicationStatus.DELETED;
                    jdbcManager.update(rapp)
                        .includes(releasedApplication().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5016", new Object[]{ rapp.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * ApplicationInstanceの状態遷移（CREATING->DEACTIVE）を行います.
     * <p>
     * 対象:ApplicationInstance<br>
     * 状態遷移：CREATING->DEACTIVE
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws IllegalStateException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     */
    @NextTask("checkRebuildingApplicationInstance")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckCreatingApplicationInstance() throws IllegalStateException, SystemException, NotSupportedException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<ApplicationInstance> ais = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.CREATING))
            .getResultList();
        for (ApplicationInstance ai : ais) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(aivm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    ai.status = ApplicationInstanceStatus.DEACTIVE;
                    jdbcManager.update(ai)
                        .includes(applicationInstance().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5017", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * ApplicationInstanceの状態遷移（REBUILDING->DEACTIVE）を行います.
     * <p>
     * 対象:ApplicationInstance<br>
     * 状態遷移：REBUILDING->DEACTIVE
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("checkDeletingApplicationInstance")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckRebuildingApplicationInstance() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<ApplicationInstance> ais = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.REBUILDING))
            .getResultList();
        for (ApplicationInstance ai : ais) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(aivm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    ai.status = ApplicationInstanceStatus.DEACTIVE;
                    jdbcManager.update(ai)
                        .includes(applicationInstance().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5018", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * ApplicationInstanceの状態遷移（DELETING->DELETED）を行います.
     * <p>
     * 対象:ApplicationInstance<br>
     * 状態遷移：DELETING->DELETED
     * <p>
     * 条件：関連コマンドがすべて終了
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCheckDeletingApplicationInstance() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        List<ApplicationInstance> ais = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.DELETING))
            .getResultList();
        for (ApplicationInstance ai : ais) {
            userTransaction.begin();
            try {
                boolean isDone = true;
                for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    List<Command> cmds = csl.getCommandListByVmId(aivm.vmId);
                    if (cmds != null) {
                        if (cmds.size() > 0) {
                            isDone = false;
                            break;
                        }
                    }
                }
                if (isDone) {
                    ai.status = ApplicationInstanceStatus.DELETED;
                    jdbcManager.update(ai)
                        .includes(applicationInstance().status())
                        .execute();
                }
            } catch (Exception e) {
                logger.log("ESVCT5019", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * すべてのタスクメソッドが終了したら呼ばれます.
     */
    public void end() {
        if (exception != null) {
            logger.end(transactionId, "ESVCT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "ISVCT0012", new Object[]{});
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
            logger.end(transactionId, "ESVCT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "ESVCT0012", new Object[]{});
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
