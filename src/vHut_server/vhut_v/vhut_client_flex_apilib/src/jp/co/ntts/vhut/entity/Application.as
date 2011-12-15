/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import com.adobe.cairngorm.integration.data.DataCache;

	import flash.events.Event;
	import flash.geom.Point;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.comp.va.domain.Topology;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.util.CloneUtil;
	import jp.co.ntts.vhut.util.CollectionUtil;
	import jp.co.ntts.vhut.util.IClonable;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.resources.ResourceManager;
	import mx.utils.ObjectUtil;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Application")]
	/**
	 * Application Entity Class.
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
    public class Application extends ApplicationBase implements IClonable
	{
		public static const CHANGE_STATUS:String = "changeStatus";
		public static const CHANGE_TOPOLOGY:String = "changeTopology"
		public static const CHANGE_VHUT_USER:String = "changeVhutUser"

		public function Application()
		{
			super();
			id = 0;
			_applicationVmList = new ArrayCollection();
			_applicationSecurityGroupList = new ArrayCollection();
			createElements();
			name = "New Application";
			status = ApplicationStatus.NONE;
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set status(value:ApplicationStatus):void {
			_status = value;
			dispatchEvent(new Event(CHANGE_STATUS));
		}

		override public function set applicationSecurityGroupList(value:ArrayCollection):void {
			_applicationSecurityGroupList.removeAll();
			_applicationSecurityGroupList.addAll(value);
			for each (var asg:ApplicationSecurityGroup in value)
			{
				if(asg.applicationId == id)
				{
					asg.application = this;
				}
			}
		}

		override public function set applicationVmList(value:ArrayCollection):void {
			_applicationVmList.removeAll();
			_applicationVmList.addAll(value);
			for each (var avm:ApplicationVm in value)
			{
				if(avm.applicationId == id)
				{
					avm.application = this;
				}
			}
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * 編集可能です.
		 * @return 編集可能な時はTrue;
		 */
		public function get isEditable():Boolean
		{
			return status.equals(ApplicationStatus.DEACTIVE);
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * アプリケーションの状態名.
		 * @return アプリケーションの状態名
		 */
		public function get statusName():String
		{
			return ResourceManager.getInstance().getString("APIUI", "application.status."+status.name);
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * アプリケーションの状態名.
		 * @return アプリケーションの状態名
		 */
		public function get nextStatus():ApplicationStatus
		{
			switch(status.name)
			{
				case ApplicationStatus.ACTIVE.name:
					return ApplicationStatus.DEACTIVE;
				case ApplicationStatus.DEACTIVE.name:
					return ApplicationStatus.ACTIVE;
				default:
					return null;
			}
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * アプリケーションの状態名.
		 * @return アプリケーションの状態名
		 */
		public function get statusActionName():String
		{
			if (nextStatus == null)
			{
				return "";
			}
			switch (nextStatus.name)
			{
				case ApplicationStatus.ACTIVE.name:
					return ResourceManager.getInstance().getString("APIUI", "application.status.action.activate");
				case ApplicationStatus.DEACTIVE.name:
					return ResourceManager.getInstance().getString("APIUI", "application.status.action.deactivate");
				default:
					return "";
			}
		}

		///////////////////////////////////////////////////////////////////////////
		//  CRUD VM
		///////////////////////////////////////////////////////////////////////////

		/**
		 * ApplicationVmを追加します.
		 * @param avm 対象のApplicationVm
		 * @param posx X座標
		 * @param posy Y座標
		 * @return 追加されたApplicationVm
		 */
		public function addApplicationVm(avm:ApplicationVm, posx:Number=0, posy:Number=0):ApplicationVm
		{
			var privateId:Number = topology.addVm();
			topology.setVmPos(privateId, new Point(posx, posy));
			avm.privateId = privateId;
			avm.applicationId = id;
			avm.application = this;
			applicationVmList.addItem(avm);
			return avm;
		}

		/**
		 * ApplicationVmを移動します.
		 * @param avm 対象のApplicationVm
		 * @param posx X座標
		 * @param posy Y座標
		 * @return 移動されたApplicationVm
		 */
		public function moveApplicationVm(avm:ApplicationVm, posx:Number, posy:Number):ApplicationVm
		{
			var privateId:Number = avm.privateId;
			if(!privateId)
				throw Error("ApplicationVm privateIp is not assigned.");
			topology.setVmPos(privateId, new Point(posx, posy));
			return avm;
		}

		/**
		 * ApplicationVmを削除します.
		 * @param avm 対象のApplicationVm
		 * @return 処理されたApplicationVm
		 */
		public function removeApplicationVm(avm:ApplicationVm):ApplicationVm
		{
			var index:int = applicationVmList.getItemIndex(avm);
			if(index >= 0)
			{
				topology.removeVm(avm.privateId);
				avm.clean();
				applicationVmList.removeItemAt(index);
			}
			return avm;
		}

		///////////////////////////////////////////////////////////////////////////
		//  CRUD SecurityGroup
		///////////////////////////////////////////////////////////////////////////

		/**
		 * ApplicationSecurityGroupを追加します.
		 * @param asg 対象のApplicationSecurityGroup
		 * @param posx X座標
		 * @param posy Y座標
		 * @return 追加されたApplicationSecurityGroup
		 */
		public function addApplicationSecurityGroup(asg:ApplicationSecurityGroup, posx:Number=0, posy:Number=0):ApplicationSecurityGroup
		{
			var privateId:Number = topology.addSg();
			topology.setSgPos(privateId, new Point(posx, posy));
			asg.privateId = privateId;
			asg.applicationId = id;
			asg.application = this;
			applicationSecurityGroupList.addItem(asg);
			return asg;
		}

		private function getUnusedAppSgPrivateId():Number
		{
			updateAppSgPrivateIdMap();
			for(var i:int=1; i<1000; i++)
			{
				if(!_appSgPrivateIdMap[i])
				{
					return i;
				}
			}
			return 0;
		}

		private function updateAppSgPrivateIdMap():void
		{
			_appSgPrivateIdMap = new Array();
			for each(var appSg:ApplicationSecurityGroup in applicationSecurityGroupList)
			{
				_appSgPrivateIdMap[appSg.privateId] = appSg;
			}
		}

		private var _appSgPrivateIdMap:Array;

		/**
		 * ApplicationSecurityGroupを移動します.
		 * @param asg 対象のApplicationSecurityGroup
		 * @param posx X座標
		 * @param posy Y座標
		 * @return 移動されたApplicationSecurityGroup
		 */
		public function moveApplicationSecurityGroup(asg:ApplicationSecurityGroup, posx:Number, posy:Number):ApplicationSecurityGroup
		{
			var privateId:Number = asg.privateId;
			if(!privateId)
				throw Error("ApplicationSecurityGroup privateIp is not assigned.");
			topology.setSgPos(privateId, new Point(posx, posy));
			return asg;
		}

		/**
		 * ApplicationSecurityGroupを削除します.
		 * @param asg 対象のApplicationSecurityGroup
		 * @return 削除されたApplicationSecurityGroup
		 */
		public function removeApplicationSecurityGroup(asg:ApplicationSecurityGroup):ApplicationSecurityGroup
		{
			var index:int = applicationSecurityGroupList.getItemIndex(asg);
			if(index >= 0)
			{
				topology.removeSg(asg.privateId);
				asg.clean();
				applicationSecurityGroupList.removeItemAt(index);
			}
			return asg;
		}

		///////////////////////////////////////////////////////////////////////////
		//  CRUD NetworkAdapter
		///////////////////////////////////////////////////////////////////////////

		/**
		 * VMとSGを接続します.
		 * @param link 接続位置の詳細
		 * @return  生成されたマップ
		 *
		 */
		public function addLink(link:VaLink):ApplicationVmSecurityGroupMap
		{
			//VMとSGの探索
			var avm:ApplicationVm = findApplicationVmByPrivateId(link.vmId);
			if(avm == null) return null;
			var asg:ApplicationSecurityGroup = findApplicationSecurityGroupByPrivateId(link.sgId);
			if(asg == null) return null;
			//ネットワークアダプタの作成（下側）
			var nwa:NetworkAdapter = NetworkAdapter.newNetworkAdapter(avm.vm, asg.securityGroup);
			if(nwa == null) return null;
			//マップの作成（上側）
			var map:ApplicationVmSecurityGroupMap = ApplicationVmSecurityGroupMap.newApplicationVmSecurityGroupMap(avm, asg);
			//描画更新
			topology.addLink(link);
			return map;
		}

		public function updateLink(link:VaLink):ApplicationVmSecurityGroupMap
		{
			var oldLink:VaLink = topology.getLinkById(link.id);
			if (!oldLink)
				return null;

			if(oldLink.vmId != link.vmId || oldLink.sgId != link.sgId)
			{
				//VM/SGの変更
				removeLink(oldLink);
				return addLink(link);
			}
			else
			{
				//外観のみ変更
				topology.updateLink(link);
				return null;
			}
		}

		public function removeLink(link:VaLink = null):ApplicationVmSecurityGroupMap
		{
			//VMとSGの探索
			var avm:ApplicationVm = findApplicationVmByPrivateId(link.vmId);
			if(avm == null) return null;
			var asg:ApplicationSecurityGroup = findApplicationSecurityGroupByPrivateId(link.sgId);
			if(asg == null) return null;
			//NetworkAdapterの探索
			if(avm.vm != null && asg.securityGroup != null)
			{
				var networkAdapter:NetworkAdapter = avm.vm.getNetworkAdapterWithConnectionTo(asg.securityGroup);
				if(networkAdapter != null)
				{
					networkAdapter.clean();
				}
			}
			//ネットワークアダプタの作成（下側）
			//マップの作成（上側）
			avm.disconnectTo(asg);
			asg.disconnectTo(avm);
			//描画更新
			topology.removeLink(link);
			return null;
		}

		/**
		 * エンティティを複製します.
		 * @param withId IDも含めて複製します.
		 * @param deep 関連要素を含めて複製します.
		 * @param ref 置換されたリファレンス
		 * @return 複製されたエンティティ.
		 */
		public function clone(withId:Boolean = true, deep:Boolean = true, ref:Dictionary = null):*
		{
			var result:Application
			if(ref == null)
			{
				ref = new Dictionary();
			}
			else
			{
				result = ref[this] as Application;
				if(result != null)
				{
					return result;
				}
			}
			result = new Application();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.ApplicationBase");
			ref[this] = result;
			//IDの初期化
			if(!withId)
			{
				result.id = 0;
			}
			if(deep)
			{
				result.applicationVmList = CloneUtil.cloneCollection(applicationVmList, withId, deep, ref);
				result.applicationSecurityGroupList = CloneUtil.cloneCollection(applicationSecurityGroupList, withId, deep, ref);
			}
			//不必要なリンクの削除
			result.applicationInstanceGroupList = new ArrayCollection();
			result.releasedApplicationList = new ArrayCollection();
			result.termList = new ArrayCollection();

			return result;
		}

		///////////////////////////////////////////////////////////////////////////
		//  Parameters for Flex Componet View
		///////////////////////////////////////////////////////////////////////////


		override public function set vhutUser(value:VhutUser):void
		{
			if(value != _vhutUser)
			{
				super.vhutUser = value;

				if (value)
				{
					vhutUserId = value.id;
				}
				else
				{
					vhutUserId = 0;
				}

				if(_ownerList == null)
				{
					_ownerList = new ArrayCollection();
					_ownerList.addItem(value);
				}
				else
				{
					_ownerList.removeAll();
					_ownerList.addItem(value);
				}
				dispatchEvent(new Event(CHANGE_VHUT_USER));
			}
		}

		[Bindable("changeVhutUser")]
		public function get ownerList():IList
		{
			if(_ownerList == null)
			{
				_ownerList = new ArrayCollection();
				_ownerList.addItem(vhutUser);
			}
			return _ownerList;
		}
		private var _ownerList:ArrayCollection;

		///////////////////////////////////////////////////////////////////////////
		//  Graphic Parameters, do not upload
		///////////////////////////////////////////////////////////////////////////

		[Transient]
		/** 描画要素 */
		public function get elements():IList
		{
			return _elements;
		}
		private var _elements:ArrayCollection;

		private function createElements():void
		{
			_elements = CollectionUtil.fusion(applicationVmList, applicationSecurityGroupList);
			_topology = new Topology(_elements);
		}

		override public function set structure(value:String):void {
			_structure = value;
			_topology.structure = _structure;
			dispatchEvent(new Event(CHANGE_TOPOLOGY));
		}
		override public function get structure():String {
			if(_topology == null)
				return _structure;
			return _topology.toString();
		}

		[Transient]
		[Bindable("changeTopology")]
		public function get topology():Topology
		{
			return _topology;
		}
		private var _topology:Topology;

		///////////////////////////////////////////////////////////////////////////
		//  Common Private.
		///////////////////////////////////////////////////////////////////////////

		private function findApplicationVmByPrivateId(id:Number):ApplicationVm
		{
			for each (var avm:ApplicationVm in applicationVmList)
			{
				if(avm.privateId == id)
				{
					return avm;
				}
			}
			return null;
		}

		private function findApplicationSecurityGroupByPrivateId(id:Number):ApplicationSecurityGroup
		{
			for each (var asg:ApplicationSecurityGroup in applicationSecurityGroupList)
			{
				if(asg.privateId == id)
				{
					return asg;
				}
			}
			return null;
		}

    }
}