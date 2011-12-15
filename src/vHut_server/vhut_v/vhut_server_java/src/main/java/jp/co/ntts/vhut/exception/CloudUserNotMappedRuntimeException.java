/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * <p>vHutユーザに対応するcloudUserが発見できない時に発生する例外.
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
public class CloudUserNotMappedRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 1013780065294586735L;

    /**
     * vHutのユーザID
     */
    private Long vhutUserId;

    /**
     * クラウドID.
     */
    private Long cloudId;


    /**
     * コンストラクタ.
     * @param vhutUserId vHutのユーザID
     * @param cloudId クラウドID
     */
    public CloudUserNotMappedRuntimeException(Long vhutUserId, Long cloudId) {
        super("ESRVS5011", new Object[]{ vhutUserId, cloudId });
        this.vhutUserId = vhutUserId;
        this.cloudId = cloudId;
    }

    /**
     * 発見できなかったvHutユーザIdを返します.
     *
     * @return vHutのユーザID
     */
    public Long getVhutUserId() {
        return vhutUserId;
    }

    /**
     * 検索の際に利用したcloudIdを返します.
     *
     * @return クラウドID.
     */
    public Long getCloudId() {
        return cloudId;
    }

    /**
     * @param vhutUserId the vhutUserId to set
     */
    public void setVhutUserId(Long vhutUserId) {
        this.vhutUserId = vhutUserId;
    }

    /**
     * @param cloudId the cloudId to set
     */
    public void setCloudId(Long cloudId) {
        this.cloudId = cloudId;
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
