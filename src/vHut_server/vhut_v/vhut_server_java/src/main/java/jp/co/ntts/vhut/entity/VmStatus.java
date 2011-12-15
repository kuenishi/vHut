/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>仮想マシンの状態です.
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
public enum VmStatus {

    /**
     * 0.作成中
     */
    DEVELOPPING
    /**
     * 1.停止状態
     */
    , DOWN
    /**
     * 2.起動動作中
     */
    , POWERING_UP
    /**
     * 3.起動状態
     */
    , UP
    /**
     * 一時的利用不可能状態.
     * 4.サスペンド、ポーズ、マイグレーション中など。
     */
    , UNAVAILABLE
    /**
     * 5.終了動作中
     */
    , SHUTTING_DOWN
    /**
     * 6.エラー発生中
     */
    , ERROR
    /**
     * 7.状態不明
     */
    , UNKNOWN

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
