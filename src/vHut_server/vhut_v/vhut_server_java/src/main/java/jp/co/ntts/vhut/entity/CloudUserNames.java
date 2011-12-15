/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.VmCloudUserMapNames._VmCloudUserMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link CloudUser}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class CloudUserNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
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
     * conflictIdのプロパティ名を返します。
     * 
     * @return conflictIdのプロパティ名
     */
    public static PropertyName<Long> conflictId() {
        return new PropertyName<Long>("conflictId");
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
     * conflictのプロパティ名を返します。
     * 
     * @return conflictのプロパティ名
     */
    public static _ConflictNames conflict() {
        return new _ConflictNames("conflict");
    }

    /**
     * vmCloudUserMapListのプロパティ名を返します。
     * 
     * @return vmCloudUserMapListのプロパティ名
     */
    public static _VmCloudUserMapNames vmCloudUserMapList() {
        return new _VmCloudUserMapNames("vmCloudUserMapList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _CloudUserNames extends PropertyName<CloudUser> {

        /**
         * インスタンスを構築します。
         */
        public _CloudUserNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _CloudUserNames(final String name) {
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
        public _CloudUserNames(final PropertyName<?> parent, final String name) {
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
         * cloudIdのプロパティ名を返します。
         *
         * @return cloudIdのプロパティ名
         */
        public PropertyName<Long> cloudId() {
            return new PropertyName<Long>(this, "cloudId");
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
         * conflictIdのプロパティ名を返します。
         *
         * @return conflictIdのプロパティ名
         */
        public PropertyName<Long> conflictId() {
            return new PropertyName<Long>(this, "conflictId");
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
         * conflictのプロパティ名を返します。
         * 
         * @return conflictのプロパティ名
         */
        public _ConflictNames conflict() {
            return new _ConflictNames(this, "conflict");
        }

        /**
         * vmCloudUserMapListのプロパティ名を返します。
         * 
         * @return vmCloudUserMapListのプロパティ名
         */
        public _VmCloudUserMapNames vmCloudUserMapList() {
            return new _VmCloudUserMapNames(this, "vmCloudUserMapList");
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
