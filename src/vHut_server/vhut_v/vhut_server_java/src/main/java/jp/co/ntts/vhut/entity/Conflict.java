/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

/**
 * Conflictエンティティクラス
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
public class Conflict implements Serializable {

    private static final long serialVersionUID = 1L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** cloudIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long cloudId;

    /** statusプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public ConflictStatus status;

    /** detailプロパティ */
    @Column(length = 10485760, nullable = true, unique = false)
    public String detail;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** cloudUserList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<CloudUser> cloudUserList;

    /** clusterList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<Cluster> clusterList;

    /** hostList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<Host> hostList;

    /** networkList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<Network> networkList;

    /** storageList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<Storage> storageList;

    /** templateList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<Template> templateList;

    /** vmList関連プロパティ */
    @OneToMany(mappedBy = "conflict")
    public List<Vm> vmList;
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
