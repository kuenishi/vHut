/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	/**
	 * MACアドレスの変換クラス
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class MacUtil
	{
		public static function hex2colon(value:String):String
		{
			if (value.length != 12)
				throw new Error("MAC address in HEX should have 12 letters.");

			var dots:Array = new Array();
			for (var i:uint=0; i<12; i+=2) {
				dots.push(value.substr(i, 2));
			}
			return dots.join(':');
		}
		public static function colon2hex(value:String):String
		{
			var dots:Array = value.split(':');
			if (dots.length != 6)
				throw new Error("MAC address in COLON should have 5 colon.");
			return dots.join('');
		}
	}
}