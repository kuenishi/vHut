/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * <p>IPアドレスの変換ユーティリティ.
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
public class Ipv4ConversionUtil {

    private Ipv4ConversionUtil() {

    }


    /**
     * @private
     */
    private static byte[] FULL_BYTES;


    /**
     * @return 255.255.255.255
     */
    public static byte[] getFullBytes() {
        if (FULL_BYTES == null) {
            FULL_BYTES = addrTobyte("FFFFFFFF");
        }
        return FULL_BYTES;
    }

    /**
     * サブネットマスク検査.
     * 検査したいＩＰアドレス、セグメントＩＰアドレス、マスク長を指定して、
     * サブネットマスクしたＩＰアドレスの範囲であるかを検査する。
     * @param ipaddr 検査したいＩＰアドレス文字列、"xxxxxxxx" HEX(区切りなし)形式
     * @param segaddr セグメントＩＰアドレス、"xxxxxxxx" HEX(区切りなし)形式
     * @param maskaddr マスクアドレス、"xxxxxxxx" HEX(区切りなし)形式
     * @return true=OK,false=NG
     */
    public static boolean isScorp(String ipaddr, String segaddr, String maskaddr) {
        if (ipaddr == null) {
            return false;
        }
        byte[] bipaddr = addrTobyte(ipaddr);
        byte[] bmaskaddr = addrTobyte(maskaddr);
        byte[] bsegaddr = addrTobyte(segaddr);
        byte[] maskedBipaddr = byteAND(bipaddr, bmaskaddr);
        byte[] maskedBsegaddr = byteAND(bsegaddr, bmaskaddr);
        return !convertToBoolean(byteXOR(maskedBipaddr, maskedBsegaddr));
    }

    /**
     * ホストアドレスをbyte[]で取得します.
     * @param ipaddr IPアドレス
     * @param maskaddr ネットマスクアドレス
     * @return ホストアドレス
     */
    public static byte[] getHostAddressAsByte(String ipaddr, String maskaddr) {
        byte[] bipaddr = addrTobyte(ipaddr);
        byte[] bmaskaddr = addrTobyte(maskaddr);
        return getHostAddressAsByte(bipaddr, bmaskaddr);
    }

    /**
     * ホストアドレスをbyte[]で取得します.
     * @param bipaddr IPアドレス
     * @param bmaskaddr ネットマスクアドレス
     * @return ホストアドレス
     */
    public static byte[] getHostAddressAsByte(byte[] bipaddr, byte[] bmaskaddr) {
        byte[] reversedBmaskaddr = byteXOR(bmaskaddr, getFullBytes());
        return byteAND(bipaddr, reversedBmaskaddr);
    }

    /**
     * ホストアドレスの順位を取得します.
     * @param ipaddr IPアドレス
     * @param maskaddr ネットマスクアドレス
     * @return ホストアドレスの順位 (ネットワークアドレス=0)
     */
    public static int getHostAddressOrder(String ipaddr, String maskaddr) {
        byte[] bipaddr = addrTobyte(ipaddr);
        byte[] bmaskaddr = addrTobyte(maskaddr);
        return getHostAddressOrder(bipaddr, bmaskaddr);
    }

    /**
     * ホストアドレスの順位を取得します.
     * @param bipaddr IPアドレス
     * @param bmaskaddr ネットマスクアドレス
     * @return ホストアドレスの順位 (ネットワークアドレス=0)
     */
    public static int getHostAddressOrder(byte[] bipaddr, byte[] bmaskaddr) {
        byte[] bhostaddr = getHostAddressAsByte(bipaddr, bmaskaddr);
        return convertToInt(bhostaddr);
    }

    /**
     * セグメント内に収容可能なホストアドレスの数を取得します.
     * ネットワークアドレスとブロードキャストを含みます
     * @param maskaddr ネットマスクアドレス
     * @return セグメント内に収容可能なホストアドレスの数
     */
    public static int getHostAddressCount(String maskaddr) {
        byte[] bmaskaddr = addrTobyte(maskaddr);
        return getHostAddressCount(bmaskaddr);
    }

