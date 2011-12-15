/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import static jp.co.ntts.vhut.entity.Names.application;
import static jp.co.ntts.vhut.entity.Names.applicationInstance;
import static jp.co.ntts.vhut.entity.Names.applicationInstanceGroup;
import static jp.co.ntts.vhut.entity.Names.applicationInstanceVm;
import static jp.co.ntts.vhut.entity.Names.applicationVm;
import static jp.co.ntts.vhut.entity.Names.releasedApplicationTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.CloudTroubleDto;
import jp.co.ntts.vhut.dto.CommandDto;
import jp.co.ntts.vhut.dto.PerformanceDto;
import jp.co.ntts.vhut.dto.PredictionDto;
import jp.co.ntts.vhut.dto.ResourceDto;
import jp.co.ntts.vhut.dto.ServiceTroubleDto;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
import jp.co.ntts.vhut.entity.ApplicationVm;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.ReleasedApplication;
import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.exception.InternalRuntimeException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudInfraLogic;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.TimestampUtil;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>Managementサービスのクラス.
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
public class ManagementService {

    /** JDBCアクセスクラス */
    public JdbcManager jdbcManager;

    /**
     * CloudLogicを取得するためのファクトリ
     */
    public CloudLogicFactory cloudLogicFactory;

    /**
     * サービス関連の設定
     */
    public ServiceConfig serviceConfig;

    /**
     * クラウドの識別子
     */
    protected long cloudId = 1;

    /**
     * クラウドロジックのサービス面
     */
    protected ICloudServiceLogic cloudServiceLogic;

    /**
     * クラウドロジックのインフラ面
     */
    protected ICloudInfraLogic cloudInfraLogic;


    /**
     * 初期化.
     */
    @InitMethod
    public void init() {
        cloudServiceLogic = cloudLogicFactory.newCloudServiceLogic(cloudId);
        cloudInfraLogic = cloudLogicFactory.newCloudInfraLogic(cloudId);
    }

    /**
     * <p>パフォーマンス詳細情報取得.
     * <br>
     *
     * @param なし
     * @return PerformanceDto パフォーマンス詳細情報
     */
    @Auth(right = Right.READ_SYS_MANAGEMENT)
    public PerformanceDto getPerformance() {
        PerformanceDto performanceDto = new PerformanceDto();
        //クライアント側で取得出来るパフォーマンス詳細を取得する
        performanceDto.cloudInfra = cloudInfraLogic.getPerformance();
        //サービス側で取得出来るパフォーマンス詳細を取得する。
        //getCountの戻り値はLongのためキャストしてセットする
        //本日の日付をsetする。
        Date today = new Date();
        //アプリケーション数を取得する.
        performanceDto.acviveAp = (int) jdbcManager.from(Application.class)
            .getCount();
        //リリースされたアプリケーション数を取得する.
        performanceDto.commitedAp = (int) jdbcManager.from(ReleasedApplication.class)
            .getCount();
        //実行中の研修の数を取得する.
        performanceDto.activeAig = (int) jdbcManager.from(ApplicationInstanceGroup.class)
            .where(new SimpleWhere().le(applicationInstanceGroup().startTime(), today)
                .ge(applicationInstanceGroup().endTime(), today))
            .getCount();
        //実行中の研修環境の数を取得する.
        List<ApplicationInstanceGroup> aigs = jdbcManager.from(ApplicationInstanceGroup.class)
            .where(new SimpleWhere().le(applicationInstanceGroup().startTime(), today)
                .ge(applicationInstanceGroup().endTime(), today))
            .getResultList();
        performanceDto.activeAi = 0;
        for (ApplicationInstanceGroup aig : aigs) {
            long count = jdbcManager.from(ApplicationInstance.class)
                .where(new SimpleWhere().eq(applicationInstance().applicationInstanceGroupId(), aig.id))
                .getCount();
            performanceDto.activeAi = performanceDto.activeAi + (int) count;
        }
        return performanceDto;
    }

