/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import jp.co.ntts.vhut.entity.CommandNames._CommandNames;
import jp.co.ntts.vhut.entity.CommandTemplateMapNames._CommandTemplateMapNames;
import jp.co.ntts.vhut.entity.CommandVmMapNames._CommandVmMapNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Command}のプロパティ名の集合です。
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Generated(value = {"S2JDBC-Gen 2.4.43", "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl"}, date = "2011/07/15 22:08:01")
public class CommandNames {

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
     * operationのプロパティ名を返します。
     * 
     * @return operationのプロパティ名
     */
    public static PropertyName<CommandOperation> operation() {
        return new PropertyName<CommandOperation>("operation");
    }

    /**
     * parameterのプロパティ名を返します。
     * 
     * @return parameterのプロパティ名
     */
    public static PropertyName<byte[]> parameter() {
        return new PropertyName<byte[]>("parameter");
    }

    /**
     * resultのプロパティ名を返します。
     * 
     * @return resultのプロパティ名
     */
    public static PropertyName<byte[]> result() {
        return new PropertyName<byte[]>("result");
    }

    /**
     * statusのプロパティ名を返します。
     * 
     * @return statusのプロパティ名
     */
    public static PropertyName<CommandStatus> status() {
        return new PropertyName<CommandStatus>("status");
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
     * errorMessageのプロパティ名を返します。
     * 
     * @return errorMessageのプロパティ名
     */
    public static PropertyName<String> errorMessage() {
        return new PropertyName<String>("errorMessage");
    }

    /**
     * dependingCommandIdのプロパティ名を返します。
     * 
     * @return dependingCommandIdのプロパティ名
     */
    public static PropertyName<Long> dependingCommandId() {
        return new PropertyName<Long>("dependingCommandId");
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
     * dependingCommandのプロパティ名を返します。
     * 
     * @return dependingCommandのプロパティ名
     */
    public static _CommandNames dependingCommand() {
        return new _CommandNames("dependingCommand");
    }

    /**
     * commandListのプロパティ名を返します。
     * 
     * @return commandListのプロパティ名
     */
    public static _CommandNames commandList() {
        return new _CommandNames("commandList");
    }

    /**
     * commandTemplateMapListのプロパティ名を返します。
     * 
     * @return commandTemplateMapListのプロパティ名
     */
    public static _CommandTemplateMapNames commandTemplateMapList() {
        return new _CommandTemplateMapNames("commandTemplateMapList");
    }

    /**
     * commandVmMapListのプロパティ名を返します。
     * 
     * @return commandVmMapListのプロパティ名
     */
    public static _CommandVmMapNames commandVmMapList() {
        return new _CommandVmMapNames("commandVmMapList");
    }

    /**
     * @author S2JDBC-Gen
     */
    public static class _CommandNames extends PropertyName<Command> {

        /**
         * インスタンスを構築します。
         */
        public _CommandNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _CommandNames(final String name) {
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
        public _CommandNames(final PropertyName<?> parent, final String name) {
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
         * operationのプロパティ名を返します。
         *
         * @return operationのプロパティ名
         */
        public PropertyName<CommandOperation> operation() {
            return new PropertyName<CommandOperation>(this, "operation");
        }

        /**
         * parameterのプロパティ名を返します。
         *
         * @return parameterのプロパティ名
         */
        public PropertyName<byte[]> parameter() {
            return new PropertyName<byte[]>(this, "parameter");
        }

        /**
         * resultのプロパティ名を返します。
         *
         * @return resultのプロパティ名
         */
        public PropertyName<byte[]> result() {
            return new PropertyName<byte[]>(this, "result");
        }

        /**
         * statusのプロパティ名を返します。
         *
         * @return statusのプロパティ名
         */
        public PropertyName<CommandStatus> status() {
            return new PropertyName<CommandStatus>(this, "status");
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
         * errorMessageのプロパティ名を返します。
         *
         * @return errorMessageのプロパティ名
         */
        public PropertyName<String> errorMessage() {
            return new PropertyName<String>(this, "errorMessage");
        }

        /**
         * dependingCommandIdのプロパティ名を返します。
         *
         * @return dependingCommandIdのプロパティ名
         */
        public PropertyName<Long> dependingCommandId() {
            return new PropertyName<Long>(this, "dependingCommandId");
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
         * dependingCommandのプロパティ名を返します。
         * 
         * @return dependingCommandのプロパティ名
         */
        public _CommandNames dependingCommand() {
            return new _CommandNames(this, "dependingCommand");
        }

        /**
         * commandListのプロパティ名を返します。
         * 
         * @return commandListのプロパティ名
         */
        public _CommandNames commandList() {
            return new _CommandNames(this, "commandList");
        }

        /**
         * commandTemplateMapListのプロパティ名を返します。
         * 
         * @return commandTemplateMapListのプロパティ名
         */
        public _CommandTemplateMapNames commandTemplateMapList() {
            return new _CommandTemplateMapNames(this, "commandTemplateMapList");
        }

        /**
         * commandVmMapListのプロパティ名を返します。
         * 
         * @return commandVmMapListのプロパティ名
         */
        public _CommandVmMapNames commandVmMapList() {
            return new _CommandVmMapNames(this, "commandVmMapList");
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
