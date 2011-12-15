/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.core
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.Command;
	
	/**
	 * コマンド関連のサーバアクセスイベント.
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
	public class CommandEvent extends Event
	{
		private static const RETRY_COMMAND:String="retryCommand";
		private static const CANCEL_COMMAND:String="cancelCommand";
		
		/**
		 * コマンドをキャンセルさせるイベントを作ります.
		 * @param command 対象のコマンド
		 * @return イベント
		 */
		public static function newCancelCommandEvent(command:Command):CommandEvent
		{
			var event:CommandEvent = new CommandEvent(CANCEL_COMMAND);
			event._command = command;
			return event;
		}
		
		/**
		 * コマンドを再実行させるイベントを作ります.
		 * @param command 対象のコマンド
		 * @return イベント
		 */
		public static function newRetryCommandEvent(command:Command):CommandEvent
		{
			var event:CommandEvent = new CommandEvent(RETRY_COMMAND);
			event._command = command;
			return event;
		}
		
		public function CommandEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _command:Command;
		
		public function get command():Command
		{
			return _command;
		}
		
		override public function clone():Event
		{
			var event:CommandEvent = new CommandEvent(type, bubbles, cancelable);
			event._command = command;
			return event;
		}
	}
}