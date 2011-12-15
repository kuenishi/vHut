/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.dragimage
{
	import flash.events.MouseEvent;
	import flash.geom.Point;

	import jp.co.ntts.vhut.comp.va.domain.VaDragDataType;
	import jp.co.ntts.vhut.core.presentation.IconImage;

	import mx.core.DragSource;
	import mx.core.IFlexDisplayObject;
	import mx.managers.DragManager;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.SgItemRenderer;

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
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class SwitchDragImage extends DragImage
	{
		public function SwitchDragImage()
		{
			super();
			setStyle("skinClass", SwitchDragImageSkin);
		}

		override protected function get dragSource():DragSource
		{
			var result:DragSource = new DragSource();
			result.addData( data, VaDragDataType.SWITCH_TEMPLATE.toString() );
			result.addData( new Point(
				(dragImage.width - width)/2 + mouseX
				, (dragImage.height - height)/2 + mouseY
			)
				, VaDragDataType.OFFSET.toString() );
			return result;
		}

		override protected function get dragImage():IFlexDisplayObject
		{
			var result:SgItemRenderer = new SgItemRenderer();
			result.data = this.data;
			return result;
		}

		override protected function updateImage():void
		{
		}

		override protected function onMouseDownHandler(event:MouseEvent):void
		{
			DragManager.showFeedback(DragManager.COPY);
			DragManager.doDrag( this
				, dragSource
				, event
				, dragImage
				, (dragImage.width - width)/2
				, (dragImage.height - height)/2
				, 0.5 );
		}

	}
}