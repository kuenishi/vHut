/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.CommandNames._CommandNames;
import jp.co.ntts.vhut.entity.TemplateNames._TemplateNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link CommandTemplateMap}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class CommandTemplateMapNames {

    /**
     * idのプロパティ名を返します。
     * 
     * @return idのプロパティ名
     */
    public static PropertyName<Long> id() {
        return new PropertyName<Long>("id");
    }

    /**
     * commandIdのプロパティ名を返します。
     * 
     * @return commandIdのプロパティ名
     */
    public static PropertyName<Long> commandId() {
        return new PropertyName<Long>("commandId");
    }

    /**
     * templateIdのプロパティ名を返します。
     * 
     * @return templateIdのプロパティ名
     */
    public static PropertyName<Long> templateId() {
        return new PropertyName<Long>("templateId");
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
     * commandのプロパティ名を返します。
     * 
     * @return commandのプロパティ名
     */
    public static _CommandNames command() {
        return new _CommandNames("command");
    }

    /**
     * templateのプロパティ名を返します。
     * 
     * @return templateのプロパティ名
     */
    public static _TemplateNames template() {
        return new _TemplateNames("template");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _CommandTemplateMapNames extends PropertyName<CommandTemplateMap> {

        /**
         * インスタンスを構築します。
         */
        public _CommandTemplateMapNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _CommandTemplateMapNames(final String name) {
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
        public _CommandTemplateMapNames(final PropertyName<?> parent, final String name) {
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
         * commandIdのプロパティ名を返します。
         *
         * @return commandIdのプロパティ名
         */
        public PropertyName<Long> commandId() {
            return new PropertyName<Long>(this, "commandId");
        }

        /**
         * templateIdのプロパティ名を返します。
         *
         * @return templateIdのプロパティ名
         */
        public PropertyName<Long> templateId() {
            return new PropertyName<Long>(this, "templateId");
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
         * commandのプロパティ名を返します。
         * 
         * @return commandのプロパティ名
         */
        public _CommandNames command() {
            return new _CommandNames(this, "command");
        }

        /**
         * templateのプロパティ名を返します。
         * 
         * @return templateのプロパティ名
         */
        public _TemplateNames template() {
            return new _TemplateNames(this, "template");
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
