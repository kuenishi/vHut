/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.Os;

/**
 * <p>内部IPに関する情報.
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
public class IpInfoDto {

    //---------------------------------
    // from cloud
    //---------------------------------

    /** 内部IP（16進数） */
    public String privateIp;

    /** マックアドレス（16進数） */
    public String mac;

    /** 外部IP（16進数） */
    public String publicIp;

    /** ネットワークアダプタID */
    public Long networkAdapterId;

    /** ネットワークアダプタ名称 */
    public String networkAdapterName;

    /** ネットワークID */
    public Long networkId;

    /** ネットワーク名称 */
    public String networkName;

    /** ゲートウェイアドレス（16進数） */
    public String gateway;

    /** ネットワークアドレス（16進数） */
    public String networkAddress;

    /** VLAN */
    public Short vlan;

    /** VMID */
    public Long vmId;

    /** VM名称（仮想化ソフトの認識名称） */
    public String vmName;

    /** OS */
    public Os os;

    //---------------------------------
    // from service
    //---------------------------------

    /** 所有するユーザのID */
    public Long vhutUserId;

    /** 所有するユーザのアカウント */
    public String vhutUserAccount;

    /** 所有するユーザの名 */
    public String vhutUserFirstName;

    /** 所有するユーザの姓 */
    public String vhutUserLastName;

    /** アプリケーションID */
    public Long applicationId;

    /** アプリケーション名称 */
    public String applicationName;

    /** アプリケーションVMID */
    public Long applicationVmId;

    /** アプリケーションVM名称 */
    public String applicationVmName;

    /** アプリケーションインスタンスID */
    public Long applicationInstanceId;

    /** アプリケーションインスタンスVMID */
    public Long applicationInstanceVmId;

    /** アプリケーションインスタンスVM名称 */
    public String applicationInstanceVmName;

    /** アプリケーションインスタンスグループID */
    public Long applicationInstanceGroupId;

    /** アプリケーションインスタンスグループ名称 */
    public String applicationInstanceGroupName;


    /**
     * ApplicationInstanceに含まれるIP情報のリストを作ります.
     * @param ai ApplicationInstance
     * @return IP情報のリスト
     */
    public static List<IpInfoDto> createIpIfoDtos(ApplicationInstance ai) {
        List<IpInfoDto> ipInfoDtos = new ArrayList<IpInfoDto>();
        Map<Long, Network> sgIdNetworkMap = new HashMap<Long, Network>();

        for (ApplicationInstanceSecurityGroup aisg : ai.applicationInstanceSecurityGroupList) {
            if (aisg.securityGroup == null) {
                continue;
            }
            Network network = aisg.securityGroup.network;
            if (network != null) {
                sgIdNetworkMap.put(aisg.securityGroup.id, network);
            }
        }

        for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
            if (aivm.vm == null || aivm.vm.networkAdapterList == null) {
                continue;
            }
            for (NetworkAdapter networkAdapter : aivm.vm.networkAdapterList) {
                IpInfoDto ipInfoDto = new IpInfoDto();
                ipInfoDtos.add(ipInfoDto);
                ipInfoDto.privateIp = networkAdapter.privateIp;
                ipInfoDto.mac = networkAdapter.mac;
                ipInfoDto.publicIp = networkAdapter.publicIp;
                ipInfoDto.networkAdapterId = networkAdapter.id;
                ipInfoDto.networkAdapterName = networkAdapter.name;
                ipInfoDto.vmId = aivm.vm.id;
                ipInfoDto.vmName = aivm.vm.name;
                ipInfoDto.os = aivm.vm.os;
                ipInfoDto.applicationInstanceVmId = aivm.id;
                ipInfoDto.applicationInstanceVmName = aivm.name;
                ipInfoDto.applicationInstanceId = ai.id;
                ipInfoDto.vhutUserId = ai.vhutUser.id;
                ipInfoDto.vhutUserAccount = ai.vhutUser.account;
                ipInfoDto.vhutUserFirstName = ai.vhutUser.firstName;
                ipInfoDto.vhutUserLastName = ai.vhutUser.lastName;

                Network network = sgIdNetworkMap.get(networkAdapter.securityGroupId);
                if (network == null) {
                    continue;
                }
                ipInfoDto.vlan = network.vlan;
                ipInfoDto.gateway = network.gateway;
                ipInfoDto.networkId = network.id;
                ipInfoDto.networkName = network.name;
                ipInfoDto.networkAddress = network.networkAddress;
            }
        }
        return ipInfoDtos;
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
