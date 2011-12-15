/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * VmDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class VmDto {

    private static final String VM_ID = "vmId";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CPU_CORE = "cpuCore";
    private static final String MEMORY = "memory";
    private static final String CPU_USAGE = "cpuUsage";
    private static final String MEMORY_USAGE = "memoryUsage";
    private static final String STATUS = "status";
    private static final String OS = "os";
    private static final String TEMPLATE_ID = "templateId";
    private static final String HOST_ID = "hostId";
    private static final String CLUSTER_ID = "clusterId";
    private static final String NETWORK_ADAPTER_LIST = "networkAdapterList";
    private static final String DISK_LIST = "diskList";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param map Map型データ
     * @return VmDto型データ
     * @throws ClassCastException クラスキャスト時の例外
     */
    public static VmDto newVmDto(Map<String, Object> map) throws ClassCastException {
        VmDto dto = new VmDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            String key = (String) entry.getKey();
            if (key.equals(VM_ID)) {
                dto.vmId = (String) entry.getValue();

            } else if (key.equals(NAME)) {
                dto.name = (String) entry.getValue();

            } else if (key.equals(DESCRIPTION)) {
                dto.description = (String) entry.getValue();

            } else if (key.equals(CPU_CORE)) {
                dto.cpuCore = (Integer) entry.getValue();

            } else if (key.equals(MEMORY)) {
                dto.memory = (Integer) entry.getValue();

            } else if (key.equals(CPU_USAGE)) {
                dto.cpuUsage = (Integer) entry.getValue();

            } else if (key.equals(MEMORY_USAGE)) {
                dto.memoryUsage = (Integer) entry.getValue();

            } else if (key.equals(STATUS)) {
                dto.status = (String) entry.getValue();

            } else if (key.equals(OS)) {
                dto.os = (String) entry.getValue();

            } else if (key.equals(TEMPLATE_ID)) {
                dto.templateId = (String) entry.getValue();

            } else if (key.equals(HOST_ID)) {
                dto.hostId = String.valueOf(entry.getValue());

            } else if (key.equals(CLUSTER_ID)) {
                dto.clusterId = String.valueOf(entry.getValue());

            } else if (key.equals(NETWORK_ADAPTER_LIST)) {

                Object[] srcList = (Object[]) entry.getValue();
                List<NetworkAdapterDto> destList = new ArrayList<NetworkAdapterDto>();

                for (Object obj : srcList) {
                    destList.add(NetworkAdapterDto.newNetworkAdapterDto((Map<String, Object>) obj));
                }
                dto.networkAdapterList = destList.toArray();

            } else if (key.equals(DISK_LIST)) {
                Object[] srcList = (Object[]) entry.getValue();
                List<DiskDto> destList = new ArrayList<DiskDto>();

                for (Object obj : srcList) {

                    destList.add(DiskDto.newDiskDto((Map<String, Object>) obj));

                }
                dto.diskList = destList.toArray();
            }

        }
        return dto;
    }


    public String vmId;
    public String name;
    public String description;
    public int cpuCore;
    public int memory;
    public int cpuUsage;
    public int memoryUsage;
    public String status;
    public String os;
    public String templateId;
    public String hostId;
    public String clusterId;
    public Object[] networkAdapterList;
    public Object[] diskList;
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
