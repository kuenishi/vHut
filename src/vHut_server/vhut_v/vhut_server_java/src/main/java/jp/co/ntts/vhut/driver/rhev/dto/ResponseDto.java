/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.driver.ConversionRuntimeException;
import jp.co.ntts.vhut.driver.rhev.RhevAgentRuntimeException;

/**
 * <p>
 * ResponseDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class ResponseDto {

    private static final String COMMAND_ID = "commandId";
    private static final String CLUSTER_LIST = "clusterList";
    private static final String HOST_LIST = "hostList";
    private static final String NETWORK_LIST = "networkList";
    private static final String STORAGE_LIST = "storageList";
    private static final String VM_LIST = "vmList";
    private static final String TEMPLATE_LIST = "templateList";
    private static final String CLOUD_USER_LIST = "cloudUserList";
    private static final String COMMAND_LIST = "commandList";
    private static final String VM = "vm";
    private static final String NETWORK_ADAPTER = "networkAdapter";
    private static final String DISK = "disk";
    private static final String CLOUD_USER = "cloudUser";
    private static final String TEMPLATE = "template";
    private static final String FAULT_CODE = "faultCode";
    private static final String FAULT_STRING = "faultString";
    private static final String NETWORK_ADAPTER_LIST = "networkAdapterList";
    private static final String DISK_LIST = "diskList";
    private static final String NETWORK_ADAPTER_TEMPLATE_LIST = "networkAdapterTemplateList";
    private static final String DISK_TEMPLATE_LIST = "diskTemplateList";


    /**
     * エージェントから受け取ったデータをResponseDto型にキャストするクラス。
     * 
     * @param map map型データ
     * @return Response型データ
     * @throws ClassCastException クラスキャスト時の例外
     * @throws RhevAgentRuntimeException
     */
    public static ResponseDto newResponseDto(Map<String, Object> map) {

        ResponseDto dto = new ResponseDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                String key = (String) entry.getKey();
                String faultCode = null;
                String faultString = null;
                if (key.equals(FAULT_CODE)) {
                    faultCode = String.valueOf(entry.getValue());
                    if (faultString != null) {
                        throw RhevAgentRuntimeException.newException(faultCode, faultString);
                    }
                }
                if (key.equals(FAULT_STRING)) {
                    faultString = (String) entry.getValue();
                    if (faultCode != null) {
                        throw RhevAgentRuntimeException.newException(faultCode, faultString);
                    }
                }
                if (key.equals(COMMAND_ID)) {
                    if (entry.getKey() != null) {
                        dto.commandId = (String) entry.getValue();
                    }

                } else if (key.equals(CLUSTER_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<ClusterDto> destList = new ArrayList<ClusterDto>();
                    for (Object obj : srcList) {
                        destList.add(ClusterDto.newClusterDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(HOST_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<HostDto> destList = new ArrayList<HostDto>();
                    for (Object obj : srcList) {
                        destList.add(HostDto.newHostDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(NETWORK_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<NetworkDto> destList = new ArrayList<NetworkDto>();
                    for (Object obj : srcList) {
                        destList.add(NetworkDto.newNetworkDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(STORAGE_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<StorageDto> destList = new ArrayList<StorageDto>();
                    for (Object obj : srcList) {
                        destList.add(StorageDto.newStorageDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(VM_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<VmDto> destList = new ArrayList<VmDto>();
                    for (Object obj : srcList) {
                        destList.add(VmDto.newVmDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(TEMPLATE_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<TemplateDto> destList = new ArrayList<TemplateDto>();
                    for (Object obj : srcList) {
                        destList.add(TemplateDto.newTemplateDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(CLOUD_USER_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<CloudUserDto> destList = new ArrayList<CloudUserDto>();
                    for (Object obj : srcList) {
                        destList.add(CloudUserDto.newCloudUserDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(COMMAND_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<CommandDto> destList = new ArrayList<CommandDto>();
                    for (Object obj : srcList) {
                        destList.add(CommandDto.newCommandDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(VM)) {
                    Object obj = (Object) entry.getValue();
                    List<VmDto> destList = new ArrayList<VmDto>();
                    destList.add(VmDto.newVmDto((Map<String, Object>) obj));
                    dto.result = destList.toArray();

                } else if (key.equals(NETWORK_ADAPTER)) {
                    Object obj = (Object) entry.getValue();
                    List<NetworkAdapterDto> destList = new ArrayList<NetworkAdapterDto>();
                    destList.add(NetworkAdapterDto.newNetworkAdapterDto((Map<String, Object>) obj));
                    dto.result = destList.toArray();

                } else if (key.equals(DISK)) {
                    Object obj = (Object) entry.getValue();
                    List<DiskDto> destList = new ArrayList<DiskDto>();
                    destList.add(DiskDto.newDiskDto((Map<String, Object>) obj));
                    dto.result = destList.toArray();

                } else if (key.equals(CLOUD_USER)) {
                    Object obj = (Object) entry.getValue();
                    List<CloudUserDto> destList = new ArrayList<CloudUserDto>();
                    destList.add(CloudUserDto.newCloudUserDto((Map<String, Object>) obj));
                    dto.result = destList.toArray();

                } else if (key.equals(TEMPLATE)) {
                    Object obj = (Object) entry.getValue();
                    List<TemplateDto> destList = new ArrayList<TemplateDto>();
                    destList.add(TemplateDto.newTemplateDto((Map<String, Object>) obj));
                    dto.result = destList.toArray();

                } else if (key.equals(NETWORK_ADAPTER_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<NetworkAdapterDto> destList = new ArrayList<NetworkAdapterDto>();
                    for (Object obj : srcList) {
                        destList.add(NetworkAdapterDto.newNetworkAdapterDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(DISK_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<DiskDto> destList = new ArrayList<DiskDto>();
                    for (Object obj : srcList) {
                        destList.add(DiskDto.newDiskDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(NETWORK_ADAPTER_TEMPLATE_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<NetworkAdapterTemplateDto> destList = new ArrayList<NetworkAdapterTemplateDto>();
                    for (Object obj : srcList) {
                        destList.add(NetworkAdapterTemplateDto.newNetworkAdapterTemplateDto((Map<String, Object>) obj));
                    }
                    dto.result = destList.toArray();

                } else if (key.equals(DISK_TEMPLATE_LIST)) {
                    Object[] srcList = (Object[]) entry.getValue();
                    List<DiskTemplateDto> destList = new ArrayList<DiskTemplateDto>();
                    for (Object obj : srcList) {
                        destList.add(DiskTemplateDto.newDiskTemplateDto((Map<String, Object>) obj));
                    }
                }
            } catch (Exception e) {
                throw ConversionRuntimeException.newOutputException(e);
            }
        }

        return dto;
    }


    public String commandId;
    public Object[] result;

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
