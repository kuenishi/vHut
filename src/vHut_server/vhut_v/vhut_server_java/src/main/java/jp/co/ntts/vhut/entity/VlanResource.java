/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.logic.IResource;

import org.seasar.framework.container.SingletonS2Container;

/**
 * VlanResourceエンティティクラス.
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
public class VlanResource implements Serializable, IResource<VlanResource, VlanReservation>, IIdentifiableEntity {

    private static final long serialVersionUID = 1L;

    /** クラウドの設定 */
    private static CloudConfig cloudConfig;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** cloudIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long cloudId;

    /** timeプロパティ */
    // NEXT :uniqueはtrueじゃなくていいの? クラスタIDと合わせてMultiColumUniqueにする必要がある。
    // NEXT :予約の粒度は日が最小でいいので
    //@Temporal(TemporalType.DATE)
    //@Column(nullable = false, unique = true)
    //public Date date;
    //とする。
    @Column(nullable = false, unique = false)
    public Timestamp time;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** vlanMaxプロパティ */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer vlanMax;

    /** vlanTerminablyUsedプロパティ */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer vlanTerminablyUsed;


    /** クラウドの設定 */
    private static CloudConfig getCloudConfig() {
        if (cloudConfig == null) {
            cloudConfig = SingletonS2Container.getComponent(CloudConfig.class);
        }
        return cloudConfig;
    }

    /** @return VLANの制限値. */
    @Transient
    public Integer getVlanLimit() {
        return Math.round((float) vlanMax * (float) getCloudConfig().getVlanResourceLimitRate() / 100f);
    }

    /** @return VLANの警告値. */
    @Transient
    public Integer getVlanWarn() {
        return Math.round((float) vlanMax * (float) getCloudConfig().getVlanResourceWarnRate() / 100f);
    }

    @Override
    public VlanResource addReservation(VlanReservation rsv) {
        if (this.cloudId != rsv.cloudId) {
            // NEXT: 例外処理
            return null;
        }
        int ss = this.vlanTerminablyUsed + 1;
        if (this.getVlanLimit() >= ss) {
            this.vlanTerminablyUsed = ss;
        } else {
            // NEXT: 例外処理
            return null;
        }

        return this;
    }

    @Override
    public VlanResource subtractReservation(VlanReservation rsv) {
        if (this.cloudId != rsv.cloudId) {
            // NEXT: 例外処理
            return null;
        }
        if (this.vlanTerminablyUsed > 0) {
            this.vlanTerminablyUsed -= 1;
        } else {
            // NEXT: 例外処理
            return null;
        }

        return this;
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
