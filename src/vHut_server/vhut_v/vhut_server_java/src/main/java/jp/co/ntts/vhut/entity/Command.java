/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import jp.co.ntts.vhut.exception.CommandByteDecodingRuntimeException;
import jp.co.ntts.vhut.exception.CommandByteEncodingRuntimeException;

import org.apache.commons.io.IOUtils;

/**
 * Commandエンティティクラス.
 *
 * @author NTT Software Corporation.
 * @version 1.0.0
 * <p>
 * <!--$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $<br>-->
 * <!--$Revision: 949 $<br>-->
 * <!--$Author: NTT Software Corporation. $<br>-->
 */
@Entity
@Generated(value = { "S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl" }, date = "2011/07/15 15:30:33")
public class Command implements Serializable, IIdentifiableEntity {

    private static final long serialVersionUID = 1L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** cloudIdプロパティ */
    @Basic(fetch = FetchType.LAZY)
    @Column(precision = 19, nullable = false, unique = false)
    public Long cloudId;

    /** operationプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public CommandOperation operation;

    /** parameterプロパティ */
    @Basic(fetch = FetchType.LAZY)
    public byte[] parameter;

    /** resultプロパティ */
    @Basic(fetch = FetchType.LAZY)
    public byte[] result;

    /** statusプロパティ */
    @Enumerated(EnumType.ORDINAL)
    public CommandStatus status;

    /** startTimeプロパティ */
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true, unique = false)
    public Timestamp startTime;

    /** endTimeプロパティ */
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true, unique = false)
    public Timestamp endTime;

    /** errorMessageプロパティ */
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 10485760, nullable = true, unique = false)
    public String errorMessage;

    /** dependingCommandIdプロパティ */
    //    @Basic(fetch = FetchType.LAZY)
    @Column(precision = 19, nullable = true, unique = false)
    public Long dependingCommandId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** dependingCommand関連プロパティ */
    @ManyToOne
    @JoinColumn(name = "depending_command_id", referencedColumnName = "id")
    public Command dependingCommand;

    /** commandList関連プロパティ */
    @OneToMany(mappedBy = "dependingCommand")
    public List<Command> commandList;

    /** commandTemplateMapList関連プロパティ */
    @OneToMany(mappedBy = "command")
    public List<CommandTemplateMap> commandTemplateMapList;

    /** commandVmMapList関連プロパティ */
    @OneToMany(mappedBy = "command")
    public List<CommandVmMap> commandVmMapList;


    public Command() {
        this.status = CommandStatus.WAITING;
    }

    public Command(Long cloudId, CommandOperation operation, Serializable... parameters) {
        this();
        this.cloudId = cloudId;
        this.operation = operation;
        this.setParameter(parameters);
    }

    /**
     * オブジェクトをバイト列に変換してparameterにセットする.
     * @see jp.co.ntts.vhut.command.AbstractCommand#encode(Serializable)
     * @param obj コマンド引数となるオブジェクト配列
     * @return バイト列
     */
    public byte[] setParameter(Serializable obj) {
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

        this.parameter = bytes;

        return bytes;
    }

    /**
     * オブジェクトをバイト列に変換してparameterにセットする.
     * @see jp.co.ntts.vhut.command.AbstractCommand#encode(Serializable)
     * @return バイト列
     */
    public Serializable[] getParameter() {
        Serializable[] result = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(this.parameter);
            ois = new ObjectInputStream(bis);
            result = (Serializable[]) ois.readObject();
        } catch (IOException e) {
            throw new CommandByteDecodingRuntimeException(operation, this.id, e);
        } catch (ClassNotFoundException e) {
            throw new CommandByteDecodingRuntimeException(operation, this.id, e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(ois);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.entity.IIdentifiableEntity#getId()
     */
    @Override
    public Long getId() {
        return id;
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
