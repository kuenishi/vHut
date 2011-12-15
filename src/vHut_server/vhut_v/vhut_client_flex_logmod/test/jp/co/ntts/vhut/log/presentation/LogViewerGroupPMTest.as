/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log.presentation
{
	import flashx.textLayout.elements.TextFlow;
	import flashx.textLayout.factory.TextFlowTextLineFactory;
	
	import flexunit.framework.Assert;
	
	import mx.logging.LogEventLevel;
	
	import spark.components.TextArea;
	import spark.utils.TextFlowUtil;
	
	/**
	 * LogViewerGroupPMのテストクラス.
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
	public class LogViewerGroupPMTest
	{		
		private static var factory:TextFlowTextLineFactory;
		private static var ns:Namespace;
		
		private var model:LogViewerGroupPM;
		
		
		[Before]
		public function setUp():void
		{
			model = new LogViewerGroupPM();
			default xml namespace = ns;
		}
		
		[After]
		public function tearDown():void
		{
		}
		
		[BeforeClass]
		public static function setUpBeforeClass():void
		{
			factory = new TextFlowTextLineFactory();
			ns = new Namespace("http://ns.adobe.com/textLayout/2008");
		}
		
		[AfterClass]
		public static function tearDownAfterClass():void
		{
		}
		
		[Test(description="すべてのエントリが消える")]
		public function testClear():void
		{
			var output:XML;
			model.log(LogEventLevel.DEBUG, "test");
			output = TextFlowUtil.export(model.textFlow);
			Assert.assertEquals("test", output.p[0].span[0].toString());
			model.clear();
			output = TextFlowUtil.export(model.textFlow);
			Assert.assertEquals(0, output.p.length());
		}
		
		[Test(description="testというエントリがtextFlowに反映される")]
		public function testLog():void
		{
			var output:XML;
			model.log(LogEventLevel.DEBUG, "test");
			output = TextFlowUtil.export(model.textFlow);
			Assert.assertEquals("test", output.p[0].span[0].toString());
		}
	}
}