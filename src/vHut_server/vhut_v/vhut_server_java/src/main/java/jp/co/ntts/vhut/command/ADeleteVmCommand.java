/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.io.Serializable;
import java.util.EnumMap;

import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.Disk;
import jp.co.ntts.vhut.entity.Names;
import jp.co.ntts.vhut.entity.NetworkAdapter;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.entity.VmStatus;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>Vmを削除するコマンド.
 * <p>
 * テンプレートの状態は以下のように変化します.
 * <br>正常系
 * <li>vm.status={@link VmStatus#DOWN}
 * <li><strong>execute</strong>
 * <li>vm.status={@link VmStatus#UNKNOWN}
 * <li><strong>finish</strong>
 * <li>DBからエントリー削除
 * <br>異常系[ドライバ実行失敗]
 * <li>vm.status={@link VmStatus#DOWN}
 * <li><strong>execute</strong>
 * <li>vm.status={@link VmStatus#UNKNOWN}
 * <br>異常系[HyperVisorエラー時]
 * <li>vm.status={@link VmStatus#DOWN}
 * <li><strong>execute</strong>
 * <li>vm.status={@link VmStatus#ERROR}
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
public class ADeleteVmCommand extends AbstractAsyncCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(ADeleteVmCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** VMのID */
        VM_ID,
        /** VMデータ消去（事後処理）の有無 */
        WILL_DELETE_VM,
    }


    /**
     * デフォルトコンストラクタ.
     */
    public ADeleteVmCommand() {
        operation = CommandOperation.DELETE_VM_ASYNC;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param vmId VmのID
     */
    public void setParameter(Long vmId) {
        setParameter(vmId, Boolean.TRUE);
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param vmId VmのID
     * @param willDeleteVm Mデータ消去（事後処理）の有無
     */
    public void setParameter(Long vmId, Boolean willDeleteVm) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.VM_ID, vmId);
        parameter.put(Key.WILL_DELETE_VM, willDeleteVm);
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#execute()
     */
    @Override
    public CommandStatus execute() {
        command.status = CommandStatus.EXECUTING;
        try {
            if (isPrivate) {
                // プライベートクラウドの処理
                prcDirver.deleteVmAsync((Long) parameter.get(Key.VM_ID), command.id);
            } else {
                // パブリッククラウドの処理
            }
            setVmStatus(VmStatus.UNKNOWN);
        } catch (Exception e) {
            command.status = CommandStatus.ERROR;
            throw new CommandExecutionRuntimeException(operation, command.id, e);
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
            if ((Boolean) parameter.get(Key.WILL_DELETE_VM)) {
                deleteVm();
            }
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
        setVmStatus(VmStatus.ERROR);
    }

    private boolean setVmStatus(VmStatus status) {
        Vm vm = jdbcManager.from(Vm.class)
            .id(parameter.get(Key.VM_ID))
            .getSingleResult();
        if (vm != null) {
            vm.status = status;
            jdbcManager.update(vm)
                .execute();
            return true;
        }
        return false;
    }

    private boolean deleteVm() {
        Vm vm = jdbcManager.from(Vm.class)
            .leftOuterJoin(Names.vm()
                .diskList())
            .leftOuterJoin(Names.vm()
                .diskList()
                .storageReservationDiskMapList())
            .leftOuterJoin(Names.vm()
                .networkAdapterList())
            .leftOuterJoin(Names.vm()
                .networkAdapterList()
                .publicIpReservationList())
            .leftOuterJoin(Names.vm()
                .commandVmMapList())
            .leftOuterJoin(Names.vm()
                .vmCloudUserMapList())
            .leftOuterJoin(Names.vm()
                .clusterReservationVmMapList())
            .id(parameter.get(Key.VM_ID))
            .getSingleResult();
        if (vm != null) {
            if (vm.commandVmMapList.size() != 0) {
                //コマンドとのリンク削除
                jdbcManager.deleteBatch(vm.commandVmMapList)
                    .execute();
            }
            if (vm.clusterReservationVmMapList.size() != 0) {
                //Cluseter予約削除処理
                jdbcManager.deleteBatch(vm.clusterReservationVmMapList)
                    .execute();
            }
            //Storage予約削除
            for (Disk disk : vm.diskList) {
                if (disk.storageReservationDiskMapList.size() != 0) {
                    jdbcManager.deleteBatch(disk.storageReservationDiskMapList)
                        .execute();
                }
            }
            if (vm.diskList.size() != 0) {
                //Storage削除
                jdbcManager.deleteBatch(vm.diskList)
                    .execute();
            }
            //***通常はDeactivateされた時点で削除済みなので以下は想定外の処理***
            //PublicIP予約削除
            for (NetworkAdapter nwa : vm.networkAdapterList) {
                if (nwa.publicIpReservationList.size() != 0) {
                    jdbcManager.deleteBatch(nwa.publicIpReservationList)
                        .execute();
                }
            }

            if (vm.networkAdapterList.size() != 0) {
                //NetworkAdapter削除
                jdbcManager.deleteBatch(vm.networkAdapterList)
                    .execute();
            }

            //***通常はDeactivateされた時点で削除済みなので以下は想定外の処理***
            if (vm.vmCloudUserMapList.size() != 0) {
                //利用者とのリンク削除
                jdbcManager.deleteBatch(vm.vmCloudUserMapList)
                    .execute();
            }

            //VM削除
            jdbcManager.delete(vm)
                .execute();
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(Serializable... args) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.VM_ID, args[0]);
        if (args.length == 2) {
            parameter.put(Key.WILL_DELETE_VM, args[1]);
        } else {
            parameter.put(Key.WILL_DELETE_VM, Boolean.TRUE);
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
