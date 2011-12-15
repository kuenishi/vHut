/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.events.Event;

	import mx.collections.ArrayCollection;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Template")]
	/**
	 * Template Entity Class.
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
    public class Template extends TemplateBase {
		public static const CHANGE_DISK_TEMPLATE:String = "changeDiskTemplate";

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set diskTemplateList(value:ArrayCollection):void
		{
			super.diskTemplateList = value;
			for each (var item:DiskTemplate in value)
			{
				if(item.templateId == id)
				{
					item.template = this;
				}
			}
			dispatchEvent(new Event(CHANGE_DISK_TEMPLATE));
		}

		override public function set networkAdapterTemplateList(value:ArrayCollection):void
		{
			super.networkAdapterTemplateList = value;
			for each (var item:DiskTemplate in value)
			{
				if(item.templateId == id)
				{
					item.template = this;
				}
			}
			dispatchEvent(new Event(CHANGE_DISK_TEMPLATE));
		}

		/////////////////////////////////////////////////////////
		//
		//  attributes to view
		//
		/////////////////////////////////////////////////////////

		[Transient]
		[Bindable("changeDiskTemplate")]
		public function get size():Number
		{
			var _size:Number = 0;
			for each (var disk:DiskTemplate in diskTemplateList)
			{
				_size += disk.size;
			}
			return _size;
		}
    }
}