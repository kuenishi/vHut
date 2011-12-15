/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.app.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.app.ApplicationEvent;
	import jp.co.ntts.vhut.app.TermEvent;
	import jp.co.ntts.vhut.app.application.UpdateAndCheckAppCommandsTask;
	import jp.co.ntts.vhut.comp.va.domain.IVaDomain;
	import jp.co.ntts.vhut.config.VhutConfig;
	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationStatus;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.Command;
	import jp.co.ntts.vhut.entity.Term;
	import jp.co.ntts.vhut.rapp.domain.ReleasedApplications;
	import jp.co.ntts.vhut.util.CollectionUtil;
	import jp.co.ntts.vhut.util.TermUtil;
	import jp.co.ntts.vhut.util.VhutTimer;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	import org.spicefactory.lib.reflect.Property;
	import org.spicefactory.lib.task.Task;
	import org.spicefactory.lib.task.enum.TaskState;
	import org.spicefactory.lib.task.events.TaskEvent;

	[Event(name="getAllApp", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getAppById", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getTermListByAppId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getAvailableTermListByAppId", type="jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent")]
	[Event(name="getCommandListByAppId", type="jp.co.ntts.core.GetByEvent")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents(names="getAllApp, getAppById, getCommandListByAppId, getTermListByAppId, getAvailableTermListByAppId")]
	/**
	 * アプリケーションリストのドメインモデル.
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
	public class Applications extends EventDispatcher
	{
		/** アプリケーションリストが変更されました. */
