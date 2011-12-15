/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.infrastructure
{
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.exception.AuthenticationException;
	import jp.co.ntts.vhut.log.VhutLog;
	import jp.co.ntts.vhut.log.VhutLogLogger;

	import mx.rpc.events.FaultEvent;

	/**
	 * Parsleyでハンドリングされるすべてのサーバー通信のエラーを解析して
	 * サーバセッションエラー処理を行うハンドラです.
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
	public class SessionHandler
	{
		protected var logger:VhutLogLogger = VhutLog.getLogger("jp.co.ntts.vhut.core.infrastructure.SessionHandler");

		[Inject]
		public var session:Session;

		[GlobalRemoteObjectFaultHandler]
		public function sessionError(event:FaultEvent):void
		{
			if(event.fault.rootCause is AuthenticationException)
			{
				session.isAuthenticated = false;
			}
		}

	}
}