/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log.infrastructure
{
	import flexunit.framework.Assert;
	
	import jp.co.ntts.vhut.log.LogData;
	
	import mx.logging.LogEventLevel;
	
	/**
	 * LogViewerEventテストクラス.
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
	public class LogViewerEventTest
	{		
		[Before]
		public function setUp():void
		{
		}
		
		[After]
		public function tearDown():void
		{
		}
		
		[BeforeClass]
		public static function setUpBeforeClass():void
		{
		}
		
		[AfterClass]
		public static function tearDownAfterClass():void
		{
		}
		
		[Test]
		public function testClone():void
		{
			var logData:LogData = new LogData(LogEventLevel.DEBUG, "test");
			var event:LogViewerEvent = LogViewerEvent.newAddedLogViewerEvent(logData);
			var clonedEvent:LogViewerEvent = event.clone() as LogViewerEvent;
			Assert.assertEquals(logData, clonedEvent.newLogData);
		}
		
		[Test]
		public function testNewAddedLogViewerEvent():void
		{
			var logData:LogData = new LogData(LogEventLevel.DEBUG, "test");
			var event:LogViewerEvent = LogViewerEvent.newAddedLogViewerEvent(logData);
			Assert.assertEquals(logData, event.newLogData);
		}
		
		[Test]
		public function testNewAddingLogViewerEvent():void
		{
			var logData:LogData = new LogData(LogEventLevel.DEBUG, "test");
			var event:LogViewerEvent = LogViewerEvent.newAddingLogViewerEvent(logData);
			Assert.assertEquals(logData, event.newLogData);
		}
	}
}