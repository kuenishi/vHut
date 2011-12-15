/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import static jp.co.ntts.vhut.entity.Names.application;
import static jp.co.ntts.vhut.entity.Names.applicationInstance;
import static jp.co.ntts.vhut.entity.Names.applicationVm;
import static jp.co.ntts.vhut.entity.Names.applicationVmSecurityGroupMap;
import static jp.co.ntts.vhut.entity.Names.releasedApplication;
import static jp.co.ntts.vhut.entity.Names.term;
import static jp.co.ntts.vhut.entity.Names.vhutUserCloudUserMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.VhutUserDto;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceStatus;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationStatus;
import jp.co.ntts.vhut.entity.ApplicationVm;
import jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMap;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.IIdentifiableEntity;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.ReleasedApplication;
import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
import jp.co.ntts.vhut.entity.ReleasedApplicationStatus;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplateSecurityGroupMap;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.SecurityGroup;
import jp.co.ntts.vhut.entity.SecurityGroupTemplate;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Term;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMap;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.AigHasNoRappException;
import jp.co.ntts.vhut.exception.ApplicationStatusException;
import jp.co.ntts.vhut.exception.AuthenticationException;
import jp.co.ntts.vhut.exception.AuthorizationException;
import jp.co.ntts.vhut.exception.CloudReservationException;
import jp.co.ntts.vhut.exception.CloudReservationPeriodException;
import jp.co.ntts.vhut.exception.CloudResourceException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.DBStillReferencedRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.exception.NoAvailableReservationException;
import jp.co.ntts.vhut.exception.NotJoinedRuntimeException;
import jp.co.ntts.vhut.exception.ReleasedApplicationStatusException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.EntityUtil;
import jp.co.ntts.vhut.util.OrderUtil;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutUtil;
import jp.co.ntts.vhut.util.EntityUtil.SortOutResult;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>アプリケーションのサービスクラス.
 * <br>
 * <p>
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
@Generated(value = { "S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl" }, date = "2011/07/15 4:21:14")
public class ApplicationService extends AbstractService<Application> {

    /** クラウドロジックを呼び出すためのファクトリ */
    public CloudLogicFactory cloudLogicFactory;
    /** クラウドの設定値 */
    public ServiceConfig serviceConfig;
    /** ユーザセッション情報 */
    public VhutUserDto vhutUserDto;
    /** 使用するクラウドのID */
    protected long cloudId = 1;
    /** クラウドのサービスロジック */
    protected ICloudServiceLogic cloudServiceLogic;


    /**
     * 初期化.
     */
    @InitMethod
    public void init() {
        cloudServiceLogic = cloudLogicFactory.newCloudServiceLogic(cloudId);
    }

