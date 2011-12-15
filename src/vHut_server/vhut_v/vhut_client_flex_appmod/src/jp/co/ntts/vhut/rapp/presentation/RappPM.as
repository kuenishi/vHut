/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.rapp.presentation
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.ReleasedApplication;
	import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
	import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.rapp.ReleasedApplicationEvent;
	import jp.co.ntts.vhut.rapp.domain.ReleasedApplications;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.ResourceManager;

	[Event(name="removeRapp", type="jp.co.ntts.vhut.rapp.ReleasedApplicationEvent")]
	[ManagedEvents(names="removeRapp")]
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
	 * $Author: NTT Software Corporation. $
	 */
	public class RappPM extends EventDispatcher
	{
		public function RappPM()
		{
		}

		[Bindable]
		public var isWizardOpen:Boolean = false;

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
		// ReleasedApplications
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set releasedApplications(value:ReleasedApplications):void
		{
			if (_releasedApplications == value)
				return;

			if (_releasedApplications)
			{
				_releasedApplications.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_releasedApplications.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}

			_releasedApplications = value;
			_releasedApplications.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_releasedApplications.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get releasedApplications():ReleasedApplications
		{
			return _releasedApplications;
		}
		private var _releasedApplications:ReleasedApplications;

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

		public function updateTarget():void
		{
			releasedApplications.updateTargetReleasedApplication();
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return releasedApplications && releasedApplications.targetReleasedApplication;
		}

		/**
		 * 選択中のリリースド・アプリケーションを変更する.
		 * @param item 新しいリリースド・アプリケーション
		 */
		public function selectReleasedApplication(releasedApplication:ReleasedApplication):void
		{
			releasedApplications.targetReleasedApplication = releasedApplication;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		/**
		 * 選択中のリリースド・アプリケーションVMを変更する.
		 * @param item 新しいリリースド・アプリケーション・テンプレート
		 */
		public function selectReleasedApplicationTemplate(item:ReleasedApplicationTemplate):void
		{
			releasedApplications.targetReleasedApplicationElement = item;
		}

		/**
		 * 選択中のリリースド・アプリケーション・セキュリティグループを変更する.
		 * @param item 新しいリリースド・アプリケーション・セキュリティグループ
		 */
		public function selectReleasedApplicationSecurityGroupTemplate(item:ReleasedApplicationSecurityGroupTemplate):void
		{
			releasedApplications.targetReleasedApplicationElement = item;
		}

		/**
		 * 選択中のリリースド・アプリケーション要素を変更する.
		 * @param item 新しいリリースド・アプリケーション要素
		 */
		public function selectReleasedApplicationElement(item:Object):void
		{
			releasedApplications.targetReleasedApplicationElement = item;
		}

		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * リリースド・アプリケーションを削除します.
		 */
		public function removeApplication():void
		{
			if(releasedApplications.targetReleasedApplication != null)
			{
				VhutAlert.show(ResourceManager.getInstance().getString('APPUI', 'alert.message.rapp.delete')
					, ResourceManager.getInstance().getString('APPUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								dispatchEvent(ReleasedApplicationEvent.newRemoveReleasedApplicationEvent(releasedApplications.targetReleasedApplication));
								break;
						}
					}
				);
			}
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
		/** コマンドボタンが有効 */
		public function get isCommandButtonEnabled():Boolean
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
		/** コマンドボタンが可視 */
		public function get isCommandButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.COMMAND_OWN_RAPP);
			}
			else
			{
				return validateRight(Right.COMMAND_ALL_RAPP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 削除ボタンが可視 */
		public function get isDeleteButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.DELETE_OWN_RAPP);
			}
			else
			{
				return validateRight(Right.DELETE_ALL_RAPP);
			}
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
			if (!releasedApplications
				|| !releasedApplications.targetReleasedApplication
				|| !releasedApplications.targetReleasedApplication.application
				|| !releasedApplications.targetReleasedApplication.application.vhutUserId)
				return false;
			if (!session || !session.user)
				return false;

			return releasedApplications.targetReleasedApplication.application.vhutUserId == session.user.id
		}
	}
}