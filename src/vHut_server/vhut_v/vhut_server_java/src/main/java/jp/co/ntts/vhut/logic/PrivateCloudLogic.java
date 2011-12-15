/*
 * Copyright 2011 NTT Software Corporation. All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import static jp.co.ntts.vhut.entity.Names.clusterResource;
import static jp.co.ntts.vhut.entity.Names.command;
import static jp.co.ntts.vhut.entity.Names.host;
import static jp.co.ntts.vhut.entity.Names.localId;
import static jp.co.ntts.vhut.entity.Names.network;
import static jp.co.ntts.vhut.entity.Names.networkAdapter;
import static jp.co.ntts.vhut.entity.Names.networkAdapterTemplate;
import static jp.co.ntts.vhut.entity.Names.publicIpResource;
import static jp.co.ntts.vhut.entity.Names.reservation;
import static jp.co.ntts.vhut.entity.Names.securityGroup;
import static jp.co.ntts.vhut.entity.Names.securityGroupTemplate;
import static jp.co.ntts.vhut.entity.Names.storage;
import static jp.co.ntts.vhut.entity.Names.storageResource;
import static jp.co.ntts.vhut.entity.Names.template;
import static jp.co.ntts.vhut.entity.Names.vlanResource;
import static jp.co.ntts.vhut.entity.Names.vm;
import static jp.co.ntts.vhut.entity.Names.vmCloudUserMap;
import static org.seasar.extension.jdbc.operation.Operations.desc;
import static org.seasar.extension.jdbc.operation.Operations.in;
import static org.seasar.extension.jdbc.parameter.Parameter.timestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.ntts.vhut.command.ICommand;
import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.dto.AdditionalDiskDto;
import jp.co.ntts.vhut.dto.CloudInfraPerformanceDto;
import jp.co.ntts.vhut.dto.CloudTroubleDto;
import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.RealmDto;
import jp.co.ntts.vhut.dto.ResourceDto;
import jp.co.ntts.vhut.dto.SpecDto;
import jp.co.ntts.vhut.dto.SwitchTemplateDto;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Cluster;
import jp.co.ntts.vhut.entity.ClusterReservationVmMap;
import jp.co.ntts.vhut.entity.ClusterReservationVmMapNames;
import jp.co.ntts.vhut.entity.ClusterResource;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.CommandTemplateMap;
import jp.co.ntts.vhut.entity.CommandTemplateMapNames;
import jp.co.ntts.vhut.entity.CommandVmMap;
import jp.co.ntts.vhut.entity.CommandVmMapNames;
import jp.co.ntts.vhut.entity.Conflict;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.DiskNames;
import jp.co.ntts.vhut.entity.DiskTemplate;
import jp.co.ntts.vhut.entity.DiskTemplateNames;
import jp.co.ntts.vhut.entity.ExternalIpRequestMode;
import jp.co.ntts.vhut.entity.Host;
import jp.co.ntts.vhut.entity.HostStatus;
import jp.co.ntts.vhut.entity.IIdentifiableEntity;
import jp.co.ntts.vhut.entity.LocalId;
import jp.co.ntts.vhut.entity.LocalIdType;
import jp.co.ntts.vhut.entity.Network;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.NetworkAdapterNames;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplate;
import jp.co.ntts.vhut.entity.NetworkAdapterTemplateNames;
import jp.co.ntts.vhut.entity.NetworkStatus;
import jp.co.ntts.vhut.entity.PublicIpResource;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.ResourceType;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.SecurityGroupTemplate;
import jp.co.ntts.vhut.entity.Storage;
import jp.co.ntts.vhut.entity.StorageResource;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.TemplateNames;
import jp.co.ntts.vhut.entity.Term;
import jp.co.ntts.vhut.entity.VlanResource;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.entity.VmCloudUserMap;
import jp.co.ntts.vhut.entity.VmCloudUserMapNames;
import jp.co.ntts.vhut.entity.VmNames;
import jp.co.ntts.vhut.entity.VmStatus;
import jp.co.ntts.vhut.exception.CloudReservationException;
import jp.co.ntts.vhut.exception.CloudReservationPeriodException;
import jp.co.ntts.vhut.exception.CloudResourceException;
import jp.co.ntts.vhut.exception.DBNoRecordRuntimeException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.DBStillReferencedRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.factory.CommandFactory;
import jp.co.ntts.vhut.util.Ipv4ConversionUtil;
import jp.co.ntts.vhut.util.MacConversionUtil;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutUtil;

import org.apache.commons.lang.RandomStringUtils;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 仮想化インフラを操作して仮想マシンの管理を行います.
 * <p>
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 */
public class PrivateCloudLogic implements ICloudServiceLogic, ICloudInfraLogic {

    /**
     * JDBCを使ってDBとやり取りします.
     */
    public JdbcManager jdbcManager;
    /**
     * クラウド関連の設定を管理します.
     */
    public CloudConfig cloudConfig;
    /**
     * 仮想化インフラと通信するためのコマンドを生成します.
     */
    public CommandFactory commandFactory;

    private long cloudId;

