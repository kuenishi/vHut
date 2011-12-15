/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import jp.co.ntts.vhut.entity.ResourceType;

/**
 * <p>コマンドの実行に必要なリソースが予約に不足している際に発生する例外.
 * <br>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 * $Created: 2010-08-16
 */
public class CloudReservationException extends AbstractVhutException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 1757806827546652683L;

    private Long reservationId;

    private ResourceType type;


    /**
     * @param reservationId 予約ID
     * @param type リソース種別
     */
    public CloudReservationException(Long reservationId, ResourceType type) {
        this(reservationId, type, null);
    }

    /**
     * @param reservationId 予約ID
     * @param type リソース種別
     * @param cause 例外
     */
    public CloudReservationException(Long reservationId, ResourceType type, Throwable cause) {
        super("WCLDL5012", new Object[]{ reservationId, type.name() }, cause);
        this.reservationId = reservationId;
        this.type = type;
    }

    /**
     * @return the reservationId
     */
    public Long getReservationId() {
        return reservationId;
    }

    /**
     * @return the type
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * @param reservationId the reservationId to set
     */
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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
