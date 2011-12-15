/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * <p>{@link jp.co.ntts.vhut.entity.Application}に{@link jp.co.ntts.vhut.entity.SecurityGroup}
 * を格納するためのエンティティクラス.
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
@Entity
public class ApplicationInstanceSecurityGroup implements Serializable, IIdentifiableEntity {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -679157030208029408L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** applicationInstanceIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long applicationInstanceId;

    /** nameプロパティ */
    @Column(length = 256, nullable = false, unique = false)
    public String name;

    /** securityGroupIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long securityGroupId;

    /** cloudIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long cloudId;

    /** privateIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long privateId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** applicationInstance関連プロパティ */
    @ManyToOne
    public ApplicationInstance applicationInstance;

    /** applicationInstanceVmSecurityGroupMapList関連プロパティ */
    @OneToMany(mappedBy = "applicationInstanceSecurityGroup")
    public List<ApplicationInstanceVmSecurityGroupMap> applicationInstanceVmSecurityGroupMapList;

    /** SecurityGroupの実態 */
    @Transient
    public SecurityGroup securityGroup;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.entity.IIdentifiableEntity#getId()
     */
    @Override
    public Long getId() {
        return id;
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
