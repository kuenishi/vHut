/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.presentation
{
	import jp.co.ntts.vhut.entity.Right;

	import spark.components.NavigatorContent;

	/**
	 *
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class VhutMainNavigatorContent extends NavigatorContent
	{
		public function VhutMainNavigatorContent()
		{
			super();
		}

		public var requiredRight:Right;
	}
}