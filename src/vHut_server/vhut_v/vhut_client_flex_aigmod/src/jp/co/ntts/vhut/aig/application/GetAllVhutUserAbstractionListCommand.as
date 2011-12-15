/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.aig.domain.Apps;
	import jp.co.ntts.vhut.aig.domain.EditingAig;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.OwnerSelectDialogPM;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * 登録ユーザ概要一覧取得.
	 * <p>DBに登録されているすべてのユーザの概要情報をリスト形式で返す。</p>
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
	public class GetAllVhutUserAbstractionListCommand extends BaseCommand
	{
		[Inject]
		/**
		 * ユーザの管理クラス.
		 */
		public var editingAig:EditingAig;

		[Inject]
		/**
		 * 所有者選択ダイアログのPM.
		 */
		public var ownerSelectDialogPM:OwnerSelectDialogPM;

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
		public function GetAllVhutUserAbstractionListCommand()
		{
			super(
				"AIG",
				"jp.co.ntts.vhut.aig.application.GetAllVhutUserAbstractionListCommand",
				"VhutUserService.getAllVhutUserAbstractionList"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:GetAllEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.getAllVhutUserAbstractionList() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response(items);
			editingAig.userList = ownerSelectDialogPM.userList = cache.synchronize(items);
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