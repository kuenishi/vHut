package jp.co.ntts.vhut.dto;

import java.util.List;

import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.Host;
import jp.co.ntts.vhut.entity.VhutUser;

/**
 * <p>サービス関連の障害ポイントのデータモデルクラス.
 * <br>
 * <p>
 * 
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * 
 */
public class ServiceTroubleDto {
    /**
     * 障害が発生しているユーザのリスト
     */
    public List<VhutUser> userList;

    /**
     * 障害が発生しているアプリケーションインスタンスグループのリスト
     */
    public List<ApplicationInstanceGroup> aigList;

    /**
     * 障害が発生しているアプリケーションのリスト
     */
    public List<Application> aList;

    /**
     * 障害が発生しているホストのリスト
     */
    public List<Host> hostList;

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
