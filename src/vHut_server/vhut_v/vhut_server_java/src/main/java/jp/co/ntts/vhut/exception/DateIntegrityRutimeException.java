/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * 日付の指定が意図していない形式になっていた際に発生する例外です.
 *
 * @version 1.0.0
 * @author nota
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class DateIntegrityRutimeException extends AbstractVhutRuntimeException {

    /** 開始日時がNULL. */
    public static final String NULL_START = "start is null";
    /** 終了日時がNULL. */
    public static final String NULL_END = "end is null";
    /** 開始日時と終了日時の順序性が不正 */
    public static final String START_AFTER_END = "start is after end";
    /** 開始日時と終了日時が同じ */
    public static final String START_EQUALS_END = "start and end are just same";

    /**
     * シリアル.
     */
    static final long serialVersionUID = 0L;


    /**
     * @param arg0
     */
    public DateIntegrityRutimeException(String arg0) {
        this(null, arg0);
    }

    /**
     * @param arg0
     */
    public DateIntegrityRutimeException(String messageCode, String arg0) {
        super(messageCode, new Object[]{ arg0 });
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
