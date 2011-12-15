/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.config;

import org.seasar.config.core.config.annotation.Config;
import org.seasar.config.core.config.annotation.ConfigKey;

/**
 * <p>サーバモジュール関連のコンフィグレーション
 * <br>
 * <p>driver.propertiesの値をインスタンスとして提供します.
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
@Config(name = "vhut")
public class DriverConfig {

    /**
     * RHEVのXMLRPCサーバのアドレス
     */
    @ConfigKey(name = "driver.RhevXmlRpcServerAddress", readOnly = true)
    public String xmlRpcServerAddress;

    /**
     * RHEVのXMLRPCサーバのポート
     */
    @ConfigKey(name = "driver.RhevXmlRpcServerPort", readOnly = true)
    public String xmlRpcServerPort;

    /**
     * RHEVエージェントのメソッドget_all_clusters
     */
    @ConfigKey(name = "driver.GET_ALL_CLUSTERS", readOnly = true)
    public String get_all_clusters;

    /**
     * RHEVエージェントのメソッドget_hosts_by_cluster_id
     */
    @ConfigKey(name = "driver.GET_HOSTS_BY_CLUSTER_ID", readOnly = true)
    public String get_hosts_by_cluster_id;

    /**
     * RHEVエージェントのメソッドget_networks_by_cluster_id
     */
    @ConfigKey(name = "driver.GET_NETWORKS_BY_CLUSTER_ID", readOnly = true)
    public String get_networks_by_cluster_id;

    /**
     * RHEVエージェントのメソッドget_all_data_storages
     */
    @ConfigKey(name = "driver.GET_ALL_DATA_STORAGES", readOnly = true)
    public String get_all_data_storages;

    /**
     * RHEVエージェントのメソッドget_vms_by_cluster_id
     */
    @ConfigKey(name = "driver.GET_VMS_BY_CLUSTER_ID", readOnly = true)
    public String get_vms_by_cluster_id;

    /**
     * RHEVエージェントのメソッドget_templates_by_cluster_id
     */
    @ConfigKey(name = "driver.GET_TEMPLATES_BY_CLUSTER_ID", readOnly = true)
    public String get_templates_by_cluster_id;

    /**
     * RHEVエージェントのメソッドget_all_users
     */
    @ConfigKey(name = "driver.GET_ALL_USERS", readOnly = true)
    public String get_all_users;

    /**
     * RHEVエージェントのメソッドget_tasks_by_task_ids
     */
    @ConfigKey(name = "driver.GET_TASKS_BY_TASKIDS", readOnly = true)
    public String get_tasks_by_task_ids;

    /**
     * RHEVエージェントのメソッドcreate_vm
     */
    @ConfigKey(name = "driver.CREATE_VM", readOnly = true)
    public String create_vm;

    /**
     * RHEVエージェントのメソッドdelete_vm
     */
    @ConfigKey(name = "driver.DELETE_VM", readOnly = true)
    public String delete_vm;

    /**
     * RHEVエージェントのメソッドchange_spec
     */
    @ConfigKey(name = "driver.CHANGE_SPEC", readOnly = true)
    public String change_spec;

    /**
     * RHEVエージェントのメソッドadd_network_adapter
     */
    @ConfigKey(name = "driver.ADD_NETWORK_ADAPTER", readOnly = true)
    public String add_network_adapter;

    /**
     * RHEVエージェントのメソッドremove_network_adapter
     */
    @ConfigKey(name = "driver.REMOVE_NETWORK_ADAPTER", readOnly = true)
    public String remove_network_adapter;

    /**
     * RHEVエージェントのメソッドadd_disk
     */
    @ConfigKey(name = "driver.ADD_DISK", readOnly = true)
    public String add_disk;

    /**
     * RHEVエージェントのメソッドremove_disk
     */
    @ConfigKey(name = "driver.REMOVE_DISK", readOnly = true)
    public String remove_disk;

    /**
     * RHEVエージェントのメソッドadd_user
     */
    @ConfigKey(name = "driver.ADD_USER", readOnly = true)
    public String add_user;

    /**
     * RHEVエージェントのメソッドremove_user
     */
    @ConfigKey(name = "driver.REMOVE_USER", readOnly = true)
    public String remove_user;

    /**
     * RHEVエージェントのメソッドstart_vm
     */
    @ConfigKey(name = "driver.START_VM", readOnly = true)
    public String start_vm;

    /**
     * RHEVエージェントのメソッドstop_vm
     */
    @ConfigKey(name = "driver.STOP_VM", readOnly = true)
    public String stop_vm;

    /**
     * RHEVエージェントのメソッドshutdown_vm
     */
    @ConfigKey(name = "driver.SHUTDOWN_VM", readOnly = true)
    public String shutdown_vm;

    /**
     * RHEVエージェントのメソッドcreate_template
     */
    @ConfigKey(name = "driver.CREATE_TEMPLATE", readOnly = true)
    public String create_template;

    /**
     * RHEVエージェントのメソッドdelete_template
     */
    @ConfigKey(name = "driver.DELETE_TEMPLATE", readOnly = true)
    public String delete_template;

    /**
     * RHEVエージェントのメソッドchange_Users_Password
     */
    @ConfigKey(name = "driver.CHANGE_USERS_PASSWORD", readOnly = true)
    public String change_users_password;

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
