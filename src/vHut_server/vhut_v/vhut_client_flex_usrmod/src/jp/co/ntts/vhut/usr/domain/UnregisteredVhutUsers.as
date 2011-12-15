/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr.domain
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;

	[Event(name="getAllUnregisteredUser", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[ManagedEvents(names="getAllUnregisteredUser")]
	/**
	 *
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
	public class UnregisteredVhutUsers extends EventDispatcher
	{

		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_UNREGISTERED_VHUT_USERS:Array = ["fullName", "account"];

		public function UnregisteredVhutUsers(target:IEventDispatcher=null)
		{
			super(target);
//			_unregisteredVhutUsers = new ArrayCollection();
//			_unregisteredVhutUsers.filterFunction = filterFunction;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  UnregisteredVhutUsers
		//
		///////////////////////////////////////////////////////////////////////////////////////

		public function updateUnregisteredVhutUsers():void
		{
			dispatchEvent(GetAllEvent.newGetAllUnregisteredUserEvent());
		}

		[Bindable]
		/** ユーザのリスト */
		public function set unregisteredVhutUsers(value:IList):void
		{
//			var filterFunction:Function = _unregisteredVhutUsers.filterFunction;
//			_unregisteredVhutUsers.filterFunction = null;
//			_unregisteredVhutUsers.refresh();
//
//			_unregisteredVhutUsers.removeAll();
//			_unregisteredVhutUsers.addAll(value);
//
//			_unregisteredVhutUsers.filterFunction = filterFunction;
//			_unregisteredVhutUsers.refresh();

			_unregisteredVhutUsers = new ArrayCollection(value.toArray())
			_unregisteredVhutUsers.filterFunction = unregisteredVhutUsersFilter;
			_unregisteredVhutUsers.refresh();

		}
		public function get unregisteredVhutUsers():IList
		{
			return _unregisteredVhutUsers;
		}
		private var _unregisteredVhutUsers:ArrayCollection = new ArrayCollection();

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  FilterFunction
		//
		///////////////////////////////////////////////////////////////////////////////////////

		private var _filterReference:IList;
		private var _filterReferenceMap:Dictionary = new Dictionary();

		/** フィルタリング関数 */
		private function unregisteredVhutUsersFilter(value:Object):Boolean
		{
			var user:VhutUser = value as VhutUser;
			if(_filterReferenceMap[user.account] == null)
			{
				return CollectionUtil.multiFilter(value, unregisteredVhutUsersFilterFields, unregisteredVhutUsersFilterKeywords);
			}
			return false;
		}

		/** フィルタリングに用いるリスト. このリストに登録されているデータは表示されません. */
		public function set filterReference(value:IList):void
		{　
			if(_filterReference)
			{
				_filterReference.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onChangefilterReference);
			}
			_filterReference = value;
			_filterReference.addEventListener(CollectionEvent.COLLECTION_CHANGE, onChangefilterReference);
			updateFilterReferenceMap();
		}
		public function get filterReference():IList
		{
			return _filterReference;
		}

		/** 追加と削除とリセットの時は再フィルタリングします. */
		private function onChangefilterReference(event:CollectionEvent):void
		{
			switch(event.kind)
			{
				case CollectionEventKind.ADD:
				case CollectionEventKind.REMOVE:
				case CollectionEventKind.RESET:
					updateFilterReferenceMap();
					break;
			}
		}

		/** 再フィルタリング */
		private function updateFilterReferenceMap():void
		{
			_filterReferenceMap = new Dictionary();
			if(filterReference)
			{
				for each(var user:VhutUser in _filterReference)
				{
					_filterReferenceMap[user.account] = user;
				}
			}
			_unregisteredVhutUsers.refresh();
		}

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setUnregisteredVhutUsersfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_unregisteredVhutUsersFilterKeywords = keywords;
			}
			else
			{
				_unregisteredVhutUsersFilterKeywords = new Array();
			}

			if(fields)
			{
				_unregisteredVhutUsersFilterFields = fields;
			}
			else
			{
				_unregisteredVhutUsersFilterFields = FILTER_FIELD_UNREGISTERED_VHUT_USERS;
			}

			_unregisteredVhutUsers.refresh();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearUnregisteredVhutUsersFilter():void
		{
			setUnregisteredVhutUsersfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get unregisteredVhutUsersFilterFields():Array
		{
			return _unregisteredVhutUsersFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get unregisteredVhutUsersFilterKeywords():Array
		{
			return _unregisteredVhutUsersFilterKeywords;
		}

		protected var _unregisteredVhutUsersFilterFields:Array = new Array();
		protected var _unregisteredVhutUsersFilterKeywords:Array = new Array();

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  UnregisteredTargetVhutUser
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/** 選択中ユーザ */
		public var targetUnregisteredVhutUser:VhutUser;

	}
}