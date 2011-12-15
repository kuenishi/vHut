/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroupNames._ApplicationInstanceGroupNames;
import jp.co.ntts.vhut.entity.ApplicationInstanceNames._ApplicationInstanceNames;
import jp.co.ntts.vhut.entity.ApplicationNames._ApplicationNames;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMapNames._VhutUserCloudUserMapNames;
import jp.co.ntts.vhut.entity.VhutUserRoleMapNames._VhutUserRoleMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link VhutUser}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class VhutUserNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * accountのプロパティ名を返します。
     * 
     * @return accountのプロパティ名
     */
    public static PropertyName<String> account() {
        return new PropertyName<String>("account");
    }

    /**
     * firstNameのプロパティ名を返します。
     * 
     * @return firstNameのプロパティ名
     */
    public static PropertyName<String> firstName() {
        return new PropertyName<String>("firstName");
    }

    /**
     * lastNameのプロパティ名を返します。
     * 
     * @return lastNameのプロパティ名
     */
    public static PropertyName<String> lastName() {
        return new PropertyName<String>("lastName");
    }

    /**
     * emailのプロパティ名を返します。
     * 
     * @return emailのプロパティ名
     */
    public static PropertyName<String> email() {
        return new PropertyName<String>("email");
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
     * sysLockのプロパティ名を返します。
     * 
     * @return sysLockのプロパティ名
     */
    public static PropertyName<Boolean> sysLock() {
        return new PropertyName<Boolean>("sysLock");
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
     * applicationListのプロパティ名を返します。
     * 
     * @return applicationListのプロパティ名
     */
    public static _ApplicationNames applicationList() {
        return new _ApplicationNames("applicationList");
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
     * applicationInstanceGroupListのプロパティ名を返します。
     * 
     * @return applicationInstanceGroupListのプロパティ名
     */
    public static _ApplicationInstanceGroupNames applicationInstanceGroupList() {
        return new _ApplicationInstanceGroupNames("applicationInstanceGroupList");
    }

    /**
     * vhutUserCloudUserMapListのプロパティ名を返します。
     * 
     * @return vhutUserCloudUserMapListのプロパティ名
     */
    public static _VhutUserCloudUserMapNames vhutUserCloudUserMapList() {
        return new _VhutUserCloudUserMapNames("vhutUserCloudUserMapList");
    }

    /**
     * vhutUserRoleMapListのプロパティ名を返します。
     * 
     * @return vhutUserRoleMapListのプロパティ名
     */
    public static _VhutUserRoleMapNames vhutUserRoleMapList() {
        return new _VhutUserRoleMapNames("vhutUserRoleMapList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _VhutUserNames extends PropertyName<VhutUser> {

        /**
         * インスタンスを構築します。
         */
        public _VhutUserNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _VhutUserNames(final String name) {
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
        public _VhutUserNames(final PropertyName<?> parent, final String name) {
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
         * accountのプロパティ名を返します。
         *
         * @return accountのプロパティ名
         */
        public PropertyName<String> account() {
            return new PropertyName<String>(this, "account");
        }

        /**
         * firstNameのプロパティ名を返します。
         *
         * @return firstNameのプロパティ名
         */
        public PropertyName<String> firstName() {
            return new PropertyName<String>(this, "firstName");
        }

        /**
         * lastNameのプロパティ名を返します。
         *
         * @return lastNameのプロパティ名
         */
        public PropertyName<String> lastName() {
            return new PropertyName<String>(this, "lastName");
        }

        /**
         * emailのプロパティ名を返します。
         *
         * @return emailのプロパティ名
         */
        public PropertyName<String> email() {
            return new PropertyName<String>(this, "email");
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
         * sysLockのプロパティ名を返します。
         *
         * @return sysLockのプロパティ名
         */
        public PropertyName<Boolean> sysLock() {
            return new PropertyName<Boolean>(this, "sysLock");
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
         * applicationListのプロパティ名を返します。
         * 
         * @return applicationListのプロパティ名
         */
        public _ApplicationNames applicationList() {
            return new _ApplicationNames(this, "applicationList");
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
         * applicationInstanceGroupListのプロパティ名を返します。
         * 
         * @return applicationInstanceGroupListのプロパティ名
         */
        public _ApplicationInstanceGroupNames applicationInstanceGroupList() {
            return new _ApplicationInstanceGroupNames(this, "applicationInstanceGroupList");
        }

        /**
         * vhutUserCloudUserMapListのプロパティ名を返します。
         * 
         * @return vhutUserCloudUserMapListのプロパティ名
         */
        public _VhutUserCloudUserMapNames vhutUserCloudUserMapList() {
            return new _VhutUserCloudUserMapNames(this, "vhutUserCloudUserMapList");
        }

        /**
         * vhutUserRoleMapListのプロパティ名を返します。
         * 
         * @return vhutUserRoleMapListのプロパティ名
         */
        public _VhutUserRoleMapNames vhutUserRoleMapList() {
            return new _VhutUserRoleMapNames(this, "vhutUserRoleMapList");
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
