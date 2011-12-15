/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.tmp.presentation.wiz
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.comp.wiz.domain.IInitiator;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.tmp.domain.EditingBaseTemplate;

	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	/**
	 * <p>ユーザ編集ウィザードの
	 * プロパティを入力する画面のPM
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
	public class PropViewPM extends EventDispatcher implements IValidator,IInitiator
	{
		public function PropViewPM()
		{
			resourceManager = ResourceManager.getInstance();
		}

		protected var resourceManager:IResourceManager;

		[Inject]
		[Bindable]
		public var editingBaseTemplate:EditingBaseTemplate;

		[Bindable]
		public var baseTemplateNameErrorMessage:String;

		public function initiate():void
		{
		}

		public function get isValid():Boolean
		{
			var result:Boolean = true;
			baseTemplateNameErrorMessage = null;

			if(!editingBaseTemplate.targetBaseTemplate.name)
			{
				baseTemplateNameErrorMessage = resourceManager.getString("APIUI", "validate.required");
				result = false;
			}

			return result;
		}
	}
}