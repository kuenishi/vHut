/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * <p>DBが設計上あってはいけない状態になっている場合に発生する例外.
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
public class DBStateRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -6078150725341538355L;

    private String entityClass;

    private Long id;

    private Long[] ids;

    private String because;


    /**
     * @param entityClass テーブルのエンティティクラス
     * @param id レコードのID
     */
    public DBStateRuntimeException(Class entityClass, Long id) {
        this(null, entityClass, id, null);
    }

    /**
     * @param messageCode メッセージコード
     * @param entityClass テーブルのエンティティクラス
     * @param id レコードのID
     */
    public DBStateRuntimeException(String messageCode, Class entityClass, Long id) {
        this(messageCode, entityClass, id, null);
    }

    /**
     * @param entityClass テーブルのエンティティクラス
     * @param id レコードのID
     * @param because 詳細な理由
     */
    public DBStateRuntimeException(Class entityClass, Long id, String because) {
        this(null, entityClass, id, because);
    }

    /**
     * @param messageCode メッセージコード
     * @param entityClass テーブルのエンティティクラス
     * @param id レコードのID
     * @param because 詳細な理由
     */
    public DBStateRuntimeException(String messageCode, Class entityClass, Long id, String because) {
        super(messageCode, new Object[]{ entityClass.toString(), id.toString(), because });
        this.entityClass = entityClass.getName();
        this.id = id;
        this.because = because;
    }

    /**
     * @param entityClass テーブルのエンティティクラス
     * @param ids レコードのIDの配列
     */
    public DBStateRuntimeException(Class entityClass, Long[] ids) {
        this(null, entityClass, ids, null);
    }

    /**
     * @param messageCode メッセージコード
     * @param entityClass テーブルのエンティティクラス
     * @param ids レコードのIDの配列
     */
    public DBStateRuntimeException(String messageCode, Class entityClass, Long[] ids) {
        this(messageCode, entityClass, ids, null);
    }

    /**
     * @param entityClass テーブルのエンティティクラス
     * @param ids レコードのIDの配列
     * @param because 詳細な理由
     */
    public DBStateRuntimeException(Class entityClass, Long[] ids, String because) {
        this(null, entityClass, ids, because);
    }

    /**
     * @param messageCode メッセージコード
     * @param entityClass テーブルのエンティティクラス
     * @param ids レコードのIDの配列
     * @param because 詳細な理由
     */
    public DBStateRuntimeException(String messageCode, Class entityClass, Long[] ids, String because) {
        super(messageCode, new Object[]{ entityClass.toString(), ids.toString(), because });
        this.entityClass = entityClass.getName();
        this.ids = ids;
        this.because = because;
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
    public Long getId() {
        return id;
    }

    /**
     * @return the ids
     */
    public Long[] getIds() {
        return ids;
    }

    /**
     * @return the because
     */
    public String getBecause() {
        return because;
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
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param ids the ids to set
     */
    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    /**
     * @param because the because to set
     */
    public void setBecause(String because) {
        this.because = because;
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
