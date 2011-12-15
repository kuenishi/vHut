package jp.co.ntts.vhut.task;

import jp.co.ntts.vhut.driver.NwDriver;

import org.seasar.framework.log.Logger;

//@Task
//@CronTrigger(expression = "*/10 * * * * ?")
//@Task
//@NonDelayTrigger
public class LogTestTask {
    private static final Logger log = Logger.getLogger(LogTestTask.class);

    //    public void doExecute() {
    //        logger.log("IDRV0003", new Object[]{"REPLY","VHUT_Agent","12345678-1234-5678-1234567812345678" });
    //    }

    public NwDriver nwDriver;


    /**
     * 初期化処理.
     */
    public void initialize() {
        log.debug("S2Chronos initialize");
    }

    /**
     * 開始処理
     */
    public void start() {
        log.debug("S2Chronos start");
    }

    /**
     * タスク処理
     */
    public void doExecute() {
        log.debug("S2Chronos doExecute");
        nwDriver.addNat("10.38.10.231", "192.168.10.11");
    }

    /**
     * 終了処理
     */
    public void end() {
        log.debug("S2Chronos end");
    }

    /**
     * 破棄処理
     */
    public void destroy() {
        log.debug("S2Chronos destroy");
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
