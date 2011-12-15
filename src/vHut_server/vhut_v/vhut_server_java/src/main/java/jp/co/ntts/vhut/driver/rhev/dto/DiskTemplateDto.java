/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.Map;

/**
 * <p>
 * DiskTemplateDtoクラス。 <br>
 * <p>
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
public class DiskTemplateDto {

    public static final String DISK_ID = "diskId";
    public static final String SIZE = "size";
    public static final String STORAGE_ID = "storageId";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param map Map型データ
     * @return DiskDto型データ
     * @throws ClassCastException クラスキャスト時の例外
     */
    public static DiskTemplateDto newDiskTemplateDto(Map<String, Object> map) throws ClassCastException {
        DiskTemplateDto dto = new DiskTemplateDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            if (key.equals(DISK_ID)) {
                dto.diskId = (String) entry.getValue();
            } else if (key.equals(SIZE)) {
                dto.size = (Integer) entry.getValue();
            } else if (key.equals(STORAGE_ID)) {
                dto.storageId = (String) entry.getValue();
            }
        }
        return dto;
    }


    public String diskId;
    public int size;
    public String storageId;

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
