/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.dto.AdditionalDiskDto;
	import jp.co.ntts.vhut.util.CloneUtil;
	import jp.co.ntts.vhut.util.IClonable;

	import mx.utils.ObjectUtil;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Disk")]
	/**
	 * Disk Entity Class.
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
    public class Disk extends DiskBase implements IClonable
	{

		/**
		 * テンプレートからディスクを作成する.
		 * @param template
		 * @return
		 */
		public static function newDisk(template:AdditionalDiskDto):Disk
		{
			var disk:Disk = new Disk();
			disk.size = template.size;
			return disk;
		}

		/**
		 * テンプレートからディスクを作成する.
		 * @param template
		 * @return
		 */
		public static function newDiskFromDiskTemplate(disktemplate:DiskTemplate):Disk
		{
			var disk:Disk = new Disk();
			disk.size = disktemplate.size;
			disk.diskTemplateId = disktemplate.id;
			disk.diskTemplate = disktemplate;
			disk.storageId = disktemplate.storageId;
			disk.storage = disktemplate.storage;
			return disk;
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
				vm.removeDisk(this);
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
				var result:Disk = ref[this] as Disk;
				if(result != null)
				{
					return result;
				}
			}
			result = new Disk();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.DiskBase");
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
			}
			return result;
		}
    }
}