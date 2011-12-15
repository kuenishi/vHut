/*
 * Copyright 2011 NTT Software Corporation. All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import static jp.co.ntts.vhut.entity.Names.disk;
import static jp.co.ntts.vhut.entity.Names.networkAdapter;
import static jp.co.ntts.vhut.entity.Names.publicIpReservation;
import static jp.co.ntts.vhut.entity.Names.securityGroup;
import static jp.co.ntts.vhut.entity.Names.template;
import static jp.co.ntts.vhut.entity.Names.vlanReservation;
import static jp.co.ntts.vhut.entity.Names.vm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.dto.SpecDto;
import jp.co.ntts.vhut.entity.ClusterReservation;
import jp.co.ntts.vhut.entity.ClusterReservationVmMap;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandTemplateMap;
import jp.co.ntts.vhut.entity.CommandVmMap;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.DiskTemplate;
import jp.co.ntts.vhut.entity.ExternalIpRequestMode;
import jp.co.ntts.vhut.entity.IIdentifiableEntity;
import jp.co.ntts.vhut.entity.Names;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplate;
import jp.co.ntts.vhut.entity.PublicIpReservation;
import jp.co.ntts.vhut.entity.PublicIpReservationNames;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.ReservationNames;
import jp.co.ntts.vhut.entity.ResourceType;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.SecurityGroupNames;
import jp.co.ntts.vhut.entity.StorageReservation;
import jp.co.ntts.vhut.entity.StorageReservationDiskMap;
import jp.co.ntts.vhut.entity.StorageReservationDiskTemplateMap;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.TemplateNames;
import jp.co.ntts.vhut.entity.TemplateStatus;
import jp.co.ntts.vhut.entity.VlanReservation;
import jp.co.ntts.vhut.entity.VlanReservationNames;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.entity.VmCloudUserMap;
import jp.co.ntts.vhut.entity.VmNames;
import jp.co.ntts.vhut.entity.VmStatus;
import jp.co.ntts.vhut.exception.CloudReservationException;
import jp.co.ntts.vhut.exception.CloudReservationPeriodException;
import jp.co.ntts.vhut.exception.CloudResourceException;
import jp.co.ntts.vhut.exception.DBStillReferencedRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.exception.InternalRuntimeException;
import jp.co.ntts.vhut.util.EntityUtil;
import jp.co.ntts.vhut.util.Ipv4ConversionUtil;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutUtil;

import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * Reservationを管理します.
 * <p>
 * 主な機能は、下記のようなものです。
 * <ul>
 * <li>VMの作成・削除・更新
 * <li>テンプレートの作成・削除
 * <li>Networkの取得・解放
 * </ul>
 * その中で以下のような処理を行います。
 * <ul>
 * <li>Reservationの確認
 * <li>Reservationと各要素の対応づけ（占有）
 * <li>Reservationと各要素の対応解除（開放）
 * </ul>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
class ReservationContent {

    private static final String DESCRITION_REGEX = "^\\w*$";

    private static final int DESCRITION_LENGTH = 64;

    private final PrivateCloudLogic parent;


    /**
     * コンストラクタ.
     * @param parent 上位のオブジェクト
     */
    ReservationContent(final PrivateCloudLogic parent) {
        this.parent = parent;
    }

    /**
     * VMにユーザを追加します.
     *
     * @param reservationId 使用する予約のID
     * @param vmId VMのID
     * @param cloudUserId ユーザID
     * @return VM
     * @throws CloudReservationPeriodException
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    Vm addVmUser(long reservationId, long vmId, long cloudUserId) throws CloudReservationPeriodException, CloudReservationException {
        Vm result = null;

        // 指定された既存予約を取得
        ReservationAdapter rsvadp = this.parent.getReservationInAdapterById(reservationId, ReservationNames.clusterReservationList());

        // このメソッドの呼び出しが予約期間内であるかどうかを判定
        if (!this.isCalledInReservationPeriod(rsvadp)) {
            throw new CloudReservationPeriodException(reservationId);
        }

        // 指定されたVMを取得
        result = this.parent.getVmById(vmId,
        // VmNames.clusterReservationVmMapList(),
                VmNames.vmCloudUserMapList(), VmNames.commandVmMapList());

        if (result.vmCloudUserMapList.contains(cloudUserId)) {
            throw new InputRuntimeException("cloudUserId", "User already exists.");
        }

        if (result.vmCloudUserMapList.size() <= 0) {
            // 充分なリソースの空きが，予約にあるかどうかを確認
            if (!rsvadp.checkClusterResource(result)) {
                throw new CloudReservationException(reservationId, ResourceType.CLUSTER);
            }
            // VmとClusterReservationの間に関連を作って，予約のリソースを消費
            List<ClusterReservationVmMap> crvmaplist = new ArrayList<ClusterReservationVmMap>();
            for (ClusterReservation cr : rsvadp.clusterReservationList) {
                ClusterReservationVmMap crvmap = new ClusterReservationVmMap();
                crvmap.clusterReservationId = cr.id;
                crvmap.vmId = result.id;
                crvmaplist.add(crvmap);
            }
            this.parent.jdbcManager.insertBatch(crvmaplist)
                .execute();
        }

        // VmCloudUserMap表に新規行を追加
        VmCloudUserMap vumap = new VmCloudUserMap();
        vumap.vmId = vmId;
        vumap.cloudUserId = cloudUserId;
        this.parent.jdbcManager.insert(vumap)
            .execute();

        result.vmCloudUserMapList.add(vumap);

        // ユーザー追加コマンドの発行
        this.parent.issueCommand(parent.getLastVmCommandId(result.id), CommandOperation.ADD_USER, result.id, cloudUserId);

        //LastCommand取得に関するロック
        parent.jdbcManager.update(result)
            .includes(vm().id())
            .execute();

        return result;
    }

    /**
     * 予約の期間内であるか判定します.
     * @param rsvadp
     * @return 期間内:true 期間外:false
     */
    private boolean isCalledInReservationPeriod(ReservationAdapter reservationAdapter) {
        Date begin = reservationAdapter.getBegin();
        Date end = reservationAdapter.getEnd();
        return this.isCalledInReservationPeriod(begin, end);
    }

    /**
     * 現在が期間内であるか判定します.
     * @param begin 開始日時
     * @param end 終了日時
     * @return 期間内:true 期間外:false
     */
    private boolean isCalledInReservationPeriod(Date begin, Date end) {
        Date current = TimestampUtil.getCurrentDate();

        if (begin != null) {
            return !current.before(TimestampUtil.floorAsDate(new Timestamp(begin.getTime())));
        }
        if (end != null) {
            return !current.after(TimestampUtil.ceilAsDate(new Timestamp(end.getTime())));
        }

        return true;
    }

    /**
     * VMからテンプレートを作成します.
     * @param reservationId 使用する予約のID
     * @param vmId VMのID
     * @param servicePrefix テンプレートの名前
     * @param description テンプレートの概要
     * @return テンプレート
     * @throws CloudReservationPeriodException
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    protected Template createTemplate(long reservationId, long vmId, String servicePrefix, String description) throws CloudReservationPeriodException, CloudReservationException {
        Template result = null;

        // 指定された既存予約を取得
        ReservationAdapter rsvadp = this.parent.getReservationInAdapterById(reservationId, ReservationNames.storageReservationList());

        // このメソッドの呼び出しが予約期間内であるかどうかを判定
        if (!this.isCalledInReservationPeriod(rsvadp)) {
            throw new CloudReservationPeriodException(reservationId);
        }

        // 指定されたVMを取得
        Vm vm = this.parent.getVmById(vmId, VmNames.diskList(), VmNames.networkAdapterList());

        // テンプレートを追加するのに充分なリソースの空きが，予約にあるかどうかを確認
        if (!rsvadp.checkStorageResource(vm)) {
            throw new CloudReservationException(reservationId, ResourceType.STORAGE);
        }

        result = new Template();
        result.description = validateDescription(description);
        result.cloudId = this.parent.getCloudId();
        result.clusterId = vm.clusterId;
        result.cpuCore = vm.cpuCore;
        result.memory = vm.memory;
        result.os = vm.os;
        result.status = TemplateStatus.DEVELOPPING;
        result.storageId = vm.storageId;
        result.specId = vm.specId;

        // 本体を挿入
        parent.jdbcManager.insert(result)
            .execute();
        // 振られたIDからVM名称を生成
        result.name = VhutUtil.createTemplateName(servicePrefix, result.id);
        // 更新
        parent.jdbcManager.update(result)
            .includes(template().name())
            .execute();
        //ディスクテンプレートのTemplateIdを更新
        result.diskTemplateList = new ArrayList<DiskTemplate>(vm.diskList.size());
        for (Disk disk : vm.diskList) {
            //                if (disk.isAdditionalDisk()) {
            DiskTemplate disktmpl = DiskTemplate.newInstance(disk);
            disktmpl.templateId = result.id;
            disktmpl.storageId = disk.storageId;
            result.diskTemplateList.add(disktmpl);
            //                }
        }
        // ディスクテンプレートをDBに格納
        if (result.diskTemplateList.size() > 0) {
            parent.jdbcManager.insertBatch(result.diskTemplateList)
                .execute();
        }
        /*
         * NetworkAdapterTemplateは別途追加する 
         */
        result.networkAdapterTemplateList = new ArrayList<NetworkAdapterTemplate>();

        // StorageReservationの消費
        Map<Long, StorageReservation> srmap = new HashMap<Long, StorageReservation>();
        for (StorageReservation sr : rsvadp.storageReservationList) {
            srmap.put(sr.storageId, sr);
        }
        List<StorageReservationDiskTemplateMap> srdtmlist = new ArrayList<StorageReservationDiskTemplateMap>();
        for (DiskTemplate dktmpl : result.diskTemplateList) {
            StorageReservationDiskTemplateMap srdtmap = new StorageReservationDiskTemplateMap();
            srdtmap.diskTemplateId = dktmpl.id;
            srdtmap.storageReservationId = srmap.get(dktmpl.storageId).id;
            srdtmlist.add(srdtmap);
        }

        if (srdtmlist.size() > 0) {
            this.parent.jdbcManager.insertBatch(srdtmlist)
                .execute();
        }

        // コマンド発行
        Command cmd = this.parent.issueCommand(parent.getLastTemplateCommandId(result.id), CommandOperation.CREATE_TEMPLATE_ASYNC, result, vmId);

        // CommandTemplateMapの追加
        CommandTemplateMap ctmap = new CommandTemplateMap();
        ctmap.templateId = result.id;
        ctmap.commandId = cmd.id;
        this.parent.jdbcManager.insert(ctmap)
            .execute();

        parent.jdbcManager.update(result)
            .includes(template().id())
            .execute();

        return result;
    }

    /**
     * テンプレートからVMを作成します.
     *
     * readmIdを外した便利メソッドです.
     *
     * @param reservationId 使用する予約のID
     * @param templateId テンプレートのID
     * @param specId VMのスペックのID
     * @param servicePrefix VMの名前
     * @param description VMの概要
     * @return VM
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    protected Vm createVm(long reservationId, long templateId, long specId, String servicePrefix, String description) throws CloudReservationException {
        return this.createVm(reservationId, templateId, specId, servicePrefix, description, -1);
    }

    /**
     * テンプレートからVMを作成します.
     *
     * @param reservationId 使用する予約のID
     * @param templateId テンプレートのID
     * @param specId VMのスペックのID
     * @param servicePrefix VMの名前
     * @param description VMの概要
     * @param realmId 地域ID
     * @return VM
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    // NEXT: 動作から考えるとメソッド名はaddVmToReservationが正しい
    // NEXT: realmIdの扱いが不明 現在は無視
    protected Vm createVm(long reservationId, long templateId, long specId, String servicePrefix, String description, long realmId) throws CloudReservationException {
        Vm result = null;

        // 指定されたテンプレートをDBから取得
        Template tmpl = this.parent.getTemplateById(templateId, TemplateNames.diskTemplateList());

        // 指定されたスペックをコンフィグから取得
        List<SpecDto> speclist = this.parent.cloudConfig.getSpecList();
        SpecDto spec = null;
        for (SpecDto tmpspec : speclist) {
            if (tmpspec.id == specId) {
                spec = tmpspec;
                break;
            }
        }
        if (spec == null) {
            throw new InputRuntimeException("specId", String.format("spec(id=%d) is not exist", specId));
        }

        // 指定されたReservationをDBから取得
        ReservationAdapter rsvadp = this.parent.getReservationInAdapterById(reservationId, ReservationNames.storageReservationList());

        // このメソッドの呼び出しが予約期間内であるかどうかを判定
        if (!this.isCalledInReservationPeriod(rsvadp)) {
            throw new CloudReservationException(reservationId, ResourceType.STORAGE);
        }

        // 追加するVm(の元になるTemplate)が消費するストレージ・サイズの取得
        // key:ストレージID, val:ストレージ・サイズ
        Map<Long, Integer> srmap = tmpl.getStorageResource();
        Map<Long, Integer> srmaxmap = rsvadp.getStorageMax();
        Map<Long, Integer> srusedmap = rsvadp.getStorageUsed();
        if (srmap == null) {
            // 元になるテンプレートにDiskTemplateが付いていないケース
            // FIXED: ストレージ消費なしとして許容 
        } else {
            // 指定されたReservationの，対象となるStorageReservationに，
            // 追加するVm分の空きがあるかどうかのチェック
            for (Long id : srmap.keySet()) {
                try {
                    if (srmap.get(id) > (srmaxmap.get(id) - srusedmap.get(id))) {
                        throw new CloudReservationException(reservationId, ResourceType.STORAGE);
                    }
                } catch (NullPointerException exnp) {
                    throw new CloudReservationException(reservationId, ResourceType.STORAGE);
                }
            }
        }

        result = new Vm();
        // VmテーブルのVm情報を設定
        // 引数から
        result.description = validateDescription(description);
        result.os = tmpl.os;
        result.templateId = tmpl.id;
        result.clusterId = tmpl.clusterId;
        result.storageId = tmpl.storageId;
        // Spec情報から
        result.specId = spec.id;
        result.cpuCore = spec.cpuCore;
        result.memory = spec.memory;
        // その他から
        result.cloudId = this.parent.getCloudId();
        result.status = VmStatus.DEVELOPPING;

        result.networkAdapterList = new ArrayList<NetworkAdapter>();
        //VMを取りあえず挿入（IDを得るため）
        parent.jdbcManager.insert(result)
            .execute();
        //名称をPrefixとid列から生成
        result.name = VhutUtil.createVmName(servicePrefix, result.id);
        //更新
        parent.jdbcManager.update(result)
            .includes(vm().name())
            .execute();

        // Vmテーブル外のVM情報を設定
        // Template#diskTemplateListからVm#diskListを設定
        result.diskList = new ArrayList<Disk>(tmpl.diskTemplateList.size());
        for (DiskTemplate disktmpl : tmpl.diskTemplateList) {
            // DiskテーブルのDisk情報を設定
            Disk disk = Disk.newInstance(disktmpl);
            disk.vmId = result.id;
            // Vm#diskListにDiskを追加
            result.diskList.add(disk);
        }
        //ディスクを追加
        if (result.diskList.size() > 0) {
            parent.jdbcManager.insertBatch(result.diskList)
                .execute();
        }

        // StorageReservationの消費
        Map<Long, StorageReservation> ssrmap = new HashMap<Long, StorageReservation>();
        for (StorageReservation sr : rsvadp.storageReservationList) {
            ssrmap.put(sr.storageId, sr);
        }
        List<StorageReservationDiskMap> srdlist = new ArrayList<StorageReservationDiskMap>();
        for (Disk disk : result.diskList) {
            StorageReservationDiskMap srdmap = new StorageReservationDiskMap();
            srdmap.diskId = disk.id;
            srdmap.storageReservationId = ssrmap.get(disk.storageId).id;
            srdlist.add(srdmap);
        }

        if (srdlist.size() > 0) {
            this.parent.jdbcManager.insertBatch(srdlist)
                .execute();
        }

        //コマンドの発行
        Command cmd = this.parent.issueCommand(parent.getLastVmCommandId(result.id), CommandOperation.CREATE_VM_ASYNC, result);

        // CommandVmMapの追加
        this.parent.insertCommandVmMap(result.id, cmd.id);

        //LastCommand取得に関するロック
        parent.jdbcManager.update(result)
            .includes(vm().id())
            .execute();

        return result;
    }

    /**
     * 正規表現によるバリデーションと文字長制限を超える場合は切り捨てを行います.
     * @param description
     * @return
     */
    private String validateDescription(String description) {
        // 正規表現によるバリデーション
        if (!description.matches(DESCRITION_REGEX)) {
            throw new InputRuntimeException("description", String.format("[%s] doesn't match [%s]", description, DESCRITION_REGEX));
        }
        // 最大長を超える場合は切り捨て
        if (description.length() > DESCRITION_LENGTH) {
            return description.substring(0, Math.min(description.length(), DESCRITION_LENGTH));
        }
        return description;
    }

    /**
     * テンプレートを削除する.
     * テーブルの削除はコマンド実行完了後、ADeleteTemplateCommand#finish()の中で実行されます。
     *
     * @param templateId テンプレートのID
     *
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#getAbstractPerformance()
     * [CSI] 1.21. テンプレート削除<br>
     * [VSQ] 21. リリースドアプリケーションの削除機能
     */
    void deleteTemplate(long templateId) {
        Template localTemplate = this.parent.jdbcManager.from(Template.class)
            .id(templateId)
            .getSingleResult();
        if (localTemplate == null) {
            throw new InputRuntimeException("templateId", String.format("Template(id=%d) is not exist.", templateId));
        }
        // コマンド発行
        Command cmd = this.parent.issueCommand(parent.getLastTemplateCommandId(templateId), CommandOperation.DELETE_TEMPLATE_ASYNC, templateId);
        this.parent.insertCommandTemplateMap(templateId, cmd.id);

        parent.jdbcManager.update(localTemplate)
            .includes(template().id())
            .execute();

    }

    /**
     * Vmを削除します.
     * テーブルの削除はコマンド実行完了後、ADeleteTemplateCommand#finish()の中で実行されます。
     *
     * @param vmId VmのID
     *
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)
     * [CSI] 1.11.VM削除<br>
     * [VSQ] p.11,19,20,51<br>
     * [VSQ]の「VM削除コマンドがなければ?」は重複発行を避ける意図だったが，考慮不要<br>
     * shallow delete として実装<br>
     * 注) ディスク,ネットワークの削除コマンドは発行しない
     */
    void deleteVm(long vmId) {
        Vm localVm = parent.jdbcManager.from(Vm.class)
            .id(vmId)
            .getSingleResult();

        if (localVm == null) {
            throw new InputRuntimeException("vmId", String.format("Vm(id=%d) is not exist.", vmId));
        }

        // VM削除コマンドの発行
        Command cmd = this.parent.issueCommand(parent.getLastVmCommandId(vmId), CommandOperation.DELETE_VM_ASYNC, vmId);
        // CommandVmMapテーブルの先のVM削除コマンドを挿入する
        this.parent.insertCommandVmMap(vmId, cmd.id);

        //LastCommand取得に関するロック
        parent.jdbcManager.update(localVm)
            .includes(vm().id())
            .execute();
    }

    /**
     * ネットワーク占有する.
     * <p>下記のように動作します.
     * 1. securityGroupを取得する。<br>
     * 2. リソース判定 指定された予約で追加のネットワークを使用可能か？<br>
     * 3. 空きNetworkをセレクト<br>
     * 4. VlanReservationをSecurityGroupにリンクさせ更新<br>
     * 5. SecurityGroup.networkAdapterListのすべての要素についてSAddIpCommnadとSAddNatCommandを発行<br>
     * 5. 予約したNetworkを返す<br>
     *
     * @param reservationId 使用する予約ID
     * @param securityGroupId ネットワークと結び付けるセキュリティグループ
     * @param exIpRequestMode 外部IPの申請モード
     * @return ネットワーク
     * @throws CloudReservationPeriodException
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    Network obtainNetwork(long reservationId, long securityGroupId, ExternalIpRequestMode exIpRequestMode) throws CloudReservationPeriodException, CloudReservationException {
        Network network = null;
        Reservation reservation = parent.jdbcManager.from(Reservation.class)
            .leftOuterJoin(Names.reservation()
                .vlanReservationList())
            .leftOuterJoin(Names.reservation()
                .vlanReservationList()
                .network())
            .leftOuterJoin(Names.reservation()
                .publicIpReservationList())
            .id(reservationId)
            .getSingleResult();

        if (reservation == null) {
            throw new InputRuntimeException("reservationId", String.format("Reservation(id=%d) is not exist.", reservationId));
        }

        // このメソッドの呼び出しが予約期間内であるかどうかを判定
        if (!isCalledInReservationPeriod(reservation.startTime, reservation.endTime)) {
            throw new CloudReservationPeriodException(reservationId);
        }

        // 指定されたSecurityGroupをDBから取得
        SecurityGroup sg = this.parent.jdbcManager.from(SecurityGroup.class)
            .id(securityGroupId)
            .leftOuterJoin(SecurityGroupNames.network())
            .leftOuterJoin(SecurityGroupNames.networkAdapterList())
            .getSingleResult();
        if (sg == null) {
            throw new InputRuntimeException("securityGroupId", String.format("Security Group(id=%d) is not exist", securityGroupId));
        }

        if (sg.networkAdapterList.size() > 0) {

            //VLANの予約リストにセキュリティグループIDを記す
            VlanReservation vlanReservation = null;

            for (VlanReservation target : reservation.vlanReservationList) {
                if (target.securityGroupId == null) {
                    vlanReservation = target;
                }
            }

            if (vlanReservation == null) {
                throw new CloudReservationException(reservationId, ResourceType.VLAN);
            }
            network = vlanReservation.network;
            vlanReservation.securityGroupId = sg.id;
            sg.networkId = network.id;
            this.parent.jdbcManager.update(vlanReservation)
                .includes(vlanReservation().securityGroupId())
                .execute();
            this.parent.jdbcManager.update(sg)
                .includes(securityGroup().networkId())
                .execute();

            List<PublicIpReservation> publicIpReservationList = new ArrayList<PublicIpReservation>();

            for (PublicIpReservation target : reservation.publicIpReservationList) {
                if (target.networkAdapterId == null) {
                    publicIpReservationList.add(target);
                }
            }

            //NetworkAdapterのPublic IPをクリーニング
            for (NetworkAdapter networkAdapter : sg.networkAdapterList) {
                networkAdapter.publicIp = null;
            }

            switch (exIpRequestMode) {
                case AUTO:
                    if (publicIpReservationList.size() < sg.networkAdapterList.size()) {
                        throw new CloudReservationException(reservationId, ResourceType.PUBLIC_IP);
                    }
                    //PUBLICIPの予約リストにネットワークアダプターIDを記す
                    for (int i = 0; i < sg.networkAdapterList.size(); i++) {
                        PublicIpReservation publicIpReservation = publicIpReservationList.get(i);
                        NetworkAdapter networkAdapter = sg.networkAdapterList.get(i);
                        networkAdapter.publicIp = publicIpReservation.publicIp;
                        publicIpReservation.networkAdapterId = networkAdapter.id;
                    }
                    break;

                case PERVM:
                    Map<Long, NetworkAdapter> vmIdNwaMap = new HashMap<Long, NetworkAdapter>();
                    for (NetworkAdapter nwa : sg.networkAdapterList) {
                        vmIdNwaMap.put(nwa.vmId, nwa);
                    }
                    if (publicIpReservationList.size() < vmIdNwaMap.keySet()
                        .size()) {
                        throw new CloudReservationException(reservationId, ResourceType.PUBLIC_IP);
                    }
                    Long[] keys = vmIdNwaMap.keySet()
                        .toArray(new Long[0]);
                    //PUBLICIPの予約リストにネットワークアダプターIDを記す
                    for (int i = 0; i < keys.length; i++) {
                        PublicIpReservation publicIpReservation = publicIpReservationList.get(i);
                        NetworkAdapter networkAdapter = vmIdNwaMap.get(keys[i]);
                        networkAdapter.publicIp = publicIpReservation.publicIp;
                        publicIpReservation.networkAdapterId = networkAdapter.id;
                    }
                    break;

                default:
                    break;
            }

            assignPrivateIpAddress(sg.networkAdapterList, network);

            if (publicIpReservationList.size() > 0) {
                this.parent.jdbcManager.updateBatch(publicIpReservationList)
                    .includes(publicIpReservation().networkAdapterId())
                    .execute();
            }

            this.parent.jdbcManager.updateBatch(sg.networkAdapterList)
                .includes(networkAdapter().privateIp(), networkAdapter().publicIp())
                .execute();

            List<CommandVmMap> commadVmMapList = new ArrayList<CommandVmMap>();

            for (NetworkAdapter nwa : sg.networkAdapterList) {
                Command cmd;
                //ロック開始
                Vm localVm = parent.jdbcManager.from(Vm.class)
                    .id(nwa.vmId)
                    .getSingleResult();

                cmd = this.parent.issueCommand(parent.getLastVmCommandId(localVm.id), CommandOperation.ADD_IP, nwa.getPrivateIpWithDotFormat(), nwa.getMacWithColonFormat());
                commadVmMapList.add(CommandVmMap.newCommandVmMap(cmd.id, localVm.id));

                if (nwa.publicIp != null) {
                    cmd = this.parent.issueCommand(cmd.id, CommandOperation.ADD_NAT, nwa.getPrivateIpWithDotFormat(), nwa.getPublicIpWithDotFormat());
                    commadVmMapList.add(CommandVmMap.newCommandVmMap(cmd.id, localVm.id));
                }

                cmd = this.parent.issueCommand(cmd.id, CommandOperation.ADD_NETWORK_ADAPTER, nwa);
                commadVmMapList.add(CommandVmMap.newCommandVmMap(cmd.id, localVm.id));

                //ロック確認
                parent.jdbcManager.update(localVm)
                    .includes(vm().id())
                    .execute();
            }

            this.parent.jdbcManager.insertBatch(commadVmMapList)
                .execute();

        }

        return network;
    }

    /**
     * @param networkAdapterList
     * @param ntwork
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    private void assignPrivateIpAddress(List<NetworkAdapter> networkAdapterList, Network network) throws CloudReservationException {
        //割り当て可能なIPの個数（0番目：ネットワークアドレス、1番目：ゲートウェイ、最後：ブロードキャスト）
        int length = Ipv4ConversionUtil.getHostAddressCount(network.mask);
        //使用中アドレスリストの作成
        boolean[] map = new boolean[length];
        //抜けの検索とIPアドレスの割り当て
        int i = 0;
        //禁則IPオーダの付与
        map[0] = true; //network address
        if (network.gateway != null) {
            map[Ipv4ConversionUtil.getHostAddressOrder(network.gateway, network.mask)] = true; //gateway
        }
        if (network.dhcp != null) {
            map[Ipv4ConversionUtil.getHostAddressOrder(network.dhcp, network.mask)] = true; //dhcp
        }
        if (parent.cloudConfig.getInIpMaskOrders() != null) {
            for (Integer order : parent.cloudConfig.getInIpMaskOrders()) {
                map[order] = true; //mask
            }
        }
        map[length - 1] = true; //broad cast
        //IPのアサイン
        for (NetworkAdapter nwa : networkAdapterList) {
            //前回同じネットワークでIPが振られている時なるべく同じIPを割り当てる
            if (nwa.privateIp != null && Ipv4ConversionUtil.isScorp(nwa.privateIp, network.networkAddress, network.mask)) {
                int count = Ipv4ConversionUtil.getHostAddressOrder(nwa.privateIp, network.mask);
                if (!map[count]) {
                    map[count] = true;
                    break;
                }
            }
            //前回と同じIPが割り振れなかった時、あいているところを探す
            nwa.privateIp = null;
            for (; i < map.length - 1; i++) {
                if (!map[i]) {
                    byte[] ippaddr = Ipv4ConversionUtil.getIpAddressWithOrderAsByte(network.networkAddress, network.mask, i);
                    nwa.privateIp = Ipv4ConversionUtil.byteToAddr(ippaddr);
                    map[i] = true;
                    break;
                }
            }
            if (nwa.privateIp == null) {
                throw new CloudReservationException(parent.getCloudId(), ResourceType.PRIVATE_IP);
            }
        }
    }

    /**
     * ネットワーク開放します.
     *
     * <p>下記のように動作します.<br>
     * 1. SecurityGroupおよび関連するNetwork、NetworkAdapterを取得する。<br>
     * 2. SecurityGroup.networkAdapterListのすべての要素についてSRemoveIpCommnadとSRemoveNatCommandを発行<br>
     * 3. NetworkとSecurityGroupのリンクを切って更新<br>
     *
     * @param securityGroupId セキュリティグループのID
     */
    protected void releaseNetwork(long securityGroupId) {
        SecurityGroup sg = this.parent.jdbcManager.from(SecurityGroup.class)
            .id(securityGroupId)
            .leftOuterJoin(SecurityGroupNames.vlanReservationList())
            .leftOuterJoin(SecurityGroupNames.network())
            .leftOuterJoin(SecurityGroupNames.networkAdapterList())
            .getSingleResult();
        if (sg == null) {
            throw new InputRuntimeException("securityGroupId", String.format("Security Group(id=%d) is not exist", securityGroupId));
        }

        if (sg.vlanReservationList.size() > 0) {
            // VlanReservation表を更新
            for (VlanReservation vr : sg.vlanReservationList) {
                vr.securityGroupId = null;
            }
            this.parent.jdbcManager.updateBatch(sg.vlanReservationList)
                .includes(VlanReservationNames.securityGroupId())
                .execute();
        }

        // PublicIpReservation表を更新
        List<PublicIpReservation> pirlist = new ArrayList<PublicIpReservation>();
        for (NetworkAdapter nwa : sg.networkAdapterList) {
            List<PublicIpReservation> tpirlist = this.parent.jdbcManager.from(PublicIpReservation.class)
                .where((new SimpleWhere()).eq(PublicIpReservationNames.networkAdapterId(), nwa.id))
                .getResultList();
            for (PublicIpReservation pir : tpirlist) {
                pir.networkAdapterId = null;
            }
            pirlist.addAll(tpirlist);
        }
        if (pirlist.size() > 0) {
            this.parent.jdbcManager.updateBatch(pirlist)
                .execute();
        }

        //            for (NetworkAdapter nwa : sg.networkAdapterList) {
        //                nwa.mac = null;
        //                nwa.publicIp = null;
        //                nwa.privateIp = null;
        //            }
        //            this.parent.jdbcManager.updateBatch(sg.networkAdapterList)
        //                .includes(networkAdapter().mac()
        //                        , networkAdapter().privateIp()
        //                        , networkAdapter().publicIp())
        //                .execute();

        // SecurityGroupのNetworkIdをNULLにする。
        sg.networkId = null;
        this.parent.jdbcManager.update(sg)
            .includes(securityGroup().networkId())
            .execute();

        List<CommandVmMap> commadVmMapList = new ArrayList<CommandVmMap>();

        // Commandの発行
        for (NetworkAdapter nwa : sg.networkAdapterList) {
            Command cmd;
            //ロック開始
            Vm localVm = parent.jdbcManager.from(Vm.class)
                .id(nwa.vmId)
                .getSingleResult();

            cmd = this.parent.issueCommand(parent.getLastVmCommandId(localVm.id), CommandOperation.REMOVE_NETWORK_ADAPTER, nwa.vmId, nwa.id);
            commadVmMapList.add(CommandVmMap.newCommandVmMap(cmd.id, localVm.id));

            cmd = this.parent.issueCommand(cmd.id, CommandOperation.REMOVE_IP, nwa.getPrivateIpWithDotFormat(), nwa.getMacWithColonFormat());
            commadVmMapList.add(CommandVmMap.newCommandVmMap(cmd.id, localVm.id));

            if (nwa.publicIp != null) {
                cmd = this.parent.issueCommand(cmd.id, CommandOperation.REMOVE_NAT, nwa.getPrivateIpWithDotFormat(), nwa.getPublicIpWithDotFormat());
                commadVmMapList.add(CommandVmMap.newCommandVmMap(cmd.id, localVm.id));
            }

            //ロック確認
            parent.jdbcManager.update(localVm)
                .includes(vm().id())
                .execute();
        }

        this.parent.jdbcManager.insertBatch(commadVmMapList)
            .execute();
    }

    /**
     * VMからユーザを削除する.
     * <p>
     * [CSI] 1.18.VM ユーザ削除 <br>
     * [VSQ] 49. サービスの定期更新機能 <br>
     * 関連テーブルの削除はコマンド実行成功後になります.
     *
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#removeVmUser(long, long)
     *
     * @param vmId VMのID
     * @param cloudUserId ユーザのID
     * @return VM
     */
    Vm removeVmUser(long vmId, long cloudUserId) {
        Vm result = null;
        // 指定されたVMを取得
        result = this.parent.getVmById(vmId, VmNames.clusterReservationVmMapList(), VmNames.vmCloudUserMapList(), VmNames.commandVmMapList());

        boolean hasMap = false;

        for (VmCloudUserMap map : result.vmCloudUserMapList) {
            if (map.cloudUserId == cloudUserId) {
                hasMap = true;
                break;
            }
        }

        if (!hasMap) {
            throw new InputRuntimeException("vmId, cloudUserId", String.format("User(id=%d) is not assigned to Vm(id=%d)", cloudUserId, vmId));
        }

        // ユーザー削除コマンドの発行
        Command cmd = this.parent.issueCommand(parent.getLastVmCommandId(result.id), CommandOperation.REMOVE_USER, result.id, cloudUserId);

        this.parent.insertCommandVmMap(vmId, cmd.id);

        //LastCommand取得に関するロック
        parent.jdbcManager.update(result)
            .includes(vm().id())
            .execute();

        return result;
    }

    /*
     * reservationIdが付いているが，影響を受けるのが該予約だけではない可能性がある. 
     * NEXT: 現状ではvmに2つ以上のReservationが関係していたら例外 
     * このメソッドはVMが起動不可能状態で実行されることを前提として実装します。
     * NEXT: Applicationに存在する状態管理（Active, Deactive）をVMレベルでも実装する必要あり。
     */
    /**
     * VMの構成を更新する.
     *
     * <p> 更新の対象は以下の項目のみです.
     * <ul>
     * <li>VMのスペック
     * <li>ディスクの追加,削除
     * </ul>
     *
     * [CSI] 1.10.VM更新<br>
     * [VSQ]? 17. アプリケーションの編集機能(VM追加更新), 49. サービスの定期更新機能(VM追加更新)<br>
     *
     * <p>以下の手順で処理を実行します。
     *
     * Phase1: 旧VMの取得（取得できない場合は例外）<br>
     * Phase2: 起動可能状態(関連ユーザがある場合)を判定して、（起動可能状態の際は例外。）<br>
     * Phase7: 既存予約からリソースを開放し、新規予約からリソースを取得し、更新。（取得できない場合は例外。）<br>
     * Phase3: VMテーブルの更新<br>
     * Phase4: スペック関連のコマンドを発行<br>
     * Phase5: ディスクの分類（削除、追加）<br>
     * Phase6: ディスクテーブルを更新し、ディスク関連のコマンドを発行<br>
     * Phase8: リソースの再取得 StorageReservationの消費<br>
     *
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#updateVm(long, jp.co.ntts.vhut.entity.Vm)
     *
     * @param reservationId 予約ID
     * @param remoteVm 変更内容が記されたVM
     * @return 変更後のVM
     * @throws CloudReservationException 対象予約のリソース不足による例外
     */
    protected Vm updateVm(long reservationId, Vm remoteVm) throws CloudReservationException {
        //コマンドを関連させるために共通変数化
        Command cmd = null;

        //Phase1: 旧VMの取得（取得できない場合は例外）
        Vm localVm = this.parent.jdbcManager.from(Vm.class)
            .leftOuterJoin(vm().diskList())
            .leftOuterJoin(vm().diskList()
                .storageReservationDiskMapList())
            .leftOuterJoin(vm().diskList()
                .storageReservationDiskMapList()
                .storageReservation())
            .id(remoteVm.id)
            .getSingleResult();
        if (localVm == null) {
            throw new InputRuntimeException("remoteVm", String.format("Vm(id=%d) is not exist.", remoteVm.id));
        }

        //Phase2: 起動可能状態(関連ユーザがある場合)を判定して、（起動可能状態の際は例外。）
        if (localVm.vmCloudUserMapList != null && localVm.vmCloudUserMapList.size() > 0) {
            throw new DBStillReferencedRuntimeException(localVm, localVm.vmCloudUserMapList.toArray(new IIdentifiableEntity[0]));
        }

        //Phase7: 既存予約からリソースを開放し、新規予約からリソースを取得し、更新。（取得できない場合は例外。）
        //既存の開放
        List<StorageReservationDiskMap> storageReservationDiskMapList = new ArrayList<StorageReservationDiskMap>();
        for (Disk disk : localVm.diskList) {
            if (disk.storageReservationDiskMapList != null) {
                storageReservationDiskMapList.addAll(disk.storageReservationDiskMapList);
            }
        }
        if (storageReservationDiskMapList.size() != 0) {
            this.parent.jdbcManager.deleteBatch(storageReservationDiskMapList)
                .execute();
        }
        //新規の取得
        ReservationAdapter rsvadp = this.parent.getReservationInAdapterById(reservationId, ReservationNames.storageReservationList());

        //PhaseX: リソースマッピング
        rsvadp.autoAssignStorageId(remoteVm.diskList);

        //PhaseX: このメソッドの呼び出しが予約期間内であるかどうかを判定
        if (!this.isCalledInReservationPeriod(rsvadp)) {
            throw new CloudReservationException(reservationId, ResourceType.STORAGE);
        }
        if (!rsvadp.checkStorageResource(remoteVm)) {
            throw new CloudReservationException(reservationId, ResourceType.STORAGE);
        }

        //Phase3: VMテーブルの更新
        boolean toBeUpdated = false;
        //cpuの確認
        if (remoteVm.specId != localVm.specId) {
            List<SpecDto> specDtoList = this.parent.cloudConfig.getSpecList();
            for (SpecDto specDto : specDtoList) {
                if (specDto.id == remoteVm.specId) {
                    if (localVm.cpuCore != specDto.cpuCore) {
                        localVm.cpuCore = specDto.cpuCore;
                        toBeUpdated = true;
                    }
                    if (localVm.memory != specDto.memory) {
                        localVm.memory = specDto.memory;
                        toBeUpdated = true;
                    }
                    break;
                }
            }
        }

        cmd = parent.getLastVmCommand(localVm.id);

        //Phase4: スペック関連のコマンドを発行
        if (toBeUpdated) {
            this.parent.jdbcManager.update(localVm)
                .includes(vm().cpuCore(), vm().memory(), vm().specId())
                .execute();
            if (cmd != null) {
                cmd = this.parent.issueCommand(cmd.id, CommandOperation.CHANGE_SPEC, localVm.id, localVm.cpuCore, localVm.memory);
            } else {
                cmd = this.parent.issueCommand(CommandOperation.CHANGE_SPEC, localVm.id, localVm.cpuCore, localVm.memory);
            }
            this.parent.insertCommandVmMap(localVm.id, cmd.id);
        }

        //Phase5: ディスクの分類（削除、追加）
        EntityUtil.SortOutResult sortOutResult = EntityUtil.sortOutToSync(localVm.diskList.toArray(new Disk[0]), remoteVm.diskList.toArray(new Disk[0]));

        //Phase6: テーブルを更新し、ディスク関連のコマンドを発行
        //削除
        for (IIdentifiableEntity entity : sortOutResult.toRemoveList) {
            Disk disk = (Disk) entity;
            this.parent.jdbcManager.delete(disk)
                .execute();
            if (cmd != null) {
                cmd = this.parent.issueCommand(cmd.id, CommandOperation.REMOVE_DISK_ASYNC, localVm.id, disk.id);
            } else {
                cmd = this.parent.issueCommand(CommandOperation.REMOVE_DISK_ASYNC, localVm.id, disk.id);
            }
            this.parent.insertCommandVmMap(localVm.id, cmd.id);
        }
        //追加
        for (IIdentifiableEntity entity : sortOutResult.toAddList) {
            Disk remoteDisk = (Disk) entity;
            Disk localDisk = new Disk();
            localDisk.cloudId = parent.getCloudId();
            localDisk.size = remoteDisk.size;
            localDisk.name = remoteDisk.name;
            localDisk.vmId = localVm.id;
            localDisk.storageId = remoteDisk.storageId;
            localDisk.diskTemplateId = remoteDisk.diskTemplateId;
            if (localDisk.diskTemplateId != null && localDisk.diskTemplateId.equals(0L)) {
                localDisk.diskTemplateId = null;
            }

            this.parent.jdbcManager.insert(localDisk)
                .execute();

            remoteDisk.id = localDisk.id;

            if (cmd != null) {
                cmd = this.parent.issueCommand(cmd.id, CommandOperation.ADD_DISK_ASYNC, localDisk);
            } else {
                cmd = this.parent.issueCommand(CommandOperation.ADD_DISK_ASYNC, localDisk);
            }
            //FIXME: VMディスク追加コマンドの引数は vmIdとdiskIdではないか確認
            this.parent.insertCommandVmMap(localVm.id, cmd.id);
        }

        //Phase8: リソースの再取得 StorageReservationの消費
        List<Disk> diskList = parent.jdbcManager.from(Disk.class)
            .where(new SimpleWhere().eq(disk().vmId(), remoteVm.id))
            .getResultList();

        if (diskList.size() == 0) {
            throw new InternalRuntimeException(String.format("Vm(id=%d) has no disk", remoteVm.id));
        }

        List<StorageReservationDiskMap> storageReservationDiskMaps = rsvadp.getStorageReservationDiskMapList(diskList);

        //リソースマップ追加
        this.parent.jdbcManager.insertBatch(storageReservationDiskMaps)
            .execute();

        //LastCommand取得に関するロック
        parent.jdbcManager.update(localVm)
            .includes(vm().id())
            .execute();
        return remoteVm;
    }

    /**
     * @param dk
     */
    private Disk shallowStoreDisk(Disk result) {
        if (result.id == null) {
            this.parent.jdbcManager.insert(result)
                .execute();
        } else {
            this.parent.jdbcManager.update(result)
                .execute();
        }

        return result;
    }

    /**
     * @param result
     */
    private DiskTemplate shallowStoreDiskTemplate(DiskTemplate result) {
        if (result.id == null) {
            this.parent.jdbcManager.insert(result)
                .execute();
        } else {
            this.parent.jdbcManager.update(result)
                .execute();
        }

        return result;
    }

    /**
     * @param nwa
     */
    private NetworkAdapter shallowStoreNetworkAdapter(NetworkAdapter result) {
        if (result.id == null) {
            this.parent.jdbcManager.insert(result)
                .execute();
        } else {
            this.parent.jdbcManager.update(result)
                .execute();
        }

        return result;
    }

    /**
     * @param result
     */
    private NetworkAdapterTemplate shallowStoreNetworkAdapterTemplate(NetworkAdapterTemplate result) {
        if (result.id == null) {
            this.parent.jdbcManager.insert(result)
                .execute();
        } else {
            this.parent.jdbcManager.update(result)
                .execute();
        }

        return result;
    }

    /**
     * @param result
     * @return
     */
    private Template storeTemplate(Template result) {
        if (result.id == null) {
            this.parent.jdbcManager.insert(result)
                .execute();
        } else {
            this.parent.jdbcManager.update(result)
                .execute();
        }

        for (DiskTemplate dktmpl : result.diskTemplateList) {
            dktmpl.templateId = result.id;

            this.shallowStoreDiskTemplate(dktmpl);
        }

        for (NetworkAdapterTemplate nwatmpl : result.networkAdapterTemplateList) {
            nwatmpl.templateId = result.id;

            this.shallowStoreNetworkAdapterTemplate(nwatmpl);
        }

        return result;
    }

    /**
     * @param result
     */
    private Vm storeVm(Vm result) {
        if (result.id == null) {
            this.parent.jdbcManager.insert(result)
                .execute();
        } else {
            this.parent.jdbcManager.update(result)
                .execute();
        }

        for (Disk dk : result.diskList) {
            dk.vmId = result.id;

            this.shallowStoreDisk(dk);
        }

        for (NetworkAdapter nwa : result.networkAdapterList) {
            nwa.vmId = result.id;

            this.shallowStoreNetworkAdapter(nwa);
        }

        return result;
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