    /**
     * <p>アプリケーション概要一覧取得.
     * <br>
     *
     * @param なし
     * @return Application アプリケーションのリスト
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_APP)
    public List<Application> getAllApplicationAbstractionList() throws AuthenticationException {
        //認可系の処理
        SimpleWhere where = new SimpleWhere();
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_APP)) {
            where = new SimpleWhere().eq(application().vhutUserId(), vhutUserDto.getVhutUser().id);
        }
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(Application.class)
            .where(where.ne(application().status(), ApplicationStatus.DELETED))
            .getResultList();
    }

    /**
     * <p>アプリケーション詳細取得.
     * <br>
     *
     * @param id アプリケーションID
     * @return Application アプリケーション詳細情報
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.READ_OWN_APP)
    public Application getApplicationById(Long id) throws AuthenticationException, AuthorizationException {
        //引数のアプリケーションIDを条件に、セレクトする。
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationInstanceGroupList())
            .leftOuterJoin(application().applicationInstanceGroupList()
                .applicationInstanceList())
            .leftOuterJoin(application().releasedApplicationList())
            .leftOuterJoin(application().termList())
            .innerJoin(application().vhutUser())
            .eager(application().structure())
            .eager(application().vhutUserId())
            .eager(application().reservationId())
            .id(id)
            .getSingleResult();
        //セレクトした値が取得出来た場合、アプリケーションIDのアプリケーションVMをセレクトする。
        if (application == null) {
            throw new InputRuntimeException("id", String.format("Appliation(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_APP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.READ_ALL_APP.name());
        }
        //認可系の処理 リリースを落とす。
        if (!vhutUserDto.isAuthorized(Right.READ_OWN_RAPP) || (!vhutUserDto.isAuthorized(Right.READ_ALL_RAPP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id))) {
            application.releasedApplicationList = new ArrayList<ReleasedApplication>();
        }

        joinAllVms(application);
        joinAllSecurityGroups(application);
        //filtering ApplicationInstanceGroup
        List<ApplicationInstanceGroup> aigList = new ArrayList<ApplicationInstanceGroup>();
        for (ApplicationInstanceGroup aig : application.applicationInstanceGroupList) {
            boolean isNotDeleted = false;
            for (ApplicationInstance ai : aig.applicationInstanceList) {
                if (ai.status != ApplicationInstanceStatus.DELETED) {
                    isNotDeleted = true;
                }
            }
            if (isNotDeleted) {
                aigList.add(aig);
            }
        }
        application.applicationInstanceGroupList = aigList;

        //filtering ReleasedApplicationList
        List<ReleasedApplication> raList = new ArrayList<ReleasedApplication>();
        for (ReleasedApplication ra : application.releasedApplicationList) {
            if (ra.status != ReleasedApplicationStatus.DELETED) {
                raList.add(ra);
            }
        }
        application.releasedApplicationList = raList;

        return application;
    }

    //    private Where createNeOrNullWhere(CharSequence propertyName, Object value) {
    //        return new ComplexWhere().ne(propertyName, value).or().isNull(propertyName, true);
    //    }

    /**
     * <p>アプリケーション詳細追加.
     * <p>
     * Applicationの状態遷移：NONE->CREATING
     * 以下を実行
     * <ul>
     * <li>Vmのリストからネットワークを抽出
     * <li>SecurityGroupを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroup()}
     * <li>Vmを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createVm(long, Vm)}
     * <li>NetworkAdapterを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapter(long, long)}
     * </ul>
     * </p>
     * <p>
     * <b>ApplicationのIDが上記以外の場合</b>:受け付けない<br>
     * </p>
     *
     * @param remoteApplication アプリケーション詳細情報
     * @return application アプリケーション詳細情報
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.CREATE_OWN_APP)
    public Application createApplication(Application remoteApplication) throws CloudResourceException, CloudReservationException, AuthenticationException, AuthorizationException {
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.CREATE_ALL_APP) && !remoteApplication.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.CREATE_ALL_APP.name());
        }

        //リソースを予約
        ////テンプレートを取得する.
        if (remoteApplication.applicationVmList == null) {
            throw new NotJoinedRuntimeException(ApplicationVm.class);
        }
        for (ApplicationVm avm : remoteApplication.applicationVmList) {
            if (avm.vm == null) {
                throw new NotJoinedRuntimeException(Vm.class);
            }
            if (avm.vm.templateId == null) {
                throw new InputRuntimeException("remoteApplication", "Template Id is null.");
            }
            avm.vm.template = cloudServiceLogic.getTemplateById(avm.vm.templateId);
        }
        ////注文を組み立てる。
        OrderDto orderDto = OrderUtil.createOrderToCreateApplication(remoteApplication);
        ////予約する。
        Reservation reservation = cloudServiceLogic.createReservation(orderDto);

        Application localApplication = new Application();
        //remoteApplicationをlocalApplicationにコピー
        localApplication.imageUrl = remoteApplication.imageUrl;
        localApplication.name = remoteApplication.name;
        localApplication.structure = remoteApplication.structure;
        localApplication.vhutUserId = remoteApplication.vhutUserId;
        //セット
        localApplication.status = ApplicationStatus.CREATING;
        ////予約番号をアプリケーションに設定
        localApplication.reservationId = reservation.id;
        //挿入
        jdbcManager.insert(localApplication)
            .execute();

        //VM作成
        for (ApplicationVm remoteApplicationVm : remoteApplication.applicationVmList) {
            if (remoteApplicationVm.vm == null) {
                throw new InputRuntimeException("remoteApplication", "Vm is empty.");
            }
            Vm remoteVm = remoteApplicationVm.vm;
            if (remoteVm.templateId == null) {
                throw new InputRuntimeException("remoteApplication", "Vm.templateId is empty.");
            }
            //            ValidationUtil.isLengthLessEqual(remoteApplicationVm.name, serviceConfig.vmNameLengthMax());
            //            ValidationUtil.isIncludingOnly(remoteApplicationVm.name, "\\w");

            // 実態は{@link PrivateCloudLogic#createVm}
            // そのまた先の{@link ReservationContent#createVm(long,long,long,String,String,long)}
            // リソース判定とかコマンド追加した後のVm型をlocalVmに格納
            Vm localVm = cloudServiceLogic.createVm(reservation.id, remoteVm.templateId, remoteVm.specId, VhutUtil.createServicePrefix(serviceConfig.applicationVmPrefix, remoteApplicationVm.privateId), remoteVm.description);

            //Disk
            if (remoteVm.diskList != null) {
                if (localVm.diskList == null) {
                    localVm.diskList = new ArrayList<Disk>();
                }
                boolean toBeUpdated = false;
                for (Disk remoteDisk : remoteVm.diskList) {
                    if (remoteDisk.isAdditionalDisk()) {
                        Disk localDisk = new Disk();
                        localDisk.cloudId = remoteDisk.cloudId;
                        localDisk.name = remoteDisk.name;
                        localDisk.size = remoteDisk.size;
                        localDisk.storageId = remoteDisk.storageId;
                        localVm.diskList.add(localDisk);
                        toBeUpdated = true;
                    }
                }
                if (toBeUpdated) {
                    localVm = cloudServiceLogic.updateVm(reservation.id, localVm);
                }
            }

            ApplicationVm localApplicationVm = new ApplicationVm();
            //コピー
            localApplicationVm.cloudId = remoteApplicationVm.cloudId;
            localApplicationVm.imageUrl = remoteApplicationVm.imageUrl;
            localApplicationVm.name = remoteApplicationVm.name;
            localApplicationVm.description = remoteApplicationVm.description;
            localApplicationVm.privateId = remoteApplicationVm.privateId;
            //セット
            localApplicationVm.cloudId = cloudId;
            localApplicationVm.applicationId = localApplication.id;
            localApplicationVm.vmId = localVm.id;
            localApplicationVm.vm = localVm;
            //挿入
            jdbcManager.insert(localApplicationVm)
                .execute();

            //ApplicationVmSecurityGroupMap作成のための布石
            remoteApplicationVm.id = localApplicationVm.id;
            remoteApplicationVm.vmId = localVm.id;
        }

        //SecurityGroup作成
        for (ApplicationSecurityGroup remoteApplicationSecurityGroup : remoteApplication.applicationSecurityGroupList) {
            SecurityGroup securityGroup = cloudServiceLogic.createSecurityGroup();
            ApplicationSecurityGroup localApplicationSecurityGroup = new ApplicationSecurityGroup();
            //コピー
            localApplicationSecurityGroup.cloudId = remoteApplicationSecurityGroup.cloudId;
            localApplicationSecurityGroup.name = remoteApplicationSecurityGroup.name;
            localApplicationSecurityGroup.privateId = remoteApplicationSecurityGroup.privateId;
            //セット
            localApplicationSecurityGroup.cloudId = cloudId;
            localApplicationSecurityGroup.applicationId = localApplication.id;
            localApplicationSecurityGroup.securityGroupId = securityGroup.id;
            localApplicationSecurityGroup.securityGroup = securityGroup;
            //挿入
            jdbcManager.insert(localApplicationSecurityGroup)
                .execute();

            //ApplicationVmSecurityGroupMap作成のための布石
            remoteApplicationSecurityGroup.id = localApplicationSecurityGroup.id;
            remoteApplicationSecurityGroup.securityGroupId = securityGroup.id;
        }

        //NetworkAdapterの作成
        for (ApplicationSecurityGroup remoteApplicationSecurityGroup : remoteApplication.applicationSecurityGroupList) {
            for (ApplicationVmSecurityGroupMap remoteMap : remoteApplicationSecurityGroup.applicationVmSecurityGroupMapList) {
                ApplicationVmSecurityGroupMap localMap = new ApplicationVmSecurityGroupMap();
                localMap.applicationSecurityGroupId = remoteMap.applicationSecurityGroup.id;
                localMap.applicationVmId = remoteMap.applicationVm.id;
                jdbcManager.insert(localMap)
                    .execute();
                //NetworkAdapterの作成
                cloudServiceLogic.createNetworkAdapter(remoteMap.applicationVm.vmId, remoteMap.applicationSecurityGroup.securityGroupId);
            }
        }

        localApplication = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationInstanceGroupList())
            .leftOuterJoin(application().applicationInstanceGroupList()
                .applicationInstanceList())
            .leftOuterJoin(application().releasedApplicationList())
            .leftOuterJoin(application().termList())
            .innerJoin(application().vhutUser())
            .eager(application().structure())
            .eager(application().vhutUserId())
            .eager(application().reservationId())
            .id(localApplication.id)
            .getSingleResult();

        return localApplication;
    }

    /**
     * <p>アプリケーション詳細更新.
     * <p>
     * Applicationの状態遷移：DEACTIVE->UPDATING
     * 以下を実行
     * <ul>
     * <li>Vmのリストからネットワークを抽出
     * <li>SecurityGroupについて過不足を確認
     * <li>追加がある場合、SecurityGroupを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroup()}
     * <li>削除がある場合、SecurityGroupを削除{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroup(long)}
     * <li>Vmについて過不足を確認
     * <li>追加がある場合、Vmを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createVm(long, Vm)}
     * <li>削除がある場合、Vmを削除{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)}
     * <li>それ以外、チェックしないでVmを更新{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#updateVm(long, Vm)}
     * <li>NetworkAdapterを追加{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapter(long, long)}
     * <li>NetworkAdapterを削除{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapter(long, long)}
     * </ul>
     * </p>
     * <p>
     * <b>ApplicationのIDが上記以外の場合</b>:受け付けない<br>
     * </p>
     *
     * @param remoteApplication アプリケーション詳細情報
     * @return application アプリケーション詳細情報
     * @throws ApplicationStatusException アプリケーションの状態に関する例外
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.UPDATE_OWN_APP)
    public Application updateApplication(Application remoteApplication) throws ApplicationStatusException, CloudResourceException, CloudReservationException, AuthorizationException, AuthenticationException {

        //既存のアプリケーションを取得
        Application localApplication = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .eager(application().reservationId(), application().structure())
            .id(remoteApplication.id)
            .getSingleResult();

        if (localApplication == null) {
            //id>0なのに更新すべきApplicationがいない。
            throw new InputRuntimeException("remoteApplication", String.format("Appliation(id=%d) was not found", remoteApplication.id));
        }

        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.UPDATE_ALL_APP) && !localApplication.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.UPDATE_ALL_APP.name());
        }

        if (localApplication.status == ApplicationStatus.DEACTIVE) {
            localApplication.status = ApplicationStatus.UPDATING;
        } else {
            throw new ApplicationStatusException(localApplication.status, "should be DEACTIVE");
        }

        //既存のApplicationVmとVmの組み合わせと、更新するApplicationVmとVmの組み合わせかたが異なる場合エラーを返す。

        ////注文を組み立てる。
        OrderDto orderDto = OrderUtil.createOrderToCreateReleasedApplication(remoteApplication);
        //TODO: 本来的にはcreateOrderToCreateReleasedApplicationなので解決する.
        ////予約する。
        Reservation reservation = cloudServiceLogic.updateReservation(localApplication.reservationId, orderDto);

        //コピー
        localApplication.name = remoteApplication.name;
        localApplication.vhutUserId = remoteApplication.vhutUserId;
        localApplication.structure = remoteApplication.structure;

        //Applicationの更新、nullの項目は更新しない.
        jdbcManager.update(localApplication)
            .includes(application().name(), application().vhutUserId(), application().structure(), application().status())
            .execute();

        //Vmについて過不足を確認
        //EntityUtilを使って追加、削除、更新に振り分け
        EntityUtil.SortOutResult vmResult = EntityUtil.sortOutToSync(localApplication.applicationVmList.toArray(new IIdentifiableEntity[]{}), remoteApplication.applicationVmList.toArray(new IIdentifiableEntity[]{}));
        //追加がある場合、Vmを追加
        //VM追加
        for (IIdentifiableEntity entity : vmResult.toAddList) {
            ApplicationVm remoteApplicationVm = (ApplicationVm) entity;
            Vm remoteVm = remoteApplicationVm.vm;
            //Validation
            //            ValidationUtil.isLengthLessEqual(remoteApplicationVm.name, serviceConfig.vmNameLengthMax());
            //            ValidationUtil.isIncludingOnly(remoteApplicationVm.name, "\\w");

            if (remoteVm.networkAdapterList != null) {
                for (NetworkAdapter networkAdapter : remoteVm.networkAdapterList) {
                    networkAdapter.securityGroupId = networkAdapter.securityGroup.id;
                }
            }

            Vm localVm = cloudServiceLogic.createVm(reservation.id, remoteVm.templateId, remoteVm.specId, VhutUtil.createServicePrefix(serviceConfig.applicationVmPrefix, remoteApplicationVm.privateId), remoteVm.description);

            if (remoteVm.diskList != null) {
                if (localVm.diskList == null) {
                    localVm.diskList = new ArrayList<Disk>();
                }
                for (Disk remoteDisk : remoteVm.diskList) {
                    if (remoteDisk.isAdditionalDisk()) {
                        Disk localDisk = new Disk();
                        localDisk.cloudId = remoteDisk.cloudId;
                        localDisk.name = remoteDisk.name;
                        localDisk.size = remoteDisk.size;
                        localVm.diskList.add(localDisk);
                    }
                }
            }
            localVm = cloudServiceLogic.updateVm(reservation.id, localVm);

            ApplicationVm localApplicationVm = new ApplicationVm();
            //コピー
            localApplicationVm.cloudId = remoteApplicationVm.cloudId;
            localApplicationVm.imageUrl = remoteApplicationVm.imageUrl;
            localApplicationVm.name = remoteApplicationVm.name;
            localApplicationVm.description = remoteApplicationVm.description;
            localApplicationVm.privateId = remoteApplicationVm.privateId;
            //セット
            localApplicationVm.cloudId = cloudId;
            localApplicationVm.applicationId = localApplication.id;
            localApplicationVm.vmId = localVm.id;
            localApplicationVm.vm = localVm;
            //
            jdbcManager.insert(localApplicationVm)
                .execute();

            //ApplicationVmSecurityGroupMap作成のための布石
            remoteApplicationVm.id = localApplicationVm.id;
            remoteApplicationVm.vmId = localApplicationVm.vmId;
        }

        //それ以外、チェックしないでVmを更新
        List<ApplicationVm> toUpdateApplicationVmList = new ArrayList<ApplicationVm>();
        for (EntityUtil.ToUpdateValue value : vmResult.toUpdateList) {
            ApplicationVm localApplicationVm = (ApplicationVm) value.local;
            ApplicationVm remoteApplicationVm = (ApplicationVm) value.remote;
            Vm remoteVm = remoteApplicationVm.vm;

            //Validation
            //            ValidationUtil.isLengthLessEqual(remoteApplicationVm.name, serviceConfig.vmNameLengthMax());
            //            ValidationUtil.isIncludingOnly(remoteApplicationVm.name, "\\w");

            Vm localVm = cloudServiceLogic.updateVm(reservation.id, remoteVm);

            localApplicationVm.name = remoteApplicationVm.name;
            localApplicationVm.description = remoteApplicationVm.description;
            localApplicationVm.imageUrl = remoteApplicationVm.imageUrl;
            localApplicationVm.privateId = remoteApplicationVm.privateId;
            localApplicationVm.vmId = localVm.id;
            localApplicationVm.vm = localVm;

            toUpdateApplicationVmList.add(localApplicationVm);
        }
        if (toUpdateApplicationVmList.size() != 0) {
            jdbcManager.updateBatch(toUpdateApplicationVmList)
                .execute();
        }

        //SecurityGroupについて過不足を確認
        //EntityUtilを使って追加、削除、更新に振り分け
        EntityUtil.SortOutResult securityGroupResult = EntityUtil.sortOutToSync(localApplication.applicationSecurityGroupList.toArray(new IIdentifiableEntity[]{}), remoteApplication.applicationSecurityGroupList.toArray(new IIdentifiableEntity[]{}));

        //追加がある場合、SecurityGroupを追加
        for (IIdentifiableEntity entity : securityGroupResult.toAddList) {
            ApplicationSecurityGroup remoteApplicationSecurityGroup = (ApplicationSecurityGroup) entity;
            SecurityGroup localSecurityGroup = cloudServiceLogic.createSecurityGroup();
            ApplicationSecurityGroup localApplicationSecurityGroup = new ApplicationSecurityGroup();
            //更新
            localApplicationSecurityGroup.cloudId = remoteApplicationSecurityGroup.cloudId;
            localApplicationSecurityGroup.name = remoteApplicationSecurityGroup.name;
            localApplicationSecurityGroup.privateId = remoteApplicationSecurityGroup.privateId;
            //セット
            localApplicationSecurityGroup.cloudId = cloudId;
            localApplicationSecurityGroup.applicationId = localApplication.id;
            localApplicationSecurityGroup.securityGroupId = localSecurityGroup.id;
            localApplicationSecurityGroup.securityGroup = localSecurityGroup;
            //挿入
            jdbcManager.insert(localApplicationSecurityGroup)
                .execute();

            //ApplicationVmSecurityGroupMap作成のための布石
            remoteApplicationSecurityGroup.id = localApplicationSecurityGroup.id;
            remoteApplicationSecurityGroup.securityGroupId = localApplicationSecurityGroup.securityGroupId;
        }

        //ApplicationVmSecurityGroupMapについて過不足を確認
        List<ApplicationVmSecurityGroupMap> localApplicationVmSecurityGroupMapList = jdbcManager.from(ApplicationVmSecurityGroupMap.class)
            .innerJoin(applicationVmSecurityGroupMap().applicationVm())
            .innerJoin(applicationVmSecurityGroupMap().applicationSecurityGroup())
            .eager(applicationVmSecurityGroupMap().applicationVm()
                .vmId())
            .where(new SimpleWhere().eq(applicationVmSecurityGroupMap().applicationVm()
                .applicationId(), remoteApplication.id))
            .getResultList();
        List<ApplicationVmSecurityGroupMap> remoteApplicationVmSecurityGroupMapList = new ArrayList<ApplicationVmSecurityGroupMap>();
        for (ApplicationSecurityGroup remoteApplicationSecurityGroup : remoteApplication.applicationSecurityGroupList) {
            for (ApplicationVmSecurityGroupMap remoteMap : remoteApplicationSecurityGroup.applicationVmSecurityGroupMapList) {
                remoteMap.applicationVmId = remoteMap.applicationVm.id;
                remoteMap.applicationSecurityGroupId = remoteMap.applicationSecurityGroup.id;
                remoteApplicationVmSecurityGroupMapList.add(remoteMap);
            }
        }
        //EntityUtilを使って追加、削除、更新に振り分け
        EntityUtil.SortOutResult applicationVmSecurityGroupMapResult = EntityUtil.sortOutToSync(localApplicationVmSecurityGroupMapList.toArray(new IIdentifiableEntity[]{}), remoteApplicationVmSecurityGroupMapList.toArray(new IIdentifiableEntity[]{}));

        //追加がある場合、ApplicationVmSecurityGroupMapを追加
        for (IIdentifiableEntity entity : applicationVmSecurityGroupMapResult.toAddList) {
            ApplicationVmSecurityGroupMap remoteApplicationVmSecurityGroupMap = (ApplicationVmSecurityGroupMap) entity;
            ApplicationVmSecurityGroupMap localApplicationVmSecurityGroupMap = new ApplicationVmSecurityGroupMap();
            //セット
            localApplicationVmSecurityGroupMap.applicationVmId = remoteApplicationVmSecurityGroupMap.applicationVmId;
            localApplicationVmSecurityGroupMap.applicationSecurityGroupId = remoteApplicationVmSecurityGroupMap.applicationSecurityGroupId;
            //挿入
            jdbcManager.insert(localApplicationVmSecurityGroupMap)
                .execute();
            //NetworkAdapterの追加
            cloudServiceLogic.createNetworkAdapter(remoteApplicationVmSecurityGroupMap.applicationVm.vmId, remoteApplicationVmSecurityGroupMap.applicationSecurityGroup.securityGroupId);
        }
        //更新がある場合、ApplicationVmSecurityGroupMapを更新
        for (EntityUtil.ToUpdateValue value : applicationVmSecurityGroupMapResult.toUpdateList) {
            ApplicationVmSecurityGroupMap remoteApplicationVmSecurityGroupMap = (ApplicationVmSecurityGroupMap) value.remote;
            ApplicationVmSecurityGroupMap localApplicationVmSecurityGroupMap = (ApplicationVmSecurityGroupMap) value.local;
            boolean toUpdate = false;
            if (!remoteApplicationVmSecurityGroupMap.applicationVmId.equals(localApplicationVmSecurityGroupMap.applicationVmId)) {
                toUpdate = true;
            }
            if (!remoteApplicationVmSecurityGroupMap.applicationSecurityGroupId.equals(localApplicationVmSecurityGroupMap.applicationSecurityGroupId)) {
                toUpdate = true;
            }
            if (toUpdate) {
                //NetworkAdapterの削除
                cloudServiceLogic.deleteNetworkAdapter(localApplicationVmSecurityGroupMap.applicationVm.vmId, localApplicationVmSecurityGroupMap.applicationSecurityGroup.securityGroupId);
                //セット
                localApplicationVmSecurityGroupMap.applicationVmId = remoteApplicationVmSecurityGroupMap.applicationVmId;
                localApplicationVmSecurityGroupMap.applicationSecurityGroupId = remoteApplicationVmSecurityGroupMap.applicationSecurityGroupId;
                //挿入
                jdbcManager.update(localApplicationVmSecurityGroupMap)
                    .execute();
                //NetworkAdapterの追加
                cloudServiceLogic.createNetworkAdapter(remoteApplicationVmSecurityGroupMap.applicationVm.vmId, remoteApplicationVmSecurityGroupMap.applicationSecurityGroup.securityGroupId);
            }
        }
        //削除がある場合、ApplicationVmSecurityGroupMapを追加
        for (IIdentifiableEntity entity : applicationVmSecurityGroupMapResult.toRemoveList) {
            ApplicationVmSecurityGroupMap localApplicationVmSecurityGroupMap = (ApplicationVmSecurityGroupMap) entity;
            //NetworkAdapterの削除
            cloudServiceLogic.deleteNetworkAdapter(localApplicationVmSecurityGroupMap.applicationVm.vmId, localApplicationVmSecurityGroupMap.applicationSecurityGroup.securityGroupId);
            //削除
            jdbcManager.delete(localApplicationVmSecurityGroupMap)
                .execute();
        }

        //削除がある場合、Vmを削除
        for (IIdentifiableEntity entity : vmResult.toRemoveList) {
            ApplicationVm localApplicationVm = (ApplicationVm) entity;
            //vm削除
            cloudServiceLogic.deleteVm(localApplicationVm.vmId);
            //削除
            jdbcManager.delete(localApplicationVm)
                .execute();
        }

        //削除がある場合、SecurityGroupを削除
        for (IIdentifiableEntity entity : securityGroupResult.toRemoveList) {
            ApplicationSecurityGroup localApplicationSecurityGroup = (ApplicationSecurityGroup) entity;
            cloudServiceLogic.deleteSecurityGroup(localApplicationSecurityGroup.securityGroupId);
            jdbcManager.delete(localApplicationSecurityGroup)
                .execute();
        }

        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationInstanceGroupList())
            .leftOuterJoin(application().releasedApplicationList())
            .leftOuterJoin(application().termList())
            .innerJoin(application().vhutUser())
            .eager(application().structure())
            .eager(application().vhutUserId())
            .eager(application().reservationId())
            .id(localApplication.id)
            .disallowNoResult()
            .getSingleResult();
        //セレクトした値が取得出来た場合、アプリケーションIDのアプリケーションVMをセレクトする。
        joinAllVms(application);
        joinAllSecurityGroups(application);
        return application;
    }

    /**
     * <p>アプリケーション削除.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> DELETING
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンのネットワークアダプタを削除します。
     * <li>セキュリティグループを削除します。
     * <li>仮想マシンを削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapter(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroup(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをDELETINGに変更します。
     * </ul>
     *
     * @param id アプリケーションID
     * @throws StillReferencedException 他のレコードから参照されている場合に発生する例外
     * @throws ApplicationStatusException 状態異常の例外
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.DELETE_OWN_APP)
    public void deleteApplicationById(Long id) throws ApplicationStatusException, AuthorizationException, AuthenticationException {
        //引数のアプリケーションIDのアプリケーションが削除できるか判定する。
        //条件//・ReleasedApplicationが存在しないこと
        ////////・参照されるApplicationInstanceGroupが存在しないこと。
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().releasedApplicationList())
            .leftOuterJoin(application().applicationInstanceGroupList())
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .eager(application().reservationId())
            .id(id)
            .getSingleResult();
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.DELETE_ALL_APP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.DELETE_ALL_APP.name());
        }
        //バリデーション
        if (application == null) {
            throw new InputRuntimeException("id", String.format("Appliation(id=%d) was not found", id));
        }
        //事前チェック
        if (application.releasedApplicationList.size() > 0) {
            List<ReleasedApplication> existingRapps = new ArrayList<ReleasedApplication>();
            for (ReleasedApplication rapp : application.releasedApplicationList) {
                if (!rapp.status.equals(ReleasedApplicationStatus.DELETED)) {
                    existingRapps.add(rapp);
                }
            }
            if (existingRapps.size() > 0) {
                throw new DBStillReferencedRuntimeException(application, existingRapps.toArray(new IIdentifiableEntity[0]));
            }
        }

        if (application.applicationInstanceGroupList.size() > 0) {
            List<ApplicationInstanceGroup> existingAigs = new ArrayList<ApplicationInstanceGroup>();
            for (ApplicationInstanceGroup aig : application.applicationInstanceGroupList) {
                List<ApplicationInstance> ais = jdbcManager.from(ApplicationInstance.class)
                    .where(new SimpleWhere().eq(applicationInstance().applicationInstanceGroupId(), aig.id))
                    .getResultList();
                for (ApplicationInstance ai : ais) {
                    if (!ai.status.equals(ApplicationInstanceStatus.DELETED)) {
                        existingAigs.add(aig);
                        break;
                    }
                }
            }
            if (existingAigs.size() > 0) {
                throw new DBStillReferencedRuntimeException(application, existingAigs.toArray(new IIdentifiableEntity[0]));
            }
        }
        if (application.status == ApplicationStatus.DEACTIVE) {
            application.status = ApplicationStatus.DELETING;
        } else {
            throw new ApplicationStatusException(application.status, "should be DEACTIVE");
        }

        //NetworkAdapterの削除
        for (ApplicationVm applicationVm : application.applicationVmList) {
            for (ApplicationVmSecurityGroupMap map : applicationVm.applicationVmSecurityGroupMapList) {
                cloudServiceLogic.deleteNetworkAdapter(map.applicationVm.vmId, map.applicationSecurityGroup.securityGroupId);
                jdbcManager.delete(map)
                    .execute();
            }
        }
        //SecurityGroupの削除
        for (ApplicationSecurityGroup applicationSecurityGroup : application.applicationSecurityGroupList) {
            cloudServiceLogic.deleteSecurityGroup(applicationSecurityGroup.securityGroupId);
            jdbcManager.delete(applicationSecurityGroup)
                .execute();
        }
        //Vmの削除
        for (ApplicationVm applicationVm : application.applicationVmList) {
            cloudServiceLogic.deleteVm(applicationVm.vmId);
        }

        //予約があれば、予約を削除する。
        if (application.reservationId != null) {
            cloudServiceLogic.deleteReservation(application.reservationId);
            //            application.reservationId = null;
        }

        jdbcManager.update(application)
            .includes(application().status())
            .execute();
    }

    /**
     * <p>アプリケーション関連コマンド概要一覧取得.
     * <br>
     *
     * @param id アプリケーションID
     * @return Command コマンド概要情報のリスト
     */
    public List<Command> getUnfinishedCommandAbstractionListByApplicationId(Long id) {
        //引数のアプリケーションIDを条件に、セレクトする。
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .id(id)
            .disallowNoResult()
            .getSingleResult();
        //セレクトした値が取得出来た場合、コマンドをセレクトする。
        List<Command> commandList = new ArrayList<Command>();
        for (ApplicationVm applicationVm : application.applicationVmList) {
            commandList.addAll(cloudServiceLogic.getCommandListByVmId(applicationVm.vmId));
        }
        return commandList;
    }

