/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * vHutモジュールの当該クラス内で発生したエラーをラップする例外クラスです.
 * 生成時はメッセージコードを省略することができます
 * 省略した場合、各モジュールのVhutExceptionInterceptorでモジュールのメッセージコードが付加されます.
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
public class InternalRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 5864202803153603557L;

    private String detail;


    /**
     * コンストラクタ.
     * @param cause 起因する非検査例外
     */
    public InternalRuntimeException(RuntimeException cause) {
        this(null, cause);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード（モジュールID+通番4桁）
     * @param cause 起因する非検査例外
     */
    public InternalRuntimeException(String messageCode, RuntimeException cause) {
        this(messageCode, null, cause);
    }

    /**
     * @param detail 詳細
     */
    public InternalRuntimeException(String detail) {
        this(null, detail);
    }

    /**
     * @param messageCode メッセージコード（モジュールID+通番4桁）
     * @param detail 詳細
     */
    public InternalRuntimeException(String messageCode, String detail) {
        this(messageCode, detail, null);
    }

    /**
     * @param messageCode メッセージコード（モジュールID+通番4桁）
     * @param detail 詳細
     * @param cause 起因する非検査例外
     */
    public InternalRuntimeException(String messageCode, String detail, RuntimeException cause) {
        super(messageCode, new Object[]{ detail }, cause);
        this.detail = detail;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
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
