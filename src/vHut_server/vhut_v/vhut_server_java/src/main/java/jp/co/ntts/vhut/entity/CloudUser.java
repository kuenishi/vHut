/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

import jp.co.ntts.vhut.exception.DBConflictException;

/**
 * CloudUserエンティティクラス.
 *
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Entity
@Generated(value = { "S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl" }, date = "2011/07/15 15:30:33")
public class CloudUser implements Serializable, ISynchronizedEntity {

    private static final long serialVersionUID = 1L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** cloudIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long cloudId;

    /** accountプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String account;

    /** firstNameプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String firstName;

    /** lastNameプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String lastName;

    /** emailプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String email;

    /** conflictIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long conflictId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** conflict関連プロパティ */
    @ManyToOne
    public Conflict conflict;

    /** vmCloudUserMapList関連プロパティ */
    @OneToMany(mappedBy = "cloudUser")
    public List<VmCloudUserMap> vmCloudUserMapList;

    @Transient
    private List<Object> updateChildren;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#getConflict()
     */
    @Override
    public Conflict getConflict() {
        return conflict;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#getConflictId()
     */
    @Override
    public Long getConflictId() {
        return conflictId;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#getCloudId()
     */
    @Override
    public Long getCloudId() {
        return cloudId;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#setConflict(jp.co.ntts.vhut.entity.Conflict)
     */
    @Override
    public void setConflict(Conflict value) {
        conflict = value;
        conflictId = conflict.id;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#setConflictId(java.lang.Long)
     */
    @Override
    public void setConflictId(Long value) {
        conflictId = value;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#sync(jp.co.ntts.vhut.exception.ISynchronizedEntity)
     */
    @Override
    public boolean sync(ISynchronizedEntity entity) throws DBConflictException {
        CloudUser remote = (CloudUser) entity;
        boolean isUpdated = false;

        DBConflictException exception = new DBConflictException(this);

        // アカウント名
        if (!account.equals(remote.account)) {
            exception.addChanged("cloudUser.account", account, remote.account);
        }

        // 名
        if (firstName != remote.firstName) {
            firstName = remote.firstName;
            isUpdated = true;
        }

        // 姓
        if (lastName != remote.lastName) {
            lastName = remote.lastName;
            isUpdated = true;
        }

        // Eメール
        if (email != remote.email) {
            email = remote.email;
            isUpdated = true;
        }

        if (exception.getSize() > 0) {
            // 不一致を検知
            throw exception;
        } else {
            // 一致
            return isUpdated;
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.exception.ISynchronizedEntity#getUpdatedChildren()
     */
    @Override
    public List<Object> getUpdatedChildren() {
        if (updateChildren == null) {
            updateChildren = new ArrayList<Object>();
        }
        return updateChildren;
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
