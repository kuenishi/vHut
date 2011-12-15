package jp.co.ntts.vhut.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.ntts.vhut.entity.ClusterResource;
import jp.co.ntts.vhut.entity.PublicIpResource;
import jp.co.ntts.vhut.entity.StorageResource;
import jp.co.ntts.vhut.entity.VlanResource;

/**
 * <p>特定の日時のリソース情報を受け渡すためのクラス.
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
/**
 * <p>//
 * <br>
 * <p>//
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
/**
 * <p>//
 * <br>
 * <p>//
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
class ResourceSet {

    /**
     * 日時 
     */
    Date date;
    /**
     * Map of <'cluster id', 'ClusterResource for the cluster id'>
     */
    Map<Long, ClusterResource> cluster = new HashMap<Long, ClusterResource>();

    /**
     * Map of <'storage id', 'StorageResource for the cluster id'>
     */
    Map<Long, StorageResource> storage = new HashMap<Long, StorageResource>();

    /** VLANリソース */
    VlanResource vlan = new VlanResource();

    /** パブリックIPリソース */
    PublicIpResource publicIp = new PublicIpResource();


    /** プライベートコンストラクタ. */
    ResourceSet() {
    }

    /**
     * クラスターのリソースを取得します.
     * @param clusterId クラスタID
     * @return リソース
     */
    public ClusterResource getCluster(Long clusterId) {
        return cluster.get(clusterId);
    }

    /**
     * ストレージのリソースを取得します.
     * @param storageId ストレージID
     * @return ストレージリソース
     */
    public StorageResource getStorage(Long storageId) {
        return storage.get(storageId);
    }

    /**
     * @return VLANリソース
     */
    public VlanResource getVlan() {
        return vlan;
    }

    /**
     * @return パブリックIPリソース
     */
    public PublicIpResource getPublicIp() {
        return publicIp;
    }

    /**
     * @return 登録されているクラスタリソースを合計したクラスタリソース
     */
    public ClusterResource getTotalCluster() {
        ClusterResource tr = new ClusterResource();
        tr.clusterId = 0L;
        tr.cpuCoreMax = 0;
        tr.cpuCoreTerminablyUsed = 0;
        tr.memoryMax = 0;
        tr.memoryTerminablyUsed = 0;

        for (ClusterResource r : cluster.values()) {
            tr.cpuCoreMax += r.cpuCoreMax;
            tr.cpuCoreTerminablyUsed += r.cpuCoreTerminablyUsed;
            tr.memoryMax += r.memoryMax;
            tr.memoryTerminablyUsed += r.memoryTerminablyUsed;
        }

        return tr;
    }

    /**
     * @return 登録されているストレージリソースを合計したストレージタリソース
     */
    public StorageResource getTotalStorage() {
        StorageResource tr = new StorageResource();
        tr.storageId = 0L;
        tr.storageMax = 0;
        tr.storageTerminablyUsed = 0;

        for (StorageResource r : storage.values()) {
            tr.storageMax += r.storageMax;
            tr.storageTerminablyUsed += r.storageTerminablyUsed;
        }

        return tr;
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
