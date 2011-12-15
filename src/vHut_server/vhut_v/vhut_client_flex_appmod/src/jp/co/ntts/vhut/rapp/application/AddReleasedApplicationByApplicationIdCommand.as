/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rapp.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;
	
	import jp.co.ntts.vhut.app.ApplicationEvent;
	import jp.co.ntts.vhut.core.AlertEvent;
	import jp.co.ntts.vhut.core.BaseCommand;
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
	 * リリースドアプリケーション追加.
	 * <p>アプリケーションのIDを受け取り、情報をDBに登録し、
	 * 作成のためのコマンドを登録する。
	 * 登録ができない場合はその理由を返す。
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
	public class AddReleasedApplicationByApplicationIdCommand extends BaseCommand
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
		public function AddReleasedApplicationByApplicationIdCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.rapp.application.AddReleasedApplicationByApplicationIdCommand",
				"ApplicationService.addReleasedApplicationByApplicationId"
			);
		}
		
		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:ApplicationEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.addReleasedApplicationByApplicationId(event.application.id) as AsyncToken;
		}
		
		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:Object):void
		{
			_response();
			releasedApplications.releasedApplications = cache.synchronize(new ArrayList([item]));
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