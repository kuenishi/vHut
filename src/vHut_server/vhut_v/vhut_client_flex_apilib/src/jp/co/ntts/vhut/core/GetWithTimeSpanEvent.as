/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	/**
	 * IDと期間指定の取得系サーバアクセスイベント.
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
	public class GetWithTimeSpanEvent extends Event
	{
		public static const GET_PERFORMANCE_WITH_TIME_SPAN:String = "getPerformanceWithTimeSpan"
		
		public static function newGetPerformance(startTime:Date, endTime:Date):GetWithTimeSpanEvent
		{
			var event:GetWithTimeSpanEvent = new GetWithTimeSpanEvent(GET_PERFORMANCE_WITH_TIME_SPAN);
			event._startTime = startTime;
			event._endTime = endTime;
			return event;
		}
		
		public function GetWithTimeSpanEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _startTime:Date;
		
		private var _endTime:Date;
		
		public function get startTime():Date
		{
			return _startTime;
		}
		public function get endTime():Date
		{
			return _endTime;
		}
		
		override public function clone():Event
		{
			var event:GetWithTimeSpanEvent = new GetWithTimeSpanEvent(type, bubbles, cancelable);
			event._startTime = startTime;
			event._endTime = endTime;
			return event;
		}
	}
}