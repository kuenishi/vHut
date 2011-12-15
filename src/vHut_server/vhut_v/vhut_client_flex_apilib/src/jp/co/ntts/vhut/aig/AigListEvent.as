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
	public class AigListEvent extends Event
	{
		/** 複数のアプリケーションインスタンスグループを一括作成します. */
		private static const CREATE_AIG_LIST:String = "createAigList";
		
		/**
		 * アプリケーションインスタンスグループを選択したというイベントを作成します.
		 * @param item 選択したアプリケーションインスタンスグループ
		 * @return イベント
		 */
		public static function newCreateAigListEvent(itemList:Array):AigListEvent
		{
			var event:AigListEvent = new AigListEvent(CREATE_AIG_LIST);
			event._aigList = itemList;
			return event;
		}
		
		/**
		 * コンストラクタ.
		 * @param type
		 * @param bubbles
		 * @param cancelable
		 */
		public function AigListEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _aigList:Array;
		
		/**
		 * ターゲットのアプリケーションインスタンスグループ.
		 */
		public function get aigList():Array
		{
			return _aigList;
		}
		
		override public function clone():Event
		{
			var event:AigListEvent = new AigListEvent(type, bubbles, cancelable);
			event._aigList = _aigList;
			return event;
		}
	}
}