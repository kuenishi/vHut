/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import org.seasar.framework.message.MessageFormatter;

/**
 * <p>vHut独自の検査例外のベースクラス.
 * <br>
 * <p>メッセージコード入りのメッセージと、単純なメッセージの両方を取得できる。
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
public abstract class AbstractVhutException extends Exception {

    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 7408225433146091249L;

    /**
     * メッセージコード.
     * ログレベル(1文字)+モジュールコード+連番(4桁)
     */
    private String messageCode;

    /**
     * メッセージを構成する要素.
     */
    private Object[] args;

    /**
     * メッセージコード付きメッセージ.
     */
    private String message;

    /**
     * メッセージコードなしメッセージ.
     */
    private String simpleMessage;


    /**
     * @param cause 起因する例外
     */
    public AbstractVhutException(Throwable cause) {
        this(null, null, cause);
    }

    /**
     * {@link AbstractVhutException}を作成します。
     *
     * @param messageCode メッセージコード（モジュールID+通番4桁）
     */
    public AbstractVhutException(String messageCode) {
        this(messageCode, null, null);
    }

    /**
     * {@link AbstractVhutException}を作成します。
     *
     * @param messageCode メッセージコード（モジュールID+通番4桁）
     * @param args
     */
    public AbstractVhutException(String messageCode, Object[] args) {
        this(messageCode, args, null);
    }

    /**
     * {@link AbstractVhutException}を作成します。
     *
     * @param messageCode メッセージコード（モジュールID+通番4桁）
     * @param args
     * @param cause
     */
    public AbstractVhutException(String messageCode, Object[] args, Throwable cause) {

        super(cause);
        this.messageCode = messageCode;
        this.args = args;
        updateMessage();
    }

    /**
     * メッセージを更新します.
     */
    private void updateMessage() {
        simpleMessage = MessageFormatter.getSimpleMessage(messageCode, args);
        message = "[" + messageCode + "]" + simpleMessage;
    }

    /**
     * メッセージコードを返します。
     *
     * @return メッセージコード
     */
    public final String getMessageCode() {
        return messageCode;
    }

    /**
     * @param messageCode メッセージコード
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        updateMessage();
    }

    /**
     * 引数の配列を返します。
     *
     * @return 引数の配列
     */
    public final Object[] getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * @param simpleMessage the simpleMessage to set
     */
    public void setSimpleMessage(String simpleMessage) {
        this.simpleMessage = simpleMessage;
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return message;
    }

    /**
     * メッセージを設定します。
     *
     * @param message
     *            メッセージ
     */
    protected void setMessage(String message) {
        this.message = message;
    }

    /**
     * メッセージコードなしの単純なメッセージを返します。
     *
     * @return メッセージコードなしの単純なメッセージ
     */
    public final String getSimpleMessage() {
        return simpleMessage;
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
