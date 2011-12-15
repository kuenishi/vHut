/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.wiz.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.app.ApplicationEvent;
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.app.domain.EditingApplication;
	import jp.co.ntts.vhut.app.presentation.AppPM;
	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;

	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Event(type="jp.co.ntts.vhut.app.ApplicationEvent", name="createApp")]
	[Event(type="jp.co.ntts.vhut.app.ApplicationEvent", name="updateApp")]
	[ManagedEvents("navigateTo, createApp, updateApp")]
	[Landmark(name="content")]
	/**
	 * <p>アプリケーション追加・複製・編集ウィザードのモデル.
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
	public class AppWizardPM extends WizardPM implements ICommandOwner
	{
		/** 追加モード */
		public static const MODE_ADD:String = "modeAdd";
		/** 複製モード */
		public static const MODE_COPY:String = "modeCopy";
		/** 編集モード */
		public static const MODE_EDIT:String = "modeEdit";

		[Inject]
		public var applications:Applications;

		[Inject]
		public var editingApplication:EditingApplication;

		[Inject]
		public var appPM:AppPM;

		protected var rm:IResourceManager

		public function AppWizardPM()
		{
			super("APPUI");
			rm = ResourceManager.getInstance();
		}

		override public function set mode(value:String):void
		{
			switch(value)
			{
				case MODE_ADD:
					editingApplication.setNewApplication();
					break;
				case MODE_COPY:
					editingApplication.setClonedApplication(applications.targetApplication);
					break;
				case MODE_EDIT:
					editingApplication.setRegisteredApplication(applications.targetApplication);
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
					key = "app.wizard.title.add";
					break;
				case MODE_COPY:
					key = "app.wizard.title.copy";
					break;
				case MODE_EDIT:
					key = "app.wizard.title.edit";
					break;
				default:
					return "not defined"
			}
			return ResourceManager.getInstance().getString(_bundle, key);
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			switch(mode)
			{
				case MODE_ADD:
					VhutAlert.show(rm.getString('APPUI', 'alert.message.app.add')
						, rm.getString('APPUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(ApplicationEvent.newCreateApplicationEvent(editingApplication.targetApplication));
//									appPM.isAppWizardOpen = false;
									break;
							}
						}
					);
					break;
				case MODE_COPY:
					VhutAlert.show(rm.getString('APPUI', 'alert.message.app.copy')
						, rm.getString('APPUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(ApplicationEvent.newCreateApplicationEvent(editingApplication.targetApplication));
//									appPM.isAppWizardOpen = false;
									break;
							}
						}
					);
					break;
				case MODE_EDIT:
					VhutAlert.show(rm.getString('APPUI', 'alert.message.app.edit')
						, rm.getString('APPUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(ApplicationEvent.newUpdateApplicationEvent(editingApplication.targetApplication));
//									appPM.isAppWizardOpen = false;
									break;
							}
						}
					);
				default:
			}
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			appPM.isAppWizardOpen = false;
			return true;
		}
	}
}