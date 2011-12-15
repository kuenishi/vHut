/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log
{
	import mx.logging.LogEvent;
	import mx.logging.targets.LineFormattedTarget;
	
	/**
	 * xxxクラス.
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
	public class LineFormattedTargetStub extends LineFormattedTarget
	{
		public function LineFormattedTargetStub()
		{
			super();
		}
		
		public var target:Object;
		public var callback:Function;
		
		override public function logEvent(event:LogEvent):void
		{
			super.logEvent(event);
			callback.call(target, event);
		}
	}
}