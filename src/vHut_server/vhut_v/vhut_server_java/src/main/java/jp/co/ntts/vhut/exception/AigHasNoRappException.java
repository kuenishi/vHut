/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import org.apache.commons.lang.StringUtils;

/**
 * ApplicationInstanceGroupが展開に必要なReleasedApplicationを持たない場合に生成される例外.
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
public class AigHasNoRappException extends AbstractVhutException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 4311953607936775019L;

    private Long[] aigIds;

    private Long appId;


    /**
     * @param aigIds Application Instance Group のIDの配列
     * @param appId 参照されているApplicationのID
     */
    public AigHasNoRappException(Long[] aigIds, Long appId) {
        super("WSRVS5041", new Object[]{ StringUtils.join(aigIds), appId });
        this.aigIds = aigIds;
        this.appId = appId;
    }

    /**
     * @return the aigIds
     */
    public Long[] getAigIds() {
        return aigIds;
    }

    /**
     * @return the appId
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * @param aigIds the aigIds to set
     */
    public void setAigIds(Long[] aigIds) {
        this.aigIds = aigIds;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(Long appId) {
        this.appId = appId;
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
