/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.tmp.presentation.wiz
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.comp.wiz.domain.IInitiator;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.entity.Template;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.tmp.domain.EditingBaseTemplate;
	import jp.co.ntts.vhut.tmp.domain.UnregisteredTemplates;

	import spark.events.IndexChangeEvent;

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
	public class SelectViewPM extends EventDispatcher implements IValidator,IInitiator
	{
		public function SelectViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var editingBaseTemplate:EditingBaseTemplate;

		[Inject]
		[Bindable]
		public var unregisteredTemplates:UnregisteredTemplates;

		public function initiate():void
		{
			unregisteredTemplates.updateUnregisteredTemplates();
			unregisteredTemplates.clearUnregisteredTemplatesFilter();
			unregisteredTemplates.targetUnregisteredTemplate=null;
		}

		public function get isValid():Boolean
		{
			return editingBaseTemplate.targetBaseTemplate
				&& editingBaseTemplate.targetBaseTemplate.template;
		}

		public function selectUnregisteredTemplateHandler(event:IndexChangeEvent):void
		{
			var template:Template = unregisteredTemplates.unregisteredTemplates.getItemAt(event.newIndex) as Template;
			unregisteredTemplates.targetUnregisteredTemplate = template;
			editingBaseTemplate.targetTemplate = template;
		}

		public function searchUnregisteredTemplatesHandler(event:SearchEvent):void
		{
			unregisteredTemplates.setUnregisteredTemplatesFilter(event.keywords);
		}
	}
}