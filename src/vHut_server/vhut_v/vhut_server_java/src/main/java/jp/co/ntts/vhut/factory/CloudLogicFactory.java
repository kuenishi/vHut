/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.factory;

import jp.co.ntts.vhut.entity.Cloud;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.logic.ICloudInfraLogic;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.logic.PrivateCloudLogic;
import jp.co.ntts.vhut.logic.PublicCloudLogic;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.SingletonS2Container;

/**
 * <p>クラウドのドライバを生成するファクトリ.
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
public class CloudLogicFactory {

    /**
     * JDBCアクセス.
     */
    public JdbcManager jdbcManager;


    /**
     * クラウドのサービス面での機能を持ったロジックを生成する.
     * @param cloudId クラウドの識別子
     * @return ロジック
     */
    public ICloudServiceLogic newCloudServiceLogic(long cloudId) {
        ICloudServiceLogic logic;

        Cloud cloud = jdbcManager.from(Cloud.class)
            .id(cloudId)
            .getSingleResult();
        if (cloud == null) {
            throw new InputRuntimeException("cloudId", String.format("Cloud(id=%d) was not found", cloudId));
        }
        if (cloud.type.isPrivate()) {
            logic = SingletonS2Container.getComponent(PrivateCloudLogic.class);
        } else {
            logic = SingletonS2Container.getComponent(PublicCloudLogic.class);
        }
        logic.setCloudId(cloudId);
        return logic;
    }

    /**
     * クラウドのインフラ面での機能を持ったロジックを生成する.
     * @param cloudId クラウドの識別子
     * @return ロジック
     */
    public ICloudInfraLogic newCloudInfraLogic(long cloudId) {
        Cloud cloud = jdbcManager.from(Cloud.class)
            .id(cloudId)
            .getSingleResult();
        if (cloud == null) {
            throw new InputRuntimeException("cloudId", String.format("Cloud(id=%d) was not found", cloudId));
        }
        if (cloud.type.isPrivate()) {
            ICloudInfraLogic logic = SingletonS2Container.getComponent(PrivateCloudLogic.class);
            logic.setCloudId(cloudId);
            return logic;
        } else {
            throw new InputRuntimeException("cloudId", String.format("Cloud(id=%d) is public cloud. So it has no ICloudInfraLogic interface.", cloudId));
        }
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
