/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.config.DriverConfig;
import jp.co.ntts.vhut.driver.rhev.RhevDataConverter;
import jp.co.ntts.vhut.driver.rhev.RhevDataValidator;
import jp.co.ntts.vhut.driver.rhev.RhevXmlRpcClient;
import jp.co.ntts.vhut.driver.rhev.dto.ResponseDto;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Cluster;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.Host;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.Storage;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.config.core.container.ConfigContainer;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>
 * RHEVドライバクラス。 <br>
 * <p>
 * サーバ(ドライバ)のメインとなる機能を実装する。
 * <p>
 * @version 1.0.0
 * @author NTT Software Corporation.
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
@Component(name = "rhevDriver")
public class RhevDriver implements IPrivateCloudDriver {
    // seasarのLoggerインスタンス取得
    private static final VhutLogger logger = VhutLogger.getLogger(RhevDriver.class);

    // ドライバーコンフィグのオブジェクト生成
    public DriverConfig driverConfig;

    public CloudConfig cloudConfig;

    // サーバ情報取得
    private String XMLRPC_SERVER_ADDRESS;
    private int XMLRPC_SERVER_PORT;

    // XMLRPC通信時に使用するドライバ側のメソッド名
    private String GET_ALL_CLUSTERS;
    private String GET_HOSTS_BY_CLUSTER_ID;
    private String GET_NETWORKS_BY_CLUSTER_ID;
    private String GET_ALL_DATA_STORAGES;
    private String GET_VMS_BY_CLUSTER_ID;
    private String GET_TEMPLATES_BY_CLUSTER_ID;
    private String GET_ALL_USERS;
    private String GET_TASKS_BY_TASKIDS;
    private String CREATE_VM;
    private String DELETE_VM;
    private String CHANGE_SPEC;
    private String ADD_NETWORK_ADAPTER;
    private String REMOVE_NETWORK_ADAPTER;
    private String ADD_DISK;
    private String REMOVE_DISK;
    private String ADD_USER;
    private String REMOVE_USER;
    private String START_VM;
    private String STOP_VM;
    private String SHUTDOWN_VM;
    private String CREATE_TEMPLATE;
    private String DELETE_TEMPLATE;
    private String CHANGE_USERS_PASSWORD;

    // タスクIDを格納
    private Long lastCommandId;

    // RhevXmlRpcClientクラスのインスタンスを格納
    protected RhevXmlRpcClient client;

    // RhevDataValidatorクラスのインスタンスを格納
    protected RhevDataConverter converter;

    /**
     * クラウドID(初期値:1)
     */
    private long cloudId = 1;


    /**
     * RhevDriverクラスのコンストラクタ。
     */
    public RhevDriver() {
    }

