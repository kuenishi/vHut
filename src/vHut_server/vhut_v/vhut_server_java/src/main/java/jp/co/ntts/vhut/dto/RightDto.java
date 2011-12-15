package jp.co.ntts.vhut.dto;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * <p>アプリケーションインスタンスグループサービスのスタブクラス.
 * <br>
 * <p>テスト時に利用する。
 * 
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * 
 */
public class RightDto implements Serializable {

    /**
     * <p>権限のデータモデルクラス.
     * <br>
     * <p>
     * 
     * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
     * $Revision: 949 $
     * $Author: NTT Software Corporation. $
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 対象
     */
    @Column(precision = 10, nullable = false, unique = true)
    public String target;

    /**
     * 最大値
     */
    @Column(precision = 10, nullable = false, unique = true)
    public int max;

    /**
     * 現在値
     */
    @Column(precision = 10, nullable = false, unique = true)
    public int now;
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
