/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import org.apache.commons.lang.StringUtils;

/**
 * Long値の変換ロジックをまとめたユーティリティ.
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
public class VhutLongConversionUtil {

    private VhutLongConversionUtil() {

    }

    /**
     * ヘキサストリングへの変換.
     * @param value Long値
     * @param size パディング桁数（変換後の値がこれより短い場合は何もしない）
     * @return 文字列
     */
    public static String toHexString(long value, int size) {
        String hexValue = Long.toHexString(value);
        return StringUtils.leftPad(hexValue, size, '0');
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
