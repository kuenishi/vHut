/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.infrastructure
{
	import flash.events.Event;

	/**
	 * VMの描画クラスのイベント
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
	public class VmItemRendererEvent extends Event
	{
		/** VMを開始します */
		public static const START_VM:String = "startVm";
		/** VMを終了します */
		public static const STOP_VM:String = "stopVm";
		/** VMを削除します */
		public static const REMOVE_VM:String = "removeVm";

		public static function newStartVmVmitemRendererEvent(data:Object):VmItemRendererEvent
		{
			var event:VmItemRendererEvent = new VmItemRendererEvent(START_VM);
			event.data = data;
			return event;
		}

		public static function newStopVmVmitemRendererEvent(data:Object):VmItemRendererEvent
		{
			var event:VmItemRendererEvent = new VmItemRendererEvent(STOP_VM);
			event.data = data;
			return event;
		}

		public static function newRemoveVmVmitemRendererEvent(data:Object):VmItemRendererEvent
		{
			var event:VmItemRendererEvent = new VmItemRendererEvent(REMOVE_VM);
			event.data = data;
			return event;
		}

		public function VmItemRendererEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:Object;

		override public function clone():Event
		{
			var result:VmItemRendererEvent = new VmItemRendererEvent(type, bubbles, cancelable);
			result.data = data
			return result;
		}
	}
}