/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.config;

import static jp.co.ntts.vhut.entity.Names.cluster;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.ntts.vhut.dto.SpecDto;
import jp.co.ntts.vhut.dto.SwitchTemplateDto;
import jp.co.ntts.vhut.entity.Cluster;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.Names;
import jp.co.ntts.vhut.entity.Storage;
import jp.co.ntts.vhut.exception.ConfigRuntimeException;
import jp.co.ntts.vhut.task.CloudTask;
import jp.co.ntts.vhut.util.Ipv4ConversionUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.chronos.core.TaskTrigger;
import org.seasar.chronos.core.trigger.CCronTrigger;
import org.seasar.config.core.config.annotation.Config;
import org.seasar.config.core.config.annotation.ConfigIgnore;
import org.seasar.config.core.config.annotation.ConfigKey;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.exception.ParseRuntimeException;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.ShortConversionUtil;

/**
 * <p>クラウドモジュール関連のコンフィグレーション
 * <br>
 * <p>server.propertiesの値をインスタンスとして提供します.
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
@Config(name = "vhut")
public class CloudConfig {

    public static final String CLOUD_NAME_DEFAULT = "Default";

    /**
     * seasarのLoggerインスタンス取得
     */
    private static final VhutLogger logger = VhutLogger.getLogger(CloudConfig.class);

    /**
     * {@link CloudTask}用のトリガーを取得します。のデフォルト設定.
     */
    private static final String CLOUD_TASK_DEFAULT_CRON_EXPRESSION = "0 * * * * ?";

    /**
     * バッチ系オペレーションのデフォルト間隔(分)
     */
    private static final int INTERVAL_DEFAULT = 60;

    /** DBアクセスクラス */
    public JdbcManager jdbcManager;


    //---------------------------------------------------
    // util
    //---------------------------------------------------

    /**
     * 分間隔のStringから秒間隔のIntegerを取り出します.
     * 値が間違っているときはデフォルト値[3600]が設定されます.
     * @param str 分間隔
     * @return 分間隔
     */
    private static Integer convertInterval(Object str) {
        try {
            Integer value = IntegerConversionUtil.toInteger(str);
            if (value == null) {
                return INTERVAL_DEFAULT;
            }
            if (value > 0) {
                return value;
            }
            return null;
        } catch (Exception e) {
            return INTERVAL_DEFAULT;
        }
    }

    /**
     * 文字列からIntegerの配列を作成する.
     * @param valueList 文字列
     * @param div 区切り文字
     * @return Integerの配列
     */
    private static List<Integer> createIntegerList(String valueList, String div) {
        List<Integer> array = new ArrayList<Integer>();
        for (String value : valueList.split(div)) {
            Integer element = IntegerConversionUtil.toInteger(value);
            if (element != null) {
                array.add(element);
            }
        }
        return array;
    }

    /**
     * 文字列からStringの配列を作成する.
     * @param valueList 文字列
     * @param div 区切り文字
     * @return Stringの配列
     */
    private static List<String> createStringList(String valueList, String div) {
        List<String> array = new ArrayList<String>();
        if (valueList.length() > 0) {
            for (String value : valueList.split(div)) {
                array.add(value);
            }
        }
        return array;
    }

    private static Integer createNetworkOrder(String orderString) {
        Integer order = IntegerConversionUtil.toInteger(orderString);
        if (order == 0) {
            throw new ConfigRuntimeException(CloudConfig.class, "createNetworkOrder", "order=0 is reserved for network address");
        } else if (order == -1) {
            throw new ConfigRuntimeException(CloudConfig.class, "createNetworkOrder", "order=-1 is reserved for broadcast address");
        }
        return order;
    }


    //----------------------------------------------
    // initialize
    //----------------------------------------------

    /**
     * サーバ起動時にクラウド関連のデータを外部サーバから取得したデータで最新化します.
     */
    @ConfigKey(name = "cloud.initialize", readOnly = true)
    public String initializeString;

    @ConfigIgnore
    private Boolean initialize;


    public boolean isInitialize() {
        if (initialize == null) {
            initialize = Boolean.valueOf(initializeString);
        }
        return initialize;
    }


    //----------------------------------------------
    // prefix
    //----------------------------------------------

    /**
     * ネットワークに付与するクラウド側のプレフィックスです.
     */
    @ConfigKey(name = "cloud.network.prefix", readOnly = true)
    public String networkPrefix;

    //----------------------------------------------
    // path
    //----------------------------------------------

    /**
     * スクリプトファイルの置き場所（絶対パス）
     */
    @ConfigKey(name = "cloud.scriptRoot", readOnly = true)
    public String scriptRoot;

    //----------------------------------------------
    // task
    //----------------------------------------------

    /**
     * クラウド関連の定期実行タスクのトリガ設定
     */
    @ConfigKey(name = "cloud.cloudTaskCronExpression", readOnly = true)
    public String cloudTaskCronExpression;

    @ConfigIgnore
    private TaskTrigger cloudTaskTrigger;


    /**
     * {@link CloudTask}用のトリガーを取得します。
     * cloud.propertiesのservice.cloudTaskCronExpressionで変更が可能
     * デフォルトは毎分実行
     * @return {@link CloudTask}用のトリガー
     */
    public TaskTrigger getCloudTaskTrigger() {
        if (cloudTaskTrigger == null) {
            try {
                cloudTaskTrigger = new CCronTrigger(cloudTaskCronExpression);
            } catch (ParseRuntimeException e) {
                cloudTaskTrigger = new CCronTrigger(CLOUD_TASK_DEFAULT_CRON_EXPRESSION);
            }
        }
        return cloudTaskTrigger;
    }


    /**
     * クラスタ一覧更新の間隔(分)
     * cloud.intervalCheckClusters=60
     */
    @ConfigKey(name = "cloud.intervalCheckClusters", readOnly = true)
    public String intervalCheckClustersString;

    /**
     * ホスト一覧更新の間隔(分)
     * cloud.intervalCheckHosts=60
     */
    @ConfigKey(name = "cloud.intervalCheckHosts", readOnly = true)
    public String intervalCheckHostsString;

    /**
     * ネットワーク一覧更新の間隔(分)
     * cloud.intervalCheckNetworks=60
     */
    @ConfigKey(name = "cloud.intervalCheckNetworks", readOnly = true)
    public String intervalCheckNetworksString;

    /**
     * ストレージ一覧更新の間隔(分)
     * cloud.intervalCheckDataStorages=60
     */
    @ConfigKey(name = "cloud.intervalCheckDataStorages", readOnly = true)
    public String intervalCheckDataStoragesString;

    /**
     * VM一覧更新の間隔(分)
     * cloud.intervalCheckVms=60
     */
    @ConfigKey(name = "cloud.intervalCheckVms", readOnly = true)
    public String intervalCheckVmsString;

    /**
     * テンプレート一覧更新の間隔(分)
     * cloud.intervalCheckTemplates=60
     */
    @ConfigKey(name = "cloud.intervalCheckTemplates", readOnly = true)
    public String intervalCheckTemplatesString;

    /**
     * ユーザ一覧更新の間隔(分)
     * cloud.intervalCheckUsers=60
     */
    @ConfigKey(name = "cloud.intervalCheckUsers", readOnly = true)
    public String intervalCheckUsersString;

    @ConfigIgnore
    private Map<CommandOperation, Integer> operationIntervalMap;


    /**
     * 一定間隔で実行されるクラウド系のオペレーションを取得します.
     * @return オペレーション{@link CommandOperation}をキーに、秒{@link Integer}をバリューにしたマップ
     */
    public Map<CommandOperation, Integer> getOperationIntervalMap() {
        if (operationIntervalMap == null) {
            operationIntervalMap = new HashMap<CommandOperation, Integer>();
            Integer value;
            //Cluster
            value = convertInterval(intervalCheckClustersString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_ALL_CLUSTERS, value);
            }
            //Host
            value = convertInterval(intervalCheckHostsString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_HOSTS_BY_CLUSTER_ID, value);
            }
            //Network
            value = convertInterval(intervalCheckNetworksString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_NETWORKS_BY_CLUSTER_ID, value);
            }
            //DataStorage
            value = convertInterval(intervalCheckDataStoragesString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_ALL_DATA_STORAGES, value);
            }
            //Vm
            value = convertInterval(intervalCheckVmsString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_VMS_BY_CLUSTER_ID, value);
            }
            //Template
            value = convertInterval(intervalCheckTemplatesString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_TEMPLATES_BY_CLUSTER_ID, value);
            }
            //User
            value = convertInterval(intervalCheckUsersString);
            if (value != null) {
                operationIntervalMap.put(CommandOperation.GET_ALL_USERS, value);
            }
        }
        return operationIntervalMap;
    }


    /**
     * コマンド実行時間の限界値（これを超えるとエラー）.
     * cloud.devResourceRate=20
     */
    @ConfigKey(name = "cloud.commandTimeLimit", readOnly = true)
    public String commandTimeLimitString;
    /**
     * コマンド実行時間の限界値（これを超えるとエラー）.
     */
    @ConfigIgnore
    private Integer commandTimeLimit = 0;


    /**
     * コマンド実行時間の限界値（これを超えるとエラー）.
     * @return the commandTimeLimit
     */
    public final Integer getCommandTimeLimit() {
        if (commandTimeLimit == 0) {
            commandTimeLimit = IntegerConversionUtil.toInteger(commandTimeLimitString);
        }
        return commandTimeLimit;
    }


    //----------------------------------------------
    // resource
    //----------------------------------------------

    /**
     * アプリケーション開発環境のリソース比率.
     * cloud.devResourceRate=20
     */
    @ConfigKey(name = "cloud.devResourceRate", readOnly = true)
    public String devResourceRateString;
    /**
     * アプリケーション開発環境のリソース比率.
     */
    @ConfigIgnore
    private Integer devResourceRate = 0;


    /**
     * アプリケーション開発環境のリソース比率.
     * @return the devResourceRate
     */
    public final Integer getDevResourceRate() {
        if (devResourceRate == 0) {
            devResourceRate = IntegerConversionUtil.toInteger(devResourceRateString);
        }
        return devResourceRate;
    }


    /**
     * CPUリソース利用率（警告）.
     * cloud.cpuResourceWarnRate=70
     */
    @ConfigKey(name = "cloud.cpuResourceWarnRate", readOnly = true)
    public String cpuResourceWarnRateString;
    /**
     * CPUリソース利用率（警告）.
     */
    @ConfigIgnore
    private Integer cpuResourceWarnRate = 0;


    /**
     * CPUリソース利用率（警告）.
     * @return the cpuResourceWarnRate
     */
    public final Integer getCpuResourceWarnRate() {
        if (cpuResourceWarnRate == 0) {
            cpuResourceWarnRate = IntegerConversionUtil.toInteger(cpuResourceWarnRateString);
        }
        return cpuResourceWarnRate;
    }


    /**
     * CPUリソース利用率（上限）.
     * cloud.cpuResourceLimitRate=90
     */
    @ConfigKey(name = "cloud.cpuResourceLimitRate", readOnly = true)
    public String cpuResourceLimitRateString;
    /**
     * CPUリソース利用率（上限）.
     */
    @ConfigIgnore
    private Integer cpuResourceLimitRate = 0;


    /**
     * CPUリソース利用率（上限）.
     * @return the cpuResourceLimitRate
     */
    public final Integer getCpuResourceLimitRate() {
        if (cpuResourceLimitRate == 0) {
            cpuResourceLimitRate = IntegerConversionUtil.toInteger(cpuResourceLimitRateString);
        }
        return cpuResourceLimitRate;
    }


    /**
     * メモリリソース利用率（警告）.
     * cloud.memoryResourceWarnRate=70
     */
    @ConfigKey(name = "cloud.memoryResourceWarnRate", readOnly = true)
    public String memoryResourceWarnRateString;
    /**
     * メモリリソース利用率（警告）.
     */
    @ConfigIgnore
    private Integer memoryResourceWarnRate = 0;


    /**
     * メモリリソース利用率（警告）.
     * @return the memoryResourceWarnRate
     */
    public final Integer getMemoryResourceWarnRate() {
        if (memoryResourceWarnRate == 0) {
            memoryResourceWarnRate = IntegerConversionUtil.toInteger(memoryResourceWarnRateString);
        }
        return memoryResourceWarnRate;
    }


    /**
     * メモリリソース利用率（上限）.
     * cloud.memoryResourceLimitRate=90
     */
    @ConfigKey(name = "cloud.memoryResourceLimitRate", readOnly = true)
    public Integer memoryResourceLimitRateString;
    /**
     * メモリリソース利用率（上限）.
     */
    @ConfigIgnore
    private Integer memoryResourceLimitRate = 0;


    /**
     * メモリリソース利用率（上限）.
     * @return the memoryResourceLimitRate
     */
    public final Integer getMemoryResourceLimitRate() {
        if (memoryResourceLimitRate == 0) {
            memoryResourceLimitRate = IntegerConversionUtil.toInteger(memoryResourceLimitRateString);
        }
        return memoryResourceLimitRate;
    }


    /**
     * ストレージリソース利用率（警告）.
     * cloud.storageResourceWarnRate=70
     */
    @ConfigKey(name = "cloud.storageResourceWarnRate", readOnly = true)
    public String storageResourceWarnRateString;
    /**
     * ストレージリソース利用率（警告）.
     */
    @ConfigIgnore
    private Integer storageResourceWarnRate = 0;


    /**
     * ストレージリソース利用率（警告）.
     * @return the storageResourceWarnRate
     */
    public final Integer getStorageResourceWarnRate() {
        if (storageResourceWarnRate == 0) {
            storageResourceWarnRate = IntegerConversionUtil.toInteger(storageResourceWarnRateString);
        }
        return storageResourceWarnRate;
    }


    /**
     * ストレージリソース利用率（上限）.
     * cloud.storageResourceLimitRate=90
     */
    @ConfigKey(name = "cloud.storageResourceLimitRate", readOnly = true)
    public String storageResourceLimitRateString;
    /**
     * ストレージリソース利用率（上限）.
     */
    @ConfigIgnore
    private Integer storageResourceLimitRate = 0;


    /**
     * ストレージリソース利用率（上限）.
     * @return the storageResourceLimitRate
     */
    public final Integer getStorageResourceLimitRate() {
        if (storageResourceLimitRate == 0) {
            storageResourceLimitRate = IntegerConversionUtil.toInteger(storageResourceLimitRateString);
        }
        return storageResourceLimitRate;
    }


    /**
     * VLANリソース利用率（警告）.
     * cloud.vlanResourceWarnRate=70
     */
    @ConfigKey(name = "cloud.vlanResourceWarnRate", readOnly = true)
    public String vlanResourceWarnRateString;
    /**
     * VLANリソース利用率（警告）.
     */
    @ConfigIgnore
    private Integer vlanResourceWarnRate = 0;


    /**
     * VLANリソース利用率（警告）.
     * @return the vlanResourceWarnRate
     */
    public final Integer getVlanResourceWarnRate() {
        if (vlanResourceWarnRate == 0) {
            vlanResourceWarnRate = IntegerConversionUtil.toInteger(vlanResourceWarnRateString);
        }
        return vlanResourceWarnRate;
    }


    /**
     * VLANリソース利用率（上限）.
     * cloud.vlanResourceLimitRate=90
     */
    @ConfigKey(name = "cloud.vlanResourceLimitRate", readOnly = true)
    public String vlanResourceLimitRateString;
    /**
     * VLANリソース利用率（上限）.
     */
    @ConfigIgnore
    private Integer vlanResourceLimitRate = 0;


    /**
     * VLANリソース利用率（上限）.
     * @return the vlanResourceLimitRate
     */
    public final Integer getVlanResourceLimitRate() {
        if (vlanResourceLimitRate == 0) {
            vlanResourceLimitRate = IntegerConversionUtil.toInteger(vlanResourceLimitRateString);
        }
        return vlanResourceLimitRate;
    }


    /**
     * 外部IPリソース利用率（警告）.
     * cloud.publicIpResourceWarnRate=70
     */
    @ConfigKey(name = "cloud.publicIpResourceWarnRate", readOnly = true)
    public String publicIpResourceWarnRateString;
    /**
     * 外部IPリソース利用外部IP）.
     */
    @ConfigIgnore
    private Integer publicIpResourceWarnRate = 0;


    /**
     * 外部IPリソース利用率（警告）.
     * @return the publicIpResourceWarnRate
     */
    public final Integer getPublicIpResourceWarnRate() {
        if (publicIpResourceWarnRate == 0) {
            publicIpResourceWarnRate = IntegerConversionUtil.toInteger(publicIpResourceWarnRateString);
        }
        return publicIpResourceWarnRate;
    }


    /**
     * 外部IPリソース利用率（上限）.
     * cloud.publicIpResourceLimitRate=90
     */
    @ConfigKey(name = "cloud.publicIpResourceLimitRate", readOnly = true)
    public String publicIpResourceLimitRateString;
    /**
     * 外部IPリソース利用率（上限）.
     */
    @ConfigIgnore
    private Integer publicIpResourceLimitRate = 0;


    /**
     * 外部IPリソース利用率（上限）.
     * @return the publicIpResourceLimitRate
     */
    public final Integer getPublicIpResourceLimitRate() {
        if (publicIpResourceLimitRate == 0) {
            publicIpResourceLimitRate = IntegerConversionUtil.toInteger(publicIpResourceLimitRateString);
        }
        return publicIpResourceLimitRate;
    }


    /**
     * スペック.
     * cloud.specList<br>
     * 形式、label:[int, int]<br>
     * サンプル small:[1,1], medium:[2,2], large:[4,4]
     */
    @ConfigKey(name = "cloud.specList", readOnly = true)
    public String specListString;
    /**
     * スペック値.
     */
    @ConfigIgnore
    private List<SpecDto> specList = null;


    /**
     * スペック値.
     * @return the specList
     */
    public final List<SpecDto> getSpecList() {
        if (specList == null) {
            specList = new ArrayList<SpecDto>();
            Pattern pattern = Pattern.compile("(\\S+?)\\s*:.*?(\\d+?)\\s*,.*?(\\d+?)\\s*\\]");
            Matcher matcher = pattern.matcher(specListString);
            long id = 1;
            while (matcher.find()) {
                SpecDto dto = new SpecDto();
                dto.id = id++;
                dto.name = matcher.group(1);
                dto.cpuCore = ShortConversionUtil.toShort(matcher.group(2));
                dto.memory = IntegerConversionUtil.toInteger(matcher.group(3));
                specList.add(dto);
            }
        }
        return specList;
    }


    /**
     * 追加ディスクテンプレート容量.
     * cloud.diskList=10,20,30
     */
    @ConfigKey(name = "cloud.diskList", readOnly = true)
    public String diskListString;
    /**
     * 追加ディスクテンプレート容量.
     */
    @ConfigIgnore
    private List<Integer> diskList = null;


    /**
     * 追加ディスクテンプレート容量.
     * @return 追加ディスクテンプレート容量のリスト
     */
    public final List<Integer> getDiskList() {
        if (diskList == null) {
            diskList = createIntegerList(diskListString, ",");
        }
        return diskList;
    }


    /**
     * デスクトップ同時起動数上限.
     * cloud.desktopLimit=30
     */
    @ConfigKey(name = "cloud.desktopLimit", readOnly = true)
    public String desktopLimitString;
    /**
     * デスクトップ同時起動数上限.
     */
    @ConfigIgnore
    private Integer desktopLimit = 0;


    /**
     * デスクトップ同時起動数上限.
     * @return the desktopLimit
     */
    public final Integer getDesktopLimit() {
        if (desktopLimit == 0) {
            desktopLimit = IntegerConversionUtil.toInteger(desktopLimitString);
        }
        return desktopLimit;
    }


    /**
     * リソース予約の最大日時.
     */
    @ConfigKey(name = "cloud.reservationEndTimeMax", readOnly = true)
    public String reservationEndTimeMaxString;
    /**
     * リソース予約の最大日時.
     */
    @ConfigIgnore
    private Timestamp reservationEndTimeMax = null;


    /**
     * リソース予約の最大日時.
     */
    public final Timestamp getReservationEndTimeMax() {
        if (reservationEndTimeMax == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(reservationEndTimeMaxString);
            } catch (ParseException e) {
                try {
                    date = simpleDateFormat.parse("2050/12/31");
                } catch (ParseException e1) {
                }
            }
            reservationEndTimeMax = new Timestamp(date.getTime());
        }
        return reservationEndTimeMax;
    }


    //-------------------------------------------
    // network
    //-------------------------------------------

    /**
     * VLAN開始番号.
     * cloud.vlanStartNumber=2
     */
    @ConfigKey(name = "cloud.vlanStartNumber", readOnly = true)
    public String vlanStartNumberString;
    /**
     * VLAN開始番号.
     */
    @ConfigIgnore
    private Integer vlanStartNumber = 0;


    /**
     * VLAN開始番号.
     * @return the vlanStartNumber
     */
    public final Integer getVlanStartNumber() {
        if (vlanStartNumber == 0) {
            vlanStartNumber = IntegerConversionUtil.toInteger(vlanStartNumberString);
        }
        return vlanStartNumber;
    }


    /**
     * VLAN終了番号.
     * cloud.vlanEndNumber=30
     */
    @ConfigKey(name = "cloud.vlanEndNumber", readOnly = true)
    public String vlanEndNumberString;
    /**
     * VLAN終了番号.
     */
    @ConfigIgnore
    private Integer vlanEndNumber = 0;


    /**
     * VLAN終了番号.
     * @return the vlanEndNumber
     */
    public final Integer getVlanEndNumber() {
        if (vlanEndNumber == 0) {
            vlanEndNumber = IntegerConversionUtil.toInteger(vlanEndNumberString);
        }
        return vlanEndNumber;
    }


    /**
     * VLAN例外リスト.
     * cloud.vlanExcludeList=5,6,9
     */
    @ConfigKey(name = "cloud.vlanExcludeList", readOnly = true)
    public String vlanExcludeListString;
    /**
     * VLAN例外リスト.
     */
    @ConfigIgnore
    private List<Integer> vlanExcludeList = null;


    /**
     * VLAN例外リスト.
     * @return the vlanExcludeList
     */
    public final List<Integer> getVlanExcludeList() {
        if (vlanExcludeList == null) {
            vlanExcludeList = createIntegerList(vlanExcludeListString, ",");
        }
        return vlanExcludeList;
    }


    /**
     * メンテナンス用VLANリスト.
     * cloud.vlanExcludeList=5,6,9
     */
    @ConfigKey(name = "cloud.vlanForMaintenanceList", readOnly = true)
    public String vlanForMaintenanceListString;
    /**
     * メンテナンス用VLANリスト.
     */
    @ConfigIgnore
    private List<Integer> vlanForMaintenanceList = null;


    /**
     * メンテナンス用VLANリスト.
     * @return the vlanForMaintenanceList
     */
    public final List<Integer> getVlanForMaintenanceList() {
        if (vlanForMaintenanceList == null) {
            vlanForMaintenanceList = createIntegerList(vlanForMaintenanceListString, ",");
        }
        return vlanForMaintenanceList;
    }


    /**
     * 外部IPアドレス（開始値）.
     * cloud.exIpStartAddress=10.38.0.0
     */
    @ConfigKey(name = "cloud.exIpStartAddress", readOnly = true)
    public String exIpStartAddress;

    /**
     * 外部IPアドレス（開始値）.
     * cloud.exIpEndAddress=10.38.0.0
     */
    @ConfigKey(name = "cloud.exIpEndAddress", readOnly = true)
    public String exIpEndAddress;

    /**
     * 外部IP例外リスト.
     * cloud.exIpExcludeList=10.38.2.5,10.38.2.6,10.38.2.7
     */
    @ConfigKey(name = "cloud.exIpExcludeList", readOnly = true)
    public String exIpExcludeListString;
    /**
     * VLAN例外リスト.
     */
    @ConfigIgnore
    private List<String> exIpExcludeList = null;


    /**
     * VLAN例外リスト.
     * @return the exIpExcludeList
     */
    public final List<String> getExIpExcludeList() {
        if (exIpExcludeList == null) {
            exIpExcludeList = createStringList(exIpExcludeListString, ",");
        }
        return exIpExcludeList;
    }


    /**
     * 内部IPネットワークアドレスの開始値.
     * cloud.inIpNwAddress=10.39.0.0
     */
    @ConfigKey(name = "cloud.inIpStartNwAddress", readOnly = true)
    public String inIpStartNwAddress;

    /**
     * 内部IPサブネットマスク.
     * cloud.inIpSubnetMask=255.255.0.0
     */
    @ConfigKey(name = "cloud.inIpSubnetMask", readOnly = true)
    public String inIpSubnetMask;

    /**
     * 内部IPに付与するGatewayアドレスのサブネットでの順番
     * 1:前から1番目
     * -1:後ろから一番目
     * 0:エラー
     * cloud.inIpGatewayOrder=1
     */
    @ConfigKey(name = "cloud.inIpGatewayOrder", readOnly = true)
    public String inIpGatewayOrderString = "1";
    /**
     * 内部IPに付与するGatewayアドレスのサブネットでの順番.
     */
    @ConfigIgnore
    private Integer inIpGatewayOrder = null;


    /**
     * 内部IPに付与するGatewayアドレスのサブネットでの順番.
     * @return the inIpGatewayOrder
     */
    public final Integer getInIpGatewayOrder() {
        if (inIpGatewayOrder == null) {
            inIpGatewayOrder = createNetworkOrder(inIpGatewayOrderString);
        }
        return inIpGatewayOrder;
    }


    /**
     * 内部IPに付与するDHCPアドレスのサブネットでの順番
     * 1:前から2番目
     * -2:後ろから2番目
     * 0, -1:エラー
     * cloud.inIpGatewayOrder=1
     */
    @ConfigKey(name = "cloud.inIpDhcpOrder", readOnly = true)
    public String inIpDhcpOrderString = "1";
    /**
     * 内部IPに付与するDhcpアドレスのサブネットでの順番.
     */
    @ConfigIgnore
    private Integer inIpDhcpOrder = null;


    /**
     * 内部IPに付与するDhcpアドレスのサブネットでの順番.
     * @return the inIpDhcpOrder
     */
    public final Integer getInIpDhcpOrder() {
        if (inIpDhcpOrder == null) {
            inIpDhcpOrder = createNetworkOrder(inIpDhcpOrderString);
        }
        return inIpDhcpOrder;
    }


    /**
     * 内部IPに付与したくないアドレスのサブネットでの順番のリスト
     * 1:前から2番目
     * -2:後ろから2番目
     * 0, -1:エラー
     * カンマ区切り
     * cloud.inIpMaskOrders=2,3
     */
    @ConfigKey(name = "cloud.inIpMaskOrders", readOnly = true)
    public String inIpMaskOrdersString = "";
    /**
     * 内部IPに付与するDhcpアドレスのサブネットでの順番.
     */
    @ConfigIgnore
    private Set<Integer> inIpMaskOrders = null;


    /**
     * 内部IPに付与するDhcpアドレスのサブネットでの順番.
     * @return the inIpDhcpOrder
     */
    public final Set<Integer> getInIpMaskOrders() {
        if (inIpMaskOrders == null) {
            inIpMaskOrders = new HashSet<Integer>();
            if (inIpMaskOrdersString != null && inIpMaskOrdersString.length() > 0) {
                for (String orderString : inIpMaskOrdersString.split(",")) {
                    inIpMaskOrders.add(createNetworkOrder(orderString));
                }
            }
        }
        return inIpMaskOrders;
    }


    /**
     * 内部IPに付与するDHCPアドレス.
     * cloud.inIpDnsAddress=10.38.11.250
     */
    @ConfigKey(name = "cloud.inIpDnsAddress", readOnly = true)
    public String inIpDnsAddress;

    /**
     * 仮想スイッチポート数.
     */
    @ConfigIgnore
    private Integer virtSwitchPortCount = null;


    /**
     * 仮想スイッチポート数.
     * @return the virtSwitchPortCount
     */
    public final Integer getVirtSwitchPortCount() {
        if (virtSwitchPortCount == null && inIpSubnetMask != null) {
            Set<Integer> disabledOrders = new HashSet<Integer>();
            disabledOrders.add(getInIpDhcpOrder());
            disabledOrders.add(getInIpGatewayOrder());
            disabledOrders.addAll(getInIpMaskOrders());
            disabledOrders.remove(null);
            virtSwitchPortCount = Math.max(0, Ipv4ConversionUtil.getHostAddressCount(inIpSubnetMask) - disabledOrders.size());
        }
        return virtSwitchPortCount;
    }


    /**
     * 仮想スイッチテンプレートリスト.
     */
    private List<SwitchTemplateDto> switchTemplateList;


    /**
     * 仮想スイッチテンプレートリスト.
     * @return List of Switch Template.
     */
    public final List<SwitchTemplateDto> getSwitchTemplateList() {
        if (switchTemplateList == null) {
            switchTemplateList = new ArrayList<SwitchTemplateDto>();
            SwitchTemplateDto stdto = new SwitchTemplateDto();
            stdto.id = 1;
            stdto.port = getVirtSwitchPortCount();
            switchTemplateList.add(stdto);
        }
        return switchTemplateList;
    }


    /**
     * NWエージェントIP（正）.
     * cloud.primaryNwAgentIp=https://127.0.0.1:443
     */
    @ConfigKey(name = "cloud.primaryNwAgentUrl", readOnly = true)
    public String primaryNwAgentUrlString;
    /**
     * NWエージェントIP（正）.
     */
    @ConfigIgnore
    private URL primaryNwAgentUrl;


    /**
     * NWエージェントIP（正）.
     * @return the virtSwitchPortCount
     * @throws MalformedURLException
     */
    public final URL getPrimaryNwAgentUrl() throws MalformedURLException {
        if (primaryNwAgentUrl == null) {
            primaryNwAgentUrl = new URL(primaryNwAgentUrlString);
        }
        return primaryNwAgentUrl;
    }


    /**
     * NWエージェントIP（副）.
     * cloud.secondaryNwAgentIp=https://192.168.30.180:443
     */
    @ConfigKey(name = "cloud.secondaryNwAgentUrl", readOnly = true)
    public String secondaryNwAgentUrlString;
    /**
     * NWエージェントIP（副）.
     */
    @ConfigIgnore
    private URL secondaryNwAgentUrl;


    /**
     * NWエージェントIP（副）.
     * @return the virtSwitchPortCount
     * @throws MalformedURLException
     */
    public final URL getSecondaryNwAgentUrl() throws MalformedURLException {
        if (secondaryNwAgentUrl == null) {
            secondaryNwAgentUrl = new URL(secondaryNwAgentUrlString);
        }
        return secondaryNwAgentUrl;
    }


    /**
     * NWエージェントのXMLRPCインターフェース名：DHCPルール追加
     */
    @ConfigKey(name = "cloud.nwaAddIpMethod", readOnly = true)
    public String nwaAddIpMethod = "network_agent.add_ip";

    /**
     * NWエージェントのXMLRPCインターフェース名：DHCPルール削除
     */
    @ConfigKey(name = "cloud.nwaRemoveIpMethod", readOnly = true)
    public String nwaRemoveIpMethod = "network_agent.del_ip";

    /**
     * NWエージェントのXMLRPCインターフェース名：NAT追加
     */
    @ConfigKey(name = "cloud.nwaAddNatMethod", readOnly = true)
    public String nwaAddNatMethod = "network_agent.add_nat";

    /**
     * NWエージェントのXMLRPCインターフェース名：NAT削除
     */
    @ConfigKey(name = "cloud.nwaRemoveNatMethod", readOnly = true)
    public String nwaRemoveNatMethod = "network_agent.del_nat";

    /**
     * NWエージェントのXMLRPCインターフェース名：詳細設定取得
     */
    @ConfigKey(name = "cloud.nwaGetConfigMethod", readOnly = true)
    public String nwaGetConfigMethod = "network_agent.get_config";

    //----------------------------------------------
    // rhev
    //----------------------------------------------

    /**
     * RHEVエージェントIP.
     * cloud.rhevAgentIp=10.38.0.29
     */
    @ConfigKey(name = "cloud.rhevAgentIp", readOnly = true)
    public String rhevAgentIp;

    /**
     * RHEVエージェントPort.
     * cloud.rhevAgentPort=6032
     */
    @ConfigKey(name = "cloud.rhevAgentPort", readOnly = true)
    public String rhevAgentPortString;
    /**
     * RHEVエージェントPort.
     */
    @ConfigIgnore
    private Integer rhevAgentPort = 0;


    /**
     * RHEVエージェントPort.
     * @return the rhevAgentPort
     */
    public final Integer getRhevAgentPort() {
        if (rhevAgentPort == 0) {
            rhevAgentPort = IntegerConversionUtil.toInteger(rhevAgentPortString);
        }
        return rhevAgentPort;
    }


    /**
     * RHEVポータルURL.
     * cloud.rhevPortalUrl=https://rhev-m.vhut.dojo.local/RHEVUserPortal/
     */
    @ConfigKey(name = "cloud.rhevPortalUrl", readOnly = true)
    public String rhevPortalUrl;
    /**
     * RHEVクラスタ名.
     * cloud.rhevCluster=Default
     */
    @ConfigKey(name = "cloud.rhevCluster", readOnly = true)
    public String rhevCluster;
    /**
     * RHEVクラスタID.
     */
    @ConfigKey(name = "cloud.rhevClusterId", readOnly = true)
    private Long rhevClusterId;


    /**
     * @return RHEVクラスタID.
     */
    public Long getRhevClusterId() {
        if (rhevClusterId == null) {
            Cluster cluster = jdbcManager.from(Cluster.class)
                .where(new SimpleWhere().eq(cluster().name(), rhevCluster))
                .getSingleResult();
            if (cluster != null) {
                rhevClusterId = cluster.id;
            }
        }
        return rhevClusterId;
    }


    /**
     * RHEVストレージドメイン名.
     * cloud.rhevStorageDomain=Default
     */
    @ConfigKey(name = "cloud.rhevStorageDomain", readOnly = true)
    public String rhevStorageDomain;
    /**
     * RHEVストレージID.
     */
    private Long rhevStorageId;


    /**
     * @return RHEVストレージID.
     */
    public Long getRhevStorageId() {
        if (rhevStorageId == null) {
            Storage storage = jdbcManager.from(Storage.class)
                .where(new SimpleWhere().eq(Names.storage()
                    .name(), rhevStorageDomain))
                .getSingleResult();
            if (storage != null) {
                rhevStorageId = storage.id;
            }
        }
        return rhevStorageId;
    }


    /**
     * マックアドレス(開始).
     * cloud.rhevMacStart=00:1A:4A:00:00:00
     */
    @ConfigKey(name = "cloud.rhevMacStart", readOnly = true)
    public String rhevMacStart;

    /**
     * マックアドレス(終了).
     * cloud.rhevMacStart=00:1A:4A:00:FF:FF
     */
    @ConfigKey(name = "cloud.rhevMacEnd", readOnly = true)
    public String rhevMacEnd;

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
