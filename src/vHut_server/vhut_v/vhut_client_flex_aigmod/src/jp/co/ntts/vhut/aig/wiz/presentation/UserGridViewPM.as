/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.aig.UserListEvent;
	import jp.co.ntts.vhut.aig.domain.ImportingAigs;
	import jp.co.ntts.vhut.aig.presentation.AigPM;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;

	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;


	[Event(type="jp.co.ntts.vhut.aig.UserListEvent", name="validateUserList")]
	[ManagedEvents("validateUserList")]
	/**
	 * アプリケーションインスタンスグループ一括ウィザードの
	 * ユーザ存在確認時のViewのPM.
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
	public class UserGridViewPM extends EventDispatcher implements IValidator
	{
		public function UserGridViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var importingAigs:ImportingAigs;

		[Inject]
		[Bindable]
		public var aigPM:AigPM;

		public function get isValid():Boolean
		{
			if(importingAigs.isAllUsersRegistered)
			{
				return true;
			}
			else
			{
				var rm:IResourceManager = ResourceManager.getInstance();
				var alertLabels:Vector.<String> = new <String>[
					rm.getString('APIUI', 'alert.ok')
					, rm.getString('APIUI', 'alert.cancel')
				];

				VhutAlert.show(rm.getString('AIGUI', 'alert.message.validateUser')
					, rm.getString('AIGUI', 'alert.title.confirm')
					, alertLabels
					, null,
					function(event:CloseEvent):void
					{
						switch(event.detail)
						{
							case 0:
								dispatchEvent(UserListEvent.newValidateUserListEvent(importingAigs.notregisteredUserList.toArray()));
								aigPM.isAigWizardOpen = false;
								break;
						}
					}
				);
				return false;
			}
		}
	}
}