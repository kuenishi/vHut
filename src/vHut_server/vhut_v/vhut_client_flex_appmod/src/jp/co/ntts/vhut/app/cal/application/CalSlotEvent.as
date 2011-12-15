/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.cal.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.app.cal.domain.CalSlot;

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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class CalSlotEvent extends Event
	{
		public static const CAL_MOUSE_DOWN:String = "calMouseDown";
		public static const CAL_MOUSE_UP:String = "calMouseUp";

		public static function newMouseDownEvent(data:CalSlot):CalSlotEvent
		{
			var event:CalSlotEvent = new CalSlotEvent(CAL_MOUSE_DOWN);
			event.data = data;
			return event;
		}

		public static function newMouseUpEvent(data:CalSlot):CalSlotEvent
		{
			var event:CalSlotEvent = new CalSlotEvent(CAL_MOUSE_UP);
			event.data = data;
			return event;
		}

		public function CalSlotEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:CalSlot;

		override public function clone():Event
		{
			var event:CalSlotEvent = new CalSlotEvent(type, bubbles, cancelable);
			event.data = data;
			return event;

		}


	}
}