/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterReservationNames._ClusterReservationNames;
import jp.co.ntts.vhut.entity.PublicIpReservationNames._PublicIpReservationNames;
import jp.co.ntts.vhut.entity.StorageReservationNames._StorageReservationNames;
import jp.co.ntts.vhut.entity.VlanReservationNames._VlanReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Reservation}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ReservationNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
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
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
    }

    /**
     * clusterReservationListのプロパティ名を返します。
     * 
     * @return clusterReservationListのプロパティ名
     */
    public static _ClusterReservationNames clusterReservationList() {
        return new _ClusterReservationNames("clusterReservationList");
    }

    /**
     * publicIpReservationListのプロパティ名を返します。
     * 
     * @return publicIpReservationListのプロパティ名
     */
    public static _PublicIpReservationNames publicIpReservationList() {
        return new _PublicIpReservationNames("publicIpReservationList");
    }

    /**
     * storageReservationListのプロパティ名を返します。
     * 
     * @return storageReservationListのプロパティ名
     */
    public static _StorageReservationNames storageReservationList() {
        return new _StorageReservationNames("storageReservationList");
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
    public static class _ReservationNames extends PropertyName<Reservation> {

        /**
         * インスタンスを構築します。
         */
        public _ReservationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ReservationNames(final String name) {
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
        public _ReservationNames(final PropertyName<?> parent, final String name) {
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
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
        }

        /**
         * clusterReservationListのプロパティ名を返します。
         * 
         * @return clusterReservationListのプロパティ名
         */
        public _ClusterReservationNames clusterReservationList() {
            return new _ClusterReservationNames(this, "clusterReservationList");
        }

        /**
         * publicIpReservationListのプロパティ名を返します。
         * 
         * @return publicIpReservationListのプロパティ名
         */
        public _PublicIpReservationNames publicIpReservationList() {
            return new _PublicIpReservationNames(this, "publicIpReservationList");
        }

        /**
         * storageReservationListのプロパティ名を返します。
         * 
         * @return storageReservationListのプロパティ名
         */
        public _StorageReservationNames storageReservationList() {
            return new _StorageReservationNames(this, "storageReservationList");
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
