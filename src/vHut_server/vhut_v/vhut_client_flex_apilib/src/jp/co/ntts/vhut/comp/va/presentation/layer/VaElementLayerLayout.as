/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import flash.geom.Point;

	import jp.co.ntts.vhut.comp.va.domain.IVaElement;

	import mx.core.ILayoutElement;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;

	import spark.components.DataGroup;
	import spark.layouts.supportClasses.LayoutBase;

	/**
	 * VM, SecurityGroup要素を配置します.
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
	public class VaElementLayerLayout extends VaListBaseLayerLayout
	{
		public function VaElementLayerLayout()
		{
			super();
		}

		override public function updateDisplayList(width:Number, height:Number):void
		{
			super.updateDisplayList(width, height);

			var container:DataGroup = target as DataGroup;

			for(var i:int=0; i<target.numElements; i++)
			{
				var element:ILayoutElement = target.getElementAt(i);
				var xpos:Number = 0;
				var ypos:Number = 0;

				if(container)
				{
					var item:IVaElement = container.dataProvider.getItemAt(i) as IVaElement;
					if(item && item.pos)
					{
						xpos = item.pos.x;
						ypos = item.pos.y;
					}
				}

				element.setLayoutBoundsSize(NaN, NaN);
				element.setLayoutBoundsPosition(xpos, ypos);
			}

			if (list)
			{
				swapToFront(list.selectedIndex);
			}
		}
	}
}