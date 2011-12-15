/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

/**
 * 認証が失敗した時に生成される例外.
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
public class AuthenticationException extends AbstractVhutException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -5444061540215282307L;

    private String account;


    /**
     * {@link AuthenticationException}を作成します。
     *
     * @param account ユーザアカウント
     */
    public AuthenticationException(String account) {
        super(null, new Object[]{ account });
        setMessageCode(account == null ? "WSRVS5011" : "WSRVS5012");
        this.account = account;
    }

    /**
     * @return the account アカウント名
     */
    public String getAccount() {
        return account;
    }

    /**
     * @return ディレクトリサービスでの認証は済んでいるが、VhutUserが見つからない.
     */
    public boolean isAuthenticatedByDirectory() {
        return account != null;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
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
