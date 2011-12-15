/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.rol
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.entity.Role;
	
	/**
	 * ロール関連のイベント
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
	public class RolEvent extends Event
	{
		
		/** ロールを作成します. */
		private static const CREATE_ROL:String = "createRol";
		
		/** ロールを更新します. */
		private static const UPDATE_ROL:String = "updateRol";
		
		/** ロールを削除します. */
		private static const DELETE_ROL:String = "deleteRol";
		
		/**
		 * ロールを作成するイベントを作成します.
		 * @param item 作成したいロール
		 * @return イベント
		 */
		public static function newCreateRolEvent(item:Role):RolEvent
		{
			var event:RolEvent = new RolEvent(CREATE_ROL);
			event._role = item;
			return event;
		}
		
		/**
		 * ロールを編集するイベントを作成します.
		 * @param item 編集したいロール
		 * @return イベント
		 */
		public static function newUpdateRolEvent(item:Role):RolEvent
		{
			var event:RolEvent = new RolEvent(UPDATE_ROL);
			event._role = item;
			return event;
		}
		
		/**
		 * ロールを削除するイベントを作成します.
		 * @param item 作成したいロール
		 * @return イベント
		 */
		public static function newDeleteRolEvent(item:Role):RolEvent
		{
			var event:RolEvent = new RolEvent(DELETE_ROL);
			event._role = item;
			return event;
		}
		
		public function RolEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		/**
		 * ターゲットのロール.
		 */
		public function get role():Role
		{
			return _role;
		}
		
		private var _role:Role;
		
		override public function clone():Event
		{
			var event:RolEvent = new RolEvent(type, bubbles, cancelable);
			event._role = _role;
			return event;
		}
	}
}