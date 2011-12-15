/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.aig.AigEvent;
	import jp.co.ntts.vhut.aig.domain.Aigs;
	import jp.co.ntts.vhut.aig.domain.EditingAig;
	import jp.co.ntts.vhut.aig.presentation.AigPM;
	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;

	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;

	[Event(type="jp.co.ntts.vhut.aig.AigEvent", name="createAig")]
	[Event(type="jp.co.ntts.vhut.aig.AigEvent", name="updateAig")]
	[ManagedEvents("navigateTo, createAig, updateAig")]
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
	public class AigWizardPM extends WizardPM implements ICommandOwner
	{
		/** 追加モード */
		public static const MODE_ADD:String = "modeAdd";
		/** 複製モード */
		public static const MODE_COPY:String = "modeCopy";
		/** 編集モード */
		public static const MODE_EDIT:String = "modeEdit";

		[Inject]
		public var aigs:Aigs;

		[Inject]
		public var editingAig:EditingAig;

		[Inject]
		public var aigPM:AigPM;

		public function AigWizardPM()
		{
			super("AIGUI");
		}

		override public function set mode(value:String):void
		{
			switch(value)
			{
				case MODE_ADD:
					editingAig.setNewAig();
					break;
				case MODE_COPY:
					editingAig.setClonedAig(aigs.targetAig);
					break;
				case MODE_EDIT:
					editingAig.setRegisteredAig(aigs.targetAig);
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
					key = "aig.wizard.title.add";
					break;
				case MODE_COPY:
					key = "aig.wizard.title.copy";
					break;
				case MODE_EDIT:
					key = "aig.wizard.title.edit";
					break;
				default:
					return "not defined"
			}
			return ResourceManager.getInstance().getString(_bundle, key);
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			var rm:IResourceManager = ResourceManager.getInstance();
			var alertLabels:Vector.<String> = new <String>[
				rm.getString('APIUI', 'alert.ok')
				, rm.getString('APIUI', 'alert.cancel')
			];
			switch(mode)
			{
				case MODE_ADD:
					VhutAlert.show(rm.getString('AIGUI', 'alert.message.add')
						, rm.getString('AIGUI', 'alert.title.confirm')
						, alertLabels
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(AigEvent.newCreateAigEvent(editingAig.targetAig));
//									aigPM.isAigWizardOpen = false;
									break;
							}
						}
					);
					break;
				case MODE_COPY:
					VhutAlert.show(rm.getString('AIGUI', 'alert.message.copy')
						, rm.getString('AIGUI', 'alert.title.confirm')
						, alertLabels
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(AigEvent.newCreateAigEvent(editingAig.targetAig));
//									aigPM.isAigWizardOpen = false;
									break;
							}
						}
					);
					break;
				case MODE_EDIT:
					VhutAlert.show(rm.getString('AIGUI', 'alert.message.edit')
						, rm.getString('AIGUI', 'alert.title.confirm')
						, alertLabels
						, null,
						function(event:CloseEvent):void
						{
							switch(event.detail)
							{
								case 0:
									dispatchEvent(AigEvent.newUpdateAigEvent(editingAig.targetAig));
//									aigPM.isAigWizardOpen = false;
									break;
							}
						}
					);
				default:
			}
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			aigPM.isAigWizardOpen = false;
			return true;
		}
	}
}