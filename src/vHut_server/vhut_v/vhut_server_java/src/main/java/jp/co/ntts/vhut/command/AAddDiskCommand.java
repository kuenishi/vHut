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
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>VMにディスクを追加するコマンド.
 * <br>
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
public class AAddDiskCommand extends AbstractAsyncCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(AAddDiskCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** ディスクエンティティ */
        DISK
    }


    /**
     * デフォルトコンストラクタ.
     */
    public AAddDiskCommand() {
        this.operation = CommandOperation.ADD_DISK_ASYNC;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param disk Diskエンティティ
     */
    public void setParameter(final Disk disk) {
        this.parameter = new EnumMap<Key, Serializable>(Key.class);
        this.parameter.put(Key.DISK, disk);
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
                this.result = this.prcDirver.addDiskAsync((Disk) this.parameter.get(Key.DISK), this.command.id);
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
            //TODO: ディスク追加コマンド終了時の動作について意識合わせが必要。
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
        this.parameter = new EnumMap<Key, Serializable>(Key.class);
        this.parameter.put(Key.DISK, Disk.class.cast(args[0]));
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
