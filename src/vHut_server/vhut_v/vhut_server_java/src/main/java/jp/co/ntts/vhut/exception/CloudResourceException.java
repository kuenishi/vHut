/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import jp.co.ntts.vhut.entity.ResourceType;

/**
 * <p>予約に必要なリソースがクラウドに不足している際に発生する例外.
 * <br>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 * $Created: 2010-08-16
 */
public class CloudResourceException extends AbstractVhutException {
    /**
     * シリアル.
     */
    private static final long serialVersionUID = -2688609763354888859L;

    /** クラウドID */
    private Long cloudId;

    /** リソース種別 */
    private ResourceType type;


    /**
     * @param cloudId クラウドID
     * @param type リソース種別
     */
    public CloudResourceException(Long cloudId, ResourceType type) {
        this(cloudId, type, null);
    }

    /**
     * @param cloudId クラウドID
     * @param type リソース種別
     * @param cause 例外
     */
    public CloudResourceException(Long cloudId, ResourceType type, Throwable cause) {
        super("WCLDL5011", new Object[]{ cloudId, type.name() }, cause);
        this.cloudId = cloudId;
        this.type = type;
    }

    /**
     * @return the cloudId
     */
    public Long getCloudId() {
        return cloudId;
    }

    /**
     * @param cloudId the cloudId to set
     */
    public void setCloudId(Long cloudId) {
        this.cloudId = cloudId;
    }

    /**
     * @return the type
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ResourceType type) {
        this.type = type;
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
