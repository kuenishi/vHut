/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.cnf.application
{
	import jp.co.ntts.vhut.cnf.domain.Configs;
	import jp.co.ntts.vhut.config.ServiceConfig;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;

	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * 設定情報（サービス系）取得.
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
	public class GetServiceConfigurationCommand extends BaseCommand
	{
		[Inject]
		/**
		 * パフォーマンス情報の管理クラス.
		 */
		public var configs:Configs;

		[Inject(id="configurationService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function GetServiceConfigurationCommand()
		{
			super(
				"CNF",
				"jp.co.ntts.vhut.cnf.application.GetServiceConfigurationCommand",
				"ConfigurationService.getServiceConfiguration"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:GetEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.getServiceConfiguration() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:ServiceConfig):void
		{
			_response(item);
			configs.serviceConfig = item;
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