/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceNames._ApplicationInstanceNames;
import jp.co.ntts.vhut.entity.ApplicationNames._ApplicationNames;
import jp.co.ntts.vhut.entity.VhutUserNames._VhutUserNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ApplicationInstanceGroup}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ApplicationInstanceGroupNames {

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
     * nameのプロパティ名を返します。
     * 
     * @return nameのプロパティ名
     */
    public static PropertyName<String> name() {
        return new PropertyName<String>("name");
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
     * passwordのプロパティ名を返します。
     * 
     * @return passwordのプロパティ名
     */
    public static PropertyName<String> password() {
        return new PropertyName<String>("password");
    }

    /**
     * startTimeのプロパティ名を返します。
     * 
     * @return startTimeのプロパティ名
     */
    public static PropertyName<Timestamp> startTime() {
        return new PropertyName<Timestamp>("startTime");
    }

    /**
     * endTimeのプロパティ名を返します。
     * 
     * @return endTimeのプロパティ名
     */
    public static PropertyName<Timestamp> endTime() {
        return new PropertyName<Timestamp>("endTime");
    }

    /**
     * deleteTimeのプロパティ名を返します。
     * 
     * @return deleteTimeのプロパティ名
     */
    public static PropertyName<Timestamp> deleteTime() {
        return new PropertyName<Timestamp>("deleteTime");
    }

    /**
     * reservationIdToCreateのプロパティ名を返します。
     * 
     * @return reservationIdToCreateのプロパティ名
     */
    public static PropertyName<Long> reservationIdToCreate() {
        return new PropertyName<Long>("reservationIdToCreate");
    }

    /**
     * reservationIdToStartのプロパティ名を返します。
     * 
     * @return reservationIdToStartのプロパティ名
     */
    public static PropertyName<Long> reservationIdToStart() {
        return new PropertyName<Long>("reservationIdToStart");
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
     * applicationInstanceListのプロパティ名を返します。
     * 
     * @return applicationInstanceListのプロパティ名
     */
    public static _ApplicationInstanceNames applicationInstanceList() {
        return new _ApplicationInstanceNames("applicationInstanceList");
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
     * vhutUserのプロパティ名を返します。
     * 
     * @return vhutUserのプロパティ名
     */
    public static _VhutUserNames vhutUser() {
        return new _VhutUserNames("vhutUser");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ApplicationInstanceGroupNames extends PropertyName<ApplicationInstanceGroup> {

        /**
         * インスタンスを構築します。
         */
        public _ApplicationInstanceGroupNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ApplicationInstanceGroupNames(final String name) {
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
        public _ApplicationInstanceGroupNames(final PropertyName<?> parent, final String name) {
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
         * nameのプロパティ名を返します。
         *
         * @return nameのプロパティ名
         */
        public PropertyName<String> name() {
            return new PropertyName<String>(this, "name");
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
         * passwordのプロパティ名を返します。
         *
         * @return passwordのプロパティ名
         */
        public PropertyName<String> password() {
            return new PropertyName<String>(this, "password");
        }

        /**
         * startTimeのプロパティ名を返します。
         *
         * @return startTimeのプロパティ名
         */
        public PropertyName<Timestamp> startTime() {
            return new PropertyName<Timestamp>(this, "startTime");
        }

        /**
         * endTimeのプロパティ名を返します。
         *
         * @return endTimeのプロパティ名
         */
        public PropertyName<Timestamp> endTime() {
            return new PropertyName<Timestamp>(this, "endTime");
        }

        /**
         * deleteTimeのプロパティ名を返します。
         *
         * @return deleteTimeのプロパティ名
         */
        public PropertyName<Timestamp> deleteTime() {
            return new PropertyName<Timestamp>(this, "deleteTime");
        }

        /**
         * reservationIdToCreateのプロパティ名を返します。
         *
         * @return reservationIdToCreateのプロパティ名
         */
        public PropertyName<Long> reservationIdToCreate() {
            return new PropertyName<Long>(this, "reservationIdToCreate");
        }

        /**
         * reservationIdToStartのプロパティ名を返します。
         *
         * @return reservationIdToStartのプロパティ名
         */
        public PropertyName<Long> reservationIdToStart() {
            return new PropertyName<Long>(this, "reservationIdToStart");
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
         * applicationInstanceListのプロパティ名を返します。
         * 
         * @return applicationInstanceListのプロパティ名
         */
        public _ApplicationInstanceNames applicationInstanceList() {
            return new _ApplicationInstanceNames(this, "applicationInstanceList");
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
         * vhutUserのプロパティ名を返します。
         * 
         * @return vhutUserのプロパティ名
         */
        public _VhutUserNames vhutUser() {
            return new _VhutUserNames(this, "vhutUser");
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
