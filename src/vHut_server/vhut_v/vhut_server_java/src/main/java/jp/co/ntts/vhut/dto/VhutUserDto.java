/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

import static jp.co.ntts.vhut.entity.VhutUserNames.account;
import static jp.co.ntts.vhut.entity.VhutUserNames.vhutUserRoleMapList;
import static org.seasar.extension.jdbc.operation.Operations.eq;

import java.io.Serializable;
import java.security.Principal;
import java.util.EnumMap;
import java.util.Map;

import javax.persistence.NoResultException;

import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.Role;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.entity.VhutUserRoleMap;
import jp.co.ntts.vhut.exception.AuthenticationException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

/**
 * <p>ユーザの認証情報を保管するDTO.
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
@Component(instance = InstanceType.SESSION)
public class VhutUserDto implements Serializable {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 7128803998417185004L;

    /**
     * ロガー.
     */
    private static final VhutLogger logger = VhutLogger.getLogger(VhutUserDto.class);

    /**
     * JDBC管理クラス.
     */
    public transient JdbcManager jdbcManager;

    /**
     * アプリケーションユーザのエンティティ.
     */
    private VhutUser vhutUser;

    /**
     * 権限を保存するマップ.
     */
    private EnumMap<Right, Boolean> rightsMap;

    /**
     * 認証の状態.
     */
    private boolean isAuthenticated;


    @InitMethod
    public void init() {
        try {
            isAuthenticated = false;
            vhutUser = null;
            updateRightsMap();
        } catch (AuthenticationException e) {
            logger.log(e);
        } catch (Exception e) {
            logger.log("ESRVS5012", e);
        }
    }

    /**
     * アカウント名の取得.
     * @throws AuthenticationException 認証例外
     * @return アカウント名
     */
    public synchronized String getAccount() throws AuthenticationException {
        if (isAuthenticated && vhutUser != null) {
            return vhutUser.account;
        } else {
            FlexSession session = FlexContext.getFlexSession();
            if (session != null) {
                Principal principal = session.getUserPrincipal();
                if (principal != null) {
                    try {
                        vhutUser = jdbcManager.from(VhutUser.class)
                            .leftOuterJoin(vhutUserRoleMapList())
                            .innerJoin(vhutUserRoleMapList().role())
                            .where(eq(account(), principal.getName()))
                            .disallowNoResult()
                            .getSingleResult();
                    } catch (NoResultException e) {
                        throw new AuthenticationException(principal.getName());
                    }
                    isAuthenticated = true;
                    return vhutUser.account;
                }
            }
            vhutUser = null;
            throw new AuthenticationException(null);
        }
    }

    /**
     * 権限マップを更新する.
     * @throws AuthenticationException 認証例外
     */
    public synchronized void updateRightsMap() throws AuthenticationException {
        int i;
        rightsMap = new EnumMap<Right, Boolean>(Right.class);
        clearRightsMap();
        String account = getAccount();
        if (account != null) {
            int length = Right.getByteLength();
            byte[] rightBytes = new byte[length];
            for (VhutUserRoleMap map : vhutUser.vhutUserRoleMapList) {
                Role role = map.role;
                if (role.rights.length < length) {
                    throw new DBStateRuntimeException("ESRVS2001", VhutUserRoleMap.class, map.id, String.format("rights byte length sould be longger than %d", length));
                }
                for (i = 0; i < length; i++) {
                    rightBytes[i] = (byte) (rightBytes[i] | role.rights[i]);
                    //                    System.out.printf("%02x:", rightBytes[i]);
                }
                //                System.out.printf("\n");
            }
            for (i = 0; i < length; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i * 8 + j >= Right.values().length)
                        return;
                    //                    int right = rightBytes[i] & (byte) (1 << (7 - j));
                    int right = rightBytes[i] & (byte) (1 << j);
                    if (right != 0) {
                        rightsMap.put(Right.values()[i * 8 + j], true);
                    }
                }
            }
        }
    }

    /**
     * 権限マップをクリアします.
     */
    protected void clearRightsMap() {
        for (Right right : Right.values()) {
            rightsMap.put(right, false);
        }
    }

    /**
     * 認証状態を返します.
     * @return the isAuthenticated
     * @throws AuthenticationException 認証例外
     */
    public synchronized boolean isAuthenticated() throws AuthenticationException {
        if (!isAuthenticated) {
            updateRightsMap();
        }
        return isAuthenticated;
    }

    /**
     * アプリケーションユーザのエンティティを返します.
     * @return the vhutUser
     * @throws AuthenticationException 認証例外
     */
    public synchronized VhutUser getVhutUser() throws AuthenticationException {
        if (!isAuthenticated) {
            updateRightsMap();
        }
        return vhutUser;
    }

    /**
     * 権限マップを返します.
     * @return 権限マップ
     * @throws AuthenticationException 認証例外
     */
    public synchronized Map<Right, Boolean> getRightsMap() throws AuthenticationException {
        if (!isAuthenticated) {
            updateRightsMap();
        }
        return rightsMap.clone();
    }

    /**
     * 認可を確認します.
     * @param right 操作
     * @return 可不可
     * @throws AuthenticationException 認証例外
     */
    public synchronized boolean isAuthorized(Right right) throws AuthenticationException {
        if (!isAuthenticated()) {
            updateRightsMap();
        }
        return rightsMap.get(right);
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
