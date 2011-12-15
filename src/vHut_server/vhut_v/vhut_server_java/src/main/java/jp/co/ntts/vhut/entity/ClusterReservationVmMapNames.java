/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterReservationNames._ClusterReservationNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ClusterReservationVmMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ClusterReservationVmMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * clusterReservationIdのプロパティ名を返します。
     * 
     * @return clusterReservationIdのプロパティ名
     */
    public static PropertyName<Long> clusterReservationId() {
        return new PropertyName<Long>("clusterReservationId");
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
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
    }

    /**
     * clusterReservationのプロパティ名を返します。
     * 
     * @return clusterReservationのプロパティ名
     */
    public static _ClusterReservationNames clusterReservation() {
        return new _ClusterReservationNames("clusterReservation");
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
    public static class _ClusterReservationVmMapNames extends PropertyName<ClusterReservationVmMap> {

        /**
         * インスタンスを構築します。
         */
        public _ClusterReservationVmMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ClusterReservationVmMapNames(final String name) {
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
        public _ClusterReservationVmMapNames(final PropertyName<?> parent, final String name) {
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
         * clusterReservationIdのプロパティ名を返します。
         *
         * @return clusterReservationIdのプロパティ名
         */
        public PropertyName<Long> clusterReservationId() {
            return new PropertyName<Long>(this, "clusterReservationId");
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
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
        }

        /**
         * clusterReservationのプロパティ名を返します。
         * 
         * @return clusterReservationのプロパティ名
         */
        public _ClusterReservationNames clusterReservation() {
            return new _ClusterReservationNames(this, "clusterReservation");
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
