/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.DiskNames._DiskNames;
import jp.co.ntts.vhut.entity.StorageReservationNames._StorageReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link StorageReservationDiskMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class StorageReservationDiskMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * storageReservationIdのプロパティ名を返します。
     * 
     * @return storageReservationIdのプロパティ名
     */
    public static PropertyName<Long> storageReservationId() {
        return new PropertyName<Long>("storageReservationId");
    }

    /**
     * diskIdのプロパティ名を返します。
     * 
     * @return diskIdのプロパティ名
     */
    public static PropertyName<Long> diskId() {
        return new PropertyName<Long>("diskId");
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
     * storageReservationのプロパティ名を返します。
     * 
     * @return storageReservationのプロパティ名
     */
    public static _StorageReservationNames storageReservation() {
        return new _StorageReservationNames("storageReservation");
    }

    /**
     * diskのプロパティ名を返します。
     * 
     * @return diskのプロパティ名
     */
    public static _DiskNames disk() {
        return new _DiskNames("disk");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _StorageReservationDiskMapNames extends PropertyName<StorageReservationDiskMap> {

        /**
         * インスタンスを構築します。
         */
        public _StorageReservationDiskMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _StorageReservationDiskMapNames(final String name) {
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
        public _StorageReservationDiskMapNames(final PropertyName<?> parent, final String name) {
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
         * storageReservationIdのプロパティ名を返します。
         *
         * @return storageReservationIdのプロパティ名
         */
        public PropertyName<Long> storageReservationId() {
            return new PropertyName<Long>(this, "storageReservationId");
        }

        /**
         * diskIdのプロパティ名を返します。
         *
         * @return diskIdのプロパティ名
         */
        public PropertyName<Long> diskId() {
            return new PropertyName<Long>(this, "diskId");
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
         * storageReservationのプロパティ名を返します。
         * 
         * @return storageReservationのプロパティ名
         */
        public _StorageReservationNames storageReservation() {
            return new _StorageReservationNames(this, "storageReservation");
        }

        /**
         * diskのプロパティ名を返します。
         * 
         * @return diskのプロパティ名
         */
        public _DiskNames disk() {
            return new _DiskNames(this, "disk");
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
