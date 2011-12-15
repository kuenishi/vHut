/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.usr.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.usr.domain.EditingVhutUser;
	import jp.co.ntts.vhut.usr.domain.EditingVhutUsers;
	import jp.co.ntts.vhut.usr.domain.VhutUsers;
	import jp.co.ntts.vhut.usr.presentation.addwiz.UsrAddWizardPM;
	import jp.co.ntts.vhut.usr.presentation.wiz.UsrWizardPM;

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
	public class UsrPM extends ModulePMBase
	{

		public function UsrPM():void
		{
		}

		override public function get moduleName():String
		{
			return ModuleName.USR;
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
		// VhutUsers
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set vhutUsers(value:VhutUsers):void
		{
			if (_vhutUsers == value)
				return;

			if (_vhutUsers)
			{
				_vhutUsers.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_vhutUsers.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}

			_vhutUsers = value;
			_vhutUsers.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_vhutUsers.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);

			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get vhutUsers():VhutUsers
		{
			return _vhutUsers;
		}
		private var _vhutUsers:VhutUsers;

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
		//-------------------------------------------------
		// View
		//-------------------------------------------------

		/** 表示（リスト+詳細）を最新化します. */
		public function updateAll():void
		{
			vhutUsers.updateVhutUsers();
		}

		/** 表示（詳細のみ）を最新化します. */
		public function updateTarget():void
		{
			vhutUsers.updateTargetVhutUser(true);
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return vhutUsers && vhutUsers.targetVhutUser;
		}

		public function selectUsr(usr:VhutUser):void
		{
			vhutUsers.targetVhutUser = usr;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		public function selectUsrHandler(event:IndexChangeEvent):void
		{
			selectUsr(vhutUsers.vhutUsers.getItemAt(event.newIndex) as VhutUser);
		}

		//-------------------------------------------------
		// Wizard
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public var usrWizardPM:UsrWizardPM;

		[Inject]
		[Bindable]
		public var usrAddWizardPM:UsrAddWizardPM;

		[Inject]
		[Bindable]
		public var editingVhutUsers:EditingVhutUsers;

		[Bindable]
		public var isUsrWizardOpen:Boolean = false;

		[Bindable]
		public var isUsrAddWizardOpen:Boolean = false;

		public function launchUsrWizardToEdit():void
		{
			usrWizardPM.prepare();
			isUsrWizardOpen = true;
		}

		public function launchUsrAddWizardToEdit():void
		{
			usrAddWizardPM.prepare();
			isUsrAddWizardOpen = true;
		}

		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * ユーザを削除します.
		 */
		public function deleteUsr():void
		{
			if(vhutUsers.targetVhutUser != null)
			{
				VhutAlert.show(resourceManager.getString('USRUI', 'alert.message.delete')
					, resourceManager.getString('APIUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL
					, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								vhutUsers.deleteTargetVhutUser();
								break;
						}
					}
				);
			}
		}

		//-------------------------------------------------
		// Search
		//-------------------------------------------------

		public function searchUsr(event:SearchEvent):void
		{
			vhutUsers.setVhutUsersfilter(event.keywords);
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
			if (isTargetMine)
			{
				return validateRight(Right.UPDATE_OWN_USER);
			}
			else
			{
				return validateRight(Right.UPDATE_ALL_USER);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 作成ボタンが可視 */
		public function get isCreateButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_ALL_USER);
		}

		[Bindable("buttonVisibleChanged")]
		/** 削除ボタンが可視 */
		public function get isDeleteButtonVisible():Boolean
		{
			return validateRight(Right.DELETE_ALL_USER);
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

		/**
		 * @return ターゲットの所有者が自分
		 *
		 */
		private function get isTargetMine():Boolean
		{
			if (!vhutUsers || !vhutUsers.targetVhutUser)
				return false;
			if (!session || !session.user)
				return false;

			return vhutUsers.targetVhutUser.id == session.user.id;
		}
	}
}