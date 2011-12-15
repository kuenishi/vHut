/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.core
{
	import flash.events.Event;
	
	/**
	 * 全選択の取得系サーバアクセスイベント.
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
	public class GetAllEvent extends Event
	{
		public static const APP:String = "getAllApp";
		public static const RAPP:String = "getAllRapp";
		public static const AIG:String = "getAllAig";
		public static const USER:String = "getAllUser";
		public static const UNREGISTERED_USER:String = "getAllUnregisteredUser";
		public static const COMMAND:String = "getAllCommand";
		public static const ROLE:String = "getAllRole";
		public static const BASE_TEMPLATE:String = "getAllBaseTemplate";
		public static const UNREGESITERED_TEMPLATE:String = "getUnregisteredTemplate";
		public static const SWITCH_TEMPLATE:String = "getAllSwitchTemplate";
		public static const DISK_TEMPLATE:String = "getAllDiskTemplate";
		public static const SPEC_TEMPLATE:String = "getAllSpecTemplate";
		
		public static function newGetAllApplicationEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(APP);
			return event;
		}
		
		public static function newGetAllReleasedApplicationEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(RAPP);
			return event;
		}
		
		public static function newGetAllAigEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(AIG);
			return event;
		}
		
		public static function newGetAllUserEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(USER);
			return event;
		}
		
		public static function newGetAllUnregisteredUserEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(UNREGISTERED_USER);
			return event;
		}
		
		public static function newGetAllCommandEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(COMMAND);
			return event;
		}
		
		public static function newGetAllRoleEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(ROLE);
			return event;
		}
		
		public static function newGetAllBaseTemplateEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(BASE_TEMPLATE);
			return event;
		}
		
		public static function newGetUnregisteredTemplateEvent():GetAllEvent
		{
			var event:GetAllEvent = new GetAllEvent(UNREGESITERED_TEMPLATE);
			return event;
		}
		
		public function GetAllEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		override public function clone():Event
		{
			var event:GetAllEvent = new GetAllEvent(type, bubbles, cancelable);
			return event;
		}
	}
}