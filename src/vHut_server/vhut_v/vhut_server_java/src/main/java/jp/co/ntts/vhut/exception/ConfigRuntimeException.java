/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * 設定ファイルの非検査例外.
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
public class ConfigRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -1766330843861776803L;

    private String configClass;

    private String method;

    private String detail;


    /**
     * コンストラクタ.
     * @param configClass 設定クラス
     * @param method 対象メソッド名
     * @param detail 詳細
     */
    public ConfigRuntimeException(Class configClass, String method, String detail) {
        this(null, configClass, method, detail);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param configClass 設定クラス
     * @param method 対象メソッド名
     * @param detail 詳細
     */
    public ConfigRuntimeException(String messageCode, Class configClass, String method, String detail) {
        super(messageCode, new Object[]{ configClass.getName(), method, detail });
        this.configClass = configClass.getName();
        this.method = method;
        this.detail = detail;
    }

    /**
     * @return the configClass
     */
    public String getConfigClass() {
        return configClass;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param configClass the configClass to set
     */
    public void setConfigClass(String configClass) {
        this.configClass = configClass;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
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
