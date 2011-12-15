/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev;

import jp.co.ntts.vhut.driver.ValidationRuntimeException;
import jp.co.ntts.vhut.driver.rhev.dto.CloudUserDto;
import jp.co.ntts.vhut.driver.rhev.dto.ClusterDto;
import jp.co.ntts.vhut.driver.rhev.dto.CommandDto;
import jp.co.ntts.vhut.driver.rhev.dto.DiskDto;
import jp.co.ntts.vhut.driver.rhev.dto.HostDto;
import jp.co.ntts.vhut.driver.rhev.dto.NetworkAdapterDto;
import jp.co.ntts.vhut.driver.rhev.dto.NetworkDto;
import jp.co.ntts.vhut.driver.rhev.dto.StorageDto;
import jp.co.ntts.vhut.driver.rhev.dto.TemplateDto;
import jp.co.ntts.vhut.driver.rhev.dto.VmDto;

import org.seasar.framework.log.Logger;

/**
 * <p>
 * RhevDriverのバリデーションクラス。 <br>
 * <p>
 * クラウドコントローラおよびエージェントから受け取った値のバリデーションを行う。
 * 
 *  $Date: 2010-06-29 10:12:31 +0900 (火, 29 6 2010)
 *  $Revision: 949 $
 *  $Author: NTT Software Corporation. $
 */
public class RhevDataValidator {

    // seasarのLoggerインスタンス取得
    private static final Logger logger = Logger.getLogger(RhevDataValidator.class);


    /**
     * RhevDataValidatorのコンストラクタ。
     * 
     * @param
     * @return
     */
    public RhevDataValidator() {

    }

