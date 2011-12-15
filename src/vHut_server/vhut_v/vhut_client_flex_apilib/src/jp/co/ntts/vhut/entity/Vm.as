/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.dto.SpecDto;
	import jp.co.ntts.vhut.util.CloneUtil;
	import jp.co.ntts.vhut.util.IClonable;

	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Vm")]
	/**
	 * Vm Entity Class.
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
    public class Vm extends VmBase implements IClonable
	{
		/** ステータスの変更 */
		public static const CHANGE_STATUS:String = "changeStatus";

		public static function newVm(template:Template, spec:SpecDto):Vm
		{
			var vm:Vm = new Vm();
			vm.specId = spec.id;
			vm.cpuCore = spec.cpuCore;
			vm.memory = spec.memory;
			vm.templateId = template.id;
			vm.description = template.description;
			vm.os = template.os;
			vm.networkAdapterList = new ArrayCollection();
			vm.diskList = new ArrayCollection();
			if (template.diskTemplateList)
			{
				for each (var diskTemplate:DiskTemplate in template.diskTemplateList)
				{
					vm.diskList.addItem(Disk.newDiskFromDiskTemplate(diskTemplate));
				}
			}
			return vm;
		}

		public function Vm()
		{
			id = 0;
			name = "Not Defined"
			status = VmStatus.UNAVAILABLE;
			networkAdapterList = new ArrayCollection();
			diskList = new ArrayCollection();
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set diskList(value:ArrayCollection):void {
			super.diskList = value;
			for each (var item:Disk in value)
			{
				if(item.vmId == id)
				{
					item.vm = this;
				}
			}
		}

		override public function set networkAdapterList(value:ArrayCollection):void {
			super.networkAdapterList = value;
			for each (var item:NetworkAdapter in value)
			{
				if(item.vmId == id)
				{
					item.vm = this;
				}
			}
		}

		override public function set vmCloudUserMapList(value:ArrayCollection):void {
			super.vmCloudUserMapList = value;
			for each (var item:VmCloudUserMap in value)
			{
				if(item.vmId == id)
				{
					item.vm = this;
				}
			}
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * @return リトライ可能です.
		 */
		public function get canStart():Boolean
		{
			return status.isIncludedIn(VmStatus.DOWN);
		}

		[Transient]
		[Bindable("changeStatus")]
		/**
		 * @return キャンセル可能です.
		 */
		public function get canStop():Boolean
		{
			return !status.isIncludedIn(VmStatus.DOWN,
				VmStatus.DEVELOPPING);
		}

		override public function set status(value:VmStatus):void
		{
			super.status = value;
			dispatchEvent(new Event(CHANGE_STATUS));
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
			if(ref == null)
			{
				ref = new Dictionary();
			}
			else
			{
				var result:Vm = ref[this] as Vm;
				if(result != null)
				{
					return result;
				}
			}
			result = new Vm();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.VmBase");
			ref[this] = result;

			//IDの初期化
			if(!withId)
			{
				result.id = 0;
			}
			if(deep)
			{
				if(networkAdapterList)
					result.networkAdapterList = CloneUtil.cloneCollection(networkAdapterList, withId, deep, ref);
				if(diskList)
					result.diskList = CloneUtil.cloneCollection(diskList, withId, deep, ref);
			}
			return result;
		}

		/**
		 * 削除に先だって関連をクリアします.
		 */
		public function clean():void
		{
			removeAllNetworkAdapters();
		}

		/**
		 * すべてのネットワークアダプタを削除します.
		 * @return 削除したネットワークアダプターのリスト
		 */
		public function removeAllNetworkAdapters():Array
		{
			var result:Array = new Array();
			if(networkAdapterList)
				for each(var networkAdapter:NetworkAdapter in networkAdapterList)
				{
					var resultElement:NetworkAdapter = removeNetworkAdapter(networkAdapter);
					if(resultElement != null) result.push(resultElement);
				}
			return result;
		}

		/**
		 * ネットワークアダプターを削除します.
		 * @param networkAdapter 削除するネットワークアダプタ
		 * @return 削除したネットワークアダプタ
		 */
		public function removeNetworkAdapter(networkAdapter:NetworkAdapter):NetworkAdapter
		{
			if(networkAdapterList == null) return null;
			var indexInVm:int = networkAdapterList.getItemIndex(networkAdapter);
			if(indexInVm == -1) return null;

			if(networkAdapter.securityGroup)
			{
				var indexInSg:int = networkAdapter.securityGroup.networkAdapterList.getItemIndex(networkAdapter);
				if(indexInSg != -1) networkAdapter.securityGroup.networkAdapterList.removeItemAt(indexInSg);
			}

			networkAdapterList.removeItemAt(indexInVm);

			return networkAdapter;
		}

		/**
		 * SecurityGroupと接続を持つNetworkAdapterを取得します.
		 * @param sg SecurityGroup
		 * @return NetworkAdapter
		 */
		public function getNetworkAdapterWithConnectionTo(sg:SecurityGroup):NetworkAdapter
		{
			for each(var nwa:NetworkAdapter in networkAdapterList)
			{
				if(nwa.securityGroupId == sg.id) return nwa;
			}
			return null;
		}

		/**
		 * NetworkApapterを追加します.
		 * @param nwa 追加されたNetworAdapter
		 * @return 追加された場所
		 */
		public function addNetworkAdapter(nwa:NetworkAdapter):int
		{
			if(nwa == null) return -1;
			if(networkAdapterList == null) return -1;
			var index:int = networkAdapterList.getItemIndex(nwa);
			if(index >= 0) return index;
			networkAdapterList.addItem(nwa);
			return networkAdapterList.length - 1;
		}

		/**
		 * Diskを追加する.
		 * @param disk 追加するディスク
		 * @return 追加されたディスク
		 */
		public function addDisk(disk:Disk):Disk
		{
			if(disk == null) return null;
			if(diskList == null) diskList = new ArrayCollection();
			if(diskList.getItemIndex(disk) >= 0) throw new Error("Disk is already exist.")
			disk.vm = this;
			diskList.addItem(disk);
			return disk;
		}

		/**
		 * Diskを削除する.
		 * @param disk 削除するディスク
		 * @return 削除されたディスク
		 */
		public function removeDisk(disk:Disk):Disk
		{
			if(disk == null) return null;
			if(diskList == null) return null;
			var index:int = diskList.getItemIndex(disk)
			if(index < 0) throw new Error("Disk is not exist.")
			diskList.removeItemAt(index);
			return disk;
		}
    }
}