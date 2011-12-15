/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.wiz.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.app.TermEvent;
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.app.presentation.AppPM;
	import jp.co.ntts.vhut.comp.wiz.presentation.WizardPM;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent;
	import jp.co.ntts.vhut.core.ICommandOwner;
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;

	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;

	[Event(type="jp.co.ntts.vhut.app.TermEvent", name="setTerm")]
	[ManagedEvents("navigateTo, setTerm")]
	/**
	 * <p>アプリケーション起動期間予約ウィザードのモデル.
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
	public class TermWizardPM extends WizardPM implements ICommandOwner
	{

		[Inject]
		public var appPM:AppPM;

		[Inject]
		public var termViewPM:TermViewPM;

		public function TermWizardPM()
		{
			super("APPUI");
		}

		override public function initByComponent():void
		{
			super.initByComponent();
			termViewPM.onInitializedByWizard();
		}

		override public function get title():String
		{
			var key:String = "term.wizard.title";
			return ResourceManager.getInstance().getString(_bundle, key);
		}

		override protected function onWizardStepsComplete(event:Event):void
		{
			var rm:IResourceManager = ResourceManager.getInstance();
			VhutAlert.show(rm.getString('APPUI', 'alert.message.term')
				, rm.getString('APPUI', 'alert.title.confirm')
				, VhutAlert.LABELS_OK_CANCEL
				, null,
				function(event:CloseEvent):void
				{
					switch(event.detail)
					{
						case 0:
							dispatchEvent(TermEvent.newSetTermEvent(termViewPM.applications.targetApplication.id, termViewPM.editingTermList));
							break;
					}
				}
			);
		}

		override public function result(command:BaseCommand, object:Object):Boolean
		{
			appPM.isTermWizardOpen = false;
			return true;
		}
	}
}