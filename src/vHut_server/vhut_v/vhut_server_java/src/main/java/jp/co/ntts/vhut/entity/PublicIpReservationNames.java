/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.NetworkAdapterNames._NetworkAdapterNames;
import jp.co.ntts.vhut.entity.ReservationNames._ReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link PublicIpReservation}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class PublicIpReservationNames {

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
     * publicIpのプロパティ名を返します。
     * 
     * @return publicIpのプロパティ名
     */
    public static PropertyName<String> publicIp() {
        return new PropertyName<String>("publicIp");
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
     * networkAdapterIdのプロパティ名を返します。
     * 
     * @return networkAdapterIdのプロパティ名
     */
    public static PropertyName<Long> networkAdapterId() {
        return new PropertyName<Long>("networkAdapterId");
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
     * networkAdapterのプロパティ名を返します。
     * 
     * @return networkAdapterのプロパティ名
     */
    public static _NetworkAdapterNames networkAdapter() {
        return new _NetworkAdapterNames("networkAdapter");
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
    public static class _PublicIpReservationNames extends PropertyName<PublicIpReservation> {

        /**
         * インスタンスを構築します。
         */
        public _PublicIpReservationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _PublicIpReservationNames(final String name) {
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
        public _PublicIpReservationNames(final PropertyName<?> parent, final String name) {
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
         * publicIpのプロパティ名を返します。
         *
         * @return publicIpのプロパティ名
         */
        public PropertyName<String> publicIp() {
            return new PropertyName<String>(this, "publicIp");
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
         * networkAdapterIdのプロパティ名を返します。
         *
         * @return networkAdapterIdのプロパティ名
         */
        public PropertyName<Long> networkAdapterId() {
            return new PropertyName<Long>(this, "networkAdapterId");
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
         * networkAdapterのプロパティ名を返します。
         * 
         * @return networkAdapterのプロパティ名
         */
        public _NetworkAdapterNames networkAdapter() {
            return new _NetworkAdapterNames(this, "networkAdapter");
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
