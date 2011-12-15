/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import flash.events.MouseEvent;

	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.LinkItemRenderer;

	import mx.core.ClassFactory;
	import mx.core.IVisualElement;
	import mx.core.mx_internal;

	import spark.components.IItemRenderer;
	import spark.components.supportClasses.ListBase;
	import spark.events.RendererExistenceEvent;

	use namespace mx_internal;  //ListBase and List share selection properties that are mx_internal

	/**
	 * リストをベースにした基本レイヤー
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
	 * $Date: 2010-12-15 19:32:16 +0900 (水, 15 12 2010) $
	 * $Revision: 639 $
	 * $Author: NTT Software Corporation. $
	 */
	public class VaListBaseLayer extends ListBase
	{

		public function VaListBaseLayer()
		{
			super();
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  ListBase, 選択関連
		//
		////////////////////////////////////////////////////////////////////////////

		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);

			if (instance == dataGroup)
			{
				dataGroup.addEventListener(
					RendererExistenceEvent.RENDERER_ADD, dataGroup_rendererAddHandler);
				dataGroup.addEventListener(
					RendererExistenceEvent.RENDERER_REMOVE, dataGroup_rendererRemoveHandler);
			}
		}

		/**
		 *  @private
		 */
		override protected function partRemoved(partName:String, instance:Object):void
		{
			if (instance == dataGroup)
			{
				dataGroup.removeEventListener(
					RendererExistenceEvent.RENDERER_ADD, dataGroup_rendererAddHandler);
				dataGroup.removeEventListener(
					RendererExistenceEvent.RENDERER_REMOVE, dataGroup_rendererRemoveHandler);
			}

			super.partRemoved(partName, instance);
		}

		/**
		 *  @private
		 *  Called when an item has been added to this component.
		 */
		protected function dataGroup_rendererAddHandler(event:RendererExistenceEvent):IVisualElement
		{
			var index:int = event.index;
			var renderer:IVisualElement = event.renderer;

			if (!renderer)
				return null;

			renderer.addEventListener(MouseEvent.MOUSE_DOWN, item_mouseDownHandler);
			renderer.addEventListener(MouseEvent.MOUSE_UP, item_mouseUpHandler);
			return renderer;
		}

		/**
		 *  @private
		 *  Called when an item has been removed from this component.
		 */
		protected function dataGroup_rendererRemoveHandler(event:RendererExistenceEvent):IVisualElement
		{
			var index:int = event.index;
			var renderer:IVisualElement = event.renderer;

			if (!renderer)
				return null;

			renderer.removeEventListener(MouseEvent.MOUSE_DOWN, item_mouseDownHandler);
			renderer.removeEventListener(MouseEvent.MOUSE_UP, item_mouseUpHandler);
			return renderer;
		}

		protected function item_mouseDownHandler(event:MouseEvent):void
		{
			//Nothing
		}

		protected function item_mouseUpHandler(event:MouseEvent):void
		{
			var newIndex:int
			if (event.currentTarget is IItemRenderer)
				newIndex = IItemRenderer(event.currentTarget).itemIndex;
			else
				newIndex = dataGroup.getElementIndex(event.currentTarget as IVisualElement);

			setSelectedIndex(newIndex, true);
			dataGroup.invalidateDisplayList();
		}

		/** 要素選択を解除します */
		public function unSelect():void
		{
			setSelectedIndex(-1, true);
		}

		/**
		 *  @private
		 */
		override protected function itemSelected(index:int, selected:Boolean):void
		{
			super.itemSelected(index, selected);

			var renderer:Object = dataGroup ? dataGroup.getElementAt(index) : null;

			if (renderer is IItemRenderer)
			{
				IItemRenderer(renderer).selected = selected;
			}
		}

	}
}