/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.VhutUserRoleMapNames._VhutUserRoleMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Role}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class RoleNames {

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
     * rightsのプロパティ名を返します。
     * 
     * @return rightsのプロパティ名
     */
    public static PropertyName<byte[]> rights() {
        return new PropertyName<byte[]>("rights");
    }

    /**
     * isDefaultのプロパティ名を返します。
     * 
     * @return isDefaultのプロパティ名
     */
    public static PropertyName<Boolean> isDefault() {
        return new PropertyName<Boolean>("isDefault");
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
    public static class _RoleNames extends PropertyName<Role> {

        /**
         * インスタンスを構築します。
         */
        public _RoleNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _RoleNames(final String name) {
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
        public _RoleNames(final PropertyName<?> parent, final String name) {
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
         * rightsのプロパティ名を返します。
         *
         * @return rightsのプロパティ名
         */
        public PropertyName<byte[]> rights() {
            return new PropertyName<byte[]>(this, "rights");
        }

        /**
         * isDefaultのプロパティ名を返します。
         *
         * @return isDefaultのプロパティ名
         */
        public PropertyName<Boolean> isDefault() {
            return new PropertyName<Boolean>(this, "isDefault");
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
