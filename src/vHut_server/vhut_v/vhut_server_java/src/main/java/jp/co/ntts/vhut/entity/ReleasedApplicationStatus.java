/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

/**
 * <p>{@link jp.co.ntts.vhut.entity.ReleasedApplication#status}の値を定義します.
 * <p>ReleasedApplicationは以下の状態をもちます
 * <ul>
 * <li>NONE
 * <li>CREATING
 * <li>READY
 * <li>DELETING
 * <li>DELETED
 * </ul>
 * <p>Applicationは以下の状態を遷移を行います
 * <ul>
 * <li>NONE -> CREATING : {@link jp.co.ntts.vhut.service.ApplicationService#addReleasedApplicationByApplicationId(Long)}
 * <li>CREATING -> READY : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckCreatingReleasedApplication()}
 * <li>READY -> DELETING : {@link jp.co.ntts.vhut.service.ApplicationService#removeReleasedApplicationById(Long)}
 * <li>DELETING -> DELETED : {@link jp.co.ntts.vhut.task.ServiceCheckTask#doCheckDeletingReleasedApplication()}
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
public enum ReleasedApplicationStatus {

    /** 0.初期状態（VMなし） */
    NONE,
    /** 1.作成中 */
    CREATING,
    /** 2.展開可能 */
    READY,
    /** 3.削除中 */
    DELETING,
    /** 4.削除後 */
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
