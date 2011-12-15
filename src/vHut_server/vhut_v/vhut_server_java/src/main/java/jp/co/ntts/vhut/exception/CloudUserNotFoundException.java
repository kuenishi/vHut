/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import org.apache.commons.lang.StringUtils;

/**
 * ユーザ追加を依頼されたユーザに対応するクラウドユーザが存在しない場合に生成される例外.
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
public class CloudUserNotFoundException extends AbstractVhutException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 8039635245586506243L;

    /**
     * 発見できなかったアカウントのリスト
     */
    private String[] accounts;


    /**
     * @param accounts 発見できなかったアカウントのリスト
     */
    public CloudUserNotFoundException(String[] accounts) {
        super("WSRVS5051", new Object[]{ StringUtils.join(accounts) });
        this.accounts = accounts;
    }

    /**
     * @return the accounts
     */
    public String[] getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(String[] accounts) {
        this.accounts = accounts;
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
