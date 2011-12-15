/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.infrastructure
{
	import jp.co.ntts.vhut.core.AuthenticationEvent;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.core.domain.Session;

	import mx.messaging.ChannelSet;
	import mx.messaging.config.ServerConfig;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * サーバにログインするためのコマンド
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
	public class LoginCommand extends BaseCommand
	{
		public function LoginCommand()
		{
			super(
				"API",
				"jp.co.ntts.vhut.core.infrastracture.CancelCommandCommand",
				"ChannelSet.login"
			);
		}

		[Inject(id="basicService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		[Inject]
		/**
		 * リモートオブジェクト.
		 */
		public var session:Session;

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:AuthenticationEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			var ch:ChannelSet = ServerConfig.getChannelSet(service.destination);
			if (ch.authenticated)
			{
				session.isAuthenticated = true;
				return null;
			}
			else
			{
				return ch.login(event.account, event.password) as AsyncToken;
			}
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response(item);
			session.isAuthenticated = true;
		}

		/**
		 * Spice Frameworkからメソッド異常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function error(fault:FaultEvent):void
		{
			_error(fault);
			session.setAuthenticationError();
		}
	}
}