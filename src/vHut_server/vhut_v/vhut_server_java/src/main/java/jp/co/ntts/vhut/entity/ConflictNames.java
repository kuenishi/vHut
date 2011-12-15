/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.CloudUserNames._CloudUserNames;
import jp.co.ntts.vhut.entity.ClusterNames._ClusterNames;
import jp.co.ntts.vhut.entity.HostNames._HostNames;
import jp.co.ntts.vhut.entity.NetworkNames._NetworkNames;
import jp.co.ntts.vhut.entity.StorageNames._StorageNames;
import jp.co.ntts.vhut.entity.TemplateNames._TemplateNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Conflict}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ConflictNames {

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
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<ConflictStatus> status() {
        return new PropertyName<ConflictStatus>("status");
    }

    /**
     * detailのプロパティ名を返します。
     * 
     * @return detailのプロパティ名
     */
    public static PropertyName<String> detail() {
        return new PropertyName<String>("detail");
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
     * cloudUserListのプロパティ名を返します。
     * 
     * @return cloudUserListのプロパティ名
     */
    public static _CloudUserNames cloudUserList() {
        return new _CloudUserNames("cloudUserList");
    }

    /**
     * clusterListのプロパティ名を返します。
     * 
     * @return clusterListのプロパティ名
     */
    public static _ClusterNames clusterList() {
        return new _ClusterNames("clusterList");
    }

    /**
     * hostListのプロパティ名を返します。
     * 
     * @return hostListのプロパティ名
     */
    public static _HostNames hostList() {
        return new _HostNames("hostList");
    }

    /**
     * networkListのプロパティ名を返します。
     * 
     * @return networkListのプロパティ名
     */
    public static _NetworkNames networkList() {
        return new _NetworkNames("networkList");
    }

    /**
     * storageListのプロパティ名を返します。
     * 
     * @return storageListのプロパティ名
     */
    public static _StorageNames storageList() {
        return new _StorageNames("storageList");
    }

    /**
     * templateListのプロパティ名を返します。
     * 
     * @return templateListのプロパティ名
     */
    public static _TemplateNames templateList() {
        return new _TemplateNames("templateList");
    }

    /**
     * vmListのプロパティ名を返します。
     * 
     * @return vmListのプロパティ名
     */
    public static _VmNames vmList() {
        return new _VmNames("vmList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ConflictNames extends PropertyName<Conflict> {

        /**
         * インスタンスを構築します。
         */
        public _ConflictNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ConflictNames(final String name) {
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
        public _ConflictNames(final PropertyName<?> parent, final String name) {
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
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<ConflictStatus> status() {
            return new PropertyName<ConflictStatus>(this, "status");
        }

        /**
         * detailのプロパティ名を返します。
         *
         * @return detailのプロパティ名
         */
        public PropertyName<String> detail() {
            return new PropertyName<String>(this, "detail");
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
         * cloudUserListのプロパティ名を返します。
         * 
         * @return cloudUserListのプロパティ名
         */
        public _CloudUserNames cloudUserList() {
            return new _CloudUserNames(this, "cloudUserList");
        }

        /**
         * clusterListのプロパティ名を返します。
         * 
         * @return clusterListのプロパティ名
         */
        public _ClusterNames clusterList() {
            return new _ClusterNames(this, "clusterList");
        }

        /**
         * hostListのプロパティ名を返します。
         * 
         * @return hostListのプロパティ名
         */
        public _HostNames hostList() {
            return new _HostNames(this, "hostList");
        }

        /**
         * networkListのプロパティ名を返します。
         * 
         * @return networkListのプロパティ名
         */
        public _NetworkNames networkList() {
            return new _NetworkNames(this, "networkList");
        }

        /**
         * storageListのプロパティ名を返します。
         * 
         * @return storageListのプロパティ名
         */
        public _StorageNames storageList() {
            return new _StorageNames(this, "storageList");
        }

        /**
         * templateListのプロパティ名を返します。
         * 
         * @return templateListのプロパティ名
         */
        public _TemplateNames templateList() {
            return new _TemplateNames(this, "templateList");
        }

        /**
         * vmListのプロパティ名を返します。
         * 
         * @return vmListのプロパティ名
         */
        public _VmNames vmList() {
            return new _VmNames(this, "vmList");
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
