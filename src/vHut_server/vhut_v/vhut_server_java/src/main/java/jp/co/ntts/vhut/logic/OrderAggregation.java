package jp.co.ntts.vhut.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.InputRuntimeException;

class OrderAggregation {
    private Map<Long, List<Disk>> storageDiskMap;
    //    private Map<Long, Long> storageSizeMap;

    private Map<Long, List<Vm>> clusterVmMap;


    /**
     * @param storageDiskMap
     * @param storageSizeMap
     */
    public OrderAggregation() {
        super();
        this.storageDiskMap = Collections.synchronizedMap(new HashMap<Long, List<Disk>>());
        //        this.storageSizeMap =
        //            Collections.synchronizedMap(new HashMap<Long, Long>());
    }

    public Map<Long, List<Disk>> getStorageDiskMap() {
        return this.storageDiskMap;
    }

    // public void setStorageDiskMap(Map<Long, List<Disk>> val) {
    // this.storageDiskMap = val;
    // }

    public void addStorageDiskList(List<Disk> newDiskList) {
        // TBD: Reservationが Long storageId と List<Disk>
        // diskList を持っていて，Diskが Long storageId
        // を持っているが，Reservation内におけるこの2つのstorageIdの関係を要確認
        for (Disk dd : newDiskList) {
            this.addStorageDisk(dd.storageId, dd);
        }
    }

    public void addStorageDisk(Long storageId, Disk newDisk) {
        List<Disk> diskList = storageDiskMap.get(storageId);
        if (diskList == null) {
            diskList = Collections.synchronizedList(new ArrayList<Disk>());
            storageDiskMap.put(storageId, diskList);
        } else {
            // 同じidを持つDiskがすでに登録されていないかチェック
            for (Disk disk : diskList) {
                if (disk.id == newDisk.id) {
                    throw new InputRuntimeException("newDisk", "other disk already has same id.");
                }
            }
        }
        diskList.add(newDisk);
    }

    // ストレージ・サイズの計算
    public Map<Long, Long> getStorageSizeMap() {
        Map<Long, Long> result = Collections.synchronizedMap(new HashMap<Long, Long>());

        for (Long storageId : this.storageDiskMap.keySet()) {
            List<Disk> diskList = this.storageDiskMap.get(storageId);
            if (diskList == null) {
                // TBD: storageDiskMapにDiskListがnullのstorageIdが格納されていたときの処理
                result.put(storageId, null);
            } else {
                for (Disk disk : diskList) {
                    Long size = result.get(disk.storageId);
                    if (null != size) {
                        size = size + disk.size;
                    } else {
                        size = disk.size.longValue();
                    }
                    result.put(storageId, size);
                }
            }
        }

        return result;
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
