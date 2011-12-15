/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.aig.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="getAllApp", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getAppById", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[ManagedEvents(names="getAppById, getAllApp")]
	/**
	 * アプリケーションの管理クラス
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
	public class Apps extends EventDispatcher
	{
		/** アプリケーションのリストが変更されました. */
		public static const CHANGE_APPS:String="changeApps";
		/** アプリケーションが選択されました. */
		public static const SELECT_APP:String="selectApp";
		/** アプリケーションが更新されました. */
		public static const UPDATE_APP:String="updateApp";
		/** アプリケーション要素が選択されました. */
		public static const SELECT_APP_ELEMENT:String="selectAppElement";

		public static const FIELD_APP:Array = ["name"];

		public function Apps(target:IEventDispatcher=null)
		{
			//TODO: implement function
			super(target);
		}


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Apps
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeApps")]
		/**
		 * Aiglicationのリスト
		 */
		public function get apps():IList
		{
			return _apps;
		}
		public function set apps(value:IList):void
		{
			_apps = new ArrayCollection(value.toArray());

			_apps.filterFunction = appsFilter;
			_apps.refresh();

			updateTargetApp();

			dispatchEvent(new Event(CHANGE_APPS));
		}
		private var _apps:ArrayCollection=new ArrayCollection();

		public function updateApps():void
		{
			_apps.removeAll();
			dispatchEvent(GetAllEvent.newGetAllApplicationEvent());
		}

		public function selectApplicationById(id:Number):Application
		{
			targetAppId = id;
			return targetApp;
		}

		public function getAppsIndexByAppId(id:Number):int
		{
			for (var i:int=0; i<apps.length; i++)
			{
				var app:Application = apps.getItemAt(i) as Application;
				if(app.id == id) return i;
			}
			return -1;
		}

		public function getAppByAppId(id:Number):Application
		{
			for each( var app:Application in apps)
			{
				if(app.id == id) return app;
			}
			return null;
		}

		public function getAppNameByAppId(id:Number):String
		{
			for each( var app:Application in apps)
			{
				if(app.id == id) return app.name;
			}
			return "";
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter Apps
		//
		///////////////////////////////////////////////////////////////////////////////////////

		public function setAppsfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_appsFilterKeywords = keywords;
			}
			else
			{
				_appsFilterKeywords = new Array();
			}

			if(fields)
			{
				_appsFilterFields = fields;
			}
			else
			{
				_appsFilterFields = FIELD_APP;
			}

			_apps.refresh();

			updateTargetApp();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		public function clearAppsFilter():void
		{
			setAppsfilter();
		}

		[Bindable("filterParamsChanged")]
		public function get appsFilterFields():Array
		{
			return _appsFilterFields;
		}

		[Bindable("filterParamsChanged")]
		public function get appsFilterKeywords():Array
		{
			return _appsFilterKeywords;
		}

		protected var _appsFilterFields:Array = new Array();
		protected var _appsFilterKeywords:Array = new Array();

		protected function appsFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, appsFilterFields, appsFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApp
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("updateApp")]
		/** targetAigが最新である. */
		public function get isTargetAppLast():Boolean
		{
			return _isTargetAppLast;
		}
		public function set isTargetAppLast(value:Boolean):void
		{
			_isTargetAppLast = value;
			dispatchEvent(new Event(UPDATE_APP));
		}
		private var _isTargetAppLast:Boolean = false;

		[Bindable("updateApp")]
		/**
		 * 選択中のアプリケーションデータ.
		 */
		public function get targetApp():Application
		{
			return _targetApp;
		}
		public function set targetApp(value:Application):void
		{
			if (_targetApp != value)
			{
				_targetApp=value;
				updateTargetApp();
			}
		}
		private var _targetApp:Application;

		public function set targetAppId(value:Number):void
		{
			_targetAppId = value;
			updateTargetApp();
		}
		public function get targetAppId():Number
		{
			if(targetApp)
			{
				return targetApp.id;
			}
			else
			{
				return _targetAppId;
			}
		}
		protected var _targetAppId:Number = -1;

//		[Bindable("updateApp")]
//		/**
//		 * ターゲットのアプリケーションインスタンスグループがサーバに登録されています.
//		 */
//		public function get isTargetAppRegistered():Boolean
//		{
//			return (_targetApp != null && _targetApp.id != 0);
//		}

		/**
		 * ターゲットのアプリケーションの詳細をサーバから取得してデータを更新します.
		 * <p>詳細を取得する前に関連するコマンドがすべてないかどうか確認します.<br>
		 * そのためにspicelibのtaskフレームワークをもちいています.
		 */
		public function updateTargetApp():void
		{
			if(_targetAppId >= 0)
			{
				_targetApp = getAppByAppId(_targetAppId);
			}

			if (_targetApp && apps.getItemIndex(_targetApp) >= 0)
			{
				_isTargetAppLast = false;
				dispatchEvent(GetByIdEvent.newGetApplication(targetApp.id));
				_targetAppId = -1;
			}
			else
			{
				_isTargetAppLast = true;
				_targetApp = null;
			}
			dispatchEvent(new Event(UPDATE_APP));
		}

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
	}
}