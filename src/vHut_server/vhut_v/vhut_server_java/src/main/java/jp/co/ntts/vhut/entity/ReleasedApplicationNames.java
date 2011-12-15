/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ApplicationInstanceNames._ApplicationInstanceNames;
import jp.co.ntts.vhut.entity.ApplicationNames._ApplicationNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplateNames._ReleasedApplicationSecurityGroupTemplateNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateNames._ReleasedApplicationTemplateNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ReleasedApplication}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ReleasedApplicationNames {

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
     * createdTimeのプロパティ名を返します。
     * 
     * @return createdTimeのプロパティ名
     */
    public static PropertyName<Timestamp> createdTime() {
        return new PropertyName<Timestamp>("createdTime");
    }

    /**
     * structureのプロパティ名を返します。
     * 
     * @return structureのプロパティ名
     */
    public static PropertyName<String> structure() {
        return new PropertyName<String>("structure");
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
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<ReleasedApplicationStatus> status() {
        return new PropertyName<ReleasedApplicationStatus>("status");
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
     * releasedApplicationTemplateListのプロパティ名を返します。
     * 
     * @return releasedApplicationTemplateListのプロパティ名
     */
    public static _ReleasedApplicationTemplateNames releasedApplicationTemplateList() {
        return new _ReleasedApplicationTemplateNames("releasedApplicationTemplateList");
    }

    /**
     * releasedApplicationSecurityGroupTemplateListのプロパティ名を返します。
     * 
     * @return releasedApplicationSecurityGroupTemplateListのプロパティ名
     */
    public static _ReleasedApplicationSecurityGroupTemplateNames releasedApplicationSecurityGroupTemplateList() {
        return new _ReleasedApplicationSecurityGroupTemplateNames("releasedApplicationSecurityGroupTemplateList");
    }

    /**
     * applicationInstanceListのプロパティ名を返します。
     * 
     * @return applicationInstanceListのプロパティ名
     */
    public static _ApplicationInstanceNames applicationInstanceList() {
        return new _ApplicationInstanceNames("applicationInstanceList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ReleasedApplicationNames extends PropertyName<ReleasedApplication> {

        /**
         * インスタンスを構築します。
         */
        public _ReleasedApplicationNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ReleasedApplicationNames(final String name) {
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
        public _ReleasedApplicationNames(final PropertyName<?> parent, final String name) {
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
         * createdTimeのプロパティ名を返します。
         *
         * @return createdTimeのプロパティ名
         */
        public PropertyName<Timestamp> createdTime() {
            return new PropertyName<Timestamp>(this, "createdTime");
        }

        /**
         * structureのプロパティ名を返します。
         *
         * @return structureのプロパティ名
         */
        public PropertyName<String> structure() {
            return new PropertyName<String>(this, "structure");
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
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<ReleasedApplicationStatus> status() {
            return new PropertyName<ReleasedApplicationStatus>(this, "status");
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

        /**
         * releasedApplicationTemplateListのプロパティ名を返します。
         * 
         * @return releasedApplicationTemplateListのプロパティ名
         */
        public _ReleasedApplicationTemplateNames releasedApplicationTemplateList() {
            return new _ReleasedApplicationTemplateNames(this, "releasedApplicationTemplateList");
        }

        /**
         * releasedApplicationSecurityGroupTemplateListのプロパティ名を返します。
         * 
         * @return releasedApplicationSecurityGroupTemplateListのプロパティ名
         */
        public _ReleasedApplicationSecurityGroupTemplateNames releasedApplicationSecurityGroupTemplateList() {
            return new _ReleasedApplicationSecurityGroupTemplateNames(this, "releasedApplicationSecurityGroupTemplateList");
        }

        /**
         * applicationInstanceListのプロパティ名を返します。
         * 
         * @return applicationInstanceListのプロパティ名
         */
        public _ApplicationInstanceNames applicationInstanceList() {
            return new _ApplicationInstanceNames(this, "applicationInstanceList");
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
