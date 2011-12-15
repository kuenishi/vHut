/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log
{
	import jp.co.ntts.vhut.log.infrastructure.LogViewerEventTest;
	import jp.co.ntts.vhut.log.presentation.LogViewerGroupPMTest;
	import jp.co.ntts.vhut.log.presentation.TraceTargetConfGroupPMTest;
	
	[Suite]
	[RunWith("org.flexunit.runners.Suite")]
	/**
	 * 統合テストクラス.
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
	public class UnitTest
	{
		public var test1:jp.co.ntts.vhut.log.presentation.LogViewerGroupPMTest;
		public var test2:jp.co.ntts.vhut.log.presentation.TraceTargetConfGroupPMTest;
		public var test3:jp.co.ntts.vhut.log.infrastructure.LogViewerEventTest;
		public var test4:jp.co.ntts.vhut.log.VhutLogLoggerTest;
		public var test5:jp.co.ntts.vhut.log.LogCacheTargetTest;
	}
}