/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link VlanResource}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class VlanResourceNames {

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
     * vlanMaxのプロパティ名を返します。
     * 
     * @return vlanMaxのプロパティ名
     */
    public static PropertyName<Integer> vlanMax() {
        return new PropertyName<Integer>("vlanMax");
    }

    /**
     * vlanTerminablyUsedのプロパティ名を返します。
     * 
     * @return vlanTerminablyUsedのプロパティ名
     */
    public static PropertyName<Integer> vlanTerminablyUsed() {
        return new PropertyName<Integer>("vlanTerminablyUsed");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _VlanResourceNames extends PropertyName<VlanResource> {

        /**
         * インスタンスを構築します。
         */
        public _VlanResourceNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _VlanResourceNames(final String name) {
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
        public _VlanResourceNames(final PropertyName<?> parent, final String name) {
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
         * vlanMaxのプロパティ名を返します。
         *
         * @return vlanMaxのプロパティ名
         */
        public PropertyName<Integer> vlanMax() {
            return new PropertyName<Integer>(this, "vlanMax");
        }

        /**
         * vlanTerminablyUsedのプロパティ名を返します。
         *
         * @return vlanTerminablyUsedのプロパティ名
         */
        public PropertyName<Integer> vlanTerminablyUsed() {
            return new PropertyName<Integer>(this, "vlanTerminablyUsed");
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
