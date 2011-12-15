/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>クラウドに対するオペレーションを表した列挙型.
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
public enum CommandOperation {
    /**
     * 0.クラスタ一覧取得.
     */
    GET_ALL_CLUSTERS("SGetAllClustersCommand", true)
    /**
     * 1.ホスト一覧取得.
     */
    , GET_HOSTS_BY_CLUSTER_ID("SGetHostsByClusterIdCommand", true)
    /**
     * 2.ネットワーク一覧取得.
     */
    , GET_NETWORKS_BY_CLUSTER_ID("SGetNetworksByClusterIdCommand", true)
    /**
     * 3.ストレージ一覧取得.
     */
    , GET_ALL_DATA_STORAGES("SGetAllDataStoragesCommand", true)
    /**
     * 4.仮想マシン一覧取得.
     */
    , GET_VMS_BY_CLUSTER_ID("SGetVmsByClusterIdCommand", true)
    /**
     * 5.テンプレート一覧取得.
     */
    , GET_TEMPLATES_BY_CLUSTER_ID("SGetTemplatesByClusterIdCommand", true)
    /**
     * 6.ユーザ一覧取得.
     */
    , GET_ALL_USERS("SGetAllUsersCommand", true)
    /**
     * 7.コマンド一覧更新.
     */
    , UPDATE_COMMAND_LIST("SUpdateCommandListCommand")
    /**
     * 8.仮想マシン作成.
     */
    , CREATE_VM_ASYNC("ACreateVmCommand")
    /**
     * 9.仮想マシン削除.
     */
    , DELETE_VM_ASYNC("ADeleteVmCommand")
    /**
     * 10.仮想マシンスペック変更.
     */
    , CHANGE_SPEC("SChangeSpecCommand")
    /**
     * 11.仮想マシンNIC追加.
     */
    , ADD_NETWORK_ADAPTER("SAddNetworkAdapterCommand")
    /**
     * 12.仮想マシンNIC削除.
     */
    , REMOVE_NETWORK_ADAPTER("SRemoveNetworkAdapterCommand")
    /**
     * 13.仮想マシンディスク追加.
     */
    , ADD_DISK_ASYNC("AAddDiskCommand")
    /**
     * 14.仮想マシンディスク削除.
     */
    , REMOVE_DISK_ASYNC("ARemoveDiskCommand")
    /**
     * 15.仮想マシンユーザ追加.
     */
    , ADD_USER("SAddUserCommand")
    /**
     * 16.仮想マシンユーザ削除.
     */
    , REMOVE_USER("SRemoveUserCommand")
    /**
     * 17.仮想マシン起動.
     */
    , START_VM_ASYNC("AStartVmCommand")
    /**
     * 18.仮想マシン停止.
     */
    , STOP_VM_ASYNC("AStopVmCommand")
    /**
     * 19.仮想マシンシャットダウン.
     */
    , SHUTDOWN_VM_ASYNC("AShutdownVmCommand")
    /**
     * 20.テンプレート作成.
     */
    , CREATE_TEMPLATE_ASYNC("ACreateTemplateCommand")
    /**
     * 21.テンプレート削除.
     */
    , DELETE_TEMPLATE_ASYNC("ADeleteTemplateCommand")
    /**
     * 22.DHCPルール追加.
     */
    , ADD_IP("SAddIpCommand")
    /**
     * 23.DHCPルール削除.
     */
    , REMOVE_IP("SRemoveIpCommand")
    /**
     * 24.NATルール追加.
     */
    , ADD_NAT("SAddNatCommand")
    /**
     * 25.NATルール削除.
     */
    , REMOVE_NAT("SRemoveNatCommand")
    /**
     * 26.ネットワークエージェント設定取得.
     */
    , GET_NW_AGENT_CONFIG("SGetNwAgentConfigCommand")
    /**
     * 27.仮想マシン起動（同期）.
     */
    , START_VM_SYNC("SStartVmCommand")
    /**
     * 28.仮想マシン停止（同期）.
     */
    , STOP_VM_SYNC("SStopVmCommand")
    /**
     * 29.仮想マシンシャットダウン（同期）.
     */
    , SHUTDOWN_VM_SYNC("SShutdownVmCommand")
    /**
     * 30.ユーザパスワード変更（同期）.
     */
    , CHANGE_USERS_PASSWORD("SChangeUsersPasswordCommand");

    /**
     * Enumコンストラクタ.
     * @param componentName コマンドのコンポート名称
     */
    CommandOperation(String componentName) {
        this.componentName = componentName;
        this.sync = false;
    }

    /**
     * Enumコンストラクタ.
     * @param componentName コマンドのコンポート名称
     * @param sync true:同期コマンドである
     */
    CommandOperation(String componentName, Boolean sync) {
        this.componentName = componentName;
        this.sync = sync;
    }

    /**
     * @return コマンドのコンポート名称
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @return 同期コマンドである
     */
    public Boolean isSync() {
        return sync;
    }


    private String componentName;

    private Boolean sync = false;

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
