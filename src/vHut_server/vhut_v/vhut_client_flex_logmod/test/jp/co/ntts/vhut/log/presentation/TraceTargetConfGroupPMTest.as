/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log.presentation
{
	import flash.trace.Trace;
	
	import flexunit.framework.Assert;
	
	import mx.logging.LogEventLevel;
	import mx.logging.targets.TraceTarget;
	
	import org.flexunit.asserts.assertEquals;
	
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
	public class TraceTargetConfGroupPMTest
	{		
		
		private var model:TraceTargetConfGroupPM;
		
		[Before]
		public function setUp():void
		{
			model = new TraceTargetConfGroupPM();
			model.target = new TraceTarget();
			model.init();
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
		
		[Test(description="logLevelを設定すると選択インデックスが変わる")]
		public function testSet_logLevel():void
		{
			model.logLevel = LogEventLevel.DEBUG;
			Assert.assertEquals(2, model.selectedLogLevelIndex);
			model.isTargetEnable = true;
			Assert.assertEquals(0, model.selectedLogLevelIndex);
		}
	}
}