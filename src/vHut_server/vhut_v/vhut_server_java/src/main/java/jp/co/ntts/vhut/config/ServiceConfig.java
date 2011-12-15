/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.config;

import static jp.co.ntts.vhut.entity.Names.role;
import static jp.co.ntts.vhut.entity.Names.vhutUser;
import jp.co.ntts.vhut.entity.ExternalIpRequestMode;
import jp.co.ntts.vhut.entity.Role;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.task.ServiceTask;

import org.seasar.chronos.core.TaskTrigger;
import org.seasar.chronos.core.trigger.CCronTrigger;
import org.seasar.config.core.config.annotation.Config;
import org.seasar.config.core.config.annotation.ConfigIgnore;
import org.seasar.config.core.config.annotation.ConfigKey;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.exception.ParseRuntimeException;

/**
 * <p>サーバモジュール関連のコンフィグレーション
 * <br>
 * <p>service.propertiesの値をインスタンスとして提供します.
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
public class ServiceConfig {

    /**
     * {@link ServiceTask}用のトリガーを取得します。のデフォルト設定.
     */
    private static final String SERVICE_TASK_DEFAULT_CRON_EXPRESSION = "*/10 * * * * ?";

    /** JDBC管理クラス */
    public JdbcManager jdbcManager;

    //--------------------------------------
    // prefix
    //--------------------------------------

    /**
     * システム全体で使うサフィックス
     */
    @ConfigKey(name = "service.suffix", readOnly = true)
    public String suffix;

    /**
     * アプリケーション関連VM作成時に付与するサービス側のプレフィックスです.
     */
    @ConfigKey(name = "service.app.vm.prefix", readOnly = true)
    public String applicationVmPrefix;

    /**
     * アプリケーションインスタンス関連VM作成時に付与するサービス側のプレフィックスです.
     */
    @ConfigKey(name = "service.ai.vm.prefix", readOnly = true)
    public String applicationInstanceVmPrefix;

    /**
     * リリースドアプリケーション関連テンプレート作成時に付与するサービス側のプレフィックスです.
     */
    @ConfigKey(name = "service.rapp.template.prefix", readOnly = true)
    public String releasedApplicationTemplatePrefix;

    //--------------------------------------
    // user
    //--------------------------------------

    /**
     * 管理者アカウント
     */
    @ConfigKey(name = "service.adminAccount", readOnly = true)
    public String adminAccount;

    /**
     * 管理者ID
     */
    @ConfigIgnore
    private Long adminVhutUserId = null;


    /**
     * 管理者ID.
     * @return the guestRoleId
     */
    public final Long getAdminVhutUserId() {
        if (adminVhutUserId == null) {
            if (adminAccount == null || jdbcManager == null) {
                return null;
            }
            VhutUser admin = jdbcManager.from(VhutUser.class)
                .where(new SimpleWhere().eq(vhutUser().account(), adminAccount)
                    .eq(vhutUser().sysLock(), true))
                .getSingleResult();
            if (admin != null) {
                adminVhutUserId = admin.id;
            }
        }
        return adminVhutUserId;
    }


    /**
     * 管理者ロールの名称
     */
    @ConfigKey(name = "service.adminRoleName", readOnly = true)
    public String adminRoleName;

    /**
     * 管理者ロールのID
     */
    @ConfigIgnore
    private Long adminRoleId = null;


    /**
     * 初期ロールのID.
     * @return the adminRoleId
     */
    public final Long getAdminRoleId() {
        if (adminRoleId == null) {
            if (adminRoleName == null || jdbcManager == null) {
                return null;
            }
            Role adminRole = jdbcManager.from(Role.class)
                .where(new SimpleWhere().eq(role().name(), adminRoleName)
                    .eq(role().sysLock(), true))
                .getSingleResult();
            if (adminRole != null) {
                adminRoleId = adminRole.id;
            }
        }
        return adminRoleId;
    }


    /**
     * ゲストロールの名称
     */
    @ConfigKey(name = "service.guestRoleName", readOnly = true)
    public String guestRoleName;

    /**
     * ゲストロールのID
     */
    @ConfigIgnore
    private Long guestRoleId = null;


    /**
     * 初期ロールのID.
     * @return the guestRoleId
     */
    public final Long getGuestRoleId() {
        if (guestRoleId == null) {
            if (guestRoleName == null || jdbcManager == null) {
                return null;
            }
            Role defaultRole = jdbcManager.from(Role.class)
                .where(new SimpleWhere().eq(role().name(), guestRoleName)
                    .eq(role().sysLock(), true))
                .getSingleResult();
            if (defaultRole != null) {
                guestRoleId = defaultRole.id;
            }
        }
        return guestRoleId;
    }


    //--------------------------------------
    // path
    //--------------------------------------

    /**
     * 画像ファイルの置き場所（絶対パス）
     */
    @ConfigKey(name = "service.imageRoot", readOnly = true)
    public String imageRoot;

    //--------------------------------------
    // task
    //--------------------------------------
    /**
     * サービス関連の定期実行タスクのトリガ設定
     */
    @ConfigKey(name = "service.serviceTaskCronExpression", readOnly = true)
    public String serviceTaskCronExpression;
    @ConfigIgnore
    private TaskTrigger serviceTaskTrigger;


    /**
     * {@link ServiceTask}用のトリガーを取得します。
     * service.propertiesのservice.serviceTaskCronExpressionで変更が可能
     * デフォルトは毎分実行
     * @return {@link ServiceTask}用のトリガー
     */
    public TaskTrigger getServiceTaskTrigger() {
        if (serviceTaskTrigger == null) {
            try {
                serviceTaskTrigger = new CCronTrigger(serviceTaskCronExpression);
            } catch (ParseRuntimeException e) {
                serviceTaskTrigger = new CCronTrigger(SERVICE_TASK_DEFAULT_CRON_EXPRESSION);
            }
        }
        return serviceTaskTrigger;
    }


    /**
     * サービス関連の定期実行タスクのトリガ設定
     */
    @ConfigKey(name = "service.serviceCheckTaskCronExpression", readOnly = true)
    public String serviceCheckTaskCronExpression;
    @ConfigIgnore
    private TaskTrigger serviceCheckTaskTrigger;


    /**
     * @return
     */
    public TaskTrigger getServiceCheckTaskTrigger() {
        if (serviceCheckTaskTrigger == null) {
            try {
                serviceCheckTaskTrigger = new CCronTrigger(serviceCheckTaskCronExpression);
            } catch (Exception e) {
                serviceCheckTaskTrigger = new CCronTrigger(SERVICE_TASK_DEFAULT_CRON_EXPRESSION);
            }
        }
        return serviceCheckTaskTrigger;
    }


    //--------------------------------------
    // resource
    //--------------------------------------

    //--------------------------------------
    // network
    //--------------------------------------

    /**
     * 外部IPの申請モード.
     * default:AUTO
     */
    @ConfigKey(name = "service.exIpRequestMode", readOnly = true)
    public String exIpRequestModeString;
    @ConfigIgnore
    private ExternalIpRequestMode exIpRequestMode;


    /**
     * @return 外部IPの申請モード.
     */
    public ExternalIpRequestMode getExIpRequestMode() {
        if (exIpRequestMode == null) {
            try {
                exIpRequestMode = ExternalIpRequestMode.valueOf(exIpRequestModeString);
            } catch (Exception e) {
                exIpRequestMode = ExternalIpRequestMode.AUTO;
            }
        }
        return exIpRequestMode;
    }

    //--------------------------------------
    // rhev
    //--------------------------------------
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
