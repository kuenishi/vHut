/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.tmp.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.tmp.domain.BaseTemplates;
	import jp.co.ntts.vhut.tmp.domain.EditingBaseTemplate;
	import jp.co.ntts.vhut.tmp.presentation.wiz.TmpWizardPM;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;

	import spark.events.IndexChangeEvent;

	/**
	 * ユーザ管理モジュールのメインのPM.
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
	 * $Date: 2010-11-11 21:34:32 +0900 (木, 11 11 2010) $
	 * $Revision: 576 $
	 * $Author: NTT Software Corporation. $ss
	 */
	public class TmpPM extends ModulePMBase
	{

		public function TmpPM():void
		{
		}

		override public function get moduleName():String
		{
			return ModuleName.TMP;
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
			updateAll();
		}

		override protected function onModuleEnter():void
		{
			updateAll();
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
		// BaseTemplates
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set baseTemplates(value:BaseTemplates):void
		{
			if (_baseTemplates == value)
				return;

			if (_baseTemplates)
			{
				_baseTemplates.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_baseTemplates.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}

			_baseTemplates = value;
			_baseTemplates.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_baseTemplates.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
//			if (_baseTemplatesIsTargetLastWatcher)
//				_baseTemplatesIsTargetLastWatcher.unwatch();
//			_baseTemplatesIsTargetLastWatcher = BindingUtils.bindSetter(baseTemplatesIsTargetLastBindingHandler, baseTemplates, "isTargetBaseTemplateLast");
			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get baseTemplates():BaseTemplates
		{
			return _baseTemplates;
		}
		private var _baseTemplates:BaseTemplates;

		protected function changeTargetItemEventHandler(event:ChangeTargetItemEvent):void
		{
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		protected function updateTargetItemEventHandler(event:UpdateTargetItemEvent):void
		{
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

//		private var _baseTemplatesIsTargetLastWatcher:ChangeWatcher;
//
//		private function baseTemplatesIsTargetLastBindingHandler(value:Boolean):void
//		{
//			dispatchEvent(new Event("buttonVisibleChanged"));
//		}

		//-------------------------------------------------
		// View
		//-------------------------------------------------

		/** 表示（リスト+詳細）を最新化します. */
		public function updateAll():void
		{
			baseTemplates.updateBaseTemplates();
		}

		/** 表示（詳細のみ）を最新化します. */
		public function updateTarget():void
		{
			baseTemplates.updateTargetBaseTemplate(true);
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return baseTemplates && baseTemplates.targetBaseTemplate;
		}

		public function selectBaseTemplate(baseTemplate:BaseTemplate):void
		{
			baseTemplates.targetBaseTemplate = baseTemplate;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		public function selectBaseTemplateHandler(event:IndexChangeEvent):void
		{
			selectBaseTemplate(baseTemplates.baseTemplates.getItemAt(event.newIndex) as BaseTemplate);
		}

		//-------------------------------------------------
		// Wizard
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public var tmpWizardPM:TmpWizardPM;

		[Inject]
		[Bindable]
		public var editingBaseTemplate:EditingBaseTemplate;

		[Bindable]
		public var isTmpWizardOpen:Boolean = false;

		public function launchTmpWizardToAdd():void
		{
			tmpWizardPM.mode = TmpWizardPM.MODE_ADD;
			isTmpWizardOpen = true;
		}

		public function launchTmpWizardToEdit():void
		{
			tmpWizardPM.mode = TmpWizardPM.MODE_EDIT;
			isTmpWizardOpen = true;
		}

		public function launchTmpWizardToCopy():void
		{
			tmpWizardPM.mode = TmpWizardPM.MODE_COPY;
			isTmpWizardOpen = true;
		}

		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * ユーザを削除します.
		 */
		public function deleteUsr():void
		{
			if(baseTemplates.targetBaseTemplate != null)
			{
				VhutAlert.show(resourceManager.getString('TMPUI', 'alert.message.delete')
					, resourceManager.getString('APIUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL
					, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								baseTemplates.deleteTargetBaseTemplate();
								break;
						}
					}
				);
			}
		}

		//-------------------------------------------------
		// Search
		//-------------------------------------------------

		public function searchBaseTemplates(event:SearchEvent):void
		{
			baseTemplates.setBaseTemplatesfilter(event.keywords);
		}

		//-------------------------------------------------
		// Enable
		//-------------------------------------------------

		[Bindable("buttonEnabledChanged")]
		/** 更新ボタンが有効 */
		public function get isUpdateButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** 編集ボタンが有効 */
		public function get isEditButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** 作成ボタンが有効 */
		public function get isCreateButtonEnabled():Boolean
		{
			return true;
		}

		[Bindable("buttonEnabledChanged")]
		/** 複製ボタンが有効 */
		public function get isCopyButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** 削除ボタンが有効 */
		public function get isDeleteButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		//-------------------------------------------------
		// Visible
		//-------------------------------------------------

		[Bindable("buttonVisibleChanged")]
		/** 更新ボタンが可視 */
		public function get isUpdateButtonVisible():Boolean
		{
			return true;
		}

		[Bindable("buttonVisibleChanged")]
		/** 編集ボタンが可視 */
		public function get isEditButtonVisible():Boolean
		{
			return validateRight(Right.UPDATE_SYS_TEMPLATE);
		}

		[Bindable("buttonVisibleChanged")]
		/** 作成ボタンが可視 */
		public function get isCreateButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_SYS_TEMPLATE);
		}

		[Bindable("buttonVisibleChanged")]
		/** 複製ボタンが可視 */
		public function get isCopyButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_SYS_TEMPLATE);
		}

		[Bindable("buttonVisibleChanged")]
		/** 削除ボタンが可視 */
		public function get isDeleteButtonVisible():Boolean
		{
			return validateRight(Right.DELETE_SYS_TEMPLATE);
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