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
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.logic.IResource;

import org.seasar.framework.container.SingletonS2Container;

/**
 * StorageResourceエンティティクラス.
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
public class StorageResource implements Serializable, IResource<StorageResource, StorageReservation>, IIdentifiableEntity {

    private static final long serialVersionUID = 1L;

    /** クラウドの設定 */
    private static CloudConfig cloudConfig;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** storageIdプロパティ */
    @Column(precision = 19, nullable = false)
    public Long storageId;

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

    /** storageMaxプロパティ */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer storageMax;

    /** storageTerminablyUsedプロパティ */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer storageTerminablyUsed;

    /** storage関連プロパティ */
    @ManyToOne
    public Storage storage;


    /** クラウドの設定 */
    private static CloudConfig getCloudConfig() {
        if (cloudConfig == null) {
            cloudConfig = SingletonS2Container.getComponent(CloudConfig.class);
        }
        return cloudConfig;
    }

    /** @return ストレージ容量の制限値. */
    @Transient
    public Integer getStorageLimit() {
        return Math.round((float) storageMax * (float) getCloudConfig().getStorageResourceLimitRate() / 100f);
    }

    /** @return ストレージ容量の警告値. */
    @Transient
    public Integer getStorageWarn() {
        return Math.round((float) storageMax * (float) getCloudConfig().getStorageResourceWarnRate() / 100f);
    }

    @Override
    public StorageResource addReservation(StorageReservation rsv) {
        // NEXT: timeが一致しているのを確かめる方法
        if (this.storageId != rsv.storageId) {
            // NEXT: 例外処理
            return null;
        }
        int ss = this.storageTerminablyUsed + rsv.size;
        if (this.getStorageLimit() >= ss) {
            this.storageTerminablyUsed = ss;
        } else {
            // NEXT: 例外処理
            return null;
        }

        return this;
    }

    @Override
    public StorageResource subtractReservation(StorageReservation rsv) {
        // NEXT: timeが一致しているのを確かめる方法
        if (this.storageId != rsv.storageId) {
            // NEXT: 例外処理
            return null;
        }
        if (this.storageTerminablyUsed >= rsv.size) {
            this.storageTerminablyUsed = this.storageTerminablyUsed - rsv.size;
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
