/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import static jp.co.ntts.vhut.entity.Names.clusterReservationVmMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jp.co.ntts.vhut.entity.ClusterReservation;
import jp.co.ntts.vhut.entity.ClusterReservationNames;
import jp.co.ntts.vhut.entity.ClusterReservationVmMap;
import jp.co.ntts.vhut.entity.ClusterReservationVmMapNames;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.PublicIpReservation;
import jp.co.ntts.vhut.entity.PublicIpReservationNames;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.ResourceType;
import jp.co.ntts.vhut.entity.StorageReservation;
import jp.co.ntts.vhut.entity.StorageReservationDiskMap;
import jp.co.ntts.vhut.entity.StorageReservationDiskMapNames;
import jp.co.ntts.vhut.entity.StorageReservationDiskTemplateMap;
import jp.co.ntts.vhut.entity.StorageReservationDiskTemplateMapNames;
import jp.co.ntts.vhut.entity.StorageReservationNames;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.VlanReservation;
import jp.co.ntts.vhut.entity.VlanReservationNames;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.CloudReservationException;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * <p>Reservationエンティティを格納し拡張する便利クラス.
 * <p>
 *
 * @version 1.0.0
 * @author nota
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
class ReservationAdapter implements IResourceConsumer, IClusterResource, IStorageResource, IVlanResource, IPublicIpResource {

    /** clusterReservationList */
    public List<ClusterReservation> clusterReservationList;
    /** storageReservationList */
    public List<StorageReservation> storageReservationList;
    /** vlanReservationList */
    public List<VlanReservation> vlanReservationList;
    /** publicIpReservationList */
    public List<PublicIpReservation> publicIpReservationList;

    private final Reservation reservation;
    private final JdbcManager jdbcManager;


    /**
     * @param reservation : 内包するReservation. nullであってはならない.
     * @param jdbcManager : DBアクセスのためのJdbcManager. nullであってはならない.
     */
    public ReservationAdapter(Reservation reservation, JdbcManager jdbcManager) {
        if (reservation == null || jdbcManager == null) {
            throw new NullPointerException();
        }

        this.reservation = reservation;
        this.jdbcManager = jdbcManager;

        if (this.reservation.clusterReservationList == null) {
            this.reservation.clusterReservationList = this.jdbcManager.from(ClusterReservation.class)
                .where((new SimpleWhere()).eq(ClusterReservationNames.reservationId(), this.reservation.id))
                .getResultList();
        }
        this.clusterReservationList = this.reservation.clusterReservationList;

        if (this.reservation.storageReservationList == null) {
            this.reservation.storageReservationList = this.jdbcManager.from(StorageReservation.class)
                .where((new SimpleWhere()).eq(StorageReservationNames.reservationId(), this.reservation.id))
                .getResultList();
        }
        this.storageReservationList = this.reservation.storageReservationList;

        if (this.reservation.vlanReservationList == null) {
            this.reservation.vlanReservationList = this.jdbcManager.from(VlanReservation.class)
                .where((new SimpleWhere()).eq(VlanReservationNames.reservationId(), this.reservation.id))
                .getResultList();
        }
        this.vlanReservationList = this.reservation.vlanReservationList;

        if (this.reservation.publicIpReservationList == null) {
            this.reservation.publicIpReservationList = this.jdbcManager.from(PublicIpReservation.class)
                .where((new SimpleWhere()).eq(PublicIpReservationNames.reservationId(), this.reservation.id))
                .getResultList();
        }
        this.publicIpReservationList = this.reservation.publicIpReservationList;
    }

    /**
     * クラスタリソースの範囲内でVMが起動できるか調べます.
     * @param target VM
     * @return 可能:true, 不可能:false
     */
    public boolean checkClusterResource(Vm target) {
        Map<Long, Integer> clsttouse = target.getCpuResource();
        Map<Long, Integer> rsvmax = this.getCpuMax();
        Map<Long, Integer> rsvused = this.getCpuUsed();

        for (Long id : clsttouse.keySet()) {
            Integer touse = clsttouse.get(id);
            Integer max = rsvmax.get(id);
            Integer used = rsvused.get(id);

            if (max == null) {
                return false;
            }

            if (used == null) {
                used = 0;
            }

            if (touse + used > max) {
                return false;
            }
        }

        clsttouse = target.getMemoryResource();
        rsvmax = this.getMemoryMax();
        rsvused = this.getMemoryUsed();

        for (Long id : clsttouse.keySet()) {
            Integer touse = clsttouse.get(id);
            Integer max = rsvmax.get(id);
            Integer used = rsvused.get(id);

            if (max == null) {
                return false;
            }

            if (used == null) {
                used = 0;
            }

            if (touse + used > max) {
                return false;
            }
        }

        return true;
    }

