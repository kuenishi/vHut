/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>ローカルIDの種別.
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
public enum LocalIdType {

    /**
     * 0: {@link CloudUser}のIDタイプ
     */
    CLOUD_USER

    /**
     * 1: {@link Command}のIDタイプ
     */
    , COMMAND

    /**
     * 2: {@link Vm}のIDタイプ
     */
    , VM

    /**
     * 3: {@link Template}のIDタイプ
     */
    , TEMPLATE

    /**
     * 4: {@link Host}のIDタイプ
     */
    , HOST

    /**
     * 5: {@link Cluster}のIDタイプ
     */
    , CLUSTER

    /**
     * 6: {@link Network}のIDタイプ
     */
    , NETWORK

    /**
     * 7: {@link Storage}のIDタイプ
     */
    , STORAGE

    /**
     * 8: {@link NetworkAdapter}のIDタイプ
     */
    , NETWORK_ADAPTER

    /**
     * 9: {@link Disk}のIDタイプ
     */
    , DISK

    /**
     * 20: {@link NetworkAdapterTemplate}のIDタイプ
     */
    , NETWORK_ADAPTER_TEMPLATE

    /**
     * 21: {@link DiskTemplate}のIDタイプ
     */
    , DISK_TEMPLATE

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