    @InitMethod
    public void init() throws InternalDriverRuntimeException {

        ConfigContainer configContainer = SingletonS2Container.getComponent(ConfigContainer.class);
        configContainer.loadToBeans();

        XMLRPC_SERVER_ADDRESS = driverConfig.xmlRpcServerAddress;
        XMLRPC_SERVER_PORT = Integer.parseInt(driverConfig.xmlRpcServerPort);

        GET_ALL_CLUSTERS = driverConfig.get_all_clusters;
        GET_HOSTS_BY_CLUSTER_ID = driverConfig.get_hosts_by_cluster_id;
        GET_NETWORKS_BY_CLUSTER_ID = driverConfig.get_networks_by_cluster_id;
        GET_ALL_DATA_STORAGES = driverConfig.get_all_data_storages;
        GET_VMS_BY_CLUSTER_ID = driverConfig.get_vms_by_cluster_id;
        GET_TEMPLATES_BY_CLUSTER_ID = driverConfig.get_templates_by_cluster_id;
        GET_ALL_USERS = driverConfig.get_all_users;
        GET_TASKS_BY_TASKIDS = driverConfig.get_tasks_by_task_ids;
        CREATE_VM = driverConfig.create_vm;
        DELETE_VM = driverConfig.delete_vm;
        CHANGE_SPEC = driverConfig.change_spec;
        ADD_NETWORK_ADAPTER = driverConfig.add_network_adapter;
        REMOVE_NETWORK_ADAPTER = driverConfig.remove_network_adapter;
        ADD_DISK = driverConfig.add_disk;
        REMOVE_DISK = driverConfig.remove_disk;
        ADD_USER = driverConfig.add_user;
        REMOVE_USER = driverConfig.remove_user;
        START_VM = driverConfig.start_vm;
        STOP_VM = driverConfig.stop_vm;
        SHUTDOWN_VM = driverConfig.shutdown_vm;
        CREATE_TEMPLATE = driverConfig.create_template;
        DELETE_TEMPLATE = driverConfig.delete_template;
        CHANGE_USERS_PASSWORD = driverConfig.change_users_password;

        try {
            client = new RhevXmlRpcClient(XMLRPC_SERVER_ADDRESS, XMLRPC_SERVER_PORT);
        } catch (MalformedURLException e) {
            // XmlRpcライブラリが返すサーバURL不正例外をキャッチ。
            // ログ出力はクラウド側行う。
            throw InternalDriverRuntimeException.newException(e);
        }

    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.driver.IDriver#getCloudId()
     */
    @Override
    public long getCloudId() {
        return cloudId;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.driver.IDriver#setCloudId(long)
     */
    @Override
    public void setCloudId(long cloudId) {
        this.cloudId = cloudId;
        converter = new RhevDataConverter(cloudId);

    }

    /**
     * lastCommandIdのゲッター。
     *
     * @return lastCommandId lastCommandIdの値
     */
    public Long getLastCommandId() {
        return lastCommandId;
    }

    /**
     * lastCommandIdのセッター。
     *
     * @param value lastCommandIdにセットする値
     */
    public void setLastCommandId(Long value) {
        lastCommandId = value;
    }

    /**
     * クラスタ一覧取得。
     *
     * @param
     * @return clusterList クラスタのリスト
     */
    @Override
    public List<Cluster> getAllClusters() {

        List<Cluster> clusterListData = new ArrayList<Cluster>();
        Object[] parameter = new Object[]{};

        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_ALL_CLUSTERS, parameter);

        // エージェントから受信したコマンドIDをvHut共通データに変換
        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateClusterList(response.result);
            // パーサークラスを呼び出してvHut共通データに変換
            clusterListData = converter.convertClusterListRh2Vh(response.result);
        }
        return clusterListData;
    }

    /**
     * ホスト一覧取得。
     *
     * @param clusterId
     *            クラスタID
     * @return hostList ホストのリスト
     */
    @Override
    public List<Host> getHostsByClusterId(long clusterId) {

        List<Host> hostListData = new ArrayList<Host>();
        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertClusterIdVh2Rh(clusterId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_HOSTS_BY_CLUSTER_ID, parameter);

        // エージェントから受信したコマンドIDをvHut共通データに変換
        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateHostList(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            hostListData = converter.convertHostListRh2Vh(response.result);
        }
        return hostListData;
    }

    /**
     * ネットワーク一覧取得。
     *
     * @param clusterId
     *            クラスタID
     * @return networkList ネットワークのリスト
     * @throws ValidationRuntimeException
     */
    @Override
    public List<Network> getNetworksByClusterId(long clusterId) {

        List<Network> networkListData = new ArrayList<Network>();
        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertClusterIdVh2Rh(clusterId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_NETWORKS_BY_CLUSTER_ID, parameter);

        if (response != null) {
            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateNetworkList(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            networkListData = converter.convertNetworkListRh2Vh(response.result);
        }
        return networkListData;
    }

    /**
     * ストレージ一覧取得。
     *
     * @param なし
     * @return storageList ストレージのリスト
     * @throws ValidationRuntimeException
     */
    @Override
    public List<Storage> getAllDataStorages() {

        List<Storage> storageListData = new ArrayList<Storage>();

        Object[] parameter = new Object[]{};
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_ALL_DATA_STORAGES, parameter);

        // エージェントから受信したコマンドIDをvHut共通データに変換
        if (response != null) {
            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateStorageList(response.result);

            // コンバータクラスを呼び出してvHut共通データに変換
            storageListData = converter.convertStorageListRh2Vh(response.result);
        }
        return storageListData;
    }

