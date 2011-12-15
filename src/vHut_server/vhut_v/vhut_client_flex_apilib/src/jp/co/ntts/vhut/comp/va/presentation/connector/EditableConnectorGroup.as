/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.connector
{
	import flash.events.Event;
	import flash.events.MouseEvent;

	import jp.co.ntts.vhut.comp.va.application.ConnectorDragEvent;
	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.VaConstant;
	import jp.co.ntts.vhut.comp.va.domain.VaDragDataType;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.comp.va.domain.VaLinkDragData;
	import jp.co.ntts.vhut.comp.va.domain.VaSide;

	import mx.core.ClassFactory;
	import mx.core.DragSource;
	import mx.core.IFlexDisplayObject;
	import mx.core.IVisualElement;
	import mx.core.SpriteAsset;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.managers.DragManager;

	import spark.components.IItemRenderer;
	import spark.components.SkinnableDataContainer;
	import spark.events.RendererExistenceEvent;

	[SkinState("normal")]
	[SkinState("hovered")]
	[SkinState("disabled")]
	[Event(name="beginDrag", type="jp.co.ntts.vhut.comp.va.application.ConnectorDragEvent")]
	[Event(name="changeDrag", type="jp.co.ntts.vhut.comp.va.application.ConnectorDragEvent")]
	[Event(name="endDrag", type="jp.co.ntts.vhut.comp.va.application.ConnectorDragEvent")]
	/**
	 * LANケーブルのコネクタを収容するコンポーネント.
	 * VMかスイッチに格納されます。
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
	public class EditableConnectorGroup extends ConnectorGroup
	{
		public static const CHANGE_DATA:String = "changeData";

		public function EditableConnectorGroup()
		{
			super();
			setStyle("skinClass", EditableConnectorGroupSkin);
			itemRenderer = new ClassFactory(ConnectorItemRenderer);
			layout = new ConnectorLayout();
			updateLayout();
		}

		[SkinPart(required="false")]
		public var cursor:UIComponent;

		protected var targetLink:VaLink = null;

		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);

			if (instance == background)
			{
				background.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
				background.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);

				background.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
				background.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
				background.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
				background.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
			}
			else if (instance == dataGroup)
			{
				dataGroup.addEventListener(
					RendererExistenceEvent.RENDERER_ADD, dataGroup_rendererAddHandler);
				dataGroup.addEventListener(
					RendererExistenceEvent.RENDERER_REMOVE, dataGroup_rendererRemoveHandler);
			}
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
			var newIndex:int
			if (event.currentTarget is IItemRenderer)
				newIndex = IItemRenderer(event.currentTarget).itemIndex;
			else
				newIndex = dataGroup.getElementIndex(event.currentTarget as IVisualElement);
			targetLink = dataProvider.getItemAt(newIndex) as VaLink;
			beginDrag(event);
			event.stopPropagation();
		}

		protected function item_mouseUpHandler(event:MouseEvent):void
		{
			event.stopPropagation();
		}

		/**
		 *  @private
		 */
		override protected function partRemoved(partName:String, instance:Object):void
		{
			if (instance == background)
			{
				background.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
				background.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);

				background.removeEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
				background.removeEventListener(DragEvent.DRAG_OVER, dragOverHandler);
				background.removeEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
				background.removeEventListener(DragEvent.DRAG_DROP, dragDropHandler);
			}
			else if (instance == dataGroup)
			{
				dataGroup.removeEventListener(
					RendererExistenceEvent.RENDERER_ADD, dataGroup_rendererAddHandler);
				dataGroup.removeEventListener(
					RendererExistenceEvent.RENDERER_REMOVE, dataGroup_rendererRemoveHandler);
			}

			super.partRemoved(partName, instance);
		}



		protected function mouseMoveHandler(event:MouseEvent):void
		{
			if(cursor)
			{
//				cursor.x = event.localX - cursor.width/2;
				cursor.x = event.localX;
				cursor.y = 0;
				event.updateAfterEvent();
			}
		}

		protected function mouseDownHandler(event:MouseEvent):void
		{
			targetLink = null;
			beginDrag(event)
			event.stopPropagation();
		}

		protected function beginDrag(event:MouseEvent):void
		{
			var source:DragSource = createDragSource();
			var image:IFlexDisplayObject = createDragImage();
			DragManager.doDrag( this, source, event, image, 0, 0, 0.5 );
			var linkDragData:VaLinkDragData = source.dataForFormat(VaDragDataType.LINK.toString()) as VaLinkDragData;
			if(linkDragData)
			{
				dispatchEvent(ConnectorDragEvent.newBeginConnectorDragEvent(linkDragData));
			}
		}

		protected function dragEnterHandler(event:DragEvent):void
		{
			var linkDragData:VaLinkDragData = extractVaLinkdragData(event);
			if(linkDragData)
			{
				if(!hasSameLinkData(linkDragData.newLink)  || isUpdateInside(linkDragData))
				{
					linkDragData.newLink = updateLinkData(linkDragData.newLink);
					dispatchEvent(ConnectorDragEvent.newChangeConnectorDragEvent(linkDragData));
					DragManager.acceptDragDrop(event.target as UIComponent);
				}
			}
		}

		protected function dragOverHandler(event:DragEvent):void
		{
			var linkDragData:VaLinkDragData = extractVaLinkdragData(event);
			if(linkDragData)
			{
				linkDragData.newLink = updateLinkData(linkDragData.newLink);
				dispatchEvent(ConnectorDragEvent.newChangeConnectorDragEvent(linkDragData));
			}
		}

		protected function dragExitHandler(event:DragEvent):void
		{
			var linkDragData:VaLinkDragData = extractVaLinkdragData(event);
			if(linkDragData)
			{
				linkDragData.newLink = clearLinkData(linkDragData.newLink);
				dispatchEvent(ConnectorDragEvent.newChangeConnectorDragEvent(linkDragData));
			}
		}

		protected function dragDropHandler(event:DragEvent):void
		{
			var linkDragData:VaLinkDragData = extractVaLinkdragData(event);
			if(linkDragData)
			{
				linkDragData.newLink = updateLinkData(linkDragData.newLink);
				dispatchEvent(ConnectorDragEvent.newEndConnectorDragEvent(linkDragData));
			}
		}

		protected function extractVaLinkdragData(event:DragEvent):VaLinkDragData
		{
			if (event.target != event.dragInitiator
				&& event.target != event.dragInitiator.owner
				&& event.dragInitiator is ConnectorGroup
				&& event.dragSource.hasFormat(VaDragDataType.LINK.toString()))
			{
				var linkDragData:VaLinkDragData = event.dragSource.dataForFormat(VaDragDataType.LINK.toString()) as VaLinkDragData;
				if(linkDragData
					&& linkDragData.newLink
					&& linkDragData.startNodeType
					&& linkDragData.startNodeType != type)
				{
					return linkDragData;
				}
			}
			return null;
		}

		protected function createDragSource():DragSource
		{
			var source:DragSource = new DragSource();
			var linkDragData:VaLinkDragData = new VaLinkDragData;
			linkDragData.oldLink = targetLink;
			var link:VaLink = new VaLink();
			if(targetLink)
			{
				link = clearLinkData(targetLink.clone());
				if (type == VaConstant.ELEMENT_TYPE_VM)
				{
					linkDragData.startNodeType = VaConstant.ELEMENT_TYPE_SG;
				}
				else if (type == VaConstant.ELEMENT_TYPE_SG)
				{
					linkDragData.startNodeType = VaConstant.ELEMENT_TYPE_VM;
				}
			} else {
				link = updateLinkData(new VaLink());
				link.id = NaN;
				linkDragData.startNodeType = type;
			}
			linkDragData.newLink = link;

			source.addData(linkDragData, VaDragDataType.LINK.toString());

			return source;
		}

		protected function updateLinkData(link:VaLink):VaLink
		{
			switch(type)
			{
				case VaConstant.ELEMENT_TYPE_VM:
					link.vmId = data.privateId
					link.vmOrder = mouseX/width;
					link.vmSide = VaSide.valueOf(side);
					link.vmRect = data.rect;
					break;
				case VaConstant.ELEMENT_TYPE_SG:
					link.sgId = data.privateId
					link.sgOrder = mouseX/width;
					link.sgSide = VaSide.valueOf(side);
					link.sgRect = data.rect;
					break;
			}
			return link;
		}

		protected function clearLinkData(link:VaLink):VaLink
		{
			switch(type)
			{
				case VaConstant.ELEMENT_TYPE_VM:
					link.vmId = NaN;
					link.vmOrder = NaN;
					link.vmSide = null;
					link.vmRect = null;
					break;
				case VaConstant.ELEMENT_TYPE_SG:
					link.sgId = NaN
					link.sgOrder = NaN;
					link.sgSide = null;
					link.sgRect = null;
					break;
			}
			return link;
		}

		protected function hasSameLinkData(link:VaLink):Boolean
		{
			for each (var myLink:VaLink in data.linkList)
			{
				switch(type)
				{
					case VaConstant.ELEMENT_TYPE_VM:
						if (myLink.vmId == data.privateId
							&& link.sgId == myLink.sgId)
						{
							return true;
						}
						break;
					case VaConstant.ELEMENT_TYPE_SG:
						if (myLink.sgId == data.privateId
							&& link.vmId == myLink.vmId)
						{
							return true;
						}
						break;
				}
			}
			return false;
		}

		protected function isUpdateInside(linkDragData:VaLinkDragData):Boolean
		{
			var oldLink:VaLink = linkDragData.oldLink;
			if(oldLink)
			{
				switch (type)
				{
					case VaConstant.ELEMENT_TYPE_VM:
						return oldLink.vmId == data.privateId;
					case VaConstant.ELEMENT_TYPE_SG:
						return oldLink.sgId == data.privateId;
				}
			}
			return false;
		}

		protected function createDragImage():IFlexDisplayObject
		{
			//何も表示しない。
			return new SpriteAsset();
		}
	}
}