    /**
     * <p>障害ポイント取得.
     * <br>
     *
     * @param なし
     * @return ServiceTroubleDto 障害ポイント
     */
    @Auth(right = Right.READ_SYS_MANAGEMENT)
    public ServiceTroubleDto getTrouble() {
        //障害が発生しているVMとホストのリストを取得する。
        CloudTroubleDto troubleDto = cloudInfraLogic.getTroubleAbstractionList();
        //取得したHostのリストを戻り値にセットする。
        ServiceTroubleDto serviceTroubleDto = new ServiceTroubleDto();
        serviceTroubleDto.hostList = troubleDto.hostList;

        List<ApplicationInstanceVm> applicationInstanceVmList = new ArrayList<ApplicationInstanceVm>();
        List<ApplicationVm> applicationVmList = new ArrayList<ApplicationVm>();
        List<Application> applicationList = new ArrayList<Application>();
        List<ApplicationInstanceGroup> applicationInstanceGroupList = new ArrayList<ApplicationInstanceGroup>();
        List<VhutUser> userList = new ArrayList<VhutUser>();

        //障害が発生しているアプリケーションのリスト
        for (Vm vm : troubleDto.vmList) {
            //障害が発生しているアプリケーションVMのリストを取得する
            applicationVmList.addAll(jdbcManager.from(ApplicationVm.class)
                .where(new SimpleWhere().eq(applicationVm().vmId(), vm.id))
                .getResultList());
            //障害が発生しているアプリケーションインスタンスVMのリストを取得する。
            applicationInstanceVmList.addAll(jdbcManager.from(ApplicationInstanceVm.class)
                .innerJoin(applicationInstanceVm().applicationInstance())
                .innerJoin(applicationInstanceVm().applicationInstance()
                    .applicationInstanceGroup())
                .where(new SimpleWhere().eq(applicationVm().vmId(), vm.id))
                .getResultList());
        }
        for (ApplicationVm avm : applicationVmList) {
            //障害が発生しているアプリケーションVMのアプリケーションのリストを取得する
            applicationList.addAll(jdbcManager.from(Application.class)
                .innerJoin(application().vhutUser())
                .eager(application().vhutUserId())
                .id(avm.applicationId)
                .getResultList());
        }

        //障害が発生しているアプリケーションインスタンスグループのリスト
        for (ApplicationInstanceVm aivm : applicationInstanceVmList) {
            //障害が発生しているアプリケーションインスタンスVMのアプリケーションインスタンスグループのリストを取得する。
            applicationInstanceGroupList.addAll(jdbcManager.from(ApplicationInstanceGroup.class)
                .innerJoin(applicationInstanceGroup().vhutUser())
                .id(aivm.applicationInstance.applicationInstanceGroupId)
                .getResultList());
        }
        for (ApplicationInstanceGroup aig : applicationInstanceGroupList) {
            userList.addAll(jdbcManager.from(VhutUser.class)
                .id(aig.vhutUser.id)
                .getResultList());
        }
        //障害が発生しているVMのユーザリスト
        //障害が発生しているアプリケーションVM.アプリケーションインスタンスVMのユーザのリストを取得する。
        for (Application app : applicationList) {
            userList.addAll(jdbcManager.from(VhutUser.class)
                .id(app.vhutUser.id)
                .getResultList());
        }
        serviceTroubleDto.aigList = applicationInstanceGroupList;
        serviceTroubleDto.aList = applicationList;
        serviceTroubleDto.userList = userList;

        return serviceTroubleDto;
    }

    /**
     * <p>利用予測詳細取得.
     * <br>
     *
     * @param startTime 開始時間
     * @param endTime 終了日時
     * @return Prediction 利用予測
     */
    @Auth(right = Right.READ_SYS_MANAGEMENT)
    public PredictionDto getPrediction(Timestamp startTime, Timestamp endTime) {

        int count = (int) TimestampUtil.countDate(startTime, endTime);

        if (count <= 0) {
            throw new InputRuntimeException("startTime, endTime", "startTime should be before endTime.");
        }

        List<ResourceDto> resourceList = cloudServiceLogic.getResourceListByTerm(startTime, endTime);

        if (resourceList.size() != count) {
            throw new InternalRuntimeException("CloudLogic#getResourceListByTerm(Timestamp, Timestamp) doesn't replay correctly.");
        }

        PredictionDto result = new PredictionDto();

        result.startTime = startTime;
        result.endTime = endTime;

        result.usedCpuCore = new int[count];
        result.usedMemory = new int[count];
        result.usedStorage = new int[count];
        result.usedVlan = new int[count];
        result.usedPublicIp = new int[count];

        for (int i = 0; i < count; i++) {
            ResourceDto dto = resourceList.get(i);
            result.usedCpuCore[i] = caliculateParcentage(dto.cpuCoreUsed, dto.cpuCoreMax);
            result.usedMemory[i] = caliculateParcentage(dto.memoryUsed, dto.memoryMax);
            result.usedStorage[i] = caliculateParcentage(dto.storageUsed, dto.storageMax);
            result.usedPublicIp[i] = caliculateParcentage(dto.publicIpUsed, dto.publicIpMax);
            result.usedVlan[i] = caliculateParcentage(dto.vlanUsed, dto.vlanMax);
        }

        return result;
    }

