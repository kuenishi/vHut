/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.ntts.vhut.VhutModule;
import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.dto.NwAgentConfigDto;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.config.core.container.ConfigContainer;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.ShortConversionUtil;

/**
 * <p>ネットワークドライバ.
 * <br>
 * <p>ネットワークエージェントと連携してNW管理サーバの制御行います。
 * <p>以下の機能を提供します。
 * <ul>
 * <li>DHCPルール追加
 * <li>DHPCルール削除
 * <li>NATルール追加
 * <li>NATルール削除
 * <li>設定情報取得
 * </ul>
 * 以下の特徴があります。
 * <ul>
 * <li>Apache XML-RPC ライブリ(http://ws.apache.org/xmlrpc/index.html)を使ってエージェントと通信します。
 * <li>エージェントとのコネクションはコールの都度確立します。
 * <li>SSLに対応しています。
 * <li>エージェントの冗長化に対応しています。
 * </ul>
 * <p><b>SSL対応</b><br>
 * 接続に必要な設定をしたトラストストアがアプリケーションサーバから参照されている必要があります。
 * Tomcatの場合、以下の起動パラメータを設定してください。
 * <ul>
 * <li>-Djavax.net.ssl.trustStore=path/to/keystore
 * <li>-Djavax.net.ssl.keyStore=path/to/keystore
 * <li>-Djavax.net.ssl.keyStorePassword=password
 * </ul>
 * <p><b>エージェントの冗長化</b><br>
 * 二つのエージェントアドレスを設定ファイルに保持し、片方が無応答の場合、もう片方に切り替えます。
 * ロールバック処理等は実装していません。
 * 以下のように動作します。
 * <ul>
 * <li>[IF:エージェントからの応答がないことを検知]
 * <li>一秒待って再送する。
 * <li>[IF:エージェントからの応答がないことを検知]
 * <li>宛先を切り替える。
 * <li>[IF:エージェントからの応答がないことを検知]
 * <li>一秒待って再送する。
 * <li>[IF:エージェントからの応答がないことを検知]
 * <li>上位クラスに接続障害の例外を応答する。
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
public class NwDriver implements IDriver {

    private static final VhutLogger logger = VhutLogger.getLogger(NwDriver.class);

    /**
     * クラウド系モジュールの設定ファイル.
     */
    public CloudConfig cloudConfig;

    /**
     * Networkエージェントの詳細設定.
     */
    public NwAgentConfigDto nwAgentConfigDto;

    /**
     * 冗長化を考慮したXMLRPCクライアント.
     */
    protected RedudantVhutXmlRpcClient client;

    /**
     * クラウドID(初期値:1)
     */
    private long cloudId = 1;


    /**
     * コンテナによって実行される初期化処理.
     */
    @InitMethod
    public void init() {

        ConfigContainer configContainer = SingletonS2Container.getComponent(ConfigContainer.class);
        configContainer.loadToBeans();

        try {
            URL[] urls = new URL[]{ cloudConfig.getPrimaryNwAgentUrl(), cloudConfig.getSecondaryNwAgentUrl() };
            client = new RedudantVhutXmlRpcClient(urls, VhutModule.NWDR);
        } catch (MalformedURLException e) {
            logger.debug("EDRIV7001", e);
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.driver.IDriver#getCloudId()
     */
    @Override
    public long getCloudId() {
        return cloudId;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.driver.IDriver#setCloudId(long)
     */
    @Override
    public void setCloudId(long cloudId) {
        this.cloudId = cloudId;
    }

    /**
     * DHCPにルールを追加します.
     * @param ip 内部IP
     * @param mac MACアドレス
     * @return 結果
     */
    public Boolean addIp(String ip, String mac) {
        validateIp(ip);
        validateMac(mac);
        return (Boolean) client.call(cloudConfig.nwaAddIpMethod, new Object[]{ ip, mac });
    }

    /**
     * DHCPからルールを削除します.
     * @param ip 内部IP
     * @param mac MACアドレス
     * @return 結果
     */
    public Boolean removeIp(String ip, String mac) {
        validateIp(ip);
        validateMac(mac);
        return (Boolean) client.call(cloudConfig.nwaRemoveIpMethod, new Object[]{ ip, mac });
    }

    /**
     * iptablesにnatルールを追加します.
     * @param publicIp パブリックアドレス
     * @param privateIp プライベートアドレス
     * @return 結果
     */
    public Boolean addNat(String publicIp, String privateIp) {
        validateIp(publicIp);
        validateIp(privateIp);
        return (Boolean) client.call(cloudConfig.nwaAddNatMethod, new Object[]{ publicIp, privateIp });
    }

    /**
     * iptablesからnatルールを削除します.
     * @param publicIp パブリックアドレス
     * @param privateIp プライベートアドレス
     * @return 結果
     */
    public Boolean removeNat(String publicIp, String privateIp) {
        validateIp(publicIp);
        validateIp(privateIp);
        return (Boolean) client.call(cloudConfig.nwaRemoveNatMethod, new Object[]{ publicIp, privateIp });
    }

    /**
     * Networkエージェントの詳細設定を取得して{@link NwAgentConfigDto}にセットします.
     * @return Networkエージェントの詳細設定.
     */
    public NwAgentConfigDto getConfig() {
        Object xmlrpcResult = client.call(cloudConfig.nwaGetConfigMethod, new Object[]{});
        return convertToNwAgentConfigDto(xmlrpcResult);
    }

    private void validateIp(String ip) {
        // TODO: IPアドレスの体系に合致することを検証.
    }

    private void validateMac(String ip) {
        // TODO: MACアドレスの体系に合致することを検証.
    }

    private NwAgentConfigDto convertToNwAgentConfigDto(Object obj) {
        try {
            Map<String, Object> root = (Map<String, Object>) obj;
            nwAgentConfigDto.initialized = BooleanConversionUtil.toBoolean(root.get("initialized"));
            nwAgentConfigDto.publicif = (String) root.get("publicif");
            nwAgentConfigDto.privateif = (String) root.get("privateif");
            nwAgentConfigDto.mynetwork = (String) root.get("mynetwork");
            nwAgentConfigDto.mynetmask = (String) root.get("mynetmask");
            nwAgentConfigDto.netfilter = (String) root.get("netfilter");

            nwAgentConfigDto.addresses = (HashMap<String, String>) root.get("addresses");

            nwAgentConfigDto.publicips = (HashMap<String, String>) root.get("publicips");

            nwAgentConfigDto.networks = new HashMap<String, Network>();

            for (Entry<String, Object> entry : ((Map<String, Object>) root.get("networks")).entrySet()) {
                Map<String, Object> network = (Map<String, Object>) entry.getValue();
                Network nw = new Network();
                nw.networkAddress = (String) network.get("network");
                nw.mask = (String) network.get("netmask");
                nw.broadcast = (String) network.get("broadcast");
                nw.gateway = (String) network.get("gateway");
                nw.dns = (String) network.get("nameserver");
                nw.vlan = ShortConversionUtil.toShort(entry.getKey());
                nw.name = (String) network.get("username");
                nwAgentConfigDto.networks.put(entry.getKey(), nw);
            }

        } catch (Exception e) {
            throw ConversionRuntimeException.newOutputException(e);
        }
        return nwAgentConfigDto;
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
