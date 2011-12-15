/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroupNames._ApplicationInstanceSecurityGroupNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceVmNames._ApplicationInstanceVmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ApplicationInstanceVmSecurityGroupMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ApplicationInstanceVmSecurityGroupMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * applicationInstanceVmIdのプロパティ名を返します。
     * 
     * @return applicationInstanceVmIdのプロパティ名
     */
    public static PropertyName<Long> applicationInstanceVmId() {
        return new PropertyName<Long>("applicationInstanceVmId");
    }

    /**
     * applicationInstanceSecurityGroupIdのプロパティ名を返します。
     * 
     * @return applicationInstanceSecurityGroupIdのプロパティ名
     */
    public static PropertyName<Long> applicationInstanceSecurityGroupId() {
        return new PropertyName<Long>("applicationInstanceSecurityGroupId");
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
     * applicationInstanceVmのプロパティ名を返します。
     * 
     * @return applicationInstanceVmのプロパティ名
     */
    public static _ApplicationInstanceVmNames applicationInstanceVm() {
        return new _ApplicationInstanceVmNames("applicationInstanceVm");
    }

    /**
     * applicationInstanceSecurityGroupのプロパティ名を返します。
     * 
     * @return applicationInstanceSecurityGroupのプロパティ名
     */
    public static _ApplicationInstanceSecurityGroupNames applicationInstanceSecurityGroup() {
        return new _ApplicationInstanceSecurityGroupNames("applicationInstanceSecurityGroup");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ApplicationInstanceVmSecurityGroupMapNames extends PropertyName<ApplicationInstanceVmSecurityGroupMap> {

        /**
         * インスタンスを構築します。
         */
        public _ApplicationInstanceVmSecurityGroupMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ApplicationInstanceVmSecurityGroupMapNames(final String name) {
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
        public _ApplicationInstanceVmSecurityGroupMapNames(final PropertyName<?> parent, final String name) {
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
         * applicationInstanceVmIdのプロパティ名を返します。
         *
         * @return applicationInstanceVmIdのプロパティ名
         */
        public PropertyName<Long> applicationInstanceVmId() {
            return new PropertyName<Long>(this, "applicationInstanceVmId");
        }

        /**
         * applicationInstanceSecurityGroupIdのプロパティ名を返します。
         *
         * @return applicationInstanceSecurityGroupIdのプロパティ名
         */
        public PropertyName<Long> applicationInstanceSecurityGroupId() {
            return new PropertyName<Long>(this, "applicationInstanceSecurityGroupId");
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
         * applicationInstanceVmのプロパティ名を返します。
         * 
         * @return applicationInstanceVmのプロパティ名
         */
        public _ApplicationInstanceVmNames applicationInstanceVm() {
            return new _ApplicationInstanceVmNames(this, "applicationInstanceVm");
        }

        /**
         * applicationInstanceSecurityGroupのプロパティ名を返します。
         * 
         * @return applicationInstanceSecurityGroupのプロパティ名
         */
        public _ApplicationInstanceSecurityGroupNames applicationInstanceSecurityGroup() {
            return new _ApplicationInstanceSecurityGroupNames(this, "applicationInstanceSecurityGroup");
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
