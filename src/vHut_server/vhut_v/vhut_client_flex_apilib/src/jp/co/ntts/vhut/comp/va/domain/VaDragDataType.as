/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.domain
{
	import jp.co.ntts.vhut.util.Enum;

	/**
	 * 接続方向を示すEnum
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
	public class VaDragDataType extends Enum
	{
		public static const BASE_TEMPLATE:VaDragDataType = new VaDragDataType("BaseTemplate", _);
		public static const SWITCH_TEMPLATE:VaDragDataType = new VaDragDataType("SwitchTemplate", _);
		public static const DISK_TEMPLATE:VaDragDataType = new VaDragDataType("DiskTemplate", _);
		public static const OFFSET:VaDragDataType = new VaDragDataType("Offset", _);
		public static const LINK:VaDragDataType = new VaDragDataType("Link", _);
		public static const VM:VaDragDataType = new VaDragDataType("Vm", _);
		public static const SG:VaDragDataType = new VaDragDataType("Sg", _);

		function VaDragDataType(value:String = null, restrictor:* = null) {
			super((value || BASE_TEMPLATE.name), restrictor);
		}

		override protected function getConstants():Array {
			return constants;
		}

		public static function get constants():Array {
			return [BASE_TEMPLATE, SWITCH_TEMPLATE, DISK_TEMPLATE, OFFSET, LINK, VM, SG];
		}

		public static function valueOf(name:String):VaDragDataType {
			return VaDragDataType(BASE_TEMPLATE.constantOf(name));
		}
	}
}