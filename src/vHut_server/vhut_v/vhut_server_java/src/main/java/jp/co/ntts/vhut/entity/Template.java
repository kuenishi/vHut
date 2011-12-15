/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jp.co.ntts.vhut.exception.NotJoinedRuntimeException;
import jp.co.ntts.vhut.logic.IResourceConsumer;

/**
 * Templateエンティティクラス.
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
public class Template implements Serializable, IResourceConsumer, ISynchronizedEntity {

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

    /** descriptionプロパティ */
    @Column(length = 10485760, nullable = true, unique = false)
    public String description;

    /** statusプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public TemplateStatus status;

    /** specIdプロパティ */
    @Column(precision = 5, nullable = true, unique = false)
    public Long specId;

    /** cpuCoreプロパティ */
    @Column(precision = 5, nullable = true, unique = false)
    public Integer cpuCore;

    /** memoryプロパティ */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer memory;

    /** osプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public Os os;

    /** conflictIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long conflictId;

    /** clusterIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long clusterId;

    /** storageIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long storageId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** commandTemplateMapList関連プロパティ */
    @OneToMany(mappedBy = "template")
    public List<CommandTemplateMap> commandTemplateMapList;

    /** diskTemplateList関連プロパティ */
    @OneToMany(mappedBy = "template")
    public List<DiskTemplate> diskTemplateList;

    /** networkAdapterTemplateList関連プロパティ */
    @OneToMany(mappedBy = "template")
    public List<NetworkAdapterTemplate> networkAdapterTemplateList;

    /** cluster関連プロパティ */
    @ManyToOne
    public Cluster cluster;

    /** conflict関連プロパティ */
    @ManyToOne
    public Conflict conflict;

    /** vmList関連プロパティ */
    @OneToMany(mappedBy = "template")
    public List<Vm> vmList;

    @Transient
    private List<Object> updateChildren;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getCpuResource()
     */
    @Override
    public Map<Long, Integer> getCpuResource() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(1);
        result.put(this.clusterId, this.cpuCore.intValue());
        return result;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getMemoryResource()
     */
    @Override
    public Map<Long, Integer> getMemoryResource() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(1);
        result.put(this.clusterId, this.memory);
        return result;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getPublicIpResource()
     */
    @Override
    public int getPublicIpResource() {
        return 0;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getStorageResource()
     */
    @Override
    public Map<Long, Integer> getStorageResource() {
        if (diskTemplateList == null) {
            throw new NotJoinedRuntimeException(Disk.class);
        }
        Map<Long, Integer> result = new HashMap<Long, Integer>();
        Integer val;
        for (DiskTemplate disk : diskTemplateList) {
            val = result.get(disk.storageId);
            result.put(disk.storageId, ((val == null) ? disk.size.intValue() : (val + disk.size)));
        }
        return result;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getVlanResource()
     */
    @Override
    public int getVlanResource() {
        return 0;
    }

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
        Template remote = (Template) entity;
        boolean isUpdated = false;
        updateChildren = new ArrayList<Object>();

        DBConflictException exception = new DBConflictException(this);

        // 名称
        if (!name.equals(remote.name)) {
            exception.addChanged("template.name", name, remote.name);
        }

        // CPUコア数
        if (cpuCore < remote.cpuCore) {
            //増えるときはコンフリクト
            exception.addChanged("tempate.cupCore", cpuCore, remote.cpuCore);
        } else if (cpuCore > remote.cpuCore) {
            //減るときは更新
            cpuCore = remote.cpuCore;
            isUpdated = true;
        }

        // メモリ容量
        if (memory < remote.memory) {
            //増えるときはコンフリクト
            exception.addChanged("template.memory", memory, remote.memory);
        } else if (memory > remote.memory) {
            //減るときは更新
            memory = remote.memory;
            isUpdated = true;
        }

        // 状態
        if (status != remote.status) {
            status = remote.status;
            isUpdated = true;
        }

        // 概要
        if (!description.equals(remote.description)) {
            description = remote.description;
            isUpdated = true;
        }

        // OS
        if (os != remote.os) {
            os = remote.os;
            isUpdated = true;
        }

        //ネットワークアダプタテンプレート
        //NullPointerException回避
        //        if (networkAdapterTemplateList != null && remote.networkAdapterTemplateList != null) {
        //            if (networkAdapterTemplateList.size() != remote.networkAdapterTemplateList.size()) {
        //                exception.addChanged("template.networkAdapterTemplateList",
        //                        networkAdapterTemplateList.size(),
        //                        remote.networkAdapterTemplateList.size());
        //            } else {
        //                for (NetworkAdapterTemplate remoteNetworkAdapterTemplate : remote.networkAdapterTemplateList) {
        //                    NetworkAdapterTemplate localNetworkAdapterTemplate = getNetworkAdapterTemplateById(remoteNetworkAdapterTemplate.id);
        //                    try {
        //                        if (!localNetworkAdapterTemplate.sync(remoteNetworkAdapterTemplate)) {
        //                            continue;
        //                        }
        //                    } catch (DBConflictException e) {
        //                        exception.add(e);
        //                    }
        //                    updateChildren.add(localNetworkAdapterTemplate);
        //                }
        //            }
        //            // networkAdapterTemplateListおよびremote.networkAdapterTemplateListのどちらかがnullだった場合
        //        } else {
        //            if (networkAdapterTemplateList == null) {
        //                exception.addChanged("template.networkAdapterTemplateList",
        //                        (int) 0,
        //                        remote.networkAdapterTemplateList.size());
        //
        //            } else if (remote.networkAdapterTemplateList == null) {
        //                exception.addChanged("template.networkAdapterTemplateList",
        //                        networkAdapterTemplateList.size(),
        //                        (int) 0);
        //
        //            } else {
        //                exception.addRemoved("template.networkAdapterTemplateList");
        //            }
        //        }

        //        //ディスクテンプレート
        //        if (diskTemplateList.size() != remote.diskTemplateList.size()) {
        //            exception.addChanged("template.disktemplateList",
        //                        diskTemplateList.size(),
        //                        remote.diskTemplateList.size());
        //        } else {
        //            for (DiskTemplate remoteDiskTemplate : remote.diskTemplateList) {
        //                DiskTemplate localDiskTemplate = getDiskTemplateById(remoteDiskTemplate.id);
        //                try {
        //                    if (!localDiskTemplate.sync(remoteDiskTemplate)) {
        //                        continue;
        //                    }
        //                } catch (DBConflictRuntimeException e) {
        //                    exception.add(e);
        //                }
        //                updateChildren.add(localDiskTemplate);
        //            }
        //        }

        //ディスクテンプレート
        //NullPointerException回避
        if (diskTemplateList != null && remote.diskTemplateList != null) {
            if (diskTemplateList.size() != remote.diskTemplateList.size()) {
                exception.addChanged("template.disktemplateList", diskTemplateList.size(), remote.diskTemplateList.size());
            } else {
                for (DiskTemplate remoteDiskTemplate : remote.diskTemplateList) {
                    DiskTemplate localDiskTemplate = getDiskTemplateById(remoteDiskTemplate.id);
                    try {
                        if (!localDiskTemplate.sync(remoteDiskTemplate)) {
                            continue;
                        }
                    } catch (DBConflictException e) {
                        exception.add(e);
                    }
                    updateChildren.add(localDiskTemplate);
                }
            }
            // diskListおよびremote.diskListのどちらかがnullだった場合
        } else {
            if (diskTemplateList == null) {
                exception.addChanged("template.disktemplateList", (int) 0, remote.diskTemplateList.size());

            } else if (remote.diskTemplateList == null) {
                exception.addChanged("template.disktemplateList", diskTemplateList.size(), (int) 0);

            } else {
                //                for (Disk remoteDisk : remote.diskList) {
                //                    Disk localDisk = getDiskById(remoteDisk.id);
                //                    try {
                //                        if (!localDisk.sync(remoteDisk)) {
                //                            continue;
                //                        }
                //                    } catch (DBConflictRuntimeException e) {
                //                        exception.add(e);
                //                    }
                //                    updateChildren.add(localDisk);
                //                }
                exception.addRemoved("template.disktemplateList");
            }
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

    private DiskTemplate getDiskTemplateById(Long id) {
        for (DiskTemplate diskTemplate : diskTemplateList) {
            if (diskTemplate.id.equals(id)) {
                return diskTemplate;
            }
        }
        return null;
    }

    private NetworkAdapterTemplate getNetworkAdapterTemplateById(Long id) {
        for (NetworkAdapterTemplate networkAdapterTemplate : networkAdapterTemplateList) {
            if (networkAdapterTemplate.id.equals(id)) {
                return networkAdapterTemplate;
            }
        }
        return null;
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
