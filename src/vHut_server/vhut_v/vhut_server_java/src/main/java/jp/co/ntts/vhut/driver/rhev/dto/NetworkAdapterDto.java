/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.Map;

/**
 * <p>
 * NetworkAdapterDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class NetworkAdapterDto {

    public static final String NETWORK_ADAPTER_ID = "networkAdapterId";
    public static final String NAME = "name";
    public static final String MAC_ADDRESS = "macAddress";
    public static final String NETWORK_ID = "networkId";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param map Map型データ
     * @return dto型データ
     * @throws ClassCastException クラスキャスト時の例外
     */
    public static NetworkAdapterDto newNetworkAdapterDto(Map<String, Object> map) throws ClassCastException {
        NetworkAdapterDto dto = new NetworkAdapterDto();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            if (key.equals(NETWORK_ADAPTER_ID)) {
                dto.networkAdapterId = (String) entry.getValue();
            } else if (key.equals(NAME)) {
                dto.name = (String) entry.getValue();
            } else if (key.equals(MAC_ADDRESS)) {
                dto.macAddress = (String) entry.getValue();
            } else if (key.equals(NETWORK_ID)) {
                dto.networkId = (String) entry.getValue();
            }
        }
        return dto;
    }


    public String networkAdapterId;
    public String name;
    public String macAddress;
    public String networkId;
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
