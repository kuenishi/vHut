package jp.co.ntts.vhut.driver.rhev;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import jp.co.ntts.vhut.VhutModule;
import jp.co.ntts.vhut.driver.VhutXmlRpcClient;
import jp.co.ntts.vhut.driver.rhev.dto.ResponseDto;

/**
 * <p>RhevドライバのXMLRPC通信クラス.
 * <br>
 * <p>Rhevドライバとエージェント間のデータ送受信に関する処理を実装する。
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
public class RhevXmlRpcClient {

    public VhutXmlRpcClient client;


    /**
     * コンストラクタ.
     * 
     * @param address サーバアドレス.
     * @param port サーバポート.
     * @throws MalformedURLException 
     * @throws MalformedURLException 
     */
    public RhevXmlRpcClient(String address, int port) throws MalformedURLException {
        client = new VhutXmlRpcClient(address, port, VhutModule.RVDR);
    }

    /**
     * XMLRPCクライアントを使って通信した結果を返す.
     * @param method メソッド名称
     * @param parameter 引数
     * @return ResponseDtoクラス
     */
    public ResponseDto getResponse(String method, Object[] parameter) {
        //        Object[] tmp = (Object[]) client.call(method, parameter);
        Object tmp2 = client.call(method, parameter);
        Object[] tmp = (Object[]) tmp2;
        Map<String, Object> result = new HashMap<String, Object>();

        for (Object obj : tmp) {
            Map<String, Object> map = (Map<String, Object>) obj;
            result.putAll(map);
            //            result.put(map);
        }

        //        Map<String, Object> result = (Map<String, Object>) tmp;
        return ResponseDto.newResponseDto(result);
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
