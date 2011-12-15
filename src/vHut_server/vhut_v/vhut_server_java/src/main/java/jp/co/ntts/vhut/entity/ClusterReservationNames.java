/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterNames._ClusterNames;
import jp.co.ntts.vhut.entity.ClusterReservationVmMapNames._ClusterReservationVmMapNames;
import jp.co.ntts.vhut.entity.ReservationNames._ReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ClusterReservation}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ClusterReservationNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
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
     * clusterのプロパティ名を返します。
     * 
     * @return clusterのプロパティ名
     */
    public static _ClusterNames cluster() {
        return new _ClusterNames("cluster");
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
    public static class _ClusterReservationNames extends PropertyName<ClusterReservation> {

        /**
         * インスタンスを構築します。
         */
        public _ClusterReservationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ClusterReservationNames(final String name) {
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
        public _ClusterReservationNames(final PropertyName<?> parent, final String name) {
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
         * clusterIdのプロパティ名を返します。
         *
         * @return clusterIdのプロパティ名
         */
        public PropertyName<Long> clusterId() {
            return new PropertyName<Long>(this, "clusterId");
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
         * clusterのプロパティ名を返します。
         * 
         * @return clusterのプロパティ名
         */
        public _ClusterNames cluster() {
            return new _ClusterNames(this, "cluster");
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
