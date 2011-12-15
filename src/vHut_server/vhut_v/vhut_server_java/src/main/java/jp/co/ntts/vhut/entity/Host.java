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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

import jp.co.ntts.vhut.exception.DBConflictException;

/**
 * Hostエンティティクラス.
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
public class Host implements Serializable, ISynchronizedEntity {

    private static final long serialVersionUID = 1L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** cloudIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long cloudId;

    /** nameプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String name;

    /** clusterIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long clusterId;

    /** cpuCoreプロパティ */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer cpuCore;

    /** memoryプロパティ */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer memory;

    /** statusプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public HostStatus status;

    /** conflictIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long conflictId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** cluster関連プロパティ */
    @ManyToOne
    public Cluster cluster;

    /** conflict関連プロパティ */
    @ManyToOne
    public Conflict conflict;

    /** vmList関連プロパティ */
    @OneToMany(mappedBy = "host")
    public List<Vm> vmList;

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
        Host remote = (Host) entity;
        boolean isUpdated = false;

        DBConflictException exception = new DBConflictException(this);

        // 名称
        if (!name.equals(remote.name)) {
            exception.addChanged("storage.name", name, remote.name);
        }

        // CPUコア数
        if (cpuCore > remote.cpuCore) {
            //減るときはコンフリクト
            exception.addChanged("host.cupCore", cpuCore, remote.cpuCore);
        } else if (cpuCore < remote.cpuCore) {
            //増えるときは更新
            cpuCore = remote.cpuCore;
            isUpdated = true;
        }

        // メモリ容量
        if (memory > remote.memory) {
            //減るときはコンフリクト
            exception.addChanged("host.memory", memory, remote.memory);
        } else if (memory < remote.memory) {
            //増えるときは更新
            memory = remote.memory;
            isUpdated = true;
        }

        // 状態
        if (status != remote.status) {
            status = remote.status;
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
