/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationNames._ApplicationNames;
import jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMapNames._ApplicationVmSecurityGroupMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ApplicationSecurityGroup}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ApplicationSecurityGroupNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * applicationIdのプロパティ名を返します。
     * 
     * @return applicationIdのプロパティ名
     */
    public static PropertyName<Long> applicationId() {
        return new PropertyName<Long>("applicationId");
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
     * securityGroupIdのプロパティ名を返します。
     * 
     * @return securityGroupIdのプロパティ名
     */
    public static PropertyName<Long> securityGroupId() {
        return new PropertyName<Long>("securityGroupId");
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
     * applicationのプロパティ名を返します。
     * 
     * @return applicationのプロパティ名
     */
    public static _ApplicationNames application() {
        return new _ApplicationNames("application");
    }

    /**
     * applicationVmSecurityGroupMapListのプロパティ名を返します。
     * 
     * @return applicationVmSecurityGroupMapListのプロパティ名
     */
    public static _ApplicationVmSecurityGroupMapNames applicationVmSecurityGroupMapList() {
        return new _ApplicationVmSecurityGroupMapNames("applicationVmSecurityGroupMapList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ApplicationSecurityGroupNames extends PropertyName<ApplicationSecurityGroup> {

        /**
         * インスタンスを構築します。
         */
        public _ApplicationSecurityGroupNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ApplicationSecurityGroupNames(final String name) {
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
        public _ApplicationSecurityGroupNames(final PropertyName<?> parent, final String name) {
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
         * applicationIdのプロパティ名を返します。
         *
         * @return applicationIdのプロパティ名
         */
        public PropertyName<Long> applicationId() {
            return new PropertyName<Long>(this, "applicationId");
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
         * securityGroupIdのプロパティ名を返します。
         *
         * @return securityGroupIdのプロパティ名
         */
        public PropertyName<Long> securityGroupId() {
            return new PropertyName<Long>(this, "securityGroupId");
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
         * applicationのプロパティ名を返します。
         * 
         * @return applicationのプロパティ名
         */
        public _ApplicationNames application() {
            return new _ApplicationNames(this, "application");
        }

        /**
         * applicationVmSecurityGroupMapListのプロパティ名を返します。
         * 
         * @return applicationVmSecurityGroupMapListのプロパティ名
         */
        public _ApplicationVmSecurityGroupMapNames applicationVmSecurityGroupMapList() {
            return new _ApplicationVmSecurityGroupMapNames(this, "applicationVmSecurityGroupMapList");
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
