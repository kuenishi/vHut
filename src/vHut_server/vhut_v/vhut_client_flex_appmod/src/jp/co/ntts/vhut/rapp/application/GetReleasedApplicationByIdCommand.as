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
	 * リリースドアプリケーション詳細取得.
	 * <p>リリースドアプリケーションのIDを受け取りその詳細情報を返す。</p>
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
	public class GetReleasedApplicationByIdCommand extends BaseCommand
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
		public function GetReleasedApplicationByIdCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.rapp.application.GetReleasedApplicationByIdCommand",
				"ApplicationService.getReleasedApplicationById"
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
			return service.getReleasedApplicationById(event.id) as AsyncToken;
		}
		
		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response();
			var rapp:ReleasedApplication = cache.synchronizeItem(item) as ReleasedApplication;
			if(rapp != null && releasedApplications.targetReleasedApplication.id == rapp.id)
			{
				releasedApplications.isTargetReleasedApplicationLast = true;
			}
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