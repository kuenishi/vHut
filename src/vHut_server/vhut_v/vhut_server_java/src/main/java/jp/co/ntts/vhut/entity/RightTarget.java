/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * 権限の中の対象を規定します.
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
public enum RightTarget {

    /** 0.基本情報[トップ] */
    BASIC,
    /** 1.Application[コンテンツ] */
    APP,
    /** 2.ReleasedApplication[リリース] */
    RAPP,
    /** 3.ApplicationInstanceGroup[研修] */
    AIG,
    /** 4.ApplicationInstance[研修環境] */
    AI,
    /** 5.BaseTemplate[テンプレート] */
    TEMPLATE,
    /** 7.User[ユーザ] */
    USER,
    /** 6.Role[ロール] */
    ROLE,
    /** 8.Management[管理] */
    MANAGEMENT,
    /** 9.Configuration[設定] */
    CONFIGURATION,

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
