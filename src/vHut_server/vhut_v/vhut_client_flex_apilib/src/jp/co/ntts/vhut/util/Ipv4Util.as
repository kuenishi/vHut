/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	/**
	 * IPv4の変換クラス
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
	public class Ipv4Util
	{

		public static function hex2dot(value:String):String
		{
			if (value.length != 8)
				throw new Error("IPv4 in HEX should have 8 letters.");

			var dots:Array = new Array();
			for (var i:uint=0; i<8; i+=2) {
				dots.push(parseInt(value.substr(i, 2), 16).toString());
			}
			return dots.join('.');
		}

		public static function dot2hex(value:String):String
		{
			var dots:Array = value.split('.');
			if (dots.length != 4)
				throw new Error("IPv4 in DOT should have 3 dots.");
			var hex:String;
			for each (var dot:String in dots) {
				hex += parseInt(dot, 10).toString(16);
			}
			return hex;
		}
	}
}