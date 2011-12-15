/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.exception;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.StringConversionUtil;

/**
 * <p>状態不一致の時に発生する例外.
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
public abstract class AbstractConflictException extends AbstractVhutException {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = -3915387807495054415L;


    /**
     * <p>不一致状態をリスト化する際に用いる属性.
     * <br>
     */
    class Entry {
        /** パラメータ名 */
        private EntryType type;
        /** パラメータ名 */
        private String param;
        /** DBの値 */
        private Object local;
        /** 同期先の値 */
        private Object remote;


        /**
         * コンストラクタ.
         * @param type 不一致エントリのタイプ
         * @param param パラメータ名
         * @param local DBの値
         * @param remote 同期先の値
         */
        public Entry(EntryType type, String param, Object local, Object remote) {
            this.type = type;
            this.param = param;
            this.local = local;
            this.remote = remote;
        }
    }

    /** 不一致エントリのタイプ */
    enum EntryType {
        /** 変更 */
        CHANGED,
        /** 削除 */
        REMOVED
    }


    /**
     * 対象となるエンティティ.
     */
    protected Object entity;

    /**
     * 不一致の詳細.
     */
    protected String detail;

    /**
     * 不一致の一覧.
     */
    protected List<Entry> entries = new ArrayList<Entry>();


    /**
     * コンストラクタ.
     * @param messageCode メッセージコード
     * @param entity 対象となるエンティティ.
     */
    public AbstractConflictException(String messageCode, Object entity) {
        super(messageCode, new Object[]{ entity.toString() });
        this.entity = entity;
    }

    private static String createDetail(List<Entry> conflicts) {
        String details = "";
        for (Entry conflict : conflicts) {
            switch (conflict.type) {
                case CHANGED:
                    details += String.format("%s : local=%s, remote=%s\n", conflict.param, StringConversionUtil.toString(conflict.local), StringConversionUtil.toString(conflict.remote));
                    break;
                case REMOVED:
                    details += String.format("%s is removed", conflict.param);
                    break;
                default:
                    break;
            }
        }
        if (conflicts.size() > 0) {
            details = details.substring(0, details.length() - 1);
        }
        return details;
    }

    /**
     * 不一致の個数を返します.
     * @return 不一致の個数
     */
    public int getSize() {
        return entries.size();
    }

    /**
     * 状態変更による不一致情報を追加します.
     * @param param パラメータ名
     * @param local DBの値
     * @param remote 同期先の値
     */
    public void addChanged(String param, Object local, Object remote) {
        entries.add(new Entry(EntryType.CHANGED, param, local, remote));
        updateDetail();
    }

    /**
     * 要素削減による不一致情報を追加します.
     * @param param パラメータ名
     */
    public void addRemoved(String param) {
        entries.add(new Entry(EntryType.REMOVED, param, null, null));
        updateDetail();
    }

    /**
     * @return 対象となるエンティティ.
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * @return 不一致の詳細情報
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 不一致の詳細情報を更新する.
     */
    protected void updateDetail() {
        detail = createDetail(entries);
        String messageCode = getMessageCode();
        String simpleMessage = MessageFormatter.getSimpleMessage(messageCode, new Object[]{ entity, detail });
        String message = "[" + messageCode + "]" + simpleMessage;
        setMessage(message);
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
