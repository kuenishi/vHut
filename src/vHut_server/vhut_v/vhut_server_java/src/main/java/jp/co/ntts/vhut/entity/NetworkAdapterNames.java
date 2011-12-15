/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.PublicIpReservationNames._PublicIpReservationNames;
import jp.co.ntts.vhut.entity.SecurityGroupNames._SecurityGroupNames;
import jp.co.ntts.vhut.entity.VmNames._VmNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link NetworkAdapter}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class NetworkAdapterNames {

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
     * securityGroupIdのプロパティ名を返します。
     * 
     * @return securityGroupIdのプロパティ名
     */
    public static PropertyName<Long> securityGroupId() {
        return new PropertyName<Long>("securityGroupId");
    }

    /**
     * publicIpのプロパティ名を返します。
     * 
     * @return publicIpのプロパティ名
     */
    public static PropertyName<String> publicIp() {
        return new PropertyName<String>("publicIp");
    }

    /**
     * privateIpのプロパティ名を返します。
     * 
     * @return privateIpのプロパティ名
     */
    public static PropertyName<String> privateIp() {
        return new PropertyName<String>("privateIp");
    }

    /**
     * macのプロパティ名を返します。
     * 
     * @return macのプロパティ名
     */
    public static PropertyName<String> mac() {
        return new PropertyName<String>("mac");
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
     * securityGroupのプロパティ名を返します。
     * 
     * @return securityGroupのプロパティ名
     */
    public static _SecurityGroupNames securityGroup() {
        return new _SecurityGroupNames("securityGroup");
    }

    /**
     * publicIpReservationListのプロパティ名を返します。
     * 
     * @return publicIpReservationListのプロパティ名
     */
    public static _PublicIpReservationNames publicIpReservationList() {
        return new _PublicIpReservationNames("publicIpReservationList");
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
    public static class _NetworkAdapterNames extends PropertyName<NetworkAdapter> {

        /**
         * インスタンスを構築します。
         */
        public _NetworkAdapterNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _NetworkAdapterNames(final String name) {
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
        public _NetworkAdapterNames(final PropertyName<?> parent, final String name) {
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
         * securityGroupIdのプロパティ名を返します。
         *
         * @return securityGroupIdのプロパティ名
         */
        public PropertyName<Long> securityGroupId() {
            return new PropertyName<Long>(this, "securityGroupId");
        }

        /**
         * publicIpのプロパティ名を返します。
         *
         * @return publicIpのプロパティ名
         */
        public PropertyName<String> publicIp() {
            return new PropertyName<String>(this, "publicIp");
        }

        /**
         * privateIpのプロパティ名を返します。
         *
         * @return privateIpのプロパティ名
         */
        public PropertyName<String> privateIp() {
            return new PropertyName<String>(this, "privateIp");
        }

        /**
         * macのプロパティ名を返します。
         *
         * @return macのプロパティ名
         */
        public PropertyName<String> mac() {
            return new PropertyName<String>(this, "mac");
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
         * securityGroupのプロパティ名を返します。
         * 
         * @return securityGroupのプロパティ名
         */
        public _SecurityGroupNames securityGroup() {
            return new _SecurityGroupNames(this, "securityGroup");
        }

        /**
         * publicIpReservationListのプロパティ名を返します。
         * 
         * @return publicIpReservationListのプロパティ名
         */
        public _PublicIpReservationNames publicIpReservationList() {
            return new _PublicIpReservationNames(this, "publicIpReservationList");
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
