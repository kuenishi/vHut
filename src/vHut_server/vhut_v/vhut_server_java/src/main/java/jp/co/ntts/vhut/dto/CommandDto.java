/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import java.sql.Timestamp;

import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.CommandTemplateMap;
import jp.co.ntts.vhut.entity.CommandVmMap;
import jp.co.ntts.vhut.entity.VhutUser;

/**
 * コマンドリスト表示に必要なデータ要素.
 *
 * @version 1.0.1
 * @author NTT Software Corporation.
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class CommandDto {

    /** コマンドID */
    public Long commandId;

    /** オペレーション */
    public CommandOperation operation;

    /** 開始日時 */
    public Timestamp startTime;

    /** 終了日時 */
    public Timestamp endTime;

    /** 状態 */
    public CommandStatus status;

    /** vm名 */
    public String vmName;

    /** vmID */
    public Long vmId;

    /** テンプレート名 */
    public String templateName;

    /** テンプレートID */
    public Long templateId;

    /** アプリケーション名 */
    public String appName;

    /** アプリケーションID */
    public Long appId;

    /** アプリケーションインスタンスグループ名 */
    public String aigName;

    /** アプリケーションインスタンスグループID */
    public Long aigId;

    /** 所有者ユーザID */
    public Long vhutUserId;

    /** 所有者ユーザアカウント */
    public String vhutUserAccount;

    /** 所有者ユーザ名 */
    public String vhutUserFirstName;

    /** 所有者ユーザ姓 */
    public String vhutUserLastName;


    /**
     * コンストラクタ.
     * @param command コマンドエンティティ
     * @return commandDto
     */
    public static CommandDto newFromCommand(Command command) {
        CommandDto dto = new CommandDto();
        dto.commandId = command.id;
        dto.operation = command.operation;
        dto.startTime = command.startTime;
        dto.endTime = command.endTime;
        dto.status = command.status;
        if (command.commandVmMapList != null && command.commandVmMapList.size() > 0) {
            CommandVmMap map = command.commandVmMapList.get(0);
            dto.vmId = map.vmId;
            if (map.vm != null) {
                dto.vmName = map.vm.name;
            }
        }
        if (command.commandTemplateMapList != null && command.commandTemplateMapList.size() > 0) {
            CommandTemplateMap map = command.commandTemplateMapList.get(0);
            dto.templateId = map.templateId;
            if (map.template != null) {
                dto.vmName = map.template.name;
            }
        }
        return dto;
    }

    /**
     * Application関連の値を更新.
     * @param app Application
     */
    public void updateByApplication(Application app) {
        if (app == null) {
            return;
        }
        appId = app.id;
        appName = app.name;
        updateByVhutUser(app.vhutUser);
    }

    /**
     * VhutUser関連の値を更新.
     * @param vhutUser ユーザ
     */
    private void updateByVhutUser(VhutUser vhutUser) {
        if (vhutUser == null) {
            return;
        }
        vhutUserId = vhutUser.id;
        vhutUserAccount = vhutUser.account;
        vhutUserFirstName = vhutUser.firstName;
        vhutUserLastName = vhutUser.lastName;
    }

    /**
     * @param aig
     */
    public void updateByApplicationInstanceGroup(ApplicationInstanceGroup aig) {
        if (aig == null) {
            return;
        }
        aigId = aig.id;
        aigName = aig.name;
        updateByVhutUser(aig.vhutUser);
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
