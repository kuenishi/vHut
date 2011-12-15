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

	import spark.components.DataGroup;
	import spark.components.supportClasses.GroupBase;
	import spark.components.supportClasses.ListBase;
	import spark.components.supportClasses.Skin;
	import spark.layouts.supportClasses.LayoutBase;
	import spark.skins.spark.SkinnableDataContainerSkin;

	/**
	 * リストを拡張したレイヤーの配置ロジックです.
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
	public class VaListBaseLayerLayout extends LayoutBase
	{
		public function VaListBaseLayerLayout()
		{
			super();
		}

		override public function set target(value:GroupBase):void
		{
			super.target = value;

			if (target.parent && target.parent is SkinnableDataContainerSkin)
			{
				var skin:SkinnableDataContainerSkin = target.parent as SkinnableDataContainerSkin;
				if(skin.hostComponent is ListBase)
				{
					_list = skin.hostComponent as ListBase;
				}
			}
		}

		public function get list():ListBase
		{
			return _list;
		}

		private var _list:ListBase;

		protected function swapToFront(index:int):void
		{

			var container:DataGroup = target as DataGroup;

			for(var i:int=0; i<target.numElements; i++)
			{
				var element:IVisualElement = target.getElementAt(i);
				if(i == index)
				{
					element.depth = target.numElements;
				}
				else
				{
					element.depth = i;
				}
			}
		}

	}
}