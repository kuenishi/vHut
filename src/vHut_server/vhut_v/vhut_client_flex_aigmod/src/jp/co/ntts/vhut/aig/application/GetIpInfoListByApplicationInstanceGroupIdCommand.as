/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.application
{
	import jp.co.ntts.vhut.aig.domain.Aigs;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dto.IpInfoDto;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * アプリケーションインスタンスグループの詳細取得.
	 * <p>アプリケーションインスタンスグループのIDを受け取りその詳細情報を返す</p>
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
	 * $Date: 2011-01-31 10:52:58 +0900 (月, 31 1 2011) $
	 * $Revision: 735 $
	 * $Author: NTT Software Corporation. $
	 */
	public class GetIpInfoListByApplicationInstanceGroupIdCommand extends BaseCommand
	{
		[Inject]
		/**
		 * アプリケーションインスタンスグループの管理クラス.
		 */
		public var aigs:Aigs;

		[Inject(id="aigService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		private var ipInfo:IpInfoDto;

		/**
		 * コンストラクタ.
		 */
		public function GetIpInfoListByApplicationInstanceGroupIdCommand()
		{
			super(
				"AIG",
				"jp.co.ntts.vhut.aig.application.GetIpInfoListByApplicationInstanceGroupIdCommand",
				"ApplicationInstanceGroupService.getIpInfoListByApplicationInstanceGroupId"
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
			return service.getIpInfoListByApplicationInstanceGroupId(event.id) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:IList):void
		{
			_response();
			if(aigs != null)
			{
				aigs.targetAigIpInfos = item as IList;
			}
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