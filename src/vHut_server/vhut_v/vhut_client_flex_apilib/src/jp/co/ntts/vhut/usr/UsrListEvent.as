/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.VhutUser;
	
	import mx.collections.IList;
	
	/**
	 * ユーザを一括操作するイベント
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
	public class UsrListEvent extends Event
	{
		
		/** ユーザを一括作成します. */
		private static const CREATE_USR_LIST:String = "createUsrList";
		
		/**
		 * ユーザを一括作成するイベントを作成します.
		 * @param items 作成したいユーザ
		 * @return イベント
		 */
		public static function newCreateUsrListEvent(items:IList):UsrListEvent
		{
			var event:UsrListEvent = new UsrListEvent(CREATE_USR_LIST);
			event._vhutUserList = items;
			return event;
		}
		
		public function UsrListEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		/**
		 * ターゲットのユーザ.
		 */
		public function get vhutUserList():IList
		{
			return _vhutUserList;
		}
		
		private var _vhutUserList:IList;
		
		override public function clone():Event
		{
			var event:UsrListEvent = new UsrListEvent(type, bubbles, cancelable);
			event._vhutUserList = _vhutUserList;
			return event;
		}
	}
}