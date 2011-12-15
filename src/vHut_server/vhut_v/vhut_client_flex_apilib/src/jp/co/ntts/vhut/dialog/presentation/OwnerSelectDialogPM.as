/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.dialog.presentation
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	/**
	 * 所有者選択ダイアログのPM
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class OwnerSelectDialogPM extends EventDispatcher
	{
		public static const USER_LIST_FILTER_FIELDS:Array = ["account", "firstName", "lastName"];

		public function OwnerSelectDialogPM(target:IEventDispatcher=null)
		{
			super(target);
			_userList.filterFunction = userListFilterFunction;
		}

		[Bindable("targetUserChanged")]
		public function set targetUser(value:VhutUser):void
		{
			_targetUser = value;
		}

		public function get targetUser():VhutUser
		{
			return _targetUser;
		}
		private var _targetUser:VhutUser;

		protected function updateTargetUser():void
		{
			if (_userList.getItemIndex(_targetUser) < 0)
			{
				_targetUser = null;
			}
		}

		[Bindable]
		public function set userList(value:IList):void
		{
			_userList.removeAll();
			_userList.addAll(value);
			updateTargetUser();
			dispatchEvent(new Event("targetUserChanged"));
		}
		public function get userList():IList
		{
			return _userList;
		}
		private var _userList:ArrayCollection = new ArrayCollection();

		protected function userListFilterFunction(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, USER_LIST_FILTER_FIELDS, userListSearchKeywords);
		}

		public function searchUserHandler(event:SearchEvent):void
		{
			userListSearchKeywords = event.keywords;
			_userList.refresh();
		}

		protected var userListSearchKeywords:Array = new Array();
	}
}