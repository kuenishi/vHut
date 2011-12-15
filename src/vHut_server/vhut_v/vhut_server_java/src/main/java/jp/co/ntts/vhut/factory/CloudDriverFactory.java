/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.factory;

import jp.co.ntts.vhut.driver.IPrivateCloudDriver;
import jp.co.ntts.vhut.driver.IPublicCloudDriver;
import jp.co.ntts.vhut.entity.Cloud;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.exception.InternalRuntimeException;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.SingletonS2Container;

/**
 * <p>クラウドのドライバを生成するファクトリ.
 * <br>
 * <p>将来の拡張ポイント。複数クラウドに対応する際は、このクラスを修正し、
 * {@link Cloud#type}に対応したドライバを返すように変更する。
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
public class CloudDriverFactory {

    /**
     * JDBCアクセス.
     */
    public JdbcManager jdbcManager;


    /**
     * プライベートクラウド用のドライバを取得する.
     * @param cloudId クラウドの識別子
     * @return {@link IPrivateCloudDriver}に準拠したドライバ.
     */
    public IPrivateCloudDriver getPrivateCloudDriver(long cloudId) {
        Cloud cloud = jdbcManager.from(Cloud.class)
            .id(cloudId)
            .getSingleResult();
        if (cloud == null) {
            throw new InputRuntimeException("cloudId", String.format("Cloud(id=%d) was not found", cloudId));
        }
        IPrivateCloudDriver driver;
        switch (cloud.type) {
            case RHEV:
                driver = SingletonS2Container.getComponent("rhevDriver");
                break;
            default:
                throw new InternalRuntimeException(String.format("CloudDriver(type=%s) is not Defined yet.", cloud.type.toString()));
        }
        driver.setCloudId(cloudId);
        return driver;
    }

    /**
     * パブリッククラウド用のドライバを取得する.
     * @param cloudId クラウドの識別子
     * @return {@link IPrivateCloudDriver}に準拠したドライバ.
     */
    public IPublicCloudDriver getPublicCloudDriver(long cloudId) {
        Cloud cloud = jdbcManager.from(Cloud.class)
            .id(cloudId)
            .getSingleResult();
        if (cloud == null) {
            throw new InputRuntimeException("cloudId", String.format("Cloud(id=%d) was not found", cloudId));
        }
        throw new InternalRuntimeException(String.format("CloudDriver(type=%s) is not Defined yet.", cloud.type.toString()));
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
