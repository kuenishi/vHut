/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rapp.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;
	
	import jp.co.ntts.vhut.core.AlertEvent;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.entity.ReleasedApplication;
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
	 * リリースドアプリケーション削除.
	 * <p>リリースドアプリケーションのIDを受け取り、
	 * 削除可能か判定し、削除できる場合はコマンドを登録し、
	 * 結果を返す。
	 * DB更新が出来ない場合は例外を返す</p>
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
	public class RemoveReleasedApplicationByIdCommand extends BaseCommand
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

		private var target:ReleasedApplication;

		/**
		 * コンストラクタ.
		 */
		public function RemoveReleasedApplicationByIdCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.rapp.application.RemoveReleasedApplicationByIdCommand",
				"ApplicationService.removeReleasedApplicationById"
			);
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:ReleasedApplicationEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			target = event.releasedApplication;
			return service.removeReleasedApplicationById(target.id) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response();
			cache.clearItem(target);
//			releasedApplications.releasedApplications = cache.synchronize(null);
			releasedApplications.updateReleasedApplications();
			//dispatcher(GetAllEvent.newGetAllCommandEvent());
			//TODO:　消しこむ処理、状態のアップデート？
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