/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * 当該関数の引数が想定外の場合に発生する例外.
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
public class InputRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 4402846803688072958L;

    private String param;

    private String detail;


    /**
     * コンストラクタ.
     * @param param 引数名
     * @param detail 詳細
     */
    public InputRuntimeException(String param, String detail) {
        this(null, param, detail);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param param 引数名
     * @param detail 詳細
     */
    public InputRuntimeException(String messageCode, String param, String detail) {
        super(messageCode, new Object[]{ param, detail });
        this.param = param;
        this.detail = detail;
    }

    /**
     * @return the param
     */
    public String getParam() {
        return param;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param param the param to set
     */
    public void setParam(String param) {
        this.param = param;
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
