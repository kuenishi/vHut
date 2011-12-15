/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import static jp.co.ntts.vhut.entity.Names.application;
import static jp.co.ntts.vhut.entity.Names.applicationInstance;
import static jp.co.ntts.vhut.entity.Names.applicationInstanceGroup;
import static jp.co.ntts.vhut.entity.Names.applicationInstanceVm;
import static jp.co.ntts.vhut.entity.Names.releasedApplication;
import static jp.co.ntts.vhut.entity.Names.vhutUserCloudUserMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.IpInfoDto;
import jp.co.ntts.vhut.dto.OrderDto;
import jp.co.ntts.vhut.dto.VhutUserDto;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceStatus;
import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
import jp.co.ntts.vhut.entity.ApplicationInstanceVmSecurityGroupMap;
import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
import jp.co.ntts.vhut.entity.ApplicationVm;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.IIdentifiableEntity;
import jp.co.ntts.vhut.entity.ReleasedApplication;
import jp.co.ntts.vhut.entity.Reservation;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMap;
import jp.co.ntts.vhut.exception.AigDeployedException;
import jp.co.ntts.vhut.exception.ApplicationInstanceStatusException;
import jp.co.ntts.vhut.exception.AuthenticationException;
import jp.co.ntts.vhut.exception.AuthorizationException;
import jp.co.ntts.vhut.exception.CloudReservationException;
import jp.co.ntts.vhut.exception.CloudReservationPeriodException;
import jp.co.ntts.vhut.exception.CloudResourceException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.exception.NoAvailableReservationException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.EntityUtil;
import jp.co.ntts.vhut.util.OrderUtil;
import jp.co.ntts.vhut.util.TimestampUtil;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>アプリケーションインスタンスグループのサービスクラス.
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

@Generated(value = { "S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl" }, date = "2011/07/15 4:21:15")
public class ApplicationInstanceGroupService extends AbstractService<ApplicationInstanceGroup> {

    /**
     * CloudLogicを取得するためのファクトリ
     */
    public CloudLogicFactory cloudLogicFactory;

    /** ユーザセッション情報 */
    public VhutUserDto vhutUserDto;

    /**
     * クラウドの識別子
     */
    protected long cloudId = 1;

    /**
     * クラウドロジックのサービス面
     */
    protected ICloudServiceLogic cloudServiceLogic;

    /** サービス系の設定値. */
    public ServiceConfig serviceConfig;


    @InitMethod
    public void init() {
        cloudServiceLogic = cloudLogicFactory.newCloudServiceLogic(cloudId);
    }

    /**
     * <p>アプリケーションインスタンスグループ概要一覧取得.
     * <br>
     *
     * @param なし
     * @return ApplicationInstanceGroup アプリケーションインスタンスグループ概要情報のリスト
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_AIG)
    public List<ApplicationInstanceGroup> getAllApplicationInstanceGroupAbstractionList() throws AuthenticationException {
        //認可系の処理
        SimpleWhere where = new SimpleWhere();
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_AIG)) {
            where = new SimpleWhere().eq(applicationInstanceGroup().vhutUserId(), vhutUserDto.getVhutUser().id);
        }
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<ApplicationInstanceGroup> results = jdbcManager.from(ApplicationInstanceGroup.class)
            .innerJoin(applicationInstanceGroup().application())
            .innerJoin(applicationInstanceGroup().applicationInstanceList(), false)
            .where(where.ne(applicationInstanceGroup().applicationInstanceList()
                .status(), ApplicationInstanceStatus.DELETED))
            .getResultList();
        return results;
    }

    /**
     * <p>アプリケーション関連アプリケーションインスタンスグループ概要一覧取得.
     * <br>
     *
     * @param id アプリケーションID
     * @return ApplicationInstanceGroup アプリケーションインスタンスグループ概要情報のリスト
     */
    public List<ApplicationInstanceGroup> getApplicationInstanceGroupAbstractionListByApplicationId(Long id) {
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<ApplicationInstanceGroup> applicationInstanceGroupList = jdbcManager.from(ApplicationInstanceGroup.class)
            .innerJoin(applicationInstanceGroup().application())
            .innerJoin(applicationInstanceGroup().applicationInstanceList(), false)
            .where(new SimpleWhere().eq(applicationInstanceGroup().applicationId(), id)
                .ne(applicationInstanceGroup().applicationInstanceList()
                    .status(), ApplicationInstanceStatus.DELETED))
            .getResultList();
        return applicationInstanceGroupList;
    }

    /**
     * <p>ユーザ関連アプリケーションインスタンスグループ概要一覧取得.
     * <br>
     *
     * @param id ユーザID
     * @return ApplicationInstanceGroup アプリケーションインスタンスグループ概要情報のリスト
     */
    public List<ApplicationInstanceGroup> getApplicationInstanceGroupAbstractionListByUserId(Long id) {
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<ApplicationInstanceGroup> applicationInstanceGroupList = jdbcManager.from(ApplicationInstanceGroup.class)
            .innerJoin(applicationInstanceGroup().application())
            .innerJoin(applicationInstanceGroup().applicationInstanceList(), false)
            .where(new SimpleWhere().eq(applicationInstanceGroup().vhutUserId(), id)
                .ne(applicationInstanceGroup().applicationInstanceList()
                    .status(), ApplicationInstanceStatus.DELETED))
            .getResultList();
        return applicationInstanceGroupList;
    }