    private int caliculateParcentage(int ch, int mo) {
        if (mo <= 0) {
            return 0;
        }
        long value = Math.round((double) ch / (double) mo * 100);
        if (value < 0) {
            return 0;
        }
        if (value > 100) {
            return 100;
        }
        return (int) value;
    }

    /**
     * <p>コマンド概要一覧取得.
     * <br>
     *
     * @param number
     * @return Command コマンドのリスト
     */
    @Auth(right = Right.READ_SYS_MANAGEMENT)
    public List<CommandDto> getAllCommandList() {
        return createCommandDtosByCommands(cloudServiceLogic.getCommandAbstractionList());
    }

    /**
     * <p>コマンド検索.
     * <br>
     *
     * @param keyword 検索キー nullは全対象 ""は対象なし
     * @param startDate 開始日 nullは全対象
     * @param endDate 終了日 nullは全対象
     * @param operations 検索対象の操作のリスト null/size=0は全対象
     * @param statuses 検索対象のコマンドの状態リスト null/size=0は全対象
     * @return Command コマンドのリスト
     */
    @Auth(right = Right.READ_SYS_MANAGEMENT)
    public List<CommandDto> searchCommandList(String keyword, Timestamp startDate, Timestamp endDate, Collection<CommandOperation> operations, Collection<CommandStatus> statuses) {
        Map<Long, Command> map = new HashMap<Long, Command>();
        List<Command> commands = new ArrayList<Command>();
        //keyword条件を作成
        if (keyword != null) {
            Set<Long> vmIds = new HashSet<Long>();
            Set<Long> templateIds = new HashSet<Long>();
            List<ApplicationVm> appVms = jdbcManager.from(ApplicationVm.class)
                .innerJoin(applicationVm().application())
                .where(new ComplexWhere().or()
                    .contains(applicationVm().application()
                        .name(), keyword))
                .getResultList();
            for (ApplicationVm appVm : appVms) {
                vmIds.add(appVm.vmId);
            }
            List<ApplicationInstanceVm> aiVms = jdbcManager.from(ApplicationInstanceVm.class)
                .innerJoin(applicationInstanceVm().applicationInstance())
                .innerJoin(applicationInstanceVm().applicationInstance()
                    .applicationInstanceGroup())
                .where(new ComplexWhere().or()
                    .contains(applicationInstanceVm().applicationInstance()
                        .applicationInstanceGroup()
                        .name(), keyword))
                .getResultList();
            for (ApplicationInstanceVm aiVm : aiVms) {
                vmIds.add(aiVm.vmId);
            }
            List<ReleasedApplicationTemplate> rappTemplates = jdbcManager.from(ReleasedApplicationTemplate.class)
                .innerJoin(releasedApplicationTemplate().releasedApplication())
                .innerJoin(releasedApplicationTemplate().releasedApplication()
                    .application())
                .where(new ComplexWhere().or()
                    .contains(releasedApplicationTemplate().releasedApplication()
                        .application()
                        .name(), keyword))
                .getResultList();
            for (ReleasedApplicationTemplate rappTemplate : rappTemplates) {
                templateIds.add(rappTemplate.templateId);
            }
            List<Command> vmRelatedCommands = cloudServiceLogic.searchCommandListByVmIds(vmIds, startDate, endDate, operations, statuses);
            if (vmRelatedCommands != null) {
                commands.addAll(vmRelatedCommands);
            }
            List<Command> templateRelatedCommands = cloudServiceLogic.searchCommandListByTemplateIds(templateIds, startDate, endDate, operations, statuses);
            if (templateRelatedCommands != null) {
                commands.addAll(templateRelatedCommands);
            }
        }
        List<Command> keywordRelatedCommands = cloudServiceLogic.searchCommandList(keyword, startDate, endDate, operations, statuses);
        if (keywordRelatedCommands != null) {
            commands.addAll(keywordRelatedCommands);
        }
        for (Command command : commands) {
            map.put(command.id, command);
        }
        return createCommandDtosByCommands(map.values());
    }

