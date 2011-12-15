/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	/**
	 * ID指定の取得系サーバアクセスイベント.
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
	public class GetByIdEvent extends Event
	{
		public static const APP:String = "getAppById";
		public static const APP_LIST_BY_USER:String = "getAppListByUserId";
		public static const RAPP:String = "getRappById";
		public static const COMMAND:String = "getCommandById";
		public static const TERM_LIST_BY_APP:String = "getTermListByAppId";
		public static const AVAILABLE_TERM_LIST_BY_APP:String = "getAvailableTermListByAppId";
		public static const AI:String = "getAiById";
		public static const AI_LIST_BY_USER:String = "getAiListByUserId";
		public static const AI_LIST_BY_AIG:String = "getAiListByAigId";
		public static const AIG:String = "getAigById";
		public static const AIG_LIST_BY_USER:String = "getAigListByUserId";
		public static const USR:String = "getUsrById";
		public static const TMP:String = "getTmpById";
		public static const ROL:String = "getRolById";
		public static const COMMAND_LIST_BY_APP:String = "getCommandListByAppId";
		public static const COMMAND_LIST_BY_RAPP:String = "getCommandListByRappId";
		public static const COMMAND_LIST_BY_AI:String = "getCommandListByAiId";
		public static const IP_INFO_LIST_BY_AIG:String = "getIpInfoListByAigId";

		public static function newGetApplication(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(APP);
			event._id = id;
			return event;
		}

		public static function newGetApplicationListByUser(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(APP_LIST_BY_USER);
			event._id = id;
			return event;
		}

		public static function newGetReleasedApplication(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(RAPP);
			event._id = id;
			return event;
		}

		public static function newGetCommand(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(COMMAND);
			event._id = id;
			return event;
		}

		public static function newGetTermListByApplication(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(TERM_LIST_BY_APP);
			event._id = id;
			return event;
		}

		public static function newGetAi(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(AI);
			event._id = id;
			return event;
		}

		public static function newGetAiListByUsr(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(AI_LIST_BY_USER);
			event._id = id;
			return event;
		}

		public static function newGetAiListByAig(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(AI_LIST_BY_AIG);
			event._id = id;
			return event;
		}

		public static function newGetAig(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(AIG);
			event._id = id;
			return event;
		}

		public static function newGetAigListByUsr(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(AIG_LIST_BY_USER);
			event._id = id;
			return event;
		}

		public static function newGetUsr(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(USR);
			event._id = id;
			return event;
		}

		public static function newGetRol(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(ROL);
			event._id = id;
			return event;
		}

		public static function newGetTmp(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(TMP);
			event._id = id;
			return event;
		}

		public static function newGetCommandListByApplication(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(COMMAND_LIST_BY_APP);
			event._id = id;
			return event;
		}

		public static function newGetCommandListByReleasedApplication(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(COMMAND_LIST_BY_RAPP);
			event._id = id;
			return event;
		}

		public static function newGetCommandListByAi(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(COMMAND_LIST_BY_AI);
			event._id = id;
			return event;
		}

		public static function newGetIpInfoListByAig(id:Number):GetByIdEvent
		{
			var event:GetByIdEvent = new GetByIdEvent(IP_INFO_LIST_BY_AIG);
			event._id = id;
			return event;
		}

		public function GetByIdEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		protected var _id:Number;

		public function get id():Number
		{
			return _id;
		}

		override public function clone():Event
		{
			var event:GetByIdEvent = new GetByIdEvent(type, bubbles, cancelable);
			event._id = _id;
			return event;
		}
	}
}