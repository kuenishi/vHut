/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.util.ArrayList;
import java.util.List;

import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.exception.CommandStatusUpdateRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>非同期系コマンドの抽象クラス.
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
public abstract class AbstractAsyncCloudDriverCommand extends AbstractCloudDriverCommand {

    private static final VhutLogger logger = VhutLogger.getLogger(AbstractAsyncCloudDriverCommand.class);


    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#updateStatus()
     */
    @Override
    public CommandStatus updateStatus() {
        try {
            List<Command> commandList = new ArrayList<Command>();
            if (isTimeOut()) {
                command.status = CommandStatus.ERROR;
                command.errorMessage = "time out";
            } else {
                commandList.add(command);
                List<Command> resCommandList = null;
                if (isPrivate) {
                    resCommandList = prcDirver.updateCommandList(commandList);
                } else {
                    // TODO パブリッククラウドの処理
                }
                if (resCommandList != null && resCommandList.size() == 1) {
                    command.status = resCommandList.get(0).status;
                }
            }
            if (command.status == CommandStatus.ERROR) {
                onCommandFailed();
            }
        } catch (Exception e) {
            throw new CommandStatusUpdateRuntimeException(operation, command.id, e);
        }
        return command.status;
    }

    /**
     * コマンド失敗時に実行されます.
     * 上位のクラスで実装される予定
     */
    protected void onCommandFailed() {

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