    /**
     * 仮想マシン一覧取得。
     *
     * @param clusterId
     *            クラスタID
     * @return vmList 仮想マシンのリスト
     * @throws ValidationRuntimeException
     */
    @Override
    public List<Vm> getVmsByClusterId(long clusterId) {

        List<Vm> vmListData = new ArrayList<Vm>();
        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertClusterIdVh2Rh(clusterId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_VMS_BY_CLUSTER_ID, parameter);

        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateVmList(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            vmListData = converter.convertVmListRh2Vh(response.result);
        }
        return vmListData;
    }

    /**
     * テンプレート一覧取得。
     *
     * @param clusterId
     *            クラスタID
     * @return templateList テンプレートのリスト
     * @throws ValidationRuntimeException
     */
    @Override
    public List<Template> getTemplatesByClusterId(long clusterId) {

        List<Template> templateListData = new ArrayList<Template>();
        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertClusterIdVh2Rh(clusterId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_TEMPLATES_BY_CLUSTER_ID, parameter);

        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateTemplateList(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            templateListData = converter.convertTemplateListRh2Vh(response.result);
        }
        return templateListData;
    }

    /**
     * ユーザ一覧取得。
     *
     * @return vhutUserList ユーザのリスト
     * @throws ValidationRuntimeException
     */
    @Override
    public List<CloudUser> getAllUsers() {

        List<CloudUser> cloudUserListData = new ArrayList<CloudUser>();

        Object[] parameter = new Object[]{};

        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_ALL_USERS, parameter);

