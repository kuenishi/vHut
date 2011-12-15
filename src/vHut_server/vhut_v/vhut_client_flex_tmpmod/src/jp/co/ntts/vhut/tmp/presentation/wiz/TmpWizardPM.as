/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.tmp.presentation.wiz
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.tmp.TmpEvent;
	import jp.co.ntts.vhut.tmp.domain.BaseTemplates;
	import jp.co.ntts.vhut.tmp.domain.EditingBaseTemplate;
	import jp.co.ntts.vhut.tmp.presentation.TmpPM;

	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;


	[Event(type="jp.co.ntts.vhut.tmp.TmpEvent", name="createTmp")]
	[Event(type="jp.co.ntts.vhut.tmp.TmpEvent", name="updateTmp")]
	[ManagedEvents("navigateTo, createTmp, updateTmp")]
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
	public class TmpWizardPM extends WizardPM implements ICommandOwner
	{

		/** 追加モード */
		public static const MODE_ADD:String = "modeAdd";
		/** 複製モード */
		public static const MODE_COPY:String = "modeCopy";
		/** 編集モード */
		public static const MODE_EDIT:String = "modeEdit";

		[Inject]
		[Bindable]
		public var baseTemplates:BaseTemplates;

		[Inject]
		[Bindable]
		public var editingBaseTemplate:EditingBaseTemplate;

		[Inject]
		public var tmpPM:TmpPM;

		public function TmpWizardPM()
		{
			super("TMPUI");
		}

		override public function set mode(value:String):void
		{
			switch(value)
			{
				case MODE_ADD:
					editingBaseTemplate.setNewBaseTemplate();
					break;
				case MODE_COPY:
					editingBaseTemplate.setClonedBaseTemplate(baseTemplates.targetBaseTemplate);
					break;
				case MODE_EDIT:
					editingBaseTemplate.setRegisteredBaseTemplate(baseTemplates.targetBaseTemplate);
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
					key = "tmp.wizard.title.add";
					break;
				case MODE_COPY:
					key = "tmp.wizard.title.copy";
					break;
				case MODE_EDIT:
					key = "tmp.wizard.title.edit";
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
					VhutAlert.show(resourceManager.getString('TMPUI', 'alert.message.add')
						, resourceManager.getString('APIUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(TmpEvent.newCreateTmpEvent(editingBaseTemplate.targetBaseTemplate));
									break;
							}
						}
					);
					break;
				case MODE_COPY:
					VhutAlert.show(resourceManager.getString('TMPUI', 'alert.message.copy')
						, resourceManager.getString('APIUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(TmpEvent.newCreateTmpEvent(editingBaseTemplate.targetBaseTemplate));
									break;
							}
						}
					);
					break;
				case MODE_EDIT:
					VhutAlert.show(resourceManager.getString('TMPUI', 'alert.message.edit')
						, resourceManager.getString('APIUI', 'alert.title.confirm')
						, VhutAlert.LABELS_OK_CANCEL
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(TmpEvent.newUpdateTmpEvent(editingBaseTemplate.targetBaseTemplate));
									break;
							}
						}
					);
				default:
			}
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			tmpPM.isTmpWizardOpen = false;
			return true;
		}
	}
}