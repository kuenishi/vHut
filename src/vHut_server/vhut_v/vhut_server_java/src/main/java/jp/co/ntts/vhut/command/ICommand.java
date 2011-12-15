/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.command;

import java.io.Serializable;

import jp.co.ntts.vhut.entity.Command;
import jp.co.ntts.vhut.entity.CommandStatus;

/**
 * <p>コマンドクラスの共通インターフェース.
 * <br>
 * <p>Driverへのアクセスは以下の手順を経る。
 * <p>{@link jp.co.ntts.vhut.logic.PrivateCloudLogic}による登録
 * <ul>
 * <li>コマンドクラスの生成：各クラス独自にinitメソッドを実装
 * <li>DBへの登録:toCommand();
 * </ul>
 * <p>{@link jp.co.ntts.vhut.task.CloudTask}による状態更新
 * <ul>
 * <li>コマンドクラスの生成：init(Command command)を使い、DBから読みだした{@link jp.co.ntts.vhut.entity.Command}を基にコマンドクラスを初期化
 * <li>状態を確認(内部的にDriverを実行):updateStatus()
 * <li>戻り値を基にDBを更新
 * </ul>
 * <p>{@link jp.co.ntts.vhut.task.CloudTask}による実行
 * <ul>
 * <li>コマンドクラスの生成：init(Command command)を使い、DBから読みだした{@link jp.co.ntts.vhut.entity.Command}を基にコマンドクラスを初期化
 * <li>コマンドの実行(内部的にDriverを実行):execute()
 * <li>戻り値を基にDBを更新
 * <li>戻り値が成功であれば、finish()を実行（終了時手続きが実行される.）
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
public interface ICommand {

    /**
     * クラウドの識別子を用いて初期化する.
     * @param cloudId クラウドの識別子
     */
    void init(long cloudId);

    /**
     * コマンドエンティティ{@link jp.co.ntts.vhut.entity.Command}を用いて初期化する.
     * 引数のコマンドはこのクラスから参照され適宜更新されます
     * @param command コマンドエンティティ（DBから取得する）
     */
    void init(Command command);

    /**
     * コマンドの実行状態を更新する.
     * コマンドエンティティを更新します
     * @return コマンドの実行状態
     */
    CommandStatus updateStatus();

    /**
     * コマンドを実行する.
     * @return コマンドの実行状態
     */
    CommandStatus execute();

    /**
     * コマンド正常終了時の後処理を実行する.
     */
    void finish();

    /**
     * コマンドエンティティ{@link jp.co.ntts.vhut.entity.Command}を生成する.
     * @return コマンドエンティティ
     */
    Command getCommand();

    /**
     * 実行結果を得る.
     * @return 実行結果.
     */
    Object getResult();

    /**
     * パラメーターをセットする.
     * @param args セットするパラメーター; 内部ではEnumMap<Key, Serializable>型
     */
    void setParameter(Serializable... args);

    /**
     * @return 制限時間オーバー
     */
    boolean isTimeOut();
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
