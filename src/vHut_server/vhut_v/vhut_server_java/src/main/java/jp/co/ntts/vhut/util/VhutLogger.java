/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.util.HashMap;
import java.util.Map;

import jp.co.ntts.vhut.VhutModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.seasar.framework.message.MessageFormatter;

/**
 * <p>vHut独自ロガー
 * <br>
 * <p>受け渡されるメッセージの前段に以下の情報を追加します。
 * <ul>
 * <li>プロセスID
 * <li>vHutコンポーネント名：vHut_Server
 * <li>vHutモジュール名
 * <li>vHutトランザクションID
 * <li>ログレベル
 * <li>処理種別
 * <li>vHutモジュール名
 * </ul>
 * <p>
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
public class VhutLogger {

    private static final String GLOBAL_ID_BASE = System.currentTimeMillis() + "/";

    private static int nextId = 0;

    /**
     * Seaser Lgging Utilityのメッセージコード長の最小値
     */
    private static final int MESSAGECODE_LENGTH_MIN = 6;

    /**
     * Seaser Lgging Utilityのメッセージコードの連番の長さ
     */
    private static final int MESSAGECODE_NUMBER_LENGTH = 4;

    /**
     * ログに出力するvHutコンポーネント名.
     */
    private static final String COMPONENT_NAME = "vHut_Server";

    /**
     * ログレベルの出力文字列：DEBUG
     */
    private static final String LEVEL_DEBUG = "DEBUG";
    /**
     * ログレベルの出力文字列：INFO
     */
    private static final String LEVEL_INFO = "INFO";
    /**
     * ログレベルの出力文字列：WARN
     */
    private static final String LEVEL_WARN = "WARNNING";
    /**
     * ログレベルの出力文字列：ERROR
     */
    private static final String LEVEL_ERROR = "ERR";
    /**
     * ログレベルの出力文字列：FATAL
     */
    private static final String LEVEL_FATAL = "EMG";

    /**
     * 処理種別:トランザクション開始.
     */
    private static final String TYPE_START = "START";
    /**
     * 処理種別:トランザクション終了.
     */
    private static final String TYPE_END = "END";
    /**
     * 処理種別:外部呼出し.
     */
    private static final String TYPE_REQUEST = "REQUEST";
    /**
     * 処理種別:外部応答.
     */
    private static final String TYPE_REPLY = "REPLY";

    /**
     * トランザクションのみ出力する特殊スコープ.
     */
    private static final String SCOPE_TRANSACTION = "TRANSACTION";

    /**
     * ロガーを格納する配列.
     */
    private static final Map<Class, VhutLogger> loggers = new HashMap<Class, VhutLogger>();

    /**
     * 初期化済みフラグ.
     */
    private static boolean initialized;

    /**
     * プロセスID.
     */
    private static String pid;

    /**
     * common-loggingのロガー（通常）.
     */
    public Log log;

    /**
     * common-loggingのロガー.
     */
    public Log transactionLog;


    /**
     * コンストラクタ
     * @param clazz スコープ
     */
    private VhutLogger(Class clazz) {
        transactionLog = LogFactory.getLog(SCOPE_TRANSACTION);
        log = LogFactory.getLog(clazz);
    }

    /**
     * {@link Logger}を返します.
     *
     * @param clazz
     * @return {@link Logger}
     */
    public static synchronized VhutLogger getLogger(final Class clazz) {
        if (!initialized) {
            initialize();
        }
        VhutLogger logger = loggers.get(clazz);
        if (logger == null) {
            logger = new VhutLogger(clazz);
            loggers.put(clazz, logger);
        }
        return logger;
    }

    /**
     * {@link Logger}を初期化します。
     */
    public static synchronized void initialize() {
        initialized = true;
        pid = "NotDefined";
        //        try {
        //            MonitoredHost host = MonitoredHost.getMonitoredHost("localhost");
        //            String[] pids = (String[]) host.activeVms().toArray(new String[0]);
        //            if(pids.length > 0) {
        //               pid = pids[0];
        //            }
        //        } catch (MonitorException e) {
        //        } catch (URISyntaxException e) {
        //        }
    }

    /**
     * リソースを開放します。
     */
    public static synchronized void dispose() {
        LogFactory.releaseAll();
        loggers.clear();
        initialized = false;
    }

    /**
     * トランザクションIDを生成します.
     * <p>現在時刻のミリ秒に連番を付加したものを生成します。
     *
     * @return 生成されたトランザクションID
     */
    public static synchronized String createTransactionId() {
        return GLOBAL_ID_BASE + Integer.toString(nextId++);
    }

    /**
     * DEBUG情報が出力されるかどうかを返します。
     *
     * @return DEBUG情報が出力されるかどうか
     */
    public final boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * DEBUG情報を出力します。
     *
     * @param message 任意メッセージ
     * @param throwable スタックトレースを出す例外
     */
    public final void debug(Object message, Throwable throwable) {
        if (isDebugEnabled()) {
            log.debug(message, throwable);
        }
    }

    /**
     * DEBUG情報を出力します。
     *
     * @param message 任意メッセージ
     */
    public final void debug(Object message) {
        if (isDebugEnabled()) {
            log.debug(message);
        }
    }

    /**
     * INFO情報が出力されるかどうかを返します。
     *
     * @return INFO情報が出力されるかどうか
     */
    public final boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * INFO情報を出力します。
     *
     * @param message 任意メッセージ
     * @param throwable スタックトレースを出す例外
     */
    public final void info(Object message, Throwable throwable) {
        if (isInfoEnabled()) {
            log.info(message, throwable);
        }
    }

    /**
     * INFO情報を出力します。
     *
     * @param message 任意メッセージ
     */
    public final void info(Object message) {
        if (isInfoEnabled()) {
            log.info(message);
        }
    }

    /**
     * WARN情報を出力します。
     *
     * @param message 任意メッセージ
     * @param throwable スタックトレースを出す例外
     */
    public final void warn(final Object message, final Throwable throwable) {
        log.warn(message, throwable);
    }

    /**
     * WARN情報を出力します。
     *
     * @param message 任意メッセージ
     */
    public final void warn(final Object message) {
        log.warn(message);
    }

    /**
     * ERROR情報を出力します。
     *
     * @param message 任意メッセージ
     * @param throwable スタックトレースを出す例外
     */
    public final void error(Object message, Throwable throwable) {
        log.error(message, throwable);
    }

    /**
     * ERROR情報を出力します。
     *
     * @param message 任意メッセージ
     */
    public final void error(Object message) {
        log.error(message);
    }

    /**
     * FATAL情報を出力します。
     *
     * @param message 任意メッセージ
     * @param throwable スタックトレースを出す例外
     */
    public final void fatal(Object message, Throwable throwable) {
        log.fatal(message, throwable);
    }

    /**
     * FATAL情報を出力します。
     *
     * @param message 任意メッセージ
     */
    public final void fatal(Object message) {
        log.fatal(message);
    }

    /**
     * ログを出力します。
     *
     * @param throwable スタックトレースを出す例外
     */
    public final void log(Throwable throwable) {
        error(throwable.getMessage(), throwable);
    }

    /**
     * ログを出力します.
     *
     * @param messageCode Seaser Lgging Utilityのメッセージコード
     * @param args メッセージに補完する情報
     */
    public final void log(String messageCode, Object[] args) {
        log(messageCode, args, null);
    }

    /**
     * ログを出力します.
     *
     * @param messageCode Seaser Lgging Utilityのメッセージコード
     * @param throwable スタックトレースを出す例外
     */
    public final void log(String messageCode, Throwable throwable) {
        log(messageCode, new Object[0], throwable);
    }

    /**
     * ログを出力します.
     *
     * @param messageCode Seaser Lgging Utilityのメッセージコード
     * @param args メッセージに補完する情報
     * @param throwable スタックトレースを出す例外
     */
    public final void log(String messageCode, Object[] args, Throwable throwable) {
        char levelChar = messageCode.charAt(0);
        if (isEnabledFor(levelChar)) {
            String message = MessageFormatter.getSimpleMessage(messageCode, args);
            switch (levelChar) {
                case 'D':
                    log.debug(message, throwable);
                    break;
                case 'I':
                    log.info(message, throwable);
                    break;
                case 'W':
                    log.warn(message, throwable);
                    break;
                case 'E':
                    log.error(message, throwable);
                    break;
                case 'F':
                    log.fatal(message, throwable);
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(levelChar));
            }
        }
    }

    /**
     * ロガーで当該レベルが有効化されているかしらべる.
     * @param levelChar
     * @return
     */
    private boolean isEnabledFor(final char levelChar) {
        switch (levelChar) {
            case 'D':
                return log.isDebugEnabled();
            case 'I':
                return log.isInfoEnabled();
            case 'W':
                return log.isWarnEnabled();
            case 'E':
                return log.isErrorEnabled();
            case 'F':
                return log.isFatalEnabled();
            default:
                throw new IllegalArgumentException(String.valueOf(levelChar));
        }
    }

    /**
     * トランザクションの開始ログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     */
    public final void start(String transactionId, String messageCode, Object[] args) {
        start(transactionId, messageCode, args, null);
    }

    /**
     * トランザクションの開始ログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     * @param throwable スタックトレースを出す例外
     */
    public final void start(String transactionId, String messageCode, Object[] args, Throwable throwable) {
        transactionLog(TYPE_START, transactionId, messageCode, args, throwable);
    }

    /**
     * トランザクションの終了ログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     */
    public final void end(String transactionId, String messageCode, Object[] args) {
        end(transactionId, messageCode, args, null);
    }

    /**
     * トランザクションの終了ログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     * @param throwable スタックトレースを出す例外
     */
    public final void end(String transactionId, String messageCode, Object[] args, Throwable throwable) {
        transactionLog(TYPE_END, transactionId, messageCode, args, throwable);
    }

    /**
     * トランザクションの外部呼出しログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     */
    public final void request(String transactionId, String messageCode, Object[] args) {
        request(transactionId, messageCode, args, null);
    }

    /**
     * トランザクションの外部呼出しログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     * @param throwable スタックトレースを出す例外
     */
    public final void request(String transactionId, String messageCode, Object[] args, Throwable throwable) {
        transactionLog(TYPE_REQUEST, transactionId, messageCode, args, throwable);
    }

    /**
     * トランザクションの外部応答ログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     */
    public final void reply(String transactionId, String messageCode, Object[] args) {
        reply(transactionId, messageCode, args, null);
    }

    /**
     * トランザクションの外部応答ログを出力する.
     * @param transactionId トランザクションのID
     * @param messageCode Seaser Logging Utilityのメッセージコード
     * @param args メッセージに埋め込む変数
     * @param throwable スタックトレースを出す例外
     */
    public final void reply(String transactionId, String messageCode, Object[] args, Throwable throwable) {
        transactionLog(TYPE_REPLY, transactionId, messageCode, args, throwable);
    }

    /**
     * トランザクションログを出す.
     *
     * @param type 処理種別
     * @param transactionId トランザクションID
     * @param messageCode メッセージコード(Seaser Loggingクラス準拠)
     * @param args
     * @param throwable
     */
    private void transactionLog(String type, String transactionId, String messageCode, Object[] args, Throwable throwable) {
        int length = messageCode.length();
        if (length >= MESSAGECODE_LENGTH_MIN) {
            char levelChar = messageCode.charAt(0);
            if (isEnabledFor(levelChar)) {
                //                try {
                //                    MonitoredHost host = MonitoredHost.getMonitoredHost("localhost");
                //                    String[] pids = (String[]) host.activeVms().toArray(new String[0]);
                //                    pid = pids[0];
                //                } catch (MonitorException e) {
                //                    pid = "NotDefined";
                //                } catch (URISyntaxException e) {
                //                    pid = "NotDefined";
                //                }
                String levelString = getLevelString(levelChar);
                String module = getModuleName(messageCode);
                String free = MessageFormatter.getSimpleMessage(messageCode, args);
                String message = String.format("[%s]: %s %s %s %s %s %s", pid, COMPONENT_NAME, module, transactionId, levelString, type, free);
                String transactionMessage = String.format("[%s]: %s %s %s %s %s %s", pid, COMPONENT_NAME, module, transactionId, levelString, type, convertToOneLine(free));
                switch (levelChar) {
                    case 'D':
                        log.debug(message, throwable);
                        break;
                    case 'I':
                        log.info(message, throwable);
                        transactionLog.info(transactionMessage);
                        break;
                    case 'W':
                        log.warn(message, throwable);
                        transactionLog.warn(transactionMessage);
                        break;
                    case 'E':
                        log.error(message, throwable);
                        transactionLog.error(transactionMessage);
                        break;
                    case 'F':
                        log.fatal(message, throwable);
                        transactionLog.fatal(transactionMessage);
                        break;
                    default:
                        throw new IllegalArgumentException(String.valueOf(levelChar));
                }
            }
        }
    }

    /**
     * メッセージを一行にする
     * @param message メッセージ
     * @return 一行メッセージ
     */
    private Object convertToOneLine(String message) {
        return message.split("\n")[0];
    }

    /**
     * ログレベルの出力用文字列を取得する.
     * @param levelChar ログレベルの頭文字
     * @return ログレベルの出力用文字列
     */
    private String getLevelString(final char levelChar) {
        switch (levelChar) {
            case 'D':
                return LEVEL_DEBUG;
            case 'I':
                return LEVEL_INFO;
            case 'W':
                return LEVEL_WARN;
            case 'E':
                return LEVEL_ERROR;
            case 'F':
                return LEVEL_FATAL;
            default:
                throw new IllegalArgumentException(String.valueOf(levelChar));
        }
    }

    private String getModuleName(String messageCode) {
        String name = messageCode.substring(1, messageCode.length() - MESSAGECODE_NUMBER_LENGTH);
        try {
            name = VhutModule.valueOf(name)
                .getName();
        } catch (Exception e) {

        }
        return name;
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
