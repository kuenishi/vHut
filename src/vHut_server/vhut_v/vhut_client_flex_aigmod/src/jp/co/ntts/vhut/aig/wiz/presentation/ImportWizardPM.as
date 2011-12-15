/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.aig.AigEvent;
	import jp.co.ntts.vhut.aig.AigListEvent;
	import jp.co.ntts.vhut.aig.domain.Aigs;
	import jp.co.ntts.vhut.aig.domain.EditingAig;
	import jp.co.ntts.vhut.aig.domain.ImportingAigs;
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

	[Event(type="jp.co.ntts.vhut.aig.AigListEvent", name="createAigList")]
	[ManagedEvents("navigateTo, createAigList")]
	[Landmark(name="content")]
	/**
	 * <p>アプリケーションインスタンスグループ一括追加ウィザードのモデル.
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
	public class ImportWizardPM extends WizardPM implements ICommandOwner
	{

		[Inject]
		public var aigs:Aigs;

		[Inject]
		public var importingAigs:ImportingAigs;

		[Inject]
		public var fileViewPM:FileViewPM;

		[Inject]
		public var aigGridViewPM:AigGridViewPM;

		[Inject]
		public var aigPM:AigPM;

		public function ImportWizardPM()
		{
			super("AIGUI");
		}

		override public function initByComponent():void
		{
			super.initByComponent();
			fileViewPM.onInitializedByWizard();
			aigGridViewPM.onInitializedByWizard();
		}

		override public function get title():String
		{
			var key:String = "import.wizard.title";
			return ResourceManager.getInstance().getString(_bundle, key);
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			var rm:IResourceManager = ResourceManager.getInstance();
			var alertLabels:Vector.<String> = new <String>[
				rm.getString('APIUI', 'alert.ok')
				, rm.getString('APIUI', 'alert.cancel')
			];

			VhutAlert.show(rm.getString('AIGUI', 'alert.message.import')
				, rm.getString('AIGUI', 'alert.title.confirm')
				, alertLabels
				, null,
				function(event:CloseEvent):void
				{
					switch(event.detail)
					{
						case 0:
							dispatchEvent(AigListEvent.newCreateAigListEvent(importingAigs.aigList.toArray()));
							break;
					}
				}
			);
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			aigPM.isImportWizardOpen = false;
			return true;
		}
	}
}