/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.io.Serializable;
import java.util.EnumMap;

import jp.co.ntts.vhut.driver.NwDriver;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>ネットワーク管理サーバに問い合わせて、DHCPルールから指定したIP+MACに関連したものを削除します.
 * <br>
 * <p>{@link NwDriver#removeIp(String, String)}に依存しています.
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
public class SRemoveIpCommand extends AbstractNetworkDriverCommand {
    private static final VhutLogger logger = VhutLogger.getLogger(SRemoveIpCommand.class);


    /** パラメータ保存用のキー */
    protected enum Key {
        /** IPアドレス */
        IP,
        /** マックアドレス */
        MAC
    }


    /**
     * コンストラクタ.
     * フレームワークから呼び出されます
     */
    public SRemoveIpCommand() {
        operation = CommandOperation.REMOVE_IP;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param ip VMに割り当てたいIPアドレス
     * @param mac VMのMACアドレス
     */
    public void setParameter(String ip, String mac) {
        parameter = new EnumMap<Key, Object>(Key.class);
        parameter.put(Key.IP, ip);
        parameter.put(Key.MAC, mac);
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#execute()
     */
    @Override
    public CommandStatus execute() {
        command.status = CommandStatus.ERROR;
        try {
            result = nwDriver.removeIp((String) parameter.get(Key.IP), (String) parameter.get(Key.MAC));
            command.status = CommandStatus.SUCCESS;
        } catch (Exception e) {
            throw new CommandExecutionRuntimeException(operation, command.id, e);
        }
        return command.status;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#finish()
     */
    @Override
    public void finish() {

    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameter(Serializable... args) {
        parameter = new EnumMap<Key, Object>(Key.class);
        parameter.put(Key.IP, args[0]);
        parameter.put(Key.MAC, args[1]);
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
