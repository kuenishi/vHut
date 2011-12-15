/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import static jp.co.ntts.vhut.entity.Names.template;
import static jp.co.ntts.vhut.entity.Names.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.driver.IPrivateCloudDriver;
import jp.co.ntts.vhut.driver.IPublicCloudDriver;
import jp.co.ntts.vhut.entity.Cloud;
import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.Conflict;
import jp.co.ntts.vhut.entity.ConflictStatus;
import jp.co.ntts.vhut.entity.ISynchronizedEntity;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.entity.Vm;
import jp.co.ntts.vhut.exception.DBConflictException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.factory.CloudDriverFactory;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudInfraLogic;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.VhutLogger;

import org.seasar.extension.jdbc.JdbcManager;

/**
 * <p>コマンドの抽象クラス.
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
public abstract class AbstractCloudDriverCommand extends AbstractCommand {

    private static final VhutLogger logger = VhutLogger.getLogger(AbstractCloudDriverCommand.class);

    /** JDBCの接続クラス */
    public JdbcManager jdbcManager;

    /** ドライバの作成クラス. */
    public CloudDriverFactory cloudDriverFactory;

    /** クラウドロジックの作成クラス. */
    public CloudLogicFactory cloudLogicFactory;

    /**  プライベートクラウドのドライバ. */
    protected IPrivateCloudDriver prcDirver;

    /**  パブリッククラウドのドライバ. */
    protected IPublicCloudDriver pucDirver;

    /**  クラウドのサービス部のIF. */
    protected ICloudServiceLogic cloudServiceLogic;

    /**  クラウドのサービス部のIF. */
    protected ICloudInfraLogic cloudInfraLogic;

    /**  プライベートクラウドに対するコマンドか？ */
    protected boolean isPrivate = true;


    /* (non-Javadoc)
     * @see jp.co.ntts.vhut.command.ICommand#init(long)
     */
    @Override
    public void init(long cloudId) {
        super.init(cloudId);
        setupDriver(cloudId);
        setupCloud(command.cloudId);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * jp.co.ntts.vhut.command.ICommand#init(jp.co.ntts.vhut.entity.Command)
     */
    @Override
    public void init(Command command) {
        super.init(command);
        setupDriver(command.cloudId);
        setupCloud(command.cloudId);
    }

    /**
     * ドライバを設定します.
     * @param cloudId クラウドID
     */
    protected void setupDriver(long cloudId) {
        Cloud cloud = jdbcManager.from(Cloud.class)
            .id(cloudId)
            .getSingleResult();
        if (cloud == null) {
            throw new InputRuntimeException("cloudId", String.format("Cloud(id=%d) was not found", cloudId));
        }
        if (cloud.type.isPrivate()) {
            isPrivate = true;
            prcDirver = cloudDriverFactory.getPrivateCloudDriver(cloudId);
        } else {
            isPrivate = false;
            pucDirver = cloudDriverFactory.getPublicCloudDriver(cloudId);
        }
    }

    /**
     * ドライバを設定します.
     * @param cloudId クラウドID
     */
    protected void setupCloud(long cloudId) {
        cloudServiceLogic = cloudLogicFactory.newCloudServiceLogic(cloudId);
        if (isPrivate) {
            cloudInfraLogic = cloudLogicFactory.newCloudInfraLogic(cloudId);
        }
    }

    /**
     * 同期対象エンティティが戻り値の時の更新処理.
     * @param clazz エンティティのクラス
     */
    protected void updateSynchronizedEntiteis(Class<?> clazz) {
        List<?> locals = new ArrayList<ISynchronizedEntity>();
        if (clazz.equals(Vm.class)) {
            //VMの時はnetworkAdapterとdiskを連結
            locals = jdbcManager.from(Vm.class)
                .leftOuterJoin(vm().networkAdapterList())
                .leftOuterJoin(vm().diskList())
                .getResultList();
        } else if (clazz.equals(Template.class)) {
            //Templateの時はnetworkAdapterTemplateとdiskTemplateを連結
            locals = jdbcManager.from(Template.class)
                .leftOuterJoin(template().networkAdapterTemplateList())
                .leftOuterJoin(template().diskTemplateList())
                .getResultList();
        } else {
            locals = jdbcManager.from(clazz)
                .getResultList();
        }

        Map<Long, ISynchronizedEntity> remoteMap = new HashMap<Long, ISynchronizedEntity>();

        for (ISynchronizedEntity remote : (ArrayList<ISynchronizedEntity>) result) {
            remoteMap.put(remote.getId(), remote);
        }

        for (Object object : locals) {
            ISynchronizedEntity local = (ISynchronizedEntity) object;
            ISynchronizedEntity remote = remoteMap.get(local.getId());
            updateSynchronizedEntity(local, remote);
        }
    }

    /**
     * エンティティを更新します.
     * @param local 内部のデータ
     * @param remote 外部のデータ
     */
    protected void updateSynchronizedEntity(ISynchronizedEntity local, ISynchronizedEntity remote) {
        if (remote != null) {
            try {
                //存在すれば、チェック
                if (local.sync(remote)) {
                    jdbcManager.update(local)
                        .execute();
                    for (Object child : local.getUpdatedChildren()) {
                        jdbcManager.update(child)
                            .execute();
                    }
                }
            } catch (DBConflictException e) {
                Conflict conflict = e.getConflict();
                if (conflict.id == null) {
                    jdbcManager.insert(conflict)
                        .execute();
                } else {
                    jdbcManager.update(conflict)
                        .execute();
                }
                local.setConflictId(conflict.id);
                jdbcManager.update(local)
                    .execute();
                return;
            }
            if (local.getConflictId() != null) {
                local.setConflictId(null);
                jdbcManager.update(local)
                    .execute();
            }
        } else {
            Conflict conflict = new Conflict();
            conflict.cloudId = local.getCloudId();
            conflict.status = ConflictStatus.UNFIXED;
            conflict.detail = "Removed";
            jdbcManager.insert(conflict)
                .execute();
            local.setConflictId(conflict.id);
            jdbcManager.update(local)
                .execute();
        }
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
