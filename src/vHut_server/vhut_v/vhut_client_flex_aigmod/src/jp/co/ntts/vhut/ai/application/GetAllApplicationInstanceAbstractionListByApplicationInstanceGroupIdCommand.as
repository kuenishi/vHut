/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.ai.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.ai.domain.Ais;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * アプリケーションインスタンスグループに所属する
	 * アプリケーションインスタンスのリストを取得します.
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
	public class GetAllApplicationInstanceAbstractionListByApplicationInstanceGroupIdCommand extends BaseCommand
	{
		[Inject]
		/**
		 * アプリケーションインスタンスの管理クラス.
		 */
		public var ais:Ais;

		[Inject(id="aiDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		[Inject(id="aiService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function GetAllApplicationInstanceAbstractionListByApplicationInstanceGroupIdCommand()
		{
			super(
				"AIG",
				"jp.co.ntts.vhut.ai.application.GetAllApplicationInstanceAbstractionListByApplicationInstanceGroupIdCommand",
				"ApplicationInstanceGroupService.getAllApplicationInstanceAbstractionListByApplicationInstanceGroupId"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:GetByIdEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.getAllApplicationInstanceAbstractionListByApplicationInstanceGroupId(event.id) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response(items);
			cache.clear();
			ais.ais = cache.synchronize(items);
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