/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import java.util.ArrayList;
import java.util.List;

import jp.co.ntts.vhut.entity.IIdentifiableEntity;

import org.apache.commons.lang.StringUtils;

/**
 * DB関連の操作を使用とした際に他のテーブルから参照されているために実行できない際に生成されるエラー.
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
public class DBStillReferencedRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -4023632294761242152L;

    private String targetEntity;

    private String targetEntityId;

    private String referencingEntity;

    private String referencingEntityIds;


    /**
     * コンストラクタ.
     * @param target 操作対象のエンティティ
     * @param referencings 関連を持っているエンティティのリスト
     */
    public DBStillReferencedRuntimeException(IIdentifiableEntity target, IIdentifiableEntity[] referencings) {
        this(null, target, referencings);
    }

    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param target 操作対象のエンティティ
     * @param referencings 関連を持っているエンティティのリスト
     */
    public DBStillReferencedRuntimeException(String messageCode, IIdentifiableEntity target, IIdentifiableEntity[] referencings) {
        super(messageCode, createArgs(target, referencings));
        Object[] args = getArgs();
        targetEntity = (String) args[0];
        targetEntityId = (String) args[1];
        referencingEntity = (String) args[2];
        referencingEntityIds = (String) args[3];

    }

    /**
     * @param target
     * @param referencings
     * @return
     */
    private static Object[] createArgs(IIdentifiableEntity target, IIdentifiableEntity[] referencings) {
        String targetClass = target.getClass()
            .getName();
        String targetId = target.getId()
            .toString();
        String referencingClass = null;
        String idlist = null;
        if (referencings.length > 0) {
            referencingClass = referencings[0].getClass()
                .getName();
            List<Long> ids = new ArrayList<Long>();
            for (IIdentifiableEntity referencig : referencings) {
                ids.add(referencig.getId());
            }
            idlist = StringUtils.join(ids.toArray(new Long[0]));
        }
        return new Object[]{ targetClass, targetId, referencingClass, idlist };
    }

    /**
     * @return the targetEntity
     */
    public String getTargetEntity() {
        return targetEntity;
    }

    /**
     * @return the targetEntityId
     */
    public String getTargetEntityId() {
        return targetEntityId;
    }

    /**
     * @return the referencingEntity
     */
    public String getReferencingEntity() {
        return referencingEntity;
    }

    /**
     * @return the referencingEntityIds
     */
    public String getReferencingEntityIds() {
        return referencingEntityIds;
    }

    /**
     * @param targetEntity the targetEntity to set
     */
    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    /**
     * @param targetEntityId the targetEntityId to set
     */
    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    /**
     * @param referencingEntity the referencingEntity to set
     */
    public void setReferencingEntity(String referencingEntity) {
        this.referencingEntity = referencingEntity;
    }

    /**
     * @param referencingEntityIds the referencingEntityIds to set
     */
    public void setReferencingEntityIds(String referencingEntityIds) {
        this.referencingEntityIds = referencingEntityIds;
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
