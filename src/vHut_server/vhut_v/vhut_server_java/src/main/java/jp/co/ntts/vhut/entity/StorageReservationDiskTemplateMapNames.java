/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.DiskTemplateNames._DiskTemplateNames;
import jp.co.ntts.vhut.entity.StorageReservationNames._StorageReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link StorageReservationDiskTemplateMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class StorageReservationDiskTemplateMapNames {

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
     * diskTemplateIdのプロパティ名を返します。
     * 
     * @return diskTemplateIdのプロパティ名
     */
    public static PropertyName<Long> diskTemplateId() {
        return new PropertyName<Long>("diskTemplateId");
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
     * diskTemplateのプロパティ名を返します。
     * 
     * @return diskTemplateのプロパティ名
     */
    public static _DiskTemplateNames diskTemplate() {
        return new _DiskTemplateNames("diskTemplate");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _StorageReservationDiskTemplateMapNames extends PropertyName<StorageReservationDiskTemplateMap> {

        /**
         * インスタンスを構築します。
         */
        public _StorageReservationDiskTemplateMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _StorageReservationDiskTemplateMapNames(final String name) {
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
        public _StorageReservationDiskTemplateMapNames(final PropertyName<?> parent, final String name) {
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
         * diskTemplateIdのプロパティ名を返します。
         *
         * @return diskTemplateIdのプロパティ名
         */
        public PropertyName<Long> diskTemplateId() {
            return new PropertyName<Long>(this, "diskTemplateId");
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
         * diskTemplateのプロパティ名を返します。
         * 
         * @return diskTemplateのプロパティ名
         */
        public _DiskTemplateNames diskTemplate() {
            return new _DiskTemplateNames(this, "diskTemplate");
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
