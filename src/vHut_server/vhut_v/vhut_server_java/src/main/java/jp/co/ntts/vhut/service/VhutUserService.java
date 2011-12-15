/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.service;

import static jp.co.ntts.vhut.entity.Names.application;
import static jp.co.ntts.vhut.entity.Names.applicationInstance;
import static jp.co.ntts.vhut.entity.Names.applicationInstanceGroup;
import static jp.co.ntts.vhut.entity.Names.role;
import static jp.co.ntts.vhut.entity.Names.vhutUser;
import static jp.co.ntts.vhut.entity.Names.vhutUserCloudUserMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Generated;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.VhutUserDto;
import jp.co.ntts.vhut.entity.Application;
import jp.co.ntts.vhut.entity.ApplicationInstance;
import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
import jp.co.ntts.vhut.entity.CloudUser;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.Role;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.entity.VhutUserCloudUserMap;
import jp.co.ntts.vhut.entity.VhutUserRoleMap;
import jp.co.ntts.vhut.exception.AuthenticationException;
import jp.co.ntts.vhut.exception.AuthorizationException;
import jp.co.ntts.vhut.exception.CloudUserNotFoundException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>
 * vHutUserサービスのクラス. <br>
 * <p>
 *
 * @version 1.0.0
 * @author NTT Software Corporation. <!-- $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $ $Revision: 949 $ $Author: NTT Software Corporation.
 *         $ -->
 */
@Generated(value = { "S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl" }, date = "2011/07/15 4:21:16")
public class VhutUserService extends AbstractService<VhutUser> {

    /** クラウドロジックを呼び出すためのファクトリ */
    public CloudLogicFactory cloudLogicFactory;
    /** クラウドの設定値 */
    public ServiceConfig serviceConfig;
    /** ユーザセッション情報 */
    public VhutUserDto vhutUserDto;
    /** 使用するクラウドのID */
    protected long cloudId = 1;
    /** クラウドのサービスロジック */
    protected ICloudServiceLogic cloudServiceLogic;


    @InitMethod
    public void init() {
        cloudServiceLogic = cloudLogicFactory.newCloudServiceLogic(cloudId);
    }