    //    /**
    //     * クラウドコントローラから受け取ったClusterIdのバリデートを行う。
    //     * 
    //     * @param clusterId
    //     *            クラスタのID
    //     * @return
    //     * @throws ValidationRuntimeException
    //     */
    //    public static void validateClusterId(long clusterId) throws ValidationRuntimeException {
    //        if (clusterId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったCommandListのバリデートを行う。
    //     * 
    //     * @param commandList
    //     *            クラスタのID
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateCommandList(List<Command> commandList) throws ValidationRuntimeException {
    //
    //        if (commandList.size() < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったvmのバリデートを行う。
    //     * 
    //     * @param vm
    //     *            クラスタのID
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateVm(Vm vm) throws ValidationRuntimeException {
    //
    //        if (vm.id < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったCommandIdのバリデートを行う。
    //     * 
    //     * @param commandId
    //     *            コマンドID
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateCommandId(long commandId) throws ValidationRuntimeException {
    //
    //        if (commandId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったvmIdのバリデートを行う。
    //     * 
    //     * @param vmId
    //     *            仮想マシンID
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateVmId(long vmId) throws ValidationRuntimeException {
    //
    //        if (vmId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったcpuCoreのバリデートを行う。
    //     * 
    //     * @param cpuCore
    //     *            CPUコア数
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateCpuCore(int cpuCore) throws ValidationRuntimeException {
    //
    //        if (cpuCore < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったmemoryのバリデートを行う。
    //     * 
    //     * @param memory
    //     *            メモリ容量
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateMemory(int memory) throws ValidationRuntimeException {
    //
    //        if (memory < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったnetworkAdapterのバリデートを行う。
    //     * 
    //     * @param networkAdapter
    //     *            クラスタのID
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateNetworkAdapter(NetworkAdapter networkAdapter) throws ValidationRuntimeException {
    //
    //        if (networkAdapter.id < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったnetworkAdapterIdのバリデートを行う。
    //     * 
    //     * @param networkAdapterId
    //     *            ネットワークアダプタID
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateNetworkAdapterId(long networkAdapterId) throws ValidationRuntimeException {
    //
    //        if (networkAdapterId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったdiskのバリデートを行う。
    //     * 
    //     * @param disk
    //     *            ディスク
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateDisk(Disk disk) throws ValidationRuntimeException {
    //
    //        if (disk.id < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったdiskIdのバリデートを行う。
    //     * 
    //     * @param diskId
    //     *            ディスク
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateDiskId(long diskId) throws ValidationRuntimeException {
    //
    //        if (diskId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったvhutUserIdのバリデートを行う。
    //     * 
    //     * @param vhutUserId
    //     *            ディスク
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateVhutUserId(long vhutUserId) throws ValidationRuntimeException {
    //
    //        if (vhutUserId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったtemplateIdのバリデートを行う。
    //     * 
    //     * @param templateId
    //     *            ディスク
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateTemplateId(long templateId) throws ValidationRuntimeException {
    //
    //        if (templateId < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }
    //
    //    /**
    //     * コマンドコントローラから受け取ったtemplateのバリデートを行う。
    //     * 
    //     * @param template
    //     *            ディスク
    //     * @throws ValidationRuntimeException 
    //     */
    //    public static void validateTemplate(Template template) throws ValidationRuntimeException {
    //
    //        if (template.id < 1) {
    //            throw ValidationRuntimeException.newInputException();
    //        }
    //    }

    /**
     * エージェントから受け取ったclusterListのバリデートを行う。
     * 
     * @param clusterList
     *            クラスタのリスト
     * @return
     * @throws ValidationRuntimeException
     */
    public static void validateClusterList(Object[] clusterList) throws ValidationRuntimeException {

        Object[] valueList = (Object[]) clusterList;
        if (valueList.length > 0) {
            ClusterDto value = (ClusterDto) valueList[0];
            if (value.clusterId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.name == null) {
                throw ValidationRuntimeException.newOutputException();
            }
        }

    }

    /**
     * エージェントから受け取ったhostListのバリデートを行う。
     * 
     * @param hostList
     *            ホストのリスト
     * @return
     * @throws ValidationRuntimeException 
     */
    public static void validateHostList(Object hostList) throws ValidationRuntimeException {

        Object[] valueList = (Object[]) hostList;
        if (valueList.length > 0) {
            HostDto value = (HostDto) valueList[0];
            if (value.hostId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.name == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.status == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.cpuCore < 1) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.memory < 1) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.cpuUsage < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.memoryUsage < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.clusterId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
        }
    }

    /**
     * エージェントから受け取ったnetworkListのバリデートを行う。
     * 
     * @param networkList
     *            ネットワークのリスト
     * @return
     * @throws ValidationRuntimeException 
     */
    public static void validateNetworkList(Object networkList) throws ValidationRuntimeException {

        Object[] valueList = (Object[]) networkList;
        if (valueList.length > 0) {
            NetworkDto value = (NetworkDto) valueList[0];
            if (value.networkId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.vlan < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.networkAddress == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.subnet == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.gateway == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.status == null) {
                throw ValidationRuntimeException.newOutputException();
            }
        }
    }

    /**
     * エージェントから受け取ったstorageListのバリデートを行う。
     * 
     * @param storageList
     *            ストレージのリスト
     * @return
     * @throws ValidationRuntimeException 
     */
    public static void validateStorageList(Object storageList) throws ValidationRuntimeException {

        Object[] valueList = (Object[]) storageList;
        if (valueList.length > 0) {
            StorageDto value = (StorageDto) valueList[0];
            if (value.storageId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.name == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.status == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.availableSize < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.commitedSize < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.usedSize < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
        }

    }

    /**
     * エージェントから受け取ったvmListのバリデートを行う。
     * 
     * @param vmList
     *            仮想マシンのリスト
     * @throws ValidationRuntimeException 
     */
    public static void validateVmList(Object vmList) throws ValidationRuntimeException {
        Object[] valueList = (Object[]) vmList;
        if (valueList.length > 0) {
            VmDto value = (VmDto) valueList[0];
            if (value.vmId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.name == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.description == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.cpuCore < 1) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.memory < 1) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.cpuUsage < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.memoryUsage < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.status == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.os == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.templateId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.hostId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.clusterId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            //            if (value.networkAdapterList == null) {
            //                throw ValidationRuntimeException.newOutputException();
            //            }
            //            if (value.diskList == null) {
            //                throw ValidationRuntimeException.newOutputException();
            //            }

        }
    }

    /**
     * エージェントから受け取ったtemplateListのバリデートを行う。
     * 
     * @param templateList
     *            テンプレートのリスト
     * @throws ValidationRuntimeException 
     */
    public static void validateTemplateList(Object templateList) throws ValidationRuntimeException {
        Object[] valueList = (Object[]) templateList;
        if (valueList.length > 0) {
            TemplateDto value = (TemplateDto) valueList[0];
            if (value.templateId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.name == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.description == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.cpuCore < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.memory < 0) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.status == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.os == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.clusterId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.networkAdapterTemplateList == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.diskTemplateList == null) {
                throw ValidationRuntimeException.newOutputException();
            }
        }
    }

    /**
     * エージェントから受け取ったデータCloudUserListのバリデートを行う。
     * 
     * @param cloudUserList Cloudユーザのリスト
     * @throws ValidationRuntimeException 
     */
    public static void validateCloudUserList(Object cloudUserList) throws ValidationRuntimeException {
        Object[] valueList = (Object[]) cloudUserList;
        if (valueList.length > 0) {
            CloudUserDto value = (CloudUserDto) valueList[0];
            if (value.userId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.account == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            //            if (value.lastName == null) {
            //                throw ValidationRuntimeException.newOutputException();
            //            }
            //            if (value.firstName == null) {
            //                throw ValidationRuntimeException.newOutputException();
            //            }
            if (value.email == null) {
                throw ValidationRuntimeException.newOutputException();
            }
        } else {
            throw ValidationRuntimeException.newOutputException();
        }

    }

    /**
     * エージェントから受け取ったCommandListのバリデートを行う。
     * 
     * @param commandList
     *            コマンドのリスト
     * @throws ValidationRuntimeException 
     */
    public static void validateCommandList(Object commandList) throws ValidationRuntimeException {
        Object[] valueList = (Object[]) commandList;
        if (valueList.length > 0) {
            CommandDto value = (CommandDto) valueList[0];
            if (value.commandId == null) {
                throw ValidationRuntimeException.newOutputException();
            }
            if (value.status == null) {
                throw ValidationRuntimeException.newOutputException();
            }

        }
    }

    /**
     * エージェントから受け取ったvmのバリデートを行う。
     * 
     * @param vm
     *            仮想マシン
     * @throws ValidationRuntimeException 
     */
    public static void validateVm(Object[] vm) throws ValidationRuntimeException {

        //        Object[] valueList = (Object[]) cloudUserList;
        //        if (valueList.length > 0) {
        //            CloudUserDto value = (CloudUserDto) valueList[0];
        //            if (value.userId == null) { 

        VmDto value = (VmDto) vm[0];

        if (value.vmId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.name == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.description == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.cpuCore < 1) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.memory < 1) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.cpuUsage < 0) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.memoryUsage < 0) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.status == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.os == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.templateId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.hostId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.clusterId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.networkAdapterList == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        //        if (value.diskList == null) {
        //            throw ValidationRuntimeException.newOutputException();
        //        }
    }

    /**
     * エージェントから受け取ったnetworkAdapterのバリデートを行う。
     * 
     * @param networkAdapter
     *            ネットワークアダプタ
     * @throws ValidationRuntimeException 
     */
    public static void validateNetworkAdapter(Object networkAdapter) throws ValidationRuntimeException {
        Object[] obj = (Object[]) networkAdapter;

        NetworkAdapterDto value = (NetworkAdapterDto) obj[0];
        if (value.networkAdapterId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.name == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.macAddress == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.networkId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
    }

    /**
     * エージェントから受け取ったDiskのバリデートを行う。
     * 
     * @param disk
     *           ディスク
     * @throws ValidationRuntimeException 
     */
    public static void validateDisk(Object disk) throws ValidationRuntimeException {
        Object[] obj = (Object[]) disk;

        DiskDto value = (DiskDto) obj[0];
        if (value.diskId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.size < 0) {
            throw ValidationRuntimeException.newOutputException();
        }
        //        if (value.storageId == null) {
        //            throw ValidationRuntimeException.newOutputException();
        //        }

    }

    /**
     * エージェントから受け取ったtemplateのバリデートを行う。
     * 
     * @param template
     *            テンプレート
     * @throws ValidationRuntimeException 
     */
    public static void validateTemplate(Object template) throws ValidationRuntimeException {
        Object[] obj = (Object[]) template;
        TemplateDto value = (TemplateDto) obj[0];
        if (value.templateId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.name == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.description == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.cpuCore < 1) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.memory < 1) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.status == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.os == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.clusterId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.networkAdapterTemplateList == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.diskTemplateList == null) {
            throw ValidationRuntimeException.newOutputException();
        }
    }

    /**
     * エージェントから受け取ったvhutUserのバリデートを行う。
     * 
     * @param vhutUser vHutuser
     * @throws ValidationRuntimeException 
     */
    public static void validateVhutUser(Object vhutUser) throws ValidationRuntimeException {
        Object[] obj = (Object[]) vhutUser;
        CloudUserDto value = (CloudUserDto) obj[0];
        if (value.userId == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        if (value.account == null) {
            throw ValidationRuntimeException.newOutputException();
        }
        //        if (value.lastName == null) {
        //            throw ValidationRuntimeException.newOutputException();
        //        }
        //        if (value.firstName == null) {
        //            throw ValidationRuntimeException.newOutputException();
        //        }
        if (value.email == null) {
            throw ValidationRuntimeException.newOutputException();
        }
    }

    /**
     * エージェントから受け取ったcommandIdのバリデートを行う。
     * 
     * @param commandId
     *            コマンドID
     * @throws ValidationRuntimeException 
     */
    public static void validateCommandId(String commandId) throws ValidationRuntimeException {
        if (commandId == null) {
            throw ValidationRuntimeException.newOutputException();
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