    /**
     * <p>アプリケーションインスタンスグループ詳細取得.
     * <br>
     *
     * @param id アプリケーションインスタンスグループID
     * @return ApplicationInstanceGroup アプリケーションインスタンスグループ詳細情報
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_AIG)
    public ApplicationInstanceGroup getApplicationInstanceGroupById(Long id) throws AuthenticationException, AuthorizationException {
        //引数のアプリケーションIDを条件に、セレクトする。
        ApplicationInstanceGroup applicationInstanceGroup =
                jdbcManager.from(ApplicationInstanceGroup.class)
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList())
                    .innerJoin(applicationInstanceGroup().vhutUser())
                    .innerJoin(applicationInstanceGroup().application())
                    .innerJoin(applicationInstanceGroup().applicationInstanceList()
                        .vhutUser())
                    .eager(applicationInstanceGroup().applicationId(), applicationInstanceGroup().deleteTime(), applicationInstanceGroup().endTime(), applicationInstanceGroup().password(), applicationInstanceGroup().reservationIdToCreate(), applicationInstanceGroup().reservationIdToStart(),
                            applicationInstanceGroup().vhutUserId(), applicationInstanceGroup().application()
                                .structure(), applicationInstanceGroup().application()
                                .vhutUserId(), applicationInstanceGroup().application()
                                .reservationId(), applicationInstanceGroup().vhutUser()
                                .email())
                    .id(id)
                    .getSingleResult();
        if (applicationInstanceGroup == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstanceGroup(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_AIG) && !applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.READ_ALL_AIG.name());
        }
        //認可系の処理 インスタンスを落とす。
        if (vhutUserDto.isAuthorized(Right.READ_ALL_AI)) {

        } else if (vhutUserDto.isAuthorized(Right.READ_CHILD_AI)) {
            if (!applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
                applicationInstanceGroup.applicationInstanceList = new ArrayList<ApplicationInstance>();
            }
        } else if (vhutUserDto.isAuthorized(Right.READ_OWN_AI)) {
            List<ApplicationInstance> removeList = new ArrayList<ApplicationInstance>();
            for (ApplicationInstance ai : applicationInstanceGroup.applicationInstanceList) {
                if (!ai.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
                    removeList.add(ai);
                }
            }
            applicationInstanceGroup.applicationInstanceList.removeAll(removeList);
        } else {
            applicationInstanceGroup.applicationInstanceList = new ArrayList<ApplicationInstance>();
        }
        //セレクトした値が取得出来た場合、値をreturnする。
        return applicationInstanceGroup;
    }

    /**
     * アプリケーションインスタンスグループに属するすべてのVMのIP情報を取得します.
     * @param id アプリケーションインスタンスグループのID
     * @return アプリケーションインスタンスグループに属するすべてのVMのIP情報
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.READ_OWN_AIG)
    public List<IpInfoDto> getIpInfoListByApplicationInstanceGroupId(Long id) throws AuthenticationException, AuthorizationException {
        //引数のアプリケーションIDを条件に、セレクトする。
        ApplicationInstanceGroup applicationInstanceGroup = jdbcManager.from(ApplicationInstanceGroup.class)
            .leftOuterJoin(applicationInstanceGroup().applicationInstanceList())
            .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                .vhutUser())
            .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                .applicationInstanceVmList())
            .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                .applicationInstanceSecurityGroupList())
            .innerJoin(applicationInstanceGroup().vhutUser())
            .eager(applicationInstanceGroup().vhutUserId(), applicationInstanceGroup().applicationInstanceList()
                .vhutUserId())
            .id(id)
            .getSingleResult();
        if (applicationInstanceGroup == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstanceGroup(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_AIG) && !applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.READ_ALL_AIG.name());
        }
        List<IpInfoDto> ipInfoDtos = new ArrayList<IpInfoDto>();

        for (ApplicationInstance ai : applicationInstanceGroup.applicationInstanceList) {
            joinAllVms(ai);
            joinAllSecurityGroups(ai);
            ipInfoDtos.addAll(IpInfoDto.createIpIfoDtos(ai));
        }

        for (IpInfoDto ipInfoDto : ipInfoDtos) {
            ipInfoDto.applicationInstanceGroupId = applicationInstanceGroup.id;
            ipInfoDto.applicationInstanceGroupName = applicationInstanceGroup.name;
        }

        return ipInfoDtos;
    }

    /**
     * <p>アプリケーションインスタンスグループ詳細追加.
     * このメソッドによるApplicationInstance状態遷移
     * <ul>
     * <li>NONE
     * </ul>
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createReservation(OrderDto)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationInstanceを作成します。
     * </ul>
     *
     * @param remoteAig アプリケーションインスタンスグループ詳細情報
     * @return アプリケーションインスタンスグループ
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.CREATE_OWN_AIG)
    public ApplicationInstanceGroup createApplicationInstanceGroup(ApplicationInstanceGroup remoteAig) throws CloudResourceException, AuthenticationException, AuthorizationException {
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.CREATE_ALL_AIG) && !remoteAig.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.CREATE_ALL_AIG.name());
        }
        ApplicationInstanceGroup localAig = new ApplicationInstanceGroup();
        //コピー
        localAig.name = remoteAig.name;
        localAig.password = remoteAig.password;
        localAig.startTime = remoteAig.startTime;
        localAig.endTime = remoteAig.endTime;
        localAig.deleteTime = remoteAig.deleteTime;
        localAig.applicationId = remoteAig.applicationId;
        localAig.vhutUserId = remoteAig.vhutUserId;
        localAig.applicationInstanceList = remoteAig.applicationInstanceList;
        //参照するApplicationの取得
        Application app = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .id(remoteAig.applicationId)
            .getSingleResult();

        if (app == null) {
            throw new InputRuntimeException("remoteAig", String.format("Application(id=%d) was not found", remoteAig.applicationId));
        }

        localAig.application = app;

        joinAllVms(localAig);
        joinAllSecurityGroups(localAig);

        //AIG作成に使用する予約を追加する。
        Reservation reservationToCreate = cloudServiceLogic.createReservation(OrderUtil.createOrderToCreateApplicationInstanceGroup(localAig));
        //AIG起動に使用する予約を追加する。
        Reservation reservationToStart = cloudServiceLogic.createReservation(OrderUtil.createOrderToStart(localAig, serviceConfig.getExIpRequestMode()));
        //リソース予約した際の予約IDをアプリケーションインスタンスグループに追加する。
        localAig.reservationIdToCreate = reservationToCreate.id;
        localAig.reservationIdToStart = reservationToStart.id;

        //挿入
        jdbcManager.insert(localAig)
            .execute();

        for (ApplicationInstance remoteAi : remoteAig.applicationInstanceList) {
            ApplicationInstance localAi = new ApplicationInstance();
            //コピー
            localAi.vhutUserId = remoteAi.vhutUserId;
            //セット
            localAi.applicationInstanceGroupId = localAig.id;
            localAi.status = ApplicationInstanceStatus.NONE;
            jdbcManager.insert(localAi)
                .execute();
        }

        localAig =
                jdbcManager.from(ApplicationInstanceGroup.class)
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList())
                    .innerJoin(applicationInstanceGroup().vhutUser())
                    .innerJoin(applicationInstanceGroup().application())
                    .eager(applicationInstanceGroup().applicationId(), applicationInstanceGroup().deleteTime(), applicationInstanceGroup().endTime(), applicationInstanceGroup().password(), applicationInstanceGroup().reservationIdToCreate(), applicationInstanceGroup().reservationIdToStart(),
                            applicationInstanceGroup().vhutUserId(), applicationInstanceGroup().application()
                                .structure(), applicationInstanceGroup().application()
                                .vhutUserId(), applicationInstanceGroup().application()
                                .reservationId(), applicationInstanceGroup().vhutUser()
                                .email())
                    .id(localAig.id)
                    .disallowNoResult()
                    .getSingleResult();

        return localAig;
    }

    /**
     * <p>アプリケーションインスタンスグループ詳細更新.
     * このメソッドによるApplicationInstance状態遷移
     * <ul>
     * <li>NONE
     * </ul>
     * <p>
     * このメソッドは環境に変更を加えません。
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#createReservation(OrderDto)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationInstanceを作成します。
     * </ul>
     *
     * @param remoteAig アプリケーションインスタンスグループ詳細情報
     * @return アプリケーションインスタンスグループ
     * @throws ApplicationInstanceStatusException Aiの状態不正
     * @throws AigDeployedException Aigが既に開始時間を過ぎており要求された処理を実行できない
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.UPDATE_OWN_AIG)
    public ApplicationInstanceGroup updateApplicationInstanceGroup(ApplicationInstanceGroup remoteAig) throws ApplicationInstanceStatusException, AigDeployedException, CloudResourceException, AuthorizationException, AuthenticationException {

        Timestamp currentTime = TimestampUtil.getCurrentTimestamp();

        ApplicationInstanceGroup localAig =
                jdbcManager.from(ApplicationInstanceGroup.class)
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList())
                    .eager(applicationInstanceGroup().applicationId(), applicationInstanceGroup().deleteTime(), applicationInstanceGroup().endTime(), applicationInstanceGroup().password(), applicationInstanceGroup().reservationIdToCreate(), applicationInstanceGroup().reservationIdToStart(),
                            applicationInstanceGroup().vhutUserId())
                    .id(remoteAig.id)
                    .getSingleResult();

        if (localAig == null) {
            throw new InputRuntimeException("remoteAig", String.format("ApplicationInstanceGroup(id=%d) was not found", remoteAig.id));
        }

        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.UPDATE_OWN_AIG) && !localAig.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.UPDATE_OWN_AIG.name());
        }

        //ApplicationInstanceについて過不足を確認
        //EntityUtilを使って追加、削除、更新に振り分け
        EntityUtil.SortOutResult aiResult = EntityUtil.sortOutToSync(localAig.applicationInstanceList.toArray(new IIdentifiableEntity[]{}), remoteAig.applicationInstanceList.toArray(new IIdentifiableEntity[]{}));

        //コピー
        localAig.name = remoteAig.name;
        localAig.password = remoteAig.password;
        localAig.startTime = remoteAig.startTime;
        localAig.endTime = remoteAig.endTime;
        localAig.deleteTime = remoteAig.deleteTime;
        localAig.vhutUserId = remoteAig.vhutUserId;
        if (!localAig.applicationId.equals(remoteAig.applicationId)) {
            if (currentTime.before(TimestampUtil.subtract(localAig.startTime, 1, TimestampUtil.Unit.DAY))) {
                //アプリケーションの変更は研修開始の1日前まで
                localAig.applicationId = remoteAig.applicationId;
            } else {
                throw new AigDeployedException(localAig.id);
            }
        }

        //参照するApplicationの取得
        Application app = jdbcManager.from(Application.class)
            .leftOuterJoin(application().applicationVmList())
            .leftOuterJoin(application().applicationVmList()
                .applicationVmSecurityGroupMapList())
            .leftOuterJoin(application().applicationSecurityGroupList())
            .leftOuterJoin(application().applicationSecurityGroupList()
                .applicationVmSecurityGroupMapList())
            .id(remoteAig.applicationId)
            .disallowNoResult()
            .getSingleResult();

        localAig.application = app;

        localAig.applicationInstanceList = remoteAig.applicationInstanceList;

        joinAllVms(localAig);
        joinAllSecurityGroups(localAig);

        //AIG作成に使用する予約を追加する。
        Reservation reservationToCreate = cloudServiceLogic.updateReservation(localAig.reservationIdToCreate, OrderUtil.createOrderToCreateApplicationInstanceGroup(localAig));
        //AIG起動に使用する予約を追加する。
        Reservation reservationToStart = cloudServiceLogic.updateReservation(localAig.reservationIdToStart, OrderUtil.createOrderToStart(localAig, serviceConfig.getExIpRequestMode()));
        //リソース予約した際の予約IDをアプリケーションインスタンスグループに追加する。
        localAig.reservationIdToCreate = reservationToCreate.id;
        localAig.reservationIdToStart = reservationToStart.id;

        //更新
        jdbcManager.update(localAig)
            .includes(applicationInstanceGroup().name(), applicationInstanceGroup().password(), applicationInstanceGroup().startTime(), applicationInstanceGroup().endTime(), applicationInstanceGroup().deleteTime(), applicationInstanceGroup().vhutUserId(),
                    applicationInstanceGroup().applicationId(), applicationInstanceGroup().reservationIdToCreate(), applicationInstanceGroup().reservationIdToStart())
            .execute();

        //追加
        for (IIdentifiableEntity entity : aiResult.toAddList) {
            ApplicationInstance remoteAi = (ApplicationInstance) entity;
            ApplicationInstance localAi = new ApplicationInstance();
            //コピー
            localAi.vhutUserId = remoteAi.vhutUserId;
            //セット
            localAi.applicationInstanceGroupId = localAig.id;
            localAi.status = ApplicationInstanceStatus.NONE;
            jdbcManager.insert(localAi)
                .execute();
        }

        //更新
        for (EntityUtil.ToUpdateValue value : aiResult.toUpdateList) {
            ApplicationInstance remoteAi = (ApplicationInstance) value.remote;
            ApplicationInstance localAi = (ApplicationInstance) value.local;
            if (!(localAi.status == ApplicationInstanceStatus.DEACTIVE || localAi.status == ApplicationInstanceStatus.NONE)) {
                throw new ApplicationInstanceStatusException(localAi.status, "should be DEACTIVE");
            }
            localAi.vhutUserId = remoteAi.vhutUserId;
            jdbcManager.update(localAi)
                .excludesNull()
                .execute();
        }

        //削除
        for (IIdentifiableEntity entity : aiResult.toRemoveList) {
            ApplicationInstance localAi = (ApplicationInstance) entity;
            if (!(localAi.status == ApplicationInstanceStatus.DEACTIVE || localAi.status == ApplicationInstanceStatus.NONE)) {
                throw new ApplicationInstanceStatusException(localAi.status, "should be DEACTIVE");
            }
            localAi.status = ApplicationInstanceStatus.DELETING;
            //削除処理
            ApplicationInstance applicationInstance = jdbcManager.from(ApplicationInstance.class)
                .leftOuterJoin(applicationInstance().applicationInstanceVmList())
                .leftOuterJoin(applicationInstance().applicationInstanceVmList()
                    .applicationInstanceVmSecurityGroupMapList())
                .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
                .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList()
                    .applicationInstanceVmSecurityGroupMapList())
                .id(localAi.id)
                .getSingleResult();

            //NetworkAdapterの削除
            for (ApplicationInstanceVm applicationInstanceVm : applicationInstance.applicationInstanceVmList) {
                for (ApplicationInstanceVmSecurityGroupMap map : applicationInstanceVm.applicationInstanceVmSecurityGroupMapList) {
                    cloudServiceLogic.deleteNetworkAdapter(map.applicationInstanceVm.vmId, map.applicationInstanceSecurityGroup.securityGroupId);
                }
            }
            //SecurityGroupの削除
            for (ApplicationInstanceSecurityGroup applicationInstnaceSecurityGroup : applicationInstance.applicationInstanceSecurityGroupList) {
                cloudServiceLogic.deleteSecurityGroup(applicationInstnaceSecurityGroup.securityGroupId);
            }
            //Vmの削除
            for (ApplicationInstanceVm applicationInstanceVm : applicationInstance.applicationInstanceVmList) {
                cloudServiceLogic.deleteVm(applicationInstanceVm.vmId);
            }
        }

        localAig =
                jdbcManager.from(ApplicationInstanceGroup.class)
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList())
                    .innerJoin(applicationInstanceGroup().vhutUser())
                    .innerJoin(applicationInstanceGroup().application())
                    .eager(applicationInstanceGroup().applicationId(), applicationInstanceGroup().deleteTime(), applicationInstanceGroup().endTime(), applicationInstanceGroup().password(), applicationInstanceGroup().reservationIdToCreate(), applicationInstanceGroup().reservationIdToStart(),
                            applicationInstanceGroup().vhutUserId(), applicationInstanceGroup().application()
                                .structure(), applicationInstanceGroup().application()
                                .vhutUserId(), applicationInstanceGroup().application()
                                .reservationId(), applicationInstanceGroup().vhutUser()
                                .email(), applicationInstanceGroup().applicationInstanceList()
                                .vhutUserId())
                    .id(localAig.id)
                    .disallowNoResult()
                    .getSingleResult();

        return localAig;
    }

    /**
     * <p>アプリケーションインスタンスグループ詳細一括追加.
     * <br>
     *
     * @param applicationInstanceGroupList アプリケーションインスタンスグループ詳細情報のリスト
     * @return
     * @throws CloudResourceException クラウド側のリソース不足による例外
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.CREATE_OWN_AIG)
    public void createApplicationInstanceGroupList(List<ApplicationInstanceGroup> applicationInstanceGroupList) throws CloudResourceException, AuthenticationException, AuthorizationException {

        List<OrderDto> orderDtoList = new ArrayList<OrderDto>();

        for (ApplicationInstanceGroup remoteAig : applicationInstanceGroupList) {
            //認可系の処理
            if (!vhutUserDto.isAuthorized(Right.CREATE_ALL_AIG) && !remoteAig.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
                throw new AuthorizationException(vhutUserDto.getAccount(), Right.CREATE_ALL_AIG.name());
            }
            //参照するApplicationの取得
            Application app = jdbcManager.from(Application.class)
                .leftOuterJoin(application().applicationVmList())
                .leftOuterJoin(application().applicationVmList()
                    .applicationVmSecurityGroupMapList())
                .leftOuterJoin(application().applicationSecurityGroupList())
                .leftOuterJoin(application().applicationSecurityGroupList()
                    .applicationVmSecurityGroupMapList())
                .id(remoteAig.applicationId)
                .getSingleResult();

            if (app == null) {
                throw new InputRuntimeException("applicationInstanceGroupList", String.format("Application(id=%d) was not found", remoteAig.applicationId));
            }

            remoteAig.application = app;

            joinAllVms(remoteAig);
            joinAllSecurityGroups(remoteAig);

            orderDtoList.add(OrderUtil.createOrderToCreateApplicationInstanceGroup(remoteAig));
            orderDtoList.add(OrderUtil.createOrderToStart(remoteAig, serviceConfig.getExIpRequestMode()));
        }

        //リソース予約一括追加。
        List<Reservation> reservationList = cloudServiceLogic.createReservationList(orderDtoList);
        //
        for (int i = 0; i < applicationInstanceGroupList.size(); i++) {
            ApplicationInstanceGroup remoteAig = applicationInstanceGroupList.get(i);
            ApplicationInstanceGroup localAig = new ApplicationInstanceGroup();
            //コピー
            localAig.name = remoteAig.name;
            localAig.password = remoteAig.password;
            localAig.startTime = remoteAig.startTime;
            localAig.endTime = remoteAig.endTime;
            localAig.deleteTime = remoteAig.deleteTime;
            localAig.applicationId = remoteAig.applicationId;
            localAig.vhutUserId = remoteAig.vhutUserId;
            localAig.applicationInstanceList = remoteAig.applicationInstanceList;
            localAig.reservationIdToCreate = reservationList.get(2 * i).id;
            localAig.reservationIdToStart = reservationList.get(2 * i + 1).id;
            //挿入
            jdbcManager.insert(localAig)
                .execute();
            //ApplicationInstanceの作成
            for (ApplicationInstance remoteAi : remoteAig.applicationInstanceList) {
                ApplicationInstance localAi = new ApplicationInstance();
                //コピー
                localAi.vhutUserId = remoteAi.vhutUserId;
                //セット
                localAi.applicationInstanceGroupId = localAig.id;
                localAi.status = ApplicationInstanceStatus.NONE;
                //挿入
                jdbcManager.insert(localAi)
                    .execute();
            }
        }
    }

    /**
     * <p>アプリケーションインスタンスグループ削除.
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> DELETING
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンを削除します。
     * <li>セキュリティグループを削除します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteVm(long)}
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#deleteSecurityGroup(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをACTIVEに変更します。
     * </ul>
     *
     * @param id アプリケーションインスタンスグループID
     * @throws ApplicationInstanceStatusException 状態不正
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.DELETE_OWN_AIG)
    public void deleteApplicationInstanceGroupById(Long id) throws ApplicationInstanceStatusException, AuthenticationException, AuthorizationException {

        //削除判定条件(and条件)
        //ApplicationInstanceGroupに関連する ApplicationInstanceが存在しない。
        ApplicationInstanceGroup aig =
                jdbcManager.from(ApplicationInstanceGroup.class)
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList())
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                        .applicationInstanceVmList())
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                        .applicationInstanceVmList()
                        .applicationInstanceVmSecurityGroupMapList())
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                        .applicationInstanceSecurityGroupList())
                    .leftOuterJoin(applicationInstanceGroup().applicationInstanceList()
                        .applicationInstanceSecurityGroupList()
                        .applicationInstanceVmSecurityGroupMapList())
                    .eager(applicationInstanceGroup().applicationId(), applicationInstanceGroup().deleteTime(), applicationInstanceGroup().endTime(), applicationInstanceGroup().password(), applicationInstanceGroup().reservationIdToCreate(), applicationInstanceGroup().reservationIdToStart(),
                            applicationInstanceGroup().vhutUserId())
                    .id(id)
                    .getSingleResult();

        if (aig == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstanceGroup(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.DELETE_ALL_AIG) && !aig.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.DELETE_ALL_AIG.name());
        }
        for (ApplicationInstance ai : aig.applicationInstanceList) {
            if (ai.status == ApplicationInstanceStatus.DEACTIVE || ai.status == ApplicationInstanceStatus.NONE) {
                ai.status = ApplicationInstanceStatus.DELETING;
            } else {
                throw new ApplicationInstanceStatusException(ai.status, "should be DEACTIVE or NONE");
            }

            //NetworkAdapterの削除
            for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                for (ApplicationInstanceVmSecurityGroupMap map : aivm.applicationInstanceVmSecurityGroupMapList) {
                    cloudServiceLogic.deleteNetworkAdapter(map.applicationInstanceVm.vmId, map.applicationInstanceSecurityGroup.securityGroupId);
                    jdbcManager.delete(map)
                        .execute();
                }
            }
            //SecurityGroupの削除
            for (ApplicationInstanceSecurityGroup aisg : ai.applicationInstanceSecurityGroupList) {
                cloudServiceLogic.deleteSecurityGroup(aisg.securityGroupId);
                jdbcManager.delete(aisg)
                    .execute();
            }
            //Vmの削除
            for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
                cloudServiceLogic.deleteVm(aivm.vmId);
            }

            jdbcManager.update(ai)
                .includes(applicationInstance().status())
                .execute();
        }
        //予約を削除する。
        cloudServiceLogic.deleteReservation(aig.reservationIdToCreate);
        cloudServiceLogic.deleteReservation(aig.reservationIdToStart);

    }

    /**
     * <p>アプリケーションインスタンスグループ関連アプリケーションインスタンス概要一覧取得.
     * <br>
     *
     * @param id アプリケーションインスタンスグループID
     * @return ApplicationInstance アプリケーションインスタンス概要情報のリスト
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_AI)
    public List<ApplicationInstance> getAllApplicationInstanceAbstractionListByApplicationInstanceGroupId(Long id) throws AuthenticationException {
        //認可系の処理
        SimpleWhere where = new SimpleWhere();
        if (!vhutUserDto.isAuthorized(Right.READ_CHILD_AI)) {
            where = new SimpleWhere().eq(applicationInstance().vhutUserId(), vhutUserDto.getVhutUser().id);
        } else if (!vhutUserDto.isAuthorized(Right.READ_ALL_AI)) {
            where = new SimpleWhere().eq(applicationInstance().applicationInstanceGroup()
                .vhutUserId(), vhutUserDto.getVhutUser().id);
        }
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<ApplicationInstance> aiList = jdbcManager.from(ApplicationInstance.class)
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .innerJoin(applicationInstance().applicationInstanceGroup()
                .application())
            .innerJoin(applicationInstance().vhutUser())
            .where(where.eq(applicationInstance().applicationInstanceGroupId(), id))
            .orderBy("id")
            .getResultList();
        return aiList;
    }

    /**
     * <p>アプリケーションインスタンス詳細取得.
     * <br>
     *
     * @param id アプリケーションインスタンスグループID
     * @return ApplicationInstance アプリケーションインスタンス詳細情報
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_AI)
    public ApplicationInstance getApplicationInstanceById(Long id) throws AuthenticationException, AuthorizationException {
        ApplicationInstance applicationInstance = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList()
                .applicationInstanceVmSecurityGroupMapList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList()
                .applicationInstanceVmSecurityGroupMapList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .innerJoin(applicationInstance().applicationInstanceGroup()
                .application())
            .innerJoin(applicationInstance().vhutUser())
            .eager(applicationInstance().releasedApplicationId())
            .id(id)
            .getSingleResult();

        if (applicationInstance == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstance(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_AI) && !applicationInstance.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.READ_ALL_AI.name());
        }

        joinAllVms(applicationInstance);
        joinAllSecurityGroups(applicationInstance);

        //リリースがある場合フェッチする。
        if (applicationInstance.releasedApplicationId != null) {
            ReleasedApplication rapp = jdbcManager.from(ReleasedApplication.class)
                .id(applicationInstance.releasedApplicationId)
                .eager(releasedApplication().structure())
                .getSingleResult();
            if (rapp != null) {
                applicationInstance.releasedApplication = rapp;
            }
        }

        return applicationInstance;
    }

    /**
     * <p>アプリケーションインスタンス再作成.
     * <p>
     * このメソッドによる状態遷移
     * <ul>
     * <li>DEACTIVE -> REBUILDING
     * </ul>
     * <p>
     * このメソッドは環境に以下の変更を加えます。
     * <ul>
     * <li>仮想マシンを再作成します。
     * </ul>
     * <p>
     * このメソッドはCloudLogicの以下のIFを使用します。
     * <ul>
     * <li>{@link jp.co.ntts.vhut.logic.ICloudServiceLogic#rebuildVm(long)}
     * </ul>
     * <p>
     * このメソッドはDBに以下の変更を加えます。
     * <ul>
     * <li>ApplicationのstatusをREBUILDINGに変更します。
     * </ul>
     *
     * @param id アプリケーションインスタンスグループID
     * @throws ApplicationInstanceStatusException 状態不正
     * @throws NoAvailableReservationException 予約不正
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.REBUILD_OWN_AI)
    public void rebuildApplicationInstanceById(Long id) throws ApplicationInstanceStatusException, NoAvailableReservationException, AuthenticationException, AuthorizationException {
        //ApplicationInstanceの存在に関するValidation
        ApplicationInstance applicationInstance = jdbcManager.from(ApplicationInstance.class)
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .eager(applicationInstance().applicationInstanceGroup()
                .endTime())
            .id(id)
            .getSingleResult();

        if (applicationInstance == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstance(id=%d) was not found", id));
        }
        //認可系の処理
        if (!applicationInstance.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

            if (!applicationInstance.applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

                if (!vhutUserDto.isAuthorized(Right.REBUILD_ALL_AI)) {
                    throw new AuthorizationException(vhutUserDto.getAccount(), Right.REBUILD_ALL_AI.name());
                }

            } else if (!vhutUserDto.isAuthorized(Right.REBUILD_CHILD_AI)) {
                throw new AuthorizationException(vhutUserDto.getAccount(), Right.REBUILD_CHILD_AI.name());
            }

        } else if (!vhutUserDto.isAuthorized(Right.REBUILD_OWN_AI)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.REBUILD_OWN_AI.name());
        }
        //ApplicationInstanceのStatusに関するValidation
        if (applicationInstance.status == ApplicationInstanceStatus.DEACTIVE) {
            applicationInstance.status = ApplicationInstanceStatus.REBUILDING;
        } else {
            throw new ApplicationInstanceStatusException(applicationInstance.status, "should be DEACTIVE");
        }

        //現在時刻の取得
        Timestamp currentTime = TimestampUtil.getCurrentTimestamp();

        //削除条件:アプリケーションインスタンスグループの利用期間内であることを確認
        if (applicationInstance.applicationInstanceGroup.startTime.after(currentTime) || applicationInstance.applicationInstanceGroup.endTime.before(currentTime)) {
            throw new NoAvailableReservationException(ApplicationInstance.class, applicationInstance.id, "rebuildApplicationInstanceById");
        }

        //再作成を依頼
        for (ApplicationInstanceVm applicationInstanceVm : applicationInstance.applicationInstanceVmList) {
            cloudServiceLogic.rebuildVm(applicationInstanceVm.vmId);
        }

        //状態更新（楽観ロック）
        jdbcManager.update(applicationInstance)
            .includes(applicationInstance().status())
            .execute();
    }

    /**
     * <p>ユーザ関連アプリケーションインスタンス概要一覧取得.
     * <br>
     *
     * @param id ユーザID
     * @return ApplicationInstance アプリケーションインスタンス概要情報のリスト
     */
    public List<ApplicationInstance> getAllApplicationInstanceAbstractionListByUserId(Long id) {
        //引数であるidでApplicationInstance.vhutUserIdをキーに
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<ApplicationInstance> aiList = jdbcManager.from(ApplicationInstance.class)
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .innerJoin(applicationInstance().applicationInstanceGroup()
                .application())
            .innerJoin(applicationInstance().vhutUser())
            .where(new SimpleWhere().eq(applicationInstance().vhutUserId(), id)
                .ne(applicationInstance().status(), ApplicationInstanceStatus.DELETED))
            .orderBy("id")
            .getResultList();
        return aiList;
    }

