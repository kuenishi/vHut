/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev;

import java.util.ArrayList;
import java.util.List;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.driver.ConversionRuntimeException;
import jp.co.ntts.vhut.driver.DriverSQLRuntimeException;
import jp.co.ntts.vhut.driver.ValidationRuntimeException;
import jp.co.ntts.vhut.driver.rhev.dto.CloudUserDto;
import jp.co.ntts.vhut.driver.rhev.dto.ClusterDto;
import jp.co.ntts.vhut.driver.rhev.dto.CommandDto;
import jp.co.ntts.vhut.driver.rhev.dto.DiskDto;
import jp.co.ntts.vhut.driver.rhev.dto.DiskTemplateDto;
import jp.co.ntts.vhut.driver.rhev.dto.HostDto;
import jp.co.ntts.vhut.driver.rhev.dto.NetworkAdapterDto;
import jp.co.ntts.vhut.driver.rhev.dto.NetworkAdapterTemplateDto;
import jp.co.ntts.vhut.driver.rhev.dto.NetworkDto;
import jp.co.ntts.vhut.driver.rhev.dto.StorageDto;
import jp.co.ntts.vhut.driver.rhev.dto.TemplateDto;
import jp.co.ntts.vhut.driver.rhev.dto.VmDto;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Cluster;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.DiskTemplate;
import jp.co.ntts.vhut.entity.Host;
import jp.co.ntts.vhut.entity.HostStatus;
import jp.co.ntts.vhut.entity.LocalId;
import jp.co.ntts.vhut.entity.LocalIdType;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplate;
import jp.co.ntts.vhut.entity.NetworkStatus;
import jp.co.ntts.vhut.entity.Os;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.Storage;
import jp.co.ntts.vhut.entity.StorageStatus;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.TemplateStatus;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.entity.VmStatus;
import jp.co.ntts.vhut.util.Ipv4ConversionUtil;
import jp.co.ntts.vhut.util.MacConversionUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.SingletonS2Container;

// 仮想マシンの状態一覧
enum RhevVmStatus {
    Unassigned, Down, Up, PoweringUp, PoweredDown, Paused, MigratingFrom, MigratingTo, Unknown, NotResponding, WaitForLaunch, RebootInProgress, SavingState, RestoringState, Suspended, ImageIllegal, ImageLocked, PoweringDown
}

// テンプレートの状態一覧
enum RhevTemplateStatus {
    OK, Locked, Illegal
}

// ホストの状態一覧
enum RhevHostStatus {
    Unassigned, Down, Maintenance, Up, NonResponsive, Error, Installing, InstallFailed, Reboot, PreparingForMaintenance, NonOperational, PendingApproval, Initializing, Problematic
}

// ネットワークの状態一覧
enum RhevNetworkStatus {
    Operational, NonOperational
}

// ストレージの状態一覧
enum RhevStorageStatus {
    Unknown, Uninitialized, Unattached, Active, InActive, Locked
}

// コマンドの状態一覧
// APIに記述なしのためAgentで設計する
enum RhevCommandStatus {
    running, finished, Unusual
}

// OS一覧
enum RhevOs {
    Unassigned, RHEL5, RHEL5x64, RHEL4, RHEL4x64, RHEL3, RHEL3x64, OtherLinux, WindowsXP, Windows2003, Windows2003x64, Windows7, Windows7x64, Windows2008, Windows2008x64, Windows2008R2x64, Other
}

