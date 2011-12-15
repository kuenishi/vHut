/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.util.CloneUtil;
	import jp.co.ntts.vhut.util.IClonable;

	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.SecurityGroup")]
	/**
	 * SecurityGroup Entity Class.
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
    public class SecurityGroup extends SecurityGroupBase implements IClonable
	{
		public function SecurityGroup()
		{
			name = "New SecurityGroup";
			networkAdapterList = new ArrayCollection();
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set networkAdapterList(value:ArrayCollection):void {
			super.networkAdapterList = value;
			for each (var item:NetworkAdapter in value)
			{
				if(item.securityGroupId == id)
				{
					item.securityGroup = this;
				}
			}
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
				var result:SecurityGroup = ref[this] as SecurityGroup;
				if(result != null)
				{
					return result;
				}
			}
			result = new SecurityGroup();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.SecurityGroupBase");
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
			var indexInSg:int = networkAdapterList.getItemIndex(networkAdapter);
			if(indexInSg == -1) return null;

			if(networkAdapter.vm)
			{
				var indexInVm:int = networkAdapter.vm.networkAdapterList.getItemIndex(networkAdapter);
				if(indexInVm != -1) networkAdapter.vm.networkAdapterList.removeItemAt(indexInVm);
			}

			networkAdapterList.removeItemAt(indexInSg);

			return networkAdapter;
		}

		/**
		 * Vmと接続を持つNetworkAdapterを取得します.
		 * @param vm Vm
		 * @return NetworkAdapter
		 */
		public function getNetworkAdapterWithConnectionTo(vm:Vm):NetworkAdapter
		{
			for each(var nwa:NetworkAdapter in networkAdapterList)
			{
				if(nwa.vm == vm) return nwa;
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
			var index:int = networkAdapterList.getItemIndex(nwa);
			if(index >= 0) return index;
			networkAdapterList.addItem(nwa);
			return networkAdapterList.length - 1;
		}
    }
}