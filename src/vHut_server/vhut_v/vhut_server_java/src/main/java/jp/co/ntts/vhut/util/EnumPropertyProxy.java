/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.InitMethod;

import flex.messaging.io.BeanProxy;
import flex.messaging.io.PropertyProxyRegistry;

/**
 * BlazeDSでEnumをStringでなく独自型で返送するための変換ロジック.
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

public class EnumPropertyProxy extends BeanProxy {

    /**
     * シリアル
     */
    private static final long serialVersionUID = -4522152354684223059L;


    /**
     * コンストラクタ.
     */
    public EnumPropertyProxy() {
        super();
    }

    /////////////////////////////////////////////
    // Registration
    /////////////////////////////////////////////
    /**
     * プロキシとして登録します.
     */
    @InitMethod
    public void registerPropertyProxy() {
        PropertyProxyRegistry.getRegistry()
            .register(Enum.class, new EnumPropertyProxy());
    }

    /////////////////////////////////////////////
    // Serialization
    /////////////////////////////////////////////

    @Override
    public String getAlias(final Object aInstance) {
        return super.getClassName(aInstance);
    }

    @Override
    public List getPropertyNames(final Object aInstance) {
        final List<String> propertyNames = new ArrayList(1);
        propertyNames.add("name");
        return propertyNames;
    }

    @Override
    public Class getType(final Object aInstance, final String aPropertyName) {
        if ("name".equals(aPropertyName)) {
            return String.class;
        }
        return null;
    }

    @Override
    public Object getValue(final Object aInstance, final String aPropertyName) {
        if ("name".equals(aPropertyName) && aInstance instanceof Enum) {
            final Enum enumValue = (Enum) aInstance;
            return enumValue.toString();
        }
        return null;
    }

    /////////////////////////////////////////////
    // Deserialization
    /////////////////////////////////////////////

    @Override
    public Object createInstance(String className) {
        final Map<String, String> tempInstance = new HashMap<String, String>(2);
        tempInstance.put("className", className);
        return tempInstance;
    }

    @Override
    public void setValue(java.lang.Object o, java.lang.String s, java.lang.Object o1) {
        final Map<String, String> tempInstance = (Map<String, String>) o;
        tempInstance.put("name", (String) o1);
    }

    @Override
    public Object instanceComplete(java.lang.Object o) {
        final Map<String, String> tempInstance = (Map<String, String>) o;
        final String className = tempInstance.get("className");
        final String name = tempInstance.get("name");
        final Class<Enum<?>> enumClass = getClassFromClassName(className);

        for (final Enum<?> constant : enumClass.getEnumConstants()) {
            if (constant.toString()
                .equals(name)) {
                return constant;
            }
        }
        return null;
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
