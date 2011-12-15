/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import jp.co.ntts.vhut.comp.va.domain.Topology;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.resources.ResourceManager;

	import org.osmf.metadata.IFacet;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ApplicationInstance")]
	/**
	 * ApplicationInstance Entity Class.
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
    public class ApplicationInstance extends ApplicationInstanceBase {
		public static const CHANGE_STATUS:String = "changeStatus";
		public static const CHANGE_TOPOLOGY:String = "changeTopology";

		public static function newAiWithUser(user:VhutUser):ApplicationInstance
		{
			var ai:ApplicationInstance = new ApplicationInstance();
			ai.vhutUser = user;
			return ai;
		}

		public function ApplicationInstance()
		{
			super();
			_applicationInstanceVmList = new ArrayCollection();
			_applicationInstanceSecurityGroupList = new ArrayCollection();
			_elements = CollectionUtil.fusion(_applicationInstanceVmList, _applicationInstanceSecurityGroupList);
			_topology = new Topology(_elements);
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set status(value:ApplicationInstanceStatus):void {
			_status = value;
			dispatchEvent(new Event(CHANGE_STATUS));
		}

		override public function set applicationInstanceSecurityGroupList(value:ArrayCollection):void {
			_applicationInstanceSecurityGroupList.removeAll();
			_applicationInstanceSecurityGroupList.addAll(value);
		}

		override public function set applicationInstanceVmList(value:ArrayCollection):void {
			_applicationInstanceVmList.removeAll();
			_applicationInstanceVmList.addAll(value);
		}

		override public function set vhutUser(value:VhutUser):void {
			super.vhutUser = value;
			if(_vhutUserWatcher)
				_vhutUserWatcher.unwatch();
			_vhutUserWatcher = BindingUtils.bindProperty(this, "vhutUserId", vhutUser, "id");
		}
		private var _vhutUserWatcher:ChangeWatcher;

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * 編集可能です.
		 * @return 編集可能な時はTrue;
		 */
		public function get isEditable():Boolean
		{
			return status.equals(ApplicationInstanceStatus.DEACTIVE);
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * アプリケーションの状態名.
		 * @return アプリケーションの状態名
		 */
		public function get statusName():String
		{
			return ResourceManager.getInstance().getString("APIUI", "applicationInstance.status."+status.name);
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * アプリケーションの状態名.
		 * @return アプリケーションの状態名
		 */
		public function get nextStatus():ApplicationInstanceStatus
		{
			switch(status.name)
			{
				case ApplicationInstanceStatus.ACTIVE.name:
					return ApplicationInstanceStatus.DEACTIVE;
				case ApplicationInstanceStatus.DEACTIVE.name:
					return ApplicationInstanceStatus.ACTIVE;
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
				case ApplicationInstanceStatus.ACTIVE.name:
					return ResourceManager.getInstance().getString("APIUI", "applicationInstance.status.action.activate");
				case ApplicationInstanceStatus.DEACTIVE.name:
					return ResourceManager.getInstance().getString("APIUI", "applicationInstance.status.action.deactivate");
				default:
					return "";
			}
		}

		override public function get releasedApplication():ReleasedApplication {
			return _releasedApplication;
		}
		override public function set releasedApplication(value:ReleasedApplication):void {
			_releasedApplication = value;
			structure = _releasedApplication.structure;
		}

		[Transient]
		public function get ownerFullName():String
		{
			if(vhutUser)
			{
				return vhutUser.fullName;
			}
			return "";
		}

		[Transient]
		public function get ownerAccount():String
		{
			if(vhutUser)
			{
				return vhutUser.account;
			}
			return "";
		}

		///////////////////////////////////////////////////////////////////////////
		//  Graphic Parameters, do not upload
		///////////////////////////////////////////////////////////////////////////

		[Transient]
		/** 描画要素 */
		public function get elements():IList
		{
			if(_elements == null)
			{
				updateElements();
			}
			return _elements;
		}
		private var _elements:ArrayCollection;

		/** 描画要素を更新します. */
		private function updateElements():void
		{
			_elements = CollectionUtil.fusion(applicationInstanceVmList, applicationInstanceSecurityGroupList);
		}

		[Transient]
		public function set structure(value:String):void {
			_structure = value;
			_topology.structure = _structure;
			dispatchEvent(new Event(CHANGE_TOPOLOGY));
		}
		public function get structure():String {
			if(_topology == null)
				return _structure;
			return _topology.toString();
		}
		private var _structure:String;

		[Transient]
		[Bindable("changeTopology")]
		public function get topology():Topology
		{
			return _topology;
		}
		private var _topology:Topology;
    }
}