/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut;

/**
 * <p>vHutのモジュール定義.
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
public enum VhutModule {

    /**
     * Common, 共有モジュール.
     */
    COMM("common_module")
    /**
     * ServiceService, 業務ロジックのフロント.
     */
    , SRVS("service_service")
    /**
     * ServiceTask, 業務ロジックのバッチ.
     */
    , SRVT("service_task")
    /**
     * ServiceCheckTask, 業務ロジックの確認用バッチ.
     */
    , SVCT("service_check_task")
    /**
     * CloudLogic, クラウド管理ロジック.
     */
    , CLDL("cloud_logic")
    /**
     * CloudTask, クラウド管理のバッチ.
     */
    , CLDT("cloud_task")
    /**
     * RHEVエージェント制御用ドライバ.
     */
    , RVDR("rhev_driver")
    /**
     * ネットワークエージェント制御用ドライバ.
     */
    , NWDR("network_driver");

    private String name;


    private VhutModule(String name) {
        this.name = name;
    }

    /**
     * 詳細名称を取得します.
     * @return 詳細な名称
     */
    public String getName() {
        return name;
    }

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
