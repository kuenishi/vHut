/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import jp.co.ntts.vhut.entity.CommandOperation;

/**
 * DBからコマンドの引数/戻り値をバイトエンコードする際に発生する例外.
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
public class CommandByteEncodingRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -2840647680358225669L;

    private CommandOperation operation;


    /**
     * コンストラクタ.
     * @param operation オペレーションの種別
     */
    public CommandByteEncodingRuntimeException(CommandOperation operation) {
        this(operation, null);
    }

    /**
     * コンストラクタ.
     * @param operation オペレーションの種別
     * @param e 付帯する例外
     */
    public CommandByteEncodingRuntimeException(CommandOperation operation, Exception e) {
        super("ECOMM5021", new Object[]{ operation.name() }, e);
        this.operation = operation;
    }

    /**
     * @return the operation
     */
    public CommandOperation getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(CommandOperation operation) {
        this.operation = operation;
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