    /**
     * <p>リリースドアプリケーション概要一覧取得.
     * <br>
     *
     * @param
     * @return ReleasedApplication リリースドアプリケーション概要情報のリスト
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_RAPP)
    public List<ReleasedApplication> getAllReleasedApplicationAbstractionList() throws AuthenticationException {
        //認可系の処理
        SimpleWhere where = new SimpleWhere();
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_RAPP)) {
            where = new SimpleWhere().eq(releasedApplication().application()
                .vhutUserId(), vhutUserDto.getVhutUser().id);
        }
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(ReleasedApplication.class)
            .where(where.ne(releasedApplication().status(), ReleasedApplicationStatus.DELETED))
            .getResultList();
    }

    /**
     * <p>リリースドアプリケーション詳細取得.
     * <br>
     *
     * @param id リリースドアプリケーションID
     * @return ReleasedApplication リリースドアプリケーション詳細情報
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_RAPP)
    public ReleasedApplication getReleasedApplicationById(Long id) throws AuthenticationException, AuthorizationException {
        //引数のリリースドアプリケーションIDを条件に、セレクトする。
        ReleasedApplication releasedApplication = jdbcManager.from(ReleasedApplication.class)
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList()
                .releasedApplicationTemplateSecurityGroupMapList())
            .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList())
            .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList()
                .releasedApplicationTemplateSecurityGroupMapList())
            .leftOuterJoin(releasedApplication().applicationInstanceList())
            .innerJoin(releasedApplication().application())
            .innerJoin(releasedApplication().application()
                .vhutUser())
            .eager(releasedApplication().structure())
            .eager(releasedApplication().reservationId())
            .id(id)
            .disallowNoResult()
            .getSingleResult();
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_RAPP) && !releasedApplication.application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.READ_ALL_RAPP.name());
        }
        //セレクトした値が取得出来た場合
        //クラウドからTemplateのをtemplateIdを引数にして取得し、releasedApplication.releasedApplicationTemplateListにセットする。
        joinAllTemplates(releasedApplication);
        joinAllSecurityGroupTemplates(releasedApplication);
        return releasedApplication;
    }

    /**
     * <p>リリースドアプリケーション追加.
     * <p>
     * このメソッドによるApplicationの状態遷移
     * <ul>
     * <li>DEACTIVE -> RELEASING
     * </ul>
     * このメソッドによるReleasedApplicationの状態遷移
     * <ul>
     * <li>NONE -> CREATING
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンからテンプレートを作成します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createTemplate(long, long, String, String)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createSecurityGroupTemplate()}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createNetworkAdapterTemplate(long, long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをRELEASINGに変更します。
     * </ul>
     *
     * @param id アプリケーションID
     * @return ReleasedApplication リリースドアプリケーション詳細情報
     * @throws ApplicationStatusException アプリケーションの状態に関する例外
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudReservationPeriodException
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.CREATE_OWN_RAPP)
    public ReleasedApplication addReleasedApplicationByApplicationId(Long id) throws ApplicationStatusException, CloudReservationPeriodException, CloudReservationException, CloudResourceException, AuthorizationException, AuthenticationException {
        //引数のアプリケーションIDでApplicationをセレクトする.
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .id(id)
            .eager("structure")
            .getSingleResult();

        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.CREATE_ALL_RAPP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.CREATE_ALL_RAPP.name());
        }

        if (application == null) {
            //id>0なのに更新すべきApplicationがいない。
            throw new InputRuntimeException("id", String.format("Appliation(id=%d) was not found", id));
        }

        if (application.status == ApplicationStatus.DEACTIVE) {
            application.status = ApplicationStatus.RELEASING;
        } else {
            throw new ApplicationStatusException(application.status, "should be DEACTIVE");
        }

        //リリースドアプリケーション型に値をセットする.
        ReleasedApplication releasedApplication = new ReleasedApplication();
        releasedApplication.applicationId = id;
        releasedApplication.structure = application.structure;
        ////createTimeに現在時間をセットする
        releasedApplication.createdTime = TimestampUtil.getCurrentTimestamp();
        releasedApplication.status = ReleasedApplicationStatus.CREATING;

        joinAllVms(application);
        //リソース予約追加の引数OrderDtoをセットする.
        OrderDto orderDto = OrderUtil.createOrderToCreateReleasedApplication(application);
        //リソース予約をする.
        Reservation reservation = cloudServiceLogic.createReservation(orderDto);
        //リリースドアプリケーションにリソース予約IDをセットする
        releasedApplication.reservationId = reservation.id;

        jdbcManager.insert(releasedApplication)
            .execute();

        //テンプレート
        Map<Long, ReleasedApplicationTemplate> templateCacheMap = new HashMap<Long, ReleasedApplicationTemplate>();
        for (ApplicationVm applicationVm : application.applicationVmList) {
            Template template = cloudServiceLogic.createTemplate(reservation.id, applicationVm.vmId, VhutUtil.createServicePrefix(serviceConfig.releasedApplicationTemplatePrefix, applicationVm.privateId), applicationVm.vm.description);
            ReleasedApplicationTemplate releasedApplicationTemplate = new ReleasedApplicationTemplate();
            releasedApplicationTemplate.cloudId = applicationVm.cloudId;
            releasedApplicationTemplate.name = applicationVm.name;
            releasedApplicationTemplate.description = applicationVm.description;
            releasedApplicationTemplate.imageUrl = applicationVm.imageUrl;
            releasedApplicationTemplate.privateId = applicationVm.privateId;
            releasedApplicationTemplate.releasedApplicationId = releasedApplication.id;
            releasedApplicationTemplate.templateId = template.id;
            jdbcManager.insert(releasedApplicationTemplate)
                .execute();
            templateCacheMap.put(applicationVm.id, releasedApplicationTemplate);
        }

        //セキュリティグループのテンプレート
        Map<Long, ReleasedApplicationSecurityGroupTemplate> securityGroupCachedMap = new HashMap<Long, ReleasedApplicationSecurityGroupTemplate>();
        for (ApplicationSecurityGroup applicationSecurityGroup : application.applicationSecurityGroupList) {
            SecurityGroupTemplate securityGroupTemplate = cloudServiceLogic.createSecurityGroupTemplate();
            ReleasedApplicationSecurityGroupTemplate releasedApplicationSecurityGroupTemplate = new ReleasedApplicationSecurityGroupTemplate();
            releasedApplicationSecurityGroupTemplate.cloudId = applicationSecurityGroup.cloudId;
            releasedApplicationSecurityGroupTemplate.name = applicationSecurityGroup.name;
            releasedApplicationSecurityGroupTemplate.privateId = applicationSecurityGroup.privateId;
            releasedApplicationSecurityGroupTemplate.releasedApplicationId = releasedApplication.id;
            releasedApplicationSecurityGroupTemplate.securityGroupTemplateId = securityGroupTemplate.id;
            jdbcManager.insert(releasedApplicationSecurityGroupTemplate)
                .execute();
            securityGroupCachedMap.put(applicationSecurityGroup.id, releasedApplicationSecurityGroupTemplate);
        }

        //セキュリティグループのテンプレートのマップ
        for (ApplicationSecurityGroup applicationSecurityGroup : application.applicationSecurityGroupList) {
            for (ApplicationVmSecurityGroupMap applicationVmSecurityGroupMap : applicationSecurityGroup.applicationVmSecurityGroupMapList) {
                ReleasedApplicationTemplateSecurityGroupMap map = new ReleasedApplicationTemplateSecurityGroupMap();
                map.releasedApplicationTemplate = templateCacheMap.get(applicationVmSecurityGroupMap.applicationVmId);
                map.releasedApplicationTemplateId = map.releasedApplicationTemplate.id;
                map.releasedApplicationSecurityGroupTemplate = securityGroupCachedMap.get(applicationVmSecurityGroupMap.applicationSecurityGroupId);
                map.releasedApplicationSecurityGroupTemplateId = map.releasedApplicationSecurityGroupTemplate.id;
                jdbcManager.insert(map)
                    .execute();
                cloudServiceLogic.createNetworkAdapterTemplate(map.releasedApplicationTemplate.templateId, map.releasedApplicationSecurityGroupTemplate.securityGroupTemplateId);
            }
        }

        jdbcManager.update(application)
            .includes(application().status())
            .execute();

        releasedApplication = jdbcManager.from(ReleasedApplication.class)
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList()
                .releasedApplicationTemplateSecurityGroupMapList())
            .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList())
            .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList()
                .releasedApplicationTemplateSecurityGroupMapList())
            .leftOuterJoin(releasedApplication().applicationInstanceList())
            .innerJoin(releasedApplication().application())
            .innerJoin(releasedApplication().application()
                .vhutUser())
            .eager(releasedApplication().structure())
            .eager(releasedApplication().reservationId())
            .id(releasedApplication.id)
            .disallowNoResult()
            .getSingleResult();
        //セレクトした値が取得出来た場合
        //クラウドからTemplateのをtemplateIdを引数にして取得し、releasedApplication.releasedApplicationTemplateListにセットする。
        joinAllTemplates(releasedApplication);
        joinAllSecurityGroupTemplates(releasedApplication);

        //追加したリリースドアプリケーションを返す
        return releasedApplication;
    }

    /**
     * <p>リリースドアプリケーション削除.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>READY -> DELETING
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>テンプレートを削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteNetworkAdapter(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroupTemplate(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteTemplate(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul
     * <li>ReleasedApplicationのstatusをDELETINGに変更します。
     * </ul>
     *
     * @param id リリースドアプリケーションID
     * @return
     * @throws ReleasedApplicationStatusException 状態不正
     * @throws AigHasNoRappException
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.DELETE_OWN_RAPP)
    public void removeReleasedApplicationById(Long id) throws ReleasedApplicationStatusException, AigHasNoRappException, AuthenticationException, AuthorizationException {

        ReleasedApplication releasedApplication = jdbcManager.from(ReleasedApplication.class)
            .innerJoin(releasedApplication().application())
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList()
                .releasedApplicationTemplateSecurityGroupMapList())
            .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList())
            .leftOuterJoin(releasedApplication().releasedApplicationSecurityGroupTemplateList()
                .releasedApplicationTemplateSecurityGroupMapList())
            .eager(releasedApplication().reservationId(), releasedApplication().application()
                .vhutUserId())
            .id(id)
            .getSingleResult();

        if (releasedApplication == null) {
            throw new InputRuntimeException("id", String.format("ReleasedAppliation(id=%d) was not found", id));
        }

        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.DELETE_ALL_RAPP) && !releasedApplication.application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.DELETE_ALL_RAPP.name());
        }

        if (releasedApplication.status == ReleasedApplicationStatus.READY) {
            releasedApplication.status = ReleasedApplicationStatus.DELETING;
        } else {
            throw new ReleasedApplicationStatusException(releasedApplication.status, "should be DEACTIVE");
        }

        //このReleasedApplicationを参照していて且つ作成作業中のApplicationInstanceが存在しないことを確認。
        //存在する場合は例外
        List<ApplicationInstance> applicationInstanceList = jdbcManager.from(ApplicationInstance.class)
            .where(new SimpleWhere().eq(applicationInstance().status(), ApplicationInstanceStatus.CREATING)
                .eq(applicationInstance().releasedApplicationId(), id))
            .getResultList();
        if (applicationInstanceList.size() > 0) {
            throw new DBStillReferencedRuntimeException(releasedApplication, applicationInstanceList.toArray(new IIdentifiableEntity[0]));
        }

        //上位のApplicationが未実施のApplicationInstanceGroupから参照され
        //このReleasedApplication以外にApplicationに所属するReleasedApplicationがない場合
        //ない場合は例外
        Application application = jdbcManager.from(Application.class)
            .innerJoin(application().applicationInstanceGroupList())
            .innerJoin(application().releasedApplicationList())
            .where(new SimpleWhere().eq(application().id(), releasedApplication.applicationId)
                .le(application().applicationInstanceGroupList()
                    .deleteTime(), TimestampUtil.getCurrentTimestamp()))
            .getSingleResult();
        if (application != null) {
            if (application.releasedApplicationList.size() == 1) {
                List<Long> ids = new ArrayList<Long>();
                for (ApplicationInstanceGroup aig : application.applicationInstanceGroupList) {
                    ids.add(aig.id);
                }
                throw new AigHasNoRappException(ids.toArray(new Long[0]), application.id);
            }
        }

        //NetworkAdapterTemplateの削除
        for (ReleasedApplicationTemplate releasedApplicationTemplate : releasedApplication.releasedApplicationTemplateList) {
            for (ReleasedApplicationTemplateSecurityGroupMap map : releasedApplicationTemplate.releasedApplicationTemplateSecurityGroupMapList) {
                cloudServiceLogic.deleteNetworkAdapterTemplate(map.releasedApplicationTemplate.templateId, map.releasedApplicationSecurityGroupTemplate.securityGroupTemplateId);
            }
        }

        //SecurityGroupTemplateの削除
        for (ReleasedApplicationSecurityGroupTemplate releasedApplicationSecurityGroupTemplate : releasedApplication.releasedApplicationSecurityGroupTemplateList) {
            cloudServiceLogic.deleteSecurityGroupTemplate(releasedApplicationSecurityGroupTemplate.securityGroupTemplateId);

        }

        //Templateの削除
        for (ReleasedApplicationTemplate releasedApplicationTemplate : releasedApplication.releasedApplicationTemplateList) {
            cloudServiceLogic.deleteTemplate(releasedApplicationTemplate.templateId);
        }

        //予約があれば、予約を削除する。
        if (releasedApplication.reservationId != null) {
            cloudServiceLogic.deleteReservation(releasedApplication.reservationId);
        }

        //更新（楽観ロック）
        jdbcManager.update(releasedApplication)
            .includes(releasedApplication().status())
            .execute();
    }

    /**
     * <p>リリースドアプリケーション関連コマンド概要一覧取得.
     * <br>
     *
     * @param id リリースドアプリケーションID
     * @return Command コマンド概要情報のリスト
     */
    public List<Command> getUnfinishedCommandAbstractionListByReleasedApplicationId(Long id) {
        //引数のリリースドアプリケーションIDを条件に、セレクトする。
        ReleasedApplication releasedApplication = new ReleasedApplication();
        releasedApplication = jdbcManager.from(ReleasedApplication.class)
            .leftOuterJoin(releasedApplication().releasedApplicationTemplateList())
            .id(id)
            .getSingleResult();
        if (releasedApplication == null) {
            throw new InputRuntimeException("id", String.format("ReleasedAppliation(id=%d) was not found", id));
        }
        //取得したreleasedApplicationTemplateList.templateIdでテンプレート関連コマンド概要一覧取得する。
        List<Command> commandList = new ArrayList<Command>();
        for (ReleasedApplicationTemplate rat : releasedApplication.releasedApplicationTemplateList) {
            commandList.addAll(cloudServiceLogic.getCommandListByTemplateId(rat.templateId));
        }
        return commandList;
    }

