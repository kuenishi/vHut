/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterNames._ClusterNames;
import jp.co.ntts.vhut.entity.CommandTemplateMapNames._CommandTemplateMapNames;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.DiskTemplateNames._DiskTemplateNames;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplateNames._NetworkAdapterTemplateNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Template}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class TemplateNames {

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
     * descriptionのプロパティ名を返します。
     * 
     * @return descriptionのプロパティ名
     */
    public static PropertyName<String> description() {
        return new PropertyName<String>("description");
    }

    /**
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<TemplateStatus> status() {
        return new PropertyName<TemplateStatus>("status");
    }

    /**
     * specIdのプロパティ名を返します。
     * 
     * @return specIdのプロパティ名
     */
    public static PropertyName<Long> specId() {
        return new PropertyName<Long>("specId");
    }

    /**
     * cpuCoreのプロパティ名を返します。
     * 
     * @return cpuCoreのプロパティ名
     */
    public static PropertyName<Integer> cpuCore() {
        return new PropertyName<Integer>("cpuCore");
    }

    /**
     * memoryのプロパティ名を返します。
     * 
     * @return memoryのプロパティ名
     */
    public static PropertyName<Integer> memory() {
        return new PropertyName<Integer>("memory");
    }

    /**
     * osのプロパティ名を返します。
     * 
     * @return osのプロパティ名
     */
    public static PropertyName<Os> os() {
        return new PropertyName<Os>("os");
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
     * clusterIdのプロパティ名を返します。
     * 
     * @return clusterIdのプロパティ名
     */
    public static PropertyName<Long> clusterId() {
        return new PropertyName<Long>("clusterId");
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
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
    }

    /**
     * commandTemplateMapListのプロパティ名を返します。
     * 
     * @return commandTemplateMapListのプロパティ名
     */
    public static _CommandTemplateMapNames commandTemplateMapList() {
        return new _CommandTemplateMapNames("commandTemplateMapList");
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
     * networkAdapterTemplateListのプロパティ名を返します。
     * 
     * @return networkAdapterTemplateListのプロパティ名
     */
    public static _NetworkAdapterTemplateNames networkAdapterTemplateList() {
        return new _NetworkAdapterTemplateNames("networkAdapterTemplateList");
    }

    /**
     * clusterのプロパティ名を返します。
     * 
     * @return clusterのプロパティ名
     */
    public static _ClusterNames cluster() {
        return new _ClusterNames("cluster");
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
    public static class _TemplateNames extends PropertyName<Template> {

        /**
         * インスタンスを構築します。
         */
        public _TemplateNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _TemplateNames(final String name) {
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
        public _TemplateNames(final PropertyName<?> parent, final String name) {
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
         * descriptionのプロパティ名を返します。
         *
         * @return descriptionのプロパティ名
         */
        public PropertyName<String> description() {
            return new PropertyName<String>(this, "description");
        }

        /**
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<TemplateStatus> status() {
            return new PropertyName<TemplateStatus>(this, "status");
        }

        /**
         * specIdのプロパティ名を返します。
         *
         * @return specIdのプロパティ名
         */
        public PropertyName<Long> specId() {
            return new PropertyName<Long>(this, "specId");
        }

        /**
         * cpuCoreのプロパティ名を返します。
         *
         * @return cpuCoreのプロパティ名
         */
        public PropertyName<Integer> cpuCore() {
            return new PropertyName<Integer>(this, "cpuCore");
        }

        /**
         * memoryのプロパティ名を返します。
         *
         * @return memoryのプロパティ名
         */
        public PropertyName<Integer> memory() {
            return new PropertyName<Integer>(this, "memory");
        }

        /**
         * osのプロパティ名を返します。
         *
         * @return osのプロパティ名
         */
        public PropertyName<Os> os() {
            return new PropertyName<Os>(this, "os");
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
         * clusterIdのプロパティ名を返します。
         *
         * @return clusterIdのプロパティ名
         */
        public PropertyName<Long> clusterId() {
            return new PropertyName<Long>(this, "clusterId");
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
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
        }

        /**
         * commandTemplateMapListのプロパティ名を返します。
         * 
         * @return commandTemplateMapListのプロパティ名
         */
        public _CommandTemplateMapNames commandTemplateMapList() {
            return new _CommandTemplateMapNames(this, "commandTemplateMapList");
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
         * networkAdapterTemplateListのプロパティ名を返します。
         * 
         * @return networkAdapterTemplateListのプロパティ名
         */
        public _NetworkAdapterTemplateNames networkAdapterTemplateList() {
            return new _NetworkAdapterTemplateNames(this, "networkAdapterTemplateList");
        }

        /**
         * clusterのプロパティ名を返します。
         * 
         * @return clusterのプロパティ名
         */
        public _ClusterNames cluster() {
            return new _ClusterNames(this, "cluster");
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