        // エージェントから受信したデータのバリデーションを行う
        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateCloudUserList(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            cloudUserListData = converter.convertCloudUserListRh2Vh(response.result);
        }
        return cloudUserListData;
    }

    /**
     * コマンド一覧更新。
     *
     * @param commandList
     *            コマンドのリスト
     * @return commandList コマンドのリスト
     * @throws ValidationRuntimeException
     */
    @Override
    public List<Command> updateCommandList(List<Command> commandList) {

        List<Command> commandListData = new ArrayList<Command>();
        Object[] rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertCommandListVh2Rh(commandList);

        Object[] parameter = new Object[]{ rhevData };
        String i;
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(GET_TASKS_BY_TASKIDS, parameter);

        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateCommandList(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            commandListData = converter.convertCommandListRh2Vh(response.result, commandList);
        }
        return commandListData;
    }

    /**
     * 仮想マシン作成。
     *
     * @param vm
     *            仮想マシン
     * @param commandId
     *            コマンドID
     * @return vm 仮想マシン
     * @throws ValidationRuntimeException
     */
    @Override
    public Vm createVmAsync(Vm vm, long commandId) {

        Vm vmData = new Vm();
        Object[] rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertVmVh2Rh(vm);
        //        converter.convertCommandIdVh2Rh(commandId);

        // XMLRPCでエージェントと通信
        Object[] parameter = new Object[]{ rhevData[0], rhevData[1], rhevData[2], rhevData[3], rhevData[4], rhevData[5], rhevData[6], };
        ResponseDto response = client.getResponse(CREATE_VM, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {
            lastCommandId = converter.bindCommandId(commandId, response.commandId);

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateVm(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            vmData = converter.convertVmRh2Vh(response.result, vm);
        }
        return vmData;
    }

    /**
     * 仮想マシン削除。
     *
     * @param vmId
     *            仮想マシンID
     * @param commandId
     *            コマンドID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void deleteVmAsync(long vmId, long commandId) {

        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertVmIdVh2Rh(vmId);

        // XMLRPCでエージェントと通信
        Object[] parameter = new Object[]{ rhevData };
        ResponseDto response = client.getResponse(DELETE_VM, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {
            lastCommandId = converter.bindCommandId(commandId, response.commandId);
        }
    }

    /**
     * 仮想マシンスペック変更。
     *
     * @param vmId
     *            仮想マシンID
     * @param cpuCore
     *            CPUコア数
     * @param memory
     *            メモリ容量
     * @return vm 仮想マシン
     * @throws ValidationRuntimeException
     */
    @Override
    public Vm changeSpec(long vmId, int cpuCore, int memory) {

        Vm vmData = new Vm();
        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        // vmIdのみ変換、cpuCoreとmemoryは変換無し
        rhevData = converter.convertVmIdVh2Rh(vmId);

        Object[] parameter = new Object[]{ rhevData, cpuCore, memory };

        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(CHANGE_SPEC, parameter);

        if (response != null) {
            // エージェントから受信したcommandIdをvHut共通データに変換
            //            lastCommandId = converter.convertCommandIdRh2Vh(response.commandId);

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateVm(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            vmData = converter.convertVmRh2Vh(response.result, vmId);
        }
        return vmData;
    }

    /**
     * 仮想マシンNIC追加。
     *
     * @param networkAdapter
     *            ネットワークアダプタ
     * @return networkAdapter ネットワークアダプタ
     * @throws ValidationRuntimeException
     */
    @Override
    public NetworkAdapter addNetworkAdapter(NetworkAdapter networkAdapter) {

        NetworkAdapter networkAdapterData = new NetworkAdapter();
        Object[] rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertNetworkAdapterVh2Rh(networkAdapter);

        Object[] parameter = new Object[]{ rhevData[0], rhevData[1], rhevData[2], rhevData[3] };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(ADD_NETWORK_ADAPTER, parameter);
        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateNetworkAdapter(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            networkAdapterData = converter.convertNetworkAdapterRh2Vh(response.result, networkAdapter.id);
        }
        return networkAdapterData;
    }

    /**
     * 仮想マシンNIC削除。
     *
     * @param vmId
     *            仮想マシンID
     * @param networkAdapterId
     *            ネットワークアダプタID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void removeNetworkAdapter(long vmId, long networkAdapterId) {

        String rhevData1 = null;
        String rhevData2 = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData1 = converter.convertVmIdVh2Rh(vmId);
        rhevData2 = converter.convertNetworkAdapterIdVh2Rh(networkAdapterId);

        // XMLRPCでエージェントと通信
        Object[] parameter = new Object[]{ rhevData1, rhevData2 };

        client.getResponse(REMOVE_NETWORK_ADAPTER, parameter);
    }

    /**
     * 仮想マシンディスク追加。
     *
     * @param disk
     *            ディスク
     * @param commandId
     *            コマンドID
     * @return disk ディスク
     * @throws ValidationRuntimeException
     */
    @Override
    public Disk addDiskAsync(Disk disk, long commandId) {

        Disk diskData = new Disk();
        Object[] rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertDiskVh2Rh(disk);
        Object[] parameter = new Object[]{ rhevData[0], rhevData[1], rhevData[2] };

        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(ADD_DISK, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {
            lastCommandId = converter.bindCommandId(commandId, response.commandId);

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateDisk(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            diskData = converter.convertDiskRh2Vh(response.result, disk.id);
        }
        return diskData;
    }

    /**
     * 仮想マシンディスク削除。
     *
     * @param vmId
     *            仮想マシンID
     * @param diskId
     *            ディスクID
     * @param commandId
     *            コマンドID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void removeDiskAsync(long vmId, long diskId, long commandId) {

        String rhevData1 = null;
        String rhevData2 = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData1 = converter.convertVmIdVh2Rh(vmId);
        rhevData2 = converter.convertDiskIdVh2Rh(diskId);

        Object[] parameter = new Object[]{ rhevData1, rhevData2 };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(REMOVE_DISK, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {
            lastCommandId = converter.bindCommandId(commandId, response.commandId);
        }

    }

    /**
     * 仮想マシンユーザ追加。
     *
     * @param vmId
     *            仮想マシンID
     * @param cloudUserId
     *            ユーザID
     * @return vhutUser ユーザ
     * @throws ValidationRuntimeException
     */
    @Override
    public CloudUser addUser(long vmId, long cloudUserId) {

        CloudUser cloudUserData = new CloudUser();

        String rhevData1 = null;
        String rhevData2 = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData1 = converter.convertVmIdVh2Rh(vmId);
        rhevData2 = converter.convertUserIdVh2Rh(cloudUserId);

        Object[] parameter = new Object[]{ rhevData1, rhevData2 };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(ADD_USER, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {

            // エージェントから受信したデータのバリデーションを行う
            RhevDataValidator.validateVhutUser(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            cloudUserData = converter.convertCloudUserRh2Vh(response.result, cloudUserId);
        }
        return cloudUserData;
    }

    /**
     * 仮想マシンユーザ削除。
     *
     * @param vmId
     *            仮想マシンID
     * @param vhutUserId
     *            ユーザID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void removeUser(long vmId, long vhutUserId) {

        String rhevData1 = null;
        String rhevData2 = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData1 = converter.convertVmIdVh2Rh(vmId);
        rhevData2 = converter.convertUserIdVh2Rh(vhutUserId);

        Object[] parameter = new Object[]{ rhevData1, rhevData2 };
        // XMLRPCでエージェントと通信
        client.getResponse(REMOVE_USER, parameter);

    }

    /**
     * 仮想マシン起動。
     *
     * @param vmId
     *            仮想マシンID
     * @param commandId
     *            コマンドID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void startVmAsync(long vmId, long commandId) {

        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertVmIdVh2Rh(vmId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(START_VM, parameter);

    }

    /**
     * 仮想マシン停止。
     *
     * @param vmId
     *            仮想マシンID
     * @param commandId
     *            コマンドID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void stopVmAsync(long vmId, long commandId) {

        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertVmIdVh2Rh(vmId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(STOP_VM, parameter);

    }

    /**
     * 仮想マシンシャットダウン。
     *
     * @param vmId
     *            仮想マシンID
     * @param commandId
     *            コマンドID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void shutdownVmAsync(long vmId, long commandId) {

        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertVmIdVh2Rh(vmId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(SHUTDOWN_VM, parameter);

    }

    /**
     * テンプレート作成。
     *
     * @param template
     *            テンプレート
     * @param vmId
     *            仮想マシンID
     * @param commandId
     *            コマンドID
     * @return template テンプレート
     * @throws ValidationRuntimeException
     */
    @Override
    public Template createTemplateAsync(Template template, long vmId, long commandId) {

        Template templateData = new Template();
        //        Object[] para = null;
        Object[] rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertTemplateVh2Rh(template, vmId);

        Object[] parameter = new Object[]{ rhevData[0], rhevData[1], rhevData[2], rhevData[3], rhevData[4] };

        // RHEV依存データに変換後の引数templateとvmIdを同じ配列に格納

        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(CREATE_TEMPLATE, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {
            lastCommandId = converter.bindCommandId(commandId, response.commandId);

            // エージェントから受信したtemplateのバリデート
            RhevDataValidator.validateTemplate(response.result);

            // パーサークラスを呼び出してvHut共通データに変換
            templateData = converter.convertTemplateRh2Vh(response.result, template);
        }
        return templateData;
    }

    /**
     * テンプレート削除。
     *
     * @param templateId
     *            テンプレートID
     * @param commandId
     *            コマンドID
     * @return
     * @throws ValidationRuntimeException
     */
    @Override
    public void deleteTemplateAsync(long templateId, long commandId) {

        String rhevData = null;

        // パーサークラスを呼び出してRHEV依存データに変換
        rhevData = converter.convertTemplateIdVh2Rh(templateId);

        Object[] parameter = new Object[]{ rhevData };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(DELETE_TEMPLATE, parameter);

        // エージェントから受信したデータをvHut共通データに変換
        if (response != null) {
            RhevDataValidator.validateCommandId(response.commandId);
            lastCommandId = converter.bindCommandId(commandId, response.commandId);
        }
    }

    /**
     * ユーザパスワード変更。
     *
     * @param accountList
     *            アカウントのリスト
     * @param password
     *            パスワード
     * @return
     */
    @Override
    public void changeUsersPassword(List<String> accountList, String password) {

        //        Object[] rhevData = null;

        //        // パーサークラスを呼び出してRHEV依存データに変換
        //        rhevData = converter.convertUserIdListVh2Rh(accountList);

        Object[] parameter = new Object[]{ accountList.toArray(), password };
        // XMLRPCでエージェントと通信
        ResponseDto response = client.getResponse(CHANGE_USERS_PASSWORD, parameter);
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
