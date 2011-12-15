/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.Map;

import jp.co.ntts.vhut.driver.ConversionRuntimeException;

/**
 * <p>
 * ClusterDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class ClusterDto {

    public static final String CLUSTER_ID = "clusterId";
    public static final String NAME = "name";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param obj
     * @return
     * @throws ClassCastExceptionをラップした例外
     */
    public static ClusterDto newClusterDto(Map<String, Object> map) {
        ClusterDto dto = new ClusterDto();
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = (String) entry.getKey();
                if (key.equals(CLUSTER_ID)) {
                    //                    dto.clusterId = (String) entry.getValue();
                    dto.clusterId = String.valueOf((Integer) entry.getValue());
                } else if (key.equals(NAME)) {
                    dto.name = (String) entry.getValue();
                }
            }
        } catch (ClassCastException e) {
            throw ConversionRuntimeException.newOutputException();
        }
        return dto;
    }


    public String clusterId;
    public String name;

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
