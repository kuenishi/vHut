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
	public class VaSide extends Enum
	{
		public static const NONE:VaSide = new VaSide("NONE", _);
		public static const NORTH:VaSide = new VaSide("NORTH", _);
		public static const EAST:VaSide = new VaSide("EAST", _);
		public static const SOUTH:VaSide = new VaSide("SOUTH", _);
		public static const WEST:VaSide = new VaSide("WEST", _);
		
		function VaSide(value:String = null, restrictor:* = null) {
			super((value || NONE.name), restrictor);
		}
		
		override protected function getConstants():Array {
			return constants;
		}
		
		public static function get constants():Array {
			return [NONE, NORTH, EAST, SOUTH, WEST];
		}
		
		public static function valueOf(name:String):VaSide {
			return VaSide(NONE.constantOf(name));
		}
	}
}