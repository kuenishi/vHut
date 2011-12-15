/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.core.infrastructure
{
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.entity.VhutUser;

	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * 最新の認証ユーザを取得します.
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
	public class GetCurrentUserCommand extends BaseCommand
	{
		public function GetCurrentUserCommand()
		{
			super("API"
				, "jp.co.ntts.vhut.core.infrastracture.GetCurrentUserCommand"
				, "BasicService.getCurrentUser");
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
		public function execute(event:GetEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.getCurrentUser() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response(item);
			session.user=item as VhutUser;
		}

		/**
		 * Spice Frameworkからメソッド異常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function error(fault:FaultEvent):void
		{
			_error(fault);
			session.setSystemError();
		}
	}
}