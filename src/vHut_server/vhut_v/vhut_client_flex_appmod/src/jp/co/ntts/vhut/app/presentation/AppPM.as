/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.app.AppVmEvent;
	import jp.co.ntts.vhut.app.ApplicationEvent;
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.app.wiz.presentation.AppWizardPM;
	import jp.co.ntts.vhut.comp.si.StatusIndicatorEvent;
	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationStatus;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	import spark.events.IndexChangeEvent;

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
	[Event(name="startAppVm", type="jp.co.ntts.vhut.app.AppVmEvent")]
	[Event(name="stopAppVm", type="jp.co.ntts.vhut.app.AppVmEvent")]
	[Event(name="selectApp", type="jp.co.ntts.vhut.app.ApplicationEvent")]
	[Event(name="activateApp", type="jp.co.ntts.vhut.app.ApplicationEvent")]
	[Event(name="deactivateApp", type="jp.co.ntts.vhut.app.ApplicationEvent")]
	[Event(name="deployApp", type="jp.co.ntts.vhut.app.ApplicationEvent")]
	[Event(name="deleteApp", type="jp.co.ntts.vhut.app.ApplicationEvent")]
	[ManagedEvents(names="getAllApp,startAppVm,stopAppVm,activateApp,deactivateApp,deployApp,deleteApp,selectApp")]
	public class AppPM extends ModulePMBase
	{

		private var _rm:IResourceManager;

		public function AppPM():void
		{
			_rm = ResourceManager.getInstance();
		}

		override public function get moduleName():String
		{
			return ModuleName.APP;
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
		// Applications
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set applications(value:Applications):void
		{
			if (_applications == value)
				return;

			if (_applications)
			{
				_applications.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_applications.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}

			_applications = value;
			_applications.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_applications.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get applications():Applications
		{
			return _applications;
		}
		private var _applications:Applications;

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
			applications.updateApplications();
		}

		/** 表示（詳細のみ）を最新化します. */
		public function updateTarget():void
		{
			applications.updateTargetApplication(true);
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return applications && applications.targetApplication;
		}

		public function selectApplicationHandler(event:IndexChangeEvent):void
		{
//			trace("change:oldIndex"+event.oldIndex+", newIndex="+event.newIndex)
//			if(event.oldIndex != event.newIndex)
			applications.targetApplication = applications.applications.getItemAt(event.newIndex) as Application;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		/**
		 * 選択中のアプリケーションVMを変更する.
		 * @param item 新しいアプリケーションVM
		 */
		public function selectApplicationVm(item:ApplicationVm):void
		{
			applications.targetApplicationElement = item;
		}

		/**
		 * 選択中のアプリケーション・セキュリティグループを変更する.
		 * @param item 新しいアプリケーション・セキュリティグループ
		 */
		public function selectApplicationSecurityGroup(item:ApplicationSecurityGroup):void
		{
			applications.targetApplicationElement = item;
		}

		/**
		 * 選択中のアプリケーション・セキュリティグループを変更する.
		 * @param item 新しいアプリケーション・セキュリティグループ
		 */
		public function selectApplicationElement(item:Object):void
		{
			applications.targetApplicationElement = item;
		}

		//-------------------------------------------------
		// Wizard
		//-------------------------------------------------

		[Bindable]
		/** アプリケーション編集・ウィザードの開閉状態 */
		public var isAppWizardOpen:Boolean = false;

		[Bindable]
		/** アプリケーション起動期間予約・ウィザードの開閉状態 */
		public var isTermWizardOpen:Boolean = false;

		[Inject]
		public var appWizardPM:AppWizardPM;

		/**
		 * コンテンツ追加用のウィザードを起動します.
		 */
		public function launchAppWizardToAdd():void
		{
			appWizardPM.mode = AppWizardPM.MODE_ADD;
			isAppWizardOpen = true;
		}

		/**
		 * コンテンツ複製用のウィザードを起動します.
		 */
		public function launchAddWizardToCopy():void
		{
			appWizardPM.mode = AppWizardPM.MODE_COPY;
			isAppWizardOpen = true;
		}

		/**
		 * コンテンツ編集用のウィザードを起動します.
		 */
		public function launchAppWizardToEdit():void
		{
			appWizardPM.mode = AppWizardPM.MODE_EDIT;
			isAppWizardOpen = true;
		}

		/**
		 * コンテンツ起動期間予約用のウィザードを起動します.
		 */
		public function launchTermWizard():void
		{
			isTermWizardOpen = true;
		}

		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * アプリケーションVMを起動します。
		 * @param appVm
		 */
		public function startApplicationVm(appVm:ApplicationVm):void
		{
			dispatchEvent(AppVmEvent.newStartAppVmEvent(appVm.id));
			applications.updateTargetApplication();
		}

		/**
		 * アプリケーションVMを強制終了します。
		 * @param appVm
		 */
		public function stopApplicationVm(appVm:ApplicationVm):void
		{
			dispatchEvent(AppVmEvent.newStopAppVmEvent(appVm.id));
			applications.updateTargetApplication();
		}

		public function changeApplicationStatus(event:StatusIndicatorEvent):void
		{
			switch(event.to)
			{
				case ApplicationStatus.ACTIVE.name:
					activateApplication();
					break;
				case ApplicationStatus.DEACTIVE.name:
					deactivateApplication();
					break;
			}
		}

		/**
		 * アプリケーションを起動可能にする.
		 */
		public function activateApplication():void
		{
			if(applications.targetApplication != null && applications.targetApplication.status.equals(ApplicationStatus.DEACTIVE))
			{
				dispatchEvent(ApplicationEvent.newActivateApplicationEvent(applications.targetApplication));
			}
		}

		/**
		 * アプリケーションを起動不可能にする.
		 */
		public function deactivateApplication():void
		{
			if(applications.targetApplication != null)
			{
				dispatchEvent(ApplicationEvent.newDeactivateApplicationEvent(applications.targetApplication));
			}
		}

		/**
		 * アプリケーションをリリースします.
		 */
		public function deployApplication():void
		{
			if(applications.targetApplication != null)
			{
				VhutAlert.show(_rm.getString('APPUI', 'alert.message.app.deploy')
					, _rm.getString('APPUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								dispatchEvent(ApplicationEvent.newDeployApplicationEvent(applications.targetApplication));
								break;
						}
					}
				);
			}
		}

		/**
		 * アプリケーションを削除します.
		 */
		public function deleteApplication():void
		{
			if(applications.targetApplication != null)
			{
				VhutAlert.show(_rm.getString('APPUI', 'alert.message.app.delete')
					, _rm.getString('APPUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								dispatchEvent(ApplicationEvent.newDeleteApplicationEvent(applications.targetApplication));
								break;
						}
					}
				);
			}
		}

		//-------------------------------------------------
		// Search
		//-------------------------------------------------

		public function searchAppHandler(event:SearchEvent):void
		{
			applications.setApplicationsfilter(event.keywords);
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
		/** 電源ボタンが有効 */
		public function get isPowerButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** 画面ボタンが有効 */
		public function get isDisplayButtonEnabled():Boolean
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
		/** 有効化ボタンが有効 */
		public function get isStatusButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** 起動予約ボタンが有効 */
		public function get isReserveButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** 編集ボタンが有効 */
		public function get isEditButtonEnabled():Boolean
		{
			return isTargetSelected
			&& applications.targetApplication.isEditable;
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
			return isTargetSelected
				&& applications.targetApplication.isEditable;
		}

		[Bindable("buttonEnabledChanged")]
		/** リリースボタンが有効 */
		public function get isSwitchButtonEnabled():Boolean
		{
			return isTargetSelected;
		}

		[Bindable("buttonEnabledChanged")]
		/** デプロイボタンが有効 */
		public function get isDeployButtonEnabled():Boolean
		{
			return isTargetSelected
			&& applications.targetApplication.isEditable;
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
		/** 電源ボタンが可視 */
		public function get isPowerButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.POWER_OWN_APP);
			}
			else
			{
				return validateRight(Right.POWER_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 画面ボタンが可視 */
		public function get isDisplaydButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.DISPLAY_OWN_APP);
			}
			else
			{
				return validateRight(Right.DISPLAY_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** コマンドボタンが可視 */
		public function get isCommandButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.COMMAND_OWN_APP);
			}
			else
			{
				return validateRight(Right.COMMAND_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 有効化ボタンが可視 */
		public function get isStatusButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.STATUS_OWN_APP);
			}
			else
			{
				return validateRight(Right.STATUS_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 起動予約ボタンが可視 */
		public function get isReserveButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.TERM_OWN_APP);
			}
			else
			{
				return validateRight(Right.TERM_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 編集ボタンが可視 */
		public function get isEditButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.UPDATE_OWN_APP);
			}
			else
			{
				return validateRight(Right.UPDATE_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 作成ボタンが可視 */
		public function get isCreateButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_OWN_APP);
		}

		[Bindable("buttonVisibleChanged")]
		/** 複製ボタンが可視 */
		public function get isCopyButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_OWN_APP);
		}

		[Bindable("buttonVisibleChanged")]
		/** 削除ボタンが可視 */
		public function get isDeleteButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.DELETE_OWN_APP);
			}
			else
			{
				return validateRight(Right.DELETE_ALL_APP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** リリースボタンが可視 */
		public function get isSwitchButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.READ_OWN_RAPP);
			}
			else
			{
				return validateRight(Right.READ_ALL_RAPP);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** デプロイボタンが可視 */
		public function get isDeployButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.CREATE_OWN_RAPP);
			}
			else
			{
				return validateRight(Right.CREATE_ALL_RAPP);
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
			if (!applications || !applications.targetApplication || !applications.targetApplication.vhutUserId)
				return false;
			if (!session || !session.user)
				return false;

			return applications.targetApplication.vhutUserId == session.user.id
		}

	}
}