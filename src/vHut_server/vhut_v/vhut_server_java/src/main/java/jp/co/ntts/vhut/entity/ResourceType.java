/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>vHutで管理されるリソースの種別.
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
public enum ResourceType {

    /**
     * 0.クラスター
     */
    CLUSTER,
    /**
     * 1.ストレージ
     */
    STORAGE,
    /**
     * 2.VLAN
     */
    VLAN,
    /**
     * 3.外部IP
     */
    PUBLIC_IP,
    /**
     * 4.内部IP
     */
    PRIVATE_IP,
    /**
     * 5.MACアドレス
     */
    MAC,

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
