/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.core.BitArray;
	import jp.co.ntts.vhut.core.domain.RightGroup;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.VhutUser")]
	/**
	 * VhutUser Entity Class.
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
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
    public class VhutUser extends VhutUserBase implements IRightOwner
	{
		public static const CHANGE_FULL_NAME:String = "changeFullName";
		public static const CHANGE_ROLE:String = "changeRole";

		public function VhutUser()
		{
			super();
			_rightGroups = RightGroup.createRightGroups();
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		////////////////////////////////////////////////////////////////////
		//
		//  LocalAttiributes for displary
		//
		////////////////////////////////////////////////////////////////////

		override public function set firstName(value:String):void {
			_firstName = value;
			dispatchEvent(new Event(CHANGE_FULL_NAME));
		}

		override public function set lastName(value:String):void {
			_lastName = value;
			dispatchEvent(new Event(CHANGE_FULL_NAME));
		}

		override public function set vhutUserRoleMapList(value:ArrayCollection):void {
			_vhutUserRoleMapList = value;
			updateRights();
			dispatchEvent(new Event(CHANGE_ROLE));
		}

		[Transient]
		[Bindable("changeFullName")]
		public function get fullName():String
		{
			return lastName + " " + firstName;
		}

		[Transient]
		[Bindable("changeRole")]
		public function get roleNames():String
		{
			var ary:Array = new Array();
			for each (var map:VhutUserRoleMap in vhutUserRoleMapList)
			{
				ary.push(map.role.name);
			}
			return ary.join(", ");
		}

		////////////////////////////////////////////////////////////////////
		//
		//  LocalAttiributes to edit.
		//
		////////////////////////////////////////////////////////////////////

		[Transient]
		public function set status(value:VhutUserStatus):void
		{
			_status = value;
		}
		public function get status():VhutUserStatus
		{
			return _status;
		}
		private var _status:VhutUserStatus = VhutUserStatus.NONE;


		[Bindable("changeRole")]
		public function get roles():IList
		{
			return _roles;
		}
		private var _roles:ArrayCollection;

		public function setRoles(items:IList):void
		{
			_roles = new ArrayCollection();
			for each(var item:Role in items)
			{
				_roles.addItem(new RoleItem(this, item));
			}
			dispatchEvent(new Event(CHANGE_ROLE));
		}

		////////////////////////////////////////////////////////////////////
		//
		//  Rights.
		//
		////////////////////////////////////////////////////////////////////

		[Transient]
		public function get rights():ByteArray
		{
			return _rights;
		}
		private var _rights:ByteArray;

		[Transient]
		public function get rightBits():BitArray
		{
			return _rightBits;
		}
		private var _rightBits:BitArray;

		[Transient]
		public function get rightsMap():Dictionary
		{
			return _rightsMap;
		}
		private var _rightsMap:Dictionary;

		[Transient]
		public function get rightGroups():IList
		{
			return _rightGroups;
		}
		private var _rightGroups:IList;

		private function updateRights():void
		{
			_rightBits = createRightBits(vhutUserRoleMapList);
			_rights = _rightBits.toByteArray();
			_rightsMap = createRightsMap(_rightBits);
			RightGroup.updateRightGroup(_rightGroups, _rightBits);
		}

		private function createRightBits(maps:IList):BitArray
		{
			var rights:BitArray = new BitArray();
			var role:Role;
			for each(var map:VhutUserRoleMap in maps)
			{
				role = map.role;
				rights = rights.or(role.rightBits);
			}
			return rights;
		}

		private function createRightsMap(bits:BitArray):Dictionary
		{
			var rightsMap:Dictionary = new Dictionary();
			for (var i:uint=0; i<Right.constants.length; i++)
			{
				var right:Right = Right.constants[i] as Right;
				var value:Boolean = bits.getAt(i);
				if (value)
					rightsMap[right] = value;
				else
					rightsMap[right] = false;
			}
			return rightsMap;
		}
    }
}
import jp.co.ntts.vhut.entity.Role;
import jp.co.ntts.vhut.entity.VhutUser;
import jp.co.ntts.vhut.entity.VhutUserRoleMap;

import mx.collections.ArrayCollection;

class RoleItem
{
	public function RoleItem(user:VhutUser, role:Role)
	{
		_user = user;
		_role = role;
	}

	private var _user:VhutUser;
	private var _role:Role;

	public function get name():String
	{
		return _role.name;
	}

	[Bindable]
	public function set enable(value:Boolean):void
	{
		var map:VhutUserRoleMap;
		if(_user.vhutUserRoleMapList == null)
		{
			_user.vhutUserRoleMapList = new ArrayCollection();
		}
		if(value && !enable)
		{
			map = new VhutUserRoleMap();
			map.vhutUser = _user;
			map.vhutUserId = _user.id;
			map.role = _role;
			map.roleId = _role.id;
			_user.vhutUserRoleMapList.addItem(map);
		}
		else
		{
			for(var i:int=0; i<_user.vhutUserRoleMapList.length; i++)
			{
				map = _user.vhutUserRoleMapList.getItemAt(i) as VhutUserRoleMap;
				if(map.roleId == _role.id)
				{
					_user.vhutUserRoleMapList.removeItemAt(i);
					break;
				}
			}
		}
	}
	public function get enable():Boolean
	{
		var result:Boolean = false;
		if(_user.vhutUserRoleMapList)
		{
			for each(var map:VhutUserRoleMap in _user.vhutUserRoleMapList)
			{
				if(map.roleId == _role.id)
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}
}