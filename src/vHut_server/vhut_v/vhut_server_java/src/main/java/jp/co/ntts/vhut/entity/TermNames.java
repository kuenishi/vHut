/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationNames._ApplicationNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Term}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:02")
public class TermNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * applicationIdのプロパティ名を返します。
     * 
     * @return applicationIdのプロパティ名
     */
    public static PropertyName<Long> applicationId() {
        return new PropertyName<Long>("applicationId");
    }

    /**
     * startTimeのプロパティ名を返します。
     * 
     * @return startTimeのプロパティ名
     */
    public static PropertyName<Timestamp> startTime() {
        return new PropertyName<Timestamp>("startTime");
    }

    /**
     * endTimeのプロパティ名を返します。
     * 
     * @return endTimeのプロパティ名
     */
    public static PropertyName<Timestamp> endTime() {
        return new PropertyName<Timestamp>("endTime");
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
     * applicationのプロパティ名を返します。
     * 
     * @return applicationのプロパティ名
     */
    public static _ApplicationNames application() {
        return new _ApplicationNames("application");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _TermNames extends PropertyName<Term> {

        /**
         * インスタンスを構築します。
         */
        public _TermNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _TermNames(final String name) {
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
        public _TermNames(final PropertyName<?> parent, final String name) {
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
         * applicationIdのプロパティ名を返します。
         *
         * @return applicationIdのプロパティ名
         */
        public PropertyName<Long> applicationId() {
            return new PropertyName<Long>(this, "applicationId");
        }

        /**
         * startTimeのプロパティ名を返します。
         *
         * @return startTimeのプロパティ名
         */
        public PropertyName<Timestamp> startTime() {
            return new PropertyName<Timestamp>(this, "startTime");
        }

        /**
         * endTimeのプロパティ名を返します。
         *
         * @return endTimeのプロパティ名
         */
        public PropertyName<Timestamp> endTime() {
            return new PropertyName<Timestamp>(this, "endTime");
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
         * applicationのプロパティ名を返します。
         * 
         * @return applicationのプロパティ名
         */
        public _ApplicationNames application() {
            return new _ApplicationNames(this, "application");
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
