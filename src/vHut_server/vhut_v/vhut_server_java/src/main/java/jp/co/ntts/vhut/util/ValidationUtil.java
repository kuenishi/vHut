/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

/**
 * 入力値検証ユーティリティ.
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
public class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * 文字列が指定した長さより短いことを検証します.
     * @param value 文字列
     * @param length 長さ
     * @throws Exception Validationが失敗した時の生成される
     */
    public static void isLengthLessThan(String value, int length) throws Exception {
        if (value.length() >= length) {
            throw new Exception(String.format("%s length has to be under %d.", value, length));
        }
    }

    /**
     * 文字列が指定した長さと等しいかそれより短いことを検証します.
     * @param value 文字列
     * @param length 長さ
     * @throws Exception Validationが失敗した時の生成される
     */
    public static void isLengthLessEqual(String value, int length) throws Exception {
        if (value.length() > length) {
            throw new Exception(String.format("%s length has to be under %d or equal to %d.", value, length, length));
        }
    }

    /**
     * 文字列が指定した文字だけで構成されていることを検証します.
     * @param value 文字列
     * @param regs 正規表現（"^[ここの中に入る記述]*$"）
     * @throws Exception Validationが失敗した時の生成される
     */
    public static void isIncludingOnly(String value, String regs) throws Exception {
        if (!value.matches(String.format("^[%s]*$", regs))) {
            throw new Exception(String.format("%s should match with regexpression /^[%s]*$/.", value, regs));
        }
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
