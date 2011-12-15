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
	import jp.co.ntts.vhut.test.domain.Applications;
	
	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * //TODO: UserModuleに移動
	 * 
	 * ユーザ関連アプリケーション概要一覧取得.
	 * <p>指定したユーザが所有するアプリケーションの概要情報をリスト形式で返す。</p>
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
	public class GetAllApplicationAbstractionListByUserIdCommand extends BaseCommand
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		public var applications:Applications;
		
		[Inject]
		public var cache:IDataCache;
		
		[Inject (id="applicationService")]
		public var service:RemoteObject;
		
		/**
		 * コンストラクタ.
		 */
		public function GetAllApplicationAbstractionListByUserIdCommand()
		{
			super(
				"APP",
				"jp.co.ntts.vhut.rapp.application.GetAllApplicationAbstractionListByUserIdCommand",
				"ApplicationService.getAllApplicationAbstractionListByUserIdCommand"
			);
		}
		
		public function execute(event:GetAllEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.getAllApplicationAbstractionList() as AsyncToken;
		}
		
		public function result(items:IList):void
		{
			_response();
			applications.addApplications(cache.synchronize(items));
		}
		
		public function error(fault:FaultEvent):void
		{
			_error(fault);
			dispatcher(new AlertEvent("Application 取得失敗"));
		}
	}
}