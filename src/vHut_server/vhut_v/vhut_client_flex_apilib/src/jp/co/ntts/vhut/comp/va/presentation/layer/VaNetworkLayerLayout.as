/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{


	/**
	 * ネットワークの接続要素を配置します.
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
	public class VaNetworkLayerLayout extends VaListBaseLayerLayout
	{
		public function VaNetworkLayerLayout()
		{
			super();
		}

		override public function updateDisplayList(width:Number, height:Number):void
		{
			super.updateDisplayList(width, height);

			if (list)
			{
				swapToFront(list.selectedIndex);
			}
		}
	}
}