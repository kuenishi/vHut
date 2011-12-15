/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

/**
 * VhutUserエンティティクラス.
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
public class VhutUser implements Serializable, IIdentifiableEntity {

    private static final long serialVersionUID = 1L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

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
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 256, nullable = true, unique = false)
    public String email;

    /** imageUrlプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String imageUrl;

    /** システムで使用するためユーザ書き込み不可 */
    @Column
    public Boolean sysLock = false;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** applicationList関連プロパティ */
    @OneToMany(mappedBy = "vhutUser")
    public List<Application> applicationList;

    /** applicationInstanceList関連プロパティ */
    @OneToMany(mappedBy = "vhutUser")
    public List<ApplicationInstance> applicationInstanceList;

    /** applicationInstanceGroupList関連プロパティ */
    @OneToMany(mappedBy = "vhutUser")
    public List<ApplicationInstanceGroup> applicationInstanceGroupList;

    /** vhutUserCloudUserMapList関連プロパティ */
    @OneToMany(mappedBy = "vhutUser")
    public List<VhutUserCloudUserMap> vhutUserCloudUserMapList;

    /** vhutUserRoleMapList関連プロパティ */
    @OneToMany(mappedBy = "vhutUser")
    public List<VhutUserRoleMap> vhutUserRoleMapList;


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
