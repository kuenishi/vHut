/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.dto.CloudInfraPerformanceDto;
import jp.co.ntts.vhut.dto.CloudTroubleDto;

/**
 * <p>プライベートクラウドのインフラに関わる操作を行うインターフェース.
 *
 * <p>クラウドをパブリックとプライベートに二分すると、
 * パブリッククラウドとプライベートクラウド両方に共通する操作と、
 * パブリッククラウドには不要だがプライベートクラウドには必要な操作がある。
 * 前者を「サービス関連操作」、後者を「インフラ関連操作」として分類し、
 * 以下のように二つのインターフェースとして定義している。
 * <ul>
 * <li>「サービス関連操作」= ICloudServiceLogic
 * <li>「インフラ関連捜査」= ICloudInfraLogic
 * </ul>
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
public interface ICloudInfraLogic {

    /**
     * クラウドの固有IDをしていします.
     * @param cloudId クラウドの固有ID
     */
    void setCloudId(long cloudId);

    /**
     * <p>初期設定取得.
     * <p>クラウドのサービス関連の初期設定値を取得する。
     *
     * @return クラウドのサービス関連の初期設定値
     */
    CloudConfig getCloudConfiguration();

    /**
     * <p>パフォーマンス概要情報取得.
     *
     * @return インフラのパフォーマンスを5段階で表した値.
     */
    short getAbstractPerformance();

    /**
     * <p>パフォーマンス詳細取得.
     *
     * @return パフォーマンス詳細情報
     */
    CloudInfraPerformanceDto getPerformance();

    /**
     * <p>障害ポイント概要一覧取得.
     *
     * @return 障害ポイント概要情報
     */
    CloudTroubleDto getTroubleAbstractionList();

    /**
     * 初期設定値を基にネットワークテーブルを最新化する.
     * Experimental
     */
    void initializeNetworks();

    /**
     * @return すべてのネットワークが未アサイン状態です.
     */
    boolean isAnyNetworksIsNotAssigned();

    /**
     * VLANリソーステーブルを現行化します.
     */
    void updateVlanResources();

    /**
     * 外部IPリソーステーブルを現行化します.
     */
    void updatePublicIpResources();

    /**
     * すべてのネットワークを削除します.
     */
    void clearNetworks();

    /**
     * ストレージリソーステーブルを現行化します.
     */
    void updateStorageResource();

    /**
     * クラスタリソーステーブルを現行化します.
     */
    void updateClusterResource();

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
