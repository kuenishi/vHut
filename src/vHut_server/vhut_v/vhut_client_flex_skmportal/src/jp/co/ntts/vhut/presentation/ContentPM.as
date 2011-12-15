/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.presentation
{
	import com.adobe.cairngorm.module.ModuleViewLoader;
	import com.adobe.cairngorm.navigation.NavigationEvent;
	import com.adobe.cairngorm.navigation.history.IHistory;
	import com.adobe.cairngorm.navigation.state.ISelectedIndex;
	import com.adobe.cairngorm.navigation.state.ISelectedName;

	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.application.ContentDestination;
	import jp.co.ntts.vhut.core.AuthenticationEvent;
	import jp.co.ntts.vhut.core.application.ModuleChangeEvent;
	import jp.co.ntts.vhut.core.domain.Session;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.containers.ViewStack;
	import mx.events.IndexChangedEvent;
	import mx.events.ItemClickEvent;

	import org.spicefactory.parsley.core.messaging.MessageProcessor;

	import spark.components.NavigatorContent;

	[Event(type="com.adobe.cairngorm.navigation.NavigationEvent", name="navigateTo")]
	[Event(type="jp.co.ntts.vhut.core.AuthenticationEvent", name="login")]
	[Event(type="jp.co.ntts.vhut.core.AuthenticationEvent", name="logout")]
	[Event(type="jp.co.ntts.vhut.core.application.ModuleChangeEvent", name="moduleChange")]
	[ManagedEvents("navigateTo, login, logout, moduleChange")]
	[Landmark(name="content")]
	/**
	 * メイン要素を管理するPresentaition Model.
	 * <p>ItemClickEventを受け取って画面を遷移させます</p>
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
	public class ContentPM extends EventDispatcher implements ISelectedIndex, ISelectedName
	{
		/** ログイン未完了状態 */
		public static const STATE_LOGOUT:String = "logout";
		/** ログイン完了状態 */
		public static const STATE_LOGIN:String = "login";

		[Inject]
		[Bindable]
		public var session:Session;

		[Bindable]
		public var selectedIndex:int = 2;

		[Bindable]
		public function set selectedName(value:String):void
		{
			/** モジュールの変更を知らせるイベントを送信する */
			try{
				var oldModuleName:String
				if (_selectedName)
				{
					oldModuleName = ContentDestination.valueOf(_selectedName).moduleName;
				}
				var newModuleName:String = ContentDestination.valueOf(value).moduleName;
				dispatchEvent(ModuleChangeEvent.newModuleChangeEvent(oldModuleName, newModuleName));
			}
			catch(e:ArgumentError)
			{
				//do nothing.
			}
			_selectedName = value;
		}
		public function get selectedName():String
		{
			return _selectedName;
		}
		private var _selectedName:String;

		[Bindable]
		public var buttonBar:Array=["Top", "Application", "AIG"];

		[Bindable]
		public var state:String = STATE_LOGOUT;

		[Inject]
		[Bindable]
		public var history:IHistory;

		private var _authentication_watcher:ChangeWatcher;

		[Init]
		public  function init():void
		{
			_authentication_watcher = BindingUtils.bindSetter(
				function(value:Boolean):void
				{
					if(value)
					{
						state = STATE_LOGIN;
					}
					else
					{
						state = STATE_LOGOUT;
					}
				}
				,session
				,"isAuthenticated"
			);
		}

		[EnterInterceptor]
		public function interceptEnter(processor:MessageProcessor):void
		{
			processor.proceed();
		}

		[ExitInterceptor]
		public function interceptExit(processor:MessageProcessor):void
		{
			processor.proceed();
		}

		/**
		 * システムにログインします.
		 * @param account ユーザアカウント
		 * @param password パスワード
		 */
		public function login(account:String, password:String):void
		{
			dispatchEvent(AuthenticationEvent.newLoginAuthenticationEvet(account, password));
		}

		/**
		 * システムからログアウトします.
		 */
		public function logout():void
		{
			dispatchEvent(AuthenticationEvent.newLogoutAuthenticationEvent());
		}
	}
}