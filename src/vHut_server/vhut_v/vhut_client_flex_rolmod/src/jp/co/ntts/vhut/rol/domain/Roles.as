/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.rol.domain
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.Role;
	import jp.co.ntts.vhut.rol.RolEvent;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(type="jp.co.ntts.vhut.core.GetAllEvent", name="getAllRole")]
	[Event(type="jp.co.ntts.vhut.core.GetByIdEvent", name="getRolById")]
	[Event(type="jp.co.ntts.vhut.rol.RolEvent", name="deleteRol")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents("getAllRole, getRolById, deleteRol")]
	/**
	 * ロールの管理クラス.
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
	public class Roles extends EventDispatcher
	{

		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_ROLES:Array = ["name"];

		public function Roles(target:IEventDispatcher=null)
		{
			super(target);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Roles
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/** ロールのリスト */
		public function set roles(value:IList):void
		{
			_roles = new ArrayCollection(value.toArray());
			_roles.filterFunction = rolesFilter;
			_roles.refresh();
			updateTargetRole(true);
		}
		public function get roles():IList
		{
			return _roles;
		}
		private var _roles:ArrayCollection = new ArrayCollection();

		/** ロールのリストの再取得. */
		public function updateRoles():void
		{
			dispatchEvent(GetAllEvent.newGetAllRoleEvent());
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter Roles
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setRolesfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_rolesFilterKeywords = keywords;
			}
			else
			{
				_rolesFilterKeywords = new Array();
			}

			if(fields)
			{
				_rolesFilterFields = fields;
			}
			else
			{
				_rolesFilterFields = FILTER_FIELD_ROLES;
			}

			_roles.refresh();

			updateTargetRole();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearRolesFilter():void
		{
			setRolesfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get rolesFilterFields():Array
		{
			return _rolesFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get rolesFilterKeywords():Array
		{
			return _rolesFilterKeywords;
		}

		protected var _rolesFilterFields:Array = new Array();
		protected var _rolesFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function rolesFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, rolesFilterFields, rolesFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetRole
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("targetRoleChanged")]
		/** 選択中ロール */
		public function set targetRole(value:Role):void
		{
			if(_targetRole == value)
				return;

			isTargetRoleLast = false;
			_targetRole = value;
			updateRoles();
			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		public function get targetRole():Role
		{
			return _targetRole;
		}
		private var _targetRole:Role;

		[Bindable]
		/** 選択中のロールが詳細まで取得できている. */
		public function set isTargetRoleLast(value:Boolean):void
		{
			if(_isTargetRoleLast == value)
				return;

			_isTargetRoleLast = value;
			if(_isTargetRoleLast)
			{
				dispatchEvent(new Event("targetRoleChanged"));
			}
			dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetRole));
		}
		public function get isTargetRoleLast():Boolean
		{
			return _isTargetRoleLast;
		}
		private var _isTargetRoleLast:Boolean = false;

		/**
		 * 選択中のベーステンプレートを更新する.
		 *
		 * @param force 既に最新化していても再取得する.
		 */
		public function updateTargetRole(force:Boolean = false):void
		{
			if(_targetRole)
			{
				_targetRole = getRoleById(_targetRole.id);
			}

			if(_targetRole)
			{
				if(force || !isTargetRoleLast)
					isTargetRoleLast = false;
					dispatchEvent(GetByIdEvent.newGetRol(_targetRole.id));
			}
			else
			{
				isTargetRoleLast = false;
				_targetRole = null;
			}
			dispatchEvent(new Event("targetRoleChanged"));
		}

		/**
		 * ID指定でロールを取得します.
		 * @param id ID
		 * @return ロール
		 */
		public function getRoleById(id:Number):Role
		{
			if(roles)
			{
				for each (var role:Role in roles)
				{
					if(role.id == id)
						return role;
				}
			}
			return null;
		}

		public function deleteTargetRole():void
		{
			dispatchEvent(RolEvent.newDeleteRolEvent(targetRole));
		}
	}
}