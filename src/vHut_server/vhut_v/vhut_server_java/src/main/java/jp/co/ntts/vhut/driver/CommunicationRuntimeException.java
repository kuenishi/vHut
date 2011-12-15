package jp.co.ntts.vhut.driver;

import jp.co.ntts.vhut.VhutModule;
import jp.co.ntts.vhut.exception.AbstractVhutRuntimeException;

/**
 * <p>通信障害時にスローされる例外.
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
public class CommunicationRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 604916312088689839L;

    private static final String MESSAGE_CODE_NUMBER = "1300";


    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     */
    private CommunicationRuntimeException(String messageCode) {
        super(messageCode);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    private CommunicationRuntimeException(String messageCode, Throwable cause) {
        super(messageCode, new Object[]{}, cause);
    }

    /**
     * インプット時の例外を得る.
     * @return 例外
     */
    public static final CommunicationRuntimeException newException() {
        return newException(VhutModule.COMM);
    }

    /**
     * インプット時の例外を得る.
     * @param module 関連するvHutモジュール
     * @return 例外
     */
    public static final CommunicationRuntimeException newException(VhutModule module) {
        return new CommunicationRuntimeException("E" + module.toString() + MESSAGE_CODE_NUMBER);
    }

    /**
     * インプット時の例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static final CommunicationRuntimeException newException(Throwable cause) {
        return newException(VhutModule.COMM, cause);
    }

    /**
     * インプット時の例外を得る.
     * @param module 関連するvHutモジュール
     * @param cause 起因する例外
     * @return 例外
     */
    public static final CommunicationRuntimeException newException(VhutModule module, Throwable cause) {
        return new CommunicationRuntimeException("E" + module.toString() + MESSAGE_CODE_NUMBER, cause);
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