    /**
     * <p>アプリケーションインスタンスコマンド再実行.
     * <br>
     *
     * @param commandId コマンドID
     * @return Command コマンド詳細情報
     */
    @Auth(right = Right.COMMAND_OWN_AI)
    public Command retryCommand(Long commandId) {
        return cloudServiceLogic.retryCommand(commandId);
    }

    /**
     * <p>アプリケーションインスタンスコマンドキャンセル.
     * <br>
     *
     * @param commandId コマンドID
     * @return Command コマンド詳細情報
     */
    @Auth(right = Right.COMMAND_OWN_AI)
    public Command cancelCommand(Long commandId) {
        return cloudServiceLogic.cancelCommand(commandId);
    }

    /**
     * <p>アプリケーションインスタンスVM 起動.
     * <br>
     *
     * @param   id アプリケーションVMID
     * @return
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.POWER_OWN_AI)
    public void startApplicationInstanceVm(Long id) throws AuthenticationException, AuthorizationException {
        ApplicationInstanceVm aivm = jdbcManager.from(ApplicationInstanceVm.class)
            .innerJoin(applicationInstanceVm().applicationInstance())
            .innerJoin(applicationInstanceVm().applicationInstance()
                .applicationInstanceGroup())
            .id(id)
            .getSingleResult();
        if (aivm == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstanceVm(id=%d) was not found", id));
        }
        //認可系の処理
        if (!aivm.applicationInstance.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

            if (!aivm.applicationInstance.applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

                if (!vhutUserDto.isAuthorized(Right.POWER_ALL_AI)) {
                    throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_ALL_AI.name());
                }

            } else if (!vhutUserDto.isAuthorized(Right.POWER_CHILD_AI)) {
                throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_CHILD_AI.name());
            }

        } else if (!vhutUserDto.isAuthorized(Right.POWER_OWN_AI)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_OWN_AI.name());
        }
        cloudServiceLogic.startVm(aivm.vmId);
    }

    /**
     * <p>アプリケーションインスタンスVM 停止.
     * <br>
     *
     * @param   id アプリケーションVMID
     * @return
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    public void stopApplicationInstanceVm(Long id) throws AuthenticationException, AuthorizationException {
        ApplicationInstanceVm aivm = jdbcManager.from(ApplicationInstanceVm.class)
            .innerJoin(applicationInstanceVm().applicationInstance())
            .innerJoin(applicationInstanceVm().applicationInstance()
                .applicationInstanceGroup())
            .id(id)
            .getSingleResult();
        if (aivm == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstanceVm(id=%d) was not found", id));
        }
        //認可系の処理
        if (!aivm.applicationInstance.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

            if (!aivm.applicationInstance.applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

                if (!vhutUserDto.isAuthorized(Right.POWER_ALL_AI)) {
                    throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_ALL_AI.name());
                }

            } else if (!vhutUserDto.isAuthorized(Right.POWER_CHILD_AI)) {
                throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_CHILD_AI.name());
            }

        } else if (!vhutUserDto.isAuthorized(Right.POWER_OWN_AI)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.POWER_OWN_AI.name());
        }
        cloudServiceLogic.stopVm(aivm.vmId);
    }

    /**
     * <p>アプリケーションインスタンス関連コマンド概要一覧取得.
     * <br>
     *
     * @param   applicationInstanceId アプリケーションインスタンスID
     * @return  @return Command コマンド概要情報のリスト
     */
    public List<Command> getUnfinishedCommandAbstractionListByApplicationInstanceId(Long applicationInstanceId) {
        // 引数のアプリケーションIDを条件に、セレクトする。
        ApplicationInstance applicationInstance = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .id(applicationInstanceId)
            .getSingleResult();

        if (applicationInstance == null) {
            throw new InputRuntimeException("applicationInstanceId", String.format("ApplicationInstance(id=%d) was not found", applicationInstanceId));
        }

        // セレクトした値が取得出来た場合、コマンドをセレクトする。
        List<Command> commandList = new ArrayList<Command>();
        for (ApplicationInstanceVm aivm : applicationInstance.applicationInstanceVmList) {
            commandList.addAll(cloudServiceLogic.getCommandListByVmId(aivm.vmId));
        }
        return commandList;
    }

