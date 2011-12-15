/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rol.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.Role;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.rol.domain.EditingRole;
	import jp.co.ntts.vhut.rol.domain.Roles;
	import jp.co.ntts.vhut.rol.presentation.wiz.RolWizardPM;

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
	public class RolPM extends ModulePMBase
	{

		public function RolPM():void
		{
		}

		override public function get moduleName():String
		{
			return ModuleName.ROL;
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
		// Roles
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set roles(value:Roles):void
		{
			if (_roles == value)
				return;
			_roles = value;

			if (_roles)
			{
				_roles.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_roles.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}

			_roles = value;
			_roles.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_roles.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);

			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get roles():Roles
		{
			return _roles;
		}
		private var _roles:Roles;

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
			roles.updateRoles();
		}

		/** 表示（詳細のみ）を最新化します. */
		public function updateTarget():void
		{
			roles.updateTargetRole(true);
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return roles && roles.targetRole;
		}

		public function selectRole(role:Role):void
		{
			roles.targetRole = role;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		public function selectRoleHandler(event:IndexChangeEvent):void
		{
			selectRole(roles.roles.getItemAt(event.newIndex) as Role);
		}

		[Bindable("buttonVisibleChanged")]
		public function get expressionForIsTargetRoleDefault():String
		{
			if (roles.targetRole)
			{
				return resourceManager.getString('ROLUI', roles.targetRole.isDefault ? 'rol.default.yes' : 'rol.default.no');
			}
			else
			{
				return "";
			}
		}

		//-------------------------------------------------
		// Wizard
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public var rolWizardPM:RolWizardPM;

		[Inject]
		[Bindable]
		public var editingRole:EditingRole;

		[Bindable]
		public var isRolWizardOpen:Boolean = false;

		public function launchRolWizardToAdd():void
		{
			rolWizardPM.mode = RolWizardPM.MODE_ADD;
			isRolWizardOpen = true;
		}

		public function launchRolWizardToEdit():void
		{
			rolWizardPM.mode = RolWizardPM.MODE_EDIT;
			isRolWizardOpen = true;
		}

		public function launchRolWizardToCopy():void
		{
			rolWizardPM.mode = RolWizardPM.MODE_COPY;
			isRolWizardOpen = true;
		}

		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * ユーザを削除します.
		 */
		public function deleteUsr():void
		{
			if(roles.targetRole != null)
			{
				VhutAlert.show(resourceManager.getString('ROLUI', 'alert.message.delete')
					, resourceManager.getString('APIUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL
					, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								roles.deleteTargetRole();
								break;
						}
					}
				);
			}
		}

		//-------------------------------------------------
		// Search
		//-------------------------------------------------

		public function searchRoles(event:SearchEvent):void
		{
			roles.setRolesfilter(event.keywords);
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
			return validateRight(Right.UPDATE_SYS_ROLE) && !roles.targetRole.sysLock;
		}

		[Bindable("buttonVisibleChanged")]
		/** 作成ボタンが可視 */
		public function get isCreateButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_SYS_ROLE);
		}

		[Bindable("buttonVisibleChanged")]
		/** 複製ボタンが可視 */
		public function get isCopyButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_SYS_ROLE);
		}

		[Bindable("buttonVisibleChanged")]
		/** 削除ボタンが可視 */
		public function get isDeleteButtonVisible():Boolean
		{
			return validateRight(Right.DELETE_SYS_ROLE);
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