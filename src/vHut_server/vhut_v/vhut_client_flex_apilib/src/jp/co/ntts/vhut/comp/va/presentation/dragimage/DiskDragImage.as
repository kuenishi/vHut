/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.dragimage
{
	import flash.events.MouseEvent;
	
	import jp.co.ntts.vhut.comp.va.domain.VaDragDataType;
	import jp.co.ntts.vhut.core.presentation.IconImage;
	
	import mx.core.DragSource;
	import mx.core.IFlexDisplayObject;
	
	import spark.components.Label;
	
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
	public class DiskDragImage extends DragImage
	{
		public function DiskDragImage()
		{
			super();
			setStyle("skinClass", DiskDragImageSkin);
		}
		
		[SkinPart(required="false")]
		public var label:Label;
		
		override protected function get dragSource():DragSource
		{
			var result:DragSource = new DragSource();
			result.addData( data, VaDragDataType.DISK_TEMPLATE.toString() );
			return result;
		}
		
		override protected function get dragImage():IFlexDisplayObject
		{
			var result:DiskDragImage = new DiskDragImage();
			result.width = this.width;
			result.height = this.height;
			result.data = this.data;
			
			return result;
		}
		
		override protected function updateImage():void
		{
		}
		
		protected function updateLabel():void
		{
			if(data && label)
				label.text = data.size;
		}
		
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
			switch(partName)
			{
				case "label":
					updateLabel();
					break;
			}
		}
		
		override protected function commitProperties():void
		{
			if(_isDataUpdated)
			{
				if(label)
					updateLabel();
			}
			super.commitProperties();
		}
	}
}