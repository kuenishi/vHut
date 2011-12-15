/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.factory;

import jp.co.ntts.vhut.command.ICommand;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;

import org.seasar.framework.container.SingletonS2Container;

/**
 * <p>コマンドを生成するファクトリ.
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
public class CommandFactory {

    /**
     * オペレーションのタイプを指定して、コマンドのインスタンスを取得します.
     * 
     * @param operation オペレーション
     * @param cloudId クラウドID
     * @return コマンド
     */
    public ICommand newCommand(CommandOperation operation, long cloudId) {
        ICommand command;
        command = SingletonS2Container.getComponent(operation.getComponentName());
        command.init(cloudId);
        return command;
    }

    /**
     * コマンドエンティティを指定して、コマンドのインスタンスを取得します.
     * 
     * @param cmd コマンドエンティティ
     * @return コマンド
     */
    public ICommand newCommand(Command cmd) {
        ICommand command;
        command = SingletonS2Container.getComponent(cmd.operation.getComponentName());
        command.init(cmd);
        return command;
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