    /**
     * ストレージリソースの範囲内でテンプレートが作成できるか調べます.
     * @param target テンプレート
     * @return 可能:true, 不可能:false
     */
    public boolean checkStorageResource(Template target) {
        Map<Long, Integer> strgtouse = target.getStorageResource();
        Map<Long, Integer> rsvmax = this.getStorageMax();
        Map<Long, Integer> rsvused = this.getStorageUsed();

        for (Long id : strgtouse.keySet()) {
            Integer touse = strgtouse.get(id);
            Integer max = rsvmax.get(id);
            Integer used = rsvused.get(id);

            if (max == null) {
                return false;
            }

            if (used == null) {
                used = 0;
            }

            if (touse + used > max) {
                return false;
            }
        }

        return true;
    }

    /**
     * ストレージリソースの範囲内でVMが作成できるか調べます.
     * @param target VM
     * @return 可能:true, 不可能:false
     */
    public boolean checkStorageResource(Vm target) {
        Map<Long, Integer> strgtouse = target.getStorageResource();
        Map<Long, Integer> rsvmax = this.getStorageMax();
        Map<Long, Integer> rsvused = this.getStorageUsed();

        for (Long id : strgtouse.keySet()) {
            Integer touse = strgtouse.get(id);
            Integer max = rsvmax.get(id);
            Integer used = rsvused.get(id);

            if (max == null) {
                return false;
            }

            if (used == null) {
                used = 0;
            }

            if (touse + used > max) {
                return false;
            }
        }

        return true;
    }

    //    /**
    //     * ストレージリソースの範囲内でVMが作成できるか調べます.
    //     * @param target VM
    //     * @return 可能:true, 不可能:false
    //     */
    //    public boolean checkVlanResource(Vm target) {
    //        Integer vlantouse = target.getVlanResource();
    //        Integer rsvmax = this.getVlanMax();
    //        Integer rsvused = this.getVlanUsed();
    //
    //        if (rsvmax == null) {
    //            return false;
    //        }
    //
    //        if (rsvused == null) {
    //            rsvused = 0;
    //        }
    //
    //        if (vlantouse + rsvused > rsvmax) {
    //            return false;
    //        }
    //
    //        return true;
    //    }

    /**
     * 開始日時を取得します.
     * @return 開始日時
     */
    public Date getBegin() {
        return this.reservation.startTime;
    }

