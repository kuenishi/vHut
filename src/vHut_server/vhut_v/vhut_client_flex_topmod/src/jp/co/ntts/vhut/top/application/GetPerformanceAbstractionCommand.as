/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.top.application
{
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.top.domain.Abstraction;

	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * パフォーマンス概要一覧取得.
	 * <p>パフォーマンスの概要を1-5の整数で返します。</p>
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
	public class GetPerformanceAbstractionCommand extends BaseCommand
	{
		[Inject]
		/**
		 * 概要情報の管理クラス.
		 */
		public var abstraction:Abstraction;

		[Inject(id="basicService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function GetPerformanceAbstractionCommand()
		{
			super(
				"TOP",
				"jp.co.ntts.vhut.top.application.GetPerformanceAbstractionCommand",
				"BasicService.getPerformanceAbstraction"
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
			return service.getPerformanceAbstraction() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Number):void
		{
			_response(item);
			abstraction.performanceRank = item;
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