/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.entity.Disk;

	/**
	 * エレメントを削除します.
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
	public class RemoveEvent extends Event
	{
		public static const REMOVE_VM:String = "removeVm";
		public static const REMOVE_SG:String = "removeSg";
		public static const REMOVE_LINK:String = "removeLink";
		public static const REMOVE_DISK:String = "removeDisk";

		public static function newRemoveVmEvent(data:Object):RemoveEvent
		{
			var event:RemoveEvent = new RemoveEvent(REMOVE_VM);
			event.data = data;
			return event;
		}

		public static function newRemoveSgEvent(data:Object):RemoveEvent
		{
			var event:RemoveEvent = new RemoveEvent(REMOVE_SG);
			event.data = data;
			return event;
		}

		public static function newRemoveLinkEvent(data:VaLink):RemoveEvent
		{
			var event:RemoveEvent = new RemoveEvent(REMOVE_LINK, true);
			event.data = data;
			return event;
		}

		public static function newRemoveDiskEvent(data:Disk):RemoveEvent
		{
			var event:RemoveEvent = new RemoveEvent(REMOVE_DISK, true);
			event.data = data;
			return event;
		}


		public function RemoveEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:Object;

		override public function clone():Event
		{
			var event:RemoveEvent = new RemoveEvent(type, bubbles, cancelable);
			event.data = data;
			return event;
		}
	}
}