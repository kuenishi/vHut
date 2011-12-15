/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroupNames._ApplicationSecurityGroupNames;
import jp.co.ntts.vhut.entity.ApplicationVmNames._ApplicationVmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ApplicationVmSecurityGroupMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ApplicationVmSecurityGroupMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * applicationVmIdのプロパティ名を返します。
     * 
     * @return applicationVmIdのプロパティ名
     */
    public static PropertyName<Long> applicationVmId() {
        return new PropertyName<Long>("applicationVmId");
    }

    /**
     * applicationSecurityGroupIdのプロパティ名を返します。
     * 
     * @return applicationSecurityGroupIdのプロパティ名
     */
    public static PropertyName<Long> applicationSecurityGroupId() {
        return new PropertyName<Long>("applicationSecurityGroupId");
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
     * applicationVmのプロパティ名を返します。
     * 
     * @return applicationVmのプロパティ名
     */
    public static _ApplicationVmNames applicationVm() {
        return new _ApplicationVmNames("applicationVm");
    }

    /**
     * applicationSecurityGroupのプロパティ名を返します。
     * 
     * @return applicationSecurityGroupのプロパティ名
     */
    public static _ApplicationSecurityGroupNames applicationSecurityGroup() {
        return new _ApplicationSecurityGroupNames("applicationSecurityGroup");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ApplicationVmSecurityGroupMapNames extends PropertyName<ApplicationVmSecurityGroupMap> {

        /**
         * インスタンスを構築します。
         */
        public _ApplicationVmSecurityGroupMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ApplicationVmSecurityGroupMapNames(final String name) {
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
        public _ApplicationVmSecurityGroupMapNames(final PropertyName<?> parent, final String name) {
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
         * applicationVmIdのプロパティ名を返します。
         *
         * @return applicationVmIdのプロパティ名
         */
        public PropertyName<Long> applicationVmId() {
            return new PropertyName<Long>(this, "applicationVmId");
        }

        /**
         * applicationSecurityGroupIdのプロパティ名を返します。
         *
         * @return applicationSecurityGroupIdのプロパティ名
         */
        public PropertyName<Long> applicationSecurityGroupId() {
            return new PropertyName<Long>(this, "applicationSecurityGroupId");
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
         * applicationVmのプロパティ名を返します。
         * 
         * @return applicationVmのプロパティ名
         */
        public _ApplicationVmNames applicationVm() {
            return new _ApplicationVmNames(this, "applicationVm");
        }

        /**
         * applicationSecurityGroupのプロパティ名を返します。
         * 
         * @return applicationSecurityGroupのプロパティ名
         */
        public _ApplicationSecurityGroupNames applicationSecurityGroup() {
            return new _ApplicationSecurityGroupNames(this, "applicationSecurityGroup");
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
