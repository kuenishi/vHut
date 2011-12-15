/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import jp.co.ntts.vhut.exception.AbstractVhutRuntimeException;

import org.apache.xmlrpc.XmlRpcException;

/**
 * <p>エージェントの内部エラー
 * <br>
 * <p>
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
public class AgentRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -597940339672941952L;

    private static final String COMM_MESSAGE_CODE = "ECOMM5000";

    private int faultCode;

    private String faultString;


    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     */
    protected AgentRuntimeException(String messageCode) {
        super(messageCode);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    protected AgentRuntimeException(String messageCode, Throwable cause) {
        super(messageCode, new Object[]{}, cause);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    protected AgentRuntimeException(String messageCode, XmlRpcException cause) {
        super(messageCode, new Object[]{ cause.code, cause.getMessage() }, cause);
        faultCode = cause.code;
        faultString = cause.getMessage();
    }

    /**
     * 不正時の例外を得る.
     * @return 例外
     */
    public static AgentRuntimeException newException() {
        return new AgentRuntimeException(COMM_MESSAGE_CODE);
    }

    /**
     * 不正時の例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static AgentRuntimeException newException(Throwable cause) {
        return new AgentRuntimeException(COMM_MESSAGE_CODE, cause);
    }

    /**
     * @return the faultCode
     */
    public int getFaultCode() {
        return faultCode;
    }

    /**
     * @return the faultString
     */
    public String getFaultString() {
        return faultString;
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
