/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.service;

import static jp.co.ntts.vhut.entity.Names.baseTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.config.ServiceConfig;
import jp.co.ntts.vhut.dto.AdditionalDiskDto;
import jp.co.ntts.vhut.dto.SpecDto;
import jp.co.ntts.vhut.dto.SwitchTemplateDto;
import jp.co.ntts.vhut.entity.BaseTemplate;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.entity.Template;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.factory.CloudLogicFactory;
import jp.co.ntts.vhut.logic.ICloudServiceLogic;
import jp.co.ntts.vhut.util.VhutUtil;

import org.seasar.framework.container.annotation.tiger.InitMethod;

/**
 * <p>ベーステンプレートのサービスクラスです.
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
@Generated(value = { "S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl" }, date = "2011/07/15 17:07:07")
public class BaseTemplateService extends AbstractService<BaseTemplate> {

    /**
     * CloudLogicを取得するためのファクトリ
     */
    public CloudLogicFactory cloudLogicFactory;

    /**
     * サービス関連の設定
     */
    public ServiceConfig serviceConfig;

    /**
     * クラウドの識別子
     */
    protected long cloudId = 1;

    /**
     * クラウドロジックのサービス面
     */
    protected ICloudServiceLogic cloudServiceLogic;


    @InitMethod
    public void init() {
        cloudServiceLogic = cloudLogicFactory.newCloudServiceLogic(cloudId);
    }

    /**
     * <p>ベーステンプレート概要一覧取得.
     * <br>
     *
     * @return BaseTemplate ベーステンプレート概要情報のリスト
     */
    @Auth(right = Right.READ_SYS_TEMPLATE)
    public List<BaseTemplate> getAllBaseTemplateAbstractionList() {
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        return jdbcManager.from(BaseTemplate.class)
            .getResultList();
    }

    /**
     * <p>ベーステンプレート概要一覧取得.
     * <br>
     *
     * @return BaseTemplate ベーステンプレート概要情報のリスト
     */
    @Auth(right = Right.READ_SYS_TEMPLATE)
    public List<BaseTemplate> getAllBaseTemplateList() {
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<BaseTemplate> baseTemplateList = jdbcManager.from(BaseTemplate.class)
            .eager(baseTemplate().description(), baseTemplate().templateId(), baseTemplate().cloudId())
            .getResultList();
        for (BaseTemplate baseTemplate : baseTemplateList) {
            baseTemplate.template = cloudServiceLogic.getTemplateById(baseTemplate.templateId);
        }
        return baseTemplateList;
    }

    /**
     * <p>ベーステンプレート詳細取得.
     * <br>
     *
     * @param id ベーステンプレートID
     * @return BaseTemplate ベーステンプレート詳細情報
     */
    @Auth(right = Right.READ_SYS_TEMPLATE)
    public BaseTemplate getBaseTemplateById(Long id) {
        //引数のベーステンプレートIDを条件に、セレクトする。
        BaseTemplate baseTemplate = jdbcManager.from(BaseTemplate.class)
            .id(id)
            .eager(baseTemplate().description(), baseTemplate().templateId(), baseTemplate().cloudId())
            .getSingleResult();

        if (baseTemplate == null) {
            throw new InputRuntimeException("id", String.format("BaseTemplate(id=%d) was not found", id));
        }

        //セレクトした値が取得出来た場合、baseTemplateの値をreturnする。
        baseTemplate.template = cloudServiceLogic.getTemplateById(baseTemplate.templateId);
        return baseTemplate;
    }

    /**
     * <p>ベーステンプレート追加.
     * <br>
     *
     * @param remoteTemplate ベーステンプレート
     * @return
     */
    @Auth(right = Right.CREATE_SYS_TEMPLATE)
    public void createBaseTemplate(BaseTemplate remoteTemplate) {
        if (remoteTemplate.templateId <= 0) {
            throw new InputRuntimeException("remoteTemplate", "templateId is null.");
        }

        BaseTemplate localTemplate = new BaseTemplate();
        localTemplate.cloudId = cloudId;
        localTemplate.name = remoteTemplate.name;
        localTemplate.description = remoteTemplate.description;
        localTemplate.imageUrl = remoteTemplate.imageUrl;
        localTemplate.templateId = remoteTemplate.templateId;

        jdbcManager.insert(localTemplate)
            .execute();
    }

    /**
     * <p>ベーステンプレート更新.
     * <br>
     *
     * @param remoteTemplate ベーステンプレート
     * @return
     */
    @Auth(right = Right.UPDATE_SYS_TEMPLATE)
    public void updateBaseTemplate(BaseTemplate remoteTemplate) {
        if (remoteTemplate.templateId <= 0) {
            throw new InputRuntimeException("remoteTemplate", "templateId is null.");
        }

        BaseTemplate localTemplate = jdbcManager.from(BaseTemplate.class)
            .id(remoteTemplate.id)
            .getSingleResult();

        if (localTemplate == null) {
            throw new InputRuntimeException("remoteTemplate", String.format("BaseTemplate(id=%d) was not found", remoteTemplate.id));
        }

        localTemplate.name = remoteTemplate.name;
        localTemplate.description = remoteTemplate.description;
        localTemplate.imageUrl = remoteTemplate.imageUrl;
        localTemplate.templateId = remoteTemplate.templateId;

        jdbcManager.update(localTemplate)
            .includes(baseTemplate().name(), baseTemplate().description(), baseTemplate().imageUrl(), baseTemplate().templateId())
            .execute();
    }

    /**
     * <p>ベーステンプレート削除.
     * <br>
     *
     * @param id ベーステンプレートID
     * @return
     */
    @Auth(right = Right.DELETE_SYS_TEMPLATE)
    public void deleteBaseTemplateById(Long id) {
        BaseTemplate localTemplate = jdbcManager.from(BaseTemplate.class)
            .id(id)
            .getSingleResult();

        if (localTemplate == null) {
            throw new InputRuntimeException("id", String.format("BaseTemplate(id=%d) was not found", id));
        }

        jdbcManager.delete(localTemplate)
            .execute();
    }

    /**
     * <p>未登録テンプレート詳細一覧取得.
     * <br>
     *
     * @param
     * @return Template テンプレート詳細情報のリスト
     */
    public List<Template> getUnregisteredTemplateList() {
        //テンプレート詳細一覧を取得する。
        //一覧が取得できない（NULL）場合は、S2JDBCの仕様で空のリストを返す。
        List<Template> templateList = cloudServiceLogic.getAllTemplateList();
        //releasedApplicationに関連するテンプレートを除く
        List<Template> unregisteredTemplateList = new ArrayList<Template>();
        for (Template template : templateList) {
            if (!VhutUtil.hasPrefix(template.name, serviceConfig.releasedApplicationTemplatePrefix)) {
                unregisteredTemplateList.add(template);
            }
        }
        return unregisteredTemplateList;
    }

    /**
     * <p>スイッチテンプレート概要一覧取得.
     * <br>
     *
     * @param
     * @return SwitchTemplate スイッチテンプレート概要情報のリスト
     */
    public List<SwitchTemplateDto> getAllSwitchTemplateAbstractionList() {
        return cloudServiceLogic.getAllSwitchTemplateList();
    }

    /**
     * <p>ディスクテンプレート概要一覧取得.
     * <br>
     *
     * @param
     * @return DiskTemplate ディスクテンプレート概要情報のリスト
     */
    public List<AdditionalDiskDto> getAllDiskTemplateAbstractionList() {

        return cloudServiceLogic.getAllAdditionalDiskList();
    }

    /**
     * <p>スペック概要情報一覧取得.
     * <br>
     *
     * @param
     * @return SpecTemplate スペックテンプレート概要情報のリスト
     */
    public List<SpecDto> getAllSpecAbstractionList() {
        return cloudServiceLogic.getAllSpecList();

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
