/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	
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
	public class AigEvent extends Event
	{
		/** アプリケーションインスタンスグループを選択しました. */
		private static const SELECT_AIG:String = "selectAig";
		
		/** アプリケーションインスタンスグループを作成します. */
		private static const CREATE_AIG:String = "createAig";
		
		/** アプリケーションインスタンスグループを削除します. */
		private static const DELETE_AIG:String = "deleteAig";
		
		/** アプリケーションインスタンスグループを更新します. */
		private static const UPDATE_AIG:String = "updateAig";
		
		/**
		 * アプリケーションインスタンスグループを選択したというイベントを作成します.
		 * @param item 選択したアプリケーションインスタンスグループ
		 * @return イベント
		 */
		public static function newSelectAigEvent(item:ApplicationInstanceGroup):AigEvent
		{
			var event:AigEvent = new AigEvent(SELECT_AIG);
			event._aig = item;
			return event;
		}
		
		/**
		 * アプリケーションインスタンスグループを作成するというイベントを作成します.
		 * @param item 作成するアプリケーションインスタンスグループ
		 * @return イベント
		 */
		public static function newCreateAigEvent(item:ApplicationInstanceGroup):AigEvent
		{
			var event:AigEvent = new AigEvent(CREATE_AIG);
			event._aig = item;
			return event;
		}
		
		/**
		 * アプリケーションインスタンスグループを削除するというイベントを作成します.
		 * @param item 削除するアプリケーションインスタンスグループ
		 * @return イベント
		 */
		public static function newDeleteAigEvent(item:ApplicationInstanceGroup):AigEvent
		{
			var event:AigEvent = new AigEvent(DELETE_AIG);
			event._aig = item;
			return event;
		}
		
		/**
		 * アプリケーションインスタンスグループを更新するというイベントを作成します.
		 * @param item 更新するアプリケーションインスタンスグループ
		 * @return イベント
		 */
		public static function newUpdateAigEvent(item:ApplicationInstanceGroup):AigEvent
		{
			var event:AigEvent = new AigEvent(UPDATE_AIG);
			event._aig = item;
			return event;
		}
		
		/**
		 * コンストラクタ.
		 * @param type
		 * @param bubbles
		 * @param cancelable
		 */
		public function AigEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _aig:ApplicationInstanceGroup;
		
		/**
		 * ターゲットのアプリケーションインスタンスグループ.
		 */
		public function get aig():ApplicationInstanceGroup
		{
			return _aig;
		}
		
		override public function clone():Event
		{
			var event:AigEvent = new AigEvent(type, bubbles, cancelable);
			event._aig = _aig;
			return event;
		}
	}
}