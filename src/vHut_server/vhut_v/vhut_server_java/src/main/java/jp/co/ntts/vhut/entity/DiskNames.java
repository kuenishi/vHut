/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.DiskTemplateNames._DiskTemplateNames;
import jp.co.ntts.vhut.entity.StorageNames._StorageNames;
import jp.co.ntts.vhut.entity.StorageReservationDiskMapNames._StorageReservationDiskMapNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Disk}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class DiskNames {

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
     * vmIdのプロパティ名を返します。
     * 
     * @return vmIdのプロパティ名
     */
    public static PropertyName<Long> vmId() {
        return new PropertyName<Long>("vmId");
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
     * storageのプロパティ名を返します。
     * 
     * @return storageのプロパティ名
     */
    public static _StorageNames storage() {
        return new _StorageNames("storage");
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
     * storageReservationDiskMapListのプロパティ名を返します。
     * 
     * @return storageReservationDiskMapListのプロパティ名
     */
    public static _StorageReservationDiskMapNames storageReservationDiskMapList() {
        return new _StorageReservationDiskMapNames("storageReservationDiskMapList");
    }

    /**
     * vmのプロパティ名を返します。
     * 
     * @return vmのプロパティ名
     */
    public static _VmNames vm() {
        return new _VmNames("vm");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _DiskNames extends PropertyName<Disk> {

        /**
         * インスタンスを構築します。
         */
        public _DiskNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _DiskNames(final String name) {
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
        public _DiskNames(final PropertyName<?> parent, final String name) {
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
         * vmIdのプロパティ名を返します。
         *
         * @return vmIdのプロパティ名
         */
        public PropertyName<Long> vmId() {
            return new PropertyName<Long>(this, "vmId");
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
         * storageのプロパティ名を返します。
         * 
         * @return storageのプロパティ名
         */
        public _StorageNames storage() {
            return new _StorageNames(this, "storage");
        }

        /**
         * diskTemplateのプロパティ名を返します。
         * 
         * @return diskTemplateのプロパティ名
         */
        public _DiskTemplateNames diskTemplate() {
            return new _DiskTemplateNames(this, "diskTemplate");
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
         * vmのプロパティ名を返します。
         * 
         * @return vmのプロパティ名
         */
        public _VmNames vm() {
            return new _VmNames(this, "vm");
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
