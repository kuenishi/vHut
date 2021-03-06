/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ConflictNames._ConflictNames;
import jp.co.ntts.vhut.entity.SecurityGroupNames._SecurityGroupNames;
import jp.co.ntts.vhut.entity.VlanReservationNames._VlanReservationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Network}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class NetworkNames {

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
     * vlanのプロパティ名を返します。
     * 
     * @return vlanのプロパティ名
     */
    public static PropertyName<Short> vlan() {
        return new PropertyName<Short>("vlan");
    }

    /**
     * networkAddressのプロパティ名を返します。
     * 
     * @return networkAddressのプロパティ名
     */
    public static PropertyName<String> networkAddress() {
        return new PropertyName<String>("networkAddress");
    }

    /**
     * maskのプロパティ名を返します。
     * 
     * @return maskのプロパティ名
     */
    public static PropertyName<String> mask() {
        return new PropertyName<String>("mask");
    }

    /**
     * gatewayのプロパティ名を返します。
     * 
     * @return gatewayのプロパティ名
     */
    public static PropertyName<String> gateway() {
        return new PropertyName<String>("gateway");
    }

    /**
     * dnsのプロパティ名を返します。
     * 
     * @return dnsのプロパティ名
     */
    public static PropertyName<String> dns() {
        return new PropertyName<String>("dns");
    }

    /**
     * broadcastのプロパティ名を返します。
     * 
     * @return broadcastのプロパティ名
     */
    public static PropertyName<String> broadcast() {
        return new PropertyName<String>("broadcast");
    }

    /**
     * dhcpのプロパティ名を返します。
     * 
     * @return dhcpのプロパティ名
     */
    public static PropertyName<String> dhcp() {
        return new PropertyName<String>("dhcp");
    }

    /**
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<NetworkStatus> status() {
        return new PropertyName<NetworkStatus>("status");
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
     * securityGroupのプロパティ名を返します。
     * 
     * @return securityGroupのプロパティ名
     */
    public static _SecurityGroupNames securityGroup() {
        return new _SecurityGroupNames("securityGroup");
    }

    /**
     * vlanReservationListのプロパティ名を返します。
     * 
     * @return vlanReservationListのプロパティ名
     */
    public static _VlanReservationNames vlanReservationList() {
        return new _VlanReservationNames("vlanReservationList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _NetworkNames extends PropertyName<Network> {

        /**
         * インスタンスを構築します。
         */
        public _NetworkNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _NetworkNames(final String name) {
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
        public _NetworkNames(final PropertyName<?> parent, final String name) {
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
         * vlanのプロパティ名を返します。
         *
         * @return vlanのプロパティ名
         */
        public PropertyName<Short> vlan() {
            return new PropertyName<Short>(this, "vlan");
        }

        /**
         * networkAddressのプロパティ名を返します。
         *
         * @return networkAddressのプロパティ名
         */
        public PropertyName<String> networkAddress() {
            return new PropertyName<String>(this, "networkAddress");
        }

        /**
         * maskのプロパティ名を返します。
         *
         * @return maskのプロパティ名
         */
        public PropertyName<String> mask() {
            return new PropertyName<String>(this, "mask");
        }

        /**
         * gatewayのプロパティ名を返します。
         *
         * @return gatewayのプロパティ名
         */
        public PropertyName<String> gateway() {
            return new PropertyName<String>(this, "gateway");
        }

        /**
         * dnsのプロパティ名を返します。
         *
         * @return dnsのプロパティ名
         */
        public PropertyName<String> dns() {
            return new PropertyName<String>(this, "dns");
        }

        /**
         * broadcastのプロパティ名を返します。
         *
         * @return broadcastのプロパティ名
         */
        public PropertyName<String> broadcast() {
            return new PropertyName<String>(this, "broadcast");
        }

        /**
         * dhcpのプロパティ名を返します。
         *
         * @return dhcpのプロパティ名
         */
        public PropertyName<String> dhcp() {
            return new PropertyName<String>(this, "dhcp");
        }

        /**
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<NetworkStatus> status() {
            return new PropertyName<NetworkStatus>(this, "status");
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
         * securityGroupのプロパティ名を返します。
         * 
         * @return securityGroupのプロパティ名
         */
        public _SecurityGroupNames securityGroup() {
            return new _SecurityGroupNames(this, "securityGroup");
        }

        /**
         * vlanReservationListのプロパティ名を返します。
         * 
         * @return vlanReservationListのプロパティ名
         */
        public _VlanReservationNames vlanReservationList() {
            return new _VlanReservationNames(this, "vlanReservationList");
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
