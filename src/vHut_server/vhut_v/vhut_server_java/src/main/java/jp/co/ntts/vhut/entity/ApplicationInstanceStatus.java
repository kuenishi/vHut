/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>{@link jp.co.ntts.vhut.entity.ApplicationInstance#status}の値を定義します.
 * <p>ApplicationInstanceは以下の状態をもちます
 * <ul>
 * <li>NONE
 * <li>CREATING
 * <li>DEACTIVE
 * <li>REBUILDING
 * <li>ACTIVE
 * <li>DELETING
 * <li>DELETED
 * </ul>
 * <p>ApplicationInstanceGroupは以下の状態を遷移を行います
 * <ul>
 * <li>NONE -> CREATING : {@link @link jp.co.ntts.vhut.task.ServiceTask#doCreateAI}
 * <li>CREATING -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckCreatingApplicationInstance() }
 * <li>DEACTIVE -> ACTIVE : {@link jp.co.ntts.vhut.task.ServiceTask#doStartA}
 * <li>ACTIVE -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceTask#doStopA}
 * <li>DEACTIVE -> REBUILDING : {@link @link jp.co.ntts.vhut.service.ApplicationInstanceGroupService#rebuildApplicationInstanceById(Long)}
 * <li>REBUILDING -> DEACTIVE : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckRebuildingApplicationInstance() }
 * <li>DEACTIVE -> DELETING : {@link @link jp.co.ntts.vhut.task.ServiceTask#doDeleteAI}
 * <li>DELETING -> DELETED : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckDeletingApplicationInstance() }
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
public enum ApplicationInstanceStatus {

    /** 0.初期状態（VMなし） */
    NONE,
    /** 1.作成中 */
    CREATING,
    /** 2.作成済み、使用不可 */
    DEACTIVE,
    /** 3.再作成中 */
    REBUILDING,
    /** 4.使用可能 */
    ACTIVE,
    /** 5.削除中 */
    DELETING,
    /** 6.削除済 */
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
