/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.ai
{
	import flash.events.Event;
	
	/**
	 * VM関連のサーバアクセスイベント.
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
	public class AiVmEvent extends Event
	{
		public static const START_AI_VM:String = "startAiVm";
		public static const STOP_AI_VM:String = "stopAiVm";
		
		/**
		 * VMの起動イベントを作成します.
		 * @param vmId VMのID
		 * @return イベント
		 */
		public static function newStartAiVmEvent(aiVmId:Number):AiVmEvent
		{
			var event:AiVmEvent = new AiVmEvent(START_AI_VM);
			event._id = aiVmId;
			return event;
		}
		
		/**
		 * VMの停止イベントを作成します.
		 * @param vmId VMのID
		 * @return イベント
		 */
		public static function newStopAiVmEvent(aiVmId:Number):AiVmEvent
		{
			var event:AiVmEvent = new AiVmEvent(STOP_AI_VM);
			event._id = aiVmId;
			return event;
		}
		
		/**
		 * @private
		 */
		public function AiVmEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _id:Number;
		
		public function get id():Number
		{
			return _id;
		}
		
		override public function clone():Event
		{
			var event:AiVmEvent = new AiVmEvent(type, bubbles, cancelable);
			event._id = _id;
			return event;
		}
		
		
	}
}