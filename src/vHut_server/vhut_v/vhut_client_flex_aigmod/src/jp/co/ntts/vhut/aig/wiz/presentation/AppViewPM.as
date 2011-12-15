/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.aig.domain.Apps;
	import jp.co.ntts.vhut.aig.domain.EditingAig;
	import jp.co.ntts.vhut.comp.wiz.domain.IInitiator;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.form.application.SearchEvent;

	import org.spicefactory.lib.reflect.types.Void;

	import spark.events.IndexChangeEvent;

	/**
	 * アプリケーションインスタンスグループ追加・編集・複製ウィザードの
	 * アプリケーション選択時のViewのPMクラス.
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
	public class AppViewPM extends EventDispatcher implements IValidator,IInitiator
	{
		public function AppViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var editingAig:EditingAig;

		[Inject]
		[Bindable]
		public var apps:Apps;

		public function initiate():void
		{
			apps.updateApps();
			apps.selectApplicationById(editingAig.applicationId);
		}

		public function get isValid():Boolean
		{
			if(apps.targetApp)
			{
				editingAig.applicationId = apps.targetApp.id;
				return true;
			}
			else
			{
				return false;
			}
		}

		public function selectApplicationSecurityGroup(appSecurityGroup:ApplicationSecurityGroup):void
		{
			apps.targetApplicationElement = appSecurityGroup;
		}

		public function selectApplicationVm(appVm:ApplicationVm):void
		{
			apps.targetApplicationElement = appVm;
		}

		/**
		 * 選択中のアプリケーション・セキュリティグループを変更する.
		 * @param item 新しいアプリケーション・セキュリティグループ
		 */
		public function selectApplicationElement(item:Object):void
		{
			apps.targetApplicationElement = item;
		}

		public function searchAppHandler(event:SearchEvent):void
		{
			apps.setAppsfilter(event.keywords);
		}

		public function selectApplicationHandler(event:IndexChangeEvent):void
		{
			apps.targetApp = apps.apps.getItemAt(event.newIndex) as Application;
		}
	}
}