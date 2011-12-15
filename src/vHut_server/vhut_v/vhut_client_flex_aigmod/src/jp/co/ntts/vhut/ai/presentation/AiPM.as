/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.ai.presentation
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.ai.AiEvent;
	import jp.co.ntts.vhut.ai.AiVmEvent;
	import jp.co.ntts.vhut.ai.domain.Ais;
	import jp.co.ntts.vhut.comp.si.StatusIndicatorEvent;
	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationInstanceStatus;
	import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.form.application.SearchEvent;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	import spark.events.IndexChangeEvent;

	[Event(name="startAiVm", type="jp.co.ntts.vhut.ai.AiVmEvent")]
	[Event(name="stopAiVm", type="jp.co.ntts.vhut.ai.AiVmEvent")]
	[Event(name="activateAi", type="jp.co.ntts.vhut.ai.AiEvent")]
	[Event(name="deactivateAi", type="jp.co.ntts.vhut.ai.AiEvent")]
	[Event(name="rebuildAi", type="jp.co.ntts.vhut.ai.AiEvent")]
	[ManagedEvents(names="startAiVm, stopAiVm, activateAi, deactivateAi, rebuildAi")]
	/**
	 * アプリケーションインスタンス管理モジュールのPM.
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
	public class AiPM extends EventDispatcher
	{
		public function AiPM(target:IEventDispatcher=null)
		{
			//TODO: implement function
			super(target);
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
		// Ais
		//-------------------------------------------------

		[Inject]
		[Bindable]
		public function set ais(value:Ais):void
		{
			if (_ais == value)
				return;
			if (_ais)
			{
				_ais.removeEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
				_ais.removeEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			}
			_ais = value;
			_ais.addEventListener(ChangeTargetItemEvent.CHANGE_TARGET_ITEM, changeTargetItemEventHandler);
			_ais.addEventListener(UpdateTargetItemEvent.UPDATE_TARGET_ITEM, updateTargetItemEventHandler);
			dispatchEvent(new Event("buttonVisibleChanged"));
		}
		public function get ais():Ais
		{
			return _ais;
		}
		private var _ais:Ais;

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
			ais.updateTargetAi();
		}

		/** 詳細が表示されている */
		public function get isTargetSelected():Boolean
		{
			return ais && ais.targetAi;
		}

		/**
		 * 選択中のアプリケーションインスタンスを変更する.
		 * @param item 新しいアプリケーションインスタンス
		 */
		public function selectAi(item:ApplicationInstance):void
		{
			ais.targetAi = item;
			dispatchEvent(new Event("buttonEnabledChanged"));
			dispatchEvent(new Event("buttonVisibleChanged"));
		}

		public function selectAiHandler(event:IndexChangeEvent):void
		{
			selectAi(ais.ais.getItemAt(event.newIndex) as ApplicationInstance);
		}

		/**
		 * 選択中のアプリケーションインスタンスVMを変更する.
		 * @param item 新しいアプリケーションインスタンスVM
		 */
		public function selectAiVm(item:ApplicationInstanceVm):void
		{
			ais.targetAiElement = item;
		}

		/**
		 * 選択中のアプリケーションインスタンスSecurityGroupを変更する.
		 * @param item 新しいアプリケーションインスタンスSecurityGroup
		 */
		public function selectAiSecurityGroup(item:ApplicationInstanceSecurityGroup):void
		{
			ais.targetAiElement = item;
		}

		/**
		 * 選択中のアプリケーションインスタンスようそを変更する.
		 * @param item 新しいアプリケーション・セキュリティグループ
		 */
		public function selectAiElement(item:Object):void
		{
			ais.targetAiElement = item;
		}


		//-------------------------------------------------
		// Command
		//-------------------------------------------------

		/**
		 * アプリケーションインスタンスVMを起動します。
		 * @param aiVm アプリケーションインスタンスVM
		 */
		public function startAiVm(aiVm:ApplicationInstanceVm):void
		{
			dispatchEvent(AiVmEvent.newStartAiVmEvent(aiVm.id));
			ais.updateTargetAi();
		}

		/**
		 * アプリケーションインスタンスVMを停止します。
		 * @param aiVm アプリケーションインスタンスVM
		 */
		public function stopAiVm(aiVm:ApplicationInstanceVm):void
		{
			dispatchEvent(AiVmEvent.newStopAiVmEvent(aiVm.id));
			ais.updateTargetAi();
		}

		/**
		 * アプリケーションインスタンスを起動可能にする.
		 */
		public function activateAi():void
		{
			if(ais.targetAi != null && ais.targetAi.status.equals(ApplicationInstanceStatus.DEACTIVE))
			{
				dispatchEvent(AiEvent.newActivateAiEvent(ais.targetAi));
			}
		}

		/**
		 * アプリケーションインスタンスを起動不可能にする.
		 */
		public function deactivateAi():void
		{
			if(ais.targetAi != null)
			{
				dispatchEvent(AiEvent.newDeactivateAiEvent(ais.targetAi));
			}
		}

		public function changeAiStatus(event:StatusIndicatorEvent):void
		{
			switch(event.to)
			{
				case ApplicationInstanceStatus.ACTIVE.name:
					activateAi();
					break;
				case ApplicationInstanceStatus.DEACTIVE.name:
					deactivateAi();
					break;
			}
		}

		/**
		 * アプリケーションインスタンスを再作成します.
		 */
		public function rebuildAi():void
		{
			var rm:IResourceManager = ResourceManager.getInstance();
			if(ais.targetAi != null)
			{
				VhutAlert.show(rm.getString('AIGUI', 'alert.message.rebuild')
					, rm.getString('AIGUI', 'alert.title.confirm')
					, VhutAlert.LABELS_OK_CANCEL
					, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								dispatchEvent(AiEvent.newRebuildAiEvent(ais.targetAi));
								break;
						}
					}
				);
			}
		}

		//-------------------------------------------------
		// Search
		//-------------------------------------------------

		public function searchAiHandler(event:SearchEvent):void
		{
			ais.filterAi(event.keywords);
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
		/** 再作成ボタンが有効 */
		public function get isRebuildButtonEnabled():Boolean
		{
			return isTargetSelected
			&& ais.targetAi.isEditable;
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
				return validateRight(Right.POWER_OWN_AI);
			}
			else if (isTargetParentMine)
			{
				return validateRight(Right.POWER_CHILD_AI);
			}
			else
			{
				return validateRight(Right.POWER_ALL_AI);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 画面ボタンが可視 */
		public function get isDisplaydButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.DISPLAY_OWN_AI);
			}
			else if (isTargetParentMine)
			{
				return validateRight(Right.DISPLAY_CHILD_AI);
			}
			else
			{
				return validateRight(Right.DISPLAY_ALL_AI);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** コマンドボタンが可視 */
		public function get isCommandButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.COMMAND_OWN_AI);
			}
			else if (isTargetParentMine)
			{
				return validateRight(Right.COMMAND_CHILD_AI);
			}
			else
			{
				return validateRight(Right.COMMAND_ALL_AI);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 有効化ボタンが可視 */
		public function get isStatusButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.STATUS_OWN_AI);
			}
			else if (isTargetParentMine)
			{
				return validateRight(Right.STATUS_CHILD_AI);
			}
			else
			{
				return validateRight(Right.STATUS_ALL_AI);
			}
		}

		[Bindable("buttonVisibleChanged")]
		/** 再作成ボタンが可視 */
		public function get isRebuildButtonVisible():Boolean
		{
			if (isTargetMine)
			{
				return validateRight(Right.REBUILD_OWN_AI);
			}
			else if (isTargetParentMine)
			{
				return validateRight(Right.READ_CHILD_AI);
			}
			else
			{
				return validateRight(Right.REBUILD_ALL_AI);
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
			if (!ais
				|| !ais.targetAi
				|| !ais.targetAi.vhutUserId)
				return false;
			if (!session || !session.user)
				return false;

			return ais.targetAi.vhutUserId == session.user.id;
		}

		/**
		 * @return ターゲットの親の所有者が自分
		 *
		 */
		private function get isTargetParentMine():Boolean
		{
			if (!ais
				|| !ais.targetAi
				|| !ais.targetAi.applicationInstanceGroup
				|| !ais.targetAi.applicationInstanceGroup.vhutUserId)
				return false;
			if (!session || !session.user)
				return false;

			return ais.targetAi.applicationInstanceGroup.vhutUserId == session.user.id;
		}
	}
}