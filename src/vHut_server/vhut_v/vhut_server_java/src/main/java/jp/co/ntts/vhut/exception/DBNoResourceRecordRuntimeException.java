/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import java.sql.Timestamp;

import org.seasar.framework.util.StringConversionUtil;

/**
 * リソーステーブルにレコードが存在しない.
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
public class DBNoResourceRecordRuntimeException extends AbstractVhutRuntimeException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 8088294827295576468L;

    private String entityClass;

    private Timestamp date;


    /**
     * @param entityClass エンティティクラス
     * @param date 日付
     */
    public DBNoResourceRecordRuntimeException(Class entityClass, Timestamp date) {
        super("ECLDL2101", new Object[]{ entityClass.getName(), StringConversionUtil.toString(date, "yyyy/MM/dd") });
        this.entityClass = entityClass.getName();
        this.date = date;
    }

    /**
     * @return the entityClass
     */
    public String getEntityClass() {
        return entityClass;
    }

    /**
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @param entityClass the entityClass to set
     */
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Timestamp date) {
        this.date = date;
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