    /**
     * <p>ユーザ関連アプリケーション概要一覧取得.
     * <br>
     *
     * @param id ユーザID
     * @return Application アプリケーション概要情報のリスト
     */
    public List<Application> getAllApplicationAbstractionListByUserId(Long id) {
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(Application.class)
            .where(new SimpleWhere().eq(application().vhutUserId(), id)
                .ne(application().status(), ApplicationStatus.DELETED))
            .getResultList();
    }

    /**
     * <p>アプリケーションコマンド再実行.
     * <br>
     *
     * @param commandId コマンドID
     * @return Command コマンド詳細情報
     */
    @Auth(right = Right.COMMAND_OWN_APP)
    public Command retryCommand(Long commandId) {
        return cloudServiceLogic.retryCommand(commandId);
    }

    /**
     * <p>アプリケーションコマンドキャンセル.
     * <br>
     *
     * @param commandId コマンドID
     * @return Command コマンド詳細情報
     */
    @Auth(right = Right.COMMAND_OWN_APP)
    public Command cancelCommand(Long commandId) {
        return cloudServiceLogic.cancelCommand(commandId);
    }

    /**
     * <p>利用可能期間一覧取得.
     * <br>
     *
     * @param applicationId アプリケーションID
     * @return List<Term> 利用可能期間のリスト
     */
    @Auth(right = Right.READ_OWN_APP)
    public List<Term> getTermListByApplicationId(Long applicationId) {
        //TermテーブルからアプリケーションIDが一致するTermを取得する。
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<Term> termList = jdbcManager.from(Term.class)
            .where(new SimpleWhere().eq(term().applicationId(), applicationId))
            .getResultList();
        return termList;
    }

