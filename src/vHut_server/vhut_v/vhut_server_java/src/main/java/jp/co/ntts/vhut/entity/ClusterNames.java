/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ClusterReservationNames._ClusterReservationNames;
import jp.co.ntts.vhut.entity.ClusterResourceNames._ClusterResourceNames;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.HostNames._HostNames;
import jp.co.ntts.vhut.entity.TemplateNames._TemplateNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Cluster}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ClusterNames {

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
     * versionのプロパティ名を返します。
     * 
     * @return versionのプロパティ名
     */
    public static PropertyName<Long> version() {
        return new PropertyName<Long>("version");
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
     * clusterReservationListのプロパティ名を返します。
     * 
     * @return clusterReservationListのプロパティ名
     */
    public static _ClusterReservationNames clusterReservationList() {
        return new _ClusterReservationNames("clusterReservationList");
    }

    /**
     * clusterResourceListのプロパティ名を返します。
     * 
     * @return clusterResourceListのプロパティ名
     */
    public static _ClusterResourceNames clusterResourceList() {
        return new _ClusterResourceNames("clusterResourceList");
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
    public static class _ClusterNames extends PropertyName<Cluster> {

        /**
         * インスタンスを構築します。
         */
        public _ClusterNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ClusterNames(final String name) {
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
        public _ClusterNames(final PropertyName<?> parent, final String name) {
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
         * versionのプロパティ名を返します。
         *
         * @return versionのプロパティ名
         */
        public PropertyName<Long> version() {
            return new PropertyName<Long>(this, "version");
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
         * clusterReservationListのプロパティ名を返します。
         * 
         * @return clusterReservationListのプロパティ名
         */
        public _ClusterReservationNames clusterReservationList() {
            return new _ClusterReservationNames(this, "clusterReservationList");
        }

        /**
         * clusterResourceListのプロパティ名を返します。
         * 
         * @return clusterResourceListのプロパティ名
         */
        public _ClusterResourceNames clusterResourceList() {
            return new _ClusterResourceNames(this, "clusterResourceList");
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
