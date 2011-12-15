/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import java.util.Map;

import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Vm;

/**
 * <p>リソースを消費する主体が持つインターフェース.
 * <br>
 * 以下のEntity/Dtoに実装
 * <ul>
 * <li>{@link Vm}
 * <li>{@link Template}
 * <li>{@link Network}
 * <li>{@link OrderDto}
 * </ul>
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
public interface IResourceConsumer {
    /**
     * @return clusterIdと使用するCPUリソースのMap
     */
    Map<Long, Integer> getCpuResource();

    /**
     * @return clusterIdと使用するメモリリソースのMap
     */
    Map<Long, Integer> getMemoryResource();

    /**
     * @return 使用するストレージリソース(MB)
     */
    Map<Long, Integer> getStorageResource();

    /**
     * @return clusterIdと使用するVLANリソースのMap
     */
    int getVlanResource();

    /**
     * @return 使用する外部IPリソース
     */
    int getPublicIpResource();
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
