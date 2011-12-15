/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.CloudTroubleDto;
import jp.co.ntts.vhut.dto.VhutUserDto;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.exception.AuthenticationException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudInfraLogic;

import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>ベーシックサービスのクラス.
 * <br>
 * <p>
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
public class BasicService {

    /**
     * CloudLogicを取得するためのファクトリ
     */
    public CloudLogicFactory cloudLogicFactory;

    /**
     * サービス関連の設定
     */
    public ServiceConfig serviceConfig;

    /**
     * ユーザ関連のセッションオブジェクト
     */
    public VhutUserDto vhutUserDto;

    /**
     * クラウドの識別子
     */
    protected long cloudId = 1;

    /**
     * クラウドロジックのインフラ面
     */
    protected ICloudInfraLogic cloudInfraLogic;


    @InitMethod
    public void init() {
        cloudInfraLogic = cloudLogicFactory.newCloudInfraLogic(cloudId);
    }

    /**
     * 現在のセッションユーザを取得します.
     * @return セッションユーザ
     * @throws AuthenticationException 認証例外
     */
    public VhutUser getCurrentUser() throws AuthenticationException {
        return vhutUserDto.getVhutUser();
    }

    /**
     * <p>パフォーマンス評価取得.
     * <br>
     *
     * @param なし
     * @return performance パフォーマンス評価
     */
    @Auth(right = Right.READ_SYS_BASIC)
    public int getPerformanceAbstraction() {
        short performance;
        performance = cloudInfraLogic.getAbstractPerformance();
        return (int) performance;
    }

    /**
     * <p>障害情報取得.
     * <br>
     *
     * @param なし
     * @return boolean 障害の有無
     */
    @Auth(right = Right.READ_SYS_BASIC)
    public boolean getTroubleAbstraction() {
        CloudTroubleDto cloudTroubleDto = cloudInfraLogic.getTroubleAbstractionList();
        //取得したCloudTroubleDtoのsizeが0であれば、正常(true)を返し
        //それ以外であれば異常（false）を返す。
        if (cloudTroubleDto.vmList != null) {
            if (cloudTroubleDto.vmList.size() > 0) {
                return false;
            }
        }
        if (cloudTroubleDto.hostList != null) {
            if (cloudTroubleDto.hostList.size() > 0) {
                return false;
            }
        }
        return true;
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
