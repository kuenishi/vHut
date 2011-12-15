/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import static jp.co.ntts.vhut.entity.Names.vm;

import java.io.Serializable;
import java.util.EnumMap;

import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.DiskTemplate;
import jp.co.ntts.vhut.entity.Names;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.TemplateStatus;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * <p>テンプレートを削除します.
 * <p>
 * テンプレートの状態は以下のように変化します.
 * <br>正常系
 * <li>template.status={@link TemplateStatus#AVAILABLE}
 * <li><strong>execute</strong>
 * <li>template.status={@link TemplateStatus#UNKNOWN}
 * <li><strong>finish</strong>
 * <li>DBからエントリー削除
 * <br>異常系[ドライバ実行失敗]
 * <li>template.status={@link TemplateStatus#AVAILABLE}
 * <li><strong>execute</strong>
 * <li>template.status={@link TemplateStatus#AVAILABLE}
 * <br>異常系[HyperVisorエラー時]
 * <li>template.status={@link TemplateStatus#AVAILABLE}
 * <li><strong>execute</strong>
 * <li>template.status={@link TemplateStatus#ERROR}
 * </p>
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
public class ADeleteTemplateCommand extends AbstractAsyncCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(ADeleteTemplateCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** テンプレートのID */
        TEMPLATE_ID
    }


    /**
     * デフォルトコンストラクタ.
     */
    public ADeleteTemplateCommand() {
        operation = CommandOperation.DELETE_TEMPLATE_ASYNC;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param templateId テンプレートのID
     */
    public void setParameter(Long templateId) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.TEMPLATE_ID, templateId);
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#execute()
     */
    @Override
    public CommandStatus execute() {
        if (canBeDeleted()) {
            command.status = CommandStatus.EXECUTING;
            try {
                if (isPrivate) {
                    // プライベートクラウドの処理
                    prcDirver.deleteTemplateAsync((Long) parameter.get(Key.TEMPLATE_ID), command.id);
                } else {
                    // パブリッククラウドの処理
                }
                setTemplateStatus(TemplateStatus.UNKNOWN);
            } catch (Exception e) {
                command.status = CommandStatus.ERROR;
                throw new CommandExecutionRuntimeException(operation, command.id, e);
            }
        } else {
            command.status = CommandStatus.ERROR;
        }
        return command.status;
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#finish()
     */
    @Override
    public void finish() {
        try {
            deleteTemplate();
        } catch (Exception e) {
            throw new CommandFinishRuntimeException(operation, command.id, e);
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.AbstractAsyncCloudDriverCommand#onCommandFailed()
     */
    @Override
    protected void onCommandFailed() {
        super.onCommandFailed();
        setTemplateStatus(TemplateStatus.ERROR);
    }

    private boolean setTemplateStatus(TemplateStatus status) {
        Template template = jdbcManager.from(Template.class)
            .id(parameter.get(Key.TEMPLATE_ID))
            .getSingleResult();
        if (template != null) {
            template.status = status;
            jdbcManager.update(template)
                .execute();
            return true;
        }
        return false;
    }

    private boolean deleteTemplate() {

        Template template = jdbcManager.from(Template.class)
            .leftOuterJoin(Names.template()
                .diskTemplateList())
            .leftOuterJoin(Names.template()
                .diskTemplateList()
                .storageReservationDiskTemplateMapList())
            .leftOuterJoin(Names.template()
                .networkAdapterTemplateList())
            .leftOuterJoin(Names.template()
                .commandTemplateMapList())
            .id(parameter.get(Key.TEMPLATE_ID))
            .getSingleResult();
        if (template != null) {
            //コマンドとのリンク削除
            if (template.commandTemplateMapList != null && template.commandTemplateMapList.size() > 0) {
                jdbcManager.deleteBatch(template.commandTemplateMapList)
                    .execute();
            }

            //Storage予約削除
            for (DiskTemplate diskTemplate : template.diskTemplateList) {
                if (diskTemplate.storageReservationDiskTemplateMapList != null && diskTemplate.storageReservationDiskTemplateMapList.size() > 0) {
                    jdbcManager.deleteBatch(diskTemplate.storageReservationDiskTemplateMapList)
                        .execute();
                }
            }
            //Storage削除
            if (template.diskTemplateList != null && template.diskTemplateList.size() > 0) {
                jdbcManager.deleteBatch(template.diskTemplateList)
                    .execute();
            }

            //NetworkAdapter削除
            if (template.networkAdapterTemplateList != null && template.networkAdapterTemplateList.size() > 0) {
                jdbcManager.deleteBatch(template.networkAdapterTemplateList)
                    .execute();
            }

            //VM削除
            jdbcManager.delete(template)
                .execute();
            return true;
        }
        return false;
    }

    private boolean canBeDeleted() {
        long refVmCount = jdbcManager.from(Vm.class)
            .where(new SimpleWhere().eq(vm().templateId(), parameter.get(Key.TEMPLATE_ID)))
            .getCount();
        return refVmCount == 0;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(Serializable... args) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.TEMPLATE_ID, args[0]);
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
