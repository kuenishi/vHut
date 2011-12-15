/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;
	
	import jp.co.ntts.vhut.app.AppVmEvent;
	import jp.co.ntts.vhut.app.ApplicationEvent;
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.core.AlertEvent;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.log.VhutLog;
	import jp.co.ntts.vhut.log.VhutLogLogger;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * アプリケーションVM 停止.
	 * <p>VMを停止する。</p>
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
	public class StopApplicationVmCommand extends BaseCommand
	{
		/**
		 * ロガー.
		 */
		private static const logger:VhutLogLogger=VhutLog.getLogger("jp.co.ntts.vhut.app.application.StopApplicationVmCommand");
		
		/**
		 * クラス名.
		 */
		private static const CLASS:String="StopApplicationVmCommand";
		
		/**
		 * JAVAファンクション名.
		 */
		private static const JAVA:String="ApplicationService.stopApplicationVm";
		
		[Inject]
		/**
		 * アプリケーション関連コマンドの管理クラス.
		 */
		public var applications:Applications;
		
		[Inject(id="applicationService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;
		
		/**
		 * コンストラクタ.
		 */
		public function StopApplicationVmCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.app.application.StopApplicationVmCommand",
				"ApplicationService.stopApplicationVmCommand"
			);
		}
		
		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:AppVmEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.stopApplicationVm(event.id) as AsyncToken;
		}
		
		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response();
			applications.updateVmStatus(item as ApplicationVm);
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