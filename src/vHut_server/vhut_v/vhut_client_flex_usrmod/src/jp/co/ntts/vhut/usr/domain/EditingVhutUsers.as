/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.entity.Role;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.entity.VhutUserCloudUserMap;
	import jp.co.ntts.vhut.entity.VhutUserRoleMap;
	import jp.co.ntts.vhut.usr.UsrEvent;
	import jp.co.ntts.vhut.usr.UsrListEvent;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.controls.List;
	import mx.utils.ObjectUtil;

	import org.spicefactory.lib.reflect.Property;

	[Event(name="createUsrList", type="jp.co.ntts.vhut.usr.UsrListEvent")]
	[ManagedEvents(names="createUsrList")]
	/**
	 * ユーザ追加用のドメインオブジェクト.
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
	public class EditingVhutUsers extends EventDispatcher
	{
		public static const CHANGE_TARGET_VHUT_USERS:String = "changeTargetVhutUsers";
		public static const CHANGE_TARGET_VHUT_USER:String = "changeTargetVhutUser";

		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_TARGET_VHUT_USERS:Array = ["fullName", "account"];

		public function EditingVhutUsers(target:IEventDispatcher=null)
		{
			super(target);
		}

		/////////////////////////////////////////////////////////////////////
		//
		// Main Operation
		//
		/////////////////////////////////////////////////////////////////////

		/**
		 * 変更をサーバに反映します.
		 */
		public function initialize():void
		{
			initializeTargetVhutUsers();
		}

		public function getToBeAddedUserList():IList
		{
			clearTargetVhutUsersFilter();
			var resultVhutUsers:IList = new ArrayCollection();
			if(!targetVhutUsers)
				return resultVhutUsers;

			for each (var remoteVhutUser:VhutUser in targetVhutUsers)
			{
				var resultVhutUser:VhutUser = new VhutUser();
				resultVhutUser.id = remoteVhutUser.id;
				resultVhutUser.account = remoteVhutUser.account;
				resultVhutUser.firstName = remoteVhutUser.firstName;
				resultVhutUser.lastName = remoteVhutUser.lastName;
				resultVhutUser.email = remoteVhutUser.email;
				resultVhutUser.imageUrl = remoteVhutUser.imageUrl;

				var cloudUserMapList:ArrayCollection = new ArrayCollection();
				for each (var remoteCloudUserMap:VhutUserCloudUserMap in  remoteVhutUser.vhutUserCloudUserMapList)
				{
					var cloudUserMap:VhutUserCloudUserMap = new VhutUserCloudUserMap();
					cloudUserMap.vhutUser = resultVhutUser;
					cloudUserMap.cloudId = remoteCloudUserMap.cloudId;
					cloudUserMap.cloudUserId = remoteCloudUserMap.cloudUserId;
					cloudUserMapList.addItem(cloudUserMap);
				}
				resultVhutUser.vhutUserCloudUserMapList = cloudUserMapList;

				var roleMapList:ArrayCollection = new ArrayCollection();
				for each (var remoteRoleMap:VhutUserRoleMap in  remoteVhutUser.vhutUserRoleMapList)
				{
					var roleMap:VhutUserRoleMap = new VhutUserRoleMap();
					roleMap.vhutUser = resultVhutUser;
					roleMap.roleId = remoteRoleMap.roleId;
					roleMap.role = remoteRoleMap.role;
					roleMapList.addItem(roleMap);
				}
				resultVhutUser.vhutUserRoleMapList = roleMapList;

				resultVhutUsers.addItem(resultVhutUser);
			}

			return resultVhutUsers;
		}

		/////////////////////////////////////////////////////////////////////
		//
		// Roles
		//
		/////////////////////////////////////////////////////////////////////

		[Inject]
		public function set roles(value:Roles):void
		{
			_roles = value;
			if(_rolesWatcher)
			{
				_rolesWatcher.unwatch();
				_rolesWatcher = null;
			}
			if(_roles)
			{
				_rolesWatcher = BindingUtils.bindSetter(
					function(value:Object):void
					{
						setRolesToTargetUsers();
					}, _roles, "roles");
			}
		}
		public function get roles():Roles
		{
			return _roles;
		}

		private function setRolesToTargetUsers():void
		{
			if(targetVhutUsers && roles)
			{
				for each (var user:VhutUser in targetVhutUsers)
				{
					user.setRoles(roles.roles);
				}
			}
		}

		private var _roles:Roles;
		private var _rolesWatcher:ChangeWatcher;


		/////////////////////////////////////////////////////////////////////
		//
		// TargetVhutUsers
		//
		/////////////////////////////////////////////////////////////////////

		[Bindable("changeTargetVhutUsers")]
		/**
		 * 追加対象のユーザリスト.
		 */
		public function set targetVhutUsers(value:IList):void
		{
			_targetVhutUsers.removeAll();
			_targetVhutUsers.addAll(value);
			_targetVhutUsers.refresh();
			setRolesToTargetUsers();
			selectFirstVhutUser();
			dispatchEvent(new Event(CHANGE_TARGET_VHUT_USERS));
		}
		public function get targetVhutUsers():IList
		{
			return _targetVhutUsers;
		}
		private var _targetVhutUsers:ArrayCollection = new ArrayCollection();

		public function initializeTargetVhutUsers():void
		{
			_targetVhutUsers = new ArrayCollection();
			_targetVhutUsers.filterFunction = targetVhutUsersFilter;
		}

		public function addTargetVhutUsers(value:Vector.<Object>):void
		{
			clearTargetVhutUsersFilter();
			for each(var item:VhutUser in value)
			{
				_targetVhutUsers.addItem(item);
			}
			selectFirstVhutUser();
			dispatchEvent(new Event(CHANGE_TARGET_VHUT_USERS));
		}

		public function removeTargetVhutUsers(value:Vector.<Object>):void
		{
			for each(var item:VhutUser in value)
			{
				var length:int = _targetVhutUsers.length;
				for(var i:int = 0; i<length; i++)
				{
					var user:VhutUser = _targetVhutUsers[i] as VhutUser;
					if(user.id == item.id)
					{
						_targetVhutUsers.removeItemAt(i);
						break;
					}
				}
			}
			selectFirstVhutUser();
			dispatchEvent(new Event(CHANGE_TARGET_VHUT_USERS));
		}


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter TargetVhutUsers
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setTargetVhutUsersfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_targetVhutUsersFilterKeywords = keywords;
			}
			else
			{
				_targetVhutUsersFilterKeywords = new Array();
			}

			if(fields)
			{
				_targetVhutUsersFilterFields = fields;
			}
			else
			{
				_targetVhutUsersFilterFields = FILTER_FIELD_TARGET_VHUT_USERS;
			}

			_targetVhutUsers.refresh();

			updateTargetVhutUser();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearTargetVhutUsersFilter():void
		{
			setTargetVhutUsersfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get targetVhutUsersFilterFields():Array
		{
			return _targetVhutUsersFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get targetVhutUsersFilterKeywords():Array
		{
			return _targetVhutUsersFilterKeywords;
		}

		protected var _targetVhutUsersFilterFields:Array = new Array();
		protected var _targetVhutUsersFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function targetVhutUsersFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, targetVhutUsersFilterFields, targetVhutUsersFilterKeywords);
		}

		/////////////////////////////////////////////////////////////////////
		//
		// TargetUser
		//
		/////////////////////////////////////////////////////////////////////

		[Bindable("targetVhutUserChanged")]
		/** 編集対象 */
		public function set targetVhutUser(value:VhutUser):void
		{
			if(_targetVhutUser == value)
				return;

			_targetVhutUser = value;
			updateTargetVhutUser();
		}
		public function get targetVhutUser():VhutUser
		{
			return _targetVhutUser;
		}
		protected var _targetVhutUser:VhutUser;

		public function selectFirstVhutUser():void
		{
			if (targetVhutUsers.length > 0)
			{
				targetVhutUser = targetVhutUsers.getItemAt(0) as VhutUser;
			} else {
				targetVhutUser = null;
			}
		}

		protected function updateTargetVhutUser():void
		{
			if(_targetVhutUser && _targetVhutUsers.getItemIndex(_targetVhutUser) < 0)
			{
				_targetVhutUser = null;
			}

			dispatchEvent(new Event("targetVhutUserChanged"));
		}
	}
}