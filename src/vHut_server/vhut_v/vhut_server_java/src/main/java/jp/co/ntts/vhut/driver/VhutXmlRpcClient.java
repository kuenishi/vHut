/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import java.net.MalformedURLException;
import java.net.URL;

import jp.co.ntts.vhut.VhutModule;
import jp.co.ntts.vhut.driver.nw.NwAgentRuntimeException;
import jp.co.ntts.vhut.driver.rhev.RhevAgentRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.seasar.framework.util.ArrayUtil;

/**
 * <p>
 * vHutでのXMLRPC通信クラス。 <br>
 * <p>
 * ドライバとエージェント間のデータ送受信に関する処理を実装する。
 * 
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class VhutXmlRpcClient {

    /**
     * seasarのLoggerインスタンス取得
     */
    private static final VhutLogger logger = VhutLogger.getLogger(VhutXmlRpcClient.class);

    /**
     * APACHE製のXMLRPCクライアント
     */
    protected XmlRpcClient client;

    /**
     * XMLRPCのconfig
     */
    protected XmlRpcClientConfigImpl config;

    /**
     * モジュールタイプ
     */
    protected VhutModule module = VhutModule.COMM;

    protected VhutXmlRpcCommonsTransportFactory vhutXmlRpcCommonsTransportFactory;


    /**
     * コンストラクタ.
     * 共通モジュールに属するものとして作成される。
     * @param address サーバアドレス.
     * @param port サーバポート.
     * @throws MalformedURLException 
     */
    public VhutXmlRpcClient(String address, int port) throws MalformedURLException {
        this(new URL(address + ":" + port), VhutModule.COMM);
    }

    /**
     * コンストラクタ.
     * 
     * @param address サーバアドレス.
     * @param port サーバポート.
     * @param module vHutモジュール種別
     * @throws MalformedURLException 
     */
    public VhutXmlRpcClient(String address, int port, VhutModule module) throws MalformedURLException {
        this(new URL(address + ":" + port), module);
    }

    /**
     * コンストラクタ.
     * 共通モジュールに属するものとして作成される。
     * @param url エージェントのURL
     */
    public VhutXmlRpcClient(URL url) {
        this(url, VhutModule.COMM);
    }

    /**
     * コンストラクタ.
     * 
     * @param url エージェントのURL
     * @param module vHutモジュール種別
     */
    public VhutXmlRpcClient(URL url, VhutModule module) {
        this.module = module;
        config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);
        client = new XmlRpcClient();
        vhutXmlRpcCommonsTransportFactory = new VhutXmlRpcCommonsTransportFactory(client, module);
        client.setTransportFactory(vhutXmlRpcCommonsTransportFactory);
        client.setConfig(config);
    }

    /**
     * ドライバとのデータ送受信を行う。
     * @param method 処理に対するエージェントのメソッド名
     * @param parameter エージェントに送信するパラメータ
     * @return result エージェントからの受信データ
     */
    public Object call(String method, Object[] parameter) {
        try {
            String transactionId = VhutLogger.createTransactionId();
            Object[] modifiedParameter = ArrayUtil.add(new Object[]{ transactionId }, parameter);
            // エージェントへリクエストして結果を得る
            return client.execute(method, modifiedParameter);
        } catch (XmlRpcException e) {
            if (e.code == 0) {
                // 通信エラー
                throw CommunicationRuntimeException.newException(module, e);
            } else {
                // エージェント内部エラー
                switch (module) {
                    case RVDR:
                        throw RhevAgentRuntimeException.newException(e);
                    case NWDR:
                        throw NwAgentRuntimeException.newException(e);
                    default:
                        break;
                }
            }
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
