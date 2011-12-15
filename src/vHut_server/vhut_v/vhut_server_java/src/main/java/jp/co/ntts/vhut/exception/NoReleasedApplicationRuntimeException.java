/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * <p>アプリケーションインスタンス作成時に参照するアプリケーションに一つもリリースがない場合に発生する例外です.
 * <br>
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
public class NoReleasedApplicationRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 6197885134767756490L;

    private static final String MESSAGE_CODE = "ESRVT2021";

    private Long applicationId;

    private Long applicationInstanceGroupId;


    /**
     * コンストラクタ.
     * @param applicationId 対象のアプリケーションのID
     * @param applicationInstanceGroupId 影響を受けたアプリケーションインスタンスグループのID
     */
    public NoReleasedApplicationRuntimeException(Long applicationId, Long applicationInstanceGroupId) {
        super(MESSAGE_CODE, new Object[]{ applicationId, applicationInstanceGroupId });
        this.applicationId = applicationId;
        this.applicationInstanceGroupId = applicationInstanceGroupId;
    }

    /**
     * @return 対象のアプリケーションのID
     */
    public Long getApplicationId() {
        return applicationId;
    }

    /**
     * @return 影響を受けたアプリケーションインスタンスグループのID
     */
    public Long getApplicationInstanceGroupId() {
        return applicationInstanceGroupId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @param applicationInstanceGroupId the applicationInstanceGroupId to set
     */
    public void setApplicationInstanceGroupId(Long applicationInstanceGroupId) {
        this.applicationInstanceGroupId = applicationInstanceGroupId;
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
