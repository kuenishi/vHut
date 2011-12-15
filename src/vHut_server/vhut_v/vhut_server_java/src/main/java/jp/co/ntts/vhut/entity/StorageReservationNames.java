/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ReservationNames._ReservationNames;
import jp.co.ntts.vhut.entity.StorageNames._StorageNames;
import jp.co.ntts.vhut.entity.StorageReservationDiskMapNames._StorageReservationDiskMapNames;
import jp.co.ntts.vhut.entity.StorageReservationDiskTemplateMapNames._StorageReservationDiskTemplateMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link StorageReservation}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class StorageReservationNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * sizeのプロパティ名を返します。
     * 
     * @return sizeのプロパティ名
     */
    public static PropertyName<Integer> size() {
        return new PropertyName<Integer>("size");
    }

    /**
     * storageIdのプロパティ名を返します。
     * 
     * @return storageIdのプロパティ名
     */
    public static PropertyName<Long> storageId() {
        return new PropertyName<Long>("storageId");
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
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
    }

    /**
     * storageReservationDiskMapListのプロパティ名を返します。
     * 
     * @return storageReservationDiskMapListのプロパティ名
     */
    public static _StorageReservationDiskMapNames storageReservationDiskMapList() {
        return new _StorageReservationDiskMapNames("storageReservationDiskMapList");
    }

    /**
     * storageReservationDiskTemplateMapListのプロパティ名を返します。
     * 
     * @return storageReservationDiskTemplateMapListのプロパティ名
     */
    public static _StorageReservationDiskTemplateMapNames storageReservationDiskTemplateMapList() {
        return new _StorageReservationDiskTemplateMapNames("storageReservationDiskTemplateMapList");
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
     * storageのプロパティ名を返します。
     * 
     * @return storageのプロパティ名
     */
    public static _StorageNames storage() {
        return new _StorageNames("storage");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _StorageReservationNames extends PropertyName<StorageReservation> {

        /**
         * インスタンスを構築します。
         */
        public _StorageReservationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _StorageReservationNames(final String name) {
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
        public _StorageReservationNames(final PropertyName<?> parent, final String name) {
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
         * sizeのプロパティ名を返します。
         *
         * @return sizeのプロパティ名
         */
        public PropertyName<Integer> size() {
            return new PropertyName<Integer>(this, "size");
        }

        /**
         * storageIdのプロパティ名を返します。
         *
         * @return storageIdのプロパティ名
         */
        public PropertyName<Long> storageId() {
            return new PropertyName<Long>(this, "storageId");
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
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
        }

        /**
         * storageReservationDiskMapListのプロパティ名を返します。
         * 
         * @return storageReservationDiskMapListのプロパティ名
         */
        public _StorageReservationDiskMapNames storageReservationDiskMapList() {
            return new _StorageReservationDiskMapNames(this, "storageReservationDiskMapList");
        }

        /**
         * storageReservationDiskTemplateMapListのプロパティ名を返します。
         * 
         * @return storageReservationDiskTemplateMapListのプロパティ名
         */
        public _StorageReservationDiskTemplateMapNames storageReservationDiskTemplateMapList() {
            return new _StorageReservationDiskTemplateMapNames(this, "storageReservationDiskTemplateMapList");
        }

        /**
         * reservationのプロパティ名を返します。
         * 
         * @return reservationのプロパティ名
         */
        public _ReservationNames reservation() {
            return new _ReservationNames(this, "reservation");
        }

        /**
         * storageのプロパティ名を返します。
         * 
         * @return storageのプロパティ名
         */
        public _StorageNames storage() {
            return new _StorageNames(this, "storage");
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
