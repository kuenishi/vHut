/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.util.List;

import jp.co.ntts.vhut.exception.DBConflictException;

/**
 * <p>リモートの情報と同期することを前提にしたエンティティです.
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
public interface ISynchronizedEntity extends IIdentifiableEntity {

    /**
     * 状態の不一致を表すエンティティを返します.
     * @return 状態不一致を表すエンティティ
     */
    Conflict getConflict();

    /**
     * 状態の不一致を表すエンティティを設定します.
     * @param value 状態不一致を表すエンティティ
     */
    void setConflict(Conflict value);

    /**
     * 状態不一致を表すエンティティのIDを返します。
     * @return 状態不一致を表すエンティティのID
     */
    Long getConflictId();

    /**
     * 状態不一致を表すエンティティのIDを設定します.
     * @param value 状態不一致を表すエンティティのID
     */
    void setConflictId(Long value);

    /**
     * クラウドのIDを返します.
     * @return クラウドのID
     */
    Long getCloudId();

    /**
     * リモートのオブジェクトと情報を同期します.
     * @param entity リモートのオブジェクト
     * @return 同期処理を実行したらtrue, 同期する必要がなければfalse
     * @throws DBConflictException DBに不一致があった
     */
    boolean sync(ISynchronizedEntity entity) throws DBConflictException;

    /**
     * sync後に更新があった子階層のエンティティ.
     * @return 更新があった子階層のエンティティ
     */
    List<Object> getUpdatedChildren();

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
