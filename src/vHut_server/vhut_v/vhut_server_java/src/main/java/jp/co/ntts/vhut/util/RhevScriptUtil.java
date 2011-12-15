/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.util.ArrayList;
import java.util.List;

import jp.co.ntts.vhut.csv.NetworkCsv;
import jp.co.ntts.vhut.entity.Network;

/**
 * <p>RHEV用のスクリプトを作成するクラス.
 * (暫定的)
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
public class RhevScriptUtil {

    private RhevScriptUtil() {
    }

    /**
     * ネットワーク構築スクリプトのデータを作成します.
     * @param networkList ネットワークエンティティリスト
     * @param rhevCluster 対象クラスタ名
     * @return CSVのリスト
     */
    public static List<NetworkCsv> createNetworkScript(List<Network> networkList, String rhevCluster) {
        List<NetworkCsv> resultList = new ArrayList<NetworkCsv>();
        for (Network network : networkList) {
            NetworkCsv csv = new NetworkCsv();
            csv.name = network.name;
            csv.vlan = network.vlan;
            csv.address = Ipv4ConversionUtil.convertHexToDot(network.networkAddress);
            csv.mask = Ipv4ConversionUtil.convertHexToDot(network.mask);
            csv.broadcast = Ipv4ConversionUtil.convertHexToDot(network.broadcast);
            csv.gateway = Ipv4ConversionUtil.convertHexToDot(network.gateway);
            csv.dhcp = Ipv4ConversionUtil.convertHexToDot(network.dhcp);
            csv.dns = Ipv4ConversionUtil.convertHexToDot(network.dns);
            csv.clusterName = rhevCluster;
            resultList.add(csv);
        }
        return resultList;
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
