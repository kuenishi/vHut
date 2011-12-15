/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.NetworkAdapterNames._NetworkAdapterNames;
import jp.co.ntts.vhut.entity.NetworkNames._NetworkNames;
import jp.co.ntts.vhut.entity.VlanReservationNames._VlanReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link SecurityGroup}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class SecurityGroupNames {

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
     * nameのプロパティ名を返します。
     * 
     * @return nameのプロパティ名
     */
    public static PropertyName<String> name() {
        return new PropertyName<String>("name");
    }

    /**
     * networkIdのプロパティ名を返します。
     * 
     * @return networkIdのプロパティ名
     */
    public static PropertyName<Long> networkId() {
        return new PropertyName<Long>("networkId");
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
     * networkのプロパティ名を返します。
     * 
     * @return networkのプロパティ名
     */
    public static _NetworkNames network() {
        return new _NetworkNames("network");
    }

    /**
     * networkAdapterListのプロパティ名を返します。
     * 
     * @return networkAdapterListのプロパティ名
     */
    public static _NetworkAdapterNames networkAdapterList() {
        return new _NetworkAdapterNames("networkAdapterList");
    }

    /**
     * vlanReservationListのプロパティ名を返します。
     * 
     * @return vlanReservationListのプロパティ名
     */
    public static _VlanReservationNames vlanReservationList() {
        return new _VlanReservationNames("vlanReservationList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _SecurityGroupNames extends PropertyName<SecurityGroup> {

        /**
         * インスタンスを構築します。
         */
        public _SecurityGroupNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _SecurityGroupNames(final String name) {
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
        public _SecurityGroupNames(final PropertyName<?> parent, final String name) {
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
         * nameのプロパティ名を返します。
         *
         * @return nameのプロパティ名
         */
        public PropertyName<String> name() {
            return new PropertyName<String>(this, "name");
        }

        /**
         * networkIdのプロパティ名を返します。
         *
         * @return networkIdのプロパティ名
         */
        public PropertyName<Long> networkId() {
            return new PropertyName<Long>(this, "networkId");
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
         * networkのプロパティ名を返します。
         * 
         * @return networkのプロパティ名
         */
        public _NetworkNames network() {
            return new _NetworkNames(this, "network");
        }

        /**
         * networkAdapterListのプロパティ名を返します。
         * 
         * @return networkAdapterListのプロパティ名
         */
        public _NetworkAdapterNames networkAdapterList() {
            return new _NetworkAdapterNames(this, "networkAdapterList");
        }

        /**
         * vlanReservationListのプロパティ名を返します。
         * 
         * @return vlanReservationListのプロパティ名
         */
        public _VlanReservationNames vlanReservationList() {
            return new _VlanReservationNames(this, "vlanReservationList");
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