    private final ReservationQuota reservationQuota = new ReservationQuota(this);
    private final ReservationContent reservationContent = new ReservationContent(this);


    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#addReservation(long,
     * jp.co.ntts.vhut.dto.OrderDto)
     * [CSI] 1.25. リソース予約追加
     * [VSQ] 5. アプリケーションインスタンスグループの追加機能
     * [VSQ] 15. アプリケーションの追加機能
     * [VSQ] 17. アプリケーションの編集機能
     * [VSQ] 20. リリースドアプリケーションの追加機能
     * 例外：
     * 　リクエスト不正
     * 　　既存予約検出不能
     * 　　期間の不一致
     * 　リソース不足
     */
    @Override
    public Reservation addReservation(long reservationId, OrderDto order) throws CloudResourceException {
        return reservationQuota.addReservation(reservationId, order);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#addVmUser(long, long, long)
     * [CSI] 1.17.VM ユーザ追加
     * [VSQ] 49. サービスの定期更新機能
     */
    @Override
    public Vm addVmUser(long reservationId, long vmId, long cloudUserId) throws CloudReservationPeriodException, CloudReservationException {
        return this.reservationContent.addVmUser(reservationId, vmId, cloudUserId);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#batchUpdateVmUser(long,
     * long)
     * [CSI] 1.19. VMユーザ一括更新
     * [VSQ] 29. ユーザの削除機能
     */
    @Override
    public List<Vm> batchUpdateVmUser(long oldUserId, long newUserId) {
        List<Vm> result = new ArrayList<Vm>();

        List<VmCloudUserMap> vumaplist = this.jdbcManager.from(VmCloudUserMap.class)
            // .forUpdate()
            .where((new SimpleWhere()).eq(vmCloudUserMap().cloudUserId(), oldUserId))
            .getResultList();

        for (VmCloudUserMap vumap : vumaplist) {
            // VmCloudUserMapの更新
            vumap.cloudUserId = newUserId;
            this.jdbcManager.update(vumap)
                .execute();

            // 返り値にVmインスタンスを格納
            Vm vm = this.jdbcManager.from(Vm.class)
                .leftOuterJoin(vm().vmCloudUserMapList())
                .id(vumap.vmId)
                .getSingleResult();

            // VMユーザー削除コマンドの作成・発行
            Command cmd1 = this.issueCommand(getLastVmCommandId(vm.id), CommandOperation.REMOVE_USER, vumap.vmId, oldUserId);
            this.insertCommandVmMap(vumap.vmId, cmd1.id);
            // VMユーザー追加コマンドの作成・発行
            Command cmd2 = this.issueCommand(cmd1.id, CommandOperation.ADD_USER, vumap.vmId, newUserId);
            this.insertCommandVmMap(vumap.vmId, cmd2.id);

            //LastCommand取得に関するロック
            jdbcManager.update(vm)
                .includes(vm().id())
                .execute();

            result.add(vm);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#cancelCommand(long)
     * [CSI] 1.31. コマンドキャンセル
     * [VSQ] 10. アプリケーションインスタンスの表示機能
     * [VSQ] 13. アプリケーションの表示 機能
     * [VSQ] 19. リリースドアプリケーションの表示機能
     */
    @Override
    public Command cancelCommand(long commandId) {

        Command cmd = this.jdbcManager.from(Command.class)
            .leftOuterJoin(command().dependingCommand())
            .leftOuterJoin(command().commandList())
            .id(commandId)
            .getSingleResult();

        if (cmd == null) {
            throw new InputRuntimeException("commandId", String.format("command(id=%d) is not exist", commandId));
        }

        switch (cmd.status) {
            case SUCCESS:
            case CANCELED:
                break;
            default:
                cmd.status = CommandStatus.CANCELED;
                this.jdbcManager.update(cmd)
                    .includes(command().status())
                    .execute();
        }

        return cmd;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapter(long, long)
     */
    @Override
    public NetworkAdapter createNetworkAdapter(long vmId, long securityGroupId) throws CloudResourceException {
        List<NetworkAdapter> networkAdapterList = jdbcManager.from(NetworkAdapter.class)
            .where(new SimpleWhere().eq(networkAdapter().vmId(), vmId)
                .eq(networkAdapter().securityGroupId(), securityGroupId))
            .getResultList();

        if (networkAdapterList.size() > 1) {
            Long[] ids = new Long[networkAdapterList.size()];
            for (int i = 0; i < networkAdapterList.size(); i++) {
                NetworkAdapter na = networkAdapterList.get(i);
                ids[i] = na.id;
            }
            throw new DBStateRuntimeException(NetworkAdapter.class, ids);
        } else if (networkAdapterList.size() == 1) {
            return networkAdapterList.get(0);
        }

        NetworkAdapter networkAdapter = new NetworkAdapter();
        networkAdapter.cloudId = getCloudId();
        networkAdapter.vmId = vmId;
        networkAdapter.securityGroupId = securityGroupId;
        networkAdapter.mac = getOpenMacAddress();
        jdbcManager.insert(networkAdapter)
            .execute();

        networkAdapter.name = VhutUtil.createNetworkAdapterName(networkAdapter.id);
        jdbcManager.update(networkAdapter)
            .execute();

        return networkAdapter;
    }

    /**
     * @param networkAdapterList
     * @throws CloudResourceException クラウド側のリソース不足による例外
     */
    private String getOpenMacAddress() throws CloudResourceException {
        //開始マックアドレス
        String macStart = cloudConfig.rhevMacStart;
        //終了マックアドレス
        String macEnd = cloudConfig.rhevMacEnd;
        //割り当て可能なアドレスの個数（2の16乗 残り1byteは0埋め）
        int count = MacConversionUtil.getCount(macStart, macEnd);
        //使用中アドレスリストの作成
        boolean[] map = new boolean[count];
        List<NetworkAdapter> nwas = jdbcManager.from(NetworkAdapter.class)
            .getResultList();
        for (NetworkAdapter nwa : nwas) {
            if (MacConversionUtil.isScorp(nwa.mac, macStart, macEnd)) {
                map[MacConversionUtil.getMacAddressOrder(nwa.mac, macStart)] = true;
            }
        }
        //抜けの検索と
        for (int i = 1; i < map.length - 1; i++) {
            if (!map[i]) {
                byte[] macaddr = MacConversionUtil.getMacAddressWithOrderAsByte(macStart, i);
                return MacConversionUtil.byteToAddr(macaddr);
            }
        }
        throw new CloudResourceException(getCloudId(), ResourceType.MAC);
    }

    //    /**
    //     * @param networkAdapterList
    //     */
    //    private void assignMacAddress(List<NetworkAdapter> networkAdapterList) {
    //        //開始マックアドレス
    //        String macStart = cloudConfig.rhevMacStart;
    //        //終了マックアドレス
    //        String macEnd = cloudConfig.rhevMacEnd;
    //        //割り当て可能なアドレスの個数（2の16乗 残り1byteは0埋め）
    //        int count = MacConversionUtil.getCount(macStart, macEnd);
    //        //使用中アドレスリストの作成
    //        boolean[] map = new boolean[count];
    //        List<NetworkAdapter> nwas = jdbcManager.from(NetworkAdapter.class)
    //            .leftOuterJoin(networkAdapter().securityGroup(), false)
    //            .where(new SimpleWhere()
    //                .isNotNull(networkAdapter().securityGroup().networkId(), true))
    //            .getResultList();
    //        for (NetworkAdapter nwa : nwas) {
    //            if (MacConversionUtil.isScorp(nwa.mac, macStart, macEnd)) {
    //                map[MacConversionUtil.getMacAddressOrder(nwa.mac, macStart)] = true;
    //            }
    //        }
    //        //抜けの検索とIPアドレスの割り当て
    //        int i = 1;
    //        for (NetworkAdapter nwa : networkAdapterList) {
    //            nwa.mac = null;
    //            for (; i < map.length - 1; i++) {
    //                if (!map[i]) {
    //                    byte[] macaddr = MacConversionUtil.getMacAddressWithOrderAsByte(macStart, i);
    //                    nwa.mac = MacConversionUtil.byteToAddr(macaddr);
    //                    break;
    //                }
    //            }
    //            if (nwa.mac == null) {
    //                throw new CloudResourceException(getCloudId(), ResourceType.MAC);
    //            }
    //        }
    //    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapterTemplate(long, long)
     */
    @Override
    public NetworkAdapterTemplate createNetworkAdapterTemplate(long templateId, long securityGroupTemplateId) {
        List<NetworkAdapterTemplate> networkAdapterTemplateList = jdbcManager.from(NetworkAdapterTemplate.class)
            .where(new SimpleWhere().eq(networkAdapterTemplate().templateId(), templateId)
                .eq(networkAdapterTemplate().securityGroupTemplateId(), securityGroupTemplateId))
            .getResultList();

        if (networkAdapterTemplateList.size() > 1) {
            Long[] ids = new Long[networkAdapterTemplateList.size()];
            for (int i = 0; i < networkAdapterTemplateList.size(); i++) {
                NetworkAdapterTemplate nat = networkAdapterTemplateList.get(i);
                ids[i] = nat.id;
            }
            throw new DBStateRuntimeException(NetworkAdapterTemplate.class, ids);
        } else if (networkAdapterTemplateList.size() == 1) {
            return networkAdapterTemplateList.get(0);
        }
        NetworkAdapterTemplate networkAdapterTemplate = new NetworkAdapterTemplate();
        networkAdapterTemplate.cloudId = getCloudId();
        networkAdapterTemplate.templateId = templateId;
        networkAdapterTemplate.securityGroupTemplateId = securityGroupTemplateId;
        jdbcManager.insert(networkAdapterTemplate)
            .execute();

        networkAdapterTemplate.name = VhutUtil.createNetworkAdapterTemplateName(networkAdapterTemplate.id);
        jdbcManager.update(networkAdapterTemplate)
            .execute();

        return networkAdapterTemplate;
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.ntts.vhut.logic.ICloudServiceLogic#createReservation(jp.co.ntts
     * .vhut.dto.OrderDto)
     * [CSI] TODO: 仕様書に追加
     * [VSQ] TODO: シーケンスに追加
     * addReservationList(OrderDto)の繰り返し要素として実装
     */
    @Override
    public Reservation createReservation(OrderDto order) throws CloudResourceException {
        return reservationQuota.createReservation(order);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createReservationList(java.util.List)
     */
    @Override
    public List<Reservation> createReservationList(List<OrderDto> orderDtoList) throws CloudResourceException {
        return reservationQuota.createReservationList(orderDtoList);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroup(jp.co.ntts.vhut.entity.SecurityGroup)
     */
    @Override
    public SecurityGroup createSecurityGroup() {
        //TODO 名前を渡せるようにする。
        SecurityGroup entity = new SecurityGroup();
        entity.name = "defaultname";
        entity.cloudId = getCloudId();
        jdbcManager.insert(entity)
            .execute();
        return entity;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroupTemplate()
     */
    @Override
    public SecurityGroupTemplate createSecurityGroupTemplate() {
        //TODO 名前を渡せるようにする。
        SecurityGroupTemplate entity = new SecurityGroupTemplate();
        entity.name = "defaultname";
        entity.cloudId = getCloudId();
        jdbcManager.insert(entity)
            .execute();
        return entity;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createTemplate(long, long,
     * java.lang.String, java.lang.String)
     * [CSI] 1.20. テンプレート作成
     * [VSQ]? FIXED: "テンプレート追加" @ 20. リリースドアプリケーションの追加機能
     * テンプレート作成になぜリソース判定が必要なのか→テンプレートはストレージを消費する
     * NEXT: [VSQ]p.22の処理だとテンプレートが二重に追加される可能性がある
     * FIXED: reservationIdがなぜ必要なのか→既存の予約にテンプレートを追加するという理解で実装
     */
    @Override
    public Template createTemplate(long reservationId, long vmId, String name, String description) throws CloudReservationPeriodException, CloudReservationException {
        return this.reservationContent.createTemplate(reservationId, vmId, name, description);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#createVm(long, long, long, java.lang.String, java.lang.String)
     * [CSI] 1.9.VM作成
     * [VSQ]「VM作成」発見できず
     * [VSQ]? 「VM追加更新」 @ 17. アプリケーションの編集機能
     * [VSQ]? 「VM追加更新」 @ 49. サービスの定期更新機能
     * reservationIdで指定された既存のReservationのリソースが足りていたらVmを作成
     * 動作から考えるとメソッド名はaddVmToReservationが正しい
     * Vmの作成はストレージ・リソースのみを消費するという解釈で実装
     * templateIdとspecIdの両方が指定されている場合の振る舞い
     * 現在はtemplateIdの情報をspecIdの情報で上書き
     * TODO: realmIdの扱いが不明
     *        バージョンアップ時に要検討
     */
    @Override
    public Vm createVm(long reservationId, long templateId, long specId, String servicePrefix, String description) throws CloudReservationException {
        return this.reservationContent.createVm(reservationId, templateId, specId, servicePrefix, description);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapter(long, long)
     */
    @Override
    public void deleteNetworkAdapter(long vmId, long securityGroupId) {
        List<NetworkAdapter> networkAdapterList = jdbcManager.from(NetworkAdapter.class)
            .leftOuterJoin(networkAdapter().publicIpReservationList())
            .where(new SimpleWhere().eq(networkAdapter().vmId(), vmId)
                .eq(networkAdapter().securityGroupId(), securityGroupId))
            .getResultList();
        if (networkAdapterList.size() == 0) {
            throw new DBNoRecordRuntimeException(String.format("can not find the network_adapter for vm(id=%d) and securityGroup(id=%d)", vmId, securityGroupId));
        } else if (networkAdapterList.size() > 1) {
            Long[] ids = new Long[networkAdapterList.size()];
            for (int i = 0; i < networkAdapterList.size(); i++) {
                NetworkAdapter na = networkAdapterList.get(i);
                ids[i] = na.id;
            }
            throw new DBStateRuntimeException(NetworkAdapter.class, ids);
        }

        NetworkAdapter networkAdapter = networkAdapterList.get(0);

        if (networkAdapter.publicIpReservationList.size() > 0) {
            throw new DBStillReferencedRuntimeException(networkAdapter, networkAdapter.publicIpReservationList.toArray(new IIdentifiableEntity[0]));
        }

        //        for (PublicIpReservation publicIpReservation : networkAdapter.publicIpReservationList) {
        //            publicIpReservation.networkAdapterId = null;
        //            jdbcManager.update(publicIpReservation)
        //                .includes(Names.publicIpReservation().networkAdapterId())
        //                .execute();
        //        }

        int count = jdbcManager.delete(networkAdapter)
            .execute();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapterTemplate(long, java.lang.Long)
     */
    @Override
    public void deleteNetworkAdapterTemplate(long templateId, Long securityGroupTemplateId) {
        List<NetworkAdapterTemplate> networkAdapterTemplateList = jdbcManager.from(NetworkAdapterTemplate.class)
            .where(new SimpleWhere().eq(networkAdapterTemplate().templateId(), templateId)
                .eq(networkAdapterTemplate().securityGroupTemplateId(), securityGroupTemplateId))
            .getResultList();
        if (networkAdapterTemplateList.size() == 1) {
            jdbcManager.delete(networkAdapterTemplateList.get(0))
                .execute();
        } else if (networkAdapterTemplateList.size() > 1) {
            Long[] ids = new Long[networkAdapterTemplateList.size()];
            for (int i = 0; i < networkAdapterTemplateList.size(); i++) {
                NetworkAdapterTemplate nat = networkAdapterTemplateList.get(i);
                ids[i] = nat.id;
            }
            throw new DBStateRuntimeException(NetworkAdapter.class, ids);
        } else {
            throw new InputRuntimeException("templateId, securityGroupTemplateId", "NetworkAdapterTemplate is not found.");
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteReservation(long)
     * [CSI] 1.26
     * [VSQ] p.11,20,49
     */
    @Override
    public void deleteReservation(long reservationId) {
        this.reservationQuota.deleteReservation(reservationId);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroup(long)
     */
    @Override
    public void deleteSecurityGroup(long securityGroupId) {
        SecurityGroup securityGroup = jdbcManager.from(SecurityGroup.class)
            .leftOuterJoin(securityGroup().vlanReservationList())
            .leftOuterJoin(securityGroup().networkAdapterList())
            .id(securityGroupId)
            .getSingleResult();
        if (securityGroup == null) {
            return;
        }
        if (securityGroup.vlanReservationList.size() > 0) {
            throw new DBStillReferencedRuntimeException(securityGroup, securityGroup.vlanReservationList.toArray(new IIdentifiableEntity[0]));
        }
        if (securityGroup.networkAdapterList.size() > 0) {
            jdbcManager.deleteBatch(securityGroup.networkAdapterList)
                .execute();
        }
        jdbcManager.delete(securityGroup)
            .execute();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroupTemplate(long)
     */
    @Override
    public void deleteSecurityGroupTemplate(long securityGroupTemplateId) {
        SecurityGroupTemplate entity = jdbcManager.from(SecurityGroupTemplate.class)
            .leftOuterJoin(securityGroupTemplate().networkAdapterTemplateList())
            .id(securityGroupTemplateId)
            .getSingleResult();
        if (entity.networkAdapterTemplateList.size() > 0) {
            jdbcManager.deleteBatch(entity.networkAdapterTemplateList)
                .execute();
        }
        jdbcManager.delete(entity)
            .execute();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#getAbstractPerformance()
     * [CSI] 1.21. テンプレート削除
     * [VSQ] 21. リリースドアプリケーションの削除機能
     */
    @Override
    public void deleteTemplate(long templateId) {
        this.reservationContent.deleteTemplate(templateId);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)
     * [CSI] 1.11.VM削除
     * [VSQ] p.11,19,20,51
     * [VSQ]の「VM削除コマンドがなければ?」は重複発行を避ける意図だったが，考慮不要
     */
    @Override
    public void deleteVm(long vmId) {
        this.reservationContent.deleteVm(vmId);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#getAbstractPerformance()
     * [CSI] TODO: 仕様を記述する
     * [VSQ] 2. 概要情報提供機能
     */
    @Override
    public short getAbstractPerformance() {

        //起動中VM
        List<Vm> activeVmList = jdbcManager.from(Vm.class)
            .where(new SimpleWhere().eq(vm().status(), VmStatus.UP)
                .eq(vm().cloudId(), getCloudId()))
            .getResultList();

        //使用中CPU周波数合計
        long activeCpuFreq = 0;
        //使用中メモリ容量合計
        long activeMemory = 0;
        for (Vm vm : activeVmList) {
            activeCpuFreq += Math.round((float) vm.cpuUsage / 100 * (float) vm.cpuCore * (float) 2000);
            activeMemory += Math.round((float) vm.memoryUsage / 100 * (float) vm.memory);
        }

        //ホスト
        List<Host> hostList = jdbcManager.from(Host.class)
            .where(new SimpleWhere().eq(host().cloudId(), getCloudId()))
            .getResultList();
        //ホストCPU周波数合計
        long totalCpuFreq = 0;
        //ホストメモリ容量合計
        long totalMemory = 0;
        for (Host host : hostList) {
            totalCpuFreq += host.cpuCore * 2000;
            //NEXT: CPU周波数をホストで持つようにする。
            totalMemory += host.memory;
        }

        float cpuRate = (float) activeCpuFreq / (float) totalCpuFreq;
        float memoryRate = (float) activeMemory / (float) totalMemory;

        float rate = Math.max(cpuRate, memoryRate);

        short result = 1;

        if (rate > .7) {
            result = 5;
        } else if (rate > 0.6) {
            result = 4;
        } else if (rate > 0.5) {
            result = 3;
        } else if (rate > 0.4) {
            result = 2;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllAdditionalDiskList()
     * [CSI] TODO: 仕様書に追加
     * [VSQ] 2. 概要情報提供機能
     */
    @Override
    public List<AdditionalDiskDto> getAllAdditionalDiskList() {
        List<AdditionalDiskDto> adiskList = new ArrayList<AdditionalDiskDto>();

        List<Integer> diskList = this.cloudConfig.getDiskList();
        long id = 1;
        for (Integer diskSize : diskList) {
            AdditionalDiskDto addto = new AdditionalDiskDto();
            addto.id = id++;
            addto.size = diskSize;
            adiskList.add(addto);
        }
        return adiskList;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllNetworkList()
     * [CSI] 1.7 ネットワーク一覧表示
     */
    @Override
    public List<Network> getAllNetworkList() {
        return this.jdbcManager.from(Network.class)
            .getResultList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllRealmList()
     * [CSI] 1.4 地域一覧表示
     */
    @Override
    public List<RealmDto> getAllRealmList() {
        // NEXT: Public Cloudに対応する場合は必要
        return null;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllSpecList()
     * [CSI] 1.6. スペック一覧表示
     * [VSQ] 「スペックテンプレート一覧取得」
     */
    @Override
    public List<SpecDto> getAllSpecList() {
        return this.cloudConfig.getSpecList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllSwitchTemplateList()
     */
    @Override
    public List<SwitchTemplateDto> getAllSwitchTemplateList() {
        // [CSI] 1.35. スイッチテンプレート一覧表示
        // [VSQ] p.17,19
        return this.cloudConfig.getSwitchTemplateList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllTemplateList()
     */
    @Override
    public List<Template> getAllTemplateList() {
        // [CSI] 1.4. テンプレート一覧表示
        // [VSQ] #15,#17「ベーステンプレート一覧取得」;
        // シーケンス図ではサービスが直接データベースアクセスするように書かれているが，クラウド経由で取得するのが正しい
        return this.jdbcManager.from(Template.class)
            .leftOuterJoin(template().diskTemplateList())
            .getResultList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllUsers()
     * [CSI]
     * [VSQ]
     */
    @Override
    public List<CloudUser> getAllUserList() {
        return this.jdbcManager.from(CloudUser.class)
            .getResultList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getAllVmList()
     * [CSI] 1.5. VM 一覧表示
     * [CSI]
     * [VSQ]
     */
    @Override
    public List<Vm> getAllVmList() {
        return this.jdbcManager.from(Vm.class)
            .getResultList();
    }

    /**
     * @return クラウドのID.
     */
    public long getCloudId() {
        return this.cloudId;
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.ntts.vhut.logic.ICloudInfraLogic#getCloudServiceConfiguration()
     * [CSI] 1.32. 初期設定取得
     * [VSQ] 39. 基本設定の表示機能
     */
    @Override
    public CloudConfig getCloudConfiguration() {
        return cloudConfig;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getCommandAbstractionList()
     * [CSI] 1.34. タスク概要一覧取得
     * [VSQ] p.40
     */
    @Override
    public List<Command> getCommandAbstractionList() {
        return jdbcManager.from(Command.class)
            .leftOuterJoin(command().commandVmMapList())
            .leftOuterJoin(command().commandTemplateMapList())
            .leftOuterJoin(command().commandVmMapList()
                .vm())
            .leftOuterJoin(command().commandTemplateMapList()
                .template())
            .eager(command().startTime(), command().endTime(), command().errorMessage())
            .orderBy(desc(command().id()
                .toString()))
            .maxRows(100)
            .getResultList();
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.ntts.vhut.logic.ICloudServiceLogic#getCommandListByTemplateId(long)
     * [CSI] 1.29. テンプレート関連コマンド一覧取得
     * [VSQ] p.21 テンプレート関連コマンド概要一覧取得
     */
    @Override
    public List<Command> getCommandListByTemplateId(long templateId) {
        return this.jdbcManager.from(Command.class)
            .leftOuterJoin(command().commandTemplateMapList(), false)
            .leftOuterJoin(command().dependingCommand())
            .leftOuterJoin(command().commandList())
            .where(new SimpleWhere().eq(command().commandTemplateMapList()
                .templateId(), templateId)
                .in(command().status(), CommandStatus.WAITING, CommandStatus.EXECUTING, CommandStatus.ERROR, CommandStatus.UNKNOWN))
            .orderBy(command().id()
                .toString())
            .getResultList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getCommandListByVmId(long)
     * [CSI]
     * [VSQ]
     */
    @Override
    public List<Command> getCommandListByVmId(long vmId) {
        return this.jdbcManager.from(Command.class)
            .leftOuterJoin(command().commandVmMapList(), false)
            .leftOuterJoin(command().dependingCommand())
            .leftOuterJoin(command().commandList())
            .where(new SimpleWhere().eq(command().commandVmMapList()
                .vmId(), vmId)
                .in(command().status(), CommandStatus.WAITING, CommandStatus.EXECUTING, CommandStatus.ERROR, CommandStatus.UNKNOWN))
            .orderBy(command().id()
                .toString())
            .getResultList();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#getPerformance()
     * [CII] 1.1. パフォーマンス詳細取得
     * [VSQ] 35. パフォーマンスの表示機能
     */
    @Override
    public CloudInfraPerformanceDto getPerformance() {
        CloudInfraPerformanceDto result = new CloudInfraPerformanceDto();

        //起動中VM
        List<Vm> activeVmList = jdbcManager.from(Vm.class)
            .where(new SimpleWhere().eq(vm().status(), VmStatus.UP)
                .eq(vm().cloudId(), getCloudId()))
            .getResultList();
        //起動中VM数
        result.activeVm = activeVmList.size();
        //使用中CPU周波数合計
        result.activeCpuFreq = 0;
        //使用中メモリ容量合計
        result.activeMemory = 0;
        for (Vm vm : activeVmList) {
            result.activeCpuFreq += Math.round((float) vm.cpuUsage / 100 * (float) vm.cpuCore * (float) 2000);
            result.activeMemory += Math.round((float) vm.memoryUsage / 100 * (float) vm.memory);
        }

        //起動可能なVM数
        List<Vm> commitedVmList = jdbcManager.from(Vm.class)
            .leftOuterJoin(vm().vmCloudUserMapList(), false)
            .where(new SimpleWhere().ne(vmCloudUserMap().cloudUserId(), null)
                .eq(vm().cloudId(), getCloudId()))
            .getResultList();
        //起動可能なVM数
        result.commitedVm = commitedVmList.size();
        //起動可能なCPU周波数合計
        result.commitedCpuFreq = 0;
        //起動可能なメモリ容量合計
        result.commitedMemory = 0;
        for (Vm vm : commitedVmList) {
            result.commitedCpuFreq += vm.cpuCore * 2000;
            //NEXT: CPU周波数をホストで持つようにする。
            result.commitedMemory += vm.memory;
        }

        //ホスト
        List<Host> hostList = jdbcManager.from(Host.class)
            .where(new SimpleWhere().eq(host().cloudId(), getCloudId()))
            .getResultList();
        //ホストCPU周波数合計
        result.totalCpuFreq = 0;
        //ホストメモリ容量合計
        result.totalMemory = 0;
        for (Host host : hostList) {
            result.totalCpuFreq += host.cpuCore * 2000;
            //NEXT: CPU周波数をホストで持つようにする。
            result.totalMemory += host.memory;
        }

        //VLAN総数
        result.totalVlan = jdbcManager.from(Network.class)
            .where(new SimpleWhere().ne(network().status(), NetworkStatus.RESERVED_BY_SYSTEM))
            .getCount();

        //使用中VLAN総数
        result.activeVlan = jdbcManager.from(SecurityGroup.class)
            .where(new SimpleWhere().isNotNull(securityGroup().networkId(), true))
            .getCount();

        //ストレージ容量
        List<Storage> storageList = jdbcManager.from(Storage.class)
            .where(new SimpleWhere().eq(storage().cloudId(), getCloudId()))
            .getResultList();
        result.totalStorage = 0;
        result.activeStorage = 0;
        for (Storage storage : storageList) {
            result.totalStorage += storage.physicalSize;
            result.activeStorage += storage.physicalSize - storage.availableSize;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.ntts.vhut.logic.ICloudServiceLogic#getResourceListByTerm(java.sql
     * .Time, java.sql.Time)
     * [CSI]
     * [VSQ]
     */
    @Override
    public List<ResourceDto> getResourceListByTerm(Timestamp startTime, Timestamp endTime) {
        List<ResourceDto> result = null;

        int count = TimestampUtil.countDate(startTime, endTime);

        if (count <= 0) {
            throw new InputRuntimeException("startTime, endTime", "startTime should be before endTime");
        }

        result = new ArrayList<ResourceDto>();

        Map<Date, ResourceSet> drsmap = this.getResourceSetMap(startTime, endTime);

        Date[] drsmapKeyList = drsmap.keySet()
            .toArray(new Date[0]);

        Arrays.sort(drsmapKeyList);

        for (Date drsmapKey : drsmapKeyList) {

            ResourceSet rs = drsmap.get(drsmapKey);

            ResourceDto rd = new ResourceDto();

            ClusterResource cr = rs.getTotalCluster();
            rd.cpuCoreMax = cr.getCpuCoreLimit();
            rd.cpuCoreUsed = cr.cpuCoreTerminablyUsed;
            rd.memoryMax = cr.getMemoryLimit();
            rd.memoryUsed = cr.memoryTerminablyUsed;

            StorageResource sr = rs.getTotalStorage();
            rd.storageMax = sr.getStorageLimit();
            rd.storageUsed = sr.storageTerminablyUsed;

            VlanResource vr = rs.getVlan();
            rd.vlanMax = vr.getVlanLimit();
            rd.vlanUsed = vr.vlanTerminablyUsed;

            PublicIpResource pr = rs.getPublicIp();
            rd.publicIpMax = pr.getPublicIpLimit();
            rd.publicIpUsed = pr.publicIpTerminablyUsed;

            result.add(rd);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getTemplateById(long)
     * [CSI]
     * [VSQ]
     */
    @Override
    public Template getTemplateById(long templateId) {
        return this.jdbcManager.from(Template.class)
            .leftOuterJoin(template().diskTemplateList())
            .id(templateId)
            .getSingleResult();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getTermListToStartVm(long,
     * java.util.Date, java.util.Date)
     * [CSI] TODO: 仕様書に追加
     * [VSQ] 47. アプリケーション予約の追加更新削除機能、予約可能期間一覧取得
     *
     * OrderDtoで指定された日時の期間内でOrderDtoによる予約が可能な日時のリストを返す。
     * すべての期間内でリソースが十分であれば、Termのリストには、OrderDtoと同じ開始日と終了日が入ったTermがひとつ入る。
     * 期間の中間に利用できない区間があれば、Termのリストには前半と後半の2つのTermが含まれる。
     *
     */
    @Override
    public List<Term> getTermListToReserve(OrderDto order) {
        return this.reservationQuota.getTermListToReserve(order);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#getTroubleAbstractionList()
     * [CII] 1.2. 障害ポイント概要一覧取得
     * [VSQ] 2. 概要情報提供機能, 36. 障害の表示機能
     */
    @Override
    public CloudTroubleDto getTroubleAbstractionList() {

        CloudTroubleDto result = new CloudTroubleDto();

        result.vmList = this.jdbcManager.from(Vm.class)
            .where((new SimpleWhere()).eq(vm().cloudId(), getCloudId())
                .in(vm().status(), VmStatus.ERROR, VmStatus.UNKNOWN))
            .getResultList();

        result.hostList = this.jdbcManager.from(Host.class)
            .leftOuterJoin(host().vmList())
            .where((new SimpleWhere()).eq(host().cloudId(), getCloudId())
                .in(host().status(), HostStatus.ERROR, HostStatus.UNKNOWN))
            .getResultList();

        return result;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getVmById(long)
     */
    @Override
    public Vm getVmById(long vmId) {
        return this.jdbcManager.from(Vm.class)
            .leftOuterJoin(vm().diskList())
            .leftOuterJoin(vm().networkAdapterList())
            .leftOuterJoin(vm().template())
            .leftOuterJoin(vm().template()
                .diskTemplateList())
            .id(vmId)
            .getSingleResult();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#obtainNetwork(long, long)
     * [CSI] 1.23. ネットワーク占有
     * [VSQ] 該当箇所発見できず ソースで参照元を確認
     *
     * 指定reservationIdを持つReservationに，
     *        指定networkTemplateIdを持つNetworkTemplateを雛形とするNetworkを追加すると解釈
     *        IFを変更しています。下記のように動作することを想定しています。
     *        1. securityGroupを取得する。
     *        2. リソース判定指定された予約で追加のネットワークを使用可能か？
     *           今回はSecurityGroupの属性を考慮した検索は行わないので単純に数とする
     *        3. 空きNetworkをセレクト
     *        4. VlanReservationをSecurityGroupにリンクさせ更新
     *        5. SecurityGroup.networkAdapterListのすべての要素についてSAddIpCommnadとSAddNatCommandを発行
     *        5. 予約したNetworkをreturn
     *        
     */
    @Override
    public Network obtainNetwork(long reservationId, long securityGroupId, ExternalIpRequestMode exIpRequestMode) throws CloudReservationPeriodException, CloudReservationException {
        // この引数からはVMとの関係がわからないので，現在はVMを変更しない実装になっている
        // 
        return this.reservationContent.obtainNetwork(reservationId, securityGroupId, exIpRequestMode);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#rebuildVm(long)
     * 指定されたVMを削除した後，再度作成する.
     * [CSI] 1.12. VM再作成
     * [VSQ] 12. アプリケーションインスタンスのVM再作成機能
     * FIXED: [VSQ]では書き込み前にコマンド表の読み出しを行うことになっているが..
     * 同系列のコマンドがすでに実行されていることはチェックしないこととする
     * NEXT: [VSQ]では予約番号を返すことになっているが，I/F定義はvoidなのでvoidのままで実装
     * この処理はリソース消費には影響を与えないはずなので，リソースの利用可否はチェックしていない
     */
    @Override
    public void rebuildVm(long vmId) {
        Vm vm = this.getVmById(vmId, VmNames.networkAdapterList(), VmNames.vmCloudUserMapList());

        Command cmd;
        //削除
        cmd = this.issueCommand(getLastVmCommandId(vmId), CommandOperation.DELETE_VM_ASYNC, vmId, Boolean.FALSE);
        this.insertCommandVmMap(vmId, cmd.id);
        //作成
        cmd = this.issueCommand(cmd.id, CommandOperation.CREATE_VM_ASYNC, vm);
        this.insertCommandVmMap(vmId, cmd.id);

    }

    // 以下，非publicメソッド

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#releaseNetwork(long)
     * [CSI] 1.24. ネットワーク解放
     * [VSQ] FIXED: 該当箇所発見できず ソースで参照元を確認
     * IFを変更しています。下記のように動作することを想定しています。
     * 1. SecurityGroupおよび関連するNetwork、NetworkAdapterを取得する。
     * 2. SecurityGroup.networkAdapterListのすべての要素についてSRemoveIpCommnadとSRemoveNatCommandを発行
     * 3. NetworkとSecurityGroupのリンクを切って更新
     * 
     */
    @Override
    public void releaseNetwork(long securityGroupId) {
        // FIXED: この引数からはVMとの関係がわからないので，現在はVMを変更しない実装になっている
        // 
        this.reservationContent.releaseNetwork(securityGroupId);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#removeVmUser(long, long)
     * [CSI] 1.18.VM ユーザ削除
     * [VSQ] 49. サービスの定期更新機能
     * TBD: REMOVE_USERの引数の渡し方を要確認
     * TBD: REMOVE_USERはsyncだが，結果の受け取り方を要確認
     */
    @Override
    public Vm removeVmUser(long vmId, long userId) {
        return this.reservationContent.removeVmUser(vmId, userId);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#retryCommand(long)
     * [CSI] 1.30. コマンド再実行
     * [VSQ] 10. アプリケーションインスタンスの表示機能, 13. アプリケーションの表示機能, 19. リリースドアプリケーションの表示機能
     * Command#statusがSUCCESSの時は再実行とする
     * 
     */
    @Override
    public Command retryCommand(long commandId) {

        Command cmd = this.jdbcManager.from(Command.class)
            .leftOuterJoin(command().dependingCommand())
            .leftOuterJoin(command().commandList())
            .id(commandId)
            .getSingleResult();

        if (cmd == null) {
            throw new InputRuntimeException("commandId", String.format("command(id=%d) is not exist", commandId));
        }

        switch (cmd.status) {
            case EXECUTING:
            case WAITING:
                break;
            default:
                cmd.status = CommandStatus.WAITING;
                cmd.startTime = null;
                cmd.endTime = null;
                cmd.errorMessage = null;
                cmd.result = null;
                this.jdbcManager.update(cmd)
                    .includes(command().status(), command().startTime(), command().endTime(), command().errorMessage(), command().result())
                    .execute();
        }

        return cmd;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#setCloudId(long)
     */
    @Override
    public void setCloudId(long cloudId) {
        this.cloudId = cloudId;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#shutdownVm(long)
     * [CSI] 1.16.VM シャットダウン
     * [VSQ]? 48. 仮想マシンの電源操作
     * TBD: [VSQ] 48. 仮想マシンの電源操作 はshutDownVm()またはstopVm
     * TBD: [VSQ]の「停止コマンドがなければ」は考慮不要
     */
    @Override
    public Vm shutdownVm(long vmId) {
        Vm vm = this.jdbcManager.from(Vm.class)
            .id(vmId)
            .getSingleResult();

        if (vm == null) {
            throw new InputRuntimeException("vmId", String.format("Vm(id=%d) is not exist", vmId));
        }

        switch (vm.status) {
            case UP:
                //VMの状態更新
                vm.status = VmStatus.SHUTTING_DOWN;
                //コマンドの発行
                Command cmd = this.issueCommand(CommandOperation.SHUTDOWN_VM_SYNC, vmId);
                this.insertCommandVmMap(vm.id, cmd.id);
                this.jdbcManager.update(vm)
                    .includes(vm().status())
                    .execute();
            default:
                break;
        }

        return vm;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#startVm(long)
     * [CSI] 1.14.VM 起動
     * [VSQ] 48. 仮想マシンの電源操作
     * TBD: [VSQ]の「停止コマンドがなければ」は考慮不要
     */
    @Override
    public Vm startVm(long vmId) {
        Vm vm = this.jdbcManager.from(Vm.class)
            .id(vmId)
            .getSingleResult();

        if (vm == null) {
            throw new InputRuntimeException("vmId", String.format("Vm(id=%d) is not exist", vmId));
        }

        switch (vm.status) {
            case DOWN:
                //VMの状態更新
                vm.status = VmStatus.POWERING_UP;
                //コマンドの発行
                Command cmd = this.issueCommand(CommandOperation.START_VM_SYNC, vmId);
                this.insertCommandVmMap(vm.id, cmd.id);
                this.jdbcManager.update(vm)
                    .includes(vm().status())
                    .execute();
            default:
                break;
        }

        return vm;

    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#stopVm(long)
     * [CSI] 1.15.VM 停止
     * [VSQ]? 48. 仮想マシンの電源操作
     * TBD: [VSQ] 48. 仮想マシンの電源操作 はshutDownVm()またはstopVm
     * TBD: [VSQ]の「停止コマンドがなければ」は考慮不要
     */
    @Override
    public Vm stopVm(long vmId) {
        Vm vm = this.jdbcManager.from(Vm.class)
            .id(vmId)
            .getSingleResult();

        if (vm == null) {
            throw new InputRuntimeException("vmId", String.format("Vm(id=%d) is not exist", vmId));
        }

        switch (vm.status) {
            case DOWN:
                break;
            default:
                //                Command cmd = this.issueCommand(CommandOperation.STOP_VM_ASYNC, vmId);
                Command cmd = this.issueCommand(CommandOperation.STOP_VM_SYNC, vmId);
                this.insertCommandVmMap(vm.id, cmd.id);
                this.jdbcManager.update(vm)
                    .includes(vm().status())
                    .execute();
        }

        return vm;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#updateReservation(long,
     * jp.co.ntts.vhut.dto.OrderDto)
     * [CSI] 1.25. リソース予約更新
     * [VSQ] 5. アプリケーションインスタンスグループの追加機能
     */
    @Override
    public Reservation updateReservation(long reservationId, OrderDto order) throws CloudResourceException {
        return this.reservationQuota.updateReservation(reservationId, order);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#updateVm(long,
     * jp.co.ntts.vhut.entity.Vm)
     * [CSI] 1.10.VM更新
     * [VSQ]? 17. アプリケーションの編集機能(VM追加更新), 49. サービスの定期更新機能(VM追加更新)
     */
    @Override
    public Vm updateVm(long reservationId, Vm vm) throws CloudReservationException {
        return this.reservationContent.updateVm(reservationId, vm);
    }

    /**
     * 機能拡張された予約を取得します.
     *
     * @param reservationId 予約ID
     * @param joinProperties 外部参照したいテーブル名
     * @return 機能拡張された予約
     */
    protected ReservationAdapter getReservationInAdapterById(long reservationId, PropertyName... joinProperties) {
        // 指定された既存予約を取得
        Reservation rsv = this.jdbcManager.from(Reservation.class)
            .leftOuterJoin(reservation().clusterReservationList())
            .leftOuterJoin(reservation().clusterReservationList()
                .cluster())
            .leftOuterJoin(reservation().storageReservationList())
            .leftOuterJoin(reservation().storageReservationList()
                .storage())
            .leftOuterJoin(reservation().vlanReservationList())
            .leftOuterJoin(reservation().vlanReservationList()
                .network())
            .leftOuterJoin(reservation().publicIpReservationList())
            .id(reservationId)
            .getSingleResult();
        if (rsv == null) {
            throw new InputRuntimeException("reservationId", String.format("Reservation(id=%d) is not exist", reservationId));
        }

        //        List<PropertyName> joinproplist = Arrays.asList(joinProperties);
        //
        //        if (joinproplist.contains(ReservationNames.clusterReservationList())) {
        //            rsv.clusterReservationList =
        //                    this.jdbcManager.from(ClusterReservation.class)
        //                                .where((new SimpleWhere()).eq(ClusterReservationNames.reservationId(),
        //                                                              reservationId))
        //                                .getResultList();
        //        }
        //        if (joinproplist.contains(ReservationNames.publicIpReservationList())) {
        //            rsv.publicIpReservationList =
        //                    this.jdbcManager.from(PublicIpReservation.class)
        //                                .where((new SimpleWhere()).eq(PublicIpReservationNames.reservationId(),
        //                                                              reservationId))
        //                                .getResultList();
        //        }
        //        if (joinproplist.contains(ReservationNames.storageReservationList())) {
        //            rsv.storageReservationList =
        //                    this.jdbcManager.from(StorageReservation.class)
        //                                .where((new SimpleWhere()).eq(StorageReservationNames.reservationId(),
        //                                                              reservationId))
        //                                .getResultList();
        //        }
        //        if (joinproplist.contains(ReservationNames.vlanReservationList())) {
        //            rsv.vlanReservationList =
        //                    this.jdbcManager.from(VlanReservation.class)
        //                                .leftOuterJoin(vlanReservation().network())
        //                                .where((new SimpleWhere()).eq(VlanReservationNames.reservationId(),
        //                                                              reservationId))
        //                                .getResultList();
        //        }

        ReservationAdapter result = new ReservationAdapter(rsv, this.jdbcManager);

        return result;
    }

    /**
     * リソース情報を日付とResourceSetのマップで取得します.
     *
     * @param conds 検索条件
     * @return 日付とResourceSetのマップ
     */
    protected Map<Date, ResourceSet> getResourceSetMap(Conditions conds) {
        Map<Date, ResourceSet> result = new HashMap<Date, ResourceSet>();

        List<ClusterResource> crl = this.jdbcManager.from(ClusterResource.class)
            .where(conds.dateCondition, conds.clusterIdCondition)
            .orderBy(clusterResource().time()
                .toString())
            .getResultList();
        List<StorageResource> srl = this.jdbcManager.from(StorageResource.class)
            .where(conds.dateCondition, conds.storageIdCondition)
            .orderBy(storageResource().time()
                .toString())
            .getResultList();
        List<VlanResource> vrl = this.jdbcManager.from(VlanResource.class)
            .where(conds.dateCondition)
            .orderBy(vlanResource().time()
                .toString())
            .getResultList();
        List<PublicIpResource> pirl = this.jdbcManager.from(PublicIpResource.class)
            .where(conds.dateCondition)
            .orderBy(publicIpResource().time()
                .toString())
            .getResultList();

        for (ClusterResource resource : crl) {
            Timestamp t = resource.time;
            long id = resource.clusterId;
            ResourceSet rs = result.get(t);
            if (rs == null) {
                rs = new ResourceSet();
                rs.date = t;
                result.put(t, rs);
            }
            rs.cluster.put(id, resource);
        }
        for (StorageResource resource : srl) {
            Timestamp t = resource.time;
            long id = resource.storageId;
            ResourceSet rs = result.get(t);
            if (rs == null) {
                rs = new ResourceSet();
                rs.date = t;
                result.put(t, rs);
            }
            rs.storage.put(id, resource);
        }
        for (VlanResource resource : vrl) {
            Timestamp t = resource.time;
            ResourceSet rs = result.get(t);
            if (rs == null) {
                rs = new ResourceSet();
                rs.date = t;
                result.put(t, rs);
            }
            rs.vlan = resource;
        }
        for (PublicIpResource resource : pirl) {
            Timestamp t = resource.time;
            ResourceSet rs = result.get(t);
            if (rs == null) {
                rs = new ResourceSet();
                rs.date = t;
                result.put(t, rs);
            }
            rs.publicIp = resource;
        }

        return result;
    }

    private Map<Date, ResourceSet> getResourceSetMap(Timestamp begin, Timestamp end) {
        Conditions conds = new Conditions(begin, end, null, null, this.cloudId);
        return this.getResourceSetMap(conds);
    }

    /**
     * テンプレートの詳細を取得します.
     *
     * @param templateId テンプレートのID
     * @param joinProperties 外部参照したいテーブル名
     * @return テンプレートエンティティ
     */
    protected Template getTemplateById(long templateId, PropertyName... joinProperties) {
        // 指定された既存テンプレートを取得
        Template result = this.jdbcManager.from(Template.class)
            // .forUpdate()
            .id(templateId)
            .getSingleResult();
        if (result == null) {
            throw new InputRuntimeException("templateId", String.format("Template(id=%d) is not exist", templateId));
        }

        List<PropertyName> joinproplist = Arrays.asList(joinProperties);

        if (joinproplist.contains(TemplateNames.cluster())) {
            result.cluster = this.jdbcManager.from(Cluster.class)
                .id(result.clusterId)
                .getSingleResult();
        }
        if (joinproplist.contains(TemplateNames.commandTemplateMapList())) {
            result.commandTemplateMapList = this.jdbcManager.from(CommandTemplateMap.class)
                .where((new SimpleWhere()).eq(CommandTemplateMapNames.templateId(), templateId))
                .getResultList();
        }
        if (joinproplist.contains(TemplateNames.conflict())) {
            result.conflict = this.jdbcManager.from(Conflict.class)
                .id(result.conflictId)
                .getSingleResult();
        }
        if (joinproplist.contains(TemplateNames.diskTemplateList())) {
            result.diskTemplateList = this.jdbcManager.from(DiskTemplate.class)
                .where((new SimpleWhere()).eq(DiskTemplateNames.templateId(), templateId))
                .getResultList();
        }

        if (joinproplist.contains(TemplateNames.networkAdapterTemplateList())) {
            result.networkAdapterTemplateList = this.jdbcManager.from(NetworkAdapterTemplate.class)
                .where((new SimpleWhere()).eq(NetworkAdapterTemplateNames.templateId(), templateId))
                .getResultList();
        }
        if (joinproplist.contains(TemplateNames.vmList())) {
            result.vmList = this.jdbcManager.from(Vm.class)
                .where((new SimpleWhere()).eq(VmNames.templateId(), templateId))
                .getResultList();
        }

        return result;
    }

    /**
     * VMの詳細を取得します.
     *
     * @param vmId VMのID
     * @param joinProperties 外部参照したいテーブル名
     * @return VMエンティティ
     */
    protected Vm getVmById(long vmId, PropertyName... joinProperties) {
        // 指定された既存VMを取得
        Vm result = this.jdbcManager.from(Vm.class)
            // .forUpdate()
            .id(vmId)
            .getSingleResult();
        if (result == null) {
            throw new InputRuntimeException("vmId", String.format("Vm(id=%d) is not exist", vmId));
        }

        List<PropertyName> joinproplist = Arrays.asList(joinProperties);

        if (joinproplist.contains(VmNames.cluster())) {
            result.cluster = this.jdbcManager.from(Cluster.class)
                .id(result.clusterId)
                .getSingleResult();
        }
        if (joinproplist.contains(VmNames.clusterReservationVmMapList())) {
            result.clusterReservationVmMapList = this.jdbcManager.from(ClusterReservationVmMap.class)
                .where((new SimpleWhere()).eq(ClusterReservationVmMapNames.vmId(), vmId))
                .getResultList();
        }
        if (joinproplist.contains(VmNames.commandVmMapList())) {
            result.commandVmMapList = this.jdbcManager.from(CommandVmMap.class)
                .where((new SimpleWhere()).eq(CommandVmMapNames.vmId(), vmId))
                .getResultList();
        }
        if (joinproplist.contains(VmNames.conflict())) {
            result.conflict = this.jdbcManager.from(Conflict.class)
                .id(result.conflictId)
                .getSingleResult();
        }
        if (joinproplist.contains(VmNames.diskList())) {
            result.diskList = this.jdbcManager.from(Disk.class)
                .where((new SimpleWhere()).eq(DiskNames.vmId(), vmId))
                .getResultList();
        }
        if (joinproplist.contains(VmNames.host())) {
            result.host = this.jdbcManager.from(Host.class)
                .id(result.hostId)
                .getSingleResult();
        }
        if (joinproplist.contains(VmNames.networkAdapterList())) {
            result.networkAdapterList = this.jdbcManager.from(NetworkAdapter.class)
                .where((new SimpleWhere()).eq(NetworkAdapterNames.vmId(), vmId))
                .getResultList();
        }
        if (joinproplist.contains(VmNames.template())) {
            result.template = this.jdbcManager.from(Template.class)
                .id(result.templateId)
                .getSingleResult();
        }
        if (joinproplist.contains(VmNames.vmCloudUserMapList())) {
            result.vmCloudUserMapList = this.jdbcManager.from(VmCloudUserMap.class)
                .where((new SimpleWhere()).eq(VmCloudUserMapNames.vmId(), vmId))
                .getResultList();
        }

        return result;
    }

    /**
     * コマンドとVMを関連付けます.
     * @param vmId VMのID
     * @param commandId コマンドID
     */
    protected void insertCommandVmMap(Long vmId, Long commandId) {
        CommandVmMap map = new CommandVmMap();
        map.vmId = vmId;
        map.commandId = commandId;
        this.jdbcManager.insert(map)
            .execute();
    }

    /**
     * コマンドとテンプレートを関連付けます.
     * @param templateId テンプレートID
     * @param commandId コマンドID
     */
    protected void insertCommandTemplateMap(Long templateId, Long commandId) {
        CommandTemplateMap map = new CommandTemplateMap();
        map.templateId = templateId;
        map.commandId = commandId;
        this.jdbcManager.insert(map)
            .execute();
    }

    /**
     * コマンドを生成して登録する便利メソッド.
     * 可変引数でコマンド引数を指定するので、型に気を付けてください.
     *
     * @param operation オペレーション
     * @param arguments コマンド引数
     * @return コマンドエンティティ
     */
    protected Command issueCommand(CommandOperation operation, Serializable... arguments) {
        return this.issueCommand(null, operation, arguments);
    }

    /**
     * コマンドを生成して登録する便利メソッド.
     * 可変引数でコマンド引数を指定するので、型に気を付けてください.
     *
     * @param dependingCommandId 関連コマンドID
     * @param operation オペレーション
     * @param arguments コマンド引数
     * @return コマンドエンティティ
     */
    protected Command issueCommand(Long dependingCommandId, CommandOperation operation, Serializable... arguments) {
        Command result = null;

        ICommand icmd = this.commandFactory.newCommand(operation, this.getCloudId());
        icmd.setParameter(arguments);

        result = icmd.getCommand();
        result.dependingCommandId = dependingCommandId;

        this.jdbcManager.insert(result)
            .execute();

        return result;
    }


    /**
     * <p>
     * リソーステーブル検索用の検索条件を作成する内部クラスです.
     * </p>
     */
    class Conditions {
        /** 開始日 */
        Date begin;
        /** 終了日 */
        Date end;
        /** 期間条件 */
        Where dateCondition;
        /** クラスタID関連条件 */
        Where clusterIdCondition;
        /** ストレージID関連条件 */
        Where storageIdCondition;
        /** クラウドID関連条件 */
        Where cloudIdCondition;


        /**
         * プライベートコンストラクタ.
         */
        Conditions() {
        }

        /**
         * コンストラクタ.
         *
         * @param begin 開始日
         * @param end 終了日
         * @param clusterIdSet clusterIdのリスト(orで連結します)
         * @param storageIdSet storageIdのリスト(orで連結します)
         * @param cloudId クラウドIDのリスト
         */
        Conditions(Date begin, Date end, Set<Long> clusterIdSet, Set<Long> storageIdSet, Long cloudId) {
            this.begin = begin;
            this.end = end;

            this.dateCondition = (new SimpleWhere()).ge("time", timestamp(begin))
                .le("time", timestamp(end));

            if (clusterIdSet == null || clusterIdSet.size() == 0) {
                this.clusterIdCondition = new SimpleWhere();
            } else {
                List<SimpleWhere> clusterCondList = new ArrayList<SimpleWhere>(clusterIdSet.size());
                for (Long id : clusterIdSet) {
                    clusterCondList.add((new SimpleWhere()).eq(clusterResource().clusterId(), id));
                }
                this.clusterIdCondition = Operations.or(clusterCondList.toArray(new SimpleWhere[]{}));
            }

            if (storageIdSet == null || storageIdSet.size() == 0) {
                this.storageIdCondition = new SimpleWhere();
            } else {
                List<SimpleWhere> storageCondList = new ArrayList<SimpleWhere>(storageIdSet.size());
                for (Long id : storageIdSet) {
                    storageCondList.add((new SimpleWhere()).eq(storageResource().storageId(), id));
                }
                this.storageIdCondition = Operations.or(storageCondList.toArray(new SimpleWhere[]{}));
            }

            this.cloudIdCondition = (new SimpleWhere()).eq("cloudId", cloudId);
        }
    }


    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getSecurityGroupById(long)
     */
    @Override
    public SecurityGroup getSecurityGroupById(long id) {
        SecurityGroup sg = jdbcManager.from(SecurityGroup.class)
            .leftOuterJoin(securityGroup().networkAdapterList())
            .id(id)
            .getSingleResult();

        if (sg.networkId != null) {
            sg.network = jdbcManager.from(Network.class)
                .id(sg.networkId)
                .getSingleResult();
        }

        return sg;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#getSecurityGroupTemplateById(long)
     */
    @Override
    public SecurityGroupTemplate getSecurityGroupTemplateById(long id) {
        return jdbcManager.from(SecurityGroupTemplate.class)
            .leftOuterJoin(securityGroupTemplate().networkAdapterTemplateList())
            .id(id)
            .getSingleResult();
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#initializeNetworks()
     */
    @Override
    public void initializeNetworks() {
        //VLANの配列を作る
        List<Integer> vlanList = new ArrayList<Integer>();
        for (Integer i = cloudConfig.getVlanStartNumber(); i <= cloudConfig.getVlanEndNumber(); i++) {
            if (!cloudConfig.getVlanExcludeList()
                .contains(i)) {
                vlanList.add(i);
            }
        }
        //内部NWのMASKから導かれる内部IPの数
        //        byte[] bInIpMask = Ipv4ConversionUtil.addrTobyte(cloudConfig.inIpSubnetMask);
        //        int inIpCount = Ipv4ConversionUtil.getHostAddressCount(bInIpMask);
        //セグメントの数
        int segCount = vlanList.size();
        //内部NWの開始アドレス
        byte[] bInIpStartAddr = Ipv4ConversionUtil.addrTobyte(cloudConfig.inIpStartNwAddress);
        //ホストアドレスに使用可能なビット数
        int maskLength = 32 - (int) Math.ceil((Math.log((double) cloudConfig.getVirtSwitchPortCount() + 3.0) / Math.log(2.0)));
        //個別セグメント内のネットマスク
        byte[] bSegIpMask = Ipv4ConversionUtil.addrTobyte(cloudConfig.inIpSubnetMask);
        String segIpMask = Ipv4ConversionUtil.convertDotToHex(cloudConfig.inIpSubnetMask);
        //個別セグメント内のIPの個数
        int segIpCount = Ipv4ConversionUtil.getHostAddressCount(bSegIpMask);
        //セグメントの数
        //        int segCount = Math.min(vlanCount, (int) (inIpCount / segIpCount));
        //        int segCount = vlanCount;
        //DNSアドレス
        String dnsAddr = null;
        if (cloudConfig.inIpDnsAddress != null && !cloudConfig.inIpDnsAddress.equals("")) {
            dnsAddr = Ipv4ConversionUtil.convertDotToHex(cloudConfig.inIpDnsAddress);
        }

        //個別セグメントのNWアドレス
        byte[] bSegIpAddr = Ipv4ConversionUtil.getNetworkAddressAsByte(bInIpStartAddr, bSegIpMask);

        List<Network> networkList = new ArrayList<Network>();
        for (int i = 0; i < segCount; i++) {
            //個別セグメントのNWアドレス
            //            byte[] bSegIpAddr = Ipv4ConversionUtil.getIpAddressWithOrderAsByte(bInIpAddr, bInIpMask, segIpCount * i);
            byte[] bBroadCast = Ipv4ConversionUtil.getIpAddressWithOrderAsByte(bSegIpAddr, bSegIpMask, segIpCount - 1);
            byte[] bGateway = Ipv4ConversionUtil.getIpAddressWithOrderAsByte(bSegIpAddr, bSegIpMask, cloudConfig.getInIpGatewayOrder());
            byte[] bDhcp = Ipv4ConversionUtil.getIpAddressWithOrderAsByte(bSegIpAddr, bSegIpMask, cloudConfig.getInIpDhcpOrder());
            Network network = new Network();
            network.cloudId = cloudId;
            network.vlan = vlanList.get(i)
                .shortValue();
            network.name = VhutUtil.createNetworkName(cloudConfig.networkPrefix, network.vlan);
            network.networkAddress = Ipv4ConversionUtil.byteToAddr(bSegIpAddr);
            network.gateway = Ipv4ConversionUtil.byteToAddr(bGateway);
            network.dhcp = Ipv4ConversionUtil.byteToAddr(bDhcp);
            network.dns = dnsAddr != null ? dnsAddr : network.gateway;
            network.broadcast = Ipv4ConversionUtil.byteToAddr(bBroadCast);
            network.mask = segIpMask;
            if (cloudConfig.getVlanForMaintenanceList()
                .contains(network.vlan.intValue())) {
                network.status = NetworkStatus.RESERVED_BY_SYSTEM;
            } else {
                network.status = NetworkStatus.AVAILABLE;
            }
            networkList.add(network);

            bSegIpAddr = Ipv4ConversionUtil.getNextNetworkAddressAsByte(bSegIpAddr, bSegIpMask);
        }

        jdbcManager.insertBatch(networkList)
            .execute();

        //LocalIdテーブルの挿入

        List<LocalId> localIdList = new ArrayList<LocalId>();
        for (Network network : networkList) {
            LocalId localId = new LocalId();
            localId.cloudId = getCloudId();
            localId.type = LocalIdType.NETWORK;
            localId.globalId = network.id;
            localId.localId = network.name;
            localIdList.add(localId);
        }
        jdbcManager.insertBatch(localIdList)
            .execute();
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#isAnyNetworksIsNotAssigned()
     */
    @Override
    public boolean isAnyNetworksIsNotAssigned() {
        long securityGroupConnectedCount = jdbcManager.from(Network.class)
            .innerJoin(network().securityGroup())
            .getCount();
        long vlanReservationConnectedCount = jdbcManager.from(Network.class)
            .innerJoin(network().vlanReservationList())
            .getCount();
        return securityGroupConnectedCount == 0 && vlanReservationConnectedCount == 0;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#updateClusterResource()
     */
    @Override
    public void updateClusterResource() {
        //ホストのCPUコアとメモリを合計する
        Integer totalCpuCore = 0;
        Integer totalMemory = 0;
        List<Host> hosts = jdbcManager.from(Host.class)
            .leftOuterJoin(host().conflict())
            .where(new SimpleWhere().eq(host().clusterId(), cloudConfig.getRhevClusterId()))
            .getResultList();
        for (Host host : hosts) {
            if (host.conflict != null && host.conflict.detail.equals("Removed")) {
                continue;
            }
            totalCpuCore += host.cpuCore;
            totalMemory += host.memory;
        }
        //リソースの更新
        List<ClusterResource> toBeInsertList = new ArrayList<ClusterResource>();
        List<ClusterResource> toBeUpdateList = new ArrayList<ClusterResource>();
        Timestamp currentDate = TimestampUtil.getCurrentDateAsTimestamp();
        Timestamp minDate = TimestampUtil.subtract(currentDate, 1, TimestampUtil.Unit.DAY);
        Timestamp maxDate = TimestampUtil.add(cloudConfig.getReservationEndTimeMax(), 1, TimestampUtil.Unit.DAY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Map<String, ClusterResource> resourceMap = new HashMap<String, ClusterResource>();
        //マップを作成
        List<ClusterResource> oldResourceList = jdbcManager.from(ClusterResource.class)
            .where(new SimpleWhere().eq(clusterResource().clusterId(), cloudConfig.getRhevClusterId())
                .ge(clusterResource().time(), minDate)
                .le(clusterResource().time(), maxDate))
            .getResultList();
        for (ClusterResource oldResource : oldResourceList) {
            String key = dateFormat.format(oldResource.time);
            resourceMap.put(key, oldResource);
        }
        //挿入および更新
        Timestamp targetDate = currentDate;
        while (targetDate.before(maxDate)) {
            String key = dateFormat.format(targetDate);
            ClusterResource oldResource = resourceMap.get(key);
            if (oldResource == null) {
                //挿入
                ClusterResource newResource = new ClusterResource();
                newResource.id = targetDate.getTime();
                newResource.cpuCoreMax = totalCpuCore;
                newResource.cpuCoreTerminablyUsed = 0;
                newResource.memoryMax = totalMemory;
                newResource.memoryTerminablyUsed = 0;
                newResource.clusterId = cloudConfig.getRhevClusterId();
                newResource.time = targetDate;
                toBeInsertList.add(newResource);
            } else {
                //更新
                oldResource.cpuCoreMax = totalCpuCore;
                oldResource.memoryMax = totalMemory;
                toBeUpdateList.add(oldResource);
            }
            targetDate = TimestampUtil.add(targetDate, 1, TimestampUtil.Unit.DAY);
        }
        //挿入バッチ
        if (toBeInsertList.size() > 0) {
            jdbcManager.insertBatch(toBeInsertList)
                .execute();
        }
        //更新バッチ
        if (toBeUpdateList.size() > 0) {
            jdbcManager.updateBatch(toBeUpdateList)
                .execute();
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#updateStorageResource()
     */
    @Override
    public void updateStorageResource() {
        //ストレージサイズを取得する
        Integer size = 0;
        Storage storage = jdbcManager.from(Storage.class)
            .id(cloudConfig.getRhevStorageId())
            .getSingleResult();
        if (storage != null) {
            size = storage.physicalSize;
        }
        //リソースの更新
        List<StorageResource> toBeInsertList = new ArrayList<StorageResource>();
        List<StorageResource> toBeUpdateList = new ArrayList<StorageResource>();
        Timestamp currentDate = TimestampUtil.getCurrentDateAsTimestamp();
        Timestamp minDate = TimestampUtil.subtract(currentDate, 1, TimestampUtil.Unit.DAY);
        Timestamp maxDate = TimestampUtil.add(cloudConfig.getReservationEndTimeMax(), 1, TimestampUtil.Unit.DAY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Map<String, StorageResource> resourceMap = new HashMap<String, StorageResource>();
        //マップを作成
        List<StorageResource> oldResourceList = jdbcManager.from(StorageResource.class)
            .where(new SimpleWhere().eq(storageResource().storageId(), cloudConfig.getRhevStorageId())
                .ge(storageResource().time(), minDate)
                .le(storageResource().time(), maxDate))
            .getResultList();
        for (StorageResource oldResource : oldResourceList) {
            String key = dateFormat.format(oldResource.time);
            resourceMap.put(key, oldResource);
        }
        //挿入および更新
        Timestamp targetDate = currentDate;
        while (targetDate.before(maxDate)) {
            String key = dateFormat.format(targetDate);
            StorageResource oldResource = resourceMap.get(key);
            if (oldResource == null) {
                //挿入
                StorageResource newResource = new StorageResource();
                newResource.id = targetDate.getTime();
                newResource.storageMax = size;
                newResource.storageTerminablyUsed = 0;
                newResource.storageId = cloudConfig.getRhevStorageId();
                newResource.time = targetDate;
                toBeInsertList.add(newResource);
            } else {
                //更新
                oldResource.storageMax = size;
                toBeUpdateList.add(oldResource);
            }
            targetDate = TimestampUtil.add(targetDate, 1, TimestampUtil.Unit.DAY);
        }
        //挿入バッチ
        if (toBeInsertList.size() > 0) {
            jdbcManager.insertBatch(toBeInsertList)
                .execute();
        }
        //更新バッチ
        if (toBeUpdateList.size() > 0) {
            jdbcManager.updateBatch(toBeUpdateList)
                .execute();
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#updatePublicIpResources()
     */
    @Override
    public void updatePublicIpResources() {
        int count = Ipv4ConversionUtil.getHostAddressCount(cloudConfig.exIpStartAddress, cloudConfig.exIpEndAddress);
        count -= cloudConfig.getExIpExcludeList()
            .size();
        List<PublicIpResource> toBeInsertList = new ArrayList<PublicIpResource>();
        List<PublicIpResource> toBeUpdateList = new ArrayList<PublicIpResource>();
        Timestamp currentDate = TimestampUtil.getCurrentDateAsTimestamp();
        Timestamp minDate = TimestampUtil.subtract(currentDate, 1, TimestampUtil.Unit.DAY);
        Timestamp maxDate = TimestampUtil.add(cloudConfig.getReservationEndTimeMax(), 1, TimestampUtil.Unit.DAY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Map<String, PublicIpResource> resourceMap = new HashMap<String, PublicIpResource>();
        //マップを作成
        List<PublicIpResource> oldResourceList = jdbcManager.from(PublicIpResource.class)
            .where(new SimpleWhere().eq(publicIpResource().cloudId(), cloudId)
                .ge(publicIpResource().time(), minDate)
                .le(publicIpResource().time(), maxDate))
            .getResultList();
        for (PublicIpResource oldResource : oldResourceList) {
            String key = dateFormat.format(oldResource.time);
            resourceMap.put(key, oldResource);
        }
        //挿入および更新
        Timestamp targetDate = currentDate;
        while (targetDate.before(maxDate)) {
            String key = dateFormat.format(targetDate);
            PublicIpResource oldResource = resourceMap.get(key);
            if (oldResource == null) {
                //挿入
                PublicIpResource newResource = new PublicIpResource();
                newResource.id = targetDate.getTime();
                newResource.publicIpMax = count;
                newResource.publicIpTerminablyUsed = 0;
                newResource.cloudId = cloudId;
                newResource.time = targetDate;
                toBeInsertList.add(newResource);
            } else {
                //更新
                oldResource.publicIpMax = count;
                toBeUpdateList.add(oldResource);
            }
            targetDate = TimestampUtil.add(targetDate, 1, TimestampUtil.Unit.DAY);
        }
        //挿入バッチ
        if (toBeInsertList.size() > 0) {
            jdbcManager.insertBatch(toBeInsertList)
                .execute();
        }
        //更新バッチ
        if (toBeUpdateList.size() > 0) {
            jdbcManager.updateBatch(toBeUpdateList)
                .execute();
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#updateVlanResources()
     */
    @Override
    public void updateVlanResources() {
        long count = jdbcManager.from(Network.class)
            .where(new SimpleWhere().ne(network().status(), NetworkStatus.RESERVED_BY_SYSTEM))
            .getCount();
        List<VlanResource> toBeInsertList = new ArrayList<VlanResource>();
        List<VlanResource> toBeUpdateList = new ArrayList<VlanResource>();
        Timestamp currentDate = TimestampUtil.getCurrentDateAsTimestamp();
        Timestamp minDate = TimestampUtil.subtract(currentDate, 1, TimestampUtil.Unit.DAY);
        Timestamp maxDate = TimestampUtil.add(cloudConfig.getReservationEndTimeMax(), 1, TimestampUtil.Unit.DAY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Map<String, VlanResource> resourceMap = new HashMap<String, VlanResource>();
        //マップを作成
        List<VlanResource> oldResourceList = jdbcManager.from(VlanResource.class)
            .where(new SimpleWhere().eq(vlanResource().cloudId(), cloudId)
                .ge(vlanResource().time(), minDate)
                .le(vlanResource().time(), maxDate))
            .getResultList();
        for (VlanResource oldResource : oldResourceList) {
            String key = dateFormat.format(oldResource.time);
            resourceMap.put(key, oldResource);
        }
        //挿入および更新
        Timestamp targetDate = currentDate;
        //        maxDate = TimestampUtil.add(maxDate, 1, TimestampUtil.Unit.DAY);
        while (targetDate.before(maxDate)) {
            String key = dateFormat.format(targetDate);
            VlanResource oldResource = resourceMap.get(key);
            if (oldResource == null) {
                //挿入
                VlanResource newResource = new VlanResource();
                newResource.id = targetDate.getTime();
                newResource.vlanMax = (int) count;
                newResource.vlanTerminablyUsed = 0;
                newResource.cloudId = cloudId;
                newResource.time = targetDate;
                toBeInsertList.add(newResource);
            } else {
                //更新
                oldResource.vlanMax = (int) count;
                toBeUpdateList.add(oldResource);
            }
            targetDate = TimestampUtil.add(targetDate, 1, TimestampUtil.Unit.DAY);
        }
        //挿入バッチ
        if (toBeInsertList.size() > 0) {
            jdbcManager.insertBatch(toBeInsertList)
                .execute();
        }
        //更新バッチ
        if (toBeUpdateList.size() > 0) {
            jdbcManager.updateBatch(toBeUpdateList)
                .execute();
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudInfraLogic#clearNetworks()
     */
    @Override
    public void clearNetworks() {
        List<Network> networkList = jdbcManager.from(Network.class)
            .getResultList();
        if (networkList != null && networkList.size() > 0) {

            for (Network network : networkList) {
                List<LocalId> localIdList = jdbcManager.from(LocalId.class)
                    .where(new SimpleWhere().eq(localId().globalId(), network.id)
                        .eq(localId().localId(), network.name))
                    .getResultList();

                if (localIdList != null && localIdList.size() > 0) {
                    jdbcManager.deleteBatch(localIdList)
                        .execute();
                }
            }

            jdbcManager.deleteBatch(networkList)
                .execute();
        }
    }

    /**
     * 指定したVMを対象にしている最新のコマンドを取得する.
     * @param vmId vmのid
     * @return 最新のコマンド
     */
    public Command getLastVmCommand(Long vmId) {
        List<Command> vmCommands = jdbcManager.from(Command.class)
            .innerJoin(command().commandVmMapList())
            .where(new SimpleWhere().eq(command().commandVmMapList()
                .vmId(), vmId))
            .orderBy(desc(command().id()
                .toString()))
            .maxRows(2)
            .getResultList();
        if (vmCommands.size() > 0) {
            return vmCommands.get(0);
        }
        return null;
    }

    /**
     * 指定したVMを対象にしている最新のコマンドのIDを取得する.
     * @param vmId vmのid
     * @return 最新のコマンドのID
     */
    public Long getLastVmCommandId(Long vmId) {
        Command command = getLastVmCommand(vmId);
        if (command != null) {
            return command.id;
        }
        return null;
    }

    /**
     * 指定したテンプレートを対象にしている最新のコマンドを取得する.
     * @param templateId テンプレートのid
     * @return 最新のコマンド
     */
    public Command getLastTemplateCommand(Long templateId) {
        List<Command> templateCommands = jdbcManager.from(Command.class)
            .innerJoin(command().commandTemplateMapList())
            .where(new SimpleWhere().eq(command().commandTemplateMapList()
                .templateId(), templateId))
            .orderBy(desc(command().id()
                .toString()))
            .maxRows(2)
            .getResultList();
        if (templateCommands.size() > 0) {
            return templateCommands.get(0);
        }
        return null;
    }

    /**
     * 指定したテンプレートを対象にしている最新のコマンドのIDを取得する.
     * @param templateId テンプレートのid
     * @return 最新のコマンドのID
     */
    public Long getLastTemplateCommandId(Long templateId) {
        Command command = getLastTemplateCommand(templateId);
        if (command != null) {
            return command.id;
        }
        return null;
    }

    /**
     * ユーザID変更のコマンドを発行する。
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#changeUsersPassword(List<String>)
     */
    @Override
    public String changeUsersPassword(List<String> accountList) {

        ICommand icmd = this.commandFactory.newCommand(CommandOperation.CHANGE_USERS_PASSWORD, this.getCloudId());

        //パスワード生成 #7桁の英数字
        String password = RandomStringUtils.randomAlphanumeric(7);
        //コマンドの発行
        this.issueCommand(CommandOperation.CHANGE_USERS_PASSWORD, (Serializable) accountList, (Serializable) password);

        return password;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#searchCommandList(java.lang.String, java.sql.Time, java.sql.Time)
     */
    @Override
    public List<Command> searchCommandList(String keyword, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses) {
        ComplexWhere where = new ComplexWhere();
        //keyword条件を作成
        if (keyword != null) {
            where.and(new ComplexWhere().contains(command().commandVmMapList()
                .vm()
                .name(), keyword)
                .or()
                .contains(command().commandTemplateMapList()
                    .template()
                    .name(), keyword));
        }
        addCommandSearchConditions(startDate, endDate, operations, statuses, where);
        return selectCommands(where);
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#searchCommandListByTemplateIds(java.util.List, java.sql.Timestamp, java.sql.Timestamp)
     */
    @Override
    public List<Command> searchCommandListByTemplateIds(Collection<Long> templateIds, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses) {
        if (templateIds == null || templateIds.size() == 0) {
            return new ArrayList<Command>();
        }
        ComplexWhere where = new ComplexWhere();
        //keyword条件を作成
        where.and(new SimpleWhere().in(command().commandTemplateMapList()
            .templateId(), templateIds));
        addCommandSearchConditions(startDate, endDate, operations, statuses, where);
        return selectCommands(where);
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.logic.ICloudServiceLogic#searchCommandListByVmIds(java.util.List, java.sql.Timestamp, java.sql.Timestamp)
     */
    @Override
    public List<Command> searchCommandListByVmIds(Collection<Long> vmIds, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses) {
        if (vmIds == null || vmIds.size() == 0) {
            return new ArrayList<Command>();
        }

        ComplexWhere where = new ComplexWhere();
        //keyword条件を作成
        where.and(new SimpleWhere().in(command().commandVmMapList()
            .vmId(), vmIds));
        addCommandSearchConditions(startDate, endDate, operations, statuses, where);
        //検索
        return selectCommands(where);
    }

    /**
     * @param where
     * @return
     */
    private List<Command> selectCommands(Where where) {
        return jdbcManager.from(Command.class)
            .leftOuterJoin(command().commandVmMapList())
            .leftOuterJoin(command().commandTemplateMapList())
            .leftOuterJoin(command().commandVmMapList()
                .vm())
            .leftOuterJoin(command().commandTemplateMapList()
                .template())
            .eager(command().startTime(), command().endTime(), command().errorMessage())
            .orderBy(desc(command().id()
                .toString()))
            .where(where)
            .getResultList();
    }

    /**
     * @param startDate
     * @param endDate
     * @param operations
     * @param statuses
     * @param where
     */
    private void addCommandSearchConditions(Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses, ComplexWhere where) {
        //日時条件を作成
        if (startDate != null && endDate != null) {
            if (startDate.getTime() > endDate.getTime()) {
                throw new InputRuntimeException("startDate, endDate", "startTime should be before endTime");
            }
            where.and(new SimpleWhere().ge(command().endTime(), TimestampUtil.floorAsDate(startDate))
                .le(command().startTime(), TimestampUtil.floorAsDate(endDate)));
        }
        //操作名リストをANDで追加
        if (operations != null) {
            where.and(in(command().operation(), operations));
        }
        //状態名リストをANDで追加
        if (statuses != null) {
            where.and(in(command().status(), statuses));
        }
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
