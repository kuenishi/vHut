/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.top.indicator
{
	import spark.components.supportClasses.SkinnableComponent;

	[Style(name="startColor", type="uint", format="Color", inherit="no")]
	[Style(name="endColor", type="uint", format="Color", inherit="no")]

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
	public class Indicator extends SkinnableComponent
	{
		public function Indicator()
		{
			super();
			setStyle("skinClass", IndicatorSkin);
		}

		[Bindable]
		/** 表示する値 */
		public var value:Number = 0;

	}
}