    /**
     * <p>予約可能期間一覧取得.
     * <br>
     *
     * @param applicationId アプリケーションID
     * @param startTime 開始日時
     * @param endTime 終了日時
     * @return List<Term> 利用可能期間のリスト
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.TERM_OWN_APP)
    public List<Term> getAvailableTermListByApplicationId(Long applicationId, Timestamp startTime, Timestamp endTime) throws AuthorizationException, AuthenticationException {
        //アプリケーション取得
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationInstanceGroupList())
            .leftOuterJoin(application().releasedApplicationList())
            .leftOuterJoin(application().termList())
            .innerJoin(application().vhutUser())
            .eager(application().structure())
            .eager(application().vhutUserId())
            .eager(application().reservationId())
            .id(applicationId)
            .getSingleResult();
        if (application == null) {
            throw new InputRuntimeException("applicationId", String.format("Appliation(id=%d) was not found", applicationId));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.TERM_ALL_APP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.TERM_ALL_APP.name());
        }
        //Vmを割り当てる
        joinAllVms(application);
        //SecurityGroupを割り当てる
        joinAllSecurityGroups(application);
        //起動のためのオーダー作成
        OrderDto order = OrderUtil.createOrderToStart(application, startTime, endTime, serviceConfig.getExIpRequestMode());
        //予約可能期間を取得
        List<Term> termList = cloudServiceLogic.getTermListToReserve(order);
        return termList;
    }

    /**
     * <p>予約可能期間一括追加更新.
     * <br>
     *
     * @param applicationId アプリケーションID
     * @param termList 利用可能期間のリスト
     * @return List<Term> 利用可能期間のリスト
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.TERM_OWN_APP)
    public List<Term> setTermList(Long applicationId, List<Term> termList) throws CloudResourceException, AuthenticationException, AuthorizationException {

        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationInstanceGroupList())
            .leftOuterJoin(application().releasedApplicationList())
            .leftOuterJoin(application().termList())
            .id(applicationId)
            .getSingleResult();
        if (application == null) {
            throw new InputRuntimeException("applicationId", String.format("Appliation(id=%d) was not found", applicationId));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.TERM_ALL_APP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.TERM_ALL_APP.name());
        }
        //Vmを割り当てる
        joinAllVms(application);
        //SecurityGroupを割り当てる
        joinAllSecurityGroups(application);

        SortOutResult result = EntityUtil.sortOutToSync(application.termList.toArray(new IIdentifiableEntity[]{}), termList.toArray(new IIdentifiableEntity[]{}));

        //追加
        for (IIdentifiableEntity entity : result.toAddList) {
            Term remoteTerm = (Term) entity;
            Term localTerm = new Term();
            localTerm.startTime = remoteTerm.startTime;
            localTerm.endTime = remoteTerm.endTime;
            localTerm.applicationId = applicationId;
            //予約追加
            OrderDto orderDto = OrderUtil.createOrderToStart(application, localTerm.startTime, localTerm.endTime, serviceConfig.getExIpRequestMode());
            Reservation reservation = cloudServiceLogic.createReservation(orderDto);
            localTerm.reservationId = reservation.id;
            //DB追加
            jdbcManager.insert(localTerm)
                .execute();
        }
        //更新
        for (EntityUtil.ToUpdateValue value : result.toUpdateList) {
            Term localTerm = (Term) value.local;
            Term remoteTerm = (Term) value.remote;
            //予約更新
            Reservation reservation;
            OrderDto orderDto = OrderUtil.createOrderToStart(application, remoteTerm.startTime, remoteTerm.endTime, serviceConfig.getExIpRequestMode());
            if (localTerm.reservationId != null && localTerm.reservationId > 0) {
                reservation = cloudServiceLogic.updateReservation(localTerm.reservationId, orderDto);
            } else {
                reservation = cloudServiceLogic.createReservation(orderDto);
            }
            localTerm.startTime = remoteTerm.startTime;
            localTerm.endTime = remoteTerm.endTime;
            localTerm.reservationId = reservation.id;
            //nullの項目は更新しない.
            jdbcManager.update(remoteTerm)
                .excludesNull()
                .execute();
        }
        //削除
        for (IIdentifiableEntity entity : result.toRemoveList) {
            Term term = (Term) entity;
            //予約削除
            if (term.reservationId != null && term.reservationId > 0) {
                cloudServiceLogic.deleteReservation(term.reservationId);
            }
            jdbcManager.delete(term)
                .execute();
        }

        List<Term> returnTermList = jdbcManager.from(Term.class)
            .where(new SimpleWhere().eq(term().applicationId(), applicationId))
            .orderBy("id")
            .getResultList();

        return returnTermList;
    }

    /**
     * <p>アプリケーションVM 起動.
     * <br>
     *
     * @param   id アプリケーションVMID
     * @return
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.POWER_OWN_APP)
    public void startApplicationVm(Long id) throws AuthorizationException, AuthenticationException {
        ApplicationVm avm = jdbcManager.from(ApplicationVm.class)
            .innerJoin(applicationVm().application())
            .id(id)
            .eager(applicationVm().application()
                .vhutUserId())
            .getSingleResult();
        if (avm == null) {
            throw new InputRuntimeException("id", String.format("Appliation(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.POWER_ALL_APP) && !avm.application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_ALL_APP.name());
        }
        cloudServiceLogic.startVm(avm.vmId);
    }

    /**
     * <p>アプリケーションVM 停止.
     * <br>
     *
     * @param   id アプリケーションVMID
     * @return
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.POWER_OWN_APP)
    public void stopApplicationVm(Long id) throws AuthenticationException, AuthorizationException {
        ApplicationVm avm = jdbcManager.from(ApplicationVm.class)
            .innerJoin(applicationVm().application())
            .id(id)
            .eager(applicationVm().application()
                .vhutUserId())
            .getSingleResult();
        if (avm == null) {
            throw new InputRuntimeException("id", String.format("Appliation(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.POWER_ALL_APP) && !avm.application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_ALL_APP.name());
        }
        cloudServiceLogic.stopVm(avm.vmId);
    }

    /**
     * アプリケーションを起動可能にする.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> ACTIVE
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>DHCPに仮想マシンのMACとPrivateIPを設定します。
     * <li>NATに仮想マシンのPrivateIPとPublicIPを設定します。
     * <li>仮想マシンにNICを追加します。
     * <li>仮想マシンにユーザを追加します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#obtainNetwork(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#addVmUser(long, long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをACTIVEに変更します。
     * </ul>
     *
     * @param id アプリケーションンのID
     * @throws ApplicationStatusException アプリケーションの状態に関する例外
     * @throws NoAvailableReservationException 予約未取得の例外
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudReservationPeriodException
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.STATUS_OWN_APP)
    public void activateApplicationById(Long id) throws ApplicationStatusException, NoAvailableReservationException, CloudReservationPeriodException, CloudReservationException, AuthenticationException, AuthorizationException {
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .eager(application().vhutUserId())
            .id(id)
            .getSingleResult();
        //バリデーション
        if (application == null) {
            throw new InputRuntimeException("id", String.format("Appliation(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.STATUS_ALL_APP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_ALL_APP.name());
        }
        //状態チェック
        if (application.status == ApplicationStatus.DEACTIVE) {
            application.status = ApplicationStatus.ACTIVE;
        } else {
            throw new ApplicationStatusException(application.status, "should be DEACTIVE");
        }

        //Termの取得
        Timestamp currentTime = TimestampUtil.getCurrentTimestamp();
        Term term = jdbcManager.from(Term.class)
            .where(new SimpleWhere().eq(term().applicationId(), id)
                .lt(term().startTime(), currentTime)
                .gt(term().endTime(), currentTime))
            .orderBy("endTime")
            .maxRows(1)
            .getSingleResult();
        if (term == null) {
            throw new NoAvailableReservationException(Application.class, id, "activateApplicationById");
        }

        //ネットワークの取得
        for (ApplicationSecurityGroup applicationSecurityGroup : application.applicationSecurityGroupList) {
            cloudServiceLogic.obtainNetwork(term.reservationId, applicationSecurityGroup.securityGroupId, serviceConfig.getExIpRequestMode());
        }

        //ユーザの割り当て
        for (ApplicationVm applicationVm : application.applicationVmList) {
            VhutUserCloudUserMap vhutUserCloudUserMap = jdbcManager.from(VhutUserCloudUserMap.class)
                .where(new SimpleWhere().eq(vhutUserCloudUserMap().vhutUserId(), application.vhutUserId)
                    .eq(vhutUserCloudUserMap().cloudId(), applicationVm.cloudId))
                .maxRows(1)
                .disallowNoResult()
                .getSingleResult();
            cloudServiceLogic.addVmUser(term.reservationId, applicationVm.vmId, vhutUserCloudUserMap.cloudUserId);
        }

        jdbcManager.update(application)
            .includes(application().status())
            .execute();
    }

    /**
     * アプリケーションを起動不可能にする.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>ACTIVE -> DEACTIVE
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンからユーザを削除します。
     * <li>仮想マシンからNICを削除します。
     * <li>DHCPから仮想マシンのMACとPrivateIPの設定を削除します。
     * <li>NATから仮想マシンのPrivateIPとPublicIPの設定を削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#stopVm(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#removeVmUser(long, long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#releaseNetwork(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをACTIVEに変更します。
     * </ul>
     *
     * @param id アプリケーションンのID
     * @throws ApplicationStatusException アプリケーションの状態異常
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.STATUS_OWN_APP)
    public void deactivateApplicationById(Long id) throws ApplicationStatusException, AuthenticationException, AuthorizationException {
        Application application = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .eager(application().vhutUserId())
            .id(id)
            .getSingleResult();
        //バリデーション
        if (application == null) {
            throw new InputRuntimeException("id", String.format("Application(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.STATUS_ALL_APP) && !application.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_ALL_APP.name());
        }
        //状態チェック
        if (application.status == ApplicationStatus.ACTIVE) {
            application.status = ApplicationStatus.DEACTIVE;
        } else {
            throw new ApplicationStatusException(application.status, "is not ACTIVE.");
        }

        //VMの停止
        for (ApplicationVm applicationVm : application.applicationVmList) {
            cloudServiceLogic.stopVm(applicationVm.vmId);
        }

        //ユーザの削除
        for (ApplicationVm applicationVm : application.applicationVmList) {
            VhutUserCloudUserMap vhutUserCloudUserMap = jdbcManager.from(VhutUserCloudUserMap.class)
                .where(new SimpleWhere().eq(vhutUserCloudUserMap().vhutUserId(), application.vhutUserId)
                    .eq(vhutUserCloudUserMap().cloudId(), applicationVm.cloudId))
                .maxRows(1)
                .getSingleResult();
            if (vhutUserCloudUserMap != null) {
                cloudServiceLogic.removeVmUser(applicationVm.vmId, vhutUserCloudUserMap.cloudUserId);
            }
        }

        //ネットワークの開放
        for (ApplicationSecurityGroup applicationSecurityGroup : application.applicationSecurityGroupList) {
            cloudServiceLogic.releaseNetwork(applicationSecurityGroup.securityGroupId);
        }

        jdbcManager.update(application)
            .includes(application().status())
            .execute();
    }

    /**
     * @param application
     * @return
     */
    private Application joinAllVms(Application application) {
        for (ApplicationVm avm : application.applicationVmList) {
            if (avm.vmId == null || avm.vmId <= 0) {
                throw new DBStateRuntimeException(avm.getClass(), avm.id);
            } else {
                avm.vm = cloudServiceLogic.getVmById(avm.vmId);
            }
        }
        return application;
    }

