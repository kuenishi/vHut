package jp.co.ntts.vhut.driver;

import jp.co.ntts.vhut.exception.AbstractVhutRuntimeException;

/**
 * <p>データ検証時にスローされる例外.
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
public class ValidationRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル
     */
    private static final long serialVersionUID = 1L;

    private static final String INPUT_MESSAGE_CODE = "EDRIV2100";

    private static final String OUTPUT_MESSAGE_CODE = "EDRIV2200";


    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     */
    private ValidationRuntimeException(String messageCode) {
        super(messageCode);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param cause 起因する例外
     */
    private ValidationRuntimeException(String messageCode, Throwable cause) {
        super(messageCode, new Object[]{}, cause);
    }

    /**
     * インプット時の例外を得る.
     * @return 例外
     */
    public static final ValidationRuntimeException newInputException() {
        return new ValidationRuntimeException(INPUT_MESSAGE_CODE);
    }

    /**
     * インプット時の例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static final ValidationRuntimeException newInputException(Throwable cause) {
        return new ValidationRuntimeException(INPUT_MESSAGE_CODE, cause);
    }

    /**
     * アウトプット時の例外を得る.
     * @return 例外
     */
    public static final ValidationRuntimeException newOutputException() {
        return new ValidationRuntimeException(OUTPUT_MESSAGE_CODE);
    }

    /**
     * アウトプット時の例外を得る.
     * @param cause 起因する例外
     * @return 例外
     */
    public static final ValidationRuntimeException newOutputException(Throwable cause) {
        return new ValidationRuntimeException(OUTPUT_MESSAGE_CODE, cause);
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
