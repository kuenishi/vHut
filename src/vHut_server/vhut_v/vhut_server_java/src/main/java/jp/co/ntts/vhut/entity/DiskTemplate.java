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
 * DiskTemplateエンティティクラス.
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
public class DiskTemplate implements Serializable, IIdentifiableEntity {

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

    /** templateIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long templateId;

    /** sizeプロパティ */
    @Column(precision = 10, nullable = false, unique = false)
    public Integer size;

    /** storageIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long storageId;

    /** storageReservationIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long storageReservationId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** storage関連プロパティ */
    @ManyToOne
    public Storage storage;

    /** storageReservationDiskTemplateMap関連プロパティ */
    @OneToMany(mappedBy = "diskTemplate")
    public List<StorageReservationDiskTemplateMap> storageReservationDiskTemplateMapList;

    /** diskMap関連プロパティ */
    @OneToMany(mappedBy = "diskTemplate")
    public List<Disk> diskMapList;

    /** template関連プロパティ */
    @ManyToOne
    public Template template;


    /**
     * リモートのオブジェクトと情報を同期します.
     * @param remote リモートのオブジェクト
     * @return 同期処理を実行したらtrue, 同期する必要がなければfalse
     * @throws DBConflictException DBに不一致があった
     */
    public boolean sync(DiskTemplate remote) throws DBConflictException {
        boolean isUpdated = false;

        DBConflictException exception = new DBConflictException(template);

        // 名称
        //        if (!name.equals(remote.name)) {
        //            exception.addChanged("diskTemplate.name", name, remote.name);
        //        }

        // 容量
        if (size < remote.size) {
            //増えるときはコンフリクト
            exception.addChanged("diskTemplate.size", size, remote.size);
        } else if (size > remote.size) {
            //減るときは更新
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
     * @param disk ディスク
     * @return 新規ディスクテンプレート
     */
    public static DiskTemplate newInstance(Disk disk) {
        DiskTemplate result = new DiskTemplate();

        result.cloudId = disk.cloudId;
        result.storageId = disk.storageId;
        result.name = disk.name;
        result.size = disk.size;

        return result;
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
