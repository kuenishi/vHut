/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.aig.domain.Aigs;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * アプリケーションインスタンスグループ概要一覧取得.
	 * <p>DBに登録されているすべてのアプリケーションインスタンスグループの概要情報をリスト形式で返す。</p>
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
	public class GetAllApplicationInstanceGroupAbstractionListCommand extends BaseCommand
	{
		[Inject]
		/**
		 * アプリケーションインスタンスグループの管理クラス.
		 */
		public var aigs:Aigs;

		[Inject(id="aigDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		[Inject(id="aigService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function GetAllApplicationInstanceGroupAbstractionListCommand()
		{
			super(
				"AIG",
				"jp.co.ntts.vhut.aig.application.GetAllApplicationInstanceGroupAbstractionListCommand",
				"ApplicationInstanceGroupService.getAllApplicationInstanceGroupAbstractionList"
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
			return service.getAllApplicationInstanceGroupAbstractionList() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response(items);
			aigs.aigs = cache.synchronize(items);
			//dispatcher(GetAllEvent.newGetAllCommandEvent());
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