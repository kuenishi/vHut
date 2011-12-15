/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver.rhev.dto;

import java.util.Map;

/**
 * <p>
 * VhutUserDtoクラス。 <br>
 * <p>
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class CloudUserDto {

    private static final String USER_ID = "cloudUserId";
    private static final String ACCOUNT = "account";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String EMAIL = "email";


    /**
     * XMLRPCの戻り値からDTOを作成する.
     * @param map Map型データ
     * @return VmDto型データ
     * @throws ClassCastException クラスキャスト時の例外
     */
    public static CloudUserDto newCloudUserDto(Map<String, Object> map) throws ClassCastException {
        CloudUserDto dto = new CloudUserDto();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            if (key.equals(USER_ID)) {
                dto.userId = (String) entry.getValue();

            } else if (key.equals(ACCOUNT)) {
                dto.account = (String) entry.getValue();

            } else if (key.equals(LAST_NAME)) {
                dto.lastName = (String) entry.getValue();

            } else if (key.equals(FIRST_NAME)) {
                dto.firstName = (String) entry.getValue();

            } else if (key.equals(EMAIL)) {
                dto.email = (String) entry.getValue();
            }
        }
        return dto;
    }


    public String userId;
    public String account;
    public String lastName;
    public String firstName;
    public String email;
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
