/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.ntts.vhut.entity.ClusterReservation;
import jp.co.ntts.vhut.entity.ClusterReservationNames;
import jp.co.ntts.vhut.entity.PublicIpReservation;
import jp.co.ntts.vhut.entity.PublicIpReservationNames;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.StorageReservation;
import jp.co.ntts.vhut.entity.StorageReservationNames;
import jp.co.ntts.vhut.entity.VlanReservation;
import jp.co.ntts.vhut.entity.VlanReservationNames;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * <p>//
 * <br>
 * <p>//
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 */
class ResourceConsumptionUnit {

    static public ResourceConsumptionUnit getResourceConsumption(Reservation reservation, JdbcManager jdbcManager) {
        if (reservation.clusterReservationList == null) {
            reservation.clusterReservationList = jdbcManager.from(ClusterReservation.class)
                .where((new SimpleWhere()).eq(ClusterReservationNames.reservationId(), reservation.id))
                .orderBy(ClusterReservationNames.clusterId()
                    .toString())
                .getResultList();
        }
        if (reservation.storageReservationList == null) {
            reservation.storageReservationList = jdbcManager.from(StorageReservation.class)
                .where((new SimpleWhere()).eq(StorageReservationNames.reservationId(), reservation.id))
                .orderBy(StorageReservationNames.storageId()
                    .toString())
                .getResultList();
        }
        if (reservation.vlanReservationList == null) {
            reservation.vlanReservationList = jdbcManager.from(VlanReservation.class)
                .where((new SimpleWhere()).eq(VlanReservationNames.reservationId(), reservation.id))
                .getResultList();
        }
        if (reservation.publicIpReservationList == null) {
            reservation.publicIpReservationList = jdbcManager.from(PublicIpReservation.class)
                .where((new SimpleWhere()).eq(PublicIpReservationNames.reservationId(), reservation.id))
                .getResultList();
        }

        return getResourceConsumption(reservation);
    }

    static public ResourceConsumptionUnit getResourceConsumption(Reservation reservation) {
        ResourceConsumptionUnit result = new ResourceConsumptionUnit();

        for (ClusterReservation rsv : reservation.clusterReservationList) {
            result.cpuResource.put(rsv.clusterId, rsv.cpuCore);
            result.memoryResource.put(rsv.clusterId, rsv.memory);
        }
        for (StorageReservation rsv : reservation.storageReservationList) {
            result.storageResource.put(rsv.storageId, rsv.size);
        }
        result.vlanResource = reservation.vlanReservationList.size();
        result.publicIpResource = reservation.publicIpReservationList.size();

        return result;
    }


    /**
     * @return clusterIdと使用するCPUリソースのMap
     */
    Map<Long, Integer> cpuResource;

    /**
     * @return clusterIdと使用するメモリリソースのMap
     */
    Map<Long, Integer> memoryResource;

    /**
     * @return 使用するストレージリソース(MB)
     */
    Map<Long, Integer> storageResource;

    /**
     * @return clusterIdと使用するVLANリソースのMap
     */
    int vlanResource;

    /**
     * @return 使用する外部IPリソース
     */
    int publicIpResource;


    /**
     * 
     */
    public ResourceConsumptionUnit() {
        this.cpuResource = new HashMap<Long, Integer>();
        this.memoryResource = new HashMap<Long, Integer>();
        this.storageResource = new HashMap<Long, Integer>();
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
