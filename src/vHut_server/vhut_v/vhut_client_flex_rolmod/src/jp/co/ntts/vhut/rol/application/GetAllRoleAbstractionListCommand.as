/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rol.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.rol.RolEvent;
	import jp.co.ntts.vhut.rol.domain.Roles;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * ロール概要一覧取得コマンド.
	 * <p></p>
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
	public class GetAllRoleAbstractionListCommand extends BaseCommand
	{
		[Inject]
		/**
		 * ロールの管理クラス.
		 */
		public var roles:Roles;

		[Inject(id="roleService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		[Inject(id="roleDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		/**
		 * コンストラクタ.
		 */
		public function GetAllRoleAbstractionListCommand()
		{
			super(
				"ROL",
				"jp.co.ntts.vhut.rol.application.GetAllRoleAbstractionListCommand",
				"RoleService.getAllRoleAbstractionList"
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
			return service.getAllRoleAbstractionList() as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(itmes:IList):void
		{
			_response(itmes);
			roles.roles = cache.synchronize(itmes);
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