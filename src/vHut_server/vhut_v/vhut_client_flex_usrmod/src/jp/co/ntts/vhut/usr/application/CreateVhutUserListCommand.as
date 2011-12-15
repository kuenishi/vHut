/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.usr.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.usr.UsrEvent;
	import jp.co.ntts.vhut.usr.UsrListEvent;
	import jp.co.ntts.vhut.usr.domain.VhutUsers;

	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * ユーザの一括追加
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
	public class CreateVhutUserListCommand extends BaseCommand
	{
		[Inject]
		/**
		 * ユーザの管理クラス.
		 */
		public var vhutUsers:VhutUsers;

		[Inject(id="vhutUserService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function CreateVhutUserListCommand()
		{
			super(
				"USR",
				"jp.co.ntts.vhut.usr.application.CreateVhutUserListCommand",
				"VhutUserService.createVhutUserList"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:UsrListEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request(event.vhutUserList);
			return service.createVhutUserList(event.vhutUserList) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result():void
		{
			_response();
			vhutUsers.updateVhutUsers();
		}

		/**
		 * Spice Frameworkからメソッド異常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function error(fault:FaultEvent):void
		{
			_error(fault);
			//dispatcher(AlertEvent.newAlertEvent("EAPP1111"));
		}
	}
}