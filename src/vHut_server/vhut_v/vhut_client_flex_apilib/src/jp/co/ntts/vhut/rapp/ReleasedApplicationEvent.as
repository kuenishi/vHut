/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rapp
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ReleasedApplication;
	
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
	public class ReleasedApplicationEvent extends Event
	{
		private static const REMOVE_RAPP:String = "removeRapp";
		
		private static const SELECT_RAPP:String = "selectRapp";
		
		/**
		 * リリースドアプリケーションを削除するイベントを作成します.
		 * @param releasedApplicationId リリースドアプリケーションのID
		 * @return イベント
		 */
		public static function newRemoveReleasedApplicationEvent(releasedApplication:ReleasedApplication):ReleasedApplicationEvent
		{
			var event:ReleasedApplicationEvent = new ReleasedApplicationEvent(REMOVE_RAPP);
			event._releasedApplication = releasedApplication;
			return event;
		}
		
		public static function newSelectReleasedApplicationEvent(releasedApplication:ReleasedApplication):ReleasedApplicationEvent
		{
			var event:ReleasedApplicationEvent = new ReleasedApplicationEvent(SELECT_RAPP);
			event._releasedApplication = releasedApplication;
			return event;
		}
		
		public function ReleasedApplicationEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _releasedApplication:ReleasedApplication;
		
		public function get releasedApplication():ReleasedApplication
		{
			return _releasedApplication;
		}
	}
}