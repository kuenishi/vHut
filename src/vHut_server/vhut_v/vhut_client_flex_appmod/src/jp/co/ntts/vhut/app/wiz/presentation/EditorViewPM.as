/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app.wiz.presentation
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.app.domain.EditingApplication;
	import jp.co.ntts.vhut.app.domain.Templates;
	import jp.co.ntts.vhut.comp.va.application.AddEvent;
	import jp.co.ntts.vhut.comp.va.application.RemoveEvent;
	import jp.co.ntts.vhut.comp.va.application.UpdateEvent;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.dto.SpecDto;
	import jp.co.ntts.vhut.dto.SwitchTemplateDto;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.entity.Disk;
	import jp.co.ntts.vhut.entity.Vm;
	import jp.co.ntts.vhut.form.application.SearchEvent;

	import mx.collections.IList;

	[Event(name="getAllBaseTemplate", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getAllSwitchTemplate", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getAllDiskTemplate", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getAllSpecTemplate", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[ManagedEvents(names="getAllBaseTemplate, getAllSwitchTemplate, getAllDiskTemplate, getAllSpecTemplate")]
	/**
	 * アプリケーションウィザードのアプリケーション編集時のViewのPMクラス.
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
	public class EditorViewPM extends EventDispatcher implements IValidator
	{
		public function EditorViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var editingApplication:EditingApplication;

		[Inject]
		[Bindable]
		public var templates:Templates;

		public function get isValid():Boolean
		{
			return true;
		}

		/**
		 * ベーステンプレートをサーバから取得します.
		 */
		public function getAllBaseTemplateAbstractionList():void
		{
			dispatchEvent(new GetAllEvent(GetAllEvent.BASE_TEMPLATE));
		}

		/**
		 * スイッチテンプレートをサーバから取得します.
		 */
		public function getAllSwitchTemplateAbstractionList():void
		{
			dispatchEvent(new GetAllEvent(GetAllEvent.SWITCH_TEMPLATE));
		}

		/**
		 * ディスクテンプレートをサーバから取得します.
		 */
		public function getAllDiskTemplateAbstractionList():void
		{
			dispatchEvent(new GetAllEvent(GetAllEvent.DISK_TEMPLATE));
		}

		/**
		 * テンプレートをすべて更新します.
		 */
		public function updateAllTemplates():void
		{
			getAllBaseTemplateAbstractionList();
			getAllDiskTemplateAbstractionList();
			getAllSwitchTemplateAbstractionList();
			getAllSpecAbstractionList();
		}

		/**
		 * スペックテンプレートをサーバから取得します.
		 */
		public function getAllSpecAbstractionList():void
		{
			dispatchEvent(new GetAllEvent(GetAllEvent.SPEC_TEMPLATE));
		}

		/**
		 * 選択中のアプリケーション要素を変更する.
		 * @param item 新しいアプリケーションVM
		 */
		public function selectApplicationElement(item:Object):void
		{
			editingApplication.targetApplicationElement = item;
		}

		/**
		 * 選択中のアプリケーションVMを変更する.
		 * @param item 新しいアプリケーションVM
		 */
		public function selectApplicationVm(item:ApplicationVm):void
		{
			editingApplication.targetApplicationElement = item;
		}

		/**
		 * 選択中のアプリケーション・セキュリティグループを変更する.
		 * @param item 新しいアプリケーション・セキュリティグループ
		 */
		public function selectApplicationSecurityGroup(item:ApplicationSecurityGroup):void
		{
			editingApplication.targetApplicationElement = item;
		}

		public function changeSpec(spec:SpecDto):void
		{
			if(editingApplication.targetApplicationVm != null)
			{
				var vm:Vm = editingApplication.targetApplicationVm.vm;
				vm.specId = spec.id;
				vm.cpuCore = spec.cpuCore;
				vm.memory = spec.memory;
			}
		}

		public function getSpecIndex(vm:Vm):int
		{
			if(!isNaN(vm.specId) && vm.specId > 0 && templates != null)
			{
				var specs:IList = templates.specTemplates;
				for (var i:int=0; i<specs.length; i++)
				{
					var spec:SpecDto = specs[i];
					if(vm.specId == spec.id)
					{
						return i;
					}
				}
			}
			return 0;
		}

		/////////////////////////////////////////////////////
		//
		//  search
		//
		/////////////////////////////////////////////////////

		public function searchBaseTemplateHandler(event:SearchEvent):void
		{
			templates.filterBaseTemplates(event.keywords);
		}

		public function searchSwitchTemplateHandler(event:SearchEvent):void
		{
			templates.filterSwitchTemplates(event.keywords);
		}

		public function searchDiskTemplateHandler(event:SearchEvent):void
		{
			templates.filterDiskTemplates(event.keywords);
		}

		/////////////////////////////////////////////////////
		//
		//  temp methods
		//
		/////////////////////////////////////////////////////

		public function addVm(event:AddEvent):void
		{
			editingApplication.addVm(event.data as BaseTemplate, event.posx, event.posy);
		}

		public function addSg(event:AddEvent):void
		{
			editingApplication.addSecurityGroup(event.data as SwitchTemplateDto, event.posx, event.posy);
		}

		public function addLink(event:AddEvent):void
		{
			editingApplication.addLink(event.data as VaLink);
		}

		public function addDisk(event:AddEvent):void
		{
			editingApplication.addDisk(event.data as Disk);
		}

		public function updateVm(event:UpdateEvent):void
		{
			editingApplication.moveVm(event.data as ApplicationVm, event.posx, event.posy);
		}

		public function updateSg(event:UpdateEvent):void
		{
			editingApplication.moveSecurityGroup(event.data as ApplicationSecurityGroup, event.posx, event.posy);
		}

		public function updateLink(event:UpdateEvent):void
		{
			editingApplication.updateLink(event.data as VaLink);
		}

		public function removeVm(event:RemoveEvent):void
		{
			editingApplication.removeVm(event.data as ApplicationVm);
		}

		public function removeSg(event:RemoveEvent):void
		{
			editingApplication.removeSecurityGroup(event.data as ApplicationSecurityGroup);
		}

		public function removeLink(event:RemoveEvent):void
		{
			editingApplication.removeLink(event.data as VaLink);
		}

		public function removeDisk(event:RemoveEvent):void
		{
			editingApplication.removeDisk(event.data as Disk);
		}

		public function connect(vmNum:int, sgNum:int):void
		{
			var avm:ApplicationVm = editingApplication.targetApplication.applicationVmList.getItemAt(vmNum-1) as ApplicationVm;
			var asg:ApplicationSecurityGroup = editingApplication.targetApplication.applicationSecurityGroupList.getItemAt(sgNum-1) as ApplicationSecurityGroup;
			editingApplication.connect(avm, asg);
		}

//		public function addDisk():void
//		{
//			if(editingApplication.targetApplicationVm != null)
//			{
//				var vm:Vm = editingApplication.targetApplicationVm.vm;
//				var template:AdditionalDiskDto = templates.diskTemplates[0] as AdditionalDiskDto;
//				editingApplication.addDisk(vm, template);
//			}
//		}
	}
}