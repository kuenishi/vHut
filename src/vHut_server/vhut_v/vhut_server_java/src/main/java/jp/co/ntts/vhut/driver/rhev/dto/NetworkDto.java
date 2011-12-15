/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.Map;

/**
 * <p>
 * NetworkDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class NetworkDto {

    private static final String NETWORK_ID = "networkId";
    private static final String VLAN = "vlan";
    private static final String NETWORK_ADDRESS = "networkAddress";
    private static final String SUBNET = "subnet";
    private static final String GATEWAY = "gateway";
    private static final String STATUS = "status";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param map Map型データ
     * @return HostDto型データ
     * @throws ClassCastException クラスキャスト時の例外
     */
    public static NetworkDto newNetworkDto(Map<String, Object> map) throws ClassCastException {
        NetworkDto dto = new NetworkDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            if (key.equals(NETWORK_ID)) {
                dto.networkId = (String) entry.getValue();

            } else if (key.equals(VLAN)) {
                dto.vlan = (Integer) entry.getValue();
            } else if (key.equals(NETWORK_ADDRESS)) {
                dto.networkAddress = (String) entry.getValue();

            } else if (key.equals(SUBNET)) {
                dto.subnet = (String) entry.getValue();

            } else if (key.equals(GATEWAY)) {
                dto.gateway = (String) entry.getValue();

            } else if (key.equals(STATUS)) {
                dto.status = (String) entry.getValue();
            }
        }
        return dto;
    }


    public String networkId;
    public int vlan;
    public String networkAddress;
    public String subnet;
    public String gateway;
    public String status;

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
