/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.usr.presentation.wiz
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.usr.UsrEvent;
	import jp.co.ntts.vhut.usr.domain.EditingVhutUser;
	import jp.co.ntts.vhut.usr.presentation.UsrPM;

	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;

	[Event(name="editUsr", type="jp.co.ntts.vhut.usr.UsrEvent")]
	[ManagedEvents(names="navigateTo, editUsr")]
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
	public class UsrWizardPM extends WizardPM implements ICommandOwner
	{

		[Inject]
		public var editingVhutUser:EditingVhutUser;

		[Inject]
		public var usrPM:UsrPM;

		public function UsrWizardPM()
		{
			super("USRUI");
		}

		public function prepare():void
		{
			editingVhutUser.setRegisteredUsr(usrPM.vhutUsers.targetVhutUser);
		}

		override public function get title():String
		{
			return resourceManager.getString(_bundle, "usr.wizard.title.edit");
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			VhutAlert.show(resourceManager.getString('USRUI', 'alert.message.edit')
				, resourceManager.getString('APIUI', 'alert.title.confirm')
				, VhutAlert.LABELS_OK_CANCEL
				, null,
				function(event:CloseEvent):void
				{
					switch(event.detail)
					{
						case 0:
							dispatchEvent(UsrEvent.newEditUsrEvent(editingVhutUser.targetVhutUser));
							break;
					}
				}
			);
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			usrPM.isUsrWizardOpen = false;
			return true;
		}
	}
}