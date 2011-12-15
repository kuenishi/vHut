/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.Map;

/**
 * <p>
 * HostDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class HostDto {

    private static final String HOST_ID = "hostId";
    private static final String NAME = "name";
    private static final String STATUS = "status";
    private static final String CPU_CORE = "cpuCore";
    private static final String MEMORY = "memory";
    private static final String CPU_USAGE = "cpuUsage";
    private static final String MEMORY_USAGE = "memoryUsage";
    private static final String CLUSTER_ID = "clusterId";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param map Map型データ
     * @return HostDto型データ
     * @throws ClassCastException クラスキャスト時の例外
     */
    public static HostDto newHostDto(Map<String, Object> map) throws ClassCastException {
        HostDto dto = new HostDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (String) entry.getKey();

            if (key.equals(HOST_ID)) {
                if (entry.getValue() != null) {
                    dto.hostId = String.valueOf(entry.getValue());
                }
            } else if (key.equals(NAME)) {
                dto.name = (String) entry.getValue();

            } else if (key.equals(STATUS)) {
                dto.status = (String) entry.getValue();

            } else if (key.equals(CPU_CORE)) {
                dto.cpuCore = (Integer) entry.getValue();

            } else if (key.equals(MEMORY)) {
                dto.memory = (Integer) entry.getValue();

            } else if (key.equals(CPU_USAGE)) {
                dto.cpuUsage = (Integer) entry.getValue();

            } else if (key.equals(MEMORY_USAGE)) {
                dto.memoryUsage = (Integer) entry.getValue();

            } else if (key.equals(CLUSTER_ID)) {
                if (entry.getValue() != null) {
                    dto.clusterId = String.valueOf(entry.getValue());
                }
            }
        }
        return dto;
    }


    public String hostId;
    public String name;
    public String status;
    public int cpuCore;
    public int memory;
    public int cpuUsage;
    public int memoryUsage;
    public String clusterId;

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
