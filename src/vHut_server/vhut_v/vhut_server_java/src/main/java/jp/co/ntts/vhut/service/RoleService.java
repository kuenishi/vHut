/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import static jp.co.ntts.vhut.entity.Names.role;
import static jp.co.ntts.vhut.entity.Names.vhutUserRoleMap;

import java.util.List;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.entity.Names;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.Role;
import jp.co.ntts.vhut.entity.VhutUserRoleMap;
import jp.co.ntts.vhut.exception.InputRuntimeException;

import org.seasar.extension.jdbc.where.SimpleWhere;

/**
 * <p>ロールのサービスクラス.
 * <br>
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

public class RoleService extends AbstractService<Role> {

    /**
     * サービスの設定
     */
    public ServiceConfig serviceConfig;


    /**
     * <p>ロール概要一覧取得.
     * <br>
     *
     * @param なし
     * @return Role ロール概要情報のリスト
     */
    @Auth(right = Right.READ_SYS_ROLE)
    public List<Role> getAllRoleAbstractionList() {
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(Role.class)
            .getResultList();
    }

    /**
     * <p>ロール詳細取得.
     * <br>
     *
     * @param roleId ロールID
     * @return Role ロール詳細情報
     */
    @Auth(right = Right.READ_SYS_ROLE)
    public Role getRoleById(Long roleId) {
        //引数のアプリケーションIDを条件に、セレクトする。
        Role role = jdbcManager.from(Role.class)
            .id(roleId)
            .getSingleResult();
        //セレクトした値が取得出来た場合、値をreturnする。
        if (role == null) {
            throw new InputRuntimeException("roleId", String.format("Role(id=%d) was not found", roleId));
        }
        return role;
    }

    /**
     * <p>ロール詳細追加.
     * <br>
     *
     * @param remoteRole ロール詳細情報
     * @return
     */
    @Auth(right = Right.CREATE_SYS_ROLE)
    public void createRole(Role remoteRole) {
        Role localRole = new Role();
        localRole.name = remoteRole.name;
        localRole.rights = padRights(remoteRole.rights);
        if (remoteRole.isDefault) {
            clearDefaultFromAllRoles();
        }
        localRole.isDefault = remoteRole.isDefault;
        jdbcManager.insert(localRole)
            .execute();
    }

    /**
     * <p>ロール詳細更新.
     * <br>
     *
     * @param remoteRole ロール詳細情報
     * @return
     */
    @Auth(right = Right.UPDATE_SYS_ROLE)
    public void updateRole(Role remoteRole) {
        Role localRole = jdbcManager.from(Role.class)
            .id(remoteRole.id)
            .getSingleResult();

        if (localRole == null) {
            throw new InputRuntimeException("remoteRole", String.format("Role(id=%d) was not found", remoteRole.id));
        }

        if (localRole.sysLock) {
            throw new InputRuntimeException("remoteRole", String.format("Role(id=%d) is managed by system", remoteRole.id));
        }
        localRole.name = remoteRole.name;
        localRole.rights = padRights(remoteRole.rights);
        if (localRole.isDefault ^ remoteRole.isDefault) {
            if (localRole.isDefault) {
                setDefaultToGuestRole();
            } else {
                clearDefaultFromAllRoles();
            }
        }
        localRole.isDefault = remoteRole.isDefault;
        jdbcManager.update(localRole)
            .execute();
    }

    /**
     * <p>ロール削除.
     * <br>
     *
     * @param roleId ロールID
     * @return
     */
    @Auth(right = Right.DELETE_SYS_ROLE)
    public void deleteRole(Long roleId) {
        //引数のロールIDと紐づくvhutUserRoleMapをセレクトする。
        Role role = jdbcManager.from(Role.class)
            .leftOuterJoin(Names.role()
                .vhutUserRoleMapList())
            .id(roleId)
            .getSingleResult();
        if (role == null) {
            throw new InputRuntimeException("roleId", String.format("Role(id=%d) was not found", roleId));
        }
        if (role.sysLock) {
            throw new InputRuntimeException("remoteRole", String.format("Role(id=%d) is managed by system", role.id));
        }
        if (role.vhutUserRoleMapList.size() > 0) {
            for (VhutUserRoleMap vhutUserRoleMap : role.vhutUserRoleMapList) {
                vhutUserRoleMap.roleId = serviceConfig.getGuestRoleId();
            }
            jdbcManager.updateBatch(role.vhutUserRoleMapList)
                .includes(vhutUserRoleMap().roleId())
                .execute();
        }
        if (role.isDefault) {
            setDefaultToGuestRole();
        }
        //引数のロールIDをRoleテーブルから削除する。
        jdbcManager.delete(role)
            .execute();
    }

    /**
     * ゲストロールにデフォルトマークを設定します.
     */
    private void setDefaultToGuestRole() {
        Role guestRole = jdbcManager.from(Role.class)
            .id(serviceConfig.getGuestRoleId())
            .getSingleResult();
        guestRole.isDefault = true;
        jdbcManager.update(guestRole)
            .execute();
    }

    /**
     * すべてのロールからデフォルトマークを除去します.
     */
    private void clearDefaultFromAllRoles() {
        List<Role> roles = jdbcManager.from(Role.class)
            .where(new SimpleWhere().eq(role().isDefault(), true))
            .getResultList();
        for (Role role : roles) {
            role.isDefault = false;
        }
        jdbcManager.updateBatch(roles)
            .execute();
    }

    /**
     * 権限マップの長さに合うように0bitでパディングします.
     * @param rights 返還前の権限マップ
     * @return 返還後の権限マップ
     */
    private byte[] padRights(byte[] rights) {
        byte[] result = Right.getEmptyRights();
        for (int i = 0; i < Math.min(rights.length, result.length); i++) {
            result[i] = (byte) (result[i] | rights[i]);
        }
        return result;
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
