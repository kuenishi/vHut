/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.VhutUserNames._VhutUserNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link VhutUserCloudUserMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class VhutUserCloudUserMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * cloudUserIdのプロパティ名を返します。
     * 
     * @return cloudUserIdのプロパティ名
     */
    public static PropertyName<Long> cloudUserId() {
        return new PropertyName<Long>("cloudUserId");
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
     * vhutUserIdのプロパティ名を返します。
     * 
     * @return vhutUserIdのプロパティ名
     */
    public static PropertyName<Long> vhutUserId() {
        return new PropertyName<Long>("vhutUserId");
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
     * @author S2JDBC-Gen
     */
    public static class _VhutUserCloudUserMapNames extends PropertyName<VhutUserCloudUserMap> {

        /**
         * インスタンスを構築します。
         */
        public _VhutUserCloudUserMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _VhutUserCloudUserMapNames(final String name) {
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
        public _VhutUserCloudUserMapNames(final PropertyName<?> parent, final String name) {
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
         * cloudUserIdのプロパティ名を返します。
         *
         * @return cloudUserIdのプロパティ名
         */
        public PropertyName<Long> cloudUserId() {
            return new PropertyName<Long>(this, "cloudUserId");
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
         * vhutUserIdのプロパティ名を返します。
         *
         * @return vhutUserIdのプロパティ名
         */
        public PropertyName<Long> vhutUserId() {
            return new PropertyName<Long>(this, "vhutUserId");
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
