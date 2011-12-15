/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

/**
 * <p>注文詳細の種別.
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
public enum OrderItemType {
    /** 
     * VM作成
     * {@link OrderItemDto#data}には{@link jp.co.ntts.vhut.entity.Vm}が入る
     */
    CREATE_VM
    /** テンプレート作成
     * {@link OrderItemDto#data}には{@link jp.co.ntts.vhut.entity.Template}が入る
     */
    , CREATE_TEMPLATE
    /** 
     * VM起動
     * {@link OrderItemDto#data}には{@link jp.co.ntts.vhut.entity.Vm}が入る
     */
    , START_VM
    /**
     * NW利用
     * {@link OrderItemDto#data}には{@link jp.co.ntts.vhut.entity.Network}が入る
      */
    , OBTAIN_NETWORK
    /**
     *  外部IP利用
     * {@link OrderItemDto#data}には{@link ??}が入る TBD
     */
    , OBTAIN_PUBLIC_IP;
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
