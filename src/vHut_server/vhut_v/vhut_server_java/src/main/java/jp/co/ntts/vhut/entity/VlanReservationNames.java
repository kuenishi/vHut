/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.NetworkNames._NetworkNames;
import jp.co.ntts.vhut.entity.ReservationNames._ReservationNames;
import jp.co.ntts.vhut.entity.SecurityGroupNames._SecurityGroupNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link VlanReservation}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class VlanReservationNames {

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
     * reservationIdのプロパティ名を返します。
     * 
     * @return reservationIdのプロパティ名
     */
    public static PropertyName<Long> reservationId() {
        return new PropertyName<Long>("reservationId");
    }

    /**
     * securityGroupIdのプロパティ名を返します。
     * 
     * @return securityGroupIdのプロパティ名
     */
    public static PropertyName<Long> securityGroupId() {
        return new PropertyName<Long>("securityGroupId");
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
     * securityGroupのプロパティ名を返します。
     * 
     * @return securityGroupのプロパティ名
     */
    public static _SecurityGroupNames securityGroup() {
        return new _SecurityGroupNames("securityGroup");
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
     * reservationのプロパティ名を返します。
     * 
     * @return reservationのプロパティ名
     */
    public static _ReservationNames reservation() {
        return new _ReservationNames("reservation");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _VlanReservationNames extends PropertyName<VlanReservation> {

        /**
         * インスタンスを構築します。
         */
        public _VlanReservationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _VlanReservationNames(final String name) {
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
        public _VlanReservationNames(final PropertyName<?> parent, final String name) {
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
         * reservationIdのプロパティ名を返します。
         *
         * @return reservationIdのプロパティ名
         */
        public PropertyName<Long> reservationId() {
            return new PropertyName<Long>(this, "reservationId");
        }

        /**
         * securityGroupIdのプロパティ名を返します。
         *
         * @return securityGroupIdのプロパティ名
         */
        public PropertyName<Long> securityGroupId() {
            return new PropertyName<Long>(this, "securityGroupId");
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
         * securityGroupのプロパティ名を返します。
         * 
         * @return securityGroupのプロパティ名
         */
        public _SecurityGroupNames securityGroup() {
            return new _SecurityGroupNames(this, "securityGroup");
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
         * reservationのプロパティ名を返します。
         * 
         * @return reservationのプロパティ名
         */
        public _ReservationNames reservation() {
            return new _ReservationNames(this, "reservation");
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
