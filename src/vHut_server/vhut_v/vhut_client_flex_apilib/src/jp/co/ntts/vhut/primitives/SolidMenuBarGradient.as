/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.primitives
{
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;

	/**
	 * メニュー
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
	 * $Date: 2010-11-04 12:04:15 +0900 (木, 04 11 2010) $
	 * $Revision: 562 $
	 * $Author: NTT Software Corporation. $
	 */
	public class SolidMenuBarGradient extends LinearGradient
	{
		public static const COLOR0:int = 0xEFEFEF;
		public static const COLOR1:int = 0xDEDEDE;
		public static const COLOR2:int = 0x909090;

		public function SolidMenuBarGradient()
		{
			super();
			updateEntries();
		}

		[Inspectable(category="General", dafaultValue=0.1, format="Number")]
		public function get flatRate():Number
		{
			return _flatRate;
		}
		public function set flatRate(value:Number):void
		{
			if(value < 0 || value>1) return;
			_flatRate = value;
			updateEntries();
		}

		private function updateEntries():void
		{
			var array:Array = new Array();
			array.push(new GradientEntry(COLOR0, 0.0));
			array.push(new GradientEntry(COLOR1, _flatRate));
			array.push(new GradientEntry(COLOR1, 1-_flatRate));
			array.push(new GradientEntry(COLOR2, 1.0));
			entries = array;
		}

		private var _flatRate:Number = 0.1;
	}
}