/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import jp.co.ntts.vhut.exception.AbstractVhutRuntimeException;

/**
 * <p>ドライバ内部エラー時の例外。
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
public class DriverSQLRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 604916312088689839L;

    private static final String CONFIG_MESSAGE_CODE = "EDRIV6000";


    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     */
    private DriverSQLRuntimeException(String messageCode) {
        super(messageCode);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    private DriverSQLRuntimeException(String messageCode, Throwable cause) {
        super(messageCode, new Object[]{}, cause);
    }

    /**
     * 例外を得る.
     * @return 例外
     */
    public static final DriverSQLRuntimeException newException() {
        return new DriverSQLRuntimeException(CONFIG_MESSAGE_CODE);
    }

    /**
     * 例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static final DriverSQLRuntimeException newException(Throwable cause) {
        return new DriverSQLRuntimeException(CONFIG_MESSAGE_CODE, cause);
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
