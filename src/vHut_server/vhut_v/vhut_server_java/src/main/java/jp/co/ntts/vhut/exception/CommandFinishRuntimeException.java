/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import jp.co.ntts.vhut.command.ICommand;
import jp.co.ntts.vhut.entity.CommandOperation;

/**
 * <p>{@link ICommand}内のコマンド終了処理時にスローされる例外.
 * <br>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class CommandFinishRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -6584107699217534452L;

    private CommandOperation operation;

    private long commandId;


    /**
     * コンストラクタ.
     * @param operation オペレーションの種別
     * @param commandId 関連するコマンドID
     */
    public CommandFinishRuntimeException(CommandOperation operation, long commandId) {
        this(operation, commandId, null);
    }

    /**
     * コンストラクタ.
     * @param operation オペレーションの種別
     * @param commandId 関連するコマンドID
     * @param e 付帯する例外
     */
    public CommandFinishRuntimeException(CommandOperation operation, long commandId, Exception e) {
        super("ECOMM5012", new Object[]{ operation, commandId }, e);
        this.operation = operation;
        this.commandId = commandId;
    }

    /**
     * @return the operation
     */
    public CommandOperation getOperation() {
        return operation;
    }

    /**
     * @return the commandId
     */
    public long getCommandId() {
        return commandId;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(CommandOperation operation) {
        this.operation = operation;
    }

    /**
     * @param commandId the commandId to set
     */
    public void setCommandId(long commandId) {
        this.commandId = commandId;
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
