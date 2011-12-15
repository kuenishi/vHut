/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.va.domain.VaLink;

	/**
	 * エレメントを移動します.
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
	 * $Date: 2010-11-08 14:13:49 +0900 (月, 08 11 2010) $
	 * $Revision: 568 $
	 * $Author: NTT Software Corporation. $
	 */
	public class UpdateEvent extends Event
	{
		public static const UPDATE_VM:String = "updateVm";
		public static const UPDATE_SG:String = "updateSg";
		public static const UPDATE_LINK:String = "updateLink";

		public static function newUpdateVmEvent(data:Object, posx:Number, posy:Number):UpdateEvent
		{
			var event:UpdateEvent = new UpdateEvent(UPDATE_VM);
			event.data = data;
			event.posx = posx;
			event.posy = posy;
			return event;
		}

		public static function newUpdateSgEvent(data:Object, posx:Number, posy:Number):UpdateEvent
		{
			var event:UpdateEvent = new UpdateEvent(UPDATE_SG);
			event.data = data;
			event.posx = posx;
			event.posy = posy;
			return event;
		}

		public static function newUpdateLinkEvent(data:VaLink):UpdateEvent
		{
			var event:UpdateEvent = new UpdateEvent(UPDATE_LINK);
			event.data = data;
			return event;
		}


		public function UpdateEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:Object;

		public var posx:Number;

		public var posy:Number;

		override public function clone():Event
		{
			var event:UpdateEvent = new UpdateEvent(type, bubbles, cancelable);
			event.data = data;
			event.posx = posx;
			event.posy = posy;
			return event;
		}
	}
}