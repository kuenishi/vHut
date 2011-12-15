/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core
{
	import flash.events.Event;
	
	/**
	 * 引数なしで要素をひとつ取得する際のイベントです.
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
	public class GetEvent extends Event
	{
		/** 現状のセッション・ユーザを取得します */
		public static const CURRENT_USER:String = "getCurrentUser";
		/** パフォーマンスの詳細情報を取得します */
		public static const PERFORMANCE:String = "getPerformance";
		/** パフォーマンスの概要情報を取得します */
		public static const PERFORMANCE_ABSTRACTION:String = "getPerformanceAbstraction";
		/** 障害の詳細情報を取得します */
		public static const TROUBLE:String = "getTrouble";
		/** 障害の概要情報を取得します */
		public static const TROUBLE_ABSTRACTION:String = "getTroubleAbstraction";
		/** 設定情報（サービス系）を取得します */
		public static const SERVICE_CONFIG:String = "getServiceConfig";
		/** 設定情報（クラウド系）を取得します */
		public static const CLOUD_CONFIG:String = "getCloudConfig";
		
		public static function newGetCurrentUserEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(CURRENT_USER);
			return event;
		}
		
		public static function newGetPerformanceEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(PERFORMANCE);
			return event;
		}
		
		public static function newGetPerformanceAbstractionEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(PERFORMANCE_ABSTRACTION);
			return event;
		}
		
		public static function newGetTroubleEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(TROUBLE);
			return event;
		}
		
		public static function newGetTroubleAbstaractionEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(TROUBLE_ABSTRACTION);
			return event;
		}
		
		public static function newGetServiceConfigEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(SERVICE_CONFIG);
			return event;
		}
		
		public static function newGetCloudConfigEvent():GetEvent
		{
			var event:GetEvent = new GetEvent(CLOUD_CONFIG);
			return event;
		}
		
		public function GetEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		override public function clone():Event
		{
			var event:GetEvent = new GetEvent(type, bubbles, cancelable);
			return event;
		}
	}
}