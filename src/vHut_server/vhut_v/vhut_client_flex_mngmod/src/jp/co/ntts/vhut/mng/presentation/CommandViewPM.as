/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.presentation
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;

	import jp.co.ntts.vhut.config.VhutConfig;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.CommandOperation;
	import jp.co.ntts.vhut.entity.CommandStatus;
	import jp.co.ntts.vhut.mng.domain.CommandOperationItem;
	import jp.co.ntts.vhut.mng.domain.CommandStatusItem;
	import jp.co.ntts.vhut.mng.domain.Commands;

	import mx.collections.ArrayCollection;

	/**
	 * CommandViewのPM
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
	public class CommandViewPM extends EventDispatcher implements IViewPM
	{

		[Inject]
		[Bindable]
		/** コマンドデータ */
		public var commands:Commands;

		[Bindable]
		/** コマンド操作名の選択用リスト */
		public var commandOperationItemList:ArrayCollection;

		[Bindable]
		/** コマンド状態名の選択用リスト */
		public var commandStatusItemList:ArrayCollection;

		private var autoUpdateTimer:Timer;

		public function CommandViewPM(target:IEventDispatcher=null)
		{
			super(target);

			commandOperationItemList = CommandOperationItem.createItemList();
			updateCommandOperationItemList();

			commandStatusItemList = CommandStatusItem.createItemList();
			updateCommandStatusItemList();
		}

		private function initialize():void
		{
			updateAutoUpdateTimer();
			updateCommands();
		}

		//-------------------------------------------
		// implements IViewPM
		//-------------------------------------------

		public function set isActivate(value:Boolean):void
		{
			if (_isActivate == value) return;
			_isActivate = value;
			initialize();
		}
		public function get isActivate():Boolean
		{
			return _isActivate;
		}
		private var _isActivate:Boolean = false;


		//-------------------------------------------
		// Server Access
		//-------------------------------------------

		/** コマンドを更新する */
		public function updateCommands():void
		{
			if (!isActivate) return;
			commands.operations = commandOperations;
			commands.statuses = commandStatuses;
			commands.keyword = keyword;
			commands.updateCommandDtos();
		}

		//-------------------------------------------
		// Timer
		//-------------------------------------------

		private function updateAutoUpdateTimer():void
		{
			if (!autoUpdateTimer)
			{
				autoUpdateTimer = new Timer(VhutConfig.COMMAND_INTERVAL);
				autoUpdateTimer.addEventListener(TimerEvent.TIMER
					, function(event:TimerEvent):void
					{
						updateCommands();
					}
				);
			}
			if (isActivate)
			{
				autoUpdateTimer.start();
			}
			else
			{
				autoUpdateTimer.stop();
			}
		}

		//-------------------------------------------
		// CommandOperation
		//-------------------------------------------

		/** 検索対象の操作名リスト */
		private function get commandOperations():Array
		{
			var items:Array = new Array()
			for each (var item:CommandOperationItem in commandOperationItemList)
			{
				if (item.selected)
				{
					items.push(item.operation);
				}
			}
			return items;
		}

		/** 検索対象の操作名リストを更新する */
		private function updateCommandOperationItemList():void
		{
			for each (var item:CommandOperationItem in commandOperationItemList)
			{
				if (!isIncludeSyncCommand)
				{
					switch (item.operation.name)
					{
						case CommandOperation.GET_ALL_CLUSTERS.name:
						case CommandOperation.GET_ALL_DATA_STORAGES.name:
						case CommandOperation.GET_ALL_USERS.name:
						case CommandOperation.GET_HOSTS_BY_CLUSTER_ID.name:
						case CommandOperation.GET_NETWORKS_BY_CLUSTER_ID.name:
						case CommandOperation.GET_TEMPLATES_BY_CLUSTER_ID.name:
						case CommandOperation.GET_VMS_BY_CLUSTER_ID.name:
							item.selected = false;
							break;
						default:
							item.selected = true;
					}
				}
				else
				{
					item.selected = true;
				}
			}
		}

		/** 検索対象の操作名を全選択 */
		public function selectAllCommandOperations():void
		{
			for each (var item:CommandOperationItem in commandOperationItemList)
			{
				item.selected = true;
			}
		}

		[Bindable]
		/** 同期系のコマンドも検索対象に含める */
		public function set isIncludeSyncCommand(value:Boolean):void
		{
			if (_isIncludeSyncCommand == value) return;
			_isIncludeSyncCommand = value;
			updateCommandOperationItemList();
		}
		public function get isIncludeSyncCommand():Boolean
		{
			return _isIncludeSyncCommand;
		}
		private var _isIncludeSyncCommand:Boolean = true;

		//-------------------------------------------
		// CommandStatus
		//-------------------------------------------

		/** 検索対象の状態名リスト */
		private function get commandStatuses():Array
		{
			var items:Array = new Array()
			for each (var item:CommandStatusItem in commandStatusItemList)
			{
				if (item.selected)
				{
					items.push(item.status);
				}
			}
			return items;
		}

		/** 検索対象の操作名リストを更新する */
		private function updateCommandStatusItemList():void
		{
			for each (var item:CommandStatusItem in commandStatusItemList)
			{
				if (!isIncludeSuccessCommand)
				{
					switch (item.status.name)
					{
						case CommandStatus.SUCCESS.name:
							item.selected = false;
							break;
						default:
							item.selected = true;
					}
				}
				else
				{
					item.selected = true;
				}
			}
		}

		/** 検索対象の状態名を全選択 */
		public function selectAllCommandStatuses():void
		{
			for each (var item:CommandStatusItem in commandStatusItemList)
			{
				item.selected = true;
			}
		}

		[Bindable]
		/** 同期系のコマンドも検索対象に含める */
		public function set isIncludeSuccessCommand(value:Boolean):void
		{
			if (_isIncludeSuccessCommand == value) return;
			_isIncludeSuccessCommand = value;
			updateCommandStatusItemList();
		}
		public function get isIncludeSuccessCommand():Boolean
		{
			return _isIncludeSuccessCommand;
		}
		private var _isIncludeSuccessCommand:Boolean = true;


		//----------------------------------------
		// keyword
		//----------------------------------------

		[Bindable]
		public function set keyword(value:String):void
		{
			if (_keyword == value) return;

			if (value == "")
			{
				_keyword = null;
			}
			else
			{
				_keyword = value;
			}
		}
		public function get keyword():String
		{
			return _keyword;
		}
		private var _keyword:String;

	}
}