    /**
     * 開始アドレスと終了アドレスを含むIPアドレスの数を得ます.
     * @param startIpaddr 開始アドレス(HEX/ドット)
     * @param endIpaddr 終了アドレス(HEX/ドット)
     * @return IPアドレスの数
     */
    public static int getHostAddressCount(String startIpaddr, String endIpaddr) {
        BigInteger startBI = new BigInteger(addrTobyte(startIpaddr));
        BigInteger endBI = new BigInteger(addrTobyte(endIpaddr));
        return (int) Math.min(endBI.subtract(startBI)
            .longValue() + 1, Integer.MAX_VALUE);
    }

    /**
     * セグメント内に収容可能なホストアドレスの数を取得します.
     * ネットワークアドレスとブロードキャストを含みます
     * @param bmaskaddr ネットマスクアドレス
     * @return セグメント内に収容可能なホストアドレスの数
     */
    public static int getHostAddressCount(byte[] bmaskaddr) {
        byte[] reversedBmaskaddr = byteXOR(bmaskaddr, getFullBytes());
        return convertToInt(reversedBmaskaddr) + 1;
    }

    /**
     * セグメント、マスク、順位からIPアドレスをbyte[] で取得します.
     * @param bsegaddr セグメント
     * @param bmaskaddr マスク
     * @param order 順位(0=1番目, 1=2番目, -1=後ろから1番目)
     * @return IPアドレス
     */
    public static byte[] getIpAddressWithOrderAsByte(byte[] bsegaddr, byte[] bmaskaddr, int order) {
        int length = getHostAddressCount(bmaskaddr);
        byte[] maskedBsegaddr = byteAND(bsegaddr, bmaskaddr);
        byte[] bhostaddr = convertOrderToByte(order % length);
        return byteOR(maskedBsegaddr, bhostaddr);
    }

    /**
     * セグメント、マスク、順位からIPアドレスをbyte[] で取得します.
     * @param segaddr セグメント
     * @param maskaddr マスク
     * @param order 順位
     * @return IPアドレス
     */
    public static byte[] getIpAddressWithOrderAsByte(String segaddr, String maskaddr, int order) {
        byte[] bsegaddr = addrTobyte(segaddr);
        byte[] bmaskaddr = addrTobyte(maskaddr);
        return getIpAddressWithOrderAsByte(bsegaddr, bmaskaddr, order);
    }

    /**
     * ＩＰアドレス文字列、"xxxxxxxx" HEX(区切りなし)形式 → byte[].
     * @param s ＩＰアドレス文字列
     * @return byte[]
     */
    public static byte[] addrTobyte(String s) {
        byte[] b = new byte[4];
        String[] addrs = s.split("\\.");
        if (addrs.length == 1) {
            for (int i = 0; i < 4; i++) {
                b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
            }
        } else if (addrs.length == 4) {
            for (int i = 0; i < 4; i++) {
                b[i] = (byte) Integer.parseInt(addrs[i]);
            }
        }
        return b;
    }

    /**
     * ＩＰアドレスbyte[] → ＩＰアドレス文字列"xxxxxxxx" HEX(区切りなし)形式.
     * @param b ＩＰアドレス
     * @return s ＩＰアドレス文字列"xxxxxxxx" HEX(区切りなし)形式
     */
    public static String byteToAddr(byte[] b) {
        String s = "";
        for (int i = 0; i < 4; i++) {
            s += StringUtils.leftPad(Integer.toHexString(convertToInt(b[i]))
                .toUpperCase(), 2, "0");
        }
        return s;
    }

    /**
     * @param length マスク長
     * @return ネットマスクアドレス
     */
    public static byte[] getMask(int length) {
        byte[] b = new BigInteger(1, getFullBytes()).shiftRight(length)
            .xor(new BigInteger(1, getFullBytes()))
            .toByteArray();
        byte[] br = new byte[4];
        for (int i = 0; i < br.length; i++) {
            br[i] = b[i + 1];
        }
        return br;
    }

