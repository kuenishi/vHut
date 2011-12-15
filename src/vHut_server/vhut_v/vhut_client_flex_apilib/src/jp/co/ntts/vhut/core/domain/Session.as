/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.core.SessionEvent;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.util.MessageUtil;

	[Event(name="sessionChanged", type="jp.co.ntts.vhut.core.SessionEvent")]
	[Event(name="getCurrentUser", type="jp.co.ntts.vhut.core.GetEvent")]
	[ManagedEvents(names="getCurrentUser")]
	/**
	 * サーバとのセッション情報を管理します.
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
	public class Session extends EventDispatcher
	{
		public static const CHANGE_USER:String = "changeUser";
		public static const CHANGE_ERROR:String = "changeError";

		[Bindable("changeUser")]
		/** 認証されている？ */
		public function set isAuthenticated(value:Boolean):void
		{
			if (value)
			{
				if(user == null)
				{
					//実態と合っていない時、userを取りに行く
					dispatchEvent(GetEvent.newGetCurrentUserEvent());
				}
				else
				{
					clearError();
				}
			} else {
				user = null;
			}
			dispatchEvent(SessionEvent.newSessionChangedEvent(isAuthenticated, user));
		}
		public function get isAuthenticated():Boolean
		{
			return user != null;
		}

		[Bindable]
		/** 認証されたユーザ */
		public function set user(value:VhutUser):void
		{
			if (_user == value) return;
			_user = value;
			dispatchEvent(new Event(CHANGE_USER));
			dispatchEvent(SessionEvent.newSessionChangedEvent(isAuthenticated, user));
			clearError();
		}
		public function get user():VhutUser
		{
			return _user;
		}
		private var _user:VhutUser;


		public function setAuthenticationError():void
		{
			_errorCode = "EAPI0021";
			dispatchEvent(new Event(CHANGE_ERROR));
		}

		public function setAuthorizationError():void
		{
			_errorCode = "EAPI0022";
			dispatchEvent(new Event(CHANGE_ERROR));
		}

		public function setSystemError():void
		{
			_errorCode = "EAPI9999";
			dispatchEvent(new Event(CHANGE_ERROR));
		}

		public function clearError():void
		{
			_errorCode = null;
			dispatchEvent(new Event(CHANGE_ERROR));
		}

		[Bindable("changeError")]
		public function get errorCode():String
		{
			return _errorCode;
		}
		private var _errorCode:String;

		[Bindable("changeError")]
		public function get errorString():String
		{
			if(errorCode == null)
			{
				return "";
			}
			else
			{
				return MessageUtil.getMessage(errorCode);
			}
		}

	}
}