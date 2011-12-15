/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.application
{
	import flash.events.Event;

	import flashx.textLayout.factory.TruncationOptions;

	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.dto.AdditionalDiskDto;
	import jp.co.ntts.vhut.entity.Disk;

	import org.spicefactory.lib.reflect.mapping.ValidationError;

	/**
	 * エレメントを追加します.
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
	public class AddEvent extends Event
	{
		public static const ADD_VM:String = "addVm";
		public static const ADD_SG:String = "addSg";;
		public static const ADD_LINK:String = "addLink";
		public static const ADD_DISK:String = "addDisk";

		public static function newAddVmEvent(data:Object, posx:Number, posy:Number):AddEvent
		{
			var event:AddEvent = new AddEvent(ADD_VM);
			event.data = data;
			event.posx = posx;
			event.posy = posy;
			return event;
		}

		public static function newAddSgEvent(data:Object, posx:Number, posy:Number):AddEvent
		{
			var event:AddEvent = new AddEvent(ADD_SG);
			event.data = data;
			event.posx = posx;
			event.posy = posy;
			return event;
		}

		public static function newAddLinkEvent(data:VaLink):AddEvent
		{
			var event:AddEvent = new AddEvent(ADD_LINK);
			event.data = data;
			return event;
		}

		public static function newAddDiskEvent(data:Disk):AddEvent
		{
			var event:AddEvent = new AddEvent(ADD_DISK, true);
			event.data = data;
			return event;
		}


		public function AddEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:Object;

		public var posx:Number;

		public var posy:Number;

		override public function clone():Event
		{
			var event:AddEvent = new AddEvent(type, bubbles, cancelable);
			event.data = data;
			event.posx = posx;
			event.posy = posy;
			return event;
		}
	}
}