    /**
     * バイト配列のAND演算.
     * @param b1 バイト配列
     * @param b2 バイト配列
     * @return バイト配列
     */
    static byte[] byteAND(byte[] b1, byte[] b2) {
        byte[] r = new byte[b1.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) (b1[i] & b2[i]);
        }
        return r;
    }

    /**
     * バイト配列のOR演算.
     * @param b1 バイト配列
     * @param b2 バイト配列
     * @return バイト配列
     */
    static byte[] byteOR(byte[] b1, byte[] b2) {
        byte[] r = new byte[b1.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) (b1[i] | b2[i]);
        }
        return r;
    }

    /**
     * バイト配列のXOR演算.
     * @param b1 バイト配列
     * @param b2 バイト配列
     * @return バイト配列
     */
    static byte[] byteXOR(byte[] b1, byte[] b2) {
        byte[] r = new byte[b1.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) (b1[i] ^ b2[i]);
        }
        return r;
    }

    /**
     * バイト配列のXOR演算.
     * @param b バイト配列
     * @return バイト配列
     */
    static boolean convertToBoolean(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            if (b[i] > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 順位をホストアドレス(byte[])に変換します.
     * @param order 順位
     * @return ホストアドレス(byte[])
     */
    static byte[] convertOrderToByte(int order) {
        byte[] baddr = new byte[4];
        baddr[0] = (byte) ((order >> 24) & 255);
        baddr[1] = (byte) ((order >> 16) & 255);
        baddr[2] = (byte) ((order >> 8) & 255);
        baddr[3] = (byte) (order & 255);
        return baddr;
    }

    /**
     * ホストアドレス(byte[])を順位に変換します.
     * @param baddr ホストアドレス
     * @return 順位
     */
    static int convertToInt(byte[] baddr) {
        BigInteger order = new BigInteger(baddr);
        return order.intValue();
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
     * HEX表記をドット表記に変換します.
     * @param s HEX表記のIPアドレス
     * @return ドット表記のIPアドレス
     */
    public static String convertHexToDot(String s) {
        Object[] addrs = new Object[4];
        for (int i = 0; i < 4; i++) {
            addrs[i] = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return StringUtils.join(addrs, ".");
    }

    /**
     * ドット表記をHEX表記に変換します.
     * @param ipaddr アドレス(ドット表記)
     * @return アドレス(HEX表記)
     */
    public static String convertDotToHex(String ipaddr) {
        return byteToAddr(addrTobyte(ipaddr));
    }

    /**
     * IPアドレス(HEX表記)の配列を得ます.
     * 配列の長さがintの正の最大値を超える場合、後ろのほうを無視します.
     * @param startIpaddr 開始アドレス(HEX/ドット)
     * @param endIpaddr 終了アドレス(HEX/ドット)
     * @return IPアドレス(HEX表記)の配列
     */
    public static Set<String> getIpAddressSetBetween(String startIpaddr, String endIpaddr) {
        BigInteger startBI = new BigInteger(addrTobyte(startIpaddr));
        BigInteger endBI = new BigInteger(addrTobyte(endIpaddr));
        int length = (int) Math.min(endBI.subtract(startBI)
            .longValue(), Integer.MAX_VALUE);

        Set<String> resultSet = new HashSet<String>();

        BigInteger currentBI = startBI;

        for (int i = 0; i <= length; i++) {
            resultSet.add(byteToAddr(currentBI.toByteArray()));
            currentBI = currentBI.add(BigInteger.ONE);
        }

        return resultSet;
    }

    /**
     * 指定したIPが所属するNWアドレスをバイト列で返却します.
     * @param bAddr IPアドレス（バイト列）
     * @param bMask マスク(バイト列)
     * @return ネットワークアドレス（バイト列）
     */
    public static byte[] getNetworkAddressAsByte(byte[] bAddr, byte[] bMask) {
        return byteAND(bAddr, bMask);
    }

    /**
     * 指定したIPが所属するNWアドレスをバイト列で返却します.
     * @param bAddr IPアドレス（バイト列）
     * @param bMask マスク(バイト列)
     * @return ネットワークアドレス（バイト列）
     */
    public static byte[] getNextNetworkAddressAsByte(byte[] bAddr, byte[] bMask) {
        BigInteger current = new BigInteger(getNetworkAddressAsByte(bAddr, bMask));
        BigInteger reversedMask = new BigInteger(byteXOR(bMask, getFullBytes()));
        return current.add(reversedMask)
            .add(BigInteger.ONE)
            .toByteArray();
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