    /**
     * 終了日時を取得します.
     * @return 終了日時
     */
    public Date getEnd() {
        return this.reservation.endTime;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IClusterResource#getCpuMax()
     */
    @Override
    public Map<Long, Integer> getCpuMax() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(this.reservation.clusterReservationList.size());

        for (ClusterReservation rsv : this.reservation.clusterReservationList) {
            Integer newValue = rsv.cpuCore;
            Integer oldValue = result.get(rsv.clusterId);
            if (oldValue != null) {
                newValue += oldValue;
            }
            result.put(rsv.clusterId, newValue);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getCpuResource()
     */
    @Override
    public Map<Long, Integer> getCpuResource() {
        return this.getCpuMax();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IClusterResource#getCpuUsed()
     * NEXT: 効率を求めるならば値をキャッシュしておくなどの対応が必要
     * (この実装では呼び出されるごとにDBにアクセスして使用量を計算している)
     */
    @Override
    public Map<Long, Integer> getCpuUsed() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(this.reservation.clusterReservationList.size());

        for (ClusterReservation rsv : this.reservation.clusterReservationList) {
            int sum = 0;
            List<ClusterReservationVmMap> rsvmaplist = this.jdbcManager.from(ClusterReservationVmMap.class)
                .where((new SimpleWhere()).eq(ClusterReservationVmMapNames.clusterReservationId(), rsv.id))
                .innerJoin(ClusterReservationVmMapNames.vm())
                //                                .orderBy("vm.id")
                .orderBy(clusterReservationVmMap().vmId()
                    .toString())
                .getResultList();
            Set<Long> idset = new HashSet<Long>(rsvmaplist.size());
            for (ClusterReservationVmMap rsvmap : rsvmaplist) {
                if (idset.contains(rsvmap.vmId)) {
                    // NEXT: 同じVmが同じClusterReservationに2度(以上)割り当てられている.
                    // マージする処理を入れるか検討
                } else {
                    idset.add(rsvmap.vmId);
                    sum += rsvmap.vm.cpuCore;
                }
            }
            result.put(rsv.clusterId, sum);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IClusterResource#getMemoryMax()
     */
    @Override
    public Map<Long, Integer> getMemoryMax() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(this.reservation.clusterReservationList.size());

        for (ClusterReservation rsv : this.reservation.clusterReservationList) {
            Integer newValue = rsv.memory;
            Integer oldValue = result.get(rsv.clusterId);
            if (oldValue != null) {
                newValue += oldValue;
            }

            result.put(rsv.clusterId, newValue);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getMemoryResource()
     */
    @Override
    public Map<Long, Integer> getMemoryResource() {
        return this.getMemoryMax();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IClusterResource#getMemoryUsed()
     * NEXT: 効率を求めるならば値をキャッシュしておくなどの対応が必要
     * (この実装では呼び出されるごとにDBにアクセスして使用量を計算している)
     */
    @Override
    public Map<Long, Integer> getMemoryUsed() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(this.reservation.clusterReservationList.size());

        for (ClusterReservation rsv : this.reservation.clusterReservationList) {
            int sum = 0;
            List<ClusterReservationVmMap> rsvmaplist = this.jdbcManager.from(ClusterReservationVmMap.class)
                .where((new SimpleWhere()).eq(ClusterReservationVmMapNames.clusterReservationId(), rsv.id))
                .innerJoin(ClusterReservationVmMapNames.vm())
                .orderBy("vm.id")
                .getResultList();
            Set<Long> idset = new HashSet<Long>(rsvmaplist.size());
            for (ClusterReservationVmMap rsvmap : rsvmaplist) {
                if (idset.contains(rsvmap.vmId)) {
                    // NEXT: 同じVmが同じClusterReservationに2度(以上)割り当てられている.
                    // マージする処理を入れるか検討する
                } else {
                    idset.add(rsvmap.vmId);
                    sum += rsvmap.vm.memory;
                }
            }
            result.put(rsv.clusterId, sum);
        }

        return result;
    }

    /**
     * 割り当てられていないPublic IPを取得します.
     * @return 割り当てられていないPublic IP
     */
    public PublicIpReservation getOpenPublicIpReservation() {
        for (PublicIpReservation result : this.reservation.publicIpReservationList) {
            if (result.networkAdapterId == null) {
                return result;
            }
        }

        return null;
    }

    /**
     * 割り当てられていないPublic IPを取得します.
     * @param unit 要求数
     * @return 割り当てられていないPublic IP
     */
    public List<PublicIpReservation> getOpenPublicIpReservationList(int unit) {
        List<PublicIpReservation> reservationList = new ArrayList<PublicIpReservation>();
        for (int i = 0; i < unit; i++) {
            for (PublicIpReservation openReservation : this.reservation.publicIpReservationList) {
                if (openReservation.networkAdapterId == null) {
                    reservationList.add(openReservation);
                    break;
                }
            }
        }
        return reservationList;
    }

    /**
     * 割り当てられていないVLANを取得します.
     * @return 割り当てられていないVLAN
     */
    public VlanReservation getOpenVlanReservation() {
        for (VlanReservation result : this.reservation.vlanReservationList) {
            if (result.securityGroupId == null) {
                return result;
            }
        }

        return null;
    }

    /**
     * 与えられたディスクに最適なストレージID割り当て更新します.
     * storageIdがnullのものだけ更新します.
     * storageIdがnull以外の場合更新しません.
     * @param diskList Disk#idを必要としません.
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    public void autoAssignStorageId(List<Disk> diskList) throws CloudReservationException {
        //key=storageId value=[storageReservationId]
        Map<Long, Set<Long>> stroageIdStorageReservationIdMap = new HashMap<Long, Set<Long>>();
        Map<Long, Long> storageReservationIdStorageIdMap = new HashMap<Long, Long>();
        //key=storageReservationId value=size
        Map<Long, Integer> availableSizeMap = new HashMap<Long, Integer>();
        for (StorageReservation storageReservation : storageReservationList) {
            if (!stroageIdStorageReservationIdMap.containsKey(storageReservation.storageId)) {
                stroageIdStorageReservationIdMap.put(storageReservation.storageId, new HashSet<Long>());
            }
            stroageIdStorageReservationIdMap.get(storageReservation.storageId)
                .add(storageReservation.id);
            storageReservationIdStorageIdMap.put(storageReservation.id, storageReservation.storageId);
            availableSizeMap.put(storageReservation.id, storageReservation.size);
        }

        //ストレージに割り当て済みのディスクのリスト
        List<Disk> assinedDiskList = new ArrayList<Disk>();
        //ストレージに割り当てられていないディスクのリスト
        List<Disk> notAssignedDiskList = new ArrayList<Disk>();

        for (Disk disk : diskList) {
            if (disk.storageId != null && disk.storageId > 0) {
                assinedDiskList.add(disk);
            } else {
                notAssignedDiskList.add(disk);
            }
        }

        if (notAssignedDiskList.size() == 0) {
            return;
        }

        Collections.sort(assinedDiskList, new DiskSizeComparator(DiskSizeComparator.DESC));
        for (Disk disk : assinedDiskList) {
            Set<Long> reservationIdSet = stroageIdStorageReservationIdMap.get(disk.storageId);
            List<Entry<Long, Integer>> availableSizeList = new ArrayList<Entry<Long, Integer>>();
            for (Entry<Long, Integer> entry : availableSizeMap.entrySet()) {
                if (reservationIdSet.contains(entry.getKey())) {
                    availableSizeList.add(entry);
                }
            }
            Collections.sort(availableSizeList, new AvailableSizeMapComparator(AvailableSizeMapComparator.DESC));
            if (availableSizeList.get(0)
                .getValue() >= disk.size) {
                Long storageReservationId = availableSizeList.get(0)
                    .getKey();
                availableSizeMap.put(storageReservationId, availableSizeMap.get(storageReservationId) - disk.size);
            } else {
                throw new CloudReservationException(reservation.id, ResourceType.STORAGE);
            }
        }

        Collections.sort(notAssignedDiskList, new DiskSizeComparator(DiskSizeComparator.DESC));
        List<Entry<Long, Integer>> availableSizeList = new ArrayList<Entry<Long, Integer>>();
        for (Entry<Long, Integer> entry : availableSizeMap.entrySet()) {
            availableSizeList.add(entry);
        }
        for (Disk disk : notAssignedDiskList) {
            Collections.sort(availableSizeList, new AvailableSizeMapComparator(AvailableSizeMapComparator.DESC));
            if (availableSizeList.get(0)
                .getValue() >= disk.size) {
                Long storageReservationId = availableSizeList.get(0)
                    .getKey();
                availableSizeMap.put(storageReservationId, availableSizeMap.get(storageReservationId) - disk.size);

                disk.storageId = storageReservationIdStorageIdMap.get(storageReservationId);
            } else {
                throw new CloudReservationException(reservation.id, ResourceType.STORAGE);
            }
        }
    }

    /**
     * 与えられたディスクに最適なストレージ予約割り当てを返します.
     * @param diskList ディスクリスト
     * @return StorageReservationDiskMapのリスト
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    public List<StorageReservationDiskMap> getStorageReservationDiskMapList(List<Disk> diskList) throws CloudReservationException {
        //返却値
        List<StorageReservationDiskMap> maps = new ArrayList<StorageReservationDiskMap>();
        //key=storageId value=[storageReservationId]
        Map<Long, Set<Long>> reservationMap = new HashMap<Long, Set<Long>>();
        //key=storageReservationId value=size
        Map<Long, Integer> availableSizeMap = new HashMap<Long, Integer>();
        for (StorageReservation storageReservation : storageReservationList) {
            if (!reservationMap.containsKey(storageReservation.storageId)) {
                reservationMap.put(storageReservation.storageId, new HashSet<Long>());
            }
            reservationMap.get(storageReservation.storageId)
                .add(storageReservation.id);
            availableSizeMap.put(storageReservation.id, storageReservation.size);
        }

        //ストレージに割り当て済みのディスクのリスト
        List<Disk> assinedDiskList = new ArrayList<Disk>();
        //ストレージに割り当てられていないディスクのリスト
        List<Disk> notAssignedDiskList = new ArrayList<Disk>();

        for (Disk disk : diskList) {
            if (disk.storageId != null && disk.storageId > 0) {
                assinedDiskList.add(disk);
            } else {
                notAssignedDiskList.add(disk);
            }
        }

        Collections.sort(assinedDiskList, new DiskSizeComparator(DiskSizeComparator.DESC));
        for (Disk disk : assinedDiskList) {
            Set<Long> reservationIdSet = reservationMap.get(disk.storageId);
            List<Entry<Long, Integer>> availableSizeList = new ArrayList<Entry<Long, Integer>>();
            for (Entry<Long, Integer> entry : availableSizeMap.entrySet()) {
                if (reservationIdSet.contains(entry.getKey())) {
                    availableSizeList.add(entry);
                }
            }
            Collections.sort(availableSizeList, new AvailableSizeMapComparator(AvailableSizeMapComparator.DESC));
            if (availableSizeList.get(0)
                .getValue() >= disk.size) {
                Long storageReservationId = availableSizeList.get(0)
                    .getKey();

                StorageReservationDiskMap map = new StorageReservationDiskMap();
                map.diskId = disk.id;
                map.storageReservationId = storageReservationId;
                maps.add(map);

                availableSizeMap.put(storageReservationId, availableSizeMap.get(storageReservationId) - disk.size);
            } else {
                throw new CloudReservationException(reservation.id, ResourceType.STORAGE);
            }
        }

        Collections.sort(notAssignedDiskList, new DiskSizeComparator(DiskSizeComparator.DESC));
        List<Entry<Long, Integer>> availableSizeList = new ArrayList<Entry<Long, Integer>>();
        for (Entry<Long, Integer> entry : availableSizeMap.entrySet()) {
            availableSizeList.add(entry);
        }
        for (Disk disk : notAssignedDiskList) {
            Collections.sort(availableSizeList, new AvailableSizeMapComparator(AvailableSizeMapComparator.DESC));
            if (availableSizeList.get(0)
                .getValue() > disk.size) {
                Long storageReservationId = availableSizeList.get(0)
                    .getKey();

                StorageReservationDiskMap map = new StorageReservationDiskMap();
                map.diskId = disk.id;
                map.storageReservationId = storageReservationId;
                maps.add(map);

                availableSizeMap.put(storageReservationId, availableSizeMap.get(storageReservationId) - disk.size);
            } else {
                throw new CloudReservationException(reservation.id, ResourceType.STORAGE);
            }
        }

        return maps;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IPublicIpResource#getPublicIpMax()
     */
    @Override
    public int getPublicIpMax() {
        return this.reservation.publicIpReservationList.size();
    }

    /**
     * ネットワークアダプタに割り当て済みのPublic IPリソースを取得します.
     * @param networkAdapterId ネットワークアダプタのID
     * @return Public IPリソース
     */
    public PublicIpReservation getPublicIpReservationByNetworkAdapterId(long networkAdapterId) {
        for (PublicIpReservation result : this.reservation.publicIpReservationList) {
            if (result.networkAdapterId == networkAdapterId) {
                return result;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getPublicIpResource()
     */
    @Override
    public int getPublicIpResource() {
        return this.getPublicIpMax();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IPublicIpResource#getPublicIpUsed()
     */
    @Override
    public int getPublicIpUsed() {
        int result = 0;

        for (PublicIpReservation rsv : this.reservation.publicIpReservationList) {
            if (rsv.networkAdapterId != null) {
                result += 1;
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IStorageResource#getStorageMax()
     */
    @Override
    public Map<Long, Integer> getStorageMax() {
        Map<Long, Integer> result = new HashMap<Long, Integer>();

        for (StorageReservation rsv : this.reservation.storageReservationList) {
            Integer currentSize = result.get(rsv.storageId);
            if (currentSize == null) {
                result.put(rsv.storageId, rsv.size);
            } else {
                result.put(rsv.storageId, currentSize + rsv.size);
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getStorageResource()
     */
    @Override
    public Map<Long, Integer> getStorageResource() {
        return this.getStorageMax();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IStorageResource#getStorageUsed()
     */
    @Override
    public Map<Long, Integer> getStorageUsed() {
        Map<Long, Integer> result = new HashMap<Long, Integer>(this.reservation.storageReservationList.size());

        for (StorageReservation rsv : this.reservation.storageReservationList) {
            int sum = 0;
            // Diskのサイズの総和を計算
            List<StorageReservationDiskMap> rsvmaplist = this.jdbcManager.from(StorageReservationDiskMap.class)
                .where((new SimpleWhere()).eq(StorageReservationDiskMapNames.storageReservationId(), rsv.id))
                .innerJoin(StorageReservationDiskMapNames.disk())
                .orderBy("disk.id")
                .getResultList();
            Set<Long> idset = new HashSet<Long>(rsvmaplist.size());
            for (StorageReservationDiskMap rsvmap : rsvmaplist) {
                if (idset.contains(rsvmap.diskId)) {
                    // NEXT: 同じDiskが同じStorageReservationに2度(以上)割り当てられている.
                    // マージする処理を入れるか検討する
                } else {
                    idset.add(rsvmap.diskId);
                    sum += rsvmap.disk.size;
                }
            }
            // DiskTemplateのサイズの総和を計算し，先の結果に追加
            List<StorageReservationDiskTemplateMap> rsvmaplist2 = this.jdbcManager.from(StorageReservationDiskTemplateMap.class)
                .where((new SimpleWhere()).eq(StorageReservationDiskTemplateMapNames.storageReservationId(), rsv.id))
                .innerJoin(StorageReservationDiskTemplateMapNames.diskTemplate())
                .orderBy("diskTemplate.id")
                .getResultList();
            Set<Long> idset2 = new HashSet<Long>(rsvmaplist.size());
            for (StorageReservationDiskTemplateMap rsvmap : rsvmaplist2) {
                if (idset.contains(rsvmap.diskTemplateId)) {
                    // NEXT: 同じDiskTemplateが同じStorageReservationに2度(以上)割り当てられている.
                    // 例外とまでは言えないが異常
                } else {
                    idset.add(rsvmap.diskTemplateId);
                    sum += rsvmap.diskTemplate.size;
                }
            }

            result.put(rsv.storageId, sum);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IVlanResource#getVlanMax()
     */
    @Override
    public int getVlanMax() {
        return this.reservation.vlanReservationList.size();
    }

    /**
     * セキュリティグループに割り当てられているVLANリソースを取得します.
     * @param securityGroupId セキュリティグループのID
     * @return VLANリソース
     */
    public VlanReservation getVlanReservationBySecurityGroupId(long securityGroupId) {
        for (VlanReservation result : this.reservation.vlanReservationList) {
            if (result.securityGroupId == securityGroupId) {
                return result;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceConsumer#getVlanResource()
     */
    @Override
    public int getVlanResource() {
        return this.getVlanMax();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IVlanResource#getVlanUsed()
     */
    @Override
    public int getVlanUsed() {
        int result = 0;

        for (VlanReservation rsv : this.reservation.vlanReservationList) {
            if (rsv.securityGroupId != null) {
                result += 1;
            }
        }

        return result;
    }

    /**
     * ストレージ・リザベーションの中で空き容量が最も最大のものを取得します.
     * @return 空き容量最大のストレージ・リザベーション
     */
    public StorageReservation getStorageReservationWithMaxRoom() {
        //NEXT: 空き容量が最大のものを選ぶ
        //現状はStorageReservationは一つだけなので暫定的に下記のように実装した。
        return storageReservationList.get(0);
    }

    /**
     * ストレージIDに対応したストレージ・リザベーションを取得します.
     * @param id ストレージID
     * @return  ストレージIDに対応したストレージ・リザベーション
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    public StorageReservation getStorageReservationByStorgeId(Long id) throws CloudReservationException {
        for (StorageReservation storageReservation : storageReservationList) {
            if (storageReservation.storageId.equals(id)) {
                return storageReservation;
            }
        }
        throw new CloudReservationException(reservation.id, ResourceType.STORAGE);
    }
}

class DiskSizeComparator implements Comparator<Disk> {
    public static final int ASC = 1; //昇順
    public static final int DESC = -1; //降順
    private int sort = ASC; //デフォルトは昇順


    public DiskSizeComparator(int sort) {
        this.sort = sort;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Disk disk1, Disk disk2) {
        if (disk1 == null && disk2 == null) {
            return 0; // arg0 = arg1
        } else if (disk1 == null) {
            return 1 * sort; // arg1 > arg2
        } else if (disk2 == null) {
            return -1 * sort; // arg1 < arg2
        }
        return (disk1.size - disk2.size) * sort;
    }
}

class AvailableSizeMapComparator implements Comparator<Entry<Long, Integer>> {
    public static final int ASC = 1; //昇順
    public static final int DESC = -1; //降順
    private int sort = ASC; //デフォルトは昇順


    public AvailableSizeMapComparator(int sort) {
        this.sort = sort;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Entry<Long, Integer> entry1, Entry<Long, Integer> entry2) {
        if (entry1 == null && entry2 == null) {
            return 0; // arg0 = arg1
        } else if (entry1 == null) {
            return 1 * sort; // arg1 > arg2
        } else if (entry2 == null) {
            return -1 * sort; // arg1 < arg2
        }
        return (entry1.getValue() - entry2.getValue()) * sort;
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
