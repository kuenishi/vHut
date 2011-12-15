/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import static jp.co.ntts.vhut.entity.Names.clusterResource;
import static jp.co.ntts.vhut.entity.Names.network;
import static jp.co.ntts.vhut.entity.Names.publicIpReservation;
import static jp.co.ntts.vhut.entity.Names.publicIpResource;
import static jp.co.ntts.vhut.entity.Names.reservation;
import static jp.co.ntts.vhut.entity.Names.storageResource;
import static jp.co.ntts.vhut.entity.Names.vlanResource;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.NoResultException;

import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.OrderItem;
import jp.co.ntts.vhut.entity.ClusterReservation;
import jp.co.ntts.vhut.entity.ClusterResource;
import jp.co.ntts.vhut.entity.ClusterResourceNames;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkStatus;
import jp.co.ntts.vhut.entity.PublicIpReservation;
import jp.co.ntts.vhut.entity.PublicIpResource;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.ResourceType;
import jp.co.ntts.vhut.entity.StorageReservation;
import jp.co.ntts.vhut.entity.StorageResource;
import jp.co.ntts.vhut.entity.StorageResourceNames;
import jp.co.ntts.vhut.entity.Term;
import jp.co.ntts.vhut.entity.VlanReservation;
import jp.co.ntts.vhut.entity.VlanResource;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.CloudResourceException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.util.Ipv4ConversionUtil;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringConversionUtil;

