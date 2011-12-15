/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.dragimage
{
	import flash.events.MouseEvent;
	
	import jp.co.ntts.vhut.core.presentation.IconImage;
	
	import mx.core.DragSource;
	import mx.core.IFlexDisplayObject;
	import mx.managers.DragManager;
	
	import spark.components.SkinnableContainer;
	
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
	public class DragImage extends SkinnableContainer
	{
		public function DragImage()
		{
			super();
			addEventListener(MouseEvent.MOUSE_DOWN, onMouseDownHandler);
		}
		
		[SkinPart(required="false")]
		public var image:IconImage;
		
		/** data */
		public function set data(value:Object):void
		{
			if(!value || value == _data) return;
			_data = value;
			_isDataUpdated = true;
			invalidateProperties();
		}
		public function get data():Object
		{
			return _data;
		}
		protected var _data:Object;
		protected var _isDataUpdated:Boolean = false;
		
		protected function get dragSource():DragSource
		{
			return new DragSource();
		}
		
	 	protected function get dragImage():IFlexDisplayObject
		{
			return null;
		}
		
		protected function updateImage():void
		{
			if(data) 
				image.url = data.imageUrl;
		}
		
		protected function onMouseDownHandler(event:MouseEvent):void
		{	
			DragManager.doDrag( this, dragSource, event, dragImage, 0, 0, 0.5 );
		}
		
		override protected function partAdded(partName:String, instance:Object):void
		{
			switch(partName)
			{
				case "image":
					updateImage();
					break;
			}
		}
		
		override protected function commitProperties():void
		{
			super.commitProperties();
			if(_isDataUpdated)
			{
				updateImage();
				_isDataUpdated = false;
			}
		}
		
	}
}