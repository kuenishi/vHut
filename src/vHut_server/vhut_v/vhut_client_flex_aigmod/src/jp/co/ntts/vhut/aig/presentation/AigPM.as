/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.aig.presentation
{
	import flash.events.Event;
	import flash.net.FileReference;

	import jp.co.ntts.vhut.aig.AigEvent;
	import jp.co.ntts.vhut.aig.domain.Aigs;
	import jp.co.ntts.vhut.aig.wiz.presentation.AigWizardPM;
	import jp.co.ntts.vhut.comp.shutter.Shutter;
	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.util.CsvUtil;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.controls.DataGrid;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	import spark.events.IndexChangeEvent;

	[Event(name="deleteAig", type="jp.co.ntts.vhut.aig.AigEvent")]
	[ManagedEvents(names="deleteAig")]
	/**
	 * アプリケーションインスタンスグループ管理モジュールのメインのPM.
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
	public class AigPM extends ModulePMBase
	{

		override public function get moduleName():String
		{
			return ModuleName.AIG;
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
		// Aigs
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set aigs(value:Aigs):void
		{
			if (_aigs == value)
				return;
			if (_aigs)
			{
				_aigs.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_aigs.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}
			_aigs = value;
			_aigs.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_aigs.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get aigs():Aigs
		{
			return _aigs;
		}
		private var _aigs:Aigs;

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
			aigs.updateAigs();
		}

		/** 表示（詳細のみ）を最新化します. */
		public function updateTarget():void
		{
			aigs.updateTargetAig(true);
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return aigs && aigs.targetAig;
		}

		/**
		 * アプリケーションインスタンスグループの概要をリストで取得する。
		 */
		public function getAllAigsAbstractionList():void
		{
			aigs.updateAigs();
		}

		/**
		 * 選択中のアプリケーションインスタンスグループを変更する.
		 * @param item 新しいアプリケーションインスタンスグループ
		 */
		public function selectAig(item:ApplicationInstanceGroup):void
		{
			aigs.targetAig = item;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		public function selectAigHandler(event:IndexChangeEvent):void
		{
			selectAig(aigs.aigs.getItemAt(event.newIndex) as ApplicationInstanceGroup);
		}

		public function ipInfoShutterChangeHander(event:Event):void
		{
			var shutter:Shutter = event.target as Shutter;
			if (shutter && shutter.isOpen)
				aigs.updateTargetAigIpInfos();
		}

		public function exportTargetIpInfos(dataGrid:DataGrid):void
		{
			var csv:String = CsvUtil.getCsvFromDataGrid(dataGrid);
			var fileReference:FileReference = new FileReference();
			fileReference.save(csv, 'ipInfo.csv');
		}

		//-------------------------------------------------
		// Wizard
		//-------------------------------------------------

		[Bindable]
		/** アプリケーションインスタンスグループ編集・ウィザードの開閉状態 */
		public var isAigWizardOpen:Boolean = false;

		[Bindable]
		/** アプリケーションインスタンスグループ一括追加・ウィザードの開閉状態 */
		public var isImportWizardOpen:Boolean = false;

		[Inject]
		public var aigWizardPM:AigWizardPM;

		/**
		 * アプリケーションインスタンスグループ追加用のウィザードを起動します.
		 */
		public function launchAigWizardToAdd():void
		{
			aigWizardPM.mode = AigWizardPM.MODE_ADD;
			isAigWizardOpen = true;
		}

		/**
		 * アプリケーションインスタンスグループ複製用のウィザードを起動します.
		 */
		public function launchAigWizardToCopy():void
		{
			aigWizardPM.mode = AigWizardPM.MODE_COPY;
			isAigWizardOpen = true;
		}

		/**
		 * アプリケーションインスタンスグループ編集用のウィザードを起動します.
		 */
		public function launchAigWizardToEdit():void
		{
			aigWizardPM.mode = AigWizardPM.MODE_EDIT;
			isAigWizardOpen = true;
		}

		/**
		 * アプリケーションインスタンスグループ一括追加用のウィザードを起動します.
		 */
		public function launchImportWizard():void
		{
			isImportWizardOpen = true;
		}

		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * アプリケーションインスタンスグループを削除します.
		 */
		public function deleteAig():void
		{
			var rm:IResourceManager = ResourceManager.getInstance();
			var alertLabels:Vector.<String> = new <String>[
				rm.getString('APIUI', 'alert.ok')
				, rm.getString('APIUI', 'alert.cancel')
			];
			if(aigs.targetAig != null)
			{
				VhutAlert.show(rm.getString('AIGUI', 'alert.message.delete')
					, rm.getString('AIGUI', 'alert.title.confirm')
					, alertLabels
					, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								dispatchEvent(AigEvent.newDeleteAigEvent(aigs.targetAig));
								break;
						}
					}
				);
			}
		}

		//-------------------------------------------------
		// Search
		//-------------------------------------------------

		public function searchAigHandler(event:SearchEvent):void
		{
			aigs.setAigsfilter(event.keywords);
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

		[Bindable("buttonEnabledChanged")]
		/** リリースボタンが有効 */
		public function get isSwitchButtonEnabled():Boolean
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
				return validateRight(Right.UPDATE_OWN_AIG);
			}
			else
			{
				return validateRight(Right.UPDATE_ALL_AIG);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 作成ボタンが可視 */
		public function get isCreateButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_OWN_AIG);
		}

		[Bindable("buttonVisibleChanged")]
		/** 複製ボタンが可視 */
		public function get isCopyButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_OWN_AIG);
		}

		[Bindable("buttonVisibleChanged")]
		/** 削除ボタンが可視 */
		public function get isDeleteButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.DELETE_OWN_AIG);
			}
			else
			{
				return validateRight(Right.DELETE_ALL_AIG);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** リリースボタンが可視 */
		public function get isSwitchButtonVisible():Boolean
		{
			return validateRight(Right.READ_OWN_AI);
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
			if (!aigs || !aigs.targetAig || !aigs.targetAig.vhutUserId)
				return false;
			if (!session || !session.user)
				return false;

			return aigs.targetAig.vhutUserId == session.user.id;
		}
	}
}