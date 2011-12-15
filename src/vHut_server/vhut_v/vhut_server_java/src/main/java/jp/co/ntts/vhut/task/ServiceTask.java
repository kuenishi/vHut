/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.task;

import static jp.co.ntts.vhut.entity.Names.application;
import static jp.co.ntts.vhut.entity.Names.applicationInstance;
import static jp.co.ntts.vhut.entity.Names.applicationInstanceGroup;
import static jp.co.ntts.vhut.entity.Names.releasedApplication;
import static jp.co.ntts.vhut.entity.Names.term;
import static jp.co.ntts.vhut.entity.Names.vhutUserCloudUserMap;
import static org.seasar.extension.jdbc.operation.Operations.desc;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceStatus;
import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
import jp.co.ntts.vhut.entity.ApplicationInstanceVmSecurityGroupMap;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationStatus;
import jp.co.ntts.vhut.entity.ApplicationVm;
import jp.co.ntts.vhut.entity.ReleasedApplication;
import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
import jp.co.ntts.vhut.entity.ReleasedApplicationStatus;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateSecurityGroupMap;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Term;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMap;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.CloudUserNotMappedRuntimeException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.NoAvailableReservationException;
import jp.co.ntts.vhut.exception.NoReleasedApplicationRuntimeException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutLogger;
import jp.co.ntts.vhut.util.VhutUtil;

import org.seasar.chronos.core.TaskTrigger;
import org.seasar.chronos.core.annotation.task.Task;
import org.seasar.chronos.core.annotation.task.method.NextTask;
import org.seasar.config.core.container.ConfigContainer;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>サービス関連のバッチ処理を行うタスク.
 * <br>
 * <p>長い間隔で呼び出され以下の処理を実行する。
 * <ul>
 * <li>停止日時が過ぎているアプリケーションインスタンスVMを取得する。
 * <li>停止日時が過ぎているアプリケーションVMを取得する。
 * <li>クラウドロジックを使って上記に対応するVMを起動不可にする。
 * <li>データ削除日時が過ぎているアプリケーションインスタンスVMを取得する。
 * <li>クラウドロジックを使って上記に対応するVMを削除する。
 * <li>開始日時がせまっているアプリケーションインスタンスVMを取得する。
 * <li>クラウドロジックを使って上記に対応するVMを作成する。
 * <li>開始日時がせまっているアプリケーションインスタンスVMを取得する。
 * <li>開始日時がせまっているアプリケーションVMを取得する。
 * <li>クラウドロジックを使って上記に対応するVMを起動可能にする。
 * </ul>
 */
@Task
public class ServiceTask {

    /**
     * ロガー.
     */
    private static VhutLogger logger = VhutLogger.getLogger(ServiceTask.class);

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

    /**
     * 最新のReleasedApplicationを保管するCache
     */
    private Map<Long, ReleasedApplication> lastReleasedApplicationCache = new HashMap<Long, ReleasedApplication>();


    @InitMethod
    public void init() {
        ConfigContainer configContainer = SingletonS2Container.getComponent(ConfigContainer.class);
        configContainer.loadToBeans();
    }

    //    /**
    //     * 初期化処理.
    //     */
    //    public void initialize() {
    //        logger.debug("DSRVT1001");
    //    }

    /**
     * このタスクを呼び出すトリガを取得する.
     * Schedulerから呼び出されます。
     * @return 設定ファイルで定義したトリガー
     */
    public TaskTrigger getTrigger() {
        return serviceConfig.getServiceTaskTrigger();
    }

    /**
     * 開始処理.
     */
    @NextTask("deactivateApplicationInstance")
    public void start() {
        transactionId = VhutLogger.createTransactionId();
        currentTime = TimestampUtil.getCurrentTimestamp();
        logger.start(transactionId, "ISRVT0011", new Object[]{});
        clearLastReleasedApplicationCache();
    }

    /**
     * アプリケーションインスタンスの停止処理.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>ACTIVE -> DEACTIVE
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンからユーザを削除します。
     * <li>仮想マシンからNICを削除します。
     * <li>DHCPから仮想マシンのMACとPrivateIPの設定を削除します。
     * <li>NATから仮想マシンのPrivateIPとPublicIPの設定を削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#stopVm(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#removeVmUser(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#releaseNetwork(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationInstanceのstatusをDEACTIVEに変更します。
     * </ul>
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("deactivateApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doDeactivateApplicationInstance() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        //停止日時が過ぎているアプリケーションインスタンスVMを取得する。
        List<ApplicationInstance> ais = searchToStopAIs();
        for (ApplicationInstance ai : ais) {
            if (!ai.status.equals(ApplicationInstanceStatus.ACTIVE)) {
                continue;
            }
            userTransaction.begin();
            try {
                //VMを起動不可にする。
                for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    //クラウドロジックを使ってVMを停止させる。
                    csl.stopVm(aivm.vmId);
                    //クラウドロジックを使って上記に対応するVMを起動不可にする。
                    Long cloudUserId = getCloudUserId(ai.vhutUserId, aivm.cloudId);
                    csl.removeVmUser(aivm.vmId, cloudUserId);
                }
                //Networkを開放する。
                for (ApplicationInstanceSecurityGroup aisg : ai.applicationInstanceSecurityGroupList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aisg.cloudId);
                    csl.releaseNetwork(aisg.securityGroupId);
                }
                //アプリケーションインスタンスグループの状態変更
                ai.status = ApplicationInstanceStatus.DEACTIVE;
                jdbcManager.update(ai)
                    .includes(applicationInstance().status())
                    .execute();
            } catch (Exception e) {
                logger.log("ESRVT5011", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 停止日時が過ぎているアプリケーションインスタンスグループを取得します.
     * @return アプリケーションインスタンスグループのリスト
     */
    private List<ApplicationInstance> searchToStopAIs() {
        return jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.ACTIVE)
                .lt(applicationInstance().applicationInstanceGroup()
                    .endTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
            .getResultList();
    }

    /**
     * アプリケーションの停止処理.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>ACTIVE -> DEACTIVE
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンからユーザを削除します。
     * <li>仮想マシンからNICを削除します。
     * <li>DHCPから仮想マシンのMACとPrivateIPの設定を削除します。
     * <li>NATから仮想マシンのPrivateIPとPublicIPの設定を削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#stopVm(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#removeVmUser(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#releaseNetwork(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをDEACTIVEに変更します。
     * </ul>
     * @throws SystemException
     * @throws IllegalStateException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws NotSupportedException
     */
    @NextTask("deleteApplicationInstance")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doDeactivateApplication() throws IllegalStateException, SystemException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException, NotSupportedException {
        //停止日時が過ぎているアプリケーションインスタンスVMを取得する。
        List<Application> apps = searchToStopAs();
        for (Application app : apps) {
            if (!app.status.equals(ApplicationStatus.ACTIVE)) {
                continue;
            }
            userTransaction.begin();
            try {
                //VMを起動不可にする。
                for (ApplicationVm avm : app.applicationVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(avm.cloudId);
                    //クラウドロジックを使ってVMを停止させる。
                    csl.stopVm(avm.vmId);
                    //クラウドロジックを使って上記に対応するVMを起動不可にする。
                    Long cloudUserId = getCloudUserId(app.vhutUserId, avm.cloudId);
                    csl.removeVmUser(avm.vmId, cloudUserId);
                }
                //Networkを開放する。
                for (ApplicationSecurityGroup asg : app.applicationSecurityGroupList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(asg.cloudId);
                    csl.releaseNetwork(asg.securityGroupId);
                }
                //アプリケーションの状態変更
                app.status = ApplicationStatus.DEACTIVE;
                jdbcManager.update(app)
                    .includes(application().status())
                    .execute();
            } catch (Exception e) {
                logger.log("ESRVT5012", new Object[]{ app.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 停止日時が過ぎているアプリケーションを取得する。
     * @return アプリケーションのリスト
     */
    private List<Application> searchToStopAs() {
        List<Application> result = new ArrayList<Application>();
        //稼働中のアプリケーションを取得
        List<Application> apps = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .eager(application().vhutUserId())
            .where(new SimpleWhere().eq(application().status(), ApplicationStatus.ACTIVE))
            .getResultList();
        if (apps.size() == 0) {
            //なければ終了
            return result;
        }
        //有効な使用期間を取得
        List<Term> terms = jdbcManager.from(Term.class)
            .where(new SimpleWhere().lt(term().startTime(), currentTime)
                .gt(term().endTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
            .getResultList();
        //ホワイトリストを作成
        Map<Long, Boolean> white = new HashMap<Long, Boolean>();
        for (Term term : terms) {
            white.put(term.applicationId, true);
        }
        //ホワイトリストになければ結果に追加
        for (Application app : apps) {
            if (white.get(app.id) == null) {
                result.add(app);
            }
        }
        return result;
    }

    /**
     * アプリケーションインスタンスの削除処理.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> DELETING
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンのネットワークアダプタを削除します。
     * <li>セキュリティグループを削除します。
     * <li>仮想マシンを削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapter(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroup(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationInstanceのstatusをDELETINGに変更します。
     * </ul>
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @NextTask("createApplicationInstance")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doDeleteApplicationInstance() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        //停止日時が過ぎているアプリケーションインスタンスVMを取得する。
        List<ApplicationInstance> ais = searchToDeleteAIs();
        for (ApplicationInstance ai : ais) {
            userTransaction.begin();
            try {
                //NetworkAdapterとSecurityGroupの削除
                for (ApplicationInstanceSecurityGroup aisg : ai.applicationInstanceSecurityGroupList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aisg.cloudId);
                    for (ApplicationInstanceVmSecurityGroupMap map : aisg.applicationInstanceVmSecurityGroupMapList) {
                        csl.deleteNetworkAdapter(map.applicationInstanceVm.vmId, map.applicationInstanceSecurityGroup.securityGroupId);
                    }
                    csl.deleteSecurityGroup(aisg.securityGroupId);
                }
                //Vmの削除
                for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    csl.deleteVm(aivm.vmId);
                }
                //アプリケーションインスタンスグループの状態変更
                ai.status = ApplicationInstanceStatus.DELETING;
                jdbcManager.update(ai)
                    .includes(applicationInstance().status())
                    .execute();
            } catch (Exception e) {
                logger.log("ESRVT5013", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 削除日時が過ぎているアプリケーションインスタンスグループを取得します.
     * @return
     */
    private List<ApplicationInstance> searchToDeleteAIs() {
        return jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList()
                .applicationInstanceVmSecurityGroupMapList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList()
                .applicationInstanceVmSecurityGroupMapList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.DEACTIVE)
                .lt(applicationInstance().applicationInstanceGroup()
                    .deleteTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
            .getResultList();
    }

    /**
     * アプリケーションインスタンスの作成処理.
     * <p>
     * ApplicationInstanceの状態遷移：NONE->CREATING
     * 以下を実行
     * <ul>
     * <li>Vmのリストからネットワークを抽出
     * <li>SecurityGroupを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroup()}
     * <li>Vmを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createVm(long, Vm)}
     * <li>NetworkAdapterを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapter(long, long)}
     * </ul>
     * </p>
     * <p>
     * <b>ApplicationInstanceのIDが上記以外の場合</b>:受け付けない<br>
     * </p>
     * @throws SystemException
     * @throws IllegalStateException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     */
    //    @NextTask("activateApplicationInstance")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doCreateApplicationInstance() throws IllegalStateException, SystemException, NotSupportedException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        //停止日時が過ぎているアプリケーションインスタンスVMを取得する。
        //↑開始日時の間違いか要確認
        List<ApplicationInstance> ais = searchToCreateAIs();
        long cloudId = -1;

        for (ApplicationInstance ai : ais) {

            userTransaction.begin();
            try {
                ReleasedApplication rapp = getLastReleasedApplication(ai.applicationInstanceGroup.applicationId);
                if (rapp == null) {
                    throw new NoReleasedApplicationRuntimeException(ai.applicationInstanceGroup.applicationId, ai.id);
                    //NEXT ApplicationInstanceGroupをエラー状態にする必要がある。
                }

                ai.releasedApplicationId = rapp.id;

                //セキュリティグループの作成
                Map<Long, ApplicationInstanceSecurityGroup> sgMap = new HashMap<Long, ApplicationInstanceSecurityGroup>();
                //リリースドアプリケーションのセキュリティグループがnullの場合はロジックをスキップ
                if (rapp.releasedApplicationSecurityGroupTemplateList.size() != 0) {
                    for (ReleasedApplicationSecurityGroupTemplate rappsgt : rapp.releasedApplicationSecurityGroupTemplateList) {
                        //セキュリティグループを作成
                        ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(rappsgt.cloudId);
                        SecurityGroup sg = csl.createSecurityGroup();
                        //ApplicationInstanceSecurityGroupを作成
                        ApplicationInstanceSecurityGroup aisg = new ApplicationInstanceSecurityGroup();
                        aisg.applicationInstanceId = ai.id;
                        aisg.name = rappsgt.name;
                        aisg.cloudId = rappsgt.cloudId;
                        aisg.privateId = rappsgt.privateId;
                        aisg.securityGroupId = sg.id;
                        jdbcManager.insert(aisg)
                            .execute();
                        //Network作成のための布石
                        sgMap.put(rappsgt.id, aisg);
                    }
                }
                //Vmの作成
                Map<Long, ApplicationInstanceVm> vmMap = new HashMap<Long, ApplicationInstanceVm>();
                for (ReleasedApplicationTemplate rappt : rapp.releasedApplicationTemplateList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(rappt.cloudId);
                    Template template = csl.getTemplateById(rappt.templateId);
                    //VM作成
                    String name = VhutUtil.createServicePrefix(serviceConfig.applicationInstanceVmPrefix, rappt.privateId);
                    Vm vm = csl.createVm(ai.applicationInstanceGroup.reservationIdToCreate, template.id, template.specId, name, rappt.description);
                    //ApplicationInstanceVmの作成
                    ApplicationInstanceVm aivm = new ApplicationInstanceVm();
                    aivm.cloudId = rappt.cloudId;
                    aivm.name = rappt.name;
                    aivm.description = rappt.description;
                    aivm.cloudId = rappt.cloudId;
                    aivm.imageUrl = rappt.imageUrl;
                    aivm.privateId = rappt.privateId;
                    aivm.vmId = vm.id;
                    aivm.applicationInstanceId = ai.id;
                    jdbcManager.insert(aivm)
                        .execute();
                    //Network作成のための布石
                    vmMap.put(rappt.id, aivm);
                }

                //Network作成
                if (rapp.releasedApplicationSecurityGroupTemplateList.size() != 0) {
                    for (ReleasedApplicationSecurityGroupTemplate rasgt : rapp.releasedApplicationSecurityGroupTemplateList) {
                        for (ReleasedApplicationTemplateSecurityGroupMap raMap : rasgt.releasedApplicationTemplateSecurityGroupMapList) {
                            ApplicationInstanceVm aivm = vmMap.get(raMap.releasedApplicationTemplateId);
                            ApplicationInstanceSecurityGroup aisg = sgMap.get(raMap.releasedApplicationSecurityGroupTemplateId);

                            //cloudId決定
                            cloudId = -1;
                            if (aivm.cloudId.equals(aisg.cloudId)) {
                                cloudId = aivm.cloudId;
                            }
                            if (cloudId < 0) {
                                //コンフリクトした場合はMapの存在が悪い
                                throw new DBStateRuntimeException(ReleasedApplicationTemplateSecurityGroupMap.class, raMap.id);
                            }

                            //NetworkAdapter作成
                            ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(cloudId);
                            csl.createNetworkAdapter(aivm.vmId, aisg.securityGroupId);

                            //ApplicationInstanceVmSecurityGroupMapの作成
                            ApplicationInstanceVmSecurityGroupMap aiMap = new ApplicationInstanceVmSecurityGroupMap();
                            aiMap.applicationInstanceVmId = aivm.id;
                            aiMap.applicationInstanceSecurityGroupId = aisg.id;
                            jdbcManager.insert(aiMap)
                                .execute();

                        }
                    }
                }

                //状態を変更
                ai.status = ApplicationInstanceStatus.CREATING;
                jdbcManager.update(ai)
                    .includes(applicationInstance().status(), applicationInstance().releasedApplicationId())
                    .execute();
            } catch (Exception e) {
                logger.log("ESRVT5014", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }

        if (ais.size() != 0 && cloudId != -1) {
            // ランダムパスワード対応 # 
            ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(cloudId);

            // 研修IDリスト作成
            List<Long> aigIdList = new ArrayList<Long>();
            for (ApplicationInstance ai : ais) {
                if (!aigIdList.contains(ai.applicationInstanceGroupId)) {
                    aigIdList.add(ai.applicationInstanceGroupId);
                }
            }
            //研修毎にコマンドを発行
            for (Long aigId : aigIdList) {
                List<String> accountList = new ArrayList<String>();
                for (ApplicationInstance ai : ais) {
                    if (aigId == ai.applicationInstanceGroupId) {
                        VhutUser vhutUser = jdbcManager.from(VhutUser.class)
                            .id(ai.vhutUserId)
                            .getSingleResult();
                        accountList.add(vhutUser.account);
                    }
                }
                //Logicでコマンド発行 # 返り値はパスワード
                String result = csl.changeUsersPassword(accountList);

                //研修エンティティ取得
                ApplicationInstanceGroup aige = jdbcManager.from(ApplicationInstanceGroup.class)
                    .eager(applicationInstanceGroup().vhutUserId())
                    .eager(applicationInstanceGroup().applicationId())
                    .eager(applicationInstanceGroup().password())
                    .eager(applicationInstanceGroup().endTime())
                    .eager(applicationInstanceGroup().deleteTime())
                    .eager(applicationInstanceGroup().reservationIdToCreate())
                    .eager(applicationInstanceGroup().reservationIdToStart())
                    .id(aigId)
                    .getSingleResult();
                //パスワード変更
                aige.password = result;
                //Update
                jdbcManager.update(aige)
                    .execute();
            }
        }
    }

    /**
     * 作成日時が過ぎているアプリケーションインスタンスグループを取得します.
     * @return アプリケーションインスタンスグループのリスト
     */
    private List<ApplicationInstance> searchToCreateAIs() {
        return jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList()
                .applicationInstanceVmSecurityGroupMapList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList()
                .applicationInstanceVmSecurityGroupMapList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .eager(applicationInstance().applicationInstanceGroup()
                .applicationId())
            .eager(applicationInstance().applicationInstanceGroup()
                .reservationIdToCreate())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.NONE)
                .lt(applicationInstance().applicationInstanceGroup()
                    .startTime(), currentTime)
                .gt(applicationInstance().applicationInstanceGroup()
                    .endTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
            .getResultList();
    }

    /**
     * アプリケーションインスタンスの開始処理.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> ACTIVE
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>DHCPに仮想マシンのMACとPrivateIPを設定します。
     * <li>NATに仮想マシンのPrivateIPとPublicIPを設定します。
     * <li>仮想マシンにNICを追加します。
     * <li>仮想マシンにユーザを追加します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#obtainNetwork(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#addVmUser(long, long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationInstanceのstatusをACTIVEに変更します。
     * </ul>
     * @throws SystemException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    //    @NextTask("activateApplication")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doActivateApplicationInstance() throws NotSupportedException, SystemException, IllegalStateException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {
        //停止日時が過ぎているアプリケーションインスタンスVMを取得する。
        List<ApplicationInstance> ais = searchToStartAIs();
        for (ApplicationInstance ai : ais) {
            userTransaction.begin();
            try {
                //Networkを取得する。
                for (ApplicationInstanceSecurityGroup aisg : ai.applicationInstanceSecurityGroupList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aisg.cloudId);
                    csl.obtainNetwork(ai.applicationInstanceGroup.reservationIdToStart, aisg.securityGroupId, serviceConfig.getExIpRequestMode());
                }

                //VMを起動可能にする。
                for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    //クラウドロジックを使って上記に対応するVMを起動可能にする。
                    Long cloudUserId = getCloudUserId(ai.vhutUserId, aivm.cloudId);
                    csl.addVmUser(ai.applicationInstanceGroup.reservationIdToStart, aivm.vmId, cloudUserId);
                }

                //アプリケーションインスタンスグループの状態変更
                ai.status = ApplicationInstanceStatus.ACTIVE;
                jdbcManager.update(ai)
                    .includes(applicationInstance().status())
                    .execute();
            } catch (Exception e) {
                logger.log("ESRVT5015", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 開始日時が過ぎているアプリケーションインスタンスグループを取得します.
     * @return アプリケーションインスタンスグループのリスト
     */
    private List<ApplicationInstance> searchToStartAIs() {
        return jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .eager(applicationInstance().applicationInstanceGroup()
                .reservationIdToStart())
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.DEACTIVE)
                .lt(applicationInstance().applicationInstanceGroup()
                    .startTime(), currentTime)
                .gt(applicationInstance().applicationInstanceGroup()
                    .endTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
            .getResultList();
    }

    /**
     * アプリケーションの開始処理.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> ACTIVE
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>DHCPに仮想マシンのMACとPrivateIPを設定します。
     * <li>NATに仮想マシンのPrivateIPとPublicIPを設定します。
     * <li>仮想マシンにNICを追加します。
     * <li>仮想マシンにユーザを追加します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#obtainNetwork(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#addVmUser(long, long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをACTIVEに変更します。
     * </ul>
     * @throws SystemException
     * @throws IllegalStateException
     * @throws NotSupportedException
     * @throws RollbackException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws SecurityException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void doActivateApplication() throws IllegalStateException, SystemException, NotSupportedException, SecurityException, HeuristicMixedException, HeuristicRollbackException, RollbackException {

        //停止日時が過ぎているアプリケーションインスタンスVMを取得する。
        List<Application> ais = searchToStartAs();
        for (Application ai : ais) {
            userTransaction.begin();
            try {
                Term term = jdbcManager.from(Term.class)
                    .where(new SimpleWhere().eq(term().applicationId(), ai.id)
                        .lt(term().startTime(), currentTime)
                        .gt(term().endTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
                    .maxRows(1)
                    .getSingleResult();

                if (term == null) {
                    throw new NoAvailableReservationException(Application.class, ai.id, "activate");
                }

                //Networkを取得する。
                for (ApplicationSecurityGroup aisg : ai.applicationSecurityGroupList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aisg.cloudId);
                    csl.obtainNetwork(term.reservationId, aisg.securityGroupId, serviceConfig.getExIpRequestMode());
                }

                //VMを起動可能にする。
                for (ApplicationVm aivm : ai.applicationVmList) {
                    ICloudServiceLogic csl = cloudLogicFactory.newCloudServiceLogic(aivm.cloudId);
                    //クラウドロジックを使って上記に対応するVMを起動可能にする。
                    Long cloudUserId = getCloudUserId(ai.vhutUserId, aivm.cloudId);
                    csl.addVmUser(term.reservationId, aivm.vmId, cloudUserId);
                }

                //アプリケーションインスタンスグループの状態変更
                ai.status = ApplicationStatus.ACTIVE;
                jdbcManager.update(ai)
                    .includes(application().status())
                    .execute();
            } catch (Exception e) {
                logger.log("ESRVT5016", new Object[]{ ai.id }, e);
                userTransaction.rollback();
                continue;
            }
            userTransaction.commit();
        }
    }

    /**
     * 開始日時が過ぎているアプリケーションを取得します.
     * @return アプリケーションのリスト
     */
    private List<Application> searchToStartAs() {
        List<Application> result = new ArrayList<Application>();
        //稼働中のアプリケーションを取得
        List<Application> apps = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .eager(application().vhutUserId())
            .where(new SimpleWhere().eq(application().status(), ApplicationStatus.DEACTIVE))
            .getResultList();
        if (apps.size() == 0) {
            //なければ終了
            return result;
        }
        //有効な使用期間を取得
        List<Term> terms = jdbcManager.from(Term.class)
            .where(new SimpleWhere().lt(term().startTime(), currentTime)
                .gt(term().endTime(), TimestampUtil.subtract(currentTime, 1, TimestampUtil.Unit.DAY)))
            .getResultList();
        //ホワイトリストを作成
        Map<Long, Boolean> white = new HashMap<Long, Boolean>();
        for (Term term : terms) {
            white.put(term.applicationId, true);
        }
        //ホワイトリストにあれば結果に追加
        for (Application app : apps) {
            try {
                if (white.get(app.id)) {
                    result.add(app);
                }
            } catch (NullPointerException e) {
                continue;
            }
        }
        return result;
    }

    private Long getCloudUserId(Long vhutUserId, Long cloudId) {
        VhutUserCloudUserMap map = jdbcManager.from(VhutUserCloudUserMap.class)
            .where(new SimpleWhere().eq(vhutUserCloudUserMap().vhutUserId(), vhutUserId)
                .eq(vhutUserCloudUserMap().cloudId(), cloudId))
            .getSingleResult();
        if (map == null) {
            throw new CloudUserNotMappedRuntimeException(vhutUserId, cloudId);
        }
        return map.cloudUserId;
    }

    /**
     * 一番新しいリリースドアプリケーションを取得する。
     * @param applicationId
     * @return
     */
    private ReleasedApplication getLastReleasedApplication(Long applicationId) {
        ReleasedApplication rapp;
        try {
            rapp = lastReleasedApplicationCache.get(applicationId);
        } catch (Exception e) {
            rapp = null;
        }
        if (rapp == null) {
            rapp = jdbcManager.from(ReleasedApplication.class)
                .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
                .leftOuterJoin(releasedApplication().releasedApplicationTemplateList()
                    .releasedApplicationTemplateSecurityGroupMapList())
                .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList())
                .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList()
                    .releasedApplicationTemplateSecurityGroupMapList())
                .where(new SimpleWhere().eq(releasedApplication().applicationId(), applicationId)
                    .eq(releasedApplication().status(), ReleasedApplicationStatus.READY))
                .orderBy(desc("createdTime"))
                .getResultList()
                .get(0);
            if (rapp != null) {
                lastReleasedApplicationCache.put(applicationId, rapp);
            }
        }
        return rapp;
    }

    private void clearLastReleasedApplicationCache() {
        lastReleasedApplicationCache = new HashMap<Long, ReleasedApplication>();
    }

    /**
     * すべてのタスクメソッドが終了したら呼ばれます.
     */
    public void end() {
        if (exception != null) {
            logger.end(transactionId, "ESRVT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "ISRVT0012", new Object[]{});
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
            logger.end(transactionId, "ESRVT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "ESRVT0012", new Object[]{});
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
