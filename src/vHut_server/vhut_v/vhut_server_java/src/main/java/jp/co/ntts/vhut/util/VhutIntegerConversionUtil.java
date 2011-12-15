/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import org.seasar.framework.util.IntegerConversionUtil;

/**
 * <p>型変換に関する共通処理を提供します.
 * <br>
 * <p>{@link IntegerConversionUtil}をラップしています。
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
public class VhutIntegerConversionUtil {

    /**
     * 非公開コンストラクター.
     */
    protected VhutIntegerConversionUtil() {

    }

    /**
     * {@link Integer}に変換します.
     * 
     * @param o 返還対象
     * @param d デフォルト値
     * @return {@link Integer}
     */
    public static Integer toInteger(Object o, Integer d) {
        Integer value = IntegerConversionUtil.toInteger(o, null);
        return value == null ? d : value;
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
