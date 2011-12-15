/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import jp.co.ntts.vhut.logic.IResourceConsumer;

/**
 * <p>{@link jp.co.ntts.vhut.entity.NetworkAdapter}をグルーピングし、
 * {@link jp.co.ntts.vhut.entity.Network}との関連を定義するエンティティ.
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
public class SecurityGroup implements Serializable, IResourceConsumer, IIdentifiableEntity {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -3005695035771380956L;

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

    /** networkIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long networkId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** network関連プロパティ */
    @OneToOne
    public Network network;

    /** networkAdapterList関連プロパティ */
    @OneToMany(mappedBy = "securityGroup")
    public List<NetworkAdapter> networkAdapterList;

    /** vlanReservationList関連プロパティ */
    @OneToMany(mappedBy = "securityGroup")
    public List<VlanReservation> vlanReservationList;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getCpuResource()
     */
    @Override
    public Map<Long, Integer> getCpuResource() {
        return null;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getMemoryResource()
     */
    @Override
    public Map<Long, Integer> getMemoryResource() {
        return null;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getPublicIpResource()
     */
    @Override
    public int getPublicIpResource() {
        return 0;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getStorageResource()
     */
    @Override
    public Map<Long, Integer> getStorageResource() {
        return null;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getVlanResource()
     */
    @Override
    public int getVlanResource() {
        return 1;
    }

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
