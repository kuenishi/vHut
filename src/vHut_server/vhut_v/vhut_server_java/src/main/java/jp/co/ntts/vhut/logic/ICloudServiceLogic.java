/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import jp.co.ntts.vhut.dto.AdditionalDiskDto;
import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.RealmDto;
import jp.co.ntts.vhut.dto.ResourceDto;
import jp.co.ntts.vhut.dto.SpecDto;
import jp.co.ntts.vhut.dto.SwitchTemplateDto;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.ExternalIpRequestMode;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplate;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.SecurityGroupTemplate;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Term;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.CloudReservationException;
import jp.co.ntts.vhut.exception.CloudReservationPeriodException;
import jp.co.ntts.vhut.exception.CloudResourceException;

/**
 * <p>クラウドのサービスに関わる操作を行うインターフェース.
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
public interface ICloudServiceLogic {

    /**
     * クラウドの固有IDをしていします.
     * @param cloudId クラウドの固有ID
     */
    void setCloudId(long cloudId);

    /**
     * <p>リソース表示.
     * <br>
     *
     * @param startTime 開始日時
     * @param endTime 終了日時
     * @return resource リソース情報
     */
    List<ResourceDto> getResourceListByTerm(Timestamp startTime, Timestamp endTime);

    /**
     * <p>ユーザ一覧表示.
     *
     * @return cloudloudUser リソース情報
     */
    List<CloudUser> getAllUserList();

    /**
     * <p>地域一覧表示.
     *
     * @return realm 地域のリスト
     */
    List<RealmDto> getAllRealmList();

    /**
     * <p>テンプレート一覧表示.
     *
     * @return template テンプレートのリスト
     */
    List<Template> getAllTemplateList();

    /**
     * <p>VM 一覧表示.
     *
     * @return VMのリスト
     */
    List<Vm> getAllVmList();

    /**
     * <p>スペック一覧表示.
     *
     * @return スペックのリスト
     */
    List<SpecDto> getAllSpecList();

    /**
     * <p>ネットワーク一覧表示.
     *
     * @return ネットワークのリスト
     */
    List<Network> getAllNetworkList();

    /**
     * <p>ディスクテンプレート一覧表示.
     *
     *
     * @return ディスクテンプレートのリスト
     */
    List<AdditionalDiskDto> getAllAdditionalDiskList();

    /**
     * <p>スイッチテンプレート一覧表示.
     *
     *
     * @return diskTemplate スイッチテンプレートのリスト
     */
    List<SwitchTemplateDto> getAllSwitchTemplateList();

    /**
     * <p>VM作成.
     * <p>新規にVMを作成する。<br>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>テンプレートからVMを作成します。
     * <li>テンプレートにないDiskを追加します。
     * </ul>
     * <p>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.ACreateVmCommand}
     * <li>{@link jp.co.ntts.vhut.command.AAddDiskCommand}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>Vmの作成
     * <li>Diskの作成
     * </ul>
     * <p>
     * このメソッドは引数VMの以下の要素が存在することを前提としています
     * <ul>
     * <li>templateId
     * <li>specId
     * <li>name
     * <li>description
     * <li>diskList
     * </ul>
     * またnetworkAdapterが参照するSecurityGroupはすでに作成済みであることが必要です。
     *
     * @param reservationId 予約ID
     * @param templateId テンプレートのID
     * @param specId スペックのID
     * @param servicePrefix サービスが付けるVmの接頭辞(英数8文字以内)
     * @prama description Vmの概要
     * @return 作成が完了したVM
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    Vm createVm(long reservationId, long templateId, long specId, String servicePrefix, String description) throws CloudReservationException;

    /**
     * <p>VM更新.
     * <p>引数のVMに対して変更実施し、クラウドに渡すとその変更が
     * VMに反映される。
     * VMの情報を更新する。更新されるものとして以下がある。
     * <ul>
     * <li>名前変更
     * <li>概要変更
     * <li>スペック変更
     * <li>ディスク追加/削除
     * </ul>
     * VMの情報を更新する。更新されないものとして以下がある。
     * <ul>
     * <li>VMID
     * <li>地域ID
     * <li>ディスクの容量
     * </ul>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.SChangeSpecCommand}
     * <li>{@link jp.co.ntts.vhut.command.AAddDiskCommand}
     * <li>{@link jp.co.ntts.vhut.command.ARemoveDiskCommand}
     * </ul>
     *
     * @param reservationId 予約ID
     * @param vm VM.
     * <br>vmには子要素として以下がセットされているものとする
     * <ul>
     * <li>vm.diskList
     * </ul>
     *
     * @return 更新後のVM
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    Vm updateVm(long reservationId, Vm vm) throws CloudReservationException;

    /**
     * <p>VM削除.
     *
     * @param vmId VMID
     */
    void deleteVm(long vmId);

    /**
     * <p>VM再作成.
     *
     * @param vmId VMID
     */
    void rebuildVm(long vmId);

    /**
     * <p>VM詳細取得.
     *
     * @param vmId VMID
     * @return VM
     */
    Vm getVmById(long vmId);

    /**
     * <p>VM 起動.
     *
     * @param vmId VMID
     * @return VM
     */
    Vm startVm(long vmId);

    /**
     * <p>VM 停止.
     *
     * @param vmId VMID
     * @return VM
     */
    Vm stopVm(long vmId);

    /**
     * <p>VM シャットダウン.
     *
     * @param vmId VMID
     * @return VM
     */
    Vm shutdownVm(long vmId);

    /**
     * <p>VM ユーザ追加.
     * 追加されたユーザは当該VMに対して起動/停止/コンソールアクセス等の操作が可能になる。
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンの使用者としてユーザを割り当てます。
     * </ul>
     * <p>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.SAddUserCommand}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.entity.VmCloudUserMap}を追加します。
     * </ul>
     * <p>
     * このメソッドはVmおよびCloudUserが存在することを前提としています。
     *
     * @param reservationId 予約ID
     * @param vmId VMID
     * @param cloudUserId クラウドのユーザID
     * @return VM　
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudReservationPeriodException
     */
    Vm addVmUser(long reservationId, long vmId, long cloudUserId) throws CloudReservationPeriodException, CloudReservationException;

    /**
     * <p>VM ユーザ削除.
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンの使用者からユーザを削除します。
     * </ul>
     * <p>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.SRemoveUserCommand}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.entity.VmCloudUserMap}を削除します。
     * </ul>
     * <p>
     * このメソッドはVm,CloudUser,CloudUserMapが存在することを前提としています。
     *
     * @param vmId VMID
     * @param cloudUserId クラウドのユーザID
     * @return VM　
     */
    Vm removeVmUser(long vmId, long cloudUserId);

    /**
     * <p>VMのユーザ一括更新.
     * <p>変更前のユーザIDがアクセス権を持つすべてのVMに対して当該ユーザの権利を削除し、
     * 代わりに変更後のユーザIDに対して権利を付与する。
     *
     * @param oldUserId 変更前のユーザID
     * @param newUserId 変更後のユーザID
     * @return 更新されたVMのリスト　
     */
    List<Vm> batchUpdateVmUser(long oldUserId, long newUserId);

    /**
     * <p>テンプレート作成.
     * <p>指定したVMから新しいテンプレートを作成する。
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンからテンプレートを削除します。
     * </ul>
     * <p>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.ACreateTemplateCommand}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterのPublicIP, PrivateIPにnullを設定
     * <li>SecurityGroupのnetworkIdにnullを指定
     * </ul>
     * <p>
     * このメソッドはSecurityGroupが存在することを前提としています。
     * またSecurityGroupにひもづけた形でVmが登録された場合にのみIPの設定を行います.
     *
     * @param reservationId 予約ID
     * @param vmId VMID
     * @param servicePrefix サービスが付けるtemplateの接頭辞(英数8文字以内)
     * @param description テンプレート概要
     * @return テンプレート
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudReservationPeriodException
     */
    Template createTemplate(long reservationId, long vmId, String servicePrefix, String description) throws CloudReservationPeriodException, CloudReservationException;

    /**
     * <p>テンプレート詳細取得.
     *
     * @param templateId テンプレートID
     * @return テンプレート
     */
    Template getTemplateById(long templateId);

    /**
     * <p>ネットワークを占有する.
     * <br>SecurityGroupの要件を満たすNetworkを選択して関連付ける。
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>DHCPに仮想マシンのMACとPrivateIPを設定します。
     * <li>NATに仮想マシンのPrivateIPとPublicIPを設定します。
     * <li>仮想マシンにNICを追加します。
     * </ul>
     * <p>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.SAddIpCommand}
     * <li>{@link jp.co.ntts.vhut.command.SAddNatCommand}
     * <li>{@link jp.co.ntts.vhut.command.SAddNetworkAdapterCommand}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterにPublicIP, PrivateIP, vlanを設定
     * <li>SecurityGroupにnetworkIdを指定
     * </ul>
     * <p>
     * このメソッドはSecurityGroupが存在することを前提としています。
     * またSecurityGroupにひもづけた形でVmが登録された場合にのみIPの設定を行います.
     *
     * @param reservationId 予約ID
     * @param securityGroupId NetworkをセットするSecurityGroupId
     * @param exIpRequestMode 外部IPの申請モード.
     * @return ネットワーク
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudReservationPeriodException
     */
    Network obtainNetwork(long reservationId, long securityGroupId, ExternalIpRequestMode exIpRequestMode) throws CloudReservationPeriodException, CloudReservationException;

    /**
     * <p>ネットワークを開放する.
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンからNICを削除します。
     * <li>DHCPから仮想マシンのMACとPrivateIPの設定を削除します。
     * <li>NATから仮想マシンのPrivateIPとPublicIPの設定を削除します。
     * </ul>
     * <p>
     * このメソッドは以下のCommandを利用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.command.SRemoveNetworkAdapterCommandUnitTest}
     * <li>{@link jp.co.ntts.vhut.command.SRemoveIpCommand}
     * <li>{@link jp.co.ntts.vhut.command.SRemoveNatCommand}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterのPublicIP, PrivateIPにnullを設定
     * <li>SecurityGroupのnetworkIdにnullを指定
     * </ul>
     * <p>
     * このメソッドはSecurityGroupが存在することを前提としています。
     * またSecurityGroupにひもづけた形でVmが登録された場合にのみIPの設定を行います.
     * @param securityGroupId ネットワークを占有しているセキュリティグループのID
     */
    void releaseNetwork(long securityGroupId);

    /**
     * <p>リソース予約作成.
     * <p>オーダーを基にして新規に予約を作成する。
     *
     * @param order オーダー
     * @return 予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    Reservation createReservation(OrderDto order) throws CloudResourceException;

    /**
     * <p>リソース予約追加.
     * <p>既存の予約にオーダーの内容を追加する。
     *
     * @param reservationID 予約ID
     * @param order オーダー
     * @return 予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    Reservation addReservation(long reservationID, OrderDto order) throws CloudResourceException;

    /**
     * <p>リソース予約更新.
     * <p>既存の予約の内容をオーダーに合ったものに変更する。
     *
     * @param reservationID 予約ID
     * @param order オーダー
     * @return 予約
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    Reservation updateReservation(long reservationID, OrderDto order) throws CloudResourceException;

    /**
     * <p>リソース予約削除.
     *
     * @param reservationID 予約ID
     */
    void deleteReservation(long reservationID);

    //    /**
    //     * <p>リソース予約一括追加.
    //     * <p>個々のオーダー毎に予約を作成する。
    //     *
    //     * @param orderList オーダーのリスト
    //     * @return reservation 予約のリスト
    //     */
    //    List<Reservation> addReservationList(List<OrderDto> orderList);

    /**
     * <p>VM関連コマンド一覧取得.
     * <p>VMに関するコマンドで未完了のものを取得する。
     *
     * @param vmId VMID
     * @return コマンドのリスト
     */
    List<Command> getCommandListByVmId(long vmId);

    /**
     * <p>テンプレート関連コマンド一覧取得.
     * <p>テンプレートに関するコマンドで未完了のものを取得する。
     *
     * @param templateId テンプレートID
     * @return コマンドのリスト
     */
    List<Command> getCommandListByTemplateId(long templateId);

    /**
     * <p>コマンド再実行.
     * <p>コマンドを再実行する。
     *
     * @param commandId コマンドID
     * @return コマンド
     */
    Command retryCommand(long commandId);

    /**
     * <p>コマンドキャンセル. <br>
     * <p>コマンドをキャンセルする。
     *
     * @param commandId コマンドID
     * @return コマンド
     */
    Command cancelCommand(long commandId);

    /**
     * <p>利用予測詳細取得.
     *
     * @param startTime 開始日時．1日単位で指定．例えば8/2～8/6の利用予測を取得したい時は8/2の午前0時を入れる．午前0時ちょうど以外の時刻を指定した場合の振る舞いは不定．
     * @param endTime 終了日時．1日単位で指定．例えば8/2～8/6の利用予測を取得したい時は8/7の午前0時を入れる．午前0時ちょうど以外の時刻を指定した場合の振る舞いは不定．
     * @return 利用予測
     */
    //    PredictionDto getPrediction(Date startTime, Date endTime);

    /**
     * <p>タスク概要一覧取得.
     *
     * @param
     * @return commandList コマンドのリスト
     */
    List<Command> getCommandAbstractionList();

    /**
     * <p>
     * VM起動可能期間一覧取得. <br>
     *
     * @param order
     * @return List<Term> 利用可能期間のリスト
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    List<Term> getTermListToReserve(OrderDto order);

    /**
     * <p>
     * テンプレート削除. <br>
     *
     * @param templateId テンプレートID
     * @return
     */
    void deleteTemplate(long templateId);

    /**
     * セキュリティグループを作成します.
     *
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>SecurityGroupを追加します。
     * </ul>
     * <p>
     * @return DB登録が完了したSecurityGroup
     */
    SecurityGroup createSecurityGroup();

    /**
     * セキュリティグループを削除します.
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>SecurityGroupを削除します。
     * <li>関連するNetworkAdapterが存在する場合は消去します。
     * </ul>
     * @param securityGroupId セキュリティグループのID
     */
    void deleteSecurityGroup(long securityGroupId);

    /**
     * IDで指定されたセキュリティグループを返します.
     * @param id セキュリティグループのID
     * @return セキュリティグループ
     */
    SecurityGroup getSecurityGroupById(long id);

    /**
     * ネットワークアダプターを作成します.
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterの作成
     * </ul>
     * @param vmId VmのID
     * @param securityGroupId セキュリティグループのID
     * @return ネットワークアダプターエンティティ
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    NetworkAdapter createNetworkAdapter(long vmId, long securityGroupId) throws CloudResourceException;

    /**
     * ネットワークアダプターを削除します.
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterの削除
     * </ul>
     * @param vmId VmのID
     * @param securityGroupId セキュリティグループのID
     */
    void deleteNetworkAdapter(long vmId, long securityGroupId);

    /**
     * セキュリティグループを作成します.
     *
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>SecurityGroupを追加します。
     * </ul>
     * @return DB登録が完了したSecurityGroup
     */
    SecurityGroupTemplate createSecurityGroupTemplate();

    /**
     * セキュリティグループを削除します.
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>SecurityGroupTemplateを削除します。
     * <li>関連するNetworkAdapterTemplateが存在する場合は消去します。
     * </ul>
     * @param securityGroupTemplateId セキュリティグループテンプレートのID
     */
    void deleteSecurityGroupTemplate(long securityGroupTemplateId);

    /**
     * IDに即したセキュリティグループのテンプレートを返します.
     * @param id セキュリティグループテンプレートのID
     * @return セキュリティグループテンプレート
     */
    SecurityGroupTemplate getSecurityGroupTemplateById(long id);

    /**
     * ネットワークアダプターのテンプレートを作成します.
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterTemplateの作成
     * </ul>
     * @param templateId TemplateのID
     * @param securityGroupTemplateId セキュリティグループテンプレートのID
     * @return ネットワークアダプターテンプレートエンティティ
     */
    NetworkAdapterTemplate createNetworkAdapterTemplate(long templateId, long securityGroupTemplateId);

    /**
     * ネットワークアダプターのテンプレートを削除します.
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCommandを利用しません。
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>NetworkAdapterTemplateの削除
     * </ul>
     * @param templateId TemplateのID
     * @param securityGroupTemplateId セキュリティグループテンプレートのID
     */
    void deleteNetworkAdapterTemplate(long templateId, Long securityGroupTemplateId);

    /**
     * @param orderDtoList
     * @return
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    List<Reservation> createReservationList(List<OrderDto> orderDtoList) throws CloudResourceException;

    /**
     * @param accountList
     * @return password
     */
    String changeUsersPassword(List<String> accountList);

    /**
     * コマンドを検索します.
     * @param keyword 検索キー nullは全対象 ""は対象なし
     * @param startDate 開始日 nullは全対象
     * @param endDate 終了日 nullは全対象
     * @param operations 検索対象の操作のリスト null/size=0は全対象
     * @param statuses 検索対象のコマンドの状態リスト null/size=0は全対象
     * @return コマンドリスト
     */
    List<Command> searchCommandList(String keyword, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses);

    /**
     * VMに関連のあるコマンドを検索します.
     * @param vmIds 検索対象の操作のリスト null/size=0は全対象
     * @param startDate 開始日 nullは全対象
     * @param endDate 終了日 nullは全対象
     * @param operations 検索対象の操作のリスト null/size=0は全対象
     * @param statuses 検索対象のコマンドの状態リスト null/size=0は全対象
     * @return コマンドリスト
     */
    List<Command> searchCommandListByVmIds(Collection<Long> vmIds, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses);

    /**
     * テンプレートに関連のあるコマンドを検索します.
     * @param templateIds 検索対象の操作のリスト null/size=0は全対象
     * @param startDate 開始日 nullは全対象
     * @param endDate 終了日 nullは全対象
     * @param operations 検索対象の操作のリスト null/size=0は全対象
     * @param statuses 検索対象のコマンドの状態リスト null/size=0は全対象
     * @return コマンドリスト
     */
    List<Command> searchCommandListByTemplateIds(Collection<Long> templateIds, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses);

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
