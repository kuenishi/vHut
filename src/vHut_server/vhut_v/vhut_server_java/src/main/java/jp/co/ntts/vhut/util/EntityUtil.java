/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ntts.vhut.entity.IIdentifiableEntity;

/**
 * <p>VMエンティティに関する共通的な処理を実施します.
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
public class EntityUtil {

    private EntityUtil() {
    }

    /**
     * Idで識別可能な要素をもったリストを比較して、追加、更新、削除の各リストに振り分けます.
     * 
     * @param localList 既存リスト
     * @param remoteList　新規リスト
     * @return 更新のための選別作業の結果
     */
    public static final SortOutResult sortOutToSync(IIdentifiableEntity[] localList, IIdentifiableEntity[] remoteList) {

        List<IIdentifiableEntity> toAddList = new ArrayList<IIdentifiableEntity>();
        List<ToUpdateValue> toUpdateList = new ArrayList<ToUpdateValue>();
        List<IIdentifiableEntity> toRemoveList = new ArrayList<IIdentifiableEntity>();

        //マップの作成
        Map<Long, IIdentifiableEntity> localEntityMap = new HashMap<Long, IIdentifiableEntity>();
        for (IIdentifiableEntity localEntity : localList) {
            localEntityMap.put(localEntity.getId(), localEntity);
        }

        //選別作業
        for (IIdentifiableEntity remoteEntity : remoteList) {
            IIdentifiableEntity localEntity = localEntityMap.remove(remoteEntity.getId());
            if (localEntity == null) {
                //追加リストへ
                toAddList.add(remoteEntity);
            } else {
                //更新リストへ
                ToUpdateValue value = new ToUpdateValue();
                value.local = localEntity;
                value.remote = remoteEntity;
                toUpdateList.add(value);
            }
        }
        //削除リストへ
        toRemoveList.addAll(localEntityMap.values());
        //結果の作成
        SortOutResult result = new SortOutResult();
        result.toAddList = toAddList.toArray(new IIdentifiableEntity[]{});
        result.toUpdateList = toUpdateList.toArray(new ToUpdateValue[]{});
        result.toRemoveList = toRemoveList.toArray(new IIdentifiableEntity[]{});
        return result;

    }


    /**
     * <p>更新のための選別作業の結果です.
     */
    public static class SortOutResult {
        /** 追加されるべき要素 */
        public IIdentifiableEntity[] toAddList;
        /** 更新されるべき要素 */
        public ToUpdateValue[] toUpdateList;
        /** 削除されるべき要素 */
        public IIdentifiableEntity[] toRemoveList;
    }

    public static class ToUpdateValue {
        public IIdentifiableEntity remote;
        public IIdentifiableEntity local;
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
