/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

/**
 * <p>MACアドレス変換ユーティリティ.
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
public class MacConversionUtil {

    private MacConversionUtil() {
    }

    /**
     * マックアドレスの開始アドレスと終了アドレスを含むアドレスの数を取得します.
     * @param macStart マックアドレス(開始)
     * @param macEnd マックアドレス(終了)
     * @return アドレス数
     */
    public static int getCount(String macStart, String macEnd) {
        BigInteger biMacStart = new BigInteger(addrToByte(macStart));
        BigInteger biMacEnd = new BigInteger(addrToByte(macEnd));
        return biMacEnd.subtract(biMacStart)
            .intValue() + 1;
    }

    /**
     * 対象のマックアドレスが開始アドレスと終了アドレスの間にあることを確認します.
     * @param mac 対象のマックアドレス
     * @param macStart 開始アドレス
     * @param macEnd 終了アドレス
     * @return 間にあるときtrue
     */
    public static boolean isScorp(String mac, String macStart, String macEnd) {
        int front = getCount(macStart, mac);
        int back = getCount(mac, macEnd);
        return front > 0 && back > 0;
    }

    /**
     * 開始アドレスからのアドレス順位を求めます.
     * @param mac 対象アドレス
     * @param macStart 開始アドレス
     * @return アドレス順位
     */
    public static int getMacAddressOrder(String mac, String macStart) {
        BigInteger biMacTarget = new BigInteger(addrToByte(mac));
        BigInteger biMacStart = new BigInteger(addrToByte(macStart));
        return biMacTarget.subtract(biMacStart)
            .intValue();
    }

    /**
     * 開始アドレスと順位からマックアドレスをバイト列で取得します.
     * @param macStart 開始アドレス.
     * @param count アドレス順位
     * @return マックアドレス
     */
    public static byte[] getMacAddressWithOrderAsByte(String macStart, int count) {
        BigInteger biMacStart = new BigInteger(addrToByte(macStart));
        BigInteger biSub = BigInteger.valueOf(count);
        byte[] bMacTarget = biMacStart.add(biSub)
            .toByteArray();
        byte[] result = new byte[6];
        int delta = 6 - bMacTarget.length;
        for (int i = 0; i < 6; i++) {
            if (i >= delta) {
                result[i] = bMacTarget[i - delta];
            } else {
                result[i] = (byte) 0;
            }
        }
        return result;
    }

    /**
     * バイト列で表されたマックアドレスを文字列に変換します.
     * @param baddr バイト列のマックアドレス
     * @return 文字列のマックアドレス
     */
    public static String byteToAddr(byte[] baddr) {
        String s = "";
        for (int i = 0; i < 6; i++) {
            s += StringUtils.leftPad(Integer.toHexString(convertToInt(baddr[i]))
                .toUpperCase(), 2, "0");
        }
        return s;
    }

    private static byte[] addrToByte(String addr) {
        byte[] b = new byte[6];
        String[] addrs = addr.split(":");
        if (addrs.length == 1) {
            for (int i = 0; i < 6; i++) {
                b[i] = (byte) Integer.parseInt(addr.substring(i * 2, i * 2 + 2), 16);
            }
        } else if (addrs.length == 6) {
            for (int i = 0; i < 6; i++) {
                b[i] = (byte) Integer.parseInt(addrs[i], 16);
            }
        }
        return b;
    }

    /**
     * byteをintに変換します.
     * @param b ホストアドレス
     * @return 順位
     */
    static int convertToInt(byte b) {
        Integer intValue = new Byte(b).intValue();
        if (intValue < 0) {
            intValue += 256;
        }
        return intValue;
    }

    /**
     * HEX表記をコロン表記に変換します.
     * @param s HEX表記のMACアドレス
     * @return ドット表記のMACアドレス
     */
    public static String convertHexToColon(String s) {
        Object[] addrs = new Object[6];
        for (int i = 0; i < 6; i++) {
            addrs[i] = s.substring(i * 2, i * 2 + 2);
        }
        return StringUtils.join(addrs, ":");
    }

    /**
     * HEX表記をコロン表記に変換します.
     * @param s HEX表記のMACアドレス
     * @return ドット表記のMACアドレス
     */
    public static String convertColonToHex(String s) {
        return byteToAddr(addrToByte(s));
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
