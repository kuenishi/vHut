/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.VhutUserDto;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudInfraLogic;

import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>コンフィグレーションサービスのクラス.
 * <br>
 * <p>
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
public class ConfigurationService {

    /**
     * CloudLogicを取得するためのファクトリ
     */
    public CloudLogicFactory cloudLogicFactory;

    /**
     * サービス関連の設定
     */
    public ServiceConfig serviceConfig;

    /**
     * ユーザ関連のセッションオブジェクト
     */
    public VhutUserDto vhutUserDto;

    /**
     * クラウドの識別子
     */
    protected long cloudId = 1;

    /**
     * クラウドロジックのインフラ面
     */
    protected ICloudInfraLogic cloudInfraLogic;


    @InitMethod
    public void init() {
        cloudInfraLogic = cloudLogicFactory.newCloudInfraLogic(cloudId);
    }

    /**
     * <p>サービス設定取得.
     * <br>
     *
     * @param なし
     * @return ServiceConfig
     */
    @Auth(right = Right.READ_SYS_CONFIGURATION)
    public ServiceConfig getServiceConfiguration() {

        return serviceConfig;
    }

    /**
     * <p>クラウド設定取得.
     * <br>
     *
     * @param なし
     * @return CloudServiceConfigurationDto
     */
    @Auth(right = Right.READ_SYS_CONFIGURATION)
    public CloudConfig getCloudConfiguration() {
        return cloudInfraLogic.getCloudConfiguration();
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