//		public static const CHANGE_APPS:String="changeApps";
		/** コマンドが変更されました. */
		public static const CHANGE_COMMANDS:String = "changeCommands";
		/** アプリケーションが選択されました. */
		public static const SELECT_APP:String="selectApp";
		/** アプリケーション要素が選択されました. */
		public static const SELECT_APP_ELEMENT:String="selectAppElement";
		/** アプリケーションの起動可能期間が変更されました. */
		public static const CHANGE_TERMS:String="changeTerms";
		/** アプリケーションの予約可能期間が変更されました. */
		public static const CHANGE_AVAILABLE_TERMS:String="changeAvailableTerms";

		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_APPLICATIONS:Array = ["name"];


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Applications
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/** Applicationのリスト */
		public function set applications(value:IList):void
		{
			_apps = new ArrayCollection(value.toArray());
			_apps.filterFunction = applicationsFilter;
			_apps.refresh();
			updateTargetApplication(true);
		}
		public function get applications():IList
		{
			return _apps;
		}
		private var _apps:ArrayCollection = new ArrayCollection();

		/** ベーステンプレートのリストをサーバから取得する. */
		public function updateApplications():void
		{
			dispatchEvent(GetAllEvent.newGetAllApplicationEvent());
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter Applications
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setApplicationsfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_applicationsFilterKeywords = keywords;
			}
			else
			{
				_applicationsFilterKeywords = new Array();
			}

			if(fields)
			{
				_applicationsFilterFields = fields;
			}
			else
			{
				_applicationsFilterFields = FILTER_FIELD_APPLICATIONS;
			}

			_apps.refresh();

			updateTargetApplication();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearApplicationsFilter():void
		{
			setApplicationsfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get applicationsFilterFields():Array
		{
			return _applicationsFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get applicationsFilterKeywords():Array
		{
			return _applicationsFilterKeywords;
		}

		protected var _applicationsFilterFields:Array = new Array();
		protected var _applicationsFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function applicationsFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, applicationsFilterFields, applicationsFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplication
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("targetApplicationChanged")]
		/**
		 * 選択中のアプリケーションデータ.
		 */
		public function set targetApplication(value:Application):void
		{
			if (_targetApplication == value)
				return;

			isTargetApplicationLast = false;
			_targetApplication=value;
			targetApplicationElement = null;
			updateTargetApplication();

			if (_targetApplicationStatusWacher)
			{
				_targetApplicationStatusWacher.unwatch();
			}
			_targetApplicationStatusWacher = BindingUtils.bindSetter(
				function(value:Object):void
				{
					updateTargetApplicationStatusUpdateTimer();
				}
				, _targetApplication
				, "status");

			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		public function get targetApplication():Application
		{
			return _targetApplication;
		}
		private var _targetApplication:Application;

		[Bindable]
		/** targetApplicationが最新である. */
		public function set isTargetApplicationLast(value:Boolean):void
		{
			if(_isTargetApplicationLast == value)
				return;

			_isTargetApplicationLast = value;
			updateTargetApplicationStatusUpdateTimer();

//			if (_isTargetApplicationLast)
				dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetApplication));
		}
		public function get isTargetApplicationLast():Boolean
		{
			return _isTargetApplicationLast;
		}
		private var _isTargetApplicationLast:Boolean = false;

		/**
		 * ターゲットのアプリケーションの詳細をサーバから取得してデータを更新します.
		 * @param force 強制アップデート
		 */
		public function updateTargetApplication(force:Boolean = false):void
		{
			_isTargetApplicationCommandsLast = false;

			if(_targetApplication)
			{
				_targetApplication = getApplicationById(_targetApplication.id);
			}

			if(_targetApplication)
			{
				_updateTargetApplication(force);
				updateTargetApplicationTermList();
			}
			else
			{
				isTargetApplicationLast = false;
				targetApplicationCommands = new ArrayCollection();
				_targetApplication = null;
				targetApplicationElement = null;
			}

			dispatchEvent(new Event("targetApplicationChanged"));
		}

		protected function _updateTargetApplication(force:Boolean = false):void
		{
			if(force || !isTargetApplicationLast)
			{
				isTargetApplicationLast = false;
				targetApplicationCommands = new ArrayCollection();

				if (!_updateAndCheckAppCommandsTask)
				{
					_updateAndCheckAppCommandsTask = new UpdateAndCheckAppCommandsTask(this);
					_updateAndCheckAppCommandsTask.addEventListener(TaskEvent.COMPLETE,
						function(event:TaskEvent):void
						{
							if(targetApplication)
								dispatchEvent(GetByIdEvent.newGetApplication(targetApplication.id));
						}
					);
				}
				if (_updateAndCheckAppCommandsTask.state != TaskState.ACTIVE)
					_updateAndCheckAppCommandsTask.start();
			}
		}

		public function updateTargetApplicationStatus():void
		{
			_updateTargetApplication(true);
		}

		protected function updateTargetApplicationStatusUpdateTimer():void
		{
			if (targetApplication && targetApplication.status)
			{
				switch (targetApplication.status.name)
				{
					case ApplicationStatus.CREATING.name:
					case ApplicationStatus.DELETING.name:
					case ApplicationStatus.RELEASING.name:
					case ApplicationStatus.UPDATING.name:
						_targetApplicationStatusUpdateTimer.start();
						break;
					default:
						_targetApplicationStatusUpdateTimer.stop();
						break;
				}
			}
			else
			{
				_targetApplicationStatusUpdateTimer.stop();
			}
		}

		/**
		 * ID指定でアプリケーションを取得します.
		 * @param id ID
		 * @return アプリケーション
		 */
		public function getApplicationById(id:Number):Application
		{
			if(applications)
			{
				for each (var application:Application in applications)
				{
					if(application.id == id)
						return application;
				}
			}
			return null;
		}

		/** 選択されたアプリケーションを更新するタスク */
		private var _updateAndCheckAppCommandsTask:Task;

		private var _targetApplicationStatusWacher:ChangeWatcher;

		private var _targetApplicationStatusUpdateTimer:VhutTimer = new VhutTimer(VhutConfig.APP_INTERVAL, updateTargetApplicationStatus, this);

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplicationElement vm/securityGroup
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("selectAppElement")]
		/**
		 * 選択中のアプリケーション要素.
		 * VMとSecurityGroupのどちらか。
		 */
		public function get targetApplicationElement():Object
		{
			return _targetApplicationElement;
		}
		public function set targetApplicationElement(value:Object):void
		{
			_targetApplicationElement=value;
			_targetApplicationVm = _targetApplicationElement as ApplicationVm;
			_targetApplicationSecurityGroup = _targetApplicationElement as ApplicationSecurityGroup;
			dispatchEvent(new Event(SELECT_APP_ELEMENT));
		}
		private var _targetApplicationElement:Object;

		[Bindable("selectAppElement")]
		/**
		 * ターゲットのアプリケーション内で選択されているApplicationVm
		 */
		public function get targetApplicationVm():ApplicationVm
		{
			return _targetApplicationVm;
		}
		private var _targetApplicationVm:ApplicationVm;

		[Bindable("selectAppElement")]
		/**
		 * ターゲットのアプリケーション内で選択されているSecurityGroup
		 */
		public function get targetApplicationSecurityGroup():ApplicationSecurityGroup
		{
			return _targetApplicationSecurityGroup;
		}
		private var _targetApplicationSecurityGroup:ApplicationSecurityGroup;

		public function updateVmStatus(vm:ApplicationVm):void
		{
			//TODO; 選択中のアプリケーションのアプリケーションVMを見つけ出して更新する処理を書く。
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplicationCommands
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeCommands")]
		/** targetApplicationCommandsが最新である. */
		public function get isTargetApplicationCommandsLast():Boolean
		{
			return _isTargetApplicationCommandsLast;
		}
		private var _isTargetApplicationCommandsLast:Boolean = false;

		[Bindable("changeCommands")]
		/** アプリケーションに関連するコマンドのリスト. */
		public function get targetApplicationCommands():IList
		{
			return _targetApplicationCommands;
		}
		public function set targetApplicationCommands(items:IList):void
		{
			_targetApplicationCommands = items;
			updateNestedTargetApplicationCommands();
			_isTargetApplicationCommandsLast = true;
			dispatchEvent(new Event(CHANGE_COMMANDS));
		}
		private var _targetApplicationCommands:IList = new ArrayCollection();

		[Bindable("changeCommands")]
		/** 二重配列形式に変換されたコマンドのリスト. */
		public function get nestedTargetApplicationCommands():ArrayCollection
		{
			return _nestedTargetApplicationCommands;
		}
		private var _nestedTargetApplicationCommands:ArrayCollection;

		[Bindable("changeCommands")]
		/** アプリケーションに関連するコマンドがすべて. */
		public function get isTargetApplicationCommandsFinished():Boolean
		{
			if (!isTargetApplicationCommandsLast)
			{
				return false;
			}
			else
			{
				return (_targetApplicationCommands == null || _targetApplicationCommands.length == 0);
			}
		}

		/** アプリケーションに関連するコマンドを二重配列形式に変換します. */
		protected function updateNestedTargetApplicationCommands():void
		{
			var cmd:Command;
			_nestedTargetApplicationCommands = new ArrayCollection();
			//IDでマップを作る
			//dependのない物事にArrayListを作り親にセットする。
			var idmap:Dictionary = new Dictionary();
			var didmap:Dictionary = new Dictionary();
			var parray:Array = new Array();
			for each(cmd in _targetApplicationCommands)
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
				_nestedTargetApplicationCommands.addItem(new ArrayCollection(carray));
			}
		}

		/** アプリケーションに関連するコマンドの状態を更新します. */
		public function updateTargetApplicationCommands():void
		{
			if(targetApplication != null && targetApplication.id > 0)
			{
				dispatchEvent(GetByIdEvent.newGetCommandListByApplication(targetApplication.id));
			}
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplicationTerms
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeTerms")]
		/** 選択中アプリケーションの起動可能期間のリスト */
		public function get targetApplicationTermList():IList
		{
			return _targetApplicationTermList;
		}
		public function set targetApplicationTermList(list:IList):void
		{
			_targetApplicationTermList = list;
			dispatchEvent(new Event(CHANGE_TERMS));
		}
		private var _targetApplicationTermList:IList;

		[Bindable("changeAvailableTerms")]
		/** 選択中アプリケーションの予約取得可能期間のリスト */
		public function get targetApplicationAvailableTermList():IList
		{
			return _targetApplicationAvailableTermList
		}
		public function set targetApplicationAvailableTermList(list:IList):void
		{
			_targetApplicationAvailableTermList = list;
			dispatchEvent(new Event(CHANGE_AVAILABLE_TERMS));
		}
		private var _targetApplicationAvailableTermList:IList;

		public function syncTargetApplicationAvailableTermList(value:IList, startTime:Date, endTime:Date):void
		{
			var availableTermList:Array = _targetApplicationAvailableTermList.toArray()
			var fullTerm:Term = Term.newTerm(startTime, endTime);
			var termList:Array = value.toArray();

			var array0:Array = TermUtil.arrayOrItem(availableTermList, fullTerm);
			var array1:Array = TermUtil.arrayXorItem(array0, fullTerm);
			var array2:Array = TermUtil.arrayOrArray(array1, termList);

			_targetApplicationAvailableTermList = new ArrayCollection(array2);
			dispatchEvent(new Event(CHANGE_AVAILABLE_TERMS));
		}

		[Bindable("changeTerms")]
		/** 選択中アプリケーションのスケジュールを表す日情報のリスト */
		public function get targetApplicationDateList():IList
		{
			return null;
		}

		/**
		 * アプリケーションの起動可能期間を更新します.
		 */
		public function updateTargetApplicationTermList():void
		{
			targetApplicationTermList = new ArrayCollection();
			targetApplicationAvailableTermList = new ArrayCollection();
			if(targetApplication != null)
			{
				dispatchEvent(GetByIdEvent.newGetTermListByApplication(targetApplication.id));
			}
		}

		/**
		 * アプリケーションの予約取得可能期間を更新します.
		 * @param from 開始日時
		 * @param to 終了日時
		 */
		public function updateTargetApplicationAvailableTermList(from:Date, to:Date):void
		{
			if(targetApplication != null && from < to)
			{
				dispatchEvent(GetByIdWithTimeSpanEvent.newGetAvailableTermListByApplication(targetApplication.id, from, to));
			}
		}
	}
}