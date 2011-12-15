/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log
{
	import flexunit.framework.Assert;
	
	import mx.logging.LogEvent;
	import mx.logging.LogEventLevel;
	
	import org.hamcrest.mxml.object.SameInstance;
	
	/**
	 * LogCacheTargetテストクラス.
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
	public class LogCacheTargetTest
	{	
		private static var dummyLogEvent:LogEvent;
		
		private var target:LogCacheTarget; 
		
		[Before]
		public function setUp():void
		{
			target = LogCacheTarget.getInstance();
		}
		
		[After]
		public function tearDown():void
		{
			target.clear();
		}
		
		[BeforeClass]
		public static function setUpBeforeClass():void
		{
			dummyLogEvent = new LogEvent(LogEvent.LOG);
			dummyLogEvent.level = LogEventLevel.DEBUG;
			dummyLogEvent.message = "test";
		}
		
		[AfterClass]
		public static function tearDownAfterClass():void
		{
		}
		
		[Test(description="Nullでないか、型があっているか")]
		public function testGetInstance():void
		{
			Assert.assertNotNull(target.limit);
			Assert.assertTrue(target is LogCacheTarget);
		}
		
		[Test(description="キャッシュしたログデータを返却できるか")]
		public function testGetLogAsLogData():void
		{
			target.logEvent(dummyLogEvent);
			var result:Vector.<LogData> = target.getLogAsLogData();
			Assert.assertEquals(1, result.length);
			Assert.assertEquals("test", result[0].message);
		}
		
		[Test(description="キャッシュしたログデータをテキストして返却できるか")]
		public function testGetLogAsString():void
		{
			target.logEvent(dummyLogEvent);
			target.logEvent(dummyLogEvent);
			var result:String = target.getLogAsString();
			Assert.assertEquals("test\ntest", result);
			result = target.getLogAsString("|");
			Assert.assertEquals("test|test", result);
		}
		
		[Test(description="上限値を返却できるか")]
		public function testGet_limit():void
		{
			target.limit = 100;
			Assert.assertEquals(100, target.limit);
		}
		
		[Test(description="上限値を設定できるか、限界値を守っているか")]
		public function testSet_limit():void
		{
			target.limit = -1;
			Assert.assertEquals(LogCacheTarget.LIMIT_MIN, target.limit);
			target.limit = LogCacheTarget.LIMIT_MAX + 1;
			Assert.assertEquals(LogCacheTarget.LIMIT_MAX, target.limit);
			target.limit = 1;
			target.logEvent(dummyLogEvent);
			target.logEvent(dummyLogEvent);
			var result:Vector.<LogData> = target.getLogAsLogData();
			Assert.assertEquals(1, result.length);
		}
	}
}