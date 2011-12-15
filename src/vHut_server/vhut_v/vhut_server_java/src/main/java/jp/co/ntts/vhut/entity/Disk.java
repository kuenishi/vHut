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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import jp.co.ntts.vhut.exception.DBConflictException;

/**
 * Diskエンティティクラス.
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
public class Disk implements Serializable, IIdentifiableEntity {

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

    /** vmIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long vmId;

    /** sizeプロパティ */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer size;

    /** storageIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long storageId;

    /** diskTemplateIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long diskTemplateId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** storage関連プロパティ */
    @ManyToOne
    public Storage storage;

    /** diskTemplate関連プロパティ */
    @ManyToOne
    public DiskTemplate diskTemplate;

    /** storageReservationDiskMap関連プロパティ */
    @OneToMany(mappedBy = "disk")
    public List<StorageReservationDiskMap> storageReservationDiskMapList;

    /** vm関連プロパティ */
    @ManyToOne
    public Vm vm;


    /**
     * リモートのオブジェクトと情報を同期します.
     * @param remote リモートのオブジェクト
     * @return 同期処理を実行したらtrue, 同期する必要がなければfalse
     * @throws DBConflictException DBに不一致があった
     */
    public boolean sync(Disk remote) throws DBConflictException {
        boolean isUpdated = false;

        DBConflictException exception = new DBConflictException(vm);

        //        // 名称
        //        if (!name.equals(remote.name)) {
        //            exception.addChanged("disk.name", name, remote.name);
        //        }

        // 容量
        if (size < remote.size) {
            // 増えるときはコンフリクト
            exception.addChanged("disk.size", size, remote.size);
        } else if (size > remote.size) {
            // 減るときは更新
            size = remote.size;
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

    /**
     * テンプレートのディスクと同じスペックのディスクを作成します.
     * @param diskTemplate テンプレートのディスク
     * @return ディスク
     */
    public static Disk newInstance(DiskTemplate diskTemplate) {
        Disk result = new Disk();

        result.cloudId = diskTemplate.cloudId;
        result.storageId = diskTemplate.storageId;
        result.name = diskTemplate.name;
        result.size = diskTemplate.size;
        result.diskTemplateId = diskTemplate.id;
        result.diskTemplate = diskTemplate;

        return result;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.entity.IIdentifiableEntity#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return 追加ディスクかどうか？
     */
    public boolean isAdditionalDisk() {
        if (diskTemplateId == null || diskTemplateId <= 0) {
            return true;
        } else {
            return false;
        }
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
