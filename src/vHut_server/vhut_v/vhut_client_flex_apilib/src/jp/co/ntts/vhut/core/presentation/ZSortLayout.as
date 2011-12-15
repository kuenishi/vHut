/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.presentation
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.core.IVisualElement;

	import spark.components.SkinnableContainer;
	import spark.components.supportClasses.Skin;
	import spark.layouts.BasicLayout;
	import spark.layouts.supportClasses.LayoutBase;
	import spark.skins.spark.SkinnableContainerSkin;

	/**
	 * 親コンポーネントのY軸を確認してエレメントのdepthを再構築します
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
	public class ZSortLayout extends BasicLayout
	{
		public function ZSortLayout()
		{
			super();
		}

//		protected var elementList:ArrayCollection = new ArrayCollection();
//
//		protected function updateElementList():void
//		{
//			elementList = new ArrayCollection();
//
//			for(var i:int=0; i<target.numElements; i++)
//			{
//				var element:IVisualElement = target.getElementAt(i);
//
//				elementList.addItem(element);
//			}
//
//			var sort:Sort = new Sort();
//			sort.fields = ["z"];
//			elementList.sort = sort;
//
//			elementList.refresh();
//		}

		override public function updateDisplayList(width:Number, height:Number):void
		{
			super.updateDisplayList(width, height);

			var i:int;
			var element:IVisualElement;
			var cos:Number = 0;

			cos = Math.cos(target.rotationY / 180 * Math.PI);

			var list:Array = new Array();

			for(i=0; i<target.numElements; i++)
			{
				element = target.getElementAt(i);
				list.push(element);
			}

			list.sortOn("z");

			if (cos < 0)
			{
				list = list.reverse();
			}

			for(i=0; i<list.length; i++)
			{
				element = list[i] as IVisualElement;
				element.depth = i;
			}
		}
	}
}