/**
 * ResourceからReservationを切りだす作業を引き受けます.
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
final class ReservationQuota {

    /**
     * ロガー.
     */
    private static VhutLogger logger = VhutLogger.getLogger(ReservationQuota.class);

    private final PrivateCloudLogic parent;


    /**
     * コンストラクタ.
     * @param parent 所属するPrivateCloudLogic
     */
    public ReservationQuota(PrivateCloudLogic parent) {
        this.parent = parent;
    }

    /**
     * リソース要求を追加して予約を拡張します。
     * @param reservationId 追加される予約のID
     * @param order リソース要求
     * @return 予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    public Reservation addReservation(long reservationId, OrderDto order) throws CloudResourceException {
        return appendOrderToReservation(reservationId, order);
    }

    /**
     * 予約申請から予約を作成します.
     * @param order 予約申請
     * @return 予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    public Reservation createReservation(OrderDto order) throws CloudResourceException {
        Reservation result = null;

        /* substitited by the implementation below 
         * TimestampUtil.checkDateIntegrity(order);
         */
        if (order.startTime == null) {
            order.startTime = TimestampUtil.getCurrentTimestamp();
        }
        if (order.endTime == null) {
            order.endTime = parent.cloudConfig.getReservationEndTimeMax();
        }

        result = new Reservation();
        result.startTime = order.startTime;
        result.endTime = order.endTime;

        this.parent.jdbcManager.insert(result)
            .execute();

        result = this.appendOrderToReservation(result, order);

        return result;
    }

    /**
     * 予約申請のリストをもとに新規の予約を複数同時に作成します.
     * @param orderList 予約申請のリスト
     * @return 作成された予約のリスト
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    public List<Reservation> createReservationList(List<OrderDto> orderList) throws CloudResourceException {
        if (orderList == null || orderList.size() == 0) {
            throw new InputRuntimeException("orderList", "Orderd are not exist.");
        }

        List<Reservation> result = new ArrayList<Reservation>();

        for (OrderDto order : orderList) {
            result.add(this.createReservation(order));
        }

        return result;
    }

    /**
     * 予約を削除します.
     * @param reservationId 予約ID
     */
    public void deleteReservation(long reservationId) {
        Reservation rsv = this.deleteReservationContent(reservationId);
        this.parent.jdbcManager.delete(rsv)
            .execute();
    }

    /**
     * 予約の削除に必要な関連項目を削除します.
     * @param reservationId 予約ID
     * @return 予約
     */
    private Reservation deleteReservationContent(long reservationId) {
        // 削除対象ReservationをDBから取得
        Reservation result = this.parent.jdbcManager.from(Reservation.class)
            .leftOuterJoin(reservation().clusterReservationList())
            .leftOuterJoin(reservation().clusterReservationList()
                .clusterReservationVmMapList())
            .leftOuterJoin(reservation().storageReservationList())
            .leftOuterJoin(reservation().storageReservationList()
                .storageReservationDiskMapList())
            .leftOuterJoin(reservation().storageReservationList()
                .storageReservationDiskTemplateMapList())
            .leftOuterJoin(reservation().vlanReservationList())
            .leftOuterJoin(reservation().publicIpReservationList())
            .id(reservationId)
            .getSingleResult();
        if (result == null) {
            throw new InputRuntimeException("reservationId", String.format("Reservation(id=%d) is not exists.", reservationId));
        }

        // 各種予約のリソース消費サイズを取得すると同時にDBから削除
        // 現在の実装では，予約に紐付いているVm,Disk,Template等の削除は行わない
        // 上記を実現するには，Vm,Disk,Template等の予約からの参照カウントが0になっているかどうか
        // (どの予約からも参照されていないかどうか)チェックする必要がある

        // クラスター予約
        Map<Long, Integer> cpucsm = new HashMap<Long, Integer>();
        Map<Long, Integer> memcsm = new HashMap<Long, Integer>();
        for (ClusterReservation trsv : result.clusterReservationList) {
            addToMap(cpucsm, trsv.clusterId, trsv.cpuCore);
            addToMap(memcsm, trsv.clusterId, trsv.memory);
            if (trsv.clusterReservationVmMapList.size() > 0) {
                this.parent.jdbcManager.deleteBatch(trsv.clusterReservationVmMapList)
                    .execute();
            }
        }
        if (result.clusterReservationList.size() > 0) {
            this.parent.jdbcManager.deleteBatch(result.clusterReservationList)
                .execute();
        }
        result.clusterReservationList = new ArrayList<ClusterReservation>();

        // ストレージ予約
        Map<Long, Integer> strgcsm = new HashMap<Long, Integer>();
        for (StorageReservation trsv : result.storageReservationList) {
            addToMap(strgcsm, trsv.storageId, trsv.size);
            if (trsv.storageReservationDiskMapList.size() > 0) {
                this.parent.jdbcManager.deleteBatch(trsv.storageReservationDiskMapList)
                    .execute();
            }
            if (trsv.storageReservationDiskTemplateMapList.size() > 0) {
                this.parent.jdbcManager.deleteBatch(trsv.storageReservationDiskTemplateMapList)
                    .execute();
            }
        }

        if (result.storageReservationList.size() > 0) {
            this.parent.jdbcManager.deleteBatch(result.storageReservationList)
                .execute();
        }
        result.storageReservationList = new ArrayList<StorageReservation>();

        // VLAN予約
        int vlancsm = result.vlanReservationList.size();
        if (vlancsm > 0) {
            this.parent.jdbcManager.deleteBatch(result.vlanReservationList)
                .execute();
        }
        result.vlanReservationList = new ArrayList<VlanReservation>();

        // Public IP 予約
        int pipcsm = result.publicIpReservationList.size();
        if (pipcsm > 0) {
            this.parent.jdbcManager.deleteBatch(result.publicIpReservationList)
                .execute();
        }
        result.publicIpReservationList = new ArrayList<PublicIpReservation>();

        //クラスタリソースの解放
        for (Long clusterId : cpucsm.keySet()) {
            updateClusterResource(-cpucsm.get(clusterId), -memcsm.get(clusterId), result.startTime, result.endTime, clusterId);
        }
        //ストレージリソースの解放
        for (Long storageId : strgcsm.keySet()) {
            updateStorageResource(-strgcsm.get(storageId), result.startTime, result.endTime, storageId);
        }
        //VLANリソースの解放
        updateVlanResource(-vlancsm, result.startTime, result.endTime);
        //PublicIPリソースの解放
        updatePublicIpResource(-pipcsm, result.startTime, result.endTime);
        return result;
    }

    /**
     * @param cpuDelta CPUコアの差分
     * @param memDelta メモリの差分
     * @param startTime 開始日
     * @param endTime 終了日
     * @param clusterId クラスタID
     */
    private void updateClusterResource(int cpuDelta, int memDelta, Timestamp startTime, Timestamp endTime, Long clusterId) {
        List<ClusterResource> resources = parent.jdbcManager.from(ClusterResource.class)
            .where(new SimpleWhere().ge(clusterResource().time(), TimestampUtil.floorAsDate(startTime))
                .le(clusterResource().time(), TimestampUtil.floorAsDate(endTime))
                .eq(clusterResource().clusterId(), clusterId))
            .orderBy(storageResource().time()
                .toString())
            .getResultList();
        for (ClusterResource resource : resources) {
            resource.cpuCoreTerminablyUsed += cpuDelta;
            if (resource.cpuCoreTerminablyUsed < 0) {
                resource.cpuCoreTerminablyUsed = 0;
                logResourceMinus(ResourceType.CLUSTER, resource.id, resource.time);
            }
            resource.memoryTerminablyUsed += memDelta;
            if (resource.memoryTerminablyUsed < 0) {
                resource.memoryTerminablyUsed = 0;
                logResourceMinus(ResourceType.CLUSTER, resource.id, resource.time);
            }
        }
        parent.jdbcManager.updateBatch(resources)
            .includes(clusterResource().cpuCoreTerminablyUsed(), clusterResource().memoryTerminablyUsed())
            .execute();
    }

    /**
     * @param delta 差分
     * @param startTime 開始日
     * @param endTime 終了日
     * @param storageId ストレージID
     */
    private void updateStorageResource(int delta, Timestamp startTime, Timestamp endTime, Long storageId) {
        List<StorageResource> resources = parent.jdbcManager.from(StorageResource.class)
            .where(new SimpleWhere().ge(storageResource().time(), TimestampUtil.floorAsDate(startTime))
                .le(storageResource().time(), TimestampUtil.floorAsDate(endTime))
                .eq(storageResource().storageId(), storageId))
            .orderBy(storageResource().time()
                .toString())
            .getResultList();
        for (StorageResource resource : resources) {
            resource.storageTerminablyUsed += delta;
            if (resource.storageTerminablyUsed < 0) {
                resource.storageTerminablyUsed = 0;
                logResourceMinus(ResourceType.STORAGE, resource.id, resource.time);
            }
        }
        parent.jdbcManager.updateBatch(resources)
            .includes(storageResource().storageTerminablyUsed())
            .execute();
    }

    /**
     * @param delta 差分
     * @param startTime 開始日
     * @param endTime 終了日
     */
    private void updateVlanResource(int delta, Timestamp startTime, Timestamp endTime) {
        List<VlanResource> resources = parent.jdbcManager.from(VlanResource.class)
            .where(new SimpleWhere().ge(vlanResource().time(), TimestampUtil.floorAsDate(startTime))
                .le(vlanResource().time(), TimestampUtil.floorAsDate(endTime)))
            .orderBy(vlanResource().time()
                .toString())
            .getResultList();
        for (VlanResource resource : resources) {
            resource.vlanTerminablyUsed += delta;
            if (resource.vlanTerminablyUsed < 0) {
                resource.vlanTerminablyUsed = 0;
                logResourceMinus(ResourceType.VLAN, resource.id, resource.time);
            }
        }
        parent.jdbcManager.updateBatch(resources)
            .includes(vlanResource().vlanTerminablyUsed())
            .execute();
    }

    /**
     * @param delta 差分
     * @param startTime 開始日
     * @param endTime 終了日
     */
    private void updatePublicIpResource(int delta, Timestamp startTime, Timestamp endTime) {
        List<PublicIpResource> resources = parent.jdbcManager.from(PublicIpResource.class)
            .where(new SimpleWhere().ge(publicIpResource().time(), TimestampUtil.floorAsDate(startTime))
                .le(publicIpResource().time(), TimestampUtil.floorAsDate(endTime)))
            .orderBy(publicIpResource().time()
                .toString())
            .getResultList();
        for (PublicIpResource resource : resources) {
            resource.publicIpTerminablyUsed += delta;
            if (resource.publicIpTerminablyUsed < 0) {
                resource.publicIpTerminablyUsed = 0;
                logResourceMinus(ResourceType.PUBLIC_IP, resource.id, resource.time);
            }
        }
        parent.jdbcManager.updateBatch(resources)
            .includes(publicIpResource().publicIpTerminablyUsed())
            .execute();
    }

    /**
     * @param cluster
     * @param id
     * @param tt
     */
    private void logResourceMinus(ResourceType type, Long id, Date date) {
        logger.log("WCLDL5014", new Object[]{ type.name(), id, StringConversionUtil.toString(date, "yyyy/MM/dd") });
    }

    /**
     * @param map マップ
     * @param key key
     * @param value 値
     */
    private void addToMap(Map<Long, Integer> map, Long key, Integer value) {
        if (map.get(key) == null) {
            map.put(key, value);
        } else {
            map.put(key, map.get(key) + value);
        }
    }

    /**
     * 新規の予約申請を基に既存の予約を更新します.
     * @param reservationId 既存の予約ID
     * @param order 新規の予約申請
     * @return 更新された予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    public Reservation updateReservation(long reservationId, OrderDto order) throws CloudResourceException {
        Reservation result = this.deleteReservationContent(reservationId);

        if (order.startTime == null) {
            order.startTime = TimestampUtil.getCurrentTimestamp();
        }
        if (order.endTime == null) {
            order.endTime = parent.cloudConfig.getReservationEndTimeMax();
        }

        result.startTime = order.startTime;
        result.endTime = order.endTime;

        this.parent.jdbcManager.update(result)
            .execute();
        result = this.appendOrderToReservation(result, order);
        return result;
    }

    /**
     * @see jp.co.ntts.vhut.logic.PrivateCloudLogic#getTermListToReserve(OrderDto)
     * @param order
     * @return orderで要求された期間の内，予約可能なTermのリスト
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    public List<Term> getTermListToReserve(OrderDto order) {
        List<Term> result = new ArrayList<Term>();

        try {
            dispatchResource(order);
        } catch (CloudResourceException e) {
            return result;
        }

        CheckResult chkres = this.checkResource(order);

        Map<Date, Shortage> shortageMap = new HashMap<Date, Shortage>();
        for (Shortage sh : chkres.shortageList) {
            shortageMap.put(sh.date, sh);
        }

        boolean isAdding = false;

        Term term = new Term();

        for (Date rsvdate = TimestampUtil.newDate(order.startTime); rsvdate.before(TimestampUtil.newDate(order.endTime)); rsvdate.setTime(rsvdate.getTime() + TimestampUtil.Unit.DAY.getValue())) {

            if (shortageMap.containsKey(rsvdate)) {
                //リソース不足
                if (isAdding) {
                    isAdding = false;
                    term.endTime = TimestampUtil.subtract(TimestampUtil.newTimestamp(rsvdate), 1, TimestampUtil.Unit.DAY);
                }
            } else {
                //予約可能
                if (!isAdding) {
                    isAdding = true;
                    term = new Term();
                    result.add(term);
                    term.startTime = TimestampUtil.newTimestamp(rsvdate);
                    term.endTime = null;
                }
            }
        }
        if (term != null && term.endTime == null) {
            term.endTime = (Timestamp) order.endTime.clone();
        }

        return result;
    }

    /**
     * @param order
     * @param orderset
     * @return
     */
    private CheckResult checkResource(OrderDto order) {
        // 関連リソースの抽出
        PrivateCloudLogic.Conditions conds = parent.new Conditions(order.startTime, order.endTime, order.getCpuResource()
            .keySet(), order.getStorageResource()
            .keySet(), parent.getCloudId());
        Map<Date, ResourceSet> rsrcsetmap = parent.getResourceSetMap(conds);
        // リソースのチェックと消費
        return checkAndConsumeResource(order, rsrcsetmap);
    }

    /**
     * リソース要求に基づいてリソースが確保可能か判定します.
     *
     * @param order リソース要求
     * @return 判定結果
     */
    private CheckResult consumeResource(OrderDto order) {
        // 関連リソースの抽出
        PrivateCloudLogic.Conditions conds = parent.new Conditions(order.startTime, order.endTime, order.getCpuResource()
            .keySet(), order.getStorageResource()
            .keySet(), parent.getCloudId());
        Map<Date, ResourceSet> rsrcsetmap = parent.getResourceSetMap(conds);
        // リソースのチェックと消費
        CheckResult result = checkAndConsumeResource(order, rsrcsetmap);

        // リソース・テーブルの更新
        if (result.isOk) { // チェック結果がOKならば更新

            List<ClusterResource> clusterResources = new ArrayList<ClusterResource>();
            List<StorageResource> storageResources = new ArrayList<StorageResource>();
            List<VlanResource> vlanResources = new ArrayList<VlanResource>();
            List<PublicIpResource> publicIpResources = new ArrayList<PublicIpResource>();

            for (Date date : rsrcsetmap.keySet()) {
                ResourceSet rsrcset = rsrcsetmap.get(date);
                for (Long clusterId : rsrcset.cluster.keySet()) {
                    clusterResources.add(rsrcset.cluster.get(clusterId));
                }
                for (Long storageId : rsrcset.storage.keySet()) {
                    storageResources.add(rsrcset.storage.get(storageId));
                }
                vlanResources.add(rsrcset.vlan);
                publicIpResources.add(rsrcset.publicIp);
            }

            parent.jdbcManager.updateBatch(clusterResources)
                .includes(ClusterResourceNames.cpuCoreTerminablyUsed(), ClusterResourceNames.memoryTerminablyUsed())
                .execute();
            parent.jdbcManager.updateBatch(storageResources)
                .includes(StorageResourceNames.storageTerminablyUsed())
                .execute();
            parent.jdbcManager.updateBatch(vlanResources)
                .includes(vlanResource().vlanTerminablyUsed())
                .execute();
            parent.jdbcManager.updateBatch(publicIpResources)
                .includes(publicIpResource().publicIpTerminablyUsed())
                .execute();

        }

        return result;
    }

    /**
     * @param order
     * @param rsrcsetmap
     * @return
     */
    private CheckResult checkAndConsumeResource(OrderDto order, Map<Date, ResourceSet> rsrcsetmap) {

        Map<Long, Integer> ordercpu = order.getCpuResource();
        Map<Long, Integer> ordermem = order.getMemoryResource();
        Map<Long, Integer> orderstrg = order.getStorageResource();
        int ordervlan = order.getVlanResource();
        int orderpip = order.getPublicIpResource();

        CheckResult result = new CheckResult();
        // // checking resources against order
        for (Date date : rsrcsetmap.keySet()) {
            ResourceSet rsrcset = rsrcsetmap.get(date);
            // CPUのチェックと更新
            for (Long clstid : ordercpu.keySet()) {
                ClusterResource rsrc = rsrcset.cluster.get(clstid);
                int touse = rsrc.cpuCoreTerminablyUsed + ordercpu.get(clstid);
                int rest = rsrc.getCpuCoreLimit() - touse;
                if (rest < 0) {
                    result.isOk = false;
                    result.shortageList.add(new Shortage(date, RType.CLUSTER_CPU, clstid, rest));
                } else {
                    rsrc.cpuCoreTerminablyUsed = touse;
                }
            }
            // メモリのチェックと更新
            for (Long clstid : ordermem.keySet()) {
                ClusterResource rsrc = rsrcset.cluster.get(clstid);
                int touse = rsrc.memoryTerminablyUsed + ordermem.get(clstid);
                int rest = rsrc.getMemoryLimit() - touse;
                if (rest < 0) {
                    result.isOk = false;
                    result.shortageList.add(new Shortage(date, RType.CLUSTER_MEMORY, clstid, rest));
                } else {
                    rsrc.memoryTerminablyUsed = touse;
                }
            }
            // ストレージのチェックと更新
            for (Long strgid : orderstrg.keySet()) {
                StorageResource rsrc = rsrcset.storage.get(strgid);
                int touse = rsrc.storageTerminablyUsed + orderstrg.get(strgid);
                int rest = rsrc.getStorageLimit() - touse;
                if (rest < 0) {
                    result.isOk = false;
                    result.shortageList.add(new Shortage(date, RType.STORAGE, strgid, rest));
                } else {
                    rsrc.storageTerminablyUsed = touse;
                }
            }
            // VLANのチェックと更新
            {
                VlanResource rsrc = rsrcset.vlan;
                int touse = rsrc.vlanTerminablyUsed + ordervlan;
                int rest = rsrc.getVlanLimit() - touse;
                if (rest < 0) {
                    result.isOk = false;
                    result.shortageList.add(new Shortage(date, RType.CLOUD_VLAN, this.parent.getCloudId(), rest));
                } else {
                    rsrc.vlanTerminablyUsed = touse;
                }
            }
            // グローバルIPアドレスのチェックと更新
            {
                PublicIpResource rsrc = rsrcset.publicIp;
                int touse = rsrc.publicIpTerminablyUsed + orderpip;
                int rest = rsrc.getPublicIpLimit() - touse;
                if (rest < 0) {
                    result.isOk = false;
                    result.shortageList.add(new Shortage(date, RType.CLOUD_PUBLICIP, this.parent.getCloudId(), rest));
                } else {
                    rsrc.publicIpTerminablyUsed = touse;
                }
            }
        }
        return result;
    }

    /**
     * リソース要求を追加して予約を拡張します.
     *
     * @param reservationId 予約ID
     * @param order リソース要求
     * @return 予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    private Reservation appendOrderToReservation(Long reservationId, OrderDto order) throws CloudResourceException {
        Reservation result = this.parent.jdbcManager.from(Reservation.class)
            .id(reservationId)
            .leftOuterJoin(reservation().clusterReservationList())
            .leftOuterJoin(reservation().storageReservationList())
            .leftOuterJoin(reservation().vlanReservationList())
            .leftOuterJoin(reservation().publicIpReservationList())
            .getSingleResult();

        if (result == null) {
            throw new InputRuntimeException("reservationId", String.format("Reservation(id=%d) is not exists.", reservationId));
        }

        if (!(result.startTime.equals(order.startTime) && result.endTime.equals(order.endTime))) {
            throw new InputRuntimeException("reservationId, order", "Time is not same between Reservation and OrderDto.");
        }

        return this.appendOrderToReservation(result, order);
    }

    /**
     * リソース要求を基に予約を拡張します
     *
     * @param result 追加先の予約
     * @param order 新規のリソース要求
     * @return 拡張された予約（アダプタ）
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    private Reservation appendOrderToReservation(Reservation result, OrderDto order) throws CloudResourceException {

        //リソースを配分していない予約申請にリソースを割り振ります。
        dispatchResource(order);

        CheckResult chk = this.consumeResource(order);

        if (!chk.isOk) {
            ResourceType type = ResourceType.STORAGE;
            Set<ResourceType> types = chk.getSortegeResourceTypes();
            if (types.size() > 0) {
                type = types.iterator()
                    .next();
            }
            throw new CloudResourceException(this.parent.getCloudId(), type);
        }

        Reservation rsv = this.parseOrderToReservation(order);

        rsv.id = result.id;
        // this.storeReservation(rsv);
        // ClusterReservationの追記
        if (rsv.clusterReservationList.size() > 0) {
            for (ClusterReservation arsv : rsv.clusterReservationList) {
                arsv.reservationId = rsv.id;
            }
            this.parent.jdbcManager.insertBatch(rsv.clusterReservationList)
                .execute();
        }
        // StorageReservationの追記
        if (rsv.storageReservationList.size() > 0) {
            for (StorageReservation arsv : rsv.storageReservationList) {
                arsv.reservationId = rsv.id;
            }
            this.parent.jdbcManager.insertBatch(rsv.storageReservationList)
                .execute();
        }
        // VlanReservationの追記
        if (rsv.vlanReservationList.size() > 0) {
            for (VlanReservation arsv : rsv.vlanReservationList) {
                arsv.reservationId = rsv.id;
            }
            this.parent.jdbcManager.insertBatch(rsv.vlanReservationList)
                .execute();
        }
        // PublicIPReservationの追記
        if (rsv.publicIpReservationList.size() > 0) {
            for (PublicIpReservation arsv : rsv.publicIpReservationList) {
                arsv.reservationId = rsv.id;
            }
            this.parent.jdbcManager.insertBatch(rsv.publicIpReservationList)
                .execute();
        }

        result.clusterReservationList.addAll(rsv.clusterReservationList);
        result.storageReservationList.addAll(rsv.storageReservationList);
        result.vlanReservationList.addAll(rsv.vlanReservationList);
        result.publicIpReservationList.addAll(rsv.publicIpReservationList);

        return result;
    }

    /**
     * リソースを配分していない予約申請にリソースを割り振ります.
     * 引数に変更を加えます.
     *
     * @param order 予約申請
     * @return リソース配分済みの予約申請
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    private OrderDto dispatchResource(OrderDto order) throws CloudResourceException {

        Set<Short> vlanWhiteSet = getVlanWhiteSet(order.startTime, order.endTime);

        Set<String> publicIpWhiteOrderList = getPublicIpWhiteOrderList(order.startTime, order.endTime);

        if (order.items == null) {
            return order;
        }

        for (OrderItem item : order.items) {
            if (item.isDispatched()) {
                //割り振られていれば次へ
                continue;
            }
            switch (item.getType()) {
                case CREATE_VM:
                case CREATE_TEMPLATE:
                    //ストレージの割り当て
                    //リソースが単一の場合：即割り当て
                    //リソースが複数の場合：一番空いているものを探す
                    //NEXT: 今回は単一ストレージのみ対応、次期バージョンで複数ストレージ対応
                    long storageId = parent.cloudConfig.getRhevStorageId();
                    Map<Long, Integer> storageMap = item.getStorageResource();
                    Integer storageSize = storageMap.remove(null);
                    if (storageSize != null && storageSize > 0) {
                        addToMap(storageMap, storageId, storageSize);
                    }
                    break;
                case START_VM:
                    //クラスタの割り当て
                    //リソースが単一の場合：即割り当て
                    //リソースが複数の場合：一番空いているものを探す
                    //NEXT: 今回は単一クラスタのみ対応、次期バージョンで複数クラスタ対応
                    long clusterId = parent.cloudConfig.getRhevClusterId();
                    Map<Long, Integer> cpuMap = item.getCpuResource();
                    Integer cpuCore = cpuMap.remove(null);
                    if (cpuCore != null && cpuCore > 0) {
                        addToMap(cpuMap, clusterId, cpuCore);
                    }
                    //NEXT: 今回は単一クラスタのみ対応、次期バージョンで複数クラスタ対応
                    Map<Long, Integer> memoryMap = item.getMemoryResource();
                    Integer memory = memoryMap.remove(null);
                    if (memory != null && memory > 0) {
                        addToMap(memoryMap, clusterId, memory);
                    }
                    break;
                case OBTAIN_NETWORK:
                    assignNetworks(item.getVlanResourceList(), vlanWhiteSet);
                    break;
                case OBTAIN_PUBLIC_IP:
                    //当該期間中のPublicIpReservationを見て空いているPublicIpをアサインする。
                    //String[]を引数に取り、なるべく同じものを探す.
                    assignPublicIpAddresses(item.getPublicIpResourceList(), publicIpWhiteOrderList);
                    break;
                default:
            }
        }
        return order;
    }

    /**
     * 未割当のVLANのセットを作成します.
     *
     * @param startTime 開始時間
     * @param endTime 終了時間
     * @return
     */
    private Set<Short> getVlanWhiteSet(Timestamp startTime, Timestamp endTime) {
        List<Network> assignedNetworkList = parent.jdbcManager.from(Network.class)
            .leftOuterJoin(network().vlanReservationList())
            .leftOuterJoin(network().vlanReservationList()
                .reservation(), false)
            .where(new SimpleWhere().le(network().vlanReservationList()
                .reservation()
                .startTime(), endTime)
                .ge(network().vlanReservationList()
                    .reservation()
                    .endTime(), startTime))
            .getResultList();

        Set<Short> assignedVlanSet = new HashSet<Short>();
        for (Network network : assignedNetworkList) {
            assignedVlanSet.add(network.vlan);
        }

        List<Network> allNetworkList = parent.jdbcManager.from(Network.class)
            .where(new SimpleWhere().ne(network().status(), NetworkStatus.RESERVED_BY_SYSTEM))
            .getResultList();

        Set<Short> allVlanSet = new HashSet<Short>();
        for (Network network : allNetworkList) {
            allVlanSet.add(network.vlan);
        }

        allVlanSet.removeAll(assignedVlanSet);

        return allVlanSet;
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    private Set<String> getPublicIpWhiteOrderList(Timestamp startTime, Timestamp endTime) {

        Set<String> availableIpSet = Ipv4ConversionUtil.getIpAddressSetBetween(parent.cloudConfig.exIpStartAddress, parent.cloudConfig.exIpEndAddress);

        //使用中アドレスリストの作成
        List<PublicIpReservation> assignedReservationList = parent.jdbcManager.from(PublicIpReservation.class)
            .leftOuterJoin(publicIpReservation().reservation(), false)
            .where(new SimpleWhere().le(publicIpReservation().reservation()
                .startTime(), endTime)
                .ge(publicIpReservation().reservation()
                    .endTime(), startTime))
            .getResultList();
        for (PublicIpReservation assignedReservation : assignedReservationList) {
            availableIpSet.remove(assignedReservation.publicIp);
        }

        //除外リストを追加
        for (String ipaddr : parent.cloudConfig.getExIpExcludeList()) {
            availableIpSet.remove(Ipv4ConversionUtil.convertDotToHex(ipaddr));
        }

        return availableIpSet;
    }

    /**
     * 当該期間中のVLanReservationを見て空いているNetworkのVLANをアサインします.
     * short[]を引数に取り、なるべく同じものを探します.
     * @param vlanList vlanの配列 配列の長さ分だけのVLANを確保しに行きます.
     * @param startTime 利用開始時間
     * @param endTime 利用終了時間
     * @return 引数とおなじもの
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    private short[] assignNetworks(short[] vlanList, Set<Short> whiteSet) throws CloudResourceException {
        for (int i = 0; i < vlanList.length; i++) {
            if (!whiteSet.contains(vlanList[i])) {
                for (short vlan : whiteSet) {
                    vlanList[i] = vlan;
                    break;
                }
            }
            if (vlanList[i] == 0) {
                throw new CloudResourceException(parent.getCloudId(), ResourceType.VLAN);
            } else {
                whiteSet.remove(vlanList[i]);
            }
        }
        return vlanList;
    }

    /**
     * 割り当て可能なIPアドレスの配列を元に与えられたIPアドレスの配列を更新します.
     * @param ipList 与えられたIPアドレスの配列
     * @param availableIpSet 割り当て可能なIPアドレスの配列
     * @return 与えられたIPアドレスの配列
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    private String[] assignPublicIpAddresses(String[] ipList, Set<String> availableIpSet) throws CloudResourceException {
        for (int i = 0; i < ipList.length; i++) {
            String requestIp = ipList[i];
            if (!availableIpSet.remove(requestIp)) {
                if (availableIpSet.isEmpty()) {
                    throw new CloudResourceException(parent.getCloudId(), ResourceType.PRIVATE_IP);
                }
                String[] availableIps = availableIpSet.toArray(new String[0]);
                for (String availableIp : availableIps) {
                    if (availableIpSet.remove(availableIp)) {
                        ipList[i] = availableIp;
                        break;
                    }
                }
            }
        }
        return ipList;
    }

    /**
     * リソース予約申請を基にリソース予約エンティティを作成します.
     *
     * @param order リソース予約申請
     * @return リソース予約エンティティ
     */
    private Reservation parseOrderToReservation(OrderDto order) {
        Reservation result = new Reservation();

        for (OrderItem item : order.items) {
            switch (item.getType()) {
                case CREATE_VM:
                case CREATE_TEMPLATE:
                    // / Reservationの作成
                    Map<Long, Integer> storageResourceMap = item.getStorageResource();
                    for (Entry<Long, Integer> entry : storageResourceMap.entrySet()) {
                        StorageReservation rsv = new StorageReservation();
                        rsv.storageId = entry.getKey();
                        rsv.size = entry.getValue();
                        result.storageReservationList.add(rsv);
                    }
                    break;
                case START_VM:
                    // / Reservationの作成
                    Map<Long, Integer> cpuResourceMap = item.getCpuResource();
                    Map<Long, Integer> memoryResourceMap = item.getMemoryResource();
                    for (Entry<Long, Integer> entry : cpuResourceMap.entrySet()) {
                        ClusterReservation rsv = new ClusterReservation();
                        rsv.clusterId = entry.getKey();
                        rsv.cpuCore = entry.getValue();
                        rsv.memory = memoryResourceMap.get(entry.getKey());
                        result.clusterReservationList.add(rsv);
                    }
                    break;
                case OBTAIN_NETWORK:
                    // Reservationの作成
                    /*
                     * 現実装では，item#dataに何があるかに関係なく item#units個のVlanReservationを生成する 
                     * アサインされたVLANに対応するNetwrokを取得してVlanReservationにNetworkIdを設定する 
                     */
                    for (int ii = 0; ii < item.getUnits(); ii++) {
                        try {
                            Network network = parent.jdbcManager.from(Network.class)
                                .where(new SimpleWhere().eq(network().vlan(), item.getVlanResourceList()[ii]))
                                .disallowNoResult()
                                .getSingleResult();
                            VlanReservation rsv = new VlanReservation();
                            rsv.cloudId = this.parent.getCloudId();
                            rsv.networkId = network.id;
                            result.vlanReservationList.add(rsv);
                        } catch (SNonUniqueResultException e) {
                            throw new DBStateRuntimeException(Network.class, 0L);
                        } catch (NoResultException e) {
                            throw new DBStateRuntimeException(Network.class, 0L);
                        }
                    }
                    break;
                case OBTAIN_PUBLIC_IP:
                    // Reservationの作成
                    // (item#dataに入っているVmが持つNetworkAdapterの個数) *
                    // (item#unitsで与えられるVmの個数)
                    // 個のPublicIpReservationを作る
                    Vm tmpitem = (Vm) (item.getData());
                    for (int ii = 0; ii < item.getUnits(); ii++) {
                        PublicIpReservation rsv = new PublicIpReservation();
                        rsv.cloudId = this.parent.getCloudId();
                        rsv.publicIp = item.getPublicIpResourceList()[ii];
                        result.publicIpReservationList.add(rsv);
                    }
                    break;
            }
        }

        return result;
    }


    /**
     * 利用申請の中からリソース情報のみを抽出した情報.
     */
    class OrderSet {
        /** cpuコア数. */
        int orderCpu;
        /** メモリ容量. */
        int orderMemory;
        /** ストレージ容量. */
        int orderStorage;
        /** VLAN数. */
        int orderVlan;
        /** Public IP数. */
        int orderPublicIp;
    }

    /**
     * リソース判定結果.
     */
    class CheckResult {
        /** 判定に使用した日時のO/Rマッパー条件. */
        public SimpleWhere dateCondition;
        /** 判定結果. */
        boolean isOk = true;
        /** リソース不足情報のリスト. */
        List<Shortage> shortageList = new ArrayList<Shortage>();
        /** 判定に使用した日時のリスト. */
        List<Date> dateList = new ArrayList<Date>();


        Set<ResourceType> getSortegeResourceTypes() {
            Set<ResourceType> list = new HashSet<ResourceType>();
            for (Shortage shortage : shortageList) {
                switch (shortage.type) {
                    case CLUSTER_CPU:
                    case CLUSTER_MEMORY:
                        list.add(ResourceType.CLUSTER);
                        break;
                    case STORAGE:
                        list.add(ResourceType.STORAGE);
                    case CLOUD_VLAN:
                        list.add(ResourceType.VLAN);
                    case CLOUD_PUBLICIP:
                        list.add(ResourceType.PUBLIC_IP);
                    default:
                        break;
                }
            }
            return list;
        }

        /**
         * 結果同士を融合させます.
         * @param another 別のリソース判定結果.
         */
        void conflate(CheckResult another) {
            this.isOk = this.isOk && another.isOk;
            this.shortageList.addAll(another.shortageList);
        }
    }

    /** リソース種別. */
    static enum RType {
        /** 0.CPUリソース（クラスタ毎） */
        CLUSTER_CPU,
        /** 1.メモリリソース（クラスタ毎） */
        CLUSTER_MEMORY,
        /** 2.VLANリソース（クラウド毎） */
        CLOUD_VLAN,
        /** 3.ストレージリソース（クラウド毎） */
        STORAGE,
        /** 4.PUBLIC IPリソース（クラウド毎） */
        CLOUD_PUBLICIP;
    }

    /**
     * リソース不足情報.
     */
    class Shortage {
        /** 対象日時. */
        Date date;
        /** リソース種別. */
        RType type;
        /** リソースの対象のID, Cluster, Storage, Cloud */
        Long id;
        /** 不足値 */
        int shortage;


        /**
         * コンストラクタ.
         * @param dt 対象日時
         * @param rt リソース種別
         * @param id ID?
         * @param val 不足値
         */
        Shortage(Date dt, RType rt, Long id, int val) {
            this.date = dt;
            this.type = rt;
            this.id = id;
            this.shortage = val;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append(DateFormat.getDateInstance()
                .format(this.date));
            result.append("; " + this.type);
            result.append("; " + this.id);
            result.append("; " + this.shortage);
            return result.toString();
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
