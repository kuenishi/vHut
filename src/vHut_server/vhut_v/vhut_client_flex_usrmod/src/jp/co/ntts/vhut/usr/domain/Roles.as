/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr.domain
{
	import flash.events.EventDispatcher;
	
	import jp.co.ntts.vhut.core.GetAllEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="getAllRole", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[ManagedEvents(names="getAllRole")]
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
		
		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Roles
		//
		///////////////////////////////////////////////////////////////////////////////////////
		
		public function updateRoles():void
		{
			dispatchEvent(GetAllEvent.newGetAllRoleEvent());
		}
		
		[Bindable]
		/** ユーザのリスト */
		public function set roles(value:IList):void
		{
			_roles.removeAll();
			_roles.addAll(value);
		}
		public function get roles():IList
		{
			return _roles;
		}
		private var _roles:ArrayCollection = new ArrayCollection();
	}
}