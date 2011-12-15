/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * <p>クラウドインフラのパフォーマンス情報.
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

@Component(instance = InstanceType.PROTOTYPE)
public class CloudInfraPerformanceDto {

    /**
     * 起動中VM数
     */
    public long activeVm;

    /**
     * 起動可能なVM数
     */
    public long commitedVm;

    /**
     * 使用中CPU周波数合計
     */
    public long activeCpuFreq;

    /**
     * 使用可能CPU周波数合計
     */
    public long commitedCpuFreq;

    /**
     * 全CPU周波数合計
     */
    public long totalCpuFreq;

    /**
     * 使用中メモリ容量合計
     */
    public long activeMemory;

    /**
     * 使用可能メモリ容量合計
     */
    public long commitedMemory;

    /**
     * 全メモリ容量合計
     */
    public long totalMemory;

    /**
     * 使用中VLAN数
     */
    public long activeVlan;

    /**
     * 全VLAN数
     */
    public long totalVlan;

    /**
     * 使用済みディスク容量合計
     */
    public long activeStorage;

    /**
     * 全ディスク容量合計
     */
    public long totalStorage;

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
