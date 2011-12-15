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
 * <p>ディスクを削除するコマンド.
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
public class ARemoveDiskCommand extends AbstractAsyncCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(ARemoveDiskCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** VMのID */
        VM_ID
        /** DISKのID */
        , DISK_ID
    }


    /**
     * デフォルトコンストラクタ.
     */
    public ARemoveDiskCommand() {
        operation = CommandOperation.REMOVE_DISK_ASYNC;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param vmId VMのID
     * @param diskId DiskのID
     */
    public void setParameter(Long vmId, Long diskId) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.VM_ID, vmId);
        parameter.put(Key.DISK_ID, diskId);
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
                prcDirver.removeDiskAsync((Long) parameter.get(Key.VM_ID), (Long) parameter.get(Key.DISK_ID), command.id);
            } else {
                // パブリッククラウドの処理
            }
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
            removeDisk();
        } catch (Exception e) {
            throw new CommandFinishRuntimeException(operation, command.id, e);
        }
    }

    private boolean removeDisk() {
        Disk disk = jdbcManager.from(Disk.class)
            .id(parameter.get(Key.DISK_ID))
            .getSingleResult();
        if (disk != null) {
            jdbcManager.delete(disk)
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
        parameter.put(Key.DISK_ID, args[1]);
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