    /**
     * アプリケーションインスタンスを起動可能にする.
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
     * <li>ApplicationInstanceのstatusをACTIVEに変更します。
     * </ul>
     *
     * @param id アプリケーションインスタンスのID
     * @throws ApplicationInstanceStatusException アプリケーションインスタンスの状態に関する例外
     * @throws NoAvailableReservationException 予約未取得の例外
     * @throws CloudReservationException 対象予約のリソース不足による例外
     * @throws CloudReservationPeriodException
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.STATUS_OWN_AI)
    public void activateApplicationInstanceById(Long id) throws ApplicationInstanceStatusException, NoAvailableReservationException, CloudReservationPeriodException, CloudReservationException, AuthenticationException, AuthorizationException {
        ApplicationInstance applicationInstance = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList()
                .applicationInstanceVmSecurityGroupMapList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList()
                .applicationInstanceVmSecurityGroupMapList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .eager(applicationInstance().applicationInstanceGroup()
                .endTime())
            .eager(applicationInstance().applicationInstanceGroup()
                .reservationIdToStart())
            .eager(applicationInstance().applicationInstanceGroup()
                .vhutUserId())
            .id(id)
            .getSingleResult();
        //バリデーション
        if (applicationInstance == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstance(id=%d) was not found", id));
        }

        //認可系の処理
        if (!applicationInstance.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

            if (!applicationInstance.applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

                if (!vhutUserDto.isAuthorized(Right.STATUS_ALL_AI)) {
                    throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_ALL_AI.name());
                }

            } else if (!vhutUserDto.isAuthorized(Right.STATUS_CHILD_AI)) {
                throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_CHILD_AI.name());
            }

        } else if (!vhutUserDto.isAuthorized(Right.STATUS_OWN_AI)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_OWN_AI.name());
        }

        //状態チェック
        if (applicationInstance.status == ApplicationInstanceStatus.DEACTIVE) {
            applicationInstance.status = ApplicationInstanceStatus.ACTIVE;
        } else {
            throw new ApplicationInstanceStatusException(applicationInstance.status, "should be DEACTIVE");
        }

        //期間の確認
        Timestamp currentTime = TimestampUtil.getCurrentTimestamp();
        if (applicationInstance.applicationInstanceGroup.startTime.after(currentTime) || applicationInstance.applicationInstanceGroup.endTime.before(currentTime)) {
            throw new NoAvailableReservationException(ApplicationInstance.class, id, "activateApplicationInstanceById");
        }

        //ネットワークの取得
        for (ApplicationInstanceSecurityGroup applicationInstanceSecurityGroup : applicationInstance.applicationInstanceSecurityGroupList) {
            cloudServiceLogic.obtainNetwork(applicationInstance.applicationInstanceGroup.reservationIdToStart, applicationInstanceSecurityGroup.securityGroupId, serviceConfig.getExIpRequestMode());
        }

        //ユーザの割り当て
        for (ApplicationInstanceVm applicationInstanceVm : applicationInstance.applicationInstanceVmList) {
            VhutUserCloudUserMap vhutUserCloudUserMap = jdbcManager.from(VhutUserCloudUserMap.class)
                .where(new SimpleWhere().eq(vhutUserCloudUserMap().vhutUserId(), applicationInstance.vhutUserId)
                    .eq(vhutUserCloudUserMap().cloudId(), applicationInstanceVm.cloudId))
                .maxRows(1)
                .disallowNoResult()
                .getSingleResult();
            cloudServiceLogic.addVmUser(applicationInstance.applicationInstanceGroup.reservationIdToStart, applicationInstanceVm.vmId, vhutUserCloudUserMap.cloudUserId);
        }

        jdbcManager.update(applicationInstance)
            .includes(applicationInstance().status())
            .execute();
    }

    /**
     * アプリケーションインスタンスを起動不可能にする.
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
     * <li>ApplicationInstanceのstatusをDEACTIVEに変更します。
     * </ul>
     *
     * @param id アプリケーションンインスタンスのID
     * @throws ApplicationInstanceStatusException アプリケーションインスタンスの状態に関する例外
     * @throws AuthorizationException 認可例外
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.STATUS_OWN_AI)
    public void deactivateApplicationInstanceById(Long id) throws ApplicationInstanceStatusException, AuthenticationException, AuthorizationException {
        ApplicationInstance applicationInstance = jdbcManager.from(ApplicationInstance.class)
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList())
            .leftOuterJoin(applicationInstance().applicationInstanceSecurityGroupList()
                .applicationInstanceVmSecurityGroupMapList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList())
            .leftOuterJoin(applicationInstance().applicationInstanceVmList()
                .applicationInstanceVmSecurityGroupMapList())
            .innerJoin(applicationInstance().applicationInstanceGroup())
            .eager(applicationInstance().applicationInstanceGroup()
                .vhutUserId())
            .id(id)
            .getSingleResult();

        //バリデーション
        if (applicationInstance == null) {
            throw new InputRuntimeException("id", String.format("ApplicationInstance(id=%d) was not found", id));
        }

        //認可系の処理
        if (!applicationInstance.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

            if (!applicationInstance.applicationInstanceGroup.vhutUserId.equals(vhutUserDto.getVhutUser().id)) {

                if (!vhutUserDto.isAuthorized(Right.STATUS_ALL_AI)) {
                    throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_ALL_AI.name());
                }

            } else if (!vhutUserDto.isAuthorized(Right.STATUS_CHILD_AI)) {
                throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_CHILD_AI.name());
            }

        } else if (!vhutUserDto.isAuthorized(Right.STATUS_OWN_AI)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.STATUS_OWN_AI.name());
        }

        //状態チェック
        if (applicationInstance.status == ApplicationInstanceStatus.ACTIVE) {
            applicationInstance.status = ApplicationInstanceStatus.DEACTIVE;
        } else {
            throw new ApplicationInstanceStatusException(applicationInstance.status, "should be ACTIVE");
        }

        //VMの停止
        for (ApplicationInstanceVm applicationInstanceVm : applicationInstance.applicationInstanceVmList) {
            cloudServiceLogic.stopVm(applicationInstanceVm.vmId);
        }

        //ユーザの削除
        for (ApplicationInstanceVm applicationInstanceVm : applicationInstance.applicationInstanceVmList) {
            VhutUserCloudUserMap vhutUserCloudUserMap = jdbcManager.from(VhutUserCloudUserMap.class)
                .where(new SimpleWhere().eq(vhutUserCloudUserMap().vhutUserId(), applicationInstance.vhutUserId)
                    .eq(vhutUserCloudUserMap().cloudId(), applicationInstanceVm.cloudId))
                .maxRows(1)
                .getSingleResult();
            if (vhutUserCloudUserMap != null) {
                cloudServiceLogic.removeVmUser(applicationInstanceVm.vmId, vhutUserCloudUserMap.cloudUserId);
            }
        }

        //ネットワークの開放
        for (ApplicationInstanceSecurityGroup applicationInstanceSecurityGroup : applicationInstance.applicationInstanceSecurityGroupList) {
            cloudServiceLogic.releaseNetwork(applicationInstanceSecurityGroup.securityGroupId);
        }

        jdbcManager.update(applicationInstance)
            .includes(applicationInstance().status())
            .execute();
    }

    /**
     * @param application
     * @return
     */
    private ApplicationInstanceGroup joinAllVms(ApplicationInstanceGroup aig) {
        for (ApplicationVm avm : aig.application.applicationVmList) {
            if (avm.vmId == null || avm.vmId <= 0) {
                throw new DBStateRuntimeException(avm.getClass(), avm.id);
            } else {
                avm.vm = cloudServiceLogic.getVmById(avm.vmId);
            }
        }
        return aig;
    }