    /**
     * コマンドエンティティのリストからコマンドDTOのリストを作成します.
     * @param commands コマンドエンティティのリスト
     * @return コマンドDTOのリスト
     */
    private List<CommandDto> createCommandDtosByCommands(Collection<Command> commands) {
        List<CommandDto> dtos = new ArrayList<CommandDto>();
        Set<Long> vmIds = new HashSet<Long>();
        Set<Long> templateIds = new HashSet<Long>();
        Map<Long, Application> vmIdAppMap = new HashMap<Long, Application>();
        Map<Long, Application> templateIdAppMap = new HashMap<Long, Application>();
        Map<Long, ApplicationInstanceGroup> vmIdAigMap = new HashMap<Long, ApplicationInstanceGroup>();
        //CommandDtoの作成
        for (Command command : commands) {
            CommandDto dto = CommandDto.newFromCommand(command);
            if (dto.vmId != null) {
                vmIds.add(dto.vmId);
            }
            if (dto.templateId != null) {
                templateIds.add(dto.templateId);
            }
            dtos.add(dto);
        }
        //Application
        List<ApplicationVm> appVms = jdbcManager.from(ApplicationVm.class)
            .innerJoin(applicationVm().application())
            .innerJoin(applicationVm().application()
                .vhutUser())
            .where(new SimpleWhere().in(applicationVm().vmId(), vmIds))
            .getResultList();
        for (ApplicationVm appVm : appVms) {
            vmIdAppMap.put(appVm.vmId, appVm.application);
        }
        //ApplicationInstance
        List<ApplicationInstanceVm> aiVms = jdbcManager.from(ApplicationInstanceVm.class)
            .innerJoin(applicationInstanceVm().applicationInstance())
            .innerJoin(applicationInstanceVm().applicationInstance()
                .applicationInstanceGroup())
            .innerJoin(applicationInstanceVm().applicationInstance()
                .applicationInstanceGroup()
                .vhutUser())
            .where(new SimpleWhere().in(applicationInstanceVm().vmId(), vmIds))
            .getResultList();
        for (ApplicationInstanceVm aiVm : aiVms) {
            vmIdAigMap.put(aiVm.vmId, aiVm.applicationInstance.applicationInstanceGroup);
        }
        //ReleasedApplication
        List<ReleasedApplicationTemplate> rappTemplates = jdbcManager.from(ReleasedApplicationTemplate.class)
            .innerJoin(releasedApplicationTemplate().releasedApplication())
            .innerJoin(releasedApplicationTemplate().releasedApplication()
                .application())
            .innerJoin(releasedApplicationTemplate().releasedApplication()
                .application()
                .vhutUser())
            .where(new SimpleWhere().in(releasedApplicationTemplate().templateId(), templateIds))
            .getResultList();
        for (ReleasedApplicationTemplate rappTemplate : rappTemplates) {
            templateIdAppMap.put(rappTemplate.templateId, rappTemplate.releasedApplication.application);
        }
        //CommandDto
        for (CommandDto dto : dtos) {
            if (dto.vmId != null) {
                Application app = vmIdAppMap.get(dto.vmId);
                if (app != null) {
                    dto.updateByApplication(app);
                }
                ApplicationInstanceGroup aig = vmIdAigMap.get(dto.vmId);
                if (aig != null) {
                    dto.updateByApplicationInstanceGroup(aig);
                }
            }
            if (dto.templateId != null) {
                Application app = templateIdAppMap.get(dto.templateId);
                if (app != null) {
                    dto.updateByApplication(app);
                }
            }
        }
        Collections.sort(dtos, new Comparator<CommandDto>() {
            public int compare(CommandDto o1, CommandDto o2) {
                return (int) (o2.commandId - o1.commandId);
            }
        });
        return dtos;
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
