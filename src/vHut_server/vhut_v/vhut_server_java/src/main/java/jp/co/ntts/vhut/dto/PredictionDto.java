/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.dto;

import java.sql.Timestamp;

/**
 * <p>利用予測のデータモデルクラス.
 * <br>
 * <p>
 * @version 1.0.0
 * @author NTT Software Corporation.
 * <p>
 * 
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class PredictionDto {

    /**
     * 開始日時
     */
    public Timestamp startTime;

    /**
     * 終了日時
     */
    public Timestamp endTime;

    /**
     * CPU使用率の配列
     */
    public int[] usedCpuCore;

    /**
     * メモリ使用率の配列
     */
    public int[] usedMemory;

    /**
     * ストレージ使用率の配列
     */
    public int[] usedStorage;

    /**
     * VLAN使用率
     */
    public int[] usedVlan;

    /**
     * パブリックIP使用率
     */
    public int[] usedPublicIp;

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
