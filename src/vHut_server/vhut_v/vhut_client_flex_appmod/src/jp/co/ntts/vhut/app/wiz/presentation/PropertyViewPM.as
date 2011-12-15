/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app.wiz.presentation
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.app.domain.EditingApplication;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.dialog.presentation.OwnerSelectDialogPM;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.VhutUser;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Event(name="getAllUser", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[ManagedEvents(names="getAllUser")]
	/**
	 * アプリケーションウィザードの属性編集時のViewのPMクラス.
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
	public class PropertyViewPM extends EventDispatcher implements IValidator
	{

		public  function PropertyViewPM()
		{
			resourceManager = ResourceManager.getInstance();
		}

		protected var resourceManager:IResourceManager;

		[Inject]
		[Bindable]
		public var editingApplication:EditingApplication;

		[Inject]
		[Bindable]
		public var ownerSelectDialogPM:OwnerSelectDialogPM;

		[Bindable]
		public var appNameErrorMessage:String;

		public function get isValid():Boolean
		{
			var result:Boolean = true;
			appNameErrorMessage = null;

			if(!editingApplication.targetApplication.name)
			{
				appNameErrorMessage = resourceManager.getString("APIUI", "validate.required");
				result = false;
			}

			return result;
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

		//-----------------------------------------------
		// Owner Selection
		//-----------------------------------------------

		[Bindable]
		public function set isOwnerSelectDialogOpen(value:Boolean):void
		{
			if(_isOwnerSelectDialogOpen == value)
				return;

			if(value)
			{
				dispatchEvent(GetAllEvent.newGetAllUserEvent());
			}
			else if(ownerSelectDialogPM.targetUser)
			{
				editingApplication.targetApplication.vhutUser = ownerSelectDialogPM.targetUser;
			}
			_isOwnerSelectDialogOpen = value;
		}
		public function get isOwnerSelectDialogOpen():Boolean
		{
			return _isOwnerSelectDialogOpen;
		}
		private var _isOwnerSelectDialogOpen:Boolean = false;

		public function launchOwnerSelectDialog():void
		{
			isOwnerSelectDialogOpen = true;
		}

		[Bindable("buttonVisibleChanged")]
		/** 所有者変更ボタンが可視 */
		public function get isOwnerButtonVisible():Boolean
		{
			return validateRight(Right.CREATE_ALL_APP);
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