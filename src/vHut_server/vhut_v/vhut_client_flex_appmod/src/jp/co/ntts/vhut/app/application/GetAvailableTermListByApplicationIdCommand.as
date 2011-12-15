/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;
	
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.core.AlertEvent;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.log.VhutLog;
	import jp.co.ntts.vhut.log.VhutLogLogger;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * 予約可能期間一覧取得.
	 * <p>指定した期間内に指定したアプリケーションの利用可能期間を取得する。</p>
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
	public class GetAvailableTermListByApplicationIdCommand extends BaseCommand
	{
		[Inject]
		/**
		 * 予約可能利用期間関連コマンドの管理クラス.
		 */
		public var applications:Applications;

		[Inject(id="availableTermDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		[Inject(id="applicationService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		protected var startTime:Date;

		protected var endTime:Date;

		/**
		 * コンストラクタ.
		 */
		public function GetAvailableTermListByApplicationIdCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.app.application.GetAvailableTermListByApplicationIdCommand",
				"ApplicationService.getAvailableTermListByApplicationIdCommand"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:GetByIdWithTimeSpanEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			startTime = event.startTime;
			endTime = event.endTime;
			return service.getAvailableTermListByApplicationId(event.id, event.startTime, event.endTime) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response();
			applications.syncTargetApplicationAvailableTermList(items, startTime, endTime);
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