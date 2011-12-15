/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.VhutUser;
	
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
	public class UsrEvent extends Event
	{
		
		/** ユーザを作成します. */
		private static const CREATE_USR:String = "createUsr";
		
		/** ユーザを編集します. */
		private static const EDIT_USR:String = "editUsr";
		
		/** ユーザを削除します. */
		private static const DELETE_USR:String = "deleteUsr";
		
		/**
		 * ユーザを作成するイベントを作成します.
		 * @param item 作成したいユーザ
		 * @return イベント
		 */
		public static function newCreateUsrEvent(item:VhutUser):UsrEvent
		{
			var event:UsrEvent = new UsrEvent(CREATE_USR);
			event._vhutUser = item;
			return event;
		}
		
		/**
		 * ユーザを編集するイベントを作成します.
		 * @param item 編集したいユーザ
		 * @return イベント
		 */
		public static function newEditUsrEvent(item:VhutUser):UsrEvent
		{
			var event:UsrEvent = new UsrEvent(EDIT_USR);
			event._vhutUser = item;
			return event;
		}
		
		/**
		 * ユーザを削除するイベントを作成します.
		 * @param item 作成したいユーザ
		 * @return イベント
		 */
		public static function newDeleteUsrEvent(item:VhutUser):UsrEvent
		{
			var event:UsrEvent = new UsrEvent(DELETE_USR);
			event._vhutUser = item;
			return event;
		}
		
		public function UsrEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		/**
		 * ターゲットのユーザ.
		 */
		public function get vhutUser():VhutUser
		{
			return _vhutUser;
		}
		
		private var _vhutUser:VhutUser;
		
		override public function clone():Event
		{
			var event:UsrEvent = new UsrEvent(type, bubbles, cancelable);
			event._vhutUser = _vhutUser;
			return event;
		}
	}
}