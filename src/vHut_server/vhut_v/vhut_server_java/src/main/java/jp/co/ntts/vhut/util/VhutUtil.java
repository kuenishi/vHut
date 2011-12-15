/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import org.apache.commons.lang.StringUtils;

/**
 * Vhut固有の事象に対するユーティリティ（その他）.
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
public class VhutUtil {

    private VhutUtil() {
    }

    /**
     * VMの名称を生成します.
     * @param prefix 接頭辞
     * @param seed 生成のもとになる値
     * @return 名称
     */
    public static String createVmName(String prefix, long seed) {
        return createElementName(prefix, seed);
    }

    /**
     * テンプレートの名称を生成します.
     * @param prefix 接頭辞
     * @param seed 生成のもとになる値
     * @return 名称
     */
    public static String createTemplateName(String prefix, long seed) {
        return createElementName(prefix, seed);
    }

    /**
     * ネットワークアダプタの名称を生成します.
     * @param prefix 接頭辞
     * @param seed 生成のもとになる値
     * @return 名称
     */
    public static String createNetworkAdapterName(long seed) {
        return createElementName("nwa", seed);
    }

    /**
     * ネットワークアダプタテンプレートの名称を生成します.
     * @param prefix 接頭辞
     * @param seed 生成のもとになる値
     * @return 名称
     */
    public static String createNetworkAdapterTemplateName(long seed) {
        return createElementName("nwat", seed);
    }

    /**
     * Vhut管理対象の名称を取得します.
     * @param prefix 接頭辞
     * @param seed 生成のもとになる値
     * @return 名称
     */
    public static String createElementName(String prefix, long seed) {
        String hex = VhutLongConversionUtil.toHexString(seed, 16);
        return String.format("%s_%s", prefix, hex.substring(hex.length() - 8));
    }

    /**
     * Vhut管理対象の名称のうちサービスのプレフィックス部分を取得します.
     * @param prefix 接頭辞
     * @param privateId ローカルのID
     * @return サービスのプレフィックス部分
     */
    public static String createServicePrefix(String prefix, long privateId) {
        String privateIdString = new Long(privateId).toString();
        if (privateIdString.length() > 2) {
            privateIdString = privateIdString.substring(privateIdString.length() - 3);
        }
        String id = StringUtils.leftPad(privateIdString, 3, "0");
        return String.format("%s%s", prefix, id);
    }

    /**
     * ネットワークの名称を生成します.
     * @param prefix ネットワークの接頭辞
     * @param vlan VLAN番号
     * @return 名称
     */
    public static String createNetworkName(String prefix, short vlan) {
        String id = StringUtils.leftPad(Short.toString(vlan), 4, "0");
        return String.format("%s%s", prefix, id);
    }

    /**
     * 指定された文字列が指定された接頭辞を持つか検査します.
     * @param name 検査対象の文字列
     * @param prefix 接頭辞
     * @return 成否
     */
    public static boolean hasPrefix(String name, String prefix) {
        if (name == null || prefix == null)
            return false;
        if (name.length() < prefix.length())
            return false;
        return name.substring(0, prefix.length())
            .equals(prefix);
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
