/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;

	/**
	 * 選択に関するイベント.
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
	public class SelectEvent extends Event
	{
		public static const SELECT_VM:String = "selectVm";
		public static const SELECT_SG:String = "selectSg";
		public static const SELECT_LINK:String = "selectLink";

		public static function newSelectVmEvent(data:IVaElement = null):SelectEvent
		{
			var event:SelectEvent = new SelectEvent(SELECT_VM);
			event.data = data;
			return event;
		}

		public static function newSelectSgEvent(data:IVaElement = null):SelectEvent
		{
			var event:SelectEvent = new SelectEvent(SELECT_SG);
			event.data = data;
			return event;
		}

		public static function newSelectLinkEvent(data:VaLink = null):SelectEvent
		{
			var event:SelectEvent = new SelectEvent(SELECT_LINK);
			event.data = data;
			return event;
		}

		public function SelectEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:Object;

		override public function clone():Event
		{
			var event:SelectEvent = new SelectEvent(type, bubbles, cancelable);
			event.data = data;
			return event;
		}
	}
}