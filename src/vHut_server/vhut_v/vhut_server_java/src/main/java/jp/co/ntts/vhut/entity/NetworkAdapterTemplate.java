/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import jp.co.ntts.vhut.exception.DBConflictException;

/**
 * NetworkAdapterTemplateエンティティクラス.
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
public class NetworkAdapterTemplate implements Serializable, IIdentifiableEntity {

    private static final long serialVersionUID = 1L;

    /** idプロパティ */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19, nullable = false, unique = true)
    public Long id;

    /** cloudIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long cloudId;

    /** nameプロパティ */
    @Column(length = 256, nullable = true, unique = false)
    public String name;

    /** templateIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long templateId;

    /** securityGroupTemplateIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long securityGroupTemplateId;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** networkTemplate関連プロパティ */
    @ManyToOne
    @JoinColumn(name = "security_group_template_id", referencedColumnName = "id")
    public SecurityGroupTemplate securityGroupTemplate;

    /** template関連プロパティ */
    @ManyToOne
    public Template template;

    /** macプロパティ */
    @Column(length = 12, nullable = true, unique = false)
    public String mac;


    /**
     * リモートのオブジェクトと情報を同期します.
     * @param remote リモートのオブジェクト
     * @return 同期処理を実行したらtrue, 同期する必要がなければfalse
     * @throws DBConflictException DBに不一致があった
     */
    public boolean sync(NetworkAdapterTemplate remote) throws DBConflictException {
        boolean isUpdated = false;

        DBConflictException exception = new DBConflictException(template);

        // 名称
        if (!name.equals(remote.name)) {
            exception.addChanged("networkAdapterTemplate.name", name, remote.name);
            isUpdated = true;
        }

        // マックアドレス
        if (!mac.equals(remote.mac)) {
            exception.addChanged("networkAdapterTemplate.mac", mac, remote.mac);
            isUpdated = true;
        }

        if (exception.getSize() > 0) {
            // 不一致を検知
            throw exception;
        } else {
            // 一致
            return isUpdated;
        }
    }

    /**
     * ネットワークアダプターを参照してネットワークアダプターテンプレートを作成します.
     * @param source ネットワークアダプター
     * @return ネットワークアダプターテンプレート
     */
    public static NetworkAdapterTemplate newInstance(NetworkAdapter source) {
        NetworkAdapterTemplate result = new NetworkAdapterTemplate();

        result.mac = source.mac;
        result.name = source.name;
        result.securityGroupTemplateId = source.securityGroupId;
        result.securityGroupTemplate = SecurityGroupTemplate.newInstance(source.securityGroup);

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
