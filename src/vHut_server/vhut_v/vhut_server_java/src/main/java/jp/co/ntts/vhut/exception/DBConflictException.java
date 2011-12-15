/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import jp.co.ntts.vhut.entity.Conflict;
import jp.co.ntts.vhut.entity.ConflictStatus;
import jp.co.ntts.vhut.entity.ISynchronizedEntity;

/**
 * <p>エンティティが管理対象と不整合を起こした際に発生します.
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
public class DBConflictException extends AbstractConflictException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 3395119095238836444L;

    private Conflict conflict;


    /**
     * コンストラクタ.
     * @param entity エンティティの名前
     */
    public DBConflictException(ISynchronizedEntity entity) {
        super("WCOMM5011", entity);
        conflict = entity.getConflict();
        if (conflict == null) {
            conflict = new Conflict();
            conflict.cloudId = entity.getCloudId();
        }
        conflict.status = ConflictStatus.UNFIXED;
    }

    /**
     * @return the entity
     */
    public ISynchronizedEntity getEntity() {
        return (ISynchronizedEntity) entity;
    }

    /**
     * @return the conflict
     */
    public Conflict getConflict() {
        return conflict;
    }

    /**
     * 不一致情報を結合します.
     * @param e リファレンスになる例外
     */
    public void add(DBConflictException e) {
        entries.addAll(e.entries);
        updateDetail();
    }

    @Override
    protected void updateDetail() {
        super.updateDetail();
        conflict.detail = detail;
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
