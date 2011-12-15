/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.rol.presentation.wiz
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.rol.RolEvent;
	import jp.co.ntts.vhut.rol.domain.EditingRole;
	import jp.co.ntts.vhut.rol.domain.Roles;
	import jp.co.ntts.vhut.rol.presentation.RolPM;
	import jp.co.ntts.vhut.tmp.TmpEvent;

	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;


	[Event(type="jp.co.ntts.vhut.rol.RolEvent", name="createRol")]
	[Event(type="jp.co.ntts.vhut.rol.RolEvent", name="updateRol")]
	[ManagedEvents("navigateTo, createRol, updateRol")]
	[Landmark(name="content")]
	/**
	 * <p>ベーステンプレート追加・複製・編集ウィザードのモデル.
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
	public class RolWizardPM extends WizardPM implements ICommandOwner
	{

		/** 追加モード */
		public static const MODE_ADD:String = "modeAdd";
		/** 複製モード */
		public static const MODE_COPY:String = "modeCopy";
		/** 編集モード */
		public static const MODE_EDIT:String = "modeEdit";

		[Inject]
		[Bindable]
		public var roles:Roles;

		[Inject]
		[Bindable]
		public var editingRole:EditingRole;

		[Inject]
		public var rolPM:RolPM;

		public function RolWizardPM()
		{
			super("ROLUI");
		}

		override public function set mode(value:String):void
		{
			switch(value)
			{
				case MODE_ADD:
					editingRole.setNewRole();
					break;
				case MODE_COPY:
					editingRole.setClonedRole(roles.targetRole);
					break;
				case MODE_EDIT:
					editingRole.setRegisteredRole(roles.targetRole);
					break;
				default:
			}
			super.mode = value;
		}

		override public function get title():String
		{
			var key:String;
			switch(mode)
			{
				case MODE_ADD:
					key = "rol.wizard.title.add";
					break;
				case MODE_COPY:
					key = "rol.wizard.title.copy";
					break;
				case MODE_EDIT:
					key = "rol.wizard.title.edit";
					break;
				default:
					return "not defined"
			}
			return resourceManager.getString(_bundle, key);
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			switch(mode)
			{
				case MODE_ADD:
					VhutAlert.show(resourceManager.getString('ROLUI', 'alert.message.add')
						, resourceManager.getString('APIUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(RolEvent.newCreateRolEvent(editingRole.targetRole));
									break;
							}
						}
					);
					break;
				case MODE_COPY:
					VhutAlert.show(resourceManager.getString('ROLUI', 'alert.message.copy')
						, resourceManager.getString('APIUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(RolEvent.newCreateRolEvent(editingRole.targetRole));
									break;
							}
						}
					);
					break;
				case MODE_EDIT:
					VhutAlert.show(resourceManager.getString('ROLUI', 'alert.message.edit')
						, resourceManager.getString('APIUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(RolEvent.newUpdateRolEvent(editingRole.targetRole));
									break;
							}
						}
					);
				default:
			}
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			rolPM.isRolWizardOpen = false;
			return true;
		}
	}
}