/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.task;

import static jp.co.ntts.vhut.entity.Names.cloudUser;
import static jp.co.ntts.vhut.entity.Names.role;
import static jp.co.ntts.vhut.entity.Names.vhutUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import jp.co.ntts.vhut.command.SGetAllClustersCommand;
import jp.co.ntts.vhut.command.SGetAllDataStoragesCommand;
import jp.co.ntts.vhut.command.SGetAllUsersCommand;
import jp.co.ntts.vhut.command.SGetHostsByClusterIdCommand;
import jp.co.ntts.vhut.command.SGetTemplatesByClusterIdCommand;
import jp.co.ntts.vhut.command.SGetVmsByClusterIdCommand;
import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.csv.NetworkCsv;
import jp.co.ntts.vhut.entity.Cloud;
import jp.co.ntts.vhut.entity.CloudType;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.Role;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMap;
import jp.co.ntts.vhut.entity.VhutUserRoleMap;
import jp.co.ntts.vhut.exception.ConfigRuntimeException;
import jp.co.ntts.vhut.exception.InternalRuntimeException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.factory.CommandFactory;
import jp.co.ntts.vhut.logic.ICloudInfraLogic;
import jp.co.ntts.vhut.util.RhevScriptUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.chronos.core.annotation.task.Task;
import org.seasar.chronos.core.annotation.task.method.NextTask;
import org.seasar.chronos.core.annotation.trigger.NonDelayTrigger;
import org.seasar.config.core.container.ConfigContainer;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

