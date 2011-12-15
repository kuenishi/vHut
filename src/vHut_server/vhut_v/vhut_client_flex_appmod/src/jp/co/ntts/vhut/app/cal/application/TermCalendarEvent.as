/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.cal.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.app.cal.domain.CalSlot;

	import mx.collections.IList;

	/**
	 * 期間予約カレンダに関するイベント
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
	public class TermCalendarEvent extends Event
	{
		public static const CHANGED:String = "reservingTermChanged";

		public static function newReservingTermChangedEvent(data:IList):TermCalendarEvent
		{
			var event:TermCalendarEvent = new TermCalendarEvent(CHANGED);
			event.data = data;
			return event;
		}

		public function TermCalendarEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:IList;

		override public function clone():Event
		{
			var event:TermCalendarEvent = new TermCalendarEvent(type, bubbles, cancelable);
			event.data = data;
			return event;

		}


	}
}