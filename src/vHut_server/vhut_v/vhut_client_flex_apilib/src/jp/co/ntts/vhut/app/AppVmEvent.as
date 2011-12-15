/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app
{
	import flash.events.Event;
	
	/**
	 * VM関連のサーバアクセスイベント.
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
	public class AppVmEvent extends Event
	{
		public static const START_APP_VM:String = "startAppVm";
		public static const STOP_APP_VM:String = "stopAppVm";
		
		/**
		 * VMの起動イベントを作成します.
		 * @param vmId VMのID
		 * @return イベント
		 */
		public static function newStartAppVmEvent(appVmId:Number):AppVmEvent
		{
			var event:AppVmEvent = new AppVmEvent(START_APP_VM);
			event._id = appVmId;
			return event;
		}
		
		/**
		 * VMの停止イベントを作成します.
		 * @param vmId VMのID
		 * @return イベント
		 */
		public static function newStopAppVmEvent(appVmId:Number):AppVmEvent
		{
			var event:AppVmEvent = new AppVmEvent(STOP_APP_VM);
			event._id = appVmId;
			return event;
		}
		
		/**
		 * @private
		 */
		public function AppVmEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _id:Number;
		
		public function get id():Number
		{
			return _id;
		}
		
		
	}
}