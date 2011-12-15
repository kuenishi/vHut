/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rapp.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;
	
	import jp.co.ntts.vhut.core.AlertEvent;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.log.VhutLog;
	import jp.co.ntts.vhut.log.VhutLogLogger;
	import jp.co.ntts.vhut.rapp.ReleasedApplicationEvent;
	import jp.co.ntts.vhut.rapp.domain.ReleasedApplications;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * リリースドアプリケーション概要一覧取得.
	 * <p>DBに登録されているすべてのリリースドアプリケーションの概要情報をリスト形式で返す。</p>
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
	public class GetAllReleasedApplicationAbstractionListCommand extends BaseCommand
	{
		[Inject]
		/**
		 * リリースドアプリケーションの管理クラス.
		 */
		public var releasedApplications:ReleasedApplications;

		[Inject(id="releasedApplicationDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		[Inject(id="releasedApplicationService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		/**
		 * コンストラクタ.
		 */
		public function GetAllReleasedApplicationAbstractionListCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.rapp.application.GetAllReleasedApplicationAbstractionListCommand",
				"ApplicationService.getAllReleasedApplicationAbstractionList"
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
			return service.getAllReleasedApplicationAbstractionList() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response();
			releasedApplications.releasedApplications = cache.synchronize(items);
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