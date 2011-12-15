/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.RoleNames._RoleNames;
import jp.co.ntts.vhut.entity.VhutUserNames._VhutUserNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link VhutUserRoleMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class VhutUserRoleMapNames {

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
     * roleIdのプロパティ名を返します。
     * 
     * @return roleIdのプロパティ名
     */
    public static PropertyName<Long> roleId() {
        return new PropertyName<Long>("roleId");
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
     * roleのプロパティ名を返します。
     * 
     * @return roleのプロパティ名
     */
    public static _RoleNames role() {
        return new _RoleNames("role");
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
    public static class _VhutUserRoleMapNames extends PropertyName<VhutUserRoleMap> {

        /**
         * インスタンスを構築します。
         */
        public _VhutUserRoleMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _VhutUserRoleMapNames(final String name) {
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
        public _VhutUserRoleMapNames(final PropertyName<?> parent, final String name) {
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
         * roleIdのプロパティ名を返します。
         *
         * @return roleIdのプロパティ名
         */
        public PropertyName<Long> roleId() {
            return new PropertyName<Long>(this, "roleId");
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
         * roleのプロパティ名を返します。
         * 
         * @return roleのプロパティ名
         */
        public _RoleNames role() {
            return new _RoleNames(this, "role");
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
