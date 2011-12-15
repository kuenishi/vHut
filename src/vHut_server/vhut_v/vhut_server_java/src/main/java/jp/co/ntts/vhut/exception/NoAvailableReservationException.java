/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import jp.co.ntts.vhut.entity.Right;

/**
 * 操作に必要な予約が見つからない場合に発生する例外.
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
public class NoAvailableReservationException extends AbstractVhutException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -691902366509589988L;

    private String entityClass;

    private long id;

    private String action;


    /**
     * @param entityClass 操作対象の主要エンティティ
     * @param id ID
     * @param right 操作
     */
    public NoAvailableReservationException(Class entityClass, long id, Right right) {
        this(entityClass, id, right.name());
    }

    /**
     * @param entityClass 操作対象の主要エンティティ
     * @param id ID
     * @param action 操作
     */
    public NoAvailableReservationException(Class entityClass, long id, String action) {
        super("WSRVS5031", new Object[]{ entityClass.getName(), id, action });
        this.entityClass = entityClass.getName();
        this.id = id;
        this.action = action;
    }

    /**
     * @return the entityClass
     */
    public String getEntityClass() {
        return entityClass;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param entityClass the entityClass to set
     */
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
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
