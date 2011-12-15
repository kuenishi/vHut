/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroupNames._ApplicationInstanceGroupNames;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroupNames._ApplicationSecurityGroupNames;
import jp.co.ntts.vhut.entity.ApplicationVmNames._ApplicationVmNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationNames._ReleasedApplicationNames;
import jp.co.ntts.vhut.entity.TermNames._TermNames;
import jp.co.ntts.vhut.entity.VhutUserNames._VhutUserNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Application}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ApplicationNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
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
     * vhutUserIdのプロパティ名を返します。
     * 
     * @return vhutUserIdのプロパティ名
     */
    public static PropertyName<Long> vhutUserId() {
        return new PropertyName<Long>("vhutUserId");
    }

    /**
     * imageUrlのプロパティ名を返します。
     * 
     * @return imageUrlのプロパティ名
     */
    public static PropertyName<String> imageUrl() {
        return new PropertyName<String>("imageUrl");
    }

    /**
     * structureのプロパティ名を返します。
     * 
     * @return structureのプロパティ名
     */
    public static PropertyName<String> structure() {
        return new PropertyName<String>("structure");
    }

    /**
     * reservationIdのプロパティ名を返します。
     * 
     * @return reservationIdのプロパティ名
     */
    public static PropertyName<Long> reservationId() {
        return new PropertyName<Long>("reservationId");
    }

    /**
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<ApplicationStatus> status() {
        return new PropertyName<ApplicationStatus>("status");
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
     * vhutUserのプロパティ名を返します。
     * 
     * @return vhutUserのプロパティ名
     */
    public static _VhutUserNames vhutUser() {
        return new _VhutUserNames("vhutUser");
    }

    /**
     * applicationInstanceGroupListのプロパティ名を返します。
     * 
     * @return applicationInstanceGroupListのプロパティ名
     */
    public static _ApplicationInstanceGroupNames applicationInstanceGroupList() {
        return new _ApplicationInstanceGroupNames("applicationInstanceGroupList");
    }

    /**
     * applicationVmListのプロパティ名を返します。
     * 
     * @return applicationVmListのプロパティ名
     */
    public static _ApplicationVmNames applicationVmList() {
        return new _ApplicationVmNames("applicationVmList");
    }

    /**
     * applicationSecurityGroupListのプロパティ名を返します。
     * 
     * @return applicationSecurityGroupListのプロパティ名
     */
    public static _ApplicationSecurityGroupNames applicationSecurityGroupList() {
        return new _ApplicationSecurityGroupNames("applicationSecurityGroupList");
    }

    /**
     * releasedApplicationListのプロパティ名を返します。
     * 
     * @return releasedApplicationListのプロパティ名
     */
    public static _ReleasedApplicationNames releasedApplicationList() {
        return new _ReleasedApplicationNames("releasedApplicationList");
    }

    /**
     * termListのプロパティ名を返します。
     * 
     * @return termListのプロパティ名
     */
    public static _TermNames termList() {
        return new _TermNames("termList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ApplicationNames extends PropertyName<Application> {

        /**
         * インスタンスを構築します。
         */
        public _ApplicationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ApplicationNames(final String name) {
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
        public _ApplicationNames(final PropertyName<?> parent, final String name) {
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
         * nameのプロパティ名を返します。
         *
         * @return nameのプロパティ名
         */
        public PropertyName<String> name() {
            return new PropertyName<String>(this, "name");
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
         * imageUrlのプロパティ名を返します。
         *
         * @return imageUrlのプロパティ名
         */
        public PropertyName<String> imageUrl() {
            return new PropertyName<String>(this, "imageUrl");
        }

        /**
         * structureのプロパティ名を返します。
         *
         * @return structureのプロパティ名
         */
        public PropertyName<String> structure() {
            return new PropertyName<String>(this, "structure");
        }

        /**
         * reservationIdのプロパティ名を返します。
         *
         * @return reservationIdのプロパティ名
         */
        public PropertyName<Long> reservationId() {
            return new PropertyName<Long>(this, "reservationId");
        }

        /**
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<ApplicationStatus> status() {
            return new PropertyName<ApplicationStatus>(this, "status");
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
         * vhutUserのプロパティ名を返します。
         * 
         * @return vhutUserのプロパティ名
         */
        public _VhutUserNames vhutUser() {
            return new _VhutUserNames(this, "vhutUser");
        }

        /**
         * applicationInstanceGroupListのプロパティ名を返します。
         * 
         * @return applicationInstanceGroupListのプロパティ名
         */
        public _ApplicationInstanceGroupNames applicationInstanceGroupList() {
            return new _ApplicationInstanceGroupNames(this, "applicationInstanceGroupList");
        }

        /**
         * applicationVmListのプロパティ名を返します。
         * 
         * @return applicationVmListのプロパティ名
         */
        public _ApplicationVmNames applicationVmList() {
            return new _ApplicationVmNames(this, "applicationVmList");
        }

        /**
         * applicationSecurityGroupListのプロパティ名を返します。
         * 
         * @return applicationSecurityGroupListのプロパティ名
         */
        public _ApplicationSecurityGroupNames applicationSecurityGroupList() {
            return new _ApplicationSecurityGroupNames(this, "applicationSecurityGroupList");
        }

        /**
         * releasedApplicationListのプロパティ名を返します。
         * 
         * @return releasedApplicationListのプロパティ名
         */
        public _ReleasedApplicationNames releasedApplicationList() {
            return new _ReleasedApplicationNames(this, "releasedApplicationList");
        }

        /**
         * termListのプロパティ名を返します。
         * 
         * @return termListのプロパティ名
         */
        public _TermNames termList() {
            return new _TermNames(this, "termList");
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