/**
 * <p>
 * RhevおよびvHut間のデータ変換を行うクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class RhevDataConverter {

    /**
     * ロガー.
     */
    private static VhutLogger logger = VhutLogger.getLogger(RhevDataConverter.class);

    protected JdbcManager jdbcManager = SingletonS2Container.getComponent(JdbcManager.class);

    protected CloudConfig cloudConfig = SingletonS2Container.getComponent(CloudConfig.class);

    protected long cloudId;


    /**
     * コンストラクタ.
     * @param cloudId クラウドID
     */
    public RhevDataConverter(long cloudId) {
        this.cloudId = cloudId;
    }

    /**
     * クラウドIDを返します.
     * @return クラウドID
     */
    public long getCloudId() {
        return cloudId;
    }

    /**
     * クラウドIDを設定します.
     * @param cloudId クラウドID
     */
    public void setCloudId(long cloudId) {
        this.cloudId = cloudId;

    }

    /**
     * Rhev固有データClusterListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return clusterList クラスタのリスト
     * @throws ConversionRuntimeException
     */
    public List<Cluster> convertClusterListRh2Vh(Object data) throws ConversionRuntimeException {

        List<Cluster> clusterList = new ArrayList<Cluster>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                ClusterDto dto = (ClusterDto) value;
                Cluster cluster = new Cluster();
                cluster.name = dto.name;
                try {
                    cluster.id = convertClusterIdRh2Vh(dto.clusterId);

                } catch (DriverSQLRuntimeException e) {
                    // cloudUserテーブルに新規挿入
                    cluster.cloudId = cloudId;
                    jdbcManager.insert(cluster)
                        .execute();
                    // LocalIdテーブルに追加
                    bindClusterId(cluster.id, dto.clusterId);
                }
                clusterList.add(cluster);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return clusterList;

    }

    /**
     * Rhev固有データhostListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return hostList ホストのリスト
     */
    public List<Host> convertHostListRh2Vh(Object data, Long globalId) {

        List<Host> hostList = new ArrayList<Host>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                HostDto dto = (HostDto) value;
                Host host = new Host();

                host.name = dto.name;
                host.status = convertHostStatusRh2Vh(dto.status);
                host.cpuCore = (int) dto.cpuCore;
                host.memory = dto.memory;

                if (globalId == null) {
                    host.clusterId = convertClusterIdRh2Vh(dto.clusterId);

                    try {
                        // LocalIdテーブルに該当レコードが無かった場合レコードを追加
                        host.id = convertHostIdRh2Vh(dto.hostId);
                    } catch (DriverSQLRuntimeException e) {
                        // hostテーブルに新規挿入
                        host.cloudId = cloudId;
                        jdbcManager.insert(host)
                            .execute();
                        // LocalIdテーブルに追加
                        bindHostId(host.id, dto.hostId);
                    }

                } else {
                    host.id = bindHostId(globalId, dto.hostId);
                    host.clusterId = bindClusterId(globalId, dto.clusterId);
                }

                hostList.add(host);
            }

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException();
        }

        return hostList;

    }

    /**
     * Rhev固有データhostListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return hostList ホストのリスト
     */
    public List<Host> convertHostListRh2Vh(Object data) {
        return convertHostListRh2Vh(data, null);
    }

    /**
     * Rhev固有データnetworkListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return networkList
     * @throws ConversionRuntimeException
     */
    public List<Network> convertNetworkListRh2Vh(Object data, Long globalId) {

        List<Network> networkList = new ArrayList<Network>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                NetworkDto dto = (NetworkDto) value;
                Network network = new Network();
                try {
                    //                    if (globalId == null) {
                    network.id = convertNetworkIdRh2Vh(dto.networkId);
                    //                    } else {
                    //                        network.id = bindNetworkId(globalId, dto.networkId);
                    //                    }
                    network.name = dto.networkId;
                    network.vlan = (short) dto.vlan;
                    network.networkAddress = Ipv4ConversionUtil.convertDotToHex(dto.networkAddress);
                    network.mask = Ipv4ConversionUtil.convertDotToHex(dto.subnet);
                    network.gateway = Ipv4ConversionUtil.convertDotToHex(dto.gateway);
                    network.dns = network.gateway;
                    network.status = convertNetworkStatusRh2Vh(dto.status);

                    networkList.add(network);
                } catch (DriverSQLRuntimeException e) {
                    logger.log("DDRIV5013", new Object[]{ dto.networkId });
                } catch (Exception e) {
                    logger.log("EDRIV5013", new Object[]{ dto.networkId }, e);
                }
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return networkList;

    }

    /**
     * Rhev固有データnetworkListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return networkList
     * @throws ConversionRuntimeException
     */
    public List<Network> convertNetworkListRh2Vh(Object data) {
        return convertNetworkListRh2Vh(data, null);
    }

    /**
     * Rhev固有データstorageListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return storageList ストレージのリスト
     * @throws ConversionRuntimeException
     */
    public List<Storage> convertStorageListRh2Vh(Object data) {
        List<Storage> storageList = new ArrayList<Storage>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                StorageDto dto = (StorageDto) value;
                Storage storage = new Storage();
                storage.name = dto.name;
                storage.status = convertStorageStatusRh2Vh(dto.status);
                storage.availableSize = (int) dto.availableSize;
                storage.commitedSize = (int) dto.commitedSize;
                storage.physicalSize = (int) dto.availableSize + (int) dto.usedSize;
                try {
                    storage.id = convertStorageIdRh2Vh(dto.storageId);
                } catch (DriverSQLRuntimeException e) {
                    // storageテーブルに新規挿入
                    storage.cloudId = cloudId;
                    jdbcManager.insert(storage)
                        .execute();
                    // LocalIdテーブルに追加
                    bindStorageId(storage.id, dto.storageId);
                }
                storageList.add(storage);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return storageList;

    }

    /**
     * Rhev固有データvmListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return vmList
     * @throws ConversionRuntimeException
     */
    public List<Vm> convertVmListRh2Vh(Object data, Long globalId) {
        List<Vm> vmList = new ArrayList<Vm>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                VmDto dto = (VmDto) value;
                Vm vm = new Vm();
                vm.name = dto.name;
                vm.templateId = convertTemplateIdRh2Vh(dto.templateId);
                //                vm.hostId = convertHostIdRh2Vh(dto.hostId);
                vm.clusterId = convertClusterIdRh2Vh(dto.clusterId);
                vm.description = dto.description;
                vm.cpuCore = dto.cpuCore;
                vm.memory = dto.memory;
                vm.cpuUsage = dto.cpuUsage;
                vm.memoryUsage = dto.memoryUsage;
                vm.status = convertVmStatusRh2Vh(dto.status);
                vm.os = convertOsRh2Vh(dto.os);
                try {
                    vm.id = convertVmIdRh2Vh(dto.vmId);
                } catch (DriverSQLRuntimeException e) {
                    // vmテーブルに新規挿入
                    vm.cloudId = cloudId;
                    jdbcManager.insert(vm)
                        .execute();
                    // LocalIdテーブルに追加
                    bindVmId(vm.id, dto.vmId);
                }
                if (dto.networkAdapterList != null) {
                    vm.networkAdapterList = convertNetworkAdapterListRh2Vh(dto.networkAdapterList, null, vm.id);
                }
                if (dto.diskList != null) {
                    vm.diskList = convertDiskListRh2Vh(dto.diskList, null, vm.id);
                }
                vmList.add(vm);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return vmList;

    }

    /**
     * Rhev固有データvmListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return vmList
     * @throws ConversionRuntimeException
     */
    public List<Vm> convertVmListRh2Vh(Object data) {
        return convertVmListRh2Vh(data, null);
    }

    /**
     * Rhev固有データtemplateListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return templateList
     * @throws ConversionRuntimeException
     */
    public List<Template> convertTemplateListRh2Vh(Object data, Long globalId) throws ConversionRuntimeException {
        List<Template> templateList = new ArrayList<Template>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                TemplateDto dto = (TemplateDto) value;
                Template template = new Template();

                template.name = dto.name;
                template.description = dto.description;
                template.cpuCore = dto.cpuCore;
                template.memory = dto.memory;
                template.status = convertTemplateStatusRh2Vh(dto.status);
                template.os = convertOsRh2Vh(dto.os);
                template.clusterId = convertClusterIdRh2Vh(dto.clusterId);

                if (dto.diskTemplateList != null && dto.diskTemplateList.length > 0) {
                    DiskTemplateDto diskTemplateDto = (DiskTemplateDto) dto.diskTemplateList[0];
                    try {
                        template.storageId = convertStorageIdRh2Vh(diskTemplateDto.storageId);
                    } catch (Exception e) {
                        template.storageId = cloudConfig.getRhevStorageId();
                    }
                } else {
                    template.storageId = cloudConfig.getRhevStorageId();
                }

                // localIdに対象レコードが無い場合の処理
                try {
                    template.id = convertTemplateIdRh2Vh(dto.templateId);

                    if (dto.diskTemplateList.length != 0) {
                        template.diskTemplateList = convertDiskTemplateRh2Vh2(dto.diskTemplateList, template.id);
                    }
                } catch (DriverSQLRuntimeException e) {
                    // templateテーブルに新規挿入
                    template.cloudId = cloudId;
                    jdbcManager.insert(template)
                        .execute();
                    // LocalIdテーブルに追加
                    bindTemplateId(template.id, dto.templateId);

                    if (dto.diskTemplateList.length != 0) {
                        template.diskTemplateList = convertDiskTemplateRh2Vh2(dto.diskTemplateList, template.id);
                    }
                }

                templateList.add(template);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return templateList;

    }

    /**
     * Rhev固有データtemplateListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return templateList
     * @throws ConversionRuntimeException
     */
    public List<Template> convertTemplateListRh2Vh(Object data) {
        return convertTemplateListRh2Vh(data, null);
    }

    /**
     * Rhev固有データNetworkAdapterTemplateListをvHut共通データに変換する。
     * @param networkAdapterTemplateList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<NetworkAdapterTemplate> convertNetworkAdapterTemplateRh2Vh(Object[] data, Long templateId) throws ConversionRuntimeException {
        List<NetworkAdapterTemplate> networkAdapterTemplateList = new ArrayList<NetworkAdapterTemplate>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                NetworkAdapterTemplateDto dto = (NetworkAdapterTemplateDto) value;
                NetworkAdapterTemplate networkAdapterTemplate = new NetworkAdapterTemplate();

                networkAdapterTemplate.name = dto.name;
                networkAdapterTemplate.mac = dto.macAddress;

                try {
                    networkAdapterTemplate.id = convertNetworkAdapterTemplateIdRh2Vh(dto.networkAdapterId);
                } catch (DriverSQLRuntimeException e) {
                    // networkAdapterTemplateテーブルに新規挿入
                    networkAdapterTemplate.cloudId = cloudId;
                    networkAdapterTemplate.templateId = templateId;

                    jdbcManager.insert(networkAdapterTemplate)
                        .execute();
                    // 挿入したデータのIDを取得
                    NetworkAdapterTemplate selectResult = jdbcManager.from(NetworkAdapterTemplate.class)
                        .where("template_id = ?", templateId)
                        .getSingleResult();
                    // LocalIdテーブルに追加
                    bindNetworkAdapterTemplateId(selectResult.id, dto.networkAdapterId);

                }

                networkAdapterTemplateList.add(networkAdapterTemplate);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return networkAdapterTemplateList;
    }

    /**
     * Rhev固有データNetworkAdapterTemplateListをvHut共通データに変換する。
     * @param networkAdapterTemplateList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<NetworkAdapterTemplate> convertNetworkAdapterTemplateRh2Vh(Object[] data) {
        return convertNetworkAdapterTemplateRh2Vh(data, null);
    }

    /**
     * Rhev固有データdiskTemplateListをvHut共通データに変換する。
     * @param diskTemplateList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<DiskTemplate> convertDiskTemplateRh2Vh2(Object[] data, Long templateId) throws ConversionRuntimeException {
        List<DiskTemplate> diskTemplateList = new ArrayList<DiskTemplate>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                DiskTemplateDto dto = (DiskTemplateDto) value;
                DiskTemplate diskTemplate = new DiskTemplate();

                diskTemplate.size = (int) dto.size;
                try {
                    diskTemplate.storageId = convertStorageIdRh2Vh(dto.storageId);
                } catch (Exception e) {
                    diskTemplate.storageId = cloudConfig.getRhevStorageId();
                }

                try {
                    diskTemplate.id = convertDiskTemplateIdRh2Vh(dto.diskId);
                } catch (DriverSQLRuntimeException e) {
                    // DiskTemplateテーブルに新規挿入
                    diskTemplate.cloudId = cloudId;
                    diskTemplate.templateId = templateId;
                    jdbcManager.insert(diskTemplate)
                        .execute();
                    // LocalIdテーブルに追加
                    bindDiskTemplateId(diskTemplate.id, dto.diskId);
                }
                diskTemplateList.add(diskTemplate);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return diskTemplateList;

    }

    /**
     * Rhev固有データdiskTemplateListをvHut共通データに変換する。
     * @param diskTemplateList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<DiskTemplate> convertDiskTemplateRh2Vh2(Object[] data) {
        return convertDiskTemplateRh2Vh2(data, null);
    }

    /**
     * Rhev固有データCloudUserListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return vhutUserList
     * @throws ConversionRuntimeException
     */
    public List<CloudUser> convertCloudUserListRh2Vh(Object data) throws ConversionRuntimeException {
        List<CloudUser> vhutUserList = new ArrayList<CloudUser>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {
                CloudUserDto dto = (CloudUserDto) value;
                CloudUser cloudUser = new CloudUser();
                //アカウントの@を含むドメインを除去
                cloudUser.account = deleteDomainInAccount(dto.account);
                cloudUser.lastName = dto.lastName;
                cloudUser.firstName = dto.firstName;
                cloudUser.email = dto.email;

                try {
                    // LocalIdテーブルに該当レコードが無ければレコードを追加
                    cloudUser.id = convertUserIdRh2Vh(dto.userId);

                } catch (DriverSQLRuntimeException e) {
                    // cloudUserテーブルに新規挿入
                    cloudUser.cloudId = cloudId;
                    jdbcManager.insert(cloudUser)
                        .execute();
                    // LocalIdテーブルに追加
                    bindUserId(cloudUser.id, dto.userId);
                }

                vhutUserList.add(cloudUser);
            }

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return vhutUserList;

    }

    /**
     * Rhev固有データcommandListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @param commandListFromCloud Cloudから受け取ったコマンドリスト
     * @return commandList
     * @throws ConversionRuntimeException
     */
    public List<Command> convertCommandListRh2Vh(Object data, List<Command> commandListFromCloud) throws ConversionRuntimeException {
        List<Command> commandList = new ArrayList<Command>();

        try {
            Object[] valueList = (Object[]) data;
            for (Object value : valueList) {

                CommandDto dto = (CommandDto) value;
                Command command = new Command();
                command.id = convertCommandIdRh2Vh(dto.commandId);
                command.status = convertCommandStatusRh2Vh(dto.status);

                commandList.add(command);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return commandList;

    }

    /**
     * Rhev固有データcommandListをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return commandList
     * @throws ConversionRuntimeException
     */
    public List<Command> convertCommandListRh2Vh(Object data) {
        return convertCommandListRh2Vh(data, null);
    }

    /**
     * Rhev固有データnetworkAdapterListをvHut共通データに変換する。
     * @param remoteNwas 外部から取得したNetworkAdapterDtoのリスト
     * @param newNwas Cloudで作成されたNetworkAdapterのリスト
     * @param vmId 所属するVmのID
     * @return
     * @throws ConversionRuntimeException
     *
     * <p>
     * localNwaが指定された時:localとremoteを結び付けremoteのIDで作成したNetworkAdapterのリストを返す.
     * <br>
     * localNwaが指定されない時:既存のものを探す
     * <br>
     * 見つからないとき->新規に作成し、そのNetworkAdapterのリストを返す
     * <br>
     * 見つかった時->選択して、NetworkAdapterのリストを返す
     *
     */
    private List<NetworkAdapter> convertNetworkAdapterListRh2Vh(Object[] remoteNwas, List<NetworkAdapter> newNwas, Long vmId) throws ConversionRuntimeException {
        List<NetworkAdapter> networkAdapterList = new ArrayList<NetworkAdapter>();

        try {
            Object[] valueList = (Object[]) remoteNwas;
            for (int i = 0; i < remoteNwas.length; i++) {
                NetworkAdapterDto dto = (NetworkAdapterDto) valueList[i];
                NetworkAdapter networkAdapter = new NetworkAdapter();
                //                SecurityGroup securityGroup = new SecurityGroup();
                //                networkAdapter.securityGroup = securityGroup;
                networkAdapter.name = dto.name;
                networkAdapter.mac = MacConversionUtil.convertColonToHex(dto.macAddress);
                if (newNwas != null) {
                    //localNwaが指定された時:localとremoteを結び付けremoteのIDで作成したNetworkAdapterのリストを返す.
                    networkAdapter.id = bindNetworkAdapterId(newNwas.get(i).id, dto.networkAdapterId);
                    networkAdapter.vmId = vmId;
                } else {
                    try {
                        //localNwaが指定されない時:既存のものを探す
                        networkAdapter.id = convertNetworkAdapterIdRh2Vh(dto.networkAdapterId);
                    } catch (DriverSQLRuntimeException e) {
                        // 見つからないとき
                        // networkAdapterテーブルに新規挿入
                        //                        networkAdapter.cloudId = cloudId;
                        //                        jdbcManager.insert(networkAdapter).execute();
                        //                        // LocalIdテーブルに追加
                        //                        bindNetworkAdapterId(networkAdapter.id, dto.networkAdapterId);
                        //NEXT: SecurityGroupがない場合InsertできないのでNetworkAdapterの新規作成は断念
                    }
                }
                networkAdapterList.add(networkAdapter);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return networkAdapterList;
    }

    /**
     * Rhev固有データnetworkAdapterListをvHut共通データに変換する。
     * @param networkAdapterList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<NetworkAdapter> convertNetworkAdapterListRh2Vh(Object[] data) {
        return convertNetworkAdapterListRh2Vh(data, null, null);
    }

    /**
     * Rhev固有データdiskListをvHut共通データに変換する。
     * @param remoteDisks 外部から取得したDiskDtoのリスト
     * @param newDisks 新規に作成したDiskのリスト
     * @param vmId 所属するVMのID
     * @return
     * @throws ConversionRuntimeException
     */
    private List<Disk> convertDiskListRh2Vh(Object[] remoteDisks, List<Disk> newDisks, Long vmId) {
        List<Disk> diskList = new ArrayList<Disk>();

        try {
            Object[] valueList = (Object[]) remoteDisks;
            for (int i = 0; i < remoteDisks.length; i++) {
                DiskDto dto = (DiskDto) valueList[i];
                Disk disk = new Disk();
                disk.size = (int) dto.size;
                try {
                    disk.storageId = convertStorageIdRh2Vh(dto.storageId);
                } catch (Exception e) {
                    disk.storageId = cloudConfig.getRhevStorageId();
                }
                if (newDisks != null) {
                    //localNwaが指定された時:localとremoteを結び付けremoteのIDで作成したNetworkAdapterのリストを返す.
                    disk.id = bindDiskId(newDisks.get(i).id, dto.diskId);
                    disk.vmId = vmId;
                } else {
                    try {
                        disk.id = convertDiskIdRh2Vh(dto.diskId);
                    } catch (DriverSQLRuntimeException e) {
                        // 見つからないとき
                        // networkAdapterテーブルに新規挿入
                        disk.cloudId = cloudId;
                        disk.vmId = vmId;
                        jdbcManager.insert(disk)
                            .execute();
                        // LocalIdテーブルに追加
                        bindDiskId(disk.id, dto.diskId);
                    }
                }
                diskList.add(disk);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return diskList;
    }

    /**
     * Rhev固有データdiskListをvHut共通データに変換する。
     * @param diskList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<Disk> convertDiskListRh2Vh(Object[] data) {
        return convertDiskListRh2Vh(data, null, null);
    }

    /**
     * Rhev固有データnetworkAdapterTemplateListをvHut共通データに変換する。
     * @param networkAdapterTemplateList
     * @return
     * @throws ConversionRuntimeException
     */
    private List<NetworkAdapterTemplate> convertNetworkAdapterTemplateListRh2Vh(Object[] data, List<NetworkAdapterTemplate> natmp) {
        List<NetworkAdapterTemplate> networkAdapterTemplateList = new ArrayList<NetworkAdapterTemplate>();

        try {
            Object[] valueList = (Object[]) data;
            for (int i = 0; i < data.length; i++) {
                //            for (Object value : valueList) {
                NetworkAdapterTemplateDto dto = (NetworkAdapterTemplateDto) valueList[i];
                NetworkAdapterTemplate networkAdapterTemplate = new NetworkAdapterTemplate();

                if (natmp == null) {
                    networkAdapterTemplate.id = convertNetworkAdapterIdRh2Vh(dto.networkAdapterId);
                } else {
                    networkAdapterTemplate.id = bindNetworkAdapterId(natmp.get(i).id, dto.networkAdapterId);
                }

                networkAdapterTemplate.name = dto.name;
                networkAdapterTemplate.mac = dto.macAddress;

                networkAdapterTemplateList.add(networkAdapterTemplate);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return networkAdapterTemplateList;
    }

    /**
     * Rhev固有データdiskTemplateListをvHut共通データに変換する。
     * @param diskList
     * @param globalId
     * @return
     */
    private List<DiskTemplate> convertDiskTemplateListRh2Vh(Object[] data, List<DiskTemplate> dtmp) {
        List<DiskTemplate> diskTemplateList = new ArrayList<DiskTemplate>();

        try {
            Object[] valueList = (Object[]) data;
            for (int i = 0; i < data.length; i++) {
                //            for (Object value : valueList) {
                DiskTemplateDto dto = (DiskTemplateDto) valueList[i];
                DiskTemplate diskTemplate = new DiskTemplate();

                if (dtmp == null) {
                    diskTemplate.id = convertDiskTemplateIdRh2Vh(dto.diskId);
                    //                    diskTemplate.storageId = convertStorageIdRh2Vh(dto.storageId);

                } else {
                    diskTemplate.id = bindDiskTemplateId(dtmp.get(i).id, dto.diskId);
                    //                    diskTemplate.storageId = bindStorageId(globalId, dto.storageId);

                }

                diskTemplate.size = (int) dto.size;

                diskTemplateList.add(diskTemplate);
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return diskTemplateList;
    }

    /**
     * Rhev固有データvhutUserをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return vhutUser
     * @throws ConversionRuntimeException
     */
    public CloudUser convertCloudUserRh2Vh(Object data) {
        return convertCloudUserRh2Vh(data, null);
    }

    /**
     * Rhev固有データvhutUserをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return cloudUser
     * @throws ConversionRuntimeException
     */
    public CloudUser convertCloudUserRh2Vh(Object data, Long globalId) throws ConversionRuntimeException {

        CloudUser cloudUser = new CloudUser();

        try {
            Object[] obj = (Object[]) data;
            CloudUserDto value = (CloudUserDto) obj[0];

            if (globalId == null) {
                cloudUser.id = convertUserIdRh2Vh(value.userId);
            } else {
                cloudUser.id = bindUserId(globalId, value.userId);
            }

            cloudUser.account = deleteDomainInAccount(value.account);
            cloudUser.lastName = value.lastName;
            cloudUser.firstName = value.firstName;
            cloudUser.email = value.email;

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return cloudUser;

    }

    //    /**
    //     * Rhev固有データvmをvHut共通データに変換する。
    //     *
    //     * @param data
    //     *            RHEV依存データ
    //     * @return vm 仮想マシン
    //     * @throws ConversionRuntimeException
    //     */
    //    public Vm convertVmRh2Vh(Object data) {
    //        return convertVmRh2Vh(data, null);
    //    }

    /**
     * Rhev固有データvmをvHut共通データに変換する。
     *
     * @param data RHEV依存データ
     * @param globalId vHut共通データのId
     * @return vm 仮想マシン
     * @throws ValidationRuntimeException
     */
    public Vm convertVmRh2Vh(Object data, Vm vmo) {
        Vm vm = new Vm();

        try {
            Object[] obj = (Object[]) data;
            VmDto value = (VmDto) obj[0];

            if (vmo.id == null) {
                vm.id = convertVmIdRh2Vh(value.vmId);
            } else {
                vm.id = bindVmId(vmo.id, value.vmId);
            }

            vm.templateId = convertTemplateIdRh2Vh(value.templateId);
            //TODO: VMの動作ホストが取れるようになったら改善する
            //            vm.hostId = convertHostIdRh2Vh(value.hostId);

            vm.clusterId = convertClusterIdRh2Vh(value.clusterId);

            vm.description = value.description;
            vm.cpuCore = value.cpuCore;
            vm.memory = value.memory;
            vm.cpuUsage = value.cpuUsage;
            vm.memoryUsage = value.memoryUsage;
            vm.status = convertVmStatusRh2Vh(value.status);
            vm.os = convertOsRh2Vh(value.os);

            if (vmo.networkAdapterList.size() != 0) {
                vm.networkAdapterList = convertNetworkAdapterListRh2Vh(value.networkAdapterList, vmo.networkAdapterList, vmo.id);
            }

            if (vmo.diskList.size() != 0) {
                vm.diskList = convertDiskListRh2Vh(value.diskList, vmo.diskList, vmo.id);
            }

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return vm;

    }

    /**
     * Rhev固有データvmをvHut共通データに変換する。
     *
     * @param data RHEV依存データ
     * @param globalId vHut共通データのId
     * @return vm 仮想マシン
     * @throws ValidationRuntimeException
     */
    public Vm convertVmRh2Vh(Object data, Long vmId) {
        Vm vm = new Vm();

        try {
            Object[] obj = (Object[]) data;
            VmDto value = (VmDto) obj[0];

            if (vmId == null) {
                vm.id = convertVmIdRh2Vh(value.vmId);
            } else {
                vm.id = bindVmId(vmId, value.vmId);
            }

            vm.name = value.name;

            vm.templateId = convertTemplateIdRh2Vh(value.templateId);
            //TODO: VMの動作ホストが取れるようになったら改善する
            //            vm.hostId = convertHostIdRh2Vh(value.hostId);

            vm.clusterId = convertClusterIdRh2Vh(value.clusterId);

            vm.description = value.description;
            vm.cpuCore = value.cpuCore;
            vm.memory = value.memory;
            vm.cpuUsage = value.cpuUsage;
            vm.memoryUsage = value.memoryUsage;
            vm.status = convertVmStatusRh2Vh(value.status);
            vm.os = convertOsRh2Vh(value.os);

            if (value.networkAdapterList != null) {
                vm.networkAdapterList = convertNetworkAdapterListRh2Vh(value.networkAdapterList);
            }

            if (value.diskList != null) {
                vm.diskList = convertDiskListRh2Vh(value.diskList);
            }

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return vm;

    }

    /**
     * Rhev固有データnetworkAdapterをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return networkAdapter
     * @throws ConversionRuntimeException
     */
    public NetworkAdapter convertNetworkAdapterRh2Vh(Object data) throws ConversionRuntimeException {
        return convertNetworkAdapterRh2Vh(data, null);
    }

    /**
     * Rhev固有データnetworkAdapterをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return networkAdapter
     * @throws ValidationRuntimeException
     */
    public NetworkAdapter convertNetworkAdapterRh2Vh(Object data, Long globalId) throws ValidationRuntimeException {
        NetworkAdapter networkAdapter = new NetworkAdapter();

        try {

            Object[] obj = (Object[]) data;
            NetworkAdapterDto value = (NetworkAdapterDto) obj[0];

            if (globalId == null) {
                networkAdapter.id = convertNetworkAdapterIdRh2Vh(value.networkAdapterId);
            } else {
                networkAdapter.id = bindNetworkAdapterId(globalId, value.networkAdapterId);
            }

            SecurityGroup securityGroup = new SecurityGroup();
            networkAdapter.securityGroup = securityGroup;
            networkAdapter.securityGroup.networkId = convertNetworkIdRh2Vh(value.networkId);
            networkAdapter.name = value.name;
            networkAdapter.mac = value.macAddress;

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return networkAdapter;

    }

    /**
     * Rhev固有データDiskをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return disk ディスク
     */
    public Disk convertDiskRh2Vh(Object data) {
        return convertDiskRh2Vh(data, null);
    }

    /**
     * Rhev固有データDiskをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return disk ディスク
     * @throws ConversionRuntimeException
     */
    public Disk convertDiskRh2Vh(Object data, Long globalId) throws ConversionRuntimeException {
        Disk disk = new Disk();

        try {

            Object[] obj = (Object[]) data;
            DiskDto value = (DiskDto) obj[0];

            if (globalId == null) {
                disk.id = convertDiskIdRh2Vh(value.diskId);

            } else {
                disk.id = bindDiskId(globalId, value.diskId);
            }

            disk.size = (int) value.size;

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }

        return disk;

    }

    /**
     * Rhev固有データtemplateをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return template テンプレート
     */
    public Template convertTemplateRh2Vh(Object data) {
        return convertTemplateRh2Vh(data, null);
    }

    /**
     * Rhev固有データtemplateをvHut共通データに変換する。
     *
     * @param data
     *            RHEV依存データ
     * @return template テンプレート
     * @throws ConversionRuntimeException
     */
    public Template convertTemplateRh2Vh(Object data, Template tmp) throws ConversionRuntimeException {

        Template template = new Template();

        try {
            Object[] obj = (Object[]) data;
            TemplateDto value = (TemplateDto) obj[0];

            if (tmp.id == null) {
                template.id = convertTemplateIdRh2Vh(value.templateId);

            } else {
                template.id = bindTemplateId(tmp.id, value.templateId);
            }
            template.clusterId = convertClusterIdRh2Vh(value.clusterId);
            template.name = value.name;
            template.description = value.description;
            template.cpuCore = value.cpuCore;
            template.memory = value.memory;
            template.status = convertTemplateStatusRh2Vh(value.status);

            template.os = convertOsRh2Vh(value.os);

            if (tmp.networkAdapterTemplateList.size() != 0) {
                template.networkAdapterTemplateList = convertNetworkAdapterTemplateListRh2Vh(value.networkAdapterTemplateList, tmp.networkAdapterTemplateList);
            }
            if (tmp.diskTemplateList.size() != 0) {
                template.diskTemplateList = convertDiskTemplateListRh2Vh(value.diskTemplateList, tmp.diskTemplateList);
            }

            if (template.diskTemplateList.size() > 0) {
                template.storageId = template.diskTemplateList.get(0).storageId;
            } else {
                template.storageId = cloudConfig.getRhevStorageId();
            }

        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException(e);
        }
        return template;
    }

    /**
     * Rhev固有データosをvHut共通データに変換する。
     * @param os
     * @return
     */
    private static Os convertOsRh2Vh(String os) {
        Os result = Os.OTHER;

        switch (RhevOs.valueOf(os)) {
            case Unassigned:
                result = Os.OTHER;
                break;
            case RHEL5:
                result = Os.RHEL_5;
                break;
            case RHEL5x64:
                result = Os.RHEL_5_64;
                break;
            case RHEL4:
                result = Os.RHEL_4;
                break;
            case RHEL4x64:
                result = Os.RHEL_4_64;
                break;
            case RHEL3:
                result = Os.RHEL_3;
                break;
            case RHEL3x64:
                result = Os.RHEL_3_64;
                break;
            case OtherLinux:
                result = Os.OTHER_LINUX;
                break;
            case WindowsXP:
                result = Os.WINDOWS_XP;
                break;
            case Windows2003:
                result = Os.WINDOWS_2003;
                break;
            case Windows2003x64:
                result = Os.WINDOWS_2003_64;
                break;
            case Windows7:
                result = Os.WINDOWS_7;
                break;
            case Windows7x64:
                result = Os.WINDOWS_7_64;
                break;
            case Windows2008:
                result = Os.WINDOWS_2008;
                break;
            case Windows2008x64:
                result = Os.WINDOWS_2008_64;
                break;
            case Windows2008R2x64:
                result = Os.WINDOWS_2003_64;
                break;
            case Other:
                result = Os.OTHER;
                break;
        }

        return result;

    }

    /**
     * commandListをvHut共通データからRHEV依存データに変換する。
     *
     * @param commandList
     *            コマンドのリスト
     * @return result 変換後のデータ
     */
    public Object[] convertCommandListVh2Rh(List<Command> commandList) {

        List<String> ids = new ArrayList<String>();

        for (Command command : commandList) {

            ids.add(convertIdVh2Rh(LocalIdType.COMMAND, command.id));

        }
        return ids.toArray();
    }

    /**
     * vmをvHut共通データからRHEV依存データに変換する。
     *
     * @param vm
     *            仮想マシン
     * @return result 変換後のデータ
     */
    public Object[] convertVmVh2Rh(Vm vm) {

        //        List<String> ids = new ArrayList<String>();
        Object[] obj = new Object[7];

        obj[0] = convertTemplateIdVh2Rh(vm.templateId);
        obj[1] = vm.name;
        obj[2] = vm.description;
        obj[3] = vm.cpuCore;
        obj[4] = vm.memory;
        obj[5] = convertStorageIdVh2Rh(vm.storageId);
        obj[6] = convertClusterIdVh2Rh(vm.clusterId);

        //      // DBにアクセスしてデータを取り出す。
        //      // SQLをレビュにもとづき修正
        //      LocalId localId = jdbcManager.from(LocalId.class)
        //      .where("globalId = ?", obj[0])
        //      .where("cloudId = ?", cloudId)
        //      .getSingleResult();
        //
        //      obj[0] = localId.localId;

        return obj;
    }

    /**
     * networkAdapterをvHut共通データからRHEV依存データに変換する。
     *
     * @param networkAdapter
     *            仮想マシンID
     * @return result 変換後のデータ
     */
    public Object[] convertNetworkAdapterVh2Rh(NetworkAdapter networkAdapter) {

        Object[] obj = new Object[4];

        obj[0] = convertVmIdVh2Rh(networkAdapter.vmId);
        obj[1] = networkAdapter.name;
        obj[2] = convertNetworkIdVh2Rh(networkAdapter.securityGroup.networkId);
        obj[3] = networkAdapter.mac;

        return obj;
    }

    /**
     * diskをvHut共通データからRHEV依存データに変換する。
     *
     * @param disk ディスク
     * @return result 変換後のデータ
     */
    public Object[] convertDiskVh2Rh(Disk disk) {
        Object[] obj = new Object[3];

        obj[0] = convertVmIdVh2Rh(disk.vmId);
        obj[1] = (int) disk.size;
        obj[2] = convertStorageIdVh2Rh(disk.storageId);

        return obj;
    }

    /**
     * templateをvHut共通データからRHEV依存データに変換する。
     *
     * @param template テンプレート
     * @return result 変換後のデータ
     */
    public Object[] convertTemplateVh2Rh(Template template, long vmId) {
        Object[] obj = new Object[5];

        obj[0] = convertVmIdVh2Rh(vmId);

        obj[1] = template.name;
        obj[2] = template.description;
        obj[3] = convertStorageIdVh2Rh(template.storageId);
        obj[4] = convertClusterIdVh2Rh(template.clusterId);

        return obj;
    }

    /**
     * Rhev固有データVmのStatusをvHut共通データに変換する。
     * @param status
     * @return
     *
     */
    private static VmStatus convertVmStatusRh2Vh(String status) {
        VmStatus result = VmStatus.UNKNOWN;
        try {
            switch (RhevVmStatus.valueOf(status)) {
                case Down:
                    result = VmStatus.DOWN;
                    break;
                case Up:
                    result = VmStatus.UP;
                    break;
                case PoweringUp:
                    result = VmStatus.POWERING_UP;
                    break;
                case RebootInProgress:
                case PoweredDown:
                    result = VmStatus.SHUTTING_DOWN;
                    break;
                case MigratingFrom:
                case MigratingTo:
                case Paused:
                case WaitForLaunch:
                case SavingState:
                case RestoringState:
                case Suspended:
                    result = VmStatus.UNAVAILABLE;
                    break;
                case ImageLocked:
                    result = VmStatus.DEVELOPPING;
                    break;
                case Unknown:
                case NotResponding:
                case ImageIllegal:
                case PoweringDown:
                case Unassigned:
                    result = VmStatus.ERROR;
                    break;
            }
        } catch (Exception e) {
            ConversionRuntimeException.newOutputException(e);
        }
        return result;
    }

    /**
     * Rhev固有データTemplateのStatusをvHut共通データに変換する。
     * @param status
     * @return
     */
    private static TemplateStatus convertTemplateStatusRh2Vh(String status) {
        TemplateStatus result = TemplateStatus.UNKNOWN;
        try {
            switch (RhevTemplateStatus.valueOf(status)) {
                case OK:
                    result = TemplateStatus.AVAILABLE;
                    break;
                case Locked:
                    result = TemplateStatus.DEVELOPPING;
                    break;
                case Illegal:
                    result = TemplateStatus.ERROR;
                    break;
            }
        } catch (Exception e) {
            ConversionRuntimeException.newOutputException(e);
        }
        return result;
    }

    /**
     * Rhev固有データHostのStatusをvHut共通データに変換する。
     * @param status
     * @return
     */

    private static HostStatus convertHostStatusRh2Vh(String status) {
        HostStatus result = HostStatus.UNKNOWN;
        try {
            switch (RhevHostStatus.valueOf(status)) {
                case Down:
                    result = HostStatus.DOWN;
                    break;
                case Maintenance:
                    result = HostStatus.MAINTENANCE;
                    break;
                case Up:
                    result = HostStatus.UP;
                    break;
                case NonResponsive:
                case Error:
                    result = HostStatus.ERROR;
                    break;
                case Installing:
                case InstallFailed:
                case Reboot:
                case PreparingForMaintenance:
                    result = HostStatus.MAINTENANCE;
                    break;
            }
        } catch (Exception e) {
            ConversionRuntimeException.newOutputException(e);
        }
        return result;
    }

    /**
     * Rhev固有データNetworkのStatusをvHut共通データに変換する。
     * @param status
     * @return
     */
    private static NetworkStatus convertNetworkStatusRh2Vh(String status) {
        NetworkStatus result = NetworkStatus.UNKNOWN;

        try {
            switch (RhevNetworkStatus.valueOf(status)) {

                case Operational:
                    result = NetworkStatus.AVAILABLE;
                    break;
                case NonOperational:
                    result = NetworkStatus.ERROR;
                    break;
            }
        } catch (Exception e) {
            ConversionRuntimeException.newOutputException(e);
        }

        return result;
    }

    /**
     * Rhev固有データStorageのStatusをvHut共通データに変換する。
     * @param status
     * @return
     *
    Unknown, Uninitialized, Unattached, Active, InActive, Locked
     */
    private static StorageStatus convertStorageStatusRh2Vh(String status) {
        StorageStatus result = StorageStatus.UNKNOWN;
        try {
            switch (RhevStorageStatus.valueOf(status)) {
                case Active:
                    result = StorageStatus.UP;
                    break;
                case InActive:
                case Unknown:
                case Uninitialized:
                case Unattached:
                    result = StorageStatus.DOWN;
                    break;
                case Locked:
            }
        } catch (Exception e) {
            ConversionRuntimeException.newOutputException(e);
        }
        return result;
    }

    /**
     * Rhev固有データCommandのStatusをvHut共通データに変換する。
     * @param status
     * @return
     *    running, finished
     */
    private static CommandStatus convertCommandStatusRh2Vh(String status) {
        CommandStatus result = CommandStatus.UNKNOWN;
        try {
            switch (RhevCommandStatus.valueOf(status)) {
                case running:
                    result = CommandStatus.EXECUTING;
                    break;
                case finished:
                    result = CommandStatus.SUCCESS;
                    break;
                case Unusual:
                    result = CommandStatus.ERROR;
                    break;
            }
        } catch (Exception e) {
            ConversionRuntimeException.newOutputException(e);
        }
        return result;
    }

    /**
     * Rhev固有データuserIdをvHut共通データに変換する。
     * @param id ユーザID
     * @return result 変換後のデータ
     */
    public Long convertUserIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.CLOUD_USER, id);
    }

    /**
     * userIdをvHut共通データからRHEV依存データに変換する。
     * @param id ユーザID
     * @return result 変換後のデータ
     */
    public String convertUserIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.CLOUD_USER, id);
    }

    /**
     * Rhev固有データcommandIdをvHut共通データに変換する。
     * @param id コマンドID
     * @return result 変換後のデータ
     */
    public Long convertCommandIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.COMMAND, id);
    }

    /**
     * commandIdをvHut共通データからRHEV依存データに変換する。
     * @param id コマンドID
     * @return result 変換後のデータ
     */
    public String convertCommandIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.COMMAND, id);
    }

    /**
     * Rhev固有データclusterIdをvHut共通データに変換する。
     * @param id クラスタID
     * @return result 変換後のデータ
     */
    public Long convertClusterIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.CLUSTER, id);
    }

    /**
     * clusterIdをvHut共通データからRHEV依存データに変換する。
     * @param id クラスタID
     * @return result 変換後のデータ
     */
    public String convertClusterIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.CLUSTER, id);
    }

    /**
     * Rhev固有データhostIdをvHut共通データに変換する。
     * @param id ホストID
     * @return result 変換後のデータ
     */
    public Long convertHostIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.HOST, id);
    }

    /**
     * hostIdをvHut共通データからRHEV依存データに変換する。
     * @param id ホストID
     * @return result 変換後のデータ
     */
    public String convertHostIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.HOST, id);
    }

    /**
     * Rhev固有データvmIdをvHut共通データに変換する。
     * @param id 仮想マシンID
     * @return result 変換後のデータ
     */
    public Long convertVmIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.VM, id);
    }

    /**
     * vmIdをvHut共通データからRHEV依存データに変換する。
     * @param id 仮想マシンID
     * @return result 変換後のデータ
     */
    public String convertVmIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.VM, id);
    }

    /**
     * Rhev固有データtemplateIdをvHut共通データに変換する。
     * @param id テンプレートID
     * @return result 変換後のデータ
     */
    public Long convertTemplateIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.TEMPLATE, id);
    }

    /**
     * templateIdをvHut共通データからRHEV依存データに変換する。
     * @param id テンプレートID
     * @return result 変換後のデータ
     */
    public String convertTemplateIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.TEMPLATE, id);
    }

    /**
     * Rhev固有データnetworkAdapterIdをvHut共通データに変換する。
     * @param id ネットワークアダプターID
     * @return result 変換後のデータ
     */
    public Long convertNetworkAdapterIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.NETWORK_ADAPTER, id);
    }

    /**
     * networkAdapterIdをvHut共通データからRHEV依存データに変換する。
     * @param id ネットワークアダプターID
     * @return result 変換後のデータ
     */
    public String convertNetworkAdapterIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.NETWORK_ADAPTER, id);
    }

    /**
     * Rhev固有データnetworkIdをvHut共通データに変換する。
     * @param id ネットワークID
     * @return result 変換後のデータ
     */
    public Long convertNetworkIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.NETWORK, id);
    }

    /**
     * networkIdをvHut共通データからRHEV依存データに変換する。
     * @param id ネットワークID
     * @return result 変換後のデータ
     */
    public String convertNetworkIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.NETWORK, id);
    }

    /**
     * Rhev固有データdiskIdをvHut共通データに変換する。
     * @param id ストレージID
     * @return result 変換後のデータ
     */
    public Long convertDiskIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.DISK, id);
    }

    /**
     * diskIdをvHut共通データからRHEV依存データに変換する。
     * @param id ストレージID
     * @return result 変換後のデータ
     */
    public String convertDiskIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.DISK, id);
    }

    /**
     * Rhev固有データstorageIdをvHut共通データに変換する。
     * @param id ストレージID
     * @return result 変換後のデータ
     */
    public Long convertStorageIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.STORAGE, id);
    }

    /**
     * storageIdをvHut共通データからRHEV依存データに変換する。
     * @param id ストレージID
     * @return result 変換後のデータ
     */
    public String convertStorageIdVh2Rh(long id) {
        return convertIdVh2Rh(LocalIdType.STORAGE, id);
    }

    /**
     * Rhev固有データdiskTemplateIdをvHut共通データに変換する。
     * @param id diskTemplateID
     * @return result 変換後のデータ
     */
    public Long convertDiskTemplateIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.DISK_TEMPLATE, id);
    }

    /**
     * Rhev固有データnetworkAdapterTemplateIdをvHut共通データに変換する。
     * @param id networkAdapterID
     * @return result 変換後のデータ
     */
    public Long convertNetworkAdapterTemplateIdRh2Vh(String id) {
        return convertIdRh2Vh(LocalIdType.NETWORK_ADAPTER_TEMPLATE, id);
    }

    /**
     * 各種IdをRHEV依存データからvHut共通データに変換する。
     * @param type Idのタイプ
     * @param id RHEV依存データのId
     * @return vHut共通データのId
     */
    protected long convertIdRh2Vh(LocalIdType type, String id) {
        LocalId localId = null;
        localId = jdbcManager.from(LocalId.class)
            .where("cloudId = ? and type = ? and local_id = ?", cloudId, type, id)
            .getSingleResult();

        if (localId == null) {
            // cloudUser, template, host, cluster, storage, vm の該当LocalIdレコードが無い場合は別処理が走る
            throw DriverSQLRuntimeException.newException();

        }
        return localId.globalId;
    }

    /**
     * 各種IdをvHut共通データからRHEV依存データに変換する。
     * @param type Idのタイプ
     * @param id 共通データのId
     * @return Rhev依存データのId
     */
    protected String convertIdVh2Rh(LocalIdType type, long id) {
        LocalId localId = null;
        localId = jdbcManager.from(LocalId.class)
            .where("cloudId = ? and type = ? and global_id = ?", cloudId, type, id)
            .getSingleResult();

        if (localId == null) {
            throw DriverSQLRuntimeException.newException();
        }
        return localId.localId;
    }

    /**
     * vmIdを関連付けてDBに保存する。
     * @param globalId {@link Vm} のID
     * @param localId Rhevから返却された{@link VmDto}のID
     * @return {@link Vm} のID
     */
    public long bindVmId(Long globalId, String localId) {
        return bindId(LocalIdType.VM, globalId, localId);
    }

    /**
     * networkAdapterIdを関連付けてDBに保存する。
     * @param globalId {@link NetworkAdapter} のID
     * @param localId Rhevから返却された{@link NetworkAdapterDto}のID
     * @return {@link NetworkAdapter} のID
     */
    public long bindNetworkAdapterId(Long globalId, String localId) {
        return bindId(LocalIdType.NETWORK_ADAPTER, globalId, localId);
    }

    /**
     * diskIdを関連付けてDBに保存する。
     * @param globalId
     * @param diskId
     * @return
     */
    private Long bindDiskId(Long globalId, String localId) {
        return bindId(LocalIdType.DISK, globalId, localId);
    }

    /**
     * networkAdapterTemplateIdを関連付けてDBに保存する。
     * @param globalId {@link NetworkAdapterTemplate} のID
     * @param localId Rhevから返却された{@link NetworkAdapterTemplateDto}のID
     * @return {@link NetworkAdapterTemplate} のID
     */
    public long bindNetworkAdapterTemplateId(Long globalId, String localId) {
        return bindId(LocalIdType.NETWORK_ADAPTER_TEMPLATE, globalId, localId);
    }

    /**
     * diskTemplateIdを関連付けてDBに保存する。
     * @param globalId
     * @param diskId
     * @return
     */
    private Long bindDiskTemplateId(Long globalId, String localId) {
        return bindId(LocalIdType.DISK_TEMPLATE, globalId, localId);
    }

    /**
     * templateIdを関連付けてDBに保存する。
     * @param globalId
     * @param templateId
     * @return
     */
    private Long bindTemplateId(Long globalId, String localId) {
        return bindId(LocalIdType.TEMPLATE, globalId, localId);

    }

    /**
     * hostIdを関連付けてDBに保存する。
     * @param globalId
     * @param hostId
     * @return
     */
    private Long bindHostId(Long globalId, String localId) {
        return bindId(LocalIdType.HOST, globalId, localId);
    }

    /**
     * clusterIdを関連付けてDBに保存する。
     * @param globalId
     * @param clusterId
     * @return
     */
    private Long bindClusterId(Long globalId, String localId) {
        return bindId(LocalIdType.CLUSTER, globalId, localId);
    }

    /**
     * networkIdを関連付けてDBに保存する。
     * @param globalId
     * @param networkId
     * @return
     */
    private Long bindNetworkId(Long globalId, String localId) {
        return bindId(LocalIdType.NETWORK, globalId, localId);
    }

    /**
     * storageIdを関連付けてDBに保存する。
     * @param globalId
     * @param storageId
     * @return
     */
    private Long bindStorageId(Long globalId, String localId) {
        return bindId(LocalIdType.STORAGE, globalId, localId);
    }

    /**
     * commandIdを関連付けてDBに保存する。
     * @param globalId
     * @param commandId
     * @return
     */
    public Long bindCommandId(Long globalId, String localId) {
        return bindId(LocalIdType.COMMAND, globalId, localId);
    }

    /**
     * userIdを関連付けてDBに保存する。
     * @param globalId
     * @param userId
     * @return
     */
    public Long bindUserId(Long globalId, String localId) {
        return bindId(LocalIdType.CLOUD_USER, globalId, localId);
    }

    /**
     * Idを関連付けてDBに保存します.
     * @param type IDの種別{@link LocalIdType}から選びます
     * @param globalId vHutの中でのID
     * @param localId Rhevの中でのID
     * @return vHutの中でのID
     */
    protected long bindId(LocalIdType type, long globalId, String localId) {

        LocalId li = new LocalId();
        li.cloudId = cloudId;
        li.type = type;
        li.globalId = globalId;
        li.localId = localId;

        try {

            LocalId selectResult = jdbcManager.from(LocalId.class)
                .where("cloudId = ? and type = ? and globalId = ?", cloudId, type, globalId)
                .getSingleResult();

            if (selectResult == null) {
                li.version = 0L;
                jdbcManager.insert(li)
                    .execute();
            } else {
                li.id = selectResult.id;
                li.version = selectResult.version;
                jdbcManager.update(li)
                    .execute();
            }

            //            // updateできなかった(0が返ってきた)場合にinsertを行う。
            //            int count = jdbcManager.update(li).execute();
            //            if (count == 0) {
            //                jdbcManager.insert(li).execute();
            //            }
            ////            jdbcManager.insert(li).execute();
        } catch (Exception e) {
            throw DriverSQLRuntimeException.newException(e);
        }

        return globalId;

    }

    /**
     * ユーザ情報のアカウントからドメインを除去する。
     * @param cloudUserAccount ユーザアカウント
     * @return ドメインを除去したユーザアカウント
     */
    private String deleteDomainInAccount(String cloudUserAccount) {
        String removedDomainAccount = cloudUserAccount.split("@")[0];
        return removedDomainAccount;

    }

    /**
     * vHutUserIdのリストをCloudUserIdのリストに変換する。
     * @param userIdList
     * @return
     */
    public Object[] convertUserIdListVh2Rh(List<Long> userIdList) {

        List<String> ids = new ArrayList<String>();

        for (long userId : userIdList) {
            ids.add(convertUserIdVh2Rh(userId));
        }
        return ids.toArray();
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
