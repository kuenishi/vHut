package jp.co.ntts.vhut.dto;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * <p>パフォーマンス詳細情報のデータモデルクラス.
 * <br>
 * <p>
 * 
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * 
 */

@Component(instance = InstanceType.PROTOTYPE)
public class PerformanceDto {

    /**
     * アプリケーション数
     */
    public int acviveAp;

    /**
     * リリースされたアプリケーション数
     */
    public int commitedAp;

    /**
     * 実行中の研修の数
     */
    public int activeAig;

    /**
     * 実行中の研修環境の数
     */
    public int activeAi;

    /**
     * クラウドのパフォーマンス詳細情報
     */
    public CloudInfraPerformanceDto cloudInfra;

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
