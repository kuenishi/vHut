/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.tmp
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.BaseTemplate;
	
	/**
	 * 
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
	public class TmpEvent extends Event
	{
		
		/** ベーステンプレートを作成します. */
		private static const CREATE_TMP:String = "createTmp";
		
		/** ベーステンプレートを更新します. */
		private static const UPDATE_TMP:String = "updateTmp";
		
		/** ベーステンプレートを削除します. */
		private static const DELETE_TMP:String = "deleteTmp";
		
		/**
		 * ベーステンプレートを作成するイベントを作成します.
		 * @param item 作成したいベーステンプレート
		 * @return イベント
		 */
		public static function newCreateTmpEvent(item:BaseTemplate):TmpEvent
		{
			var event:TmpEvent = new TmpEvent(CREATE_TMP);
			event._baseTemplate = item;
			return event;
		}
		
		/**
		 * ベーステンプレートを編集するイベントを作成します.
		 * @param item 編集したいベーステンプレート
		 * @return イベント
		 */
		public static function newUpdateTmpEvent(item:BaseTemplate):TmpEvent
		{
			var event:TmpEvent = new TmpEvent(UPDATE_TMP);
			event._baseTemplate = item;
			return event;
		}
		
		/**
		 * ベーステンプレートを削除するイベントを作成します.
		 * @param item 作成したいベーステンプレート
		 * @return イベント
		 */
		public static function newDeleteTmpEvent(item:BaseTemplate):TmpEvent
		{
			var event:TmpEvent = new TmpEvent(DELETE_TMP);
			event._baseTemplate = item;
			return event;
		}
		
		public function TmpEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		/**
		 * ターゲットのベーステンプレート.
		 */
		public function get baseTemplate():BaseTemplate
		{
			return _baseTemplate;
		}
		
		private var _baseTemplate:BaseTemplate;
		
		override public function clone():Event
		{
			var event:TmpEvent = new TmpEvent(type, bubbles, cancelable);
			event._baseTemplate = _baseTemplate;
			return event;
		}
	}
}