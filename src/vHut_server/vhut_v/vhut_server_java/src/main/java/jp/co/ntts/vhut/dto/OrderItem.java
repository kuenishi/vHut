/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import java.util.HashMap;
import java.util.Map;

import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.logic.IResourceConsumer;

/**
 * <p>予約注文の詳細クラス.
 * <br>
 * コンストラクタはprivate宣言しています.
 * 静的メソッドnewXxxxxを使って作成してください.
 * 作成したインスタンスはOrderDto#itemsにセットして使います.
 *
 * @author NTT Software Corporation.
 * @version 1.0.0
 *
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class OrderItem implements IResourceConsumer {

    /** typeプロパティ */
    private OrderItemType type;

    /** dataプロパティ */
    private IResourceConsumer data;

    /** 申請の個数 */
    private int units = 1;

    /** CPUの要求(KEY:クラスタID, VALUE:コア数) */
    private Map<Long, Integer> cpuResource;

    /** メモリの要求(KEY:クラスタID, VALUE:メモリ容量) */
    private Map<Long, Integer> memoryResource;

    /** ストレージの要求(KEY:ストレージID, VALUE:ストレージ容量) */
    private Map<Long, Integer> storageResource;

    /** VLANの要求(VLAN数) */
    private int vlanResource;

    /** VLANの要求(VLAN番号リスト) */
    private short[] vlanResourceList;

    /** PUBLIC IPの要求(PUBLIC IP数) */
    private int publicIpResource;

    /** IPアドレス(HEX文字)の配列 */
    private String[] publicIpResourceList;

    /** 予約の割り当てがすでにされている */
    private boolean dispatched;


    /**
     * プライベートコンストラクタ.
     */
    private OrderItem() {
    }

    /**
     * 申請の個数を取得します.
     * @return 申請の個数
     */
    public int getUnits() {
        return units;
    }

    /**
     * 申請の個数を設定します.
     * @param value 申請の個数
     */
    public void setUnits(int value) {
        units = value;
        updateResource();
    }

    /**
     * @return the type
     */
    public OrderItemType getType() {
        return type;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @return the storageResource
     */
    public Map<Long, Integer> getStorageResource() {
        return storageResource;
    }

    /**
     * @return the cpuResource
     */
    public Map<Long, Integer> getCpuResource() {
        return cpuResource;
    }

    /**
     * @return the memoryResource
     */
    public Map<Long, Integer> getMemoryResource() {
        return memoryResource;
    }

    /**
     * @return the vlanResource
     */
    public int getVlanResource() {
        return vlanResource;
    }

    /**
     * @return the vlanResourceList
     */
    public short[] getVlanResourceList() {
        return vlanResourceList;
    }

    /**
     * @return the publicIpResource
     */
    public int getPublicIpResource() {
        return publicIpResource;
    }

    /**
     * @return the publicIpResourceList
     */
    public String[] getPublicIpResourceList() {
        return publicIpResourceList;
    }

    /**
     * 予約の割り当てが済んでいる.
     * @return 済んでいる場合True
     */
    public boolean isDispatched() {
        return dispatched;
    }

    /**
     * VM作成用の注文詳細を作成する.
     * @param vm 作成するVMのエンティティ
     * @param numUnits 利用数
     * @return VM作成用の注文詳細
     */
    public static final OrderItem newCreateVmOrderItem(Vm vm, int numUnits) {
        OrderItem dto = new OrderItem();
        dto.type = OrderItemType.CREATE_VM;
        dto.data = vm;
        dto.units = numUnits;
        dto.updateResource();
        return dto;
    }

    /**
     * VM作成用の注文詳細を作成する.
     * @param vm 作成するVMのエンティティ
     * @return VM作成用の注文詳細
     */
    public static final OrderItem newCreateVmOrderItem(Vm vm) {
        return newCreateVmOrderItem(vm, 1);
    }

    /**
     * テンプレート作成用の注文詳細を作成する.
     * @param vm 参照するVMのエンティティ
     * @param numUnits 利用数
     * @return テンプレート作成用の注文詳細
     */
    public static final OrderItem newCreateTemplateOrderItem(Vm vm, int numUnits) {
        OrderItem dto = new OrderItem();
        dto.type = OrderItemType.CREATE_TEMPLATE;
        dto.data = vm;
        dto.units = numUnits;
        dto.updateResource();
        return dto;
    }

    /**
     * テンプレート作成用の注文詳細を作成する.
     * @param vm 参照するVMのエンティティ
     * @return テンプレート作成用の注文詳細
     */
    public static final OrderItem newCreateTemplateOrderItem(Vm vm) {
        return newCreateTemplateOrderItem(vm, 1);
    }

    /**
     * VM起動用の注文詳細を作成する.
     * @param vm 起動するVMのエンティティ
     * @param numUnits 利用数
     * @return VM起動用の注文詳細
     */
    public static final OrderItem newStartVmOrderItem(Vm vm, int numUnits) {
        OrderItem dto = new OrderItem();
        dto.type = OrderItemType.START_VM;
        dto.data = vm;
        dto.units = numUnits;
        dto.updateResource();
        return dto;
    }

    /**
     * VM起動用の注文詳細を作成する.
     * @param vm 起動するVMのエンティティ
     * @return VM起動用の注文詳細
     */
    public static final OrderItem newStartVmOrderItem(Vm vm) {
        return newStartVmOrderItem(vm, 1);
    }

    /**
     * NW利用の注文詳細を作成する.
     * @param sg 利用するNWのエンティティ
     * @param numUnits 利用数
     * @return NW利用の注文詳細
     */
    public static final OrderItem newObtainNetworkOrderItem(SecurityGroup sg, int numUnits) {
        OrderItem dto = new OrderItem();
        dto.type = OrderItemType.OBTAIN_NETWORK;
        dto.data = sg;
        dto.units = numUnits;
        dto.updateResource();
        return dto;
    }

    /**
     * NW利用の注文詳細を作成する.
     * @param sg 利用するNWのエンティティ
     * @return NW利用の注文詳細
     */
    public static final OrderItem newObtainNetworkOrderItem(SecurityGroup sg) {
        return newObtainNetworkOrderItem(sg, 1);
    }

    /**
     * 外部IP利用の注文詳細を作成する.
     * @param numUnits 利用数
     * @return 外部IP利用の注文詳細
     */
    public static final OrderItem newObtainPublicIpOrderItem(int numUnits) {
        OrderItem dto = new OrderItem();
        dto.type = OrderItemType.OBTAIN_PUBLIC_IP;
        dto.units = numUnits;
        dto.updateResource();
        return dto;
    }

    /**
     * 外部IP利用の注文詳細を作成する.
     * @param vm Vm
     * @param numUnits 利用数
     * @return 外部IP利用の注文詳細
     */
    public static final OrderItem newObtainPublicIpOrderItem(Vm vm, int numUnits) {
        OrderItem dto = new OrderItem();
        dto.type = OrderItemType.OBTAIN_PUBLIC_IP;
        dto.units = numUnits;
        dto.data = vm;
        dto.updateResource();
        return dto;
    }

    /**
     * リソースの使用状況を更新します.
     */
    private void updateResource() {

        cpuResource = new HashMap<Long, Integer>();
        memoryResource = new HashMap<Long, Integer>();
        storageResource = new HashMap<Long, Integer>();
        vlanResource = 0;
        publicIpResource = 0;

        switch (type) {
            case CREATE_VM:
                Vm vm = (Vm) data;
                storageResource = vm.getStorageResource();
                //                for (Entry<Long, Integer> entry : vm.template.getStorageResource().entrySet()) {
                //                    Integer vmStorage = storageResource.get(entry.getKey());
                //                    storageResource.put(entry.getKey(), entry.getValue() + (vmStorage == null ? 0 : vmStorage));
                //                }
                for (Long storageId : storageResource.keySet()) {
                    storageResource.put(storageId, (storageResource.get(storageId) * units));
                    if (storageId == null) {
                        dispatched = false;
                    }
                }
                break;
            case CREATE_TEMPLATE: {
                storageResource = data.getStorageResource();
                for (Long storageId : storageResource.keySet()) {
                    storageResource.put(storageId, (storageResource.get(storageId) * units));
                    if (storageId == null) {
                        dispatched = false;
                    }
                }
                break;
            }
            case START_VM: {
                cpuResource = data.getCpuResource();
                for (Long clusterId : cpuResource.keySet()) {
                    cpuResource.put(clusterId, (cpuResource.get(clusterId) * units));
                    if (clusterId == null) {
                        dispatched = false;
                    }
                }
                memoryResource = data.getMemoryResource();
                for (Long clusterId : memoryResource.keySet()) {
                    memoryResource.put(clusterId, (memoryResource.get(clusterId) * units));
                    if (clusterId == null) {
                        dispatched = false;
                    }
                }
                break;
            }
            case OBTAIN_NETWORK: {
                vlanResource = units;
                vlanResourceList = new short[units];
                dispatched = false;
                break;
            }
            case OBTAIN_PUBLIC_IP:
                publicIpResource = units;
                publicIpResourceList = new String[units];
                dispatched = false;
                break;
            default:
                dispatched = false;
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
