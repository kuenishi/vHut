/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterNames._ClusterNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ClusterResource}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ClusterResourceNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * timeのプロパティ名を返します。
     * 
     * @return timeのプロパティ名
     */
    public static PropertyName<Timestamp> time() {
        return new PropertyName<Timestamp>("time");
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
     * cpuCoreMaxのプロパティ名を返します。
     * 
     * @return cpuCoreMaxのプロパティ名
     */
    public static PropertyName<Integer> cpuCoreMax() {
        return new PropertyName<Integer>("cpuCoreMax");
    }

    /**
     * cpuCoreTerminablyUsedのプロパティ名を返します。
     * 
     * @return cpuCoreTerminablyUsedのプロパティ名
     */
    public static PropertyName<Integer> cpuCoreTerminablyUsed() {
        return new PropertyName<Integer>("cpuCoreTerminablyUsed");
    }

    /**
     * memoryMaxのプロパティ名を返します。
     * 
     * @return memoryMaxのプロパティ名
     */
    public static PropertyName<Integer> memoryMax() {
        return new PropertyName<Integer>("memoryMax");
    }

    /**
     * memoryTerminablyUsedのプロパティ名を返します。
     * 
     * @return memoryTerminablyUsedのプロパティ名
     */
    public static PropertyName<Integer> memoryTerminablyUsed() {
        return new PropertyName<Integer>("memoryTerminablyUsed");
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
     * clusterのプロパティ名を返します。
     * 
     * @return clusterのプロパティ名
     */
    public static _ClusterNames cluster() {
        return new _ClusterNames("cluster");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ClusterResourceNames extends PropertyName<ClusterResource> {

        /**
         * インスタンスを構築します。
         */
        public _ClusterResourceNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ClusterResourceNames(final String name) {
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
        public _ClusterResourceNames(final PropertyName<?> parent, final String name) {
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
         * timeのプロパティ名を返します。
         *
         * @return timeのプロパティ名
         */
        public PropertyName<Timestamp> time() {
            return new PropertyName<Timestamp>(this, "time");
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
         * cpuCoreMaxのプロパティ名を返します。
         *
         * @return cpuCoreMaxのプロパティ名
         */
        public PropertyName<Integer> cpuCoreMax() {
            return new PropertyName<Integer>(this, "cpuCoreMax");
        }

        /**
         * cpuCoreTerminablyUsedのプロパティ名を返します。
         *
         * @return cpuCoreTerminablyUsedのプロパティ名
         */
        public PropertyName<Integer> cpuCoreTerminablyUsed() {
            return new PropertyName<Integer>(this, "cpuCoreTerminablyUsed");
        }

        /**
         * memoryMaxのプロパティ名を返します。
         *
         * @return memoryMaxのプロパティ名
         */
        public PropertyName<Integer> memoryMax() {
            return new PropertyName<Integer>(this, "memoryMax");
        }

        /**
         * memoryTerminablyUsedのプロパティ名を返します。
         *
         * @return memoryTerminablyUsedのプロパティ名
         */
        public PropertyName<Integer> memoryTerminablyUsed() {
            return new PropertyName<Integer>(this, "memoryTerminablyUsed");
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
         * clusterのプロパティ名を返します。
         * 
         * @return clusterのプロパティ名
         */
        public _ClusterNames cluster() {
            return new _ClusterNames(this, "cluster");
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
