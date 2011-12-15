/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.aig.UserListEvent;
	import jp.co.ntts.vhut.aig.domain.EditingAig;
	import jp.co.ntts.vhut.aig.domain.ImportingAigs;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.entity.VhutUser;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * ユーザの一括検証
	 * <p>指定したユーザリストのうち登録できるものは登録して、登録できないものは返却します</p>
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
	public class ValidateVhutUserListCommand extends BaseCommand
	{
		[Inject]
		/**
		 * 編集中アプリケーションインスタンスグループの管理クラス.
		 */
		public var importingAigs:ImportingAigs;

		[Inject(id="userDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		[Inject(id="vhutUserService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function ValidateVhutUserListCommand()
		{
			super(
				"AIG",
				"jp.co.ntts.vhut.aig.application.ValidateUserListCommand",
				"VhutUserService.validateVhutUserList"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:UserListEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();

			var accountList:Array = new Array();
			for each (var user:VhutUser in event.userList)
			{
				if(user.account != null)
				{
					accountList.push(user.account);
				}
			}

			return service.validateVhutUserList(accountList) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response();
			importingAigs.addRegisteredUsers(items);
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