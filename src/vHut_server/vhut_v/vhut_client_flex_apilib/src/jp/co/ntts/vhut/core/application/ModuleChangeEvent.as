/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.application
{
	import flash.events.Event;
	
	import mx.modules.Module;
	
	/**
	 * モジュール変更後のイベント
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
	public class ModuleChangeEvent extends Event
	{
		/** モジュール変更しました. */
		public static const MODULE_CHANGE:String = "moduleChange";
		
		/**
		 * 新しいモジュール変更イベントを作成します.
		 * @param oldModuleId 古いモジュールID
		 * @param newModuleId 新しいモジュールID
		 * @return イベント
		 */
		public static function newModuleChangeEvent(oldModuleId:String, newModuleId:String):ModuleChangeEvent
		{
			var event:ModuleChangeEvent = new ModuleChangeEvent(MODULE_CHANGE);
			event.oldModuleId = oldModuleId;
			event.newModuleId = newModuleId;
			return event;
		}
		
		public function ModuleChangeEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		/** 古いモジュールID */
		public var oldModuleId:String;
		
		/** 新しいモジュールID */
		public var newModuleId:String;
		
		/** 複製します. */
		override public function clone():Event
		{
			var event:Event = new ModuleChangeEvent(type, bubbles, cancelable);
			(event as ModuleChangeEvent).oldModuleId = oldModuleId;
			(event as ModuleChangeEvent).newModuleId = newModuleId;
			return event;
		}
	}
}