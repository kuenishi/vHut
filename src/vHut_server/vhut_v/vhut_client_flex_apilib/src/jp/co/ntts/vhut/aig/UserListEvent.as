/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	
	/**
	 * アプリケーション関連の更新系サーバアクセスイベント.
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
	public class UserListEvent extends Event
	{
		/** 複数のユーザを一括で検証します. */
		private static const VALIDATE_USER_LIST:String = "validateUserList";
		
		/**
		 * アプリケーションインスタンスグループを選択したというイベントを作成します.
		 * @param item 選択したアプリケーションインスタンスグループ
		 * @return イベント
		 */
		public static function newValidateUserListEvent(itemList:Array):UserListEvent
		{
			var event:UserListEvent = new UserListEvent(VALIDATE_USER_LIST);
			event._userList = itemList;
			return event;
		}
		
		/**
		 * コンストラクタ.
		 * @param type
		 * @param bubbles
		 * @param cancelable
		 */
		public function UserListEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _userList:Array;
		
		/**
		 * ターゲットのアプリケーションインスタンスグループ.
		 */
		public function get userList():Array
		{
			return _userList;
		}
		
		override public function clone():Event
		{
			var event:UserListEvent = new UserListEvent(type, bubbles, cancelable);
			event._userList = _userList;
			return event;
		}
	}
}