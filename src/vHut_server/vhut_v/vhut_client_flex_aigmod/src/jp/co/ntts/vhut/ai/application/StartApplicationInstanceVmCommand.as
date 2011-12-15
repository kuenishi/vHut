/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.ai.application
{
	import jp.co.ntts.vhut.ai.AiVmEvent;
	import jp.co.ntts.vhut.ai.domain.Ais;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.entity.ApplicationInstanceVm;

	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * アプリケーションインスタンスVM 起動.
	 * <p>VMを起動する。</p>
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
	public class StartApplicationInstanceVmCommand extends BaseCommand
	{
		[Inject]
		/**
		 * アプリケーション関連コマンドの管理クラス.
		 */
		public var ais:Ais;

		[Inject(id="applicationService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function StartApplicationInstanceVmCommand()
		{
			super(
				"AIG",
				"jp.co.ntts.vhut.ai.application.StartApplicationInstanceVmCommand",
				"ApplicationInstanceGroupService.startApplicationInstanceVm"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:AiVmEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.startApplicationVm(event.id) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response();
			ais.updateVmStatus(item as ApplicationInstanceVm);
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