/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroupNames._ApplicationInstanceGroupNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroupNames._ApplicationInstanceSecurityGroupNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceVmNames._ApplicationInstanceVmNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationNames._ReleasedApplicationNames;
import jp.co.ntts.vhut.entity.VhutUserNames._VhutUserNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ApplicationInstance}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ApplicationInstanceNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * vhutUserIdのプロパティ名を返します。
     * 
     * @return vhutUserIdのプロパティ名
     */
    public static PropertyName<Long> vhutUserId() {
        return new PropertyName<Long>("vhutUserId");
    }

    /**
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<ApplicationInstanceStatus> status() {
        return new PropertyName<ApplicationInstanceStatus>("status");
    }

    /**
     * applicationInstanceGroupIdのプロパティ名を返します。
     * 
     * @return applicationInstanceGroupIdのプロパティ名
     */
    public static PropertyName<Long> applicationInstanceGroupId() {
        return new PropertyName<Long>("applicationInstanceGroupId");
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
     * applicationInstanceGroupのプロパティ名を返します。
     * 
     * @return applicationInstanceGroupのプロパティ名
     */
    public static _ApplicationInstanceGroupNames applicationInstanceGroup() {
        return new _ApplicationInstanceGroupNames("applicationInstanceGroup");
    }

    /**
     * vhutUserのプロパティ名を返します。
     * 
     * @return vhutUserのプロパティ名
     */
    public static _VhutUserNames vhutUser() {
        return new _VhutUserNames("vhutUser");
    }

    /**
     * applicationInstanceVmListのプロパティ名を返します。
     * 
     * @return applicationInstanceVmListのプロパティ名
     */
    public static _ApplicationInstanceVmNames applicationInstanceVmList() {
        return new _ApplicationInstanceVmNames("applicationInstanceVmList");
    }

    /**
     * applicationInstanceSecurityGroupListのプロパティ名を返します。
     * 
     * @return applicationInstanceSecurityGroupListのプロパティ名
     */
    public static _ApplicationInstanceSecurityGroupNames applicationInstanceSecurityGroupList() {
        return new _ApplicationInstanceSecurityGroupNames("applicationInstanceSecurityGroupList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ApplicationInstanceNames extends PropertyName<ApplicationInstance> {

        /**
         * インスタンスを構築します。
         */
        public _ApplicationInstanceNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ApplicationInstanceNames(final String name) {
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
        public _ApplicationInstanceNames(final PropertyName<?> parent, final String name) {
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
         * vhutUserIdのプロパティ名を返します。
         *
         * @return vhutUserIdのプロパティ名
         */
        public PropertyName<Long> vhutUserId() {
            return new PropertyName<Long>(this, "vhutUserId");
        }

        /**
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<ApplicationInstanceStatus> status() {
            return new PropertyName<ApplicationInstanceStatus>(this, "status");
        }

        /**
         * applicationInstanceGroupIdのプロパティ名を返します。
         *
         * @return applicationInstanceGroupIdのプロパティ名
         */
        public PropertyName<Long> applicationInstanceGroupId() {
            return new PropertyName<Long>(this, "applicationInstanceGroupId");
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
         * applicationInstanceGroupのプロパティ名を返します。
         * 
         * @return applicationInstanceGroupのプロパティ名
         */
        public _ApplicationInstanceGroupNames applicationInstanceGroup() {
            return new _ApplicationInstanceGroupNames(this, "applicationInstanceGroup");
        }

        /**
         * vhutUserのプロパティ名を返します。
         * 
         * @return vhutUserのプロパティ名
         */
        public _VhutUserNames vhutUser() {
            return new _VhutUserNames(this, "vhutUser");
        }

        /**
         * applicationInstanceVmListのプロパティ名を返します。
         * 
         * @return applicationInstanceVmListのプロパティ名
         */
        public _ApplicationInstanceVmNames applicationInstanceVmList() {
            return new _ApplicationInstanceVmNames(this, "applicationInstanceVmList");
        }

        /**
         * applicationInstanceSecurityGroupListのプロパティ名を返します。
         * 
         * @return applicationInstanceSecurityGroupListのプロパティ名
         */
        public _ApplicationInstanceSecurityGroupNames applicationInstanceSecurityGroupList() {
            return new _ApplicationInstanceSecurityGroupNames(this, "applicationInstanceSecurityGroupList");
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
