/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import static jp.co.ntts.vhut.entity.Names.clusterReservationVmMap;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;

import jp.co.ntts.vhut.entity.ClusterReservationVmMap;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.Names;
import jp.co.ntts.vhut.entity.VmCloudUserMap;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * <p>ネットワークアダプターを削除するコマンド.
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
public class SRemoveUserCommand extends AbstractCloudDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(SRemoveUserCommand.class);


    /** パラメータ保存用のキー */
    protected static enum Key {
        /** VMのID */
        VM_ID,
        /** ユーザのID */
        USER_ID
    }


    /**
     * デフォルトコンストラクタ.
     */
    public SRemoveUserCommand() {
        operation = CommandOperation.REMOVE_USER;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param vmId VMのID
     * @param userId ユーザのID
     */
    public void setParameter(Long vmId, Long userId) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.VM_ID, vmId);
        parameter.put(Key.USER_ID, userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jp.co.ntts.vhut.command.ICommand#execute()
     */
    @Override
    public CommandStatus execute() {
        command.status = CommandStatus.ERROR;
        try {
            if (isPrivate) {
                // プライベートクラウドの処理
                prcDirver.removeUser((Long) parameter.get(Key.VM_ID), (Long) parameter.get(Key.USER_ID));
            } else {
                // パブリッククラウドの処理
            }
            command.status = CommandStatus.SUCCESS;
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
            removeVmCloudUserMap();
            removeReservation();
        } catch (Exception e) {
            throw new CommandFinishRuntimeException(operation, command.id, e);
        }
    }

    private boolean removeVmCloudUserMap() {

        VmCloudUserMap vmCloudUserMap = jdbcManager.from(VmCloudUserMap.class)
            .where(new SimpleWhere().eq(Names.vmCloudUserMap()
                .cloudUserId(), parameter.get(Key.USER_ID))
                .eq(Names.vmCloudUserMap()
                    .vmId(), parameter.get(Key.VM_ID)))
            .getSingleResult();

        if (vmCloudUserMap != null) {
            jdbcManager.delete(vmCloudUserMap)
                .execute();
        }

        return true;
    }

    private boolean removeReservation() {
        List<ClusterReservationVmMap> mapList = jdbcManager.from(ClusterReservationVmMap.class)
            .where(new SimpleWhere().eq(clusterReservationVmMap().vmId(), parameter.get(Key.VM_ID)))
            .getResultList();

        if (mapList.size() < 1) {
            //警告
        } else {

            jdbcManager.deleteBatch(mapList)
                .execute();
        }
        return true;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(Serializable... args) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.VM_ID, args[0]);
        parameter.put(Key.USER_ID, args[1]);
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
