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
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.entity.VmStatus;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>VMを作成するコマンド. 
 * <p>
 * テンプレートの状態は以下のように変化します.
 * <br>正常系
 * <li>template.status={@link VmStatus#UNKNOWN}
 * <li><strong>execute</strong>
 * <li>template.status={@link VmStatus#DEVELOPPING}
 * <li><strong>finish</strong>
 * <li>template.status={@link VmStatus#DOWN}
 * <br>異常系[ドライバ実行失敗]
 * <li>template.status={@link VmStatus#UNKNOWN}
 * <li><strong>execute</strong>
 * <li>template.status={@link VmStatus#UNKNOWN}
 * <br>異常系[HyperVisorエラー時]
 * <li>template.status={@link VmStatus#UNKNOWN}
 * <li><strong>execute</strong>
 * <li>template.status={@link VmStatus#DEVELOPPING}
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
public class ACreateVmCommand extends AbstractAsyncCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(ACreateVmCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** VM */
        VM
    }


    /**
     * デフォルトコンストラクタ.
     */
    public ACreateVmCommand() {
        this.operation = CommandOperation.CREATE_VM_ASYNC;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param vm VMエンティティ
     */
    public void setParameter(final Vm vm) {
        this.parameter = new EnumMap<Key, Serializable>(Key.class);
        this.parameter.put(Key.VM, vm);
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
                // プライベートクラウドの処理
                Vm local = (Vm) this.parameter.get(Key.VM);
                this.result = this.prcDirver.createVmAsync(local, this.command.id);
                local = jdbcManager.from(Vm.class)
                    .leftOuterJoin(vm().networkAdapterList())
                    .leftOuterJoin(vm().diskList())
                    .id(local.id)
                    .getSingleResult();
                Vm remote = (Vm) this.result;
                updateSynchronizedEntity(local, remote);

                //                this.command.status = CommandStatus.SUCCESS;
                this.command.status = CommandStatus.EXECUTING;

            } else {
                // パブリッククラウドの処理
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
            // ステータスをDOWNに変更
            Vm vm = (Vm) this.parameter.get(Key.VM);
            vm = jdbcManager.from(Vm.class)
                .leftOuterJoin(vm().networkAdapterList())
                .leftOuterJoin(vm().diskList())
                .id(vm.id)
                .getSingleResult();
            vm.status = VmStatus.DOWN;
            this.jdbcManager.update(vm)
                .includes(vm().status())
                .execute();

        } catch (final Exception e) {
            throw new CommandFinishRuntimeException(this.operation, this.command.id, e);
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(Serializable... args) {
        setParameter(Vm.class.cast(args[0]));
        //        Vm vm = Vm.class.cast(args[0]);
        //        this.parameter = new EnumMap<Key, Serializable>(Key.class);
        //        this.parameter.put(Key.VM, Vm.class.cast(args[0]));
        //        Vm vm2 = Vm.class.cast(args[0]);
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
