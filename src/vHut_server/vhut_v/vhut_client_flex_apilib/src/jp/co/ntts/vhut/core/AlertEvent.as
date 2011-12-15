/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.core
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.util.MessageUtil;
	
	import mx.logging.Log;
	import mx.logging.LogEventLevel;
	
	/**
	 * アラートを生成するイベント.
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
	public class AlertEvent extends Event
	{
		private static const DEBUG:String = "debug";
		private static const INFO:String = "info";
		private static const WARN:String = "warn";
		private static const ERROR:String = "error";
		private static const FATAL:String = "fatal";
		
		public static function newAlertEvent(messageCode:String, ... rest):AlertEvent
		{
			var type:String = ERROR;
			switch(MessageUtil.getLogEventLevel(messageCode))
			{
				case LogEventLevel.DEBUG:
					type = DEBUG;
					break;
				case LogEventLevel.INFO:
					type = INFO;
					break;
				case LogEventLevel.WARN:
					type = WARN;
					break;
				case LogEventLevel.ERROR:
					type = ERROR;
					break;
				case LogEventLevel.FATAL:
					type = FATAL;
					break;
			}
			
			var event:AlertEvent = new AlertEvent(type);
			event._text = MessageUtil.getMessage.apply(MessageUtil.getMessage, [messageCode].concat(rest));
			return event;
		}
		
		public function AlertEvent(type:String){
			super(type);
		}
		
		private var _text:String;
		
		public function get text():String
		{
			return _text;
		}
	}
}