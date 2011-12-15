package jp.co.ntts.vhut.usr.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.usr.UsrEvent;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="getAllUser", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getUsrById", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getAppListByUserId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getAigListByUserId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getAiListByUserId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="deleteUsr", type="jp.co.ntts.vhut.usr.UsrEvent")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents(names="getAllUser, getUsrById, getAppListByUserId, getAigListByUserId, getAiListByUserId, deleteUsr")]
	/**
	 * ユーザの管理クラス.
	 * <p></p>
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
	 * $Date:$
	 * $Revision:$
	 * $Author:$
	 */
	public class VhutUsers extends EventDispatcher
	{

		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_VHUT_USERS:Array = ["fullName", "account"];

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  VhutUsers
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/** ユーザのリスト */
		public function set vhutUsers(value:IList):void
		{
			_vhutUsers = new ArrayCollection(value.toArray());
			_vhutUsers.filterFunction = vhutUsersFilter;
			_vhutUsers.refresh();
			updateTargetVhutUser(true);
		}
		public function get vhutUsers():IList
		{
			return _vhutUsers;
		}
		private var _vhutUsers:ArrayCollection = new ArrayCollection();

		/** ユーザのリストを再取得する. */
		public function updateVhutUsers():void
		{
			dispatchEvent(GetAllEvent.newGetAllUserEvent());
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter VhutUsers
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setVhutUsersfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_vhutUsersFilterKeywords = keywords;
			}
			else
			{
				_vhutUsersFilterKeywords = new Array();
			}

			if(fields)
			{
				_vhutUsersFilterFields = fields;
			}
			else
			{
				_vhutUsersFilterFields = FILTER_FIELD_VHUT_USERS;
			}

			_vhutUsers.refresh();

			updateTargetVhutUser();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearVhutUsersFilter():void
		{
			setVhutUsersfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get vhutUsersFilterFields():Array
		{
			return _vhutUsersFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get vhutUsersFilterKeywords():Array
		{
			return _vhutUsersFilterKeywords;
		}

		protected var _vhutUsersFilterFields:Array = new Array();
		protected var _vhutUsersFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function vhutUsersFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, vhutUsersFilterFields, vhutUsersFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetVhutUser
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("targetVhutUserChanged")]
		/** 選択中ユーザ */
		public function set targetVhutUser(value:VhutUser):void
		{
			if(_targetVhutUser == value)
				return;

			isTargetVhutUserLast = false;
			_targetVhutUser = value;
			updateTargetVhutUser();
			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		public function get targetVhutUser():VhutUser
		{
			return _targetVhutUser;
		}
		private var _targetVhutUser:VhutUser;

		[Bindable]
		/** 選択中のユーザが詳細まで取得できている. */
		public function set isTargetVhutUserLast(value:Boolean):void
		{
			if(_isTargetVhutUserLast == value)
				return;

			_isTargetVhutUserLast = value;
			if(_isTargetVhutUserLast)
			{
				dispatchEvent(new Event("targetVhutUserChanged"));
			}
			dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetVhutUser));
		}
		public function get isTargetVhutUserLast():Boolean
		{
			return _isTargetVhutUserLast;
		}
		private var _isTargetVhutUserLast:Boolean = false;

		/**
		 * 選択中のユーザを更新する.
		 *
		 * @param force 既に最新化していても再取得する.
		 */
		public function updateTargetVhutUser(force:Boolean = false):void
		{
			if(_targetVhutUser)
			{
				_targetVhutUser = getVhutUserById(_targetVhutUser.id);
			}

			if(_targetVhutUser)
			{
				if(force || !isTargetVhutUserLast)
				{
					isTargetVhutUserLast = false;
					dispatchEvent(GetByIdEvent.newGetUsr(_targetVhutUser.id));

					targetAppList=null;
					dispatchEvent(GetByIdEvent.newGetApplicationListByUser(_targetVhutUser.id));

					targetAigList=null;
					dispatchEvent(GetByIdEvent.newGetAigListByUsr(_targetVhutUser.id));

					targetAiList=null;
					dispatchEvent(GetByIdEvent.newGetAiListByUsr(_targetVhutUser.id));
				}
			}
			else
			{
				isTargetVhutUserLast = false;
				_targetVhutUser = null;
			}
			dispatchEvent(new Event("targetVhutUserChanged"));
		}

		/**
		 * ID指定でユーザを取得します.
		 * @param id ID
		 * @return ユーザ
		 */
		public function getVhutUserById(id:Number):VhutUser
		{
			if(vhutUsers)
			{
				for each (var vhutUser:VhutUser in vhutUsers)
				{
					if(vhutUser.id == id)
						return vhutUser;
				}
			}
			return null;
		}

		/** 選択中のユーザを削除する. */
		public function deleteTargetVhutUser():void
		{
			dispatchEvent(UsrEvent.newDeleteUsrEvent(targetVhutUser));
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetVhutUser's Application
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		public var targetAppList:IList;

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetVhutUser's ApplicationInstanceGroup
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		public var targetAigList:IList;

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetVhutUser's ApplicationInstance
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		public var targetAiList:IList;
	}
}