/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.CommandExecutionRuntimeException;
import jp.co.ntts.vhut.exception.CommandFinishRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>指定したクラスターに関連するVMの一覧を取得するコマンド.
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
public class SGetVmsByClusterIdCommand extends AbstractCloudDriverCommand {

    private static final VhutLogger logger = VhutLogger.getLogger(SGetVmsByClusterIdCommand.class);

    /**
     * 外部ファイルcloud.propertiesを読み込んで設定値を提供します.
     */
    public CloudConfig cloudConfig;


    /** パラメータ保存用のキー */
    protected static enum Key {
        /** クラスターのID */
        CLUSTER_ID
    }


    /**
     * デフォルトコンストラクタ.
     */
    public SGetVmsByClusterIdCommand() {
        operation = CommandOperation.GET_VMS_BY_CLUSTER_ID;
    }

    /**
     * コマンド実行時に必要なパラメータを設定します.
     * <p> {@link ICommand#init(long)}を用いて生成した後に
     * 引数が必要なコマンドに対して個々の引数を設定するための関数です。
     * @param clusterId クラスターのID
     */
    public void setParameter(Long clusterId) {
        parameter = new EnumMap<Key, Serializable>(Key.class);
        parameter.put(Key.CLUSTER_ID, clusterId);
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
                result = new ArrayList<Vm>(prcDirver.getVmsByClusterId((Long) parameter.get(Key.CLUSTER_ID)));
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
            updateSynchronizedEntiteis(Vm.class);
        } catch (Exception e) {
            throw new CommandFinishRuntimeException(operation, command.id, e);
        }
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#setParameter(java.io.Serializable[])
     */
    @Override
    public void setParameter(Serializable... args) {
        if (args.length == 1) {
            setParameter(Long.class.cast(args[0]));
        } else {
            throw new UnsupportedOperationException();
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
