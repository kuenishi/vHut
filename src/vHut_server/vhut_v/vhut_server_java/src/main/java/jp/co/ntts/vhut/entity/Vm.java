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
 * Vmエンティティクラス.
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
public class Vm implements Serializable, IResourceConsumer, ISynchronizedEntity {

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
    public VmStatus status = VmStatus.DEVELOPPING;

    /** specIdプロパティ */
    @Column(precision = 5, nullable = true, unique = false)
    public Long specId;

    /** cpuCoreプロパティ */
    @Column(precision = 5, nullable = true, unique = false)
    public Integer cpuCore;

    /** memoryプロパティ */
    @Column(precision = 10, nullable = true, unique = false)
    public Integer memory;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** cpuUsageプロパティ */
    @Column(precision = 5, nullable = true, unique = false)
    public Integer cpuUsage = 0;

    /** memoryUsageプロパティ */
    @Column(precision = 5, nullable = true, unique = false)
    public Integer memoryUsage = 0;

    /** osプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public Os os;

    /** templateIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long templateId;

    /** conflictIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long conflictId;

    /** clusterIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long clusterId;

    /** hostIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long hostId;

    /** storageIdプロパティ */
    @Column(precision = 19, nullable = true, unique = false)
    public Long storageId;

    /** commandVmMapList関連プロパティ */
    @OneToMany(mappedBy = "vm")
    public List<CommandVmMap> commandVmMapList;

    /** diskList関連プロパティ */
    @OneToMany(mappedBy = "vm")
    public List<Disk> diskList;

    /** networkAdapterList関連プロパティ */
    @OneToMany(mappedBy = "vm")
    public List<NetworkAdapter> networkAdapterList;

    /** cluster関連プロパティ */
    @ManyToOne
    public Cluster cluster;

    /** conflict関連プロパティ */
    @ManyToOne
    public Conflict conflict;

    /** host関連プロパティ */
    @ManyToOne
    public Host host;

    /** template関連プロパティ */
    @ManyToOne
    public Template template;

    /** vmCloudUserMapList関連プロパティ */
    @OneToMany(mappedBy = "vm")
    public List<VmCloudUserMap> vmCloudUserMapList;

    /** clusterReservationVmMap関連プロパティ */
    @OneToMany(mappedBy = "vm")
    public List<ClusterReservationVmMap> clusterReservationVmMapList;

    @Transient
    private List<Object> updateChildren;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getCpuResource()
     */
    @Override
    public Map<Long, Integer> getCpuResource() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(1);
        result.put(this.clusterId, this.cpuCore);
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
        if (diskList == null) {
            throw new NotJoinedRuntimeException(Disk.class);
        }
        Map<Long, Integer> result = new HashMap<Long, Integer>();
        Integer val;
        for (Disk disk : diskList) {
            //            if (disk.isAdditionalDisk()) {
            Long storageId = disk.storageId;
            if (storageId != null && storageId.longValue() == 0) {
                storageId = null;
            }
            val = result.get(storageId);
            result.put(storageId, ((val == null) ? disk.size.intValue() : (val + disk.size)));
            //            }
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
        Vm remote = (Vm) entity;
        boolean isUpdated = false;
        updateChildren = new ArrayList<Object>();

        DBConflictException exception = new DBConflictException(this);

        // 名称
        if (!name.equals(remote.name)) {
            exception.addChanged("vm.name", name, remote.name);
        }

        // CPUコア数
        if (cpuCore < remote.cpuCore) {
            //減るときはコンフリクト
            exception.addChanged("vm.cupCore", cpuCore, remote.cpuCore);
        } else if (cpuCore > remote.cpuCore) {
            //増えるときは更新
            cpuCore = remote.cpuCore;
            isUpdated = true;
        }

        // メモリ容量
        if (memory < remote.memory) {
            //減るときはコンフリクト
            exception.addChanged("vm.memory", memory, remote.memory);
        } else if (memory > remote.memory) {
            //増えるときは更新
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

        //ネットワークアダプタ
        //NullPointerException回避
        if (networkAdapterList != null && remote.networkAdapterList != null) {
            if (networkAdapterList.size() != remote.networkAdapterList.size()) {
                exception.addChanged("template.networkAdapterList", networkAdapterList.size(), remote.networkAdapterList.size());
            } else {
                for (NetworkAdapter remoteNetworkAdapter : remote.networkAdapterList) {
                    NetworkAdapter localNetworkAdapter = getNetworkAdapterById(remoteNetworkAdapter.id);
                    try {
                        if (!localNetworkAdapter.sync(remoteNetworkAdapter)) {
                            continue;
                        }
                    } catch (DBConflictException e) {
                        exception.add(e);
                    }
                    updateChildren.add(localNetworkAdapter);
                }
            }
            // networkAdapterListおよびremote.networkAdapterListのどちらかがnullだった場合
        } else {
            if (networkAdapterList == null) {
                exception.addChanged("template.networkAdapterList", (int) 0, remote.networkAdapterList.size());

            } else if (remote.networkAdapterList == null) {
                exception.addChanged("template.networkAdapterList", networkAdapterList.size(), (int) 0);

            } else {
                //                for (NetworkAdapter remoteNetworkAdapter : remote.networkAdapterList) {
                //                    NetworkAdapter localNetworkAdapter = getNetworkAdapterById(remoteNetworkAdapter.id);
                //                    try {
                //                        if (!localNetworkAdapter.sync(remoteNetworkAdapter)) {
                //                            continue;
                //                        }
                //                    } catch (DBConflictRuntimeException e) {
                //                        exception.add(e);
                //                    }
                //                    updateChildren.add(localNetworkAdapter);
                //                }
                exception.addRemoved("template.networkAdapterList");
            }
        }

        //ディスク
        //NullPointerException回避
        if (diskList != null && remote.diskList != null) {
            if (diskList.size() != remote.diskList.size()) {
                exception.addChanged("template.disktemplateList", diskList.size(), remote.diskList.size());
            } else {
                for (Disk remoteDisk : remote.diskList) {
                    Disk localDisk = getDiskById(remoteDisk.id);
                    try {
                        if (!localDisk.sync(remoteDisk)) {
                            continue;
                        }
                    } catch (DBConflictException e) {
                        exception.add(e);
                    }
                    updateChildren.add(localDisk);
                }
            }
            // diskListおよびremote.diskListのどちらかがnullだった場合
        } else {
            if (diskList == null) {
                exception.addChanged("template.disktemplateList", (int) 0, remote.diskList.size());

            } else if (remote.diskList == null) {
                exception.addChanged("template.disktemplateList", diskList.size(), (int) 0);

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

    private Disk getDiskById(Long id) {
        for (Disk disk : diskList) {
            if (disk.id.equals(id)) {
                return disk;
            }
        }
        return null;
    }

    private NetworkAdapter getNetworkAdapterById(Long id) {
        for (NetworkAdapter networkAdapter : networkAdapterList) {
            if (networkAdapter.id.equals(id)) {
                return networkAdapter;
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
