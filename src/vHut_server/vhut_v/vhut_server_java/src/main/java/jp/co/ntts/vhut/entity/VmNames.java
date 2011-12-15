/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterNames._ClusterNames;
import jp.co.ntts.vhut.entity.ClusterReservationVmMapNames._ClusterReservationVmMapNames;
import jp.co.ntts.vhut.entity.CommandVmMapNames._CommandVmMapNames;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.DiskNames._DiskNames;
import jp.co.ntts.vhut.entity.HostNames._HostNames;
import jp.co.ntts.vhut.entity.NetworkAdapterNames._NetworkAdapterNames;
import jp.co.ntts.vhut.entity.TemplateNames._TemplateNames;
import jp.co.ntts.vhut.entity.VmCloudUserMapNames._VmCloudUserMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Vm}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class VmNames {

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
    public static PropertyName<VmStatus> status() {
        return new PropertyName<VmStatus>("status");
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
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
    }

    /**
     * cpuUsageのプロパティ名を返します。
     * 
     * @return cpuUsageのプロパティ名
     */
    public static PropertyName<Integer> cpuUsage() {
        return new PropertyName<Integer>("cpuUsage");
    }

    /**
     * memoryUsageのプロパティ名を返します。
     * 
     * @return memoryUsageのプロパティ名
     */
    public static PropertyName<Integer> memoryUsage() {
        return new PropertyName<Integer>("memoryUsage");
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
     * templateIdのプロパティ名を返します。
     * 
     * @return templateIdのプロパティ名
     */
    public static PropertyName<Long> templateId() {
        return new PropertyName<Long>("templateId");
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
     * hostIdのプロパティ名を返します。
     * 
     * @return hostIdのプロパティ名
     */
    public static PropertyName<Long> hostId() {
        return new PropertyName<Long>("hostId");
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
     * commandVmMapListのプロパティ名を返します。
     * 
     * @return commandVmMapListのプロパティ名
     */
    public static _CommandVmMapNames commandVmMapList() {
        return new _CommandVmMapNames("commandVmMapList");
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
     * networkAdapterListのプロパティ名を返します。
     * 
     * @return networkAdapterListのプロパティ名
     */
    public static _NetworkAdapterNames networkAdapterList() {
        return new _NetworkAdapterNames("networkAdapterList");
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
     * hostのプロパティ名を返します。
     * 
     * @return hostのプロパティ名
     */
    public static _HostNames host() {
        return new _HostNames("host");
    }

    /**
     * templateのプロパティ名を返します。
     * 
     * @return templateのプロパティ名
     */
    public static _TemplateNames template() {
        return new _TemplateNames("template");
    }

    /**
     * vmCloudUserMapListのプロパティ名を返します。
     * 
     * @return vmCloudUserMapListのプロパティ名
     */
    public static _VmCloudUserMapNames vmCloudUserMapList() {
        return new _VmCloudUserMapNames("vmCloudUserMapList");
    }

    /**
     * clusterReservationVmMapListのプロパティ名を返します。
     * 
     * @return clusterReservationVmMapListのプロパティ名
     */
    public static _ClusterReservationVmMapNames clusterReservationVmMapList() {
        return new _ClusterReservationVmMapNames("clusterReservationVmMapList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _VmNames extends PropertyName<Vm> {

        /**
         * インスタンスを構築します。
         */
        public _VmNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _VmNames(final String name) {
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
        public _VmNames(final PropertyName<?> parent, final String name) {
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
        public PropertyName<VmStatus> status() {
            return new PropertyName<VmStatus>(this, "status");
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
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
        }

        /**
         * cpuUsageのプロパティ名を返します。
         *
         * @return cpuUsageのプロパティ名
         */
        public PropertyName<Integer> cpuUsage() {
            return new PropertyName<Integer>(this, "cpuUsage");
        }

        /**
         * memoryUsageのプロパティ名を返します。
         *
         * @return memoryUsageのプロパティ名
         */
        public PropertyName<Integer> memoryUsage() {
            return new PropertyName<Integer>(this, "memoryUsage");
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
         * templateIdのプロパティ名を返します。
         *
         * @return templateIdのプロパティ名
         */
        public PropertyName<Long> templateId() {
            return new PropertyName<Long>(this, "templateId");
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
         * hostIdのプロパティ名を返します。
         *
         * @return hostIdのプロパティ名
         */
        public PropertyName<Long> hostId() {
            return new PropertyName<Long>(this, "hostId");
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
         * commandVmMapListのプロパティ名を返します。
         * 
         * @return commandVmMapListのプロパティ名
         */
        public _CommandVmMapNames commandVmMapList() {
            return new _CommandVmMapNames(this, "commandVmMapList");
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
         * networkAdapterListのプロパティ名を返します。
         * 
         * @return networkAdapterListのプロパティ名
         */
        public _NetworkAdapterNames networkAdapterList() {
            return new _NetworkAdapterNames(this, "networkAdapterList");
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
         * hostのプロパティ名を返します。
         * 
         * @return hostのプロパティ名
         */
        public _HostNames host() {
            return new _HostNames(this, "host");
        }

        /**
         * templateのプロパティ名を返します。
         * 
         * @return templateのプロパティ名
         */
        public _TemplateNames template() {
            return new _TemplateNames(this, "template");
        }

        /**
         * vmCloudUserMapListのプロパティ名を返します。
         * 
         * @return vmCloudUserMapListのプロパティ名
         */
        public _VmCloudUserMapNames vmCloudUserMapList() {
            return new _VmCloudUserMapNames(this, "vmCloudUserMapList");
        }

        /**
         * clusterReservationVmMapListのプロパティ名を返します。
         * 
         * @return clusterReservationVmMapListのプロパティ名
         */
        public _ClusterReservationVmMapNames clusterReservationVmMapList() {
            return new _ClusterReservationVmMapNames(this, "clusterReservationVmMapList");
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
