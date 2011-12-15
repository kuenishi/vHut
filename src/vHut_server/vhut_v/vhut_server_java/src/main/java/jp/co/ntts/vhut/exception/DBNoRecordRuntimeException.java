/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * DBにあるはずのレコードがない場合に生成される非検査例外.
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
public class DBNoRecordRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 982374726008975812L;

    private String detail;


    /**
     * @param messageCode メッセージコード
     * @param cause 例外
     */
    public DBNoRecordRuntimeException(String messageCode, Throwable cause) {
        this(messageCode, null, cause);
    }

    /**
     * @param detail 詳細
     */
    public DBNoRecordRuntimeException(String detail) {
        this(null, detail);
    }

    /**
     * @param messageCode メッセージコード
     * @param detail 詳細
     */
    public DBNoRecordRuntimeException(String messageCode, String detail) {
        this(messageCode, detail, null);
    }

    /**
     * @param messageCode メッセージコード
     * @param detail 詳細
     * @param cause 例外
     */
    public DBNoRecordRuntimeException(String messageCode, String detail, Throwable cause) {
        super(messageCode, new Object[]{ detail }, cause);
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