    /**
     * <p>
     * ユーザ概要一覧取得. <br>
     *
     * @param なし
     * @return VhutUser ユーザ概要情報のリスト
     * @throws AuthenticationException 認証例外
     */
    @Auth(right = Right.READ_OWN_USER)
    public List<VhutUser> getAllVhutUserAbstractionList() throws AuthenticationException {
        //認可系の処理
        SimpleWhere where = new SimpleWhere();
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_USER)) {
            where = new SimpleWhere().eq(vhutUser().id(), vhutUserDto.getVhutUser().id);
        }
        // 一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(VhutUser.class)
            .where(where)
            .getResultList();
    }

    /**
     * <p>
     * ロール関連ユーザ概要一覧取得. <br>
     *
     * @param roleId
     *            ロールID
     * @return VhutUser ユーザ概要情報のリスト
     */
    public List<VhutUser> getVhutUserAbstractionListByRoleId(Long roleId) {
        // 引数のroleIdとvhutUserRoleMapList.roleIdが等しいVhutUserのリストをセレクトする。
        // 一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(VhutUser.class)
            .innerJoin(vhutUser().vhutUserRoleMapList())
            .where(new SimpleWhere().eq(vhutUser().vhutUserRoleMapList()
                .roleId(), roleId))
            .getResultList();
    }

    /**
     * <p>
     * ユーザ詳細取得. <br>
     *
     * @param id
     *            ユーザID
     * @return VhutUser ユーザ詳細情報
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.READ_OWN_USER)
    public VhutUser getVhutUserById(Long id) throws AuthorizationException, AuthenticationException {
        // 引数のアプリケーションIDを条件に、セレクトする。
        VhutUser vhutUser = jdbcManager.from(VhutUser.class)
            .id(id)
            .eager(vhutUser().email())
            .leftOuterJoin(vhutUser().vhutUserRoleMapList())
            .leftOuterJoin(vhutUser().vhutUserRoleMapList()
                .role())
            .getSingleResult();
        if (vhutUser == null) {
            throw new InputRuntimeException("id", String.format("VhutUser(id=%d) was not found", id));
        }
        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.READ_ALL_USER) && !vhutUser.id.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.READ_ALL_USER.name());
        }
        // セレクトした値が取得出来た場合、値をreturnする。
        return vhutUser;
    }

    @Auth(right = Right.UPDATE_OWN_USER)
    public void updateVhutUser(VhutUser remoteVhutUser) throws AuthorizationException, AuthenticationException {
        internalUpdateVhutUser(remoteVhutUser);
    }

    /**
     * @param remoteVhutUser
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    private void internalUpdateVhutUser(VhutUser remoteVhutUser) throws AuthorizationException, AuthenticationException {
        VhutUser localVhutUser = jdbcManager.from(VhutUser.class)
            .leftOuterJoin(vhutUser().vhutUserCloudUserMapList())
            .leftOuterJoin(vhutUser().vhutUserRoleMapList())
            .id(remoteVhutUser.id)
            .getSingleResult();

        if (localVhutUser == null) {
            throw new InputRuntimeException("remoteVhutUser", String.format("VhutUser(id=%d) was not found", remoteVhutUser.id));
        }
        if (localVhutUser.sysLock) {
            throw new InputRuntimeException("remoteVhutUser", String.format("VhutUser(id=%d) is managed by system", remoteVhutUser.id));
        }

        //認可系の処理
        if (!vhutUserDto.isAuthorized(Right.UPDATE_ALL_USER) && !localVhutUser.id.equals(vhutUserDto.getVhutUser().id)) {
            throw new AuthorizationException(vhutUserDto.getAccount(), Right.UPDATE_ALL_USER.name());
        }

        localVhutUser.firstName = remoteVhutUser.firstName;
        localVhutUser.lastName = remoteVhutUser.lastName;
        localVhutUser.email = remoteVhutUser.email;
        localVhutUser.imageUrl = remoteVhutUser.imageUrl;

        jdbcManager.update(localVhutUser)
            .execute();

        syncVhutUserCloudUserMap(remoteVhutUser, localVhutUser);

        syncVhutUserRoleMap(remoteVhutUser, localVhutUser);

    }

    /**
     * @param remoteVhutUser
     * @param localVhutUser
     */
    private void syncVhutUserCloudUserMap(VhutUser remoteVhutUser, VhutUser localVhutUser) {
        Map<String, VhutUserCloudUserMap> remoteCloudUserMap = new HashMap<String, VhutUserCloudUserMap>();
        if (remoteVhutUser.vhutUserCloudUserMapList != null) {
            for (VhutUserCloudUserMap remoteVhutUserCloudUserMap : remoteVhutUser.vhutUserCloudUserMapList) {
                String key = createHushKey(remoteVhutUserCloudUserMap.cloudId, remoteVhutUserCloudUserMap.cloudUserId);
                remoteCloudUserMap.put(key, remoteVhutUserCloudUserMap);
            }
        }
        Map<String, VhutUserCloudUserMap> localCloudUserMap = new HashMap<String, VhutUserCloudUserMap>();
        if (remoteVhutUser.vhutUserCloudUserMapList != null) {
            for (VhutUserCloudUserMap localVhutUserCloudUserMap : localVhutUser.vhutUserCloudUserMapList) {
                String key = createHushKey(localVhutUserCloudUserMap.cloudId, localVhutUserCloudUserMap.cloudUserId);
                localCloudUserMap.put(key, localVhutUserCloudUserMap);
            }
        }

        // insert & update

        for (Entry<String, VhutUserCloudUserMap> entry : remoteCloudUserMap.entrySet()) {
            if (!localCloudUserMap.containsKey(entry.getKey())) {
                //insert
                VhutUserCloudUserMap remoteVhutUserCloudUserMap = entry.getValue();
                VhutUserCloudUserMap localVhutUserCloudUserMap = new VhutUserCloudUserMap();
                localVhutUserCloudUserMap.vhutUserId = localVhutUser.id;
                localVhutUserCloudUserMap.cloudId = remoteVhutUserCloudUserMap.cloudId;
                localVhutUserCloudUserMap.cloudUserId = remoteVhutUserCloudUserMap.cloudUserId;
                jdbcManager.insert(localVhutUserCloudUserMap)
                    .execute();
            } else {
                //update
                VhutUserCloudUserMap remoteVhutUserCloudUserMap = entry.getValue();
                VhutUserCloudUserMap localVhutUserCloudUserMap = localCloudUserMap.get(entry.getKey());
                localVhutUserCloudUserMap.vhutUserId = localVhutUser.id;
                localVhutUserCloudUserMap.cloudId = remoteVhutUserCloudUserMap.cloudId;
                localVhutUserCloudUserMap.cloudUserId = remoteVhutUserCloudUserMap.cloudUserId;
                jdbcManager.update(localVhutUserCloudUserMap)
                    .execute();
            }
        }

        // delete

        for (Entry<String, VhutUserCloudUserMap> entry : localCloudUserMap.entrySet()) {
            if (!remoteCloudUserMap.containsKey(entry.getKey())) {
                //delete
                jdbcManager.delete(entry.getValue())
                    .execute();
            }
        }
    }

    /**
     * @param remoteVhutUser
     * @param localVhutUser
     */
    private void syncVhutUserRoleMap(VhutUser remoteVhutUser, VhutUser localVhutUser) {
        Map<Long, VhutUserRoleMap> remoteRoleMap = new HashMap<Long, VhutUserRoleMap>();
        if (remoteVhutUser.vhutUserRoleMapList != null) {
            for (VhutUserRoleMap remoteVhutUserRoleMap : remoteVhutUser.vhutUserRoleMapList) {
                Long key = remoteVhutUserRoleMap.roleId;
                remoteRoleMap.put(key, remoteVhutUserRoleMap);
            }
        }
        Map<Long, VhutUserRoleMap> localCloudUserMap = new HashMap<Long, VhutUserRoleMap>();
        if (localVhutUser.vhutUserRoleMapList != null) {
            for (VhutUserRoleMap localVhutUserRoleMap : localVhutUser.vhutUserRoleMapList) {
                Long key = localVhutUserRoleMap.roleId;
                localCloudUserMap.put(key, localVhutUserRoleMap);
            }
        }

        // insert & update

        for (Entry<Long, VhutUserRoleMap> entry : remoteRoleMap.entrySet()) {
            if (!localCloudUserMap.containsKey(entry.getKey())) {
                //insert
                VhutUserRoleMap remoteVhutUserRoleMap = entry.getValue();
                VhutUserRoleMap localVhutUserRoleMap = new VhutUserRoleMap();
                localVhutUserRoleMap.vhutUserId = localVhutUser.id;
                localVhutUserRoleMap.roleId = remoteVhutUserRoleMap.roleId;
                jdbcManager.insert(localVhutUserRoleMap)
                    .execute();
            } else {
                //update
                VhutUserRoleMap remoteVhutUserRoleMap = entry.getValue();
                VhutUserRoleMap localVhutUserRoleMap = localCloudUserMap.get(entry.getKey());
                localVhutUserRoleMap.vhutUserId = localVhutUser.id;
                localVhutUserRoleMap.roleId = remoteVhutUserRoleMap.roleId;
                jdbcManager.update(localVhutUserRoleMap)
                    .execute();
            }
        }

        // delete

        for (Entry<Long, VhutUserRoleMap> entry : localCloudUserMap.entrySet()) {
            if (!remoteRoleMap.containsKey(entry.getKey())) {
                //delete
                jdbcManager.delete(entry.getValue())
                    .execute();
            }
        }
    }

    private String createHushKey(long ida, long idb) {
        return Long.toHexString(ida) + ":" + Long.toHexString(idb);
    }

    @Auth(right = Right.CREATE_ALL_USER)
    public void createVhutUser(VhutUser remoteVhutUser) {
        internalCreateVhutUser(remoteVhutUser);
    }

    /**
     * @param remoteVhutUser
     */
    private void internalCreateVhutUser(VhutUser remoteVhutUser) {
        VhutUser localVhutUser = new VhutUser();
        localVhutUser.account = remoteVhutUser.account;
        localVhutUser.firstName = remoteVhutUser.firstName;
        localVhutUser.lastName = remoteVhutUser.lastName;
        localVhutUser.imageUrl = remoteVhutUser.imageUrl;
        localVhutUser.email = remoteVhutUser.email;

        jdbcManager.insert(localVhutUser)
            .execute();

        if (remoteVhutUser.vhutUserRoleMapList != null) {
            // 引数のuser.vhutUserRoleMapList、user.idをセットして、VhutUserRoleMapを追加する。
            for (VhutUserRoleMap remoteVhutUserRoleMap : remoteVhutUser.vhutUserRoleMapList) {
                VhutUserRoleMap localVhutUserRoleMap = new VhutUserRoleMap();
                localVhutUserRoleMap.vhutUserId = localVhutUser.id;
                localVhutUserRoleMap.roleId = remoteVhutUserRoleMap.roleId;
                jdbcManager.insert(localVhutUserRoleMap)
                    .execute();
            }
        }

        if (remoteVhutUser.vhutUserCloudUserMapList != null) {
            for (VhutUserCloudUserMap remoteVhutUserCloudUserMap : remoteVhutUser.vhutUserCloudUserMapList) {
                VhutUserCloudUserMap localVhutUserCloudUserMap = new VhutUserCloudUserMap();
                localVhutUserCloudUserMap.cloudId = remoteVhutUserCloudUserMap.cloudId;
                localVhutUserCloudUserMap.cloudUserId = remoteVhutUserCloudUserMap.cloudUserId;
                localVhutUserCloudUserMap.vhutUserId = localVhutUser.id;
                jdbcManager.insert(localVhutUserCloudUserMap)
                    .execute();
            }
        }
    }

    /**
     * <p>
     * ユーザ詳細一括追加更新. <br>
     *
     * @param userList
     *            ユーザ詳細情報のリスト
     * @return
     * @throws AuthenticationException 認証例外
     * @throws AuthorizationException 認可例外
     */
    @Auth(right = Right.CREATE_ALL_USER)
    public void createVhutUserList(List<VhutUser> userList) throws AuthorizationException, AuthenticationException {
        for (VhutUser remoteVhutUser : userList) {
            if (remoteVhutUser.id == 0) {
                //追加処理
                internalCreateVhutUser(remoteVhutUser);
            } else {
                internalUpdateVhutUser(remoteVhutUser);
            }
        }
    }

    /**
     * <p>
     * ユーザ削除. <br>
     *
     * @param id
     *            ユーザID
     * @return
     */
    @Auth(right = Right.DELETE_ALL_USER)
    public void deleteVhutUserById(Long id) {
        //既存ユーザを取得
        VhutUser localUser = jdbcManager.from(VhutUser.class)
            .leftOuterJoin(vhutUser().vhutUserRoleMapList())
            .leftOuterJoin(vhutUser().vhutUserCloudUserMapList())
            .leftOuterJoin(vhutUser().applicationList())
            .leftOuterJoin(vhutUser().applicationInstanceList())
            .leftOuterJoin(vhutUser().applicationInstanceGroupList())
            .id(id)
            .getSingleResult();

        if (localUser.sysLock) {
            throw new InputRuntimeException("id", String.format("VhutUser(id=%d) is managed by system", id));
        }

        // VMのユーザIDが引数のユーザIDと紐づくVMの所有者を管理者にする。
        cloudServiceLogic.batchUpdateVmUser(id, serviceConfig.getAdminVhutUserId());
        // 各テーブル更新・削除用にエンティティクラスをnewし
        // 値を代入する。
        // 更新・削除する。

        // VhutUserRoleMap
        // 引数のIDと紐づくユーザIDを持つレコードをバッチ削除する。
        if (localUser.vhutUserRoleMapList.size() != 0) {
            jdbcManager.deleteBatch(localUser.vhutUserRoleMapList)
                .execute();
        }
        // Application
        // 引数のIDと紐づくユーザIDを持つレコードのユーザIDを管理者IDに変更し、バッチ追加する。
        for (Application app : localUser.applicationList) {
            app.vhutUserId = serviceConfig.getAdminVhutUserId();
            jdbcManager.update(app)
                .includes(application().vhutUserId())
                .execute();
        }

        // ApplicationInstance
        // 引数のIDと紐づくユーザIDを持つレコードのユーザIDを管理者IDに変更し、バッチ追加する。
        for (ApplicationInstance ai : localUser.applicationInstanceList) {
            ai.vhutUserId = serviceConfig.getAdminVhutUserId();
            jdbcManager.update(ai)
                .includes(applicationInstance().vhutUserId())
                .execute();
        }

        // ApplicationInstanceGroup
        // 引数のIDと紐づくユーザIDを持つレコードのユーザIDを管理者IDに変更し、バッチ追加する。
        for (ApplicationInstanceGroup aig : localUser.applicationInstanceGroupList) {
            aig.vhutUserId = serviceConfig.getAdminVhutUserId();
            jdbcManager.update(aig)
                .includes(applicationInstanceGroup().vhutUserId())
                .execute();
        }

        // VhutUserCloudUserMap
        // 引数のIDと紐づくユーザIDを持つレコードを削除する。
        if (localUser.vhutUserCloudUserMapList.size() != 0) {
            jdbcManager.deleteBatch(localUser.vhutUserCloudUserMapList)
                .execute();
        }

        // VhutUser
        // 引数のIDと紐づくユーザIDを持つレコードを削除する。
        jdbcManager.delete(localUser)
            .execute();
    }

    /**
     * <p>
     * 未登録ユーザ概要一覧取得. <br>
     *
     * @param なし
     * @return VhutUser ユーザ概要情報のリスト
     */
    public List<VhutUser> getUnregisteredVhutUserAbstractionList() {
        List<CloudUser> cloudUserList = new ArrayList<CloudUser>();
        cloudUserList = cloudServiceLogic.getAllUserList();
        //        int i = 0;
        List<VhutUser> vhutUserList = new ArrayList<VhutUser>();
        for (CloudUser cloudUser : cloudUserList) {
            //        for (i = 0; i < cloudUserList.size(); i++) {
            //cloudUserList.cloudUserIdがVhutUserCloudUserMapテーブルに存在しなければ、
            //未登録ユーザリスト：vhutUserListに追加する
            long count = jdbcManager.from(VhutUserCloudUserMap.class)
                .where(new SimpleWhere().eq(vhutUserCloudUserMap().cloudUserId(), cloudUser.id))
                .getCount();
            if (count == 0) {
                VhutUser vhutUser = new VhutUser();
                VhutUserCloudUserMap vhutUserCloudUserMap = new VhutUserCloudUserMap();
                List<VhutUserCloudUserMap> vhutUserCloudUserMapList = new ArrayList<VhutUserCloudUserMap>();
                vhutUserCloudUserMap.cloudUserId = cloudUser.id;
                vhutUserCloudUserMap.cloudId = cloudUser.cloudId;
                vhutUserCloudUserMapList.add(vhutUserCloudUserMap);
                vhutUser.account = cloudUser.account;
                vhutUser.firstName = cloudUser.firstName;
                vhutUser.lastName = cloudUser.lastName;
                vhutUser.email = cloudUser.email;
                vhutUser.vhutUserCloudUserMapList = vhutUserCloudUserMapList;
                vhutUserList.add(vhutUser);
            }
        }
        return vhutUserList;
    }

    /**
     * <p>ユーザ一括検証.
     * <br>
     *
     * @param   accountList アカウントのリスト
     * @return VhutUserList ユーザのリスト
     * @throws CloudUserNotFoundException 検索不可能なユーザが発生
     */
    public List<VhutUser> validateVhutUserList(List<String> accountList) throws CloudUserNotFoundException {
        //defaultRoleIdの確認
        Role defaultRole = null;
        try {
            defaultRole = jdbcManager.from(Role.class)
                .where(new SimpleWhere().eq(role().isDefault(), true))
                .disallowNoResult()
                .getSingleResult();
        } catch (SNonUniqueResultException e) {
            throw new DBStateRuntimeException(Role.class, 0L, "multiple default role");
        } catch (SNoResultException e) {
            throw new DBStateRuntimeException(Role.class, 0L, "no default role");
        }
        // 引数のアカウントリストでvHutUserに登録されていないアカウントのリストを作成する。
        List<String> noRegisterAccountList = new ArrayList<String>();
        List<VhutUser> vhutUserList = new ArrayList<VhutUser>();
        for (String account : accountList) {
            VhutUser localUser = jdbcManager.from(VhutUser.class)
                .where(new SimpleWhere().eq(vhutUser().account(), account))
                .getSingleResult();
            if (localUser == null) {
                noRegisterAccountList.add(account);
            } else {
                vhutUserList.add(localUser);
            }
        }
        // クラウドユーザ一覧を取得する。
        List<CloudUser> cloudUserList = cloudServiceLogic.getAllUserList();
        // vHutUserに登録されていないアカウントがcloudUserListに登録されているか確認する。
        List<String> notFoundAccountList = new ArrayList<String>();
        for (String noRegisterAccount : noRegisterAccountList) {
            boolean isNotFound = true;
            for (CloudUser cloudUser : cloudUserList) {
                if (noRegisterAccount.equals(cloudUser.account)) {
                    isNotFound = false;

                    // VhutUser挿入
                    VhutUser vhutUser = new VhutUser();
                    vhutUser.account = noRegisterAccount;
                    vhutUser.firstName = cloudUser.firstName;
                    vhutUser.lastName = cloudUser.lastName;
                    vhutUser.email = cloudUser.email;
                    jdbcManager.insert(vhutUser)
                        .execute();
                    // VhutUserCloudUserMap挿入
                    VhutUserCloudUserMap map = new VhutUserCloudUserMap();
                    map.cloudId = cloudUser.cloudId;
                    map.cloudUserId = cloudUser.id;
                    map.vhutUserId = vhutUser.id;
                    jdbcManager.insert(map)
                        .execute();
                    // RoleMap挿入
                    VhutUserRoleMap roleMap = new VhutUserRoleMap();
                    roleMap.vhutUserId = vhutUser.id;
                    roleMap.roleId = defaultRole.id;
                    jdbcManager.insert(roleMap)
                        .execute();
                    vhutUserList.add(vhutUser);
                    break;
                }
            }
            if (isNotFound) {
                notFoundAccountList.add(noRegisterAccount);
            }
        }
        if (notFoundAccountList.size() > 0) {
            throw new CloudUserNotFoundException(notFoundAccountList.toArray(new String[0]));
        }
        //VhutUserの値を返す。
        return vhutUserList;
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