    /**
     * @param application
     * @return
     */
    private ApplicationInstanceGroup joinAllSecurityGroups(ApplicationInstanceGroup aig) {
        for (ApplicationSecurityGroup asg : aig.application.applicationSecurityGroupList) {
            if (asg.securityGroupId == null || asg.securityGroupId <= 0) {
                throw new DBStateRuntimeException(asg.getClass(), asg.id);
            } else {
                asg.securityGroup = cloudServiceLogic.getSecurityGroupById(asg.securityGroupId);
            }
        }
        return aig;
    }

    /**
     * @param ai
     */
    private ApplicationInstance joinAllSecurityGroups(ApplicationInstance ai) {
        for (ApplicationInstanceSecurityGroup aisg : ai.applicationInstanceSecurityGroupList) {
            if (aisg.securityGroupId == null || aisg.securityGroupId <= 0) {
                throw new DBStateRuntimeException(aisg.getClass(), aisg.id);
            } else {
                aisg.securityGroup = cloudServiceLogic.getSecurityGroupById(aisg.securityGroupId);
            }
        }
        return ai;
    }

    /**
     * @param applicationInstance
     */
    private ApplicationInstance joinAllVms(ApplicationInstance ai) {
        for (ApplicationInstanceVm aivm : ai.applicationInstanceVmList) {
            if (aivm.vmId == null || aivm.vmId <= 0) {
                throw new DBStateRuntimeException(aivm.getClass(), aivm.id);
            } else {
                aivm.vm = cloudServiceLogic.getVmById(aivm.vmId);
            }
        }
        return ai;
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
