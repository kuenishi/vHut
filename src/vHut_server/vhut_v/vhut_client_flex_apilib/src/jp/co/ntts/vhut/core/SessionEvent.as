/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	import jp.co.ntts.vhut.entity.VhutUser;

	/**
	 * セッションの状態変更に関するイベント
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class SessionEvent extends Event
	{
		public static const SESSION_CHANGED:String = "sessionChanged";

		public static function newSessionChangedEvent(isAuthenticated:Boolean, vhutUser:VhutUser=null):SessionEvent
		{
			var event:SessionEvent = new SessionEvent(SESSION_CHANGED);
			event.isAuthenticated = isAuthenticated;
			event.vhutUser = vhutUser;
			return event;
		}

		public function SessionEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var isAuthenticated:Boolean;

		public var vhutUser:VhutUser;

		override public function clone():Event
		{
			var event:SessionEvent = new SessionEvent(type, bubbles, cancelable);
			event.isAuthenticated = isAuthenticated;
			event.vhutUser = vhutUser;
			return event;
		}
	}
}