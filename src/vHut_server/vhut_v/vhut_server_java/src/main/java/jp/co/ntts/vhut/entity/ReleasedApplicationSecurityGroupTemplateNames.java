/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ReleasedApplicationNames._ReleasedApplicationNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateSecurityGroupMapNames._ReleasedApplicationTemplateSecurityGroupMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ReleasedApplicationSecurityGroupTemplate}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ReleasedApplicationSecurityGroupTemplateNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * releasedApplicationIdのプロパティ名を返します。
     * 
     * @return releasedApplicationIdのプロパティ名
     */
    public static PropertyName<Long> releasedApplicationId() {
        return new PropertyName<Long>("releasedApplicationId");
    }

    /**
     * nameのプロパティ名を返します。
     * 
     * @return nameのプロパティ名
     */
    public static PropertyName<String> name() {
        return new PropertyName<String>("name");
    }

    /**
     * securityGroupTemplateIdのプロパティ名を返します。
     * 
     * @return securityGroupTemplateIdのプロパティ名
     */
    public static PropertyName<Long> securityGroupTemplateId() {
        return new PropertyName<Long>("securityGroupTemplateId");
    }

    /**
     * cloudIdのプロパティ名を返します。
     * 
     * @return cloudIdのプロパティ名
     */
    public static PropertyName<Long> cloudId() {
        return new PropertyName<Long>("cloudId");
    }

    /**
     * privateIdのプロパティ名を返します。
     * 
     * @return privateIdのプロパティ名
     */
    public static PropertyName<Long> privateId() {
        return new PropertyName<Long>("privateId");
    }

    /**
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
    }

    /**
     * releasedApplicationのプロパティ名を返します。
     * 
     * @return releasedApplicationのプロパティ名
     */
    public static _ReleasedApplicationNames releasedApplication() {
        return new _ReleasedApplicationNames("releasedApplication");
    }

    /**
     * releasedApplicationTemplateSecurityGroupMapListのプロパティ名を返します。
     * 
     * @return releasedApplicationTemplateSecurityGroupMapListのプロパティ名
     */
    public static _ReleasedApplicationTemplateSecurityGroupMapNames releasedApplicationTemplateSecurityGroupMapList() {
        return new _ReleasedApplicationTemplateSecurityGroupMapNames("releasedApplicationTemplateSecurityGroupMapList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ReleasedApplicationSecurityGroupTemplateNames extends PropertyName<ReleasedApplicationSecurityGroupTemplate> {

        /**
         * インスタンスを構築します。
         */
        public _ReleasedApplicationSecurityGroupTemplateNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ReleasedApplicationSecurityGroupTemplateNames(final String name) {
            super(name);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param parent
         *            親
         * @param name
         *            名前
         */
        public _ReleasedApplicationSecurityGroupTemplateNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }

        /**
         * idのプロパティ名を返します。
         *
         * @return idのプロパティ名
         */
        public PropertyName<Long> id() {
            return new PropertyName<Long>(this, "id");
        }

        /**
         * releasedApplicationIdのプロパティ名を返します。
         *
         * @return releasedApplicationIdのプロパティ名
         */
        public PropertyName<Long> releasedApplicationId() {
            return new PropertyName<Long>(this, "releasedApplicationId");
        }

        /**
         * nameのプロパティ名を返します。
         *
         * @return nameのプロパティ名
         */
        public PropertyName<String> name() {
            return new PropertyName<String>(this, "name");
        }

        /**
         * securityGroupTemplateIdのプロパティ名を返します。
         *
         * @return securityGroupTemplateIdのプロパティ名
         */
        public PropertyName<Long> securityGroupTemplateId() {
            return new PropertyName<Long>(this, "securityGroupTemplateId");
        }

        /**
         * cloudIdのプロパティ名を返します。
         *
         * @return cloudIdのプロパティ名
         */
        public PropertyName<Long> cloudId() {
            return new PropertyName<Long>(this, "cloudId");
        }

        /**
         * privateIdのプロパティ名を返します。
         *
         * @return privateIdのプロパティ名
         */
        public PropertyName<Long> privateId() {
            return new PropertyName<Long>(this, "privateId");
        }

        /**
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
        }

        /**
         * releasedApplicationのプロパティ名を返します。
         * 
         * @return releasedApplicationのプロパティ名
         */
        public _ReleasedApplicationNames releasedApplication() {
            return new _ReleasedApplicationNames(this, "releasedApplication");
        }

        /**
         * releasedApplicationTemplateSecurityGroupMapListのプロパティ名を返します。
         * 
         * @return releasedApplicationTemplateSecurityGroupMapListのプロパティ名
         */
        public _ReleasedApplicationTemplateSecurityGroupMapNames releasedApplicationTemplateSecurityGroupMapList() {
            return new _ReleasedApplicationTemplateSecurityGroupMapNames(this, "releasedApplicationTemplateSecurityGroupMapList");
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
