/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplateNames._ReleasedApplicationSecurityGroupTemplateNames;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateNames._ReleasedApplicationTemplateNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link ReleasedApplicationTemplateSecurityGroupMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class ReleasedApplicationTemplateSecurityGroupMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * releasedApplicationTemplateIdのプロパティ名を返します。
     * 
     * @return releasedApplicationTemplateIdのプロパティ名
     */
    public static PropertyName<Long> releasedApplicationTemplateId() {
        return new PropertyName<Long>("releasedApplicationTemplateId");
    }

    /**
     * releasedApplicationSecurityGroupTemplateIdのプロパティ名を返します。
     * 
     * @return releasedApplicationSecurityGroupTemplateIdのプロパティ名
     */
    public static PropertyName<Long> releasedApplicationSecurityGroupTemplateId() {
        return new PropertyName<Long>("releasedApplicationSecurityGroupTemplateId");
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
     * releasedApplicationTemplateのプロパティ名を返します。
     * 
     * @return releasedApplicationTemplateのプロパティ名
     */
    public static _ReleasedApplicationTemplateNames releasedApplicationTemplate() {
        return new _ReleasedApplicationTemplateNames("releasedApplicationTemplate");
    }

    /**
     * releasedApplicationSecurityGroupTemplateのプロパティ名を返します。
     * 
     * @return releasedApplicationSecurityGroupTemplateのプロパティ名
     */
    public static _ReleasedApplicationSecurityGroupTemplateNames releasedApplicationSecurityGroupTemplate() {
        return new _ReleasedApplicationSecurityGroupTemplateNames("releasedApplicationSecurityGroupTemplate");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _ReleasedApplicationTemplateSecurityGroupMapNames extends PropertyName<ReleasedApplicationTemplateSecurityGroupMap> {

        /**
         * インスタンスを構築します。
         */
        public _ReleasedApplicationTemplateSecurityGroupMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _ReleasedApplicationTemplateSecurityGroupMapNames(final String name) {
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
        public _ReleasedApplicationTemplateSecurityGroupMapNames(final PropertyName<?> parent, final String name) {
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
         * releasedApplicationTemplateIdのプロパティ名を返します。
         *
         * @return releasedApplicationTemplateIdのプロパティ名
         */
        public PropertyName<Long> releasedApplicationTemplateId() {
            return new PropertyName<Long>(this, "releasedApplicationTemplateId");
        }

        /**
         * releasedApplicationSecurityGroupTemplateIdのプロパティ名を返します。
         *
         * @return releasedApplicationSecurityGroupTemplateIdのプロパティ名
         */
        public PropertyName<Long> releasedApplicationSecurityGroupTemplateId() {
            return new PropertyName<Long>(this, "releasedApplicationSecurityGroupTemplateId");
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
         * releasedApplicationTemplateのプロパティ名を返します。
         * 
         * @return releasedApplicationTemplateのプロパティ名
         */
        public _ReleasedApplicationTemplateNames releasedApplicationTemplate() {
            return new _ReleasedApplicationTemplateNames(this, "releasedApplicationTemplate");
        }

        /**
         * releasedApplicationSecurityGroupTemplateのプロパティ名を返します。
         * 
         * @return releasedApplicationSecurityGroupTemplateのプロパティ名
         */
        public _ReleasedApplicationSecurityGroupTemplateNames releasedApplicationSecurityGroupTemplate() {
            return new _ReleasedApplicationSecurityGroupTemplateNames(this, "releasedApplicationSecurityGroupTemplate");
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
