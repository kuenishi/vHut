/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * 権限の中の動作を規定します.
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
public enum RightAction {

    /** 0.参照する. */
    READ,
    /** 1.VMを起動停止する. */
    POWER,
    /** 2.VMの画面を取得する. */
    DISPLAY,
    /** 3.コマンドを制御する. */
    COMMAND,
    /** 4.起動可能状態を変更する. */
    STATUS,
    /** 5.起動可能期間を更新する. */
    TERM,
    /** 6.更新する. */
    UPDATE,
    /** 7.作成する. */
    CREATE,
    /** 8.削除する. */
    DELETE,
    /** 9.再作成する. */
    REBUILD,

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
