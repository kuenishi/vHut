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
	import jp.co.ntts.vhut.usr.UsrEvent;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.IList;
	import mx.utils.ObjectUtil;

	import org.spicefactory.lib.reflect.Property;

	[Event(name="editUsr", type="jp.co.ntts.vhut.usr.UsrEvent")]
	[ManagedEvents(names="editUsr")]
	/**
	 * ユーザ編集用のドメインオブジェクト.
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
	public class EditingVhutUser extends EventDispatcher
	{
		public static const UPDATE_TARGET_VHUT_USER:String = "updateTargetVhutUser";

		public function EditingVhutUser(target:IEventDispatcher=null)
		{
			super(target);
		}

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
						if(_targetVhutUser)
						{
							_targetVhutUser.setRoles(value as IList);
						}
					}, _roles, "roles");
			}
		}
		public function get roles():Roles
		{
			return _roles;
		}

		private var _roles:Roles;
		private var _rolesWatcher:ChangeWatcher;

		/**
		 * 登録済みユーザを編集対象として設定します.
		 * @param source
		 */
		public function setRegisteredUsr(source:VhutUser):void
		{
			_targetVhutUser = ObjectUtil.copy(source) as VhutUser;
			roles.updateRoles();
			dispatchEvent(new Event(UPDATE_TARGET_VHUT_USER));
		}

		/** 編集対象 */
		[Bindable("updateTargetVhutUser")]
		public function get targetVhutUser():VhutUser
		{
			return _targetVhutUser;
		}
		private var _targetVhutUser:VhutUser;

//		/**
//		 * 変更をサーバに反映します.
//		 */
//		public function save():void
//		{
//			if(targetVhutUser)
//				dispatchEvent(UsrEvent.newEditUsrEvent(targetVhutUser));
//		}
	}
}