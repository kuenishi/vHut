/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.entity;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.exception.InternalRuntimeException;

/**
 * <p>{@link Auth#right}に設定する列挙型
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
public enum Right {
    //////////////////////////////////////////
    //  基本情報
    //////////////////////////////////////////
    /** 0.基本情報を[参照]する. */
    READ_SYS_BASIC

    //////////////////////////////////////////
    //  自分のアプリケーション
    //////////////////////////////////////////
    /** 1.自分のアプリケーションを[参照]する. */
    , READ_OWN_APP
    /** 2.自分のアプリケーションの[VMを起動停止]する. */
    , POWER_OWN_APP
    /** 3.自分のアプリケーションの[VMの画面を取得]する. */
    , DISPLAY_OWN_APP
    /** 4.自分のアプリケーションの[コマンドを制御]する. */
    , COMMAND_OWN_APP
    /** 5.自分のアプリケーションの[起動可能状態を変更]する. */
    , STATUS_OWN_APP
    /** 6.自分のアプリケーションの[起動可能期間を更新]する. */
    , TERM_OWN_APP
    /** 7.自分のアプリケーションを[更新]する. */
    , UPDATE_OWN_APP
    /** 8.自分のアプリケーションを[作成]する. */
    , CREATE_OWN_APP
    /** 9.自分のアプリケーションを[削除]する. */
    , DELETE_OWN_APP

    //////////////////////////////////////////
    //  すべてのアプリケーション
    //////////////////////////////////////////
    /** 10.すべてのアプリケーションを[参照]する. */
    , READ_ALL_APP
    /** 11.すべてのアプリケーションの[VMを起動停止]する. */
    , POWER_ALL_APP
    /** 12.すべてのアプリケーションの[VMの画面を取得]する. */
    , DISPLAY_ALL_APP
    /** 13.すべてのアプリケーションの[コマンドを制御]する. */
    , COMMAND_ALL_APP
    /** 14.すべてのアプリケーションの[起動可能状態を変更]する. */
    , STATUS_ALL_APP
    /** 15.すべてのアプリケーションの[起動可能期間を更新]する. */
    , TERM_ALL_APP
    /** 16.すべてのアプリケーションを[更新]する. */
    , UPDATE_ALL_APP
    /** 17.すべてのアプリケーションを[作成]する. */
    , CREATE_ALL_APP
    /** 18.すべてのアプリケーションを[削除]する. */
    , DELETE_ALL_APP

    //////////////////////////////////////////
    //  すべてのリリースド・アプリケーション
    //////////////////////////////////////////
    /** 19.すべてのリリースド・アプリケーションを[参照]する. */
    , READ_ALL_RAPP
    /** 20.すべてのリリースド・アプリケーションの[コマンドを制御]する. */
    , COMMAND_ALL_RAPP
    /** 21.すべてのリリースド・アプリケーションを[作成]する. */
    , CREATE_ALL_RAPP
    /** 22.すべてのリリースド・アプリケーションを[削除]する. */
    , DELETE_ALL_RAPP

    //////////////////////////////////////////
    //  自分のリリースド・アプリケーション
    //////////////////////////////////////////
    /** 23.自分のリリースド・アプリケーションを[参照]する. */
    , READ_OWN_RAPP
    /** 24.自分のリリースド・アプリケーションの[コマンドを制御]する. */
    , COMMAND_OWN_RAPP
    /** 25.自分のリリースド・アプリケーションを[作成]する. */
    , CREATE_OWN_RAPP
    /** 26.自分のリリースド・アプリケーションを[削除]する. */
    , DELETE_OWN_RAPP

    //////////////////////////////////////////
    //  自分のアプリケーション・インスタンス・グループ
    //////////////////////////////////////////
    /** 27.自分のアプリケーション・インスタンス・グループを[参照]する. */
    , READ_OWN_AIG
    /** 28.自分のアプリケーション・インスタンス・グループを[更新]する. */
    , UPDATE_OWN_AIG
    /** 29.自分のアプリケーション・インスタンス・グループを[作成]する. */
    , CREATE_OWN_AIG
    /** 30.自分のアプリケーション・インスタンス・グループを[削除]する. */
    , DELETE_OWN_AIG

    //////////////////////////////////////////
    //  すべてのアプリケーション・インスタンス・グループ
    //////////////////////////////////////////
    /** 31.すべてのアプリケーション・インスタンス・グループを[参照]する. */
    , READ_ALL_AIG
    /** 32.すべてのアプリケーション・インスタンス・グループを[更新]する. */
    , UPDATE_ALL_AIG
    /** 33.すべてのアプリケーション・インスタンス・グループを[作成]する. */
    , CREATE_ALL_AIG
    /** 34.すべてのアプリケーション・インスタンス・グループを[削除]する. */
    , DELETE_ALL_AIG

    //////////////////////////////////////////
    //  自分のアプリケーション・インスタンス
    //////////////////////////////////////////
    /** 35.自分のアプリケーション・インスタンスを[参照]する. */
    , READ_OWN_AI
    /** 36.自分のアプリケーション・インスタンスの[VMを起動停止]する. */
    , POWER_OWN_AI
    /** 37.自分のアプリケーション・インスタンスの[VMの画面を取得]する. */
    , DISPLAY_OWN_AI
    /** 38.自分のアプリケーション・インスタンスの[コマンドを制御]する. */
    , COMMAND_OWN_AI
    /** 39.自分のアプリケーションの[起動可能状態を変更]する. */
    , STATUS_OWN_AI
    /** 40.自分のアプリケーション・インスタンスを[再作成]する. */
    , REBUILD_OWN_AI

    //////////////////////////////////////////
    //  自分のアプリケーション・インスタンス・グループ配下のアプリケーション・インスタンス
    //////////////////////////////////////////
    /** 41.自分のアプリケーション・インスタンス・グループ配下のアプリケーション・インスタンスを[参照]する. */
    , READ_CHILD_AI
    /** 42.自分のアプリケーション・インスタンス・グループ配下のアプリケーション・インスタンスの[VMを起動停止]する. */
    , POWER_CHILD_AI
    /** 43.自分のアプリケーション・インスタンス・グループ配下のアプリケーション・インスタンスの[VMの画面を取得]する. */
    , DISPLAY_CHILD_AI
    /** 44.自分のアプリケーション・インスタンス・グループ配下のアプリケーション・インスタンスの[コマンドを制御]する. */
    , COMMAND_CHILD_AI
    /** 45.自分のアプリケーション・インスタンス・グループ配下のアプリケーションの[起動可能状態を変更]する. */
    , STATUS_CHILD_AI
    /** 46.自分のアプリケーション・インスタンス・グループ配下のアプリケーション・インスタンスを[再作成]する. */
    , REBUILD_CHILD_AI

    //////////////////////////////////////////
    //  すべてのアプリケーション・インスタンス
    //////////////////////////////////////////
    /** 47.すべてのアプリケーション・インスタンスを[参照]する. */
    , READ_ALL_AI
    /** 48.すべてのアプリケーション・インスタンスの[VMを起動停止]する. */
    , POWER_ALL_AI
    /** 49.すべてのアプリケーション・インスタンスの[VMの画面を取得]する. */
    , DISPLAY_ALL_AI
    /** 50.すべてのアプリケーション・インスタンスの[コマンドを制御]する. */
    , COMMAND_ALL_AI
    /** 51.自分のアプリケーションの[起動可能状態を変更]する. */
    , STATUS_ALL_AI
    /** 52.すべてのアプリケーション・インスタンスを[再作成]する. */
    , REBUILD_ALL_AI

    //////////////////////////////////////////
    //  ベース・テンプレート
    //////////////////////////////////////////
    /** 53.ベース・テンプレートを[参照]する. */
    , READ_SYS_TEMPLATE
    /** 54.ベース・テンプレートを[更新]する. */
    , UPDATE_SYS_TEMPLATE
    /** 55.ベース・テンプレートを[作成]する. */
    , CREATE_SYS_TEMPLATE
    /** 56.ベース・テンプレートを[削除]する. */
    , DELETE_SYS_TEMPLATE

    //////////////////////////////////////////
    //  ロール
    //////////////////////////////////////////
    /** 57.ロールを[参照]する. */
    , READ_SYS_ROLE
    /** 58.ロールを[更新]する. */
    , UPDATE_SYS_ROLE
    /** 59.ロールを[作成]する. */
    , CREATE_SYS_ROLE
    /** 60.ロールを[削除]する. */
    , DELETE_SYS_ROLE

    //////////////////////////////////////////
    //  自分のユーザ
    //////////////////////////////////////////
    /** 61.自分のユーザを[参照]する. */
    , READ_OWN_USER
    /** 62.自分のユーザを[更新]する. */
    , UPDATE_OWN_USER

    //////////////////////////////////////////
    //  すべてのユーザ
    //////////////////////////////////////////
    /** 63.すべてのユーザを[参照]する. */
    , READ_ALL_USER
    /** 64.すべてのユーザを[更新]する. */
    , UPDATE_ALL_USER
    /** 65.すべてのユーザを[作成]する. */
    , CREATE_ALL_USER
    /** 66.すべてのユーザを[削除]する. */
    , DELETE_ALL_USER

    //////////////////////////////////////////
    //  管理情報
    //////////////////////////////////////////
    /** 67.管理情報を[参照]する. */
    , READ_SYS_MANAGEMENT

    //////////////////////////////////////////
    //  設定
    //////////////////////////////////////////
    /** 68.設定を[参照]する. */
    , READ_SYS_CONFIGURATION
    /** 69.設定を[更新]する. */
    , UPDATE_SYS_CONFIGURATION;

    /**
     * @return 権限をbyte[]表示する際の長さ
     */
    public static final int getByteLength() {
        return (int) Math.ceil((double) values().length / 8d);
    }

    /**
     * @return 空の権限マップ
     */
    public static final byte[] getEmptyRights() {
        return new byte[getByteLength()];
    }


    private RightAction action;
    private RightLevel level;
    private RightTarget target;


    /**
     *
     */
    private Right() {
        updateChildren();
    }

    /**
     *
     */
    private void updateChildren() {
        String[] elements = name().split("_");
        if (elements.length != 3) {
            throw new InternalRuntimeException(String.format("Right#%s should have two under scores.", name()));
        }
        action = RightAction.valueOf(elements[0]);
        level = RightLevel.valueOf(elements[1]);
        target = RightTarget.valueOf(elements[2]);
    }

    /**
     * @return the action
     */
    public RightAction getAction() {
        return action;
    }

    /**
     * @return the level
     */
    public RightLevel getLevel() {
        return level;
    }

    /**
     * @return the target
     */
    public RightTarget getTarget() {
        return target;
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
