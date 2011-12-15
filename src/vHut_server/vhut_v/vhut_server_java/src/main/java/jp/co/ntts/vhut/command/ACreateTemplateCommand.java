/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import static jp.co.ntts.vhut.entity.Names.template;

import java.io.Serializable;
import java.util.EnumMap;

import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.TemplateStatus;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.exception.InternalRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>テンプレートを作成するコマンド.
 * <p>
 * テンプレートの状態は以下のように変化します.
 * <br>正常系
 * <li>template.status={@link TemplateStatus#UNKNOWN}
 * <li><strong>execute</strong>
 * <li>template.status={@link TemplateStatus#DEVELOPPING}
 * <li><strong>finish</strong>
 * <li>template.status={@link TemplateStatus#AVAILABLE}
 * <br>異常系[ドライバ実行失敗]
 * <li>template.status={@link TemplateStatus#UNKNOWN}
 * <li><strong>execute</strong>
 * <li>template.status={@link TemplateStatus#UNKNOWN}
 * <br>異常系[HyperVisorエラー時]
 * <li>template.status={@link TemplateStatus#UNKNOWN}
 * <li><strong>execute</strong>
 * <li>template.status={@link TemplateStatus#DEVELOPPING}
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
public class ACreateTemplateCommand extends AbstractAsyncCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(ACreateTemplateCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** テンプレート */
        TEMPLATE,
        /** VMのID */
        VM_ID
    }


    /**
     * デフォルトコンストラクタ.
     */
    public ACreateTemplateCommand() {
        this.operation = CommandOperation.CREATE_TEMPLATE_ASYNC;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param template Tempalteエンティティ
     * @param vmId VmエンティティのID
     */
    public void setParameter(final Template template, final Long vmId) {
        this.parameter = new EnumMap<Key, Serializable>(Key.class);
        this.parameter.put(Key.TEMPLATE, template);
        this.parameter.put(Key.VM_ID, vmId);
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#execute()
     */
    @Override
    public CommandStatus execute() {
        this.command.status = CommandStatus.EXECUTING;
        try {
            if (this.isPrivate) {
                //プライベートクラウドの処理

                Template paramTemplate = (Template) this.parameter.get(Key.TEMPLATE);
                long vmId = (Long) this.parameter.get(Key.VM_ID);

                this.result = this.prcDirver.createTemplateAsync(paramTemplate, vmId, this.command.id);
                Template resultTemplate = (Template) this.result;

                Template localTemplate = jdbcManager.from(Template.class)
                    .id(paramTemplate.id)
                    .getSingleResult();

                localTemplate.status = resultTemplate.status;

                this.jdbcManager.update(localTemplate)
                    .includes(template().status())
                    .execute();
            } else {
                //パブリッククラウドの処理
            }
        } catch (final Exception e) {
            this.command.status = CommandStatus.ERROR;
            throw new CommandExecutionRuntimeException(this.operation, this.command.id, e);
        }
        return this.command.status;
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#finish()
     */
    @Override
    public void finish() {
        try {
            //テンプレートの更新
            if (this.result != null) {
                //NEXT 強引にAVAILABLEにしているがUPDATEでテンプレートすべてを取得するほうがより正確。
                final Template resultTemplate = (Template) this.result;
                resultTemplate.status = TemplateStatus.AVAILABLE;

                Template localTemplate = jdbcManager.from(Template.class)
                    .id(resultTemplate.id)
                    .getSingleResult();

                localTemplate.status = resultTemplate.status;

                this.jdbcManager.update(localTemplate)
                    .includes(template().status())
                    .execute();
            } else {
                throw new InternalRuntimeException("ACreateTemplateCommand has no result.");
            }
        } catch (final Exception e) {
            throw new CommandFinishRuntimeException(this.operation, this.command.id, e);
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(final Serializable... args) {
        this.parameter = new EnumMap<Key, Serializable>(Key.class);
        this.parameter.put(Key.TEMPLATE, Template.class.cast(args[0]));
        this.parameter.put(Key.VM_ID, Long.class.cast(args[1]));
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
