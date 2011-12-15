/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EnumMap;

import jp.co.ntts.vhut.config.CloudConfig;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandOperation;
import jp.co.ntts.vhut.entity.CommandStatus;
import jp.co.ntts.vhut.exception.CommandByteDecodingRuntimeException;
import jp.co.ntts.vhut.exception.CommandByteEncodingRuntimeException;
import jp.co.ntts.vhut.util.TimestampUtil;
import jp.co.ntts.vhut.util.VhutLogger;

import org.apache.commons.io.IOUtils;

/**
 * <p>すべてのコマンドの基底となる抽象クラス.
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
public abstract class AbstractCommand implements ICommand {

    private static final VhutLogger logger = VhutLogger.getLogger(AbstractCommand.class);

    /** クラウドの設定 */
    public CloudConfig cloudConfig;

    /**
     * コマンドエンティティ.
     */
    protected Command command;

    /**
     * オペレーション.
     */
    protected CommandOperation operation;

    // ↓保存対象↓//
    protected EnumMap parameter;

    /**
     * コマンドの実行結果オブジェクト.
     */
    protected Serializable result;


    // ↑保存対象↑//

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#init(long)
     */
    @Override
    public void init(long cloudId) {
        command = new Command();
        command.id = 0L;
        command.cloudId = cloudId;
        command.operation = operation;
        command.status = CommandStatus.WAITING;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * jp.co.ntts.vhut.command.ICommand#init(jp.co.ntts.vhut.entity.Command)
     */
    @Override
    public void init(Command command) {
        this.command = command;
        if (command.parameter != null) {
            parameter = (EnumMap) decode(command.parameter);
        }
        if (command.result != null) {
            result = (Serializable) decode(command.result);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#getCommand()
     */
    @Override
    public Command getCommand() {
        if (parameter != null) {
            command.parameter = encode(parameter);
        }
        if (result != null) {
            command.result = encode(result);
        }
        return command;
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#getResult()
     */
    @Override
    public Object getResult() {
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see jp.co.ntts.vhut.command.ICommand#updateStatus()
     */
    @Override
    public CommandStatus updateStatus() {
        if (isTimeOut()) {
            setTimeOutErrorToCommand();
        }
        return command.status;
    }

    /**
     * オブジェクトをバイト列に変換します.
     * @param obj オブジェクト
     * @return バイト列
     */
    protected byte[] encode(Serializable obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            throw new CommandByteEncodingRuntimeException(operation, e);
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(oos);
        }
        return bytes;
    }

    /**
     * バイト列をオブジェクトに変換します.
     * @param bytes オブジェクトをシリアライズしたバイト列
     * @return 基のオブジェクト
     */
    protected Object decode(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } catch (IOException e) {
            throw new CommandByteDecodingRuntimeException(operation, command.id, e);
        } catch (ClassNotFoundException e) {
            throw new CommandByteDecodingRuntimeException(operation, command.id, e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(ois);
        }
        return obj;
    }

    /**
     * @return 制限時間オーバー
     */
    public boolean isTimeOut() {
        if (command.startTime == null) {
            return false;
        }
        long delta = TimestampUtil.getCurrentDateAsTimestamp()
            .getTime() - command.startTime.getTime();
        return delta > cloudConfig.getCommandTimeLimit();
    }

    /** コマンドをタイムアウト扱いにする */
    protected void setTimeOutErrorToCommand() {
        command.status = CommandStatus.ERROR;
        command.errorMessage = "time out";
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
