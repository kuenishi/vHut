/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>{@link Command#status}の値を定義します.
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
public enum CommandStatus {
    /**
     * 0.実行待ち
     */
    WAITING
    /**
     * 1.実行中
     */
    , EXECUTING
    /**
     * 2.正常終了
     */
    , SUCCESS
    /**
     * 3.異常終了
     */
    , ERROR
    /**
     * 4.キャンセル済み
     */
    , CANCELED
    /**
     * 5.状態不明
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
