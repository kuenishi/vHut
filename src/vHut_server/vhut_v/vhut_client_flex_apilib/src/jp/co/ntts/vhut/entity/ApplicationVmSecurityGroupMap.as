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
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMap")]
	/**
	 * ApplicationVmSecurityGroupMap Entity Class.
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
    public class ApplicationVmSecurityGroupMap extends ApplicationVmSecurityGroupMapBase implements IClonable
	{

		public static function newApplicationVmSecurityGroupMap(avm:ApplicationVm, asg:ApplicationSecurityGroup):ApplicationVmSecurityGroupMap
		{
			if(avm == null) throw new Error("ApplicationVmSecurityGroupMap can not be created, because avm is null");
			if(asg == null) throw new Error("ApplicationVmSecurityGroupMap can not be created, because asg is null");
			var vmMap:ApplicationVmSecurityGroupMap = avm.getApplicationVmSecurityGroupMapWithConnectionTo(asg);
			if(vmMap != null) return vmMap;

			var map:ApplicationVmSecurityGroupMap = new ApplicationVmSecurityGroupMap();
			if(avm.addApplicationVmSecurityGroupMap(map) >= 0 && asg.addApplicationVmSecurityGroupMap(map) >= 0)
			{
				map.applicationVm = avm;
				map.applicationSecurityGroup = asg;
				return map;
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
				var result:ApplicationVmSecurityGroupMap = ref[this] as ApplicationVmSecurityGroupMap;
				if(result != null)
				{
					return result;
				}
			}
			result = new ApplicationVmSecurityGroupMap();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.ApplicationVmSecurityGroupMapBase");
			ref[this] = result;

			//IDの初期化
			if(!withId)
			{
				result.id = 0;
			}
			if(deep)
			{
				if(applicationVm)
					result.applicationVm = applicationVm.clone(withId, deep, ref);
				if(applicationSecurityGroup)
					result.applicationSecurityGroup = applicationSecurityGroup.clone(withId, deep, ref);
			}

			return result;
		}
    }
}