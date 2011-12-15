/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.entity.Application;
	
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
	public class ApplicationEvent extends Event
	{
		private static const CREATE_APP:String = "createApp";
		
		private static const DELETE_APP:String = "deleteApp";
		
		private static const SELECT_APP:String = "selectApp";
		
		private static const UPDATE_APP:String = "updateApp";
		
		private static const ACTIVATE_APP:String = "activateApp";
		
		private static const DEACTIVATE_APP:String = "deactivateApp";
		
		private static const DEPLOY_APP:String = "deployApp";
		
		private static const ADD_RAPP:String = "addRapp";
		
		public static function newCreateApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(CREATE_APP);
			event._application = item;
			return event;
		}
		
		public static function newSelectApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(SELECT_APP);
			event._application = item;
			return event;
		}
		
		public static function newUpdateApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(UPDATE_APP);
			event._application = item;
			return event;
		}
		
		public static function newActivateApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(ACTIVATE_APP);
			event._application = item;
			return event;
		}
		
		public static function newDeactivateApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(DEACTIVATE_APP);
			event._application = item;
			return event;
		}
		
		public static function newDeployApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(DEPLOY_APP);
			event._application = item;
			return event;
		}
		
		public static function newDeleteApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(DELETE_APP);
			event._application = item;
			return event;
		}
		
		/**
		 * アプリケーションからリリースドアプリケーションを作成するイベントを作成します.
		 * @param applicationId 基になるアプリケーション
		 * @return イベント
		 */
		public function newAddReleasedApplicationEvent(item:Application):ApplicationEvent
		{
			var event:ApplicationEvent = new ApplicationEvent(ADD_RAPP);
			event._application = item;
			return event;
		}
		
		public function ApplicationEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _application:Application;
		
		public function get application():Application
		{
			return _application;
		}
	}
}