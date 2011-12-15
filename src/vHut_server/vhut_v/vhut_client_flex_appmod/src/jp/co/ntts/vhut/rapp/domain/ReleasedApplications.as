/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.rapp.domain
{
	import com.adobe.cairngorm.integration.data.IDataCache;

	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.Command;
	import jp.co.ntts.vhut.entity.ReleasedApplication;
	import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
	import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;
	import jp.co.ntts.vhut.rapp.ReleasedApplicationEvent;
	import jp.co.ntts.vhut.rapp.application.UpdateAndCheckRappCommandsTask;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	import org.spicefactory.lib.task.Task;
	import org.spicefactory.lib.task.enum.TaskState;
	import org.spicefactory.lib.task.events.TaskEvent;

	[Event(name="getRappById", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getCommandListByRappId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents(names="getRappById, selectRapp, getCommandListByRappId")]
	/**
	 * リリース済みコンテンツを格納するモデルクラスです.
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
	public class ReleasedApplications extends EventDispatcher
	{
		/** リリースドアプリケーションのリストが変更されました. */
		public static const CHANGE_RAPPS:String="changeRapps";
		/** リリースドアプリケーション要素が選択されました. */
		public static const SELECT_RAPP_ELEMENT:String="selectRappElement";
		/** コマンドが変更されました. */
		public static const CHANGE_COMMANDS:String = "changeCommands";


		[Inject(id="releasedApplicationDataCache")]
		/**
		 * データキャッシュ.
		 */
		public var cache:IDataCache;

		[Inject]
		[Bindable]
		public var applications:Applications;


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  ReleasedApplications
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Init]
		public function init():void
		{
			//選択中アプリケーションの情報とバインディング
			_applicationsWatcher = BindingUtils.bindProperty( this, "releasedApplications", applications, ["targetApplication", "releasedApplicationList"]);
		}
		private var _applicationsWatcher:ChangeWatcher;

		[Bindable]
		public function get releasedApplications():IList
		{
			return _rapps;
		}
		public function set releasedApplications(items:IList):void
		{
			cache.clear();
			_rapps = cache.synchronize(items);
			updateTargetReleasedApplication(true);
		}
		private var _rapps:IList=new ArrayCollection();

		public function updateReleasedApplications():void
		{
			applications.updateTargetApplication();
		}


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetReleasedApplication
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("updateRapp")]
		/**
		 * 選択中のアプリケーションデータ.
		 */
		public function set targetReleasedApplication(value:ReleasedApplication):void
		{
			if (_targetRapp == value)
				return;

			isTargetReleasedApplicationLast = false;
			_targetRapp=value;
			targetReleasedApplicationElement = null;
			updateTargetReleasedApplication();

			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		public function get targetReleasedApplication():ReleasedApplication
		{
			return _targetRapp;
		}
		private var _targetRapp:ReleasedApplication;

		[Bindable]
		/** targetApplicationが最新である. */
		public function set isTargetReleasedApplicationLast(value:Boolean):void
		{
			_isTargetReleasedApplicationLast = value;

			dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetReleasedApplication));
		}
		public function get isTargetReleasedApplicationLast():Boolean
		{
			return _isTargetReleasedApplicationLast;
		}
		private var _isTargetReleasedApplicationLast:Boolean = false;

		/**
		 * ターゲットのアプリケーションの詳細をサーバから取得してデータを更新します.
		 * <p>詳細を取得する前に関連するコマンドがすべてないかどうか確認します.<br>
		 * そのためにspicelibのtaskフレームワークをもちいています.
		 */
		public function updateTargetReleasedApplication(force:Boolean = false):void
		{
			_isTargetReleasedApplicationCommandsLast = false;

			if(_targetRapp)
			{
				_targetRapp = getRappById(_targetRapp.id);
			}

			if(_targetRapp)
			{
				_updateTargetReleasedApplication(force);
			}
			else
			{
				isTargetReleasedApplicationLast = false;
				targetReleasedApplicationCommands = new ArrayCollection();
				_targetRapp = null;
				targetReleasedApplicationElement = null;
			}

			dispatchEvent(new Event("updateRapp"));
		}


		protected function _updateTargetReleasedApplication(force:Boolean = false):void
		{

			if(force || !isTargetReleasedApplicationLast)
			{
				isTargetReleasedApplicationLast = false;
				targetReleasedApplicationCommands = new ArrayCollection();

				if (!_updateAndCheckRappCommandsTask)
				{
					_updateAndCheckRappCommandsTask = new UpdateAndCheckRappCommandsTask(this);
					_updateAndCheckRappCommandsTask.addEventListener(TaskEvent.COMPLETE,
						function(event:TaskEvent):void
						{
							if (targetReleasedApplication)
								dispatchEvent(GetByIdEvent.newGetReleasedApplication(targetReleasedApplication.id));
						}
					);
				}
				if (_updateAndCheckRappCommandsTask.state != TaskState.ACTIVE)
					_updateAndCheckRappCommandsTask.start();
			}
		}

		/**
		 * ID指定でリリースを取得します.
		 * @param id ID
		 * @return リリース
		 */
		public function getRappById(id:Number):ReleasedApplication
		{
			if(releasedApplications)
			{
				for each (var rapp:ReleasedApplication in releasedApplications)
				{
					if(rapp.id == id)
						return rapp;
				}
			}
			return null;
		}

		/** 選択されたアプリケーションを更新するタスク */
		private var _updateAndCheckRappCommandsTask:Task;

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetReleasedApplicationElement
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("selectRappElement")]
		/**
		 * 選択中のアプリケーション要素.
		 * VMとSecurityGroupのどちらか。
		 */
		public function get targetReleasedApplicationElement():Object
		{
			return _targetRappElement;
		}
		public function set targetReleasedApplicationElement(value:Object):void
		{
			_targetRappElement=value;
			_targetRappTemplate = _targetRappElement as ReleasedApplicationTemplate;
			_targetRappSecurityGroupTemplate = _targetRappElement as ReleasedApplicationSecurityGroupTemplate;
			dispatchEvent(new Event(SELECT_RAPP_ELEMENT));
		}
		private var _targetRappElement:Object;

		[Bindable("selectRappElement")]
		public function get targetReleasedApplicationTemplate():ReleasedApplicationTemplate
		{
			return _targetRappTemplate;
		}
		private var _targetRappTemplate:ReleasedApplicationTemplate;

		[Bindable("selectRappElement")]
		public function get targetReleasedApplicationSecurityGroupTemplate():ReleasedApplicationSecurityGroupTemplate
		{
			return _targetRappSecurityGroupTemplate;
		}
		private var _targetRappSecurityGroupTemplate:ReleasedApplicationSecurityGroupTemplate;


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetReleasedApplicationCommands
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeCommands")]
		/** targetReleasedApplicationCommandsが最新である. */
		public function get isTargetReleasedApplicationCommandsLast():Boolean
		{
			return _isTargetReleasedApplicationCommandsLast;
		}
		private var _isTargetReleasedApplicationCommandsLast:Boolean = false;

		[Bindable("changeCommands")]
		/** アプリケーションに関連するコマンドのリスト. */
		public function get targetReleasedApplicationCommands():IList
		{
			return _targetReleasedApplicationCommands;
		}
		public function set targetReleasedApplicationCommands(items:IList):void
		{
			_targetReleasedApplicationCommands = items;
			updateNestedTargetReleasedApplicationCommands();
			_isTargetReleasedApplicationCommandsLast = true;
			dispatchEvent(new Event(CHANGE_COMMANDS));
		}
		private var _targetReleasedApplicationCommands:IList = new ArrayCollection();

		[Bindable("changeCommands")]
		/** 二重配列形式に変換されたコマンドのリスト. */
		public function get nestedTargetReleasedApplicationCommands():ArrayCollection
		{
			return _nestedTargetReleasedApplicationCommands;
		}
		private var _nestedTargetReleasedApplicationCommands:ArrayCollection;

		[Bindable("changeCommands")]
		/** アプリケーションに関連するコマンドがすべて. */
		public function get isTargetReleasedApplicationCommandsFinished():Boolean
		{
			if (!isTargetReleasedApplicationCommandsLast)
			{
				return false;
			}
			else
			{
				return (_targetReleasedApplicationCommands == null || _targetReleasedApplicationCommands.length == 0);
			}
		}

		/** アプリケーションに関連するコマンドを二重配列形式に変換します. */
		protected function updateNestedTargetReleasedApplicationCommands():void
		{
			var cmd:Command;
			_nestedTargetReleasedApplicationCommands = new ArrayCollection();
			//IDでマップを作る
			//dependのない物事にArrayListを作り親にセットする。
			var idmap:Dictionary = new Dictionary();
			var didmap:Dictionary = new Dictionary();
			var parray:Array = new Array();
			for each(cmd in _targetReleasedApplicationCommands)
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
				_nestedTargetReleasedApplicationCommands.addItem(new ArrayCollection(carray));
			}
		}

		/** アプリケーションに関連するコマンドの状態を更新します. */
		public function updateTargetReleasedApplicationCommands():void
		{
			if(targetReleasedApplication != null && targetReleasedApplication.id > 0)
			{
				dispatchEvent(GetByIdEvent.newGetCommandListByReleasedApplication(targetReleasedApplication.id));
			}
		}
	}
}