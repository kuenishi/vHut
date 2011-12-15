/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import jp.co.ntts.vhut.driver.NwDriver;
import jp.co.ntts.vhut.util.VhutLogger;

/**
 * <p>ネットワークドライバ用のコマンドの抽象クラス.
 * <br>今のところネットワーク系の処理で非同期は
 * 存在しないのですべてこのクラスを基底とします。
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
public abstract class AbstractNetworkDriverCommand extends AbstractCommand {

    private static final VhutLogger logger = VhutLogger.getLogger(AbstractNetworkDriverCommand.class);

    public NwDriver nwDriver;

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
