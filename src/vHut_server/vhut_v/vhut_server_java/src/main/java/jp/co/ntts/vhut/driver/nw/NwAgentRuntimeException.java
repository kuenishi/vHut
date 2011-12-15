/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.nw;

import jp.co.ntts.vhut.driver.AgentRuntimeException;

import org.apache.xmlrpc.XmlRpcException;

/**
 * <p>ネットワークドライバ内部エラー時の例外。
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
public class NwAgentRuntimeException extends AgentRuntimeException {

    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 604916312088689839L;

    private static final String NW_MESSAGE_CODE = "ENWDR5000";


    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     */
    private NwAgentRuntimeException(String messageCode) {
        super(messageCode);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    private NwAgentRuntimeException(String messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    private NwAgentRuntimeException(String messageCode, XmlRpcException cause) {
        super(messageCode, cause);
    }

    /**
     * 不正時の例外を得る.
     * @return 例外
     */
    public static NwAgentRuntimeException newException() {
        return new NwAgentRuntimeException(NW_MESSAGE_CODE);
    }

    /**
     * 不正時の例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static NwAgentRuntimeException newException(Throwable cause) {
        return new NwAgentRuntimeException(NW_MESSAGE_CODE, cause);
    }

    /**
     * 不正時の例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static NwAgentRuntimeException newException(XmlRpcException cause) {
        return new NwAgentRuntimeException(NW_MESSAGE_CODE, cause);
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
