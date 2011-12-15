/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	/**
	 * 認証イベントクラス.
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
	public class AuthenticationEvent extends Event
	{

		/**
		 * ログアウト.
		 */
		public static const LOGIN:String="login";
		
		/**
		 * ログイン.
		 */
		public static const LOGOUT:String="logout";
		
		/**
		 * ログインイベント生成.
		 * @param account　アカウント
		 * @param password　パスワード
		 * @return  ログインイベント
		 * 
		 */
		public static function newLoginAuthenticationEvet(account:String, password:String):AuthenticationEvent
		{
			var event:AuthenticationEvent = new AuthenticationEvent(LOGIN);
			event._account = account;
			event._password = password;
			return event;
		}
		
		/**
		 * ログアウトイベント生成. 
		 * @return ログアウトイベント
		 * 
		 */
		public static function newLogoutAuthenticationEvent():AuthenticationEvent
		{
			return new AuthenticationEvent(LOGOUT);
		}

		public function AuthenticationEvent(type:String)
		{
			super(type);
		}
		
		/**
		 * アカウント.
		 */
		private var _account:String;
		
		/**
		 * パスワード.
		 */
		private var _password:String;

		/**
		 * アカウント.
		 * @return アカウント
		 * 
		 */
		public function get account():String
		{
			return _account;
		}
		
		/**
		 * パスワード.
		 * @return パスワード
		 * 
		 */
		public function get password():String
		{
			return _password;
		}
		
		/** クローン */
		override public function clone():Event
		{
			var event:AuthenticationEvent = new AuthenticationEvent(type);
			event._account = _account;
			event._password = _password;
			return event;
		}
	}
}