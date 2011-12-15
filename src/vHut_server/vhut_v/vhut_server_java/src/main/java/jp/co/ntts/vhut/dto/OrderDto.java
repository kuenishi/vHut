/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.logic.IResourceConsumer;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * OrderDtoクラス.
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * 
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */

@Component(instance = InstanceType.PROTOTYPE)
public class OrderDto implements IResourceConsumer {

    /** itemsプロパティ */
    public List<OrderItem> items;

    /** リソース利用の開始時間 */
    public Timestamp startTime;

    /** リソース利用の終了時間 */
    public Timestamp endTime;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getCpuResource()
     */
    @Override
    public Map<Long, Integer> getCpuResource() {
        if (items == null) {
            return null;
        } else {
            Map<Long, Integer> result = new HashMap<Long, Integer>();
            for (OrderItem item : items) {
                this.addResourceMap(result, item.getCpuResource());
            }
            return result;
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getMemoryResource()
     */
    @Override
    public Map<Long, Integer> getMemoryResource() {
        if (items == null) {
            return null;
        } else {
            Map<Long, Integer> result = new HashMap<Long, Integer>();
            for (OrderItem item : items) {
                this.addResourceMap(result, item.getMemoryResource());
            }
            return result;
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getStorageResource()
     */
    @Override
    public Map<Long, Integer> getStorageResource() {
        if (items == null) {
            return null;
        } else {
            Map<Long, Integer> result = new HashMap<Long, Integer>();
            for (OrderItem item : items) {
                this.addResourceMap(result, item.getStorageResource());
            }
            return result;
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getVlanResource()
     */
    @Override
    public int getVlanResource() {
        if (items == null) {
            return 0;
        } else {
            int result = 0;
            for (OrderItem item : items) {
                result += item.getVlanResource();
            }
            return result;
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.IResourceCosumer#getPublicIpResource()
     */
    @Override
    public int getPublicIpResource() {
        if (items == null) {
            return 0;
        } else {
            int result = 0;
            for (OrderItem item : items) {
                result += item.getPublicIpResource();
            }
            return result;
        }
    }

    /**
     * リソースマップにリソースマップを追加します.
     * リソースマップ（KEY:クラスタID/ストレージID, VALUE:リソースのID）
     * 
     * @param result 追加先リソースマップ
     * @param summand 追加対象のリソースマップ
     * @return 合算されたリソースマップ
     */
    private Map<Long, Integer> addResourceMap(Map<Long, Integer> result, Map<Long, Integer> summand) {
        Integer val;
        if (summand == null) {
            return result;
        }
        for (Long id : summand.keySet()) {
            val = result.get(id);
            result.put(id, ((val == null) ? summand.get(id) : (val + summand.get(id))));
        }
        return result;
    }

    /**
     * オーダーの内容を単位数分拡張させます.
     * @param unit 単位数
     * @return 拡張されたオーダー
     */
    public OrderDto multiply(int unit) {
        for (OrderItem item : items) {
            item.setUnits(item.getUnits() * unit);
        }
        return this;
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
