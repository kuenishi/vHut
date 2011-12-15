/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.DiskNames._DiskNames;
import jp.co.ntts.vhut.entity.DiskTemplateNames._DiskTemplateNames;
import jp.co.ntts.vhut.entity.StorageReservationNames._StorageReservationNames;
import jp.co.ntts.vhut.entity.StorageResourceNames._StorageResourceNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Storage}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class StorageNames {

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
     * conflictIdのプロパティ名を返します。
     * 
     * @return conflictIdのプロパティ名
     */
    public static PropertyName<Long> conflictId() {
        return new PropertyName<Long>("conflictId");
    }

    /**
     * availableSizeのプロパティ名を返します。
     * 
     * @return availableSizeのプロパティ名
     */
    public static PropertyName<Integer> availableSize() {
        return new PropertyName<Integer>("availableSize");
    }

    /**
     * commitedSizeのプロパティ名を返します。
     * 
     * @return commitedSizeのプロパティ名
     */
    public static PropertyName<Integer> commitedSize() {
        return new PropertyName<Integer>("commitedSize");
    }

    /**
     * physicalSizeのプロパティ名を返します。
     * 
     * @return physicalSizeのプロパティ名
     */
    public static PropertyName<Integer> physicalSize() {
        return new PropertyName<Integer>("physicalSize");
    }

    /**
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<StorageStatus> status() {
        return new PropertyName<StorageStatus>("status");
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
     * diskListのプロパティ名を返します。
     * 
     * @return diskListのプロパティ名
     */
    public static _DiskNames diskList() {
        return new _DiskNames("diskList");
    }

    /**
     * diskTemplateListのプロパティ名を返します。
     * 
     * @return diskTemplateListのプロパティ名
     */
    public static _DiskTemplateNames diskTemplateList() {
        return new _DiskTemplateNames("diskTemplateList");
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
     * storageReservationListのプロパティ名を返します。
     * 
     * @return storageReservationListのプロパティ名
     */
    public static _StorageReservationNames storageReservationList() {
        return new _StorageReservationNames("storageReservationList");
    }

    /**
     * storageResourceListのプロパティ名を返します。
     * 
     * @return storageResourceListのプロパティ名
     */
    public static _StorageResourceNames storageResourceList() {
        return new _StorageResourceNames("storageResourceList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _StorageNames extends PropertyName<Storage> {

        /**
         * インスタンスを構築します。
         */
        public _StorageNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _StorageNames(final String name) {
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
        public _StorageNames(final PropertyName<?> parent, final String name) {
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
         * conflictIdのプロパティ名を返します。
         *
         * @return conflictIdのプロパティ名
         */
        public PropertyName<Long> conflictId() {
            return new PropertyName<Long>(this, "conflictId");
        }

        /**
         * availableSizeのプロパティ名を返します。
         *
         * @return availableSizeのプロパティ名
         */
        public PropertyName<Integer> availableSize() {
            return new PropertyName<Integer>(this, "availableSize");
        }

        /**
         * commitedSizeのプロパティ名を返します。
         *
         * @return commitedSizeのプロパティ名
         */
        public PropertyName<Integer> commitedSize() {
            return new PropertyName<Integer>(this, "commitedSize");
        }

        /**
         * physicalSizeのプロパティ名を返します。
         *
         * @return physicalSizeのプロパティ名
         */
        public PropertyName<Integer> physicalSize() {
            return new PropertyName<Integer>(this, "physicalSize");
        }

        /**
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<StorageStatus> status() {
            return new PropertyName<StorageStatus>(this, "status");
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
         * diskListのプロパティ名を返します。
         * 
         * @return diskListのプロパティ名
         */
        public _DiskNames diskList() {
            return new _DiskNames(this, "diskList");
        }

        /**
         * diskTemplateListのプロパティ名を返します。
         * 
         * @return diskTemplateListのプロパティ名
         */
        public _DiskTemplateNames diskTemplateList() {
            return new _DiskTemplateNames(this, "diskTemplateList");
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
         * storageReservationListのプロパティ名を返します。
         * 
         * @return storageReservationListのプロパティ名
         */
        public _StorageReservationNames storageReservationList() {
            return new _StorageReservationNames(this, "storageReservationList");
        }

        /**
         * storageResourceListのプロパティ名を返します。
         * 
         * @return storageResourceListのプロパティ名
         */
        public _StorageResourceNames storageResourceList() {
            return new _StorageResourceNames(this, "storageResourceList");
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
