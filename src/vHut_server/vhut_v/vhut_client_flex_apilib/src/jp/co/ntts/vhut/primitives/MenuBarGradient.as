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
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class MenuBarGradient extends LinearGradient
	{
		public static const COLOR0:int = 0xFEF5E2;
		public static const COLOR1:int = 0xFFD37B;
		public static const COLOR2:int = 0xCAA761;
		
		public function MenuBarGradient()
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