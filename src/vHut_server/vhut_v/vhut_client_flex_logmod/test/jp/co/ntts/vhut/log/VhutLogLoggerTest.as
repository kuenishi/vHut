/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.log
{
	import flash.events.Event;
	
	import flexunit.framework.Assert;
	
	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.logging.Log;
	import mx.logging.LogEvent;
	import mx.logging.LogEventLevel;
	
	import org.flexunit.async.Async;
	
	[ResourceBundle("LOGMessages")]

	/**
	 * xxxテストクラス.
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
	public class VhutLogLoggerTest
	{
		
		private static var logger:VhutLogLogger=VhutLog.getLogger("jp.co.ntts.vhut.log.VhutLogLoggerTest");
		private static var dummyTarget:LineFormattedTargetStub

		[Before]
		public function setUp():void
		{
			dummyTarget=new LineFormattedTargetStub();
			Log.addTarget(dummyTarget);
		}

		[After]
		public function tearDown():void
		{
			Log.removeTarget(dummyTarget);
		}

		[BeforeClass]
		public static function setUpBeforeClass():void
		{
		}

		[AfterClass]
		public static function tearDownAfterClass():void
		{
		}

		[Test(async)]
		public function testLog():void
		{
			dummyTarget.target = this;
			dummyTarget.callback = Async.asyncHandler(this, checkLog, 1000);
			logger.log("DLOG0001", "val0", "val1");

		}

		private function checkLog(event:LogEvent, passThroughData:Object):void
		{
			Assert.assertEquals(LogEventLevel.DEBUG, event.level);
			Assert.assertEquals("DEBUGメッセージ value0=val0 value1=val1", event.message);
		}
	}
}