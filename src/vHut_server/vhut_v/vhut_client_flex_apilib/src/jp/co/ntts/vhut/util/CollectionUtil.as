/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;

	/**
	 * コレクションに関連する便利クラス.
	 *
	 * <p>
	 * <b>Author :</b> NTT Software Corporation.
	 * <b>Version :</b> 1.0.0
	 * </p>
	 *
	 * @langversion 3.0
	 * @playerversion Flash 10.1
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class CollectionUtil
	{

		/**
		 * コレクションを二つ融合して新しいコレクションを作成します.
		 * @param list0 コレクション
		 * @param list1 コレクション
		 * @param cache キャッシュ
		 * @return コレクション
		 */
		public static function fusion(list0:IList, list1:IList):ArrayCollection
		{
			var i:int;
			var item:Object;
			var result:ArrayCollection = new ArrayCollection();
			var updateFunction:Function = function(event:CollectionEvent):void
				{
					var targetList:IList = event.target as IList;
					if(targetList == null) return;
					switch(event.kind)
					{
						case CollectionEventKind.ADD:
							for each(item in event.items)
							{
								result.addItem(item);
							}
							break;
						case CollectionEventKind.REMOVE:
							for each(item in event.items)
							{
								result.removeItemAt(result.getItemIndex(item));
							}
							break;
						case CollectionEventKind.REPLACE:
							var item:Object = targetList.getItemAt(event.location);
							result.removeItemAt(result.getItemIndex(item));
							result.addItem(item);
							break;
						case CollectionEventKind.UPDATE:
//							item = targetList.getItemAt(event.location);
							break;
						case CollectionEventKind.RESET:
							result.removeAll();
							result.addAll(list0);
							result.addAll(list1);
							break;
						case CollectionEventKind.MOVE:
						case CollectionEventKind.REFRESH:
							break;
					}
				}
			list0.addEventListener(CollectionEvent.COLLECTION_CHANGE, updateFunction);
			list1.addEventListener(CollectionEvent.COLLECTION_CHANGE, updateFunction);
			result.addAll(list0);
			result.addAll(list1);
			return result;
		}

		/**
		 * コレクションの複合キーフィルタ関数
		 * @param item 判定対象
		 * @param fields フィールドのリスト
		 * @param keywords キーワードのリスト
		 * @return 判定結果
		 *
		 */
		public static function multiFilter(item:Object, fields:Array, keywords:Array):Boolean
		{
			top: for each (var keyword:String in keywords)
			{
				for each (var field:String in fields)
				{
					if(item[field].toString().indexOf(keyword) >= 0)
						continue top;
				}
				return false;
			}
			return true;
		}
	}
}