    /**
     * @param application
     * @return
     */
    private Application joinAllSecurityGroups(Application application) {
        for (ApplicationSecurityGroup asg : application.applicationSecurityGroupList) {
            if (asg.securityGroupId == null || asg.securityGroupId <= 0) {
                throw new DBStateRuntimeException(asg.getClass(), asg.id);
            } else {
                asg.securityGroup = cloudServiceLogic.getSecurityGroupById(asg.securityGroupId);
            }
        }
        return application;
    }

    /**
     * @param application
     * @return
     */
    private ReleasedApplication joinAllTemplates(ReleasedApplication releasedApplication) {
        for (ReleasedApplicationTemplate rat : releasedApplication.releasedApplicationTemplateList) {
            if (rat.templateId == null || rat.templateId <= 0) {
                throw new DBStateRuntimeException(rat.getClass(), rat.id);
            } else {
                rat.template = cloudServiceLogic.getTemplateById(rat.templateId);
            }
        }
        return releasedApplication;
    }

    /**
     * @param application
     * @return
     */
    private ReleasedApplication joinAllSecurityGroupTemplates(ReleasedApplication releasedApplication) {
        for (ReleasedApplicationSecurityGroupTemplate rasgt : releasedApplication.releasedApplicationSecurityGroupTemplateList) {
            if (rasgt.securityGroupTemplateId == null || rasgt.securityGroupTemplateId <= 0) {
                throw new DBStateRuntimeException(rasgt.getClass(), rasgt.id);
            } else {
                rasgt.securityGroupTemplate = cloudServiceLogic.getSecurityGroupTemplateById(rasgt.securityGroupTemplateId);
            }
        }
        return releasedApplication;
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
