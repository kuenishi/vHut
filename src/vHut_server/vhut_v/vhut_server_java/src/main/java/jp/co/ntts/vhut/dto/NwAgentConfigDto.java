/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import java.io.Serializable;
import java.util.HashMap;

import jp.co.ntts.vhut.entity.Network;

/**
 * <p>ネットワークエージェントの設定情報を格納するDTO.
 * <p>シングルトンです。
 * <p>情報の更新は{@link }を使って行います。
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
public class NwAgentConfigDto implements Serializable {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -984606106640480287L;

    public boolean initialized = false;
    public String publicif;
    public String privateif;
    public String mynetwork;
    public String mynetmask;
    public String netfilter;
    public HashMap<String, String> addresses;
    public HashMap<String, String> publicips;
    public HashMap<String, Network> networks;

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
