/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>{@link jp.co.ntts.vhut.entity.Application#status}の値を定義します.
 * <p>Applicationは以下の状態をもちます
 * <ul>
 * <li>NONE
 * <li>CREATING
 * <li>DEACTIVE
 * <li>UPDATING
 * <li>RELEASING
 * <li>ACTIVE
 * <li>DELETING
 * <li>DELETED
 * </ul>
 * <p>Applicationは以下の状態を遷移を行います
 * <ul>
 * <li>NONE -> CREATING : {@link jp.co.ntts.vhut.service.ApplicationService#setApplication}
 * <li>CREATING -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckCreatingApplication()}
 * <li>DEACTIVE -> ACTIVE : {@link jp.co.ntts.vhut.task.ServiceTask#doStartA}
 * <li>ACTIVE -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceTask#doStopA}
 * <li>DEACTIVE -> RELEASING : {@link jp.co.ntts.vhut.service.ApplicationService#addReleasedApplicationByApplicationId(Long)}
 * <li>RELEASING -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckReleasingApplication()}
 * <li>DEACTIVE -> UPDATING : {@link jp.co.ntts.vhut.service.ApplicationService#update(Application)}
 * <li>UPDATING -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckUpdatingApplication()}
 * <li>DEACTIVE -> DELETING : {@link jp.co.ntts.vhut.service.ApplicationService#deleteApplicationById(Long)}
 * <li>DELETING -> DELETED : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckDeletingApplication()}
 * </ul>
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
public enum ApplicationStatus {

    /** 0.初期状態（VMなし） */
    NONE,
    /** 1.作成中 */
    CREATING,
    /** 2.作成済み、使用不可 */
    DEACTIVE,
    /** 3.使用可能 */
    ACTIVE,
    /** 4.テンプレート作成中 */
    RELEASING,
    /** 5.構成変更中 */
    UPDATING,
    /** 6.削除中 */
    DELETING,
    /** 7.削除後 */
    DELETED

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
