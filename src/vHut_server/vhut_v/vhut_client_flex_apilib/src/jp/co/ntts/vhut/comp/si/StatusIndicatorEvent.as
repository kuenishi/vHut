/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.si
{
	import flash.events.Event;

	/**
	 * StatusIndicatorに関連するイベント
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
	public class StatusIndicatorEvent extends Event
	{
		/** アクションの実行 */
		public static const CHANGE_STATUS:String = "changeStatus";

		/**
		 * 変更イベントの生成
		 * @param from 変更後の状態
		 * @param to 変更前の状態
		 * @return イベント
		 *
		 */
		public static function newStatusIndicatorChangeStatusEvent(from:String, to:String):StatusIndicatorEvent
		{
			var event:StatusIndicatorEvent = new StatusIndicatorEvent(CHANGE_STATUS);
			event.from = from;
			event.to = to;
			return event;
		}

		public function StatusIndicatorEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		/** 変更前の状態 */
		public var from:String;

		/** 変更後の状態 */
		public var to:String;

		override public function clone():Event
		{
			var event:StatusIndicatorEvent = new StatusIndicatorEvent(type);
			event.from = from;
			event.to = to;
			return event;
		}
	}
}