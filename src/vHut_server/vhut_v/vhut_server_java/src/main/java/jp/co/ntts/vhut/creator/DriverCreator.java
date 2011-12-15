/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * <p>driverをSMART deployに対応させるためのカスタマイザ.
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
public class DriverCreator extends ComponentCreatorImpl {

    private static final String NAME_SUFFIX_DRIVER = "Driver";


    /**
     * @param namingConvention
     */
    public DriverCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(NAME_SUFFIX_DRIVER);
        setInstanceDef(InstanceDefFactory.PROTOTYPE);
        setExternalBinding(false);
        setEnableAbstract(false);
        setEnableInterface(false);
    }

    /**
     * {@link ComponentCustomizer}を返します.
     *
     * @return コンポーネントカスタマイザ
     */
    public ComponentCustomizer getDriverCustomizer() {
        return getCustomizer();
    }

    /**
     * {@link ComponentCustomizer}を設定します。
     *
     * @param customizer コンポーネントカスタマイザ
     */
    public void setDriverCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
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
