/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr.presentation.addwiz
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.usr.UsrListEvent;
	import jp.co.ntts.vhut.usr.domain.EditingVhutUsers;
	import jp.co.ntts.vhut.usr.domain.Roles;
	import jp.co.ntts.vhut.usr.domain.UnregisteredVhutUsers;
	import jp.co.ntts.vhut.usr.presentation.UsrPM;

	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;

	[Event(name="createUsrList", type="jp.co.ntts.vhut.usr.UsrListEvent")]
	[ManagedEvents(names="navigateTo, createUsrList")]
	[Landmark(name="content")]
	/**
	 * <p>アプリケーションインスタンスグループ追加・複製・編集ウィザードのモデル.
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
	public class UsrAddWizardPM extends WizardPM implements ICommandOwner
	{

		[Inject]
		public var editingVhutUsers:EditingVhutUsers;

		[Inject]
		public var unregisteredVhutUsers:UnregisteredVhutUsers;

		[Inject]
		public var roles:Roles;

		[Inject]
		public var usrPM:UsrPM;

		public function UsrAddWizardPM()
		{
			super("USRUI");
		}

		public function prepare():void
		{
			editingVhutUsers.initialize();
			unregisteredVhutUsers.filterReference = editingVhutUsers.targetVhutUsers;
			unregisteredVhutUsers.updateUnregisteredVhutUsers();
		}

		override public function get title():String
		{
			return resourceManager.getString(_bundle, "usr.wizard.title.add");
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			VhutAlert.show(resourceManager.getString('USRUI', 'alert.message.add')
				, resourceManager.getString('APIUI', 'alert.title.confirm')
				, VhutAlert.LABELS_OK_CANCEL
				, null,
				function(event:CloseEvent):void
				{
					switch(event.detail)
					{
						case 0:
							dispatchEvent(UsrListEvent.newCreateUsrListEvent(editingVhutUsers.getToBeAddedUserList()));
							break;
					}
				}
			);
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			usrPM.isUsrAddWizardOpen = false;
			return true;
		}
	}
}