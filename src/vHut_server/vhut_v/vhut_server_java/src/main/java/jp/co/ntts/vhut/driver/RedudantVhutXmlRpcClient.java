/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.co.ntts.vhut.VhutModule;
import jp.co.ntts.vhut.util.VhutLogger;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * <p>冗長化されたサーバに順番にアクセスするXMLRPCクライアント.
 * <br>
 * 二つのXMLRPCサーバアドレスを設定ファイルに保持し、片方が無応答の場合、もう片方に切り替えます。
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
public class RedudantVhutXmlRpcClient extends VhutXmlRpcClient {

    /**
     * seasarのLoggerインスタンス取得
     */
    private static final VhutLogger logger = VhutLogger.getLogger(RedudantVhutXmlRpcClient.class);

    /**
     * 現状のXMLRPCの設定
     */
    protected XmlRpcClientConfigImpl config;

    /**
     * XMLRPCの設定の配列
     */
    protected List<XmlRpcClientConfigImpl> configs;

    private int currentConfigIndex;

    private int retry = 1;

    private int interval = 1000;


    /**
     * コンストラクタ.
     * 共通モジュールに属するものとして作成される。
     * @param urls エージェントURLのリスト
     */
    public RedudantVhutXmlRpcClient(URL[] urls) {
        this(urls, VhutModule.COMM);
    }

    /**
     * コンストラクタ.
     * @param urls エージェントURLのリスト
     * @param module vHutモジュール種別
     */
    public RedudantVhutXmlRpcClient(URL[] urls, VhutModule module) {
        this(urls, module, 1, 1000);
    }

    /**
     * コンストラクタ.
     * @param urls エージェントURLのリスト
     * @param module vHutモジュール種別
     * @param retry 再試行回数
     * @param interval 再試行の間隔(msec)
     */
    public RedudantVhutXmlRpcClient(URL[] urls, VhutModule module, int retry, int interval) {
        super(urls[0], module);
        configs = new ArrayList<XmlRpcClientConfigImpl>();
        for (URL url : urls) {
            XmlRpcClientConfigImpl conf = new XmlRpcClientConfigImpl();
            conf.setServerURL(url);
            configs.add(conf);
        }
        this.retry = retry;
        this.interval = interval;
    }

    /**
     * 宛先を変更する.
     * @return 変更した際のXMLRPC設定
     */
    public XmlRpcClientConfigImpl rotateConfiguration() {
        currentConfigIndex++;
        if (currentConfigIndex >= configs.size()) {
            currentConfigIndex = 0;
            config = getConfiguration();
        }
        return config;
    }

    /**
     * 現在のXMLRPC設定を返却します.
     * @return 現在のXMLRPC設定
     */
    public XmlRpcClientConfigImpl getConfiguration() {
        return configs.get(currentConfigIndex);
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.driver.VhutXmlRpcClient#call(java.lang.String, java.lang.Object[])
     */
    @Override
    public Object call(String method, Object[] parameter) throws CommunicationRuntimeException {
        int i = 0;
        int j = 0;
        RuntimeException lastRuntimeException = null;
        while (i < configs.size()) {
            client.setConfig(getConfiguration());
            while (j < retry + 1) {
                try {
                    return super.call(method, parameter);
                } catch (CommunicationRuntimeException e) {
                    lastRuntimeException = e;
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                }
                j++;
            }
            j = 0;
            rotateConfiguration();
            i++;
        }
        if (lastRuntimeException != null) {
            throw lastRuntimeException;
        }
        return null;
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
