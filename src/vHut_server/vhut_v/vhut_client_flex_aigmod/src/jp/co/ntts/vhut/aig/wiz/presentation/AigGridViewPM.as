/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.aig.domain.Apps;
	import jp.co.ntts.vhut.aig.domain.ImportingAigs;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;

	/**
	 * アプリケーションインスタンスグループ一括ウィザードの
	 * アプリケーションインスタンスグループ編集時のViewのPMクラス.
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
	public class AigGridViewPM extends EventDispatcher implements IValidator
	{
		public function AigGridViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var importingAigs:ImportingAigs;

		[Inject]
		[Bindable]
		public var apps:Apps;

		public function onInitializedByWizard():void
		{
			apps.updateApps();
			apps.clearAppsFilter();
		}

		public function get isValid():Boolean
		{
//			return true;
			if(!importingAigs.isAllAiOnAigDefined)
			{
				return false;
			}
			if(!importingAigs.isAllUserOnAigDefined)
			{
				return false;
			}
			return true;
		}
	}
}