/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.util.CloneUtil;
	import jp.co.ntts.vhut.util.IClonable;

	import mx.utils.ObjectUtil;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.NetworkAdapter")]
	/**
	 * NetworkAdapter Entity Class.
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
    public class NetworkAdapter extends NetworkAdapterBase implements IClonable
	{
		/**
		 * ネットワークアダプタを作成します.
		 * @param vm 接続するVm
		 * @param securityGroup 接続するSecurityGroup
		 * @return ネットワークアダプタ
		 * @throws Error vmがnullの場合
		 * @throws Error securityGroupがnullの場合
		 */
		public static function newNetworkAdapter(vm:Vm, securityGroup:SecurityGroup):NetworkAdapter
		{
			if(vm == null) throw new Error("Network can not be created, because vm is null");
			if(securityGroup == null) throw new Error("Network can not be created, because vm is securityGroup");
			var vmNwa:NetworkAdapter = vm.getNetworkAdapterWithConnectionTo(securityGroup);
			if(vmNwa != null) return vmNwa;

			var nwa:NetworkAdapter = new NetworkAdapter();
			if(vm.addNetworkAdapter(nwa) >= 0 && securityGroup.addNetworkAdapter(nwa) >= 0)
			{
				nwa.vm = vm;
				nwa.securityGroup = securityGroup;
				return nwa;
			}
			return null;
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		public function clean():void
		{
			if(vm != null)
			{
				vm.removeNetworkAdapter(this);
			}
			else if(securityGroup != null)
			{
				securityGroup.removeNetworkAdapter(this);
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
				var result:NetworkAdapter = ref[this] as NetworkAdapter;
				if(result != null)
				{
					return result;
				}
			}
			result = new NetworkAdapter();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.NetworkAdapterBase");
			ref[this] = result;

			//IDの初期化
			if(!withId)
			{
				result.id = 0;
			}
			if(deep)
			{
				if(vm)
					result.vm = vm.clone(withId, deep, ref);
				if(securityGroup)
					result.securityGroup = securityGroup.clone(withId, deep, ref);
			}
			return result;
		}
    }
}