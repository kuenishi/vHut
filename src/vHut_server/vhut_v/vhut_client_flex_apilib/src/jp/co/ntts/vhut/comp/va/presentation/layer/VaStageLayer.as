/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import flash.display.Graphics;

	import mx.core.UIComponent;

	/**
	 * 最下層のレイヤー.
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
	public class VaStageLayer extends UIComponent
	{
		public function VaStageLayer()
		{
			super();
		}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			var g:flash.display.Graphics = graphics;
			g.clear();
			g.beginFill(0xFF0000, 0);
			g.drawRect(0, 0, unscaledWidth, unscaledHeight);
			g.endFill();
		}
	}
}