/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.log.infrastructure
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.log.LogData;

	/**
	 * ログビューアーに関連するイベント.
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
	public class LogViewerEvent extends Event
	{
		private static const ADDING:String="adding"
		
		private static const ADDED:String="added"
		
		public static function newAddingLogViewerEvent(logData:LogData = null):LogViewerEvent
		{
			var event:LogViewerEvent=new LogViewerEvent(ADDING);
			event._newLogData = logData;
			return event;
		}
		
		public static function newAddedLogViewerEvent(logData:LogData = null):LogViewerEvent
		{
			var event:LogViewerEvent=new LogViewerEvent(ADDED);
			event._newLogData = logData;
			return event;
		}

		public function LogViewerEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		protected var _newLogData:LogData;
		
		public function get newLogData():LogData
		{
			return _newLogData;
		}
		
		/**
		 * イベントを複製する. 
		 * @return 
		 * 
		 */
		override public function clone():Event
		{
			var event:LogViewerEvent=new LogViewerEvent(type);
			event._newLogData = newLogData;
			return event;
		}
	}
}