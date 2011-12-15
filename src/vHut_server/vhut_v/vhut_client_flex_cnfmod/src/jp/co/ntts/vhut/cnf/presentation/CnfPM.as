/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.cnf.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.cnf.domain.Configs;
	import jp.co.ntts.vhut.cnf.domain.Content;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.FlexEvent;

	/**
	 * アプリケーション管理モジュールのメインのPM.
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
	 * $Author: NTT Software Corporation. $ss
	 */
	public class CnfPM extends ModulePMBase
	{
		public static const CHANGE_CONTENTS:String = "changeContents";
		public static const CHANGE_TARGET_CONTENT:String = "changeTargetContent";

		[Inject]
		[Bindable]
		public var configs:Configs;

		public function CnfPM():void
		{

			_contents = new ArrayCollection();
			_contents.addItem(Content.newContent(0, resourceManager.getString('CNFUI', 'title.serviceConfig')));
			_contents.addItem(Content.newContent(1, resourceManager.getString('CNFUI', 'title.cloudConfig')));

			targetContent = _contents[0];
		}

		override public function get moduleName():String
		{
			return ModuleName.MNG;
		}

		[Init]
		/**
		 * フレームワークから初期化される際に呼ばれるイベントです.
		 */
		public function init():void
		{
		}

		/**
		 * メインモジュールがインスタンス完了した際に呼ばれるイベントです.
		 */
		public function onCreationComplete(event:FlexEvent):void
		{
			refresh();
		}

		override protected function onModuleEnter():void
		{
			refresh();
		}

		override protected function onModuleExit():void
		{

		}

		//-------------------------------------------------
		// Session
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set session(value:Session):void
		{
			if (_session == value)
				return;
			_session = value;
			if (_sessionUserWatcher)
				_sessionUserWatcher.unwatch();
			_sessionUserWatcher = BindingUtils.bindSetter(sessionUserBindingHandler, session, "user");
			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get session():Session
		{
			return _session;
		}
		private var _session:Session;

		private var _sessionUserWatcher:ChangeWatcher;

		private function sessionUserBindingHandler(value:VhutUser):void
		{
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		//-------------------------------------------------
		// View
		//-------------------------------------------------

		[Bindable("changeTargetContent")]
		public function set targetContent(value:Content):void
		{
			_targetContent = value;
			refresh();
			dispatchEvent(new Event(CHANGE_TARGET_CONTENT));
		}
		public function get targetContent():Content
		{
			return _targetContent;
		}
		private var _targetContent:Content;

		/** 表示を最新化します. */
		public function refresh():void
		{
			if(targetContent)
			{
				switch(targetContent.index)
				{
					case 0:
						if(configs)
							configs.updateServiceConfig();
						break;
					case 1:
						if(configs)
							configs.updateCloudConfig();
						break;
						break;
				}
			}
		}

		[Bindable("changeContents")]
		public function get contents():IList
		{
			return _contents;
		}
		private var _contents:ArrayCollection;


		//-------------------------------------------------
		// Enable
		//-------------------------------------------------

		[Bindable("buttonEnabledChanged")]
		/** 編集ボタンが有効 */
		public function get isEditButtonEnabled():Boolean
		{
			return true;
		}

		//-------------------------------------------------
		// Visible
		//-------------------------------------------------

		[Bindable("buttonVisibleChanged")]
		/** 編集ボタンが可視 */
		public function get isEditButtonVisible():Boolean
		{
			return validateRight(Right.UPDATE_SYS_CONFIGURATION);
		}

		/**
		 * 権利を検証します
		 * @param right 権利
		 * @return 持っている
		 *
		 */
		private function validateRight(right:Right):Boolean
		{
			if (!session || !session.user || !session.user.rightsMap)
				return false;

			return session.user.rightsMap[right];
		}
	}
}