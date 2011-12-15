/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.application
{
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.core.SearchCommandEvent;
	import jp.co.ntts.vhut.dto.CommandDto;
	import jp.co.ntts.vhut.mng.domain.Commands;

	import mx.collections.IList;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;

	/**
	 *
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class SearchCommandListCommand extends BaseCommand
	{

		[Inject]
		/**
		 * コマンドの管理クラス.
		 */
		public var commands:Commands;

		[Inject(id="managementService")]
		/**
		 * リモートオブジェクト.
		 */
		public var service:RemoteObject;

		public function SearchCommandListCommand()
		{
			super(
				"MNG",
				"jp.co.ntts.vhut.mng.application.SearchCommandListCommand",
				"ManagementService.searchCommandList"
			);
		}

		private var commandDto:CommandDto;

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		public function execute(event:SearchCommandEvent):AsyncToken
		{
			owner = event.target as ICommandOwner;
			_request();
			return service.searchCommandList(
				event.keyword
				, event.startTime
				, event.endTime
				, event.operations
				, event.statuses
				) as AsyncToken;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function result(items:IList):void
		{
			_response(items);
			commands.commandDtos = items;
		}

		/**
		 * Spice Frameworkからメソッド異常終了時に実行されます.
		 * @param event
		 * @return
		 */
		public function error(fault:FaultEvent):void
		{
			_error(fault);
		}
	}
}