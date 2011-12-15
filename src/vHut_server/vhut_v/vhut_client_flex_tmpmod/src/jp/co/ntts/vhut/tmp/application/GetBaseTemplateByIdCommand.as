/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.tmp.application
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.tmp.domain.BaseTemplates;

	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 * ベーステンプレート概要一覧取得.
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
	public class GetBaseTemplateByIdCommand extends BaseCommand
	{
		[Inject]
		/**
		 * 概要情報の管理クラス.
		 */
		public var baseTempaltes:BaseTemplates;

		[Inject(id="baseTemplateService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		[Inject(id="baseTemplateDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		/**
		 * コンストラクタ.
		 */
		public function GetBaseTemplateByIdCommand()
		{
			super(
				"TMP",
				"jp.co.ntts.vhut.tmp.application.GetBaseTemplateByIdCommand",
				"BaseTemplateService.getBaseTemplateById"
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
			_request(event.id);
			return service.getBaseTemplateById(event.id) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(item:BaseTemplate):void
		{
			_response(item);
			cache.synchronizeItem(item);
			baseTempaltes.isTargetBaseTemplateLast = true;
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