/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import jp.co.ntts.vhut.exception.DBConflictException;
import jp.co.ntts.vhut.util.Ipv4ConversionUtil;
import jp.co.ntts.vhut.util.MacConversionUtil;

/**
 * NetworkAdapterエンティティクラス.
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
public class NetworkAdapter implements Serializable, IIdentifiableEntity {

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

    /** vmIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long vmId;

    /** securityGroupIdプロパティ */
    @Column(precision = 19, nullable = false, unique = false)
    public Long securityGroupId;

    /** publicIpプロパティ */
    @Column(length = 8, nullable = true, unique = false)
    public String publicIp;

    /** privateIpプロパティ */
    @Column(length = 8, nullable = true, unique = false)
    public String privateIp;

    /** macプロパティ */
    @Column(length = 12, nullable = true, unique = false)
    public String mac;

    /** versionプロパティ */
    @Version
    @Column(precision = 19, nullable = false, unique = false, columnDefinition = "default 0")
    public Long version = 0L;

    /** securityGroup関連プロパティ */
    @ManyToOne
    public SecurityGroup securityGroup;

    /** publicIpReservationList関連プロパティ */
    @OneToMany(mappedBy = "networkAdapter")
    public List<PublicIpReservation> publicIpReservationList;

    /** vm関連プロパティ */
    @ManyToOne
    public Vm vm;


    /**
     * リモートのオブジェクトと情報を同期します.
     * @param remote リモートのオブジェクト
     * @return 同期処理を実行したらtrue, 同期する必要がなければfalse
     * @throws DBConflictException DBに不一致があった
     */
    public boolean sync(NetworkAdapter remote) throws DBConflictException {
        boolean isUpdated = false;

        DBConflictException exception = new DBConflictException(vm);

        //        // 名称
        //        if (!name.equals(remote.name)) {
        //            exception.addChanged("networkAdapterTemplate.name", name, remote.name);
        //        }

        // マックアドレス
        if (!mac.equals(remote.mac)) {
            exception.addChanged("networkAdapter.mac", mac, remote.mac);
        }

        if (exception.getSize() > 0) {
            // 不一致を検知
            throw exception;
        } else {
            // 一致
            return isUpdated;
        }
    }

    //--------------------------------------------
    // 変換済み値取得
    //--------------------------------------------

    /** ドット区切りのパブリックIPアドレス. */
    public String getPublicIpWithDotFormat() {
        if (publicIp != null) {
            return Ipv4ConversionUtil.convertHexToDot(publicIp);
        }
        return null;
    }

    /** ドット区切りのプライベートIPアドレス. */
    public String getPrivateIpWithDotFormat() {
        if (privateIp != null) {
            return Ipv4ConversionUtil.convertHexToDot(privateIp);
        }
        return null;
    }

    /** コロン区切りのMACアドレス. */
    public String getMacWithColonFormat() {
        if (mac != null) {
            return MacConversionUtil.convertHexToColon(mac);
        }
        return null;
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
