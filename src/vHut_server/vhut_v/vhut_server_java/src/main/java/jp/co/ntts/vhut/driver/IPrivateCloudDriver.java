/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import java.util.List;

import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Cluster;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.Host;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.Storage;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Vm;

/**
 * <p>プライベートクラウド用のドライバのインタフェースクラス。
 * <br>
 * <p>仮想化ソフト毎のドライバでのメイン処理を定義する。
 * 
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public interface IPrivateCloudDriver extends IDriver {

    /**
     * クラスタ一覧取得。 
     * @param なし
     * @return clusterList クラスタのリスト
     * @throws ValidationRuntimeException 
     */
    List<Cluster> getAllClusters() throws ValidationRuntimeException;

    /**
     * ホスト一覧取得。
     * @param clusterId クラスタID 
     * @return hostList ホストのリスト
     * @throws ValidationRuntimeException 
     */
    List<Host> getHostsByClusterId(long clusterId) throws ValidationRuntimeException;

    /**
     * ネットワーク一覧取得。
     * @param clusterId クラスタID 
     * @return networkList ネットワークのリスト
     * @throws ValidationRuntimeException 
     */
    List<Network> getNetworksByClusterId(long clusterId) throws ValidationRuntimeException;

    /**
     * ストレージ一覧取得。
     * @param
     * @return storageList ストレージのリスト
     * @throws ValidationRuntimeException 
     */
    List<Storage> getAllDataStorages() throws ValidationRuntimeException;

    /**
     * 仮想マシン一覧取得。
     * @param clusterId クラスタID
     * @return vmList 仮想マシンのリスト
     * @throws ValidationRuntimeException 
     */
    List<Vm> getVmsByClusterId(long clusterId) throws ValidationRuntimeException;

    /**
     * テンプレート一覧取得。
     * @param clusterId クラスタID 
     * @return templateList テンプレートのリスト
     * @throws ValidationRuntimeException 
     */
    List<Template> getTemplatesByClusterId(long clusterId) throws ValidationRuntimeException;

    /**
     * ユーザ一覧取得。
     * @param なし
     * @return vhutUserList ユーザのリスト
     * @throws ValidationRuntimeException 
     */
    List<CloudUser> getAllUsers() throws ValidationRuntimeException;

    /**
     * コマンド一覧更新。
     * @param commandList コマンドのリスト
     * @return commandList コマンドのリスト
     * @throws ValidationRuntimeException 
     */
    List<Command> updateCommandList(List<Command> commandList) throws ValidationRuntimeException;

    /**
     * 仮想マシン作成。
     * @param vm 仮想マシン
     * @param commandId コマンドID 
     * @return vm 仮想マシン
     * @throws ValidationRuntimeException 
     */
    Vm createVmAsync(Vm vm, long commandId) throws ValidationRuntimeException;

    /**
     * 仮想マシン削除。
     * @param vmId 仮想マシンID
     * @param commandId コマンドID
     * @return
     * @throws ValidationRuntimeException 
     */
    void deleteVmAsync(long vmId, long commandId) throws ValidationRuntimeException;

    /**
     * 仮想マシンスペック変更。
     * @param vmId 仮想マシンID
     * @param cpuCore CPUコア数
     * @param memory メモリ容量
     * @return vm 仮想マシン
     * @throws ValidationRuntimeException 
     */
    Vm changeSpec(long vmId, int cpuCore, int memory) throws ValidationRuntimeException;

    /**
     * 仮想マシンNIC追加。
     * @param networkAdapter ネットワークアダプタ
     * @return networkAdapter ネットワークアダプタ
     * @throws ValidationRuntimeException 
     */
    NetworkAdapter addNetworkAdapter(NetworkAdapter networkAdapter) throws ValidationRuntimeException;

    /**
     * 仮想マシンNIC削除。
     * @param vmId 仮想マシンID
     * @param networkAdapterId ネットワークアダプタID
     * @return
     * @throws ValidationRuntimeException 
     */
    void removeNetworkAdapter(long vmId, long networkAdapterId) throws ValidationRuntimeException;

    /**
     * 仮想マシンディスク追加。
     * @param disk ディスク
     * @param commandId コマンドID
     * @return disk ディスク
     * @throws ValidationRuntimeException 
     */
    Disk addDiskAsync(Disk disk, long commandId) throws ValidationRuntimeException;

    /**
     * 仮想マシンディスク削除。
     * @param vmId 仮想マシンID
     * @param diskId ディスクID
     * @param commandId コマンドID
     * @return
     * @throws ValidationRuntimeException 
     */
    void removeDiskAsync(long vmId, long diskId, long commandId) throws ValidationRuntimeException;

    /**
     * 仮想マシンユーザ追加。
     * @param vmId 仮想マシンID
     * @param cloudUserId cloudユーザID
     * @return クラウドユーザ
     * @throws ValidationRuntimeException 
     */
    CloudUser addUser(long vmId, long cloudUserId) throws ValidationRuntimeException;

    /**
     * 仮想マシンユーザ削除。
     * @param vmId 仮想マシンID
     * @param vhutUserId ユーザID
     * @return
     * @throws ValidationRuntimeException 
     */
    void removeUser(long vmId, long vhutUserId) throws ValidationRuntimeException;

    /**
     * 仮想マシン起動。
     * @param vmId 仮想マシンID 
     * @param commandId コマンドID
     * @return
     * @throws ValidationRuntimeException 
     */
    void startVmAsync(long vmId, long commandId) throws ValidationRuntimeException;

    /**
     * 仮想マシン停止。
     * @param vmId 仮想マシンID 
     * @param commandId コマンドID
     * @return
     * @throws ValidationRuntimeException 
     */
    void stopVmAsync(long vmId, long commandId) throws ValidationRuntimeException;

    /**
     * 仮想マシンシャットダウン。
     * @param vmId 仮想マシンID 
     * @param commandId コマンドID
     * @return
     * @throws ValidationRuntimeException 
     */
    void shutdownVmAsync(long vmId, long commandId) throws ValidationRuntimeException;

    /**
     * テンプレート作成。
     * @param template テンプレート
     * @param vmId 仮想マシンID 
     * @param commandId コマンドID
     * @return template テンプレート
     * @throws ValidationRuntimeException 
     */
    Template createTemplateAsync(Template template, long vmId, long commandId) throws ValidationRuntimeException;

    /**
     * テンプレート削除。
     * @param templateId テンプレートID
     * @param commandId コマンドID
     * @return
     * @throws ValidationRuntimeException 
     */
    void deleteTemplateAsync(long templateId, long commandId) throws ValidationRuntimeException;

    /**
     * ユーザパスワード変更。
     * @param accountList アカウントのリスト
     * @param password
     * @return
     */
    void changeUsersPassword(List<String> accountList, String password) throws ValidationRuntimeException;

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
