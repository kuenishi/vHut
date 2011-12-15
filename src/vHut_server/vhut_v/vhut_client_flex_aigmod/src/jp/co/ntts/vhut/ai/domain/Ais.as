/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.ai.domain
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.ai.application.UpdateAndCheckAiCommandsTask;
	import jp.co.ntts.vhut.aig.domain.Aigs;
	import jp.co.ntts.vhut.config.VhutConfig;
	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationInstanceStatus;
	import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
	import jp.co.ntts.vhut.entity.Command;
	import jp.co.ntts.vhut.util.CollectionUtil;
	import jp.co.ntts.vhut.util.VhutTimer;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	import org.spicefactory.lib.task.Task;
	import org.spicefactory.lib.task.enum.TaskState;
	import org.spicefactory.lib.task.events.TaskEvent;

	[Event(name="selectAi", type="jp.co.ntts.vhut.Ai.ReleasedApplicationEvent")]
	[Event(name="getAiById", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getAiListByAigId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getCommandListByAiId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents(names="getAiById, selectAi, getAiListByAigId, getCommandListByAiId")]
	/**
	 * アプリケーションインスタンスの管理クラス
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
	public class Ais extends EventDispatcher
	{

		/** リリースドアプリケーションのリストが変更されました. */
		public static const CHANGE_AIS:String="changeAis";
		/** リリースドアプリケーションの詳細が変更されました. */
		public static const UPDATE_AI:String="updateAi";
		/** リリースドアプリケーション要素が選択されました. */
		public static const SELECT_AI_ELEMENT:String="selectAiElement";
		/** コマンドが変更されました. */
		public static const CHANGE_COMMANDS:String = "changeCommands";

		/** AIリストのフィルタリングに用いる変数名のリスト */
		public static const FIELDS_AI:Array = ["ownerAccount", "ownerFullName"];

		public function Ais(target:IEventDispatcher=null)
		{
			//TODO: implement function
			super(target);
		}


//		[Inject(id="aiDataCache")]
//		/**
//		 * データキャッシュ.
//		 */
//		public var cache:IDataCache;

		[Bindable]
		[Inject]
		public var aigs:Aigs;


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  ReleasedApplications
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Init]
		public function init():void
		{
			//選択中アプリケーションの情報とバインディング
//			_aigsWatcher = BindingUtils.bindProperty( this, "ais", aigs, ["targetAig", "applicationInstanceList"]);
			_aigsWatcher = BindingUtils.bindSetter(function(obj:Object):void
				{
					var aig:ApplicationInstanceGroup = obj as ApplicationInstanceGroup;
					if(aig != null && _targetAig != aig)
					{
						_targetAig = aig;
						dispatchEvent(GetByIdEvent.newGetAiListByAig(aig.id));
					}
				}, aigs, "targetAig" );
		}
		private var _aigsWatcher:ChangeWatcher;
		private var _targetAig:ApplicationInstanceGroup;

		[Bindable("changeAis")]
		public function get ais():IList
		{
			return _ais;
		}

		public function set ais(items:IList):void
		{
			_ais = new ArrayCollection(items.toArray());
			updateAis();
		}
		private var _ais:ArrayCollection = new ArrayCollection();

		protected function updateAis():void
		{
			if(_ais.getItemIndex(_targetAi) >= 0)
			{
				updateTargetAi();
			} else {
				targetAi = null;
			}
			dispatchEvent(new Event(CHANGE_AIS));
		}

		public function filterAi(keywords:Array, fields:Array=null):void
		{
			if(!fields)
			{
				fields = FIELDS_AI;
			}

			_ais.filterFunction = function(item:Object):Boolean
			{
				return CollectionUtil.multiFilter(item, fields, keywords);
			}
			_ais.refresh();

			updateAis();
		}


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplicationInstance
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("updateAi")]
		/** targetAiが最新である. */
		public function get isTargetAiLast():Boolean
		{
			return _isTargetAiLast;
		}
		public function set isTargetAiLast(value:Boolean):void
		{
			_isTargetAiLast = value;
			dispatchEvent(new Event(UPDATE_AI));
			dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetAi));
		}
		private var _isTargetAiLast:Boolean = false;

		[Bindable("updateAi")]
		/**
		 * 選択中のアプリケーションデータ.
		 */
		public function get targetAi():ApplicationInstance
		{
			return _targetAi;
		}
		public function set targetAi(value:ApplicationInstance):void
		{
			if (_targetAi == value) return;
			_targetAi = value;
			targetAiElement = null;
			updateTargetAi();

			if (_targetAiStatusWacher)
			{
				_targetAiStatusWacher.unwatch();
			}
			_targetAiStatusWacher = BindingUtils.bindSetter(
				function(value:Object):void
				{
					updateTargetAiStatusUpdateTimer();
				}
				, _targetAi
				, "status");

			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		private var _targetAi:ApplicationInstance;

		/**
		 * ターゲットのアプリケーションの詳細をサーバから取得してデータを更新します.
		 * <p>詳細を取得する前に関連するコマンドがすべてないかどうか確認します.<br>
		 * そのためにspicelibのtaskフレームワークをもちいています.
		 */
		public function updateTargetAi():void
		{
			_isTargetAiLast = false;
			_isTargetAiCommandsLast = false;
			if(!_updateAndCheckAiCommandsTask)
			{
				_updateAndCheckAiCommandsTask = new UpdateAndCheckAiCommandsTask(this);
				_updateAndCheckAiCommandsTask.addEventListener(TaskEvent.COMPLETE,
					function(event:TaskEvent):void
					{
						dispatchEvent(GetByIdEvent.newGetAi(targetAi.id));
					}
				);
			}
			if (_updateAndCheckAiCommandsTask.state != TaskState.ACTIVE)
				_updateAndCheckAiCommandsTask.start();
			dispatchEvent(new Event(UPDATE_AI));
		}

		public function updateTargetAiStatus():void
		{
			updateTargetAi();
		}

		protected function updateTargetAiStatusUpdateTimer():void
		{
			if (targetAi && targetAi.status)
			{
				switch (targetAi.status.name)
				{
					case ApplicationInstanceStatus.CREATING.name:
					case ApplicationInstanceStatus.DELETING.name:
					case ApplicationInstanceStatus.REBUILDING.name:
						_targetAiStatusUpdateTimer.start();
						break;
					default:
						_targetAiStatusUpdateTimer.stop();
						break;
				}
			}
			else
			{
				_targetAiStatusUpdateTimer.stop();
			}
		}

		/** 選択されたアプリケーションを更新するタスク */
		private var _updateAndCheckAiCommandsTask:Task;

		private var _targetAiStatusWacher:ChangeWatcher;

		private var _targetAiStatusUpdateTimer:VhutTimer = new VhutTimer(VhutConfig.AI_INTERVAL, updateTargetAiStatus, this);


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetAiElement
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("selectAiElement")]
		/**
		 * 選択中のアプリケーション要素.
		 * VMとSecurityGroupのどちらか。
		 */
		public function get targetAiElement():Object
		{
			return _targetAiElement;
		}
		public function set targetAiElement(value:Object):void
		{
			_targetAiElement=value;
			_targetAiVm = _targetAiElement as ApplicationInstanceVm;
			_targetAiSecurityGroup = _targetAiElement as ApplicationInstanceSecurityGroup;
			dispatchEvent(new Event(SELECT_AI_ELEMENT));
		}
		private var _targetAiElement:Object;

		[Bindable("selectAiElement")]
		public function get targetAiVm():ApplicationInstanceVm
		{
			return _targetAiVm;
		}
		private var _targetAiVm:ApplicationInstanceVm;

		[Bindable("selectAiElement")]
		public function get targetAiSecurityGroup():ApplicationInstanceSecurityGroup
		{
			return _targetAiSecurityGroup;
		}
		private var _targetAiSecurityGroup:ApplicationInstanceSecurityGroup;

		public function updateVmStatus(aivm:ApplicationInstanceVm):void
		{
			//TODO: VMの状態変更を実施
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetAiCommands
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeCommands")]
		/** targetAiCommandsが最新である. */
		public function get isTargetAiCommandsLast():Boolean
		{
			return _isTargetAiCommandsLast;
		}
		private var _isTargetAiCommandsLast:Boolean = false;

		[Bindable("changeCommands")]
		/** アプリケーションに関連するコマンドのリスト. */
		public function get targetAiCommands():IList
		{
			return _targetAiCommands;
		}
		public function set targetAiCommands(items:IList):void
		{
			_targetAiCommands = items;
			updateNestedTargetAiCommands();
			_isTargetAiCommandsLast = true;
			dispatchEvent(new Event(CHANGE_COMMANDS));
		}
		private var _targetAiCommands:IList = new ArrayCollection();

		[Bindable("changeCommands")]
		/** 二重配列形式に変換されたコマンドのリスト. */
		public function get nestedTargetAiCommands():ArrayCollection
		{
			return _nestedTargetAiCommands;
		}
		private var _nestedTargetAiCommands:ArrayCollection;

		[Bindable("changeCommands")]
		/** アプリケーションに関連するコマンドがすべて. */
		public function get isTargetAiCommandsFinished():Boolean
		{
			if (!isTargetAiCommandsLast)
			{
				return false;
			}
			else
			{
				return (_targetAiCommands == null || _targetAiCommands.length == 0);
			}
		}

		/** アプリケーションに関連するコマンドを二重配列形式に変換します. */
		protected function updateNestedTargetAiCommands():void
		{
			var cmd:Command;
			_nestedTargetAiCommands = new ArrayCollection();
			//IDでマップを作る
			//dependのない物事にArrayListを作り親にセットする。
			var idmap:Dictionary = new Dictionary();
			var didmap:Dictionary = new Dictionary();
			var parray:Array = new Array();
			for each(cmd in _targetAiCommands)
			{
				idmap[cmd.id] = cmd;
				didmap[cmd.dependingCommandId] = cmd;
			}

			for each(cmd in idmap)
			{
				if(idmap[cmd.dependingCommandId] == null)
				{
					parray.push([cmd]);
				}
			}
			//ArrayList毎にdependをたどってリストを完成させる
			for each(var carray:Array in parray)
			{
				while(true)
				{
					var headCmd:Command = carray[0] as Command;
					var nextCmd:Command = didmap[headCmd.id];
					if(nextCmd != null)
					{
						carray.unshift(nextCmd);
					}
					else
					{
						break;
					}
				}
				_nestedTargetAiCommands.addItem(new ArrayCollection(carray));
			}
		}

		/** アプリケーションに関連するコマンドの状態を更新します. */
		public function updateTargetAiCommands():void
		{
			if(targetAi != null && targetAi.id > 0)
			{
				dispatchEvent(GetByIdEvent.newGetCommandListByAi(targetAi.id));
			}
		}
	}
}