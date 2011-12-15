/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import mx.formatters.NumberBase;
	import mx.formatters.NumberBaseRoundType;

	/**
	 * 数値関連の便利クラスです。
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
	public class NumberUtil
	{
		private  static var _numberBase:NumberBase = new NumberBase();
		
		public static function formatRoundingWithPrecision(value:Number, precision:int):String
		{
			return _numberBase.formatRoundingWithPrecision(value.toString(), NumberBaseRoundType.NEAREST, precision);
		}
	}
}