/**
 * <p>サーバ起動時に初期設定タスクを実行します.
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
@Task
@NonDelayTrigger
public class InitializeTask {

    /**
     * ロガー.
     */
    private static VhutLogger logger = VhutLogger.getLogger(InitializeTask.class);

    /**
     * VHUT用のネットワーク作成スクリリプトのCSVデータファイル名
     */
    private static final String FILE_CREATE_NETWORK_CSV = "createVhutNetwork.csv";

    /**
     * クラウドID.
     */
    public long cloudId = 1;

    /**
     * サービスの設定.
     */
    public ServiceConfig serviceConfig;

    /**
     * クラウドの設定.
     */
    public CloudConfig cloudConfig;

    /**
     * クラウドロジックの作成クラス.
     */
    public CloudLogicFactory cloudLogicFactory;

    /**
     * CSVコントロールを生成するクラス
     */
    public S2CSVCtrlFactory csvCtrlFactory;

    /**
     * コマンドを生成するクラス
     */
    public CommandFactory commandFactory;

    /**
     * DBアクセスクラス.
     */
    public JdbcManager jdbcManager;

    /**
     * トランザクションIDタスクの識別子.
     */
    private String transactionId;

    /**
     * 各処理で発生した例外.
     */
    private Exception exception;


    /** 初期化処理. */
    @InitMethod
    public void init() {
        ConfigContainer configContainer = SingletonS2Container.getComponent(ConfigContainer.class);
        configContainer.loadToBeans();
    }

    /**
     * 開始処理.
     * @throws CancelException コンフィグでタスク実行が停止されています
     */
    @NextTask("InitializeBase")
    public void start() throws CancelException {
        if (!cloudConfig.isInitialize()) {
            logger.info("Initialize task is canceled.");
            throw new CancelException();
        }
        transactionId = VhutLogger.createTransactionId();
        logger.start(transactionId, "IINIT0011", new Object[]{});
    }

    /**
     * 設定値の初期化.
     */
    @NextTask("initializeUser")
    public void doInitializeBase() {
        try {
            //クラウドの設定
            createDefaultCloud();
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5011", e);
        }
    }

    /**
     * ユーザの初期化.
     */
    @NextTask("initializeCluster")
    public void doInitializeUser() {
        try {
            //ユーザの同期
            SGetAllUsersCommand userCommand = (SGetAllUsersCommand) commandFactory.newCommand(CommandOperation.GET_ALL_USERS, cloudId);
            if (userCommand.execute()
                .equals(CommandStatus.SUCCESS)) {
                userCommand.finish();
            }
            //デフォルトロールの作成
            createDefaultRole();
            //管理者ロールの作成
            createAdminRole();
            //管理者の作成
            createAdminVhutUser();
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5012", e);
        }
    }

    /**
     * クラスタの初期化.
     */
    @NextTask("initializeHost")
    public void doInitializeCluster() {
        try {
            //クラスタの同期
            SGetAllClustersCommand clusterCommand = (SGetAllClustersCommand) commandFactory.newCommand(CommandOperation.GET_ALL_CLUSTERS, cloudId);
            if (!clusterCommand.execute()
                .equals(CommandStatus.SUCCESS)) {
                throw new InternalRuntimeException("GET_ALL_CLUSTERS command was faild");
            }
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5013", e);
        }
    }

    /**
     * ホストの初期化.
     */
    @NextTask("initializeStorage")
    public void doInitializeHost() {
        try {
            Long clusterId = cloudConfig.getRhevClusterId();
            if (clusterId == null) {
                throw new ConfigRuntimeException(CloudConfig.class, "getRhevClusterId", "cluster not found");
            }
            //ホストの同期->クラスタリソースの更新
            SGetHostsByClusterIdCommand hostCommand = (SGetHostsByClusterIdCommand) commandFactory.newCommand(CommandOperation.GET_HOSTS_BY_CLUSTER_ID, cloudId);
            hostCommand.setParameter(clusterId);
            if (hostCommand.execute()
                .equals(CommandStatus.SUCCESS)) {
                hostCommand.finish();
            }
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5014", e);
        }
    }

    /**
     * ストレージの初期化.
     */
    @NextTask("initializeNetwork")
    public void doInitializeStorage() {
        try {
            //ストレージの同期->ストレージリソースの更新
            SGetAllDataStoragesCommand storageCommand = (SGetAllDataStoragesCommand) commandFactory.newCommand(CommandOperation.GET_ALL_DATA_STORAGES, cloudId);
            if (storageCommand.execute()
                .equals(CommandStatus.SUCCESS)) {
                storageCommand.finish();
            }
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5015", e);
        }
    }

    /**
     * ネットワークの初期化.
     */
    @NextTask("initializeTemplate")
    public void doInitializeNetwork() {
        try {
            Long clusterId = cloudConfig.getRhevClusterId();
            if (clusterId == null) {
                throw new ConfigRuntimeException(CloudConfig.class, "getRhevClusterId", "cluster not found");
            }
            ICloudInfraLogic csl = cloudLogicFactory.newCloudInfraLogic(cloudId);
            if (csl.isAnyNetworksIsNotAssigned()) {
                csl.clearNetworks();
                csl.initializeNetworks();
                csl.updateVlanResources();
                csl.updatePublicIpResources();
            }
            //ネットワークの同期->ネットワークリソースの更新
            //TODO: トランザクションのみなおし
            //            SGetNetworksByClusterIdCommand networkCommand = (SGetNetworksByClusterIdCommand) commandFactory.newCommand(CommandOperation.GET_NETWORKS_BY_CLUSTER_ID, cloudId);
            //            networkCommand.setParameter(clusterId);
            //            if (networkCommand.execute().equals(CommandStatus.SUCCESS)) {
            //                networkCommand.finish();
            //            }
            createRhevNetworkScript();
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5016", e);
        }
    }

    /**
     * テンプレートの初期化.
     */
    @NextTask("initializeVm")
    public void doInitializeTemplate() {
        try {
            Long clusterId = cloudConfig.getRhevClusterId();
            if (clusterId == null) {
                throw new ConfigRuntimeException(CloudConfig.class, "getRhevClusterId", "cluster not found");
            }
            SGetTemplatesByClusterIdCommand templateCommand = (SGetTemplatesByClusterIdCommand) commandFactory.newCommand(CommandOperation.GET_TEMPLATES_BY_CLUSTER_ID, cloudId);
            templateCommand.setParameter(clusterId);
            if (templateCommand.execute()
                .equals(CommandStatus.SUCCESS)) {
                templateCommand.finish();
            }
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5017", e);
        }
    }

    /**
     * VMの初期化.
     */
    public void doInitializeVm() {
        //存在しないVMを追加する処理が実現できたら有効化する
        try {
            Long clusterId = cloudConfig.getRhevClusterId();
            if (clusterId == null) {
                throw new ConfigRuntimeException(CloudConfig.class, "getRhevClusterId", "cluster not found");
            }
            SGetVmsByClusterIdCommand vmCommand = (SGetVmsByClusterIdCommand) commandFactory.newCommand(CommandOperation.GET_VMS_BY_CLUSTER_ID, cloudId);
            vmCommand.setParameter(clusterId);
            if (vmCommand.execute()
                .equals(CommandStatus.SUCCESS)) {
                vmCommand.finish();
            }
        } catch (RuntimeException e) {
            throw new InternalRuntimeException("EINIT5018", e);
        }
    }

    /**
     * RHEV用のネットワーク作成スクリプトをファイル出力する.
     */
    private void createRhevNetworkScript() {
        List<Network> networkList = jdbcManager.from(Network.class)
            .getResultList();
        //RHEV用のスクリプト作成
        List<NetworkCsv> csvData = RhevScriptUtil.createNetworkScript(networkList, cloudConfig.rhevCluster);
        //ファイル書き出し
        String fileName = getCreateNetworkCsvPath();
        Writer fileWriter;
        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            S2CSVWriteCtrl<NetworkCsv> csvWriter = csvCtrlFactory.getWriteController(NetworkCsv.class, fileWriter);
            csvWriter.writeAll(csvData);
            fileWriter.close();
        } catch (Exception e) {
            logger.log("WINIT5011", new Object[]{ fileName }, e);
        }
    }

    private String getRhevScriptDirectoryPath() {
        //        return cloudConfig.scriptRoot + File.separator + DIR_RHEV;
        return cloudConfig.scriptRoot;
    }

    private String getCreateNetworkCsvPath() {
        return getRhevScriptDirectoryPath() + File.separator + FILE_CREATE_NETWORK_CSV;
    }

    /**
     * [仮] デフォルトクラウドを作成(既にある場合は更新)します.
     */
    private void createDefaultCloud() {
        //既存の取得
        Cloud oldCloud = jdbcManager.from(Cloud.class)
            .id(1)
            .getSingleResult();
        if (oldCloud == null) {
            //挿入
            Cloud newCloud = new Cloud();
            newCloud.id = 1L;
            newCloud.name = CloudConfig.CLOUD_NAME_DEFAULT;
            newCloud.type = CloudType.RHEV;
            jdbcManager.insert(newCloud)
                .execute();
        } else {
            //更新
            oldCloud.name = CloudConfig.CLOUD_NAME_DEFAULT;
            oldCloud.type = CloudType.RHEV;
            jdbcManager.update(oldCloud)
                .execute();
        }
    }

    /**
     * 管理者ユーザ（ユーザ変更不可）を作成(既にある場合は更新)します.
     */
    private void createAdminVhutUser() {
        Long adminVhutUserId;
        //管理者ロールIDの取得
        if (serviceConfig.getAdminRoleId() == null) {
            throw new InternalRuntimeException("Admin Role is not defined");
        }
        //CloudUserの取得
        CloudUser adminCloudUser = jdbcManager.from(CloudUser.class)
            .where(new SimpleWhere().eq(cloudUser().account(), serviceConfig.adminAccount))
            .getSingleResult();
        if (adminCloudUser == null) {
            throw new InternalRuntimeException(String.format("Cloud User(account==%s) is not defined", serviceConfig.adminAccount));
        }
        //既存の取得
        VhutUser oldVhutUser = jdbcManager.from(VhutUser.class)
            .leftOuterJoin(vhutUser().vhutUserCloudUserMapList())
            .leftOuterJoin(vhutUser().vhutUserRoleMapList())
            .where(new SimpleWhere().eq(vhutUser().account(), serviceConfig.adminAccount)
                .eq(vhutUser().sysLock(), true))
            .eager(vhutUser().account())
            .getSingleResult();
        //VhutUser
        if (oldVhutUser == null) {
            //挿入
            VhutUser newVhutUser = new VhutUser();
            newVhutUser.account = serviceConfig.adminAccount;
            newVhutUser.email = adminCloudUser.email;
            newVhutUser.firstName = adminCloudUser.firstName;
            newVhutUser.lastName = adminCloudUser.lastName;
            newVhutUser.sysLock = true;
            jdbcManager.insert(newVhutUser)
                .execute();
            adminVhutUserId = newVhutUser.id;
        } else {
            //更新
            oldVhutUser.email = adminCloudUser.email;
            oldVhutUser.firstName = adminCloudUser.firstName;
            oldVhutUser.lastName = adminCloudUser.lastName;
            jdbcManager.update(oldVhutUser)
                .execute();
            adminVhutUserId = oldVhutUser.id;
        }
        //CloudUserとのマップ
        boolean isCloudUserAddigned = false;
        if (oldVhutUser != null) {
            for (VhutUserCloudUserMap map : oldVhutUser.vhutUserCloudUserMapList) {
                if (map.cloudUserId.equals(adminCloudUser.id)) {
                    isCloudUserAddigned = true;
                    break;
                }
            }
        }
        if (!isCloudUserAddigned) {
            VhutUserCloudUserMap map = new VhutUserCloudUserMap();
            map.cloudId = cloudId;
            map.cloudUserId = adminCloudUser.id;
            map.vhutUserId = adminVhutUserId;
            jdbcManager.insert(map)
                .execute();
        }
        //Roleとのマップ
        boolean isRoleAssigned = false;
        if (oldVhutUser != null) {
            for (VhutUserRoleMap map : oldVhutUser.vhutUserRoleMapList) {
                if (map.roleId.equals(serviceConfig.getAdminRoleId())) {
                    isRoleAssigned = true;
                    break;
                }
            }
        }
        if (!isRoleAssigned) {
            VhutUserRoleMap map = new VhutUserRoleMap();
            map.roleId = serviceConfig.getAdminRoleId();
            map.vhutUserId = adminVhutUserId;
            jdbcManager.insert(map)
                .execute();
        }
    }

    /**
     * 管理者ロール（ユーザ変更不可）を作成(既にある場合は更新)します.
     */
    private void createAdminRole() {
        byte[] adminRigts = new byte[]{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
        //既存の取得
        Role oldRole = jdbcManager.from(Role.class)
            .where(new SimpleWhere().eq(role().name(), serviceConfig.adminRoleName)
                .eq(role().sysLock(), true))
            .getSingleResult();
        if (oldRole == null) {
            //挿入
            Role newRole = new Role();
            newRole.name = serviceConfig.adminRoleName;
            newRole.sysLock = true;
            newRole.rights = adminRigts;
            newRole.isDefault = true;
            jdbcManager.insert(newRole)
                .execute();
        } else {
            long defaultCount = jdbcManager.from(Role.class)
                .where(new SimpleWhere().eq(role().isDefault(), true))
                .getCount();
            if (defaultCount == 0) {
                oldRole.isDefault = true;
            }
            oldRole.rights = adminRigts;
            jdbcManager.update(oldRole)
                .execute();
        }
    }

    /**
     * 初期ロールの作成
     */
    private void createDefaultRole() {
        byte[] adminRigts = new byte[]{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        //既存の取得
        Role oldRole = jdbcManager.from(Role.class)
            .where(new SimpleWhere().eq(role().name(), serviceConfig.guestRoleName)
                .eq(role().sysLock(), true))
            .getSingleResult();
        if (oldRole == null) {
            //挿入
            Role newRole = new Role();
            newRole.name = serviceConfig.guestRoleName;
            newRole.sysLock = true;
            newRole.rights = adminRigts;
            jdbcManager.insert(newRole)
                .execute();
        } else {
            oldRole.rights = adminRigts;
            jdbcManager.update(oldRole)
                .execute();
        }
    }

    /**
     * すべてのタスクメソッドが終了したら呼ばれます.
     */
    public void end() {
        if (exception != null) {
            logger.end(transactionId, "EINIT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "IINIT0012", new Object[]{});
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
            logger.end(transactionId, "EINIT0012", new Object[]{}, exception);
        } else {
            logger.end(transactionId, "EINIT0012", new Object[]{});
        }
    }


    /**
     * タスクが設定によりキャンセルされた時の検査例外.
     */
    class CancelException extends Exception {

        /**
         * シリアル.
         */
        private static final long serialVersionUID = 4773522854468862416L;
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
