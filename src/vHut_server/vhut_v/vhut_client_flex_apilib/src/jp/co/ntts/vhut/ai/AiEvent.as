/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.ai
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	
	/**
	 * アプリケーション関連の更新系サーバアクセスイベント.
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
	public class AiEvent extends Event
	{
		/** アプリケーションインスタンスを選択しました. */
		private static const SELECT_AI:String = "selectAi";
		
		/** アプリケーションインスタンスを有効化します. */
		private static const ACTIVATE_AI:String = "activateAi";
		
		/** アプリケーションインスタンスを無効化します. */
		private static const DEACTIVATE_AI:String = "deactivateAi";
		
		/** アプリケーションインスタンスを再作成します. */
		private static const REBUILD_AI:String = "rebuildAi";
		
		/**
		 * アプリケーションインスタンスを選択したというイベントを作成します.
		 * @param item 選択したアプリケーションインスタンス
		 * @return イベント
		 */
		public static function newSelectAiEvent(item:ApplicationInstance):AiEvent
		{
			var event:AiEvent = new AiEvent(SELECT_AI);
			event._ai = item;
			return event;
		}
		
		/**
		 * アプリケーションインスタンスを有効化するというイベントを作成します.
		 * @param item 有効化するアプリケーションインスタンス
		 * @return イベント
		 */
		public static function newActivateAiEvent(item:ApplicationInstance):AiEvent
		{
			var event:AiEvent = new AiEvent(ACTIVATE_AI);
			event._ai = item;
			return event;
		}
		
		/**
		 * アプリケーションインスタンスを無効化するというイベントを作成します.
		 * @param item 無効化するアプリケーションインスタンス
		 * @return イベント
		 */
		public static function newDeactivateAiEvent(item:ApplicationInstance):AiEvent
		{
			var event:AiEvent = new AiEvent(DEACTIVATE_AI);
			event._ai = item;
			return event;
		}
		
		/**
		 * アプリケーションインスタンスを再作成するというイベントを作成します.
		 * @param item 再作成するアプリケーションインスタンス
		 * @return イベント
		 */
		public static function newRebuildAiEvent(item:ApplicationInstance):AiEvent
		{
			var event:AiEvent = new AiEvent(REBUILD_AI);
			event._ai = item;
			return event;
		}
		
		/**
		 * コンストラクタ.
		 * @param type
		 * @param bubbles
		 * @param cancelable
		 */
		public function AiEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _ai:ApplicationInstance;
		
		/**
		 * ターゲットのアプリケーションインスタンス.
		 */
		public function get ai():ApplicationInstance
		{
			return _ai;
		}
		
		override public function clone():Event
		{
			var event:AiEvent = new AiEvent(type, bubbles, cancelable);
			event._ai = _ai;
			return event;
		}
	}
}