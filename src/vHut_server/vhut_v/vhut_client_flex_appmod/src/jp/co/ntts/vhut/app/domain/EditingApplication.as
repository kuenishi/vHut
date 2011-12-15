/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.app.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.comp.va.domain.IEditableVaDomain;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.dto.AdditionalDiskDto;
	import jp.co.ntts.vhut.dto.SwitchTemplateDto;
	import jp.co.ntts.vhut.entity.Application;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationStatus;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMap;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.entity.Disk;
	import jp.co.ntts.vhut.entity.NetworkAdapter;
	import jp.co.ntts.vhut.entity.SecurityGroup;
	import jp.co.ntts.vhut.entity.Template;
	import jp.co.ntts.vhut.entity.Vm;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.utils.ObjectUtil;

	/**
	 * 編集対象のアプリケーションを管理するドメインオブジェクト
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
	public class EditingApplication extends EventDispatcher
	{
		/** アプリケーションが更新されました. */
		public static const UPDATE_APP:String="updateApp";
		/** アプリケーションが更新されました. */
		public static const UPDATE_APP_VM:String="updateAppVm";
		/** アプリケーションが更新されました. */
		public static const UPDATE_APP_SG:String="updateAppSg";
		/** アプリケーション要素が選択されました. */
		public static const SELECT_APP_ELEMENT:String="selectAppElement";

		public function EditingApplication()
		{
		}

		[Inject]
		[Bindalbe]
		public var applications:Applications;

		[Inject]
		[Bindable]
		public var templates:Templates;

		[Inject]
		[Bindable]
		public var session:Session;

		/**
		 * アプリケーションを新規に作成します.
		 * <p> 新規に作成されたアプリケーションはターゲットにセットされます.
		 * @return
		 */
		public function setNewApplication():Application
		{
			targetApplication=new Application();
			assignCurrentUserAsOwner();
			return targetApplication;
		}

		/**
		 * アプリケーションを複製します.
		 * <p> 新規に作成されたアプリケーションはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setRegisteredApplication(source:Application=null):Application
		{
			if (source == null && applications.targetApplication != null)
			{
				source=applications.targetApplication;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			if (source.id <= 0)
			{
				throw new Error("source is not registered.");
			}
			try
			{
//				targetApplication=source.clone(true, true);
				targetApplication=ObjectUtil.copy(source) as Application;
			}
			catch(e:Error)
			{
				trace(e.message);
			}
			assignCurrentUserAsOwner();
			return targetApplication;
		}

		/**
		 * アプリケーションを複製します.
		 * <p> 新規に作成されたアプリケーションはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setClonedApplication(source:Application):Application
		{
			if (source == null && applications.targetApplication != null)
			{
				source=applications.targetApplication;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			try
			{
//				targetApplication=source.clone(true, true);
				targetApplication=ObjectUtil.copy(source) as Application;
			}
			catch(e:Error)
			{
				trace(e.message);
			}
			assignCurrentUserAsOwner();
			return targetApplication;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplication
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("updateApp")]
		/**
		 * 選択中のアプリケーションデータ.
		 */
		public function get targetApplication():Application
		{
			return _targetApplication;
		}

		public function set targetApplication(value:Application):void
		{
			if (_targetApplication == value) return;

			_targetApplication=value;
			//選択の解除
			targetApplicationElement=null;

			//vmリストの作成
			_vms = new ArrayCollection();
			_vmMap = new Dictionary();
			if(_targetApplication != null && _targetApplication.applicationVmList != null)
			{
				for each(var avm:ApplicationVm in _targetApplication.applicationVmList)
				{
					if(avm.vm != null)
					{
						_vms.addItem(avm.vm);
						_vmMap[avm.vm] = avm;
					}
				}
			}
			dispatchEvent(new Event(UPDATE_APP_VM));

			//securityGroupリストの作成
			_securityGroups = new ArrayCollection();
			_securityGroupMap = new Dictionary();
			if(_targetApplication != null && _targetApplication.applicationSecurityGroupList != null)
			{
				for each(var asg:ApplicationSecurityGroup in _targetApplication.applicationSecurityGroupList)
				{
					if(asg.securityGroup != null)
					{
						_securityGroups.addItem(asg.securityGroup);
						_securityGroupMap[asg.securityGroup] = asg;
					}
				}
			}
			dispatchEvent(new Event(UPDATE_APP_SG));

			dispatchEvent(new Event(UPDATE_APP));
		}
		private var _targetApplication:Application;

		[Bindable("updateApp")]
		/**
		 * ターゲットのアプリケーションがサーバに登録されています.
		 */
		public function get isTargetApplicationRegistered():Boolean
		{
			return (_targetApplication == null || _targetApplication.id != 0);
		}

		private function assignCurrentUserAsOwner():Boolean
		{
			if(session == null || session.user == null)
				return false;
			if(targetApplication == null)
				return false;

			targetApplication.vhutUser = session.user;
			targetApplication.vhutUserId = session.user.id;

			return true;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetApplicationElement vm/securityGroup
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("selectAppElement")]
		/**
		 * 選択中のアプリケーション要素.
		 * VMとSecurityGroupのどちらか。
		 */
		public function get targetApplicationElement():Object
		{
			return _targetApplicationElement;
		}

		public function set targetApplicationElement(value:Object):void
		{
			_targetApplicationElement=value;
			_targetApplicationVm=_targetApplicationElement as ApplicationVm;
			_targetApplicationSecurityGroup=_targetApplicationElement as ApplicationSecurityGroup;
			dispatchEvent(new Event(SELECT_APP_ELEMENT));
		}
		private var _targetApplicationElement:Object;

		[Bindable("selectAppElement")]
		/**
		 * ターゲットのアプリケーション内で選択されているApplicationVm
		 */
		public function get targetApplicationVm():ApplicationVm
		{
			return _targetApplicationVm;
		}
		private var _targetApplicationVm:ApplicationVm;

		[Bindable("selectAppElement")]
		/**
		 * ターゲットのアプリケーション内で選択されているSecurityGroup
		 */
		public function get targetApplicationSecurityGroup():ApplicationSecurityGroup
		{
			return _targetApplicationSecurityGroup;
		}
		private var _targetApplicationSecurityGroup:ApplicationSecurityGroup;

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  For VaEditor
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("updateAppVm")]
		/** vmリストの取得 */
		public function get vms():IList
		{
			return _vms;
		}
		private var _vms:IList;
		private var _vmMap:Dictionary;

		[Bindable("updateAppSg")]
		/** securityGroupリストの取得 */
		public function get securityGroups():IList
		{
			return _securityGroups;
		}
		private var _securityGroups:IList;
		private var _securityGroupMap:Dictionary;

		[Bindable("updateApp")]
		/** 描画データの取得 */
		public function get structure():String
		{
			return targetApplication.structure;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  For VaEditor : Selection
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * Vmを選択します.
		 * @param vm
		 */
		public function selectVm(vm:Vm):void
		{
			var avm:ApplicationVm = _vmMap[vm];
			if(avm != null) targetApplicationElement = avm;
		}

		/**
		 * SecurityGroupを選択します.
		 * @param securityGroup
		 */
		public function selectSecurityGroup(securityGroup:SecurityGroup):void
		{
			var asg:ApplicationSecurityGroup = _securityGroupMap[securityGroup];
			if(asg != null) targetApplicationElement = asg;
		}

		/**
		 * 選択を解除します.
		 */
		public function unselect():void
		{
			targetApplicationElement = null;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  For VaEditor : Control VM
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * Vmを追加します.
		 * @param template 参照するテンプレート
		 * @return Vm
		 */
		public function addVm(baseTemplate:BaseTemplate, posx:Number, posy:Number):ApplicationVm
		{
			var vm:Vm = Vm.newVm(baseTemplate.template, templates.defaultSpecTemplate);
			var appVm:ApplicationVm = new ApplicationVm();
			appVm.vm = vm;
			appVm.imageUrl = baseTemplate.imageUrl;
			appVm = targetApplication.addApplicationVm(appVm, posx, posy);
			//update vmMap
			_vmMap[vm] = appVm;
			return appVm;
		}

		/**
		 * Vmを移動します.
		 * @param appVm 移動対象のアプリケーションVM
		 * @return アプリケーションVM
		 */
		public function moveVm(appVm:ApplicationVm, posx:Number, posy:Number):ApplicationVm
		{
			targetApplication.moveApplicationVm(appVm, posx, posy);
			return appVm;
		}

		/**
		 * Vmを削除します.
		 * @param vm VM
		 * @return Vm
		 */
		public function removeVm(avm:ApplicationVm):ApplicationVm
		{
			//ApplicationVmの削除
			targetApplication.removeApplicationVm(avm);
			//update vmMap
			delete _vmMap[avm.vm]
			return avm;
		}

		/**
		 * Vmを起動します.
		 * @param vm Vm
		 * @return Vm
		 */
		public function startVm(vm:Vm):Vm
		{
			return vm;
		}

		/**
		 * Vmを停止します.
		 * @param vm Vm
		 * @return Vm
		 */
		public function stopVm(vm:Vm):Vm
		{
			return vm;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  For VaEditor : Control SecurityGroup
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * SecurityGroupを追加します.
		 * @param template 参照するスイッチテンプレート
		 * @return セキュリティグループ
		 */
		public function addSecurityGroup(template:SwitchTemplateDto, posx:Number, posy:Number):ApplicationSecurityGroup
		{
			var securityGroup:SecurityGroup = new SecurityGroup();
			var appSg:ApplicationSecurityGroup = new ApplicationSecurityGroup();
			appSg.securityGroup = securityGroup;
			appSg = targetApplication.addApplicationSecurityGroup(appSg, posx, posy);
			//update securityGroupMap
			_securityGroupMap[securityGroup] = appSg;
			return appSg;
		}

		/**
		 * SecurityGroupを移動します.
		 * @param appSg 移動対象のアプリケーションのセキュリティグループ
		 * @return アプリケーションVM
		 */
		public function moveSecurityGroup(appSg:ApplicationSecurityGroup, posx:Number, posy:Number):ApplicationSecurityGroup
		{
			targetApplication.moveApplicationSecurityGroup(appSg, posx, posy);
			return appSg;
		}

		/**
		 * SecurityGroupを削除します.
		 * @param vm VM
		 * @return セキュリティグループ
		 */
		public function removeSecurityGroup(asg:ApplicationSecurityGroup):ApplicationSecurityGroup
		{
			//ApplicationVmの削除
			targetApplication.removeApplicationSecurityGroup(asg);
			//update securityGroupMap
			delete _securityGroupMap[asg.securityGroup];
			return asg;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  For VaEditor : Control Network
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * 接続を追加します.
		 * @param remoteLink 追加する接続情報
		 * @return アプリケーションのVMとSGの関連マップ
		 */
		public function addLink(remoteLink:VaLink):ApplicationVmSecurityGroupMap
		{
			if(targetApplication)
			{
				return targetApplication.addLink(remoteLink);
			}
			else
			{
				return null;
			}
		}

		/**
		 * 接続を移動します.
		 * @param remoteLink 更新する接続情報
		 * @return アプリケーションのVMとSGの関連マップ
		 */
		public function updateLink(remoteLink:VaLink):ApplicationVmSecurityGroupMap
		{
			if(targetApplication)
			{
				return targetApplication.updateLink(remoteLink);
			}
			else
			{
				return null;
			}
		}

		/**
		 * 接続を削除します.
		 * @param remoteLink 削除する接続情報
		 * @return アプリケーションのVMとSGの関連マップ
		 */
		public function removeLink(remoteLink:VaLink):ApplicationVmSecurityGroupMap
		{
			if(targetApplication)
			{
				return targetApplication.removeLink(remoteLink);
			}
			else
			{
				return null;
			}
		}

		/**
		 * NetworkAdapterを追加します.
		 * @param vm 接続するVm
		 * @param securityGroup 接続するSecurityGroup
		 * @return ネットワークアダプター
		 */
		public function connect(avm:ApplicationVm, asg:ApplicationSecurityGroup):ApplicationVmSecurityGroupMap
		{
			if(targetApplication == null) return null;
//			return targetApplication.connectById(avm, asg);
			return null;
		}

		/**
		 * NetworkAdapterを削除します.
		 * @param networkAdapter 切断するネットワークアダプター
		 * @return ネットワークアダプター
		 */
		public function removeNetworkAdapter(networkAdapter:NetworkAdapter):NetworkAdapter
		{
			var avm:ApplicationVm = _vmMap[networkAdapter.vm] as ApplicationVm;
			if(avm == null) return null;
			var asg:ApplicationSecurityGroup = _securityGroupMap[networkAdapter.securityGroup] as ApplicationSecurityGroup;
			if(asg == null) return null;
			avm.disconnectTo(asg);
			asg.disconnectTo(avm);
			networkAdapter.clean();
			return null;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  For VaEditor : Control Disk
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * Diskを追加します.
		 * @param vm ディスクを追加するVm
		 * @param template 追加ディスクテンプレート
		 * @return ディスク
		 */
		public function addDisk(disk:Disk):Disk
		{
			var vm:Vm = disk.vm;
			if(vm)
			{
				return vm.addDisk(disk);
			}
			else
			{
				return null;
			}
		}

		/**
		 * Diskを削除します.
		 * @param disk 削除するディスク
		 * @return ディスク
		 */
		public function removeDisk(disk:Disk):Disk
		{
			var vm:Vm = disk.vm;
			if(vm)
			{
				return vm.removeDisk(disk);
			}
			else
			{
				return null;
			}
		}

		protected function findVmById(id:Number):Vm
		{
			for each (var vm:Vm in vms)
			{
				if(vm.id == id)
					return vm;
			}
			return null;
		}

	}
}