/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.comp.va
{
	import flash.events.MouseEvent;
	import flash.geom.Point;

	import jp.co.ntts.vhut.comp.va.application.AddEvent;
	import jp.co.ntts.vhut.comp.va.application.ConnectorDragEvent;
	import jp.co.ntts.vhut.comp.va.application.RemoveEvent;
	import jp.co.ntts.vhut.comp.va.application.UpdateEvent;
	import jp.co.ntts.vhut.comp.va.domain.ConnectorGuide;
	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.VaDragDataType;
	import jp.co.ntts.vhut.comp.va.domain.VaLinkDragData;
	import jp.co.ntts.vhut.comp.va.presentation.layer.EditableVaElementLayer;
	import jp.co.ntts.vhut.comp.va.presentation.layer.VaConnectorGuideLayer;
	import jp.co.ntts.vhut.dto.SwitchTemplateDto;
	import jp.co.ntts.vhut.entity.BaseTemplate;

	import mx.core.IUIComponent;
	import mx.events.DragEvent;
	import mx.managers.DragManager;


	[Event(name="addVm", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="addSg", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="addLink", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="addDisk", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="updateVm", type="jp.co.ntts.vhut.comp.va.application.UpdateEvent")]
	[Event(name="updateSg", type="jp.co.ntts.vhut.comp.va.application.UpdateEvent")]
	[Event(name="updateLink", type="jp.co.ntts.vhut.comp.va.application.UpdateEvent")]
	[Event(name="removeVm", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	[Event(name="removeSg", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	[Event(name="removeLink", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	[Event(name="removeDisk", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	/**
	 * VM構成の編集コンポーネント.
	 * <p>下記の処理を行います
	 * <ul>
	 * <li>VMの選択変更
	 * <li>NWの選択変更
	 * <li>VMの構成変更
	 * <li>NWの構成変更
	 * <li>NWの起動
	 * <li>NWの停止
	 * </ul>
	 * <p>下記の要素を表示します
	 * <ul>
	 * <li>VMの構成データ
	 * <li>NWの構成データ
	 * </ul>
	 * </p>
	 * vaはvirtualized applicationの略
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
	public class VaEditor extends VaController
	{
		public function VaEditor()
		{
			super();
			setStyle("skinClass", VaEditorSkin);
			addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
			addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
		}

		protected var _connectorGuide:ConnectorGuide = new ConnectorGuide();

		/////////////////////////////////////////////////////////////////////////////
		//
		//  スキン関連
		//
		////////////////////////////////////////////////////////////////////////////

		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);

			if (instance == connectorGuideLayer)
			{
				initializeConnectorGuideLayer();
			}
		}

		/**
		 *  @private
		 */
		override protected function partRemoved(partName:String, instance:Object):void
		{

			if (instance == connectorGuideLayer)
			{
				finalizeConnectorGuideLayer();
			}
			super.partRemoved(partName, instance);
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  要素（VM, SecurityGroup）描画
		//
		/////////////////////////////////////////////////////////////////////////////

		override protected function initializeElementLayer():void
		{
			super.initializeElementLayer();

			if(elementLayer is EditableVaElementLayer)
			{
				var layer:EditableVaElementLayer = elementLayer as EditableVaElementLayer;

				layer.addEventListener(UpdateEvent.UPDATE_VM, dispatchEvent);
				layer.addEventListener(UpdateEvent.UPDATE_SG, dispatchEvent);
				layer.addEventListener(RemoveEvent.REMOVE_VM, dispatchEvent);
				layer.addEventListener(RemoveEvent.REMOVE_SG, dispatchEvent);

				layer.addEventListener(ConnectorDragEvent.BEGIN_DRAG, beginConnectorDragHandler)
				layer.addEventListener(ConnectorDragEvent.CHANGE_DRAG, changeConnectorDragHandler)
				layer.addEventListener(ConnectorDragEvent.END_DRAG, endConnectorDragHandler)
			}
		}

		override protected function finalizeElementLayer():void
		{
			if(elementLayer is EditableVaElementLayer)
			{
				var layer:EditableVaElementLayer = elementLayer as EditableVaElementLayer;

				layer.removeEventListener(UpdateEvent.UPDATE_VM, dispatchEvent);
				layer.removeEventListener(UpdateEvent.UPDATE_SG, dispatchEvent);
				layer.removeEventListener(RemoveEvent.REMOVE_VM, dispatchEvent);
				layer.removeEventListener(RemoveEvent.REMOVE_SG, dispatchEvent);

				layer.removeEventListener(ConnectorDragEvent.BEGIN_DRAG, beginConnectorDragHandler)
				layer.removeEventListener(ConnectorDragEvent.CHANGE_DRAG, changeConnectorDragHandler)
				layer.removeEventListener(ConnectorDragEvent.END_DRAG, endConnectorDragHandler)
			}
			super.finalizeElementLayer();
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  ネットワーク接続補助ライン描画
		//
		/////////////////////////////////////////////////////////////////////////////

		[SkinPart(required="false")]
		public var connectorGuideLayer:VaConnectorGuideLayer;


		protected function initializeConnectorGuideLayer():void
		{
			connectorGuideLayer.connectorGuide = _connectorGuide;
			connectorGuideLayer.horizontalScrollPosition = horizontalScrollPosition;
			connectorGuideLayer.verticalScrollPosition = verticalScrollPosition;
		}

		protected function finalizeConnectorGuideLayer():void
		{

		}

		///////////////////////////////////////////////////////////////
		//
		//  Drop from outside
		//
		///////////////////////////////////////////////////////////////

		protected function dragEnterHandler(event:DragEvent):void
		{
			if(event.dragSource.hasFormat(VaDragDataType.BASE_TEMPLATE.toString())
				|| event.dragSource.hasFormat(VaDragDataType.SWITCH_TEMPLATE.toString())
				|| event.dragSource.hasFormat(VaDragDataType.VM.toString())
				|| event.dragSource.hasFormat(VaDragDataType.SG.toString()))
			{
				DragManager.acceptDragDrop(event.target as IUIComponent);
			}
		}

		protected function dragDropHandler(event:DragEvent):void
		{
			var posx:Number = event.localX + horizontalScrollPosition;
			var posy:Number = event.localY + verticalScrollPosition;

			if(event.dragSource.hasFormat(VaDragDataType.OFFSET.toString()))
			{
				var offset:Point = event.dragSource.dataForFormat(VaDragDataType.OFFSET.toString()) as Point;
				posx -= offset.x;
				posy -= offset.y;
			}

			if(event.dragSource.hasFormat(VaDragDataType.BASE_TEMPLATE.toString()))
			{
				var baseTemplate:BaseTemplate = event.dragSource.dataForFormat(VaDragDataType.BASE_TEMPLATE.toString()) as BaseTemplate;
				if(baseTemplate)
					dispatchEvent(AddEvent.newAddVmEvent(baseTemplate, posx, posy));
			}
			else if(event.dragSource.hasFormat(VaDragDataType.SWITCH_TEMPLATE.toString()))
			{
				var switchTemplate:SwitchTemplateDto = event.dragSource.dataForFormat(VaDragDataType.SWITCH_TEMPLATE.toString()) as SwitchTemplateDto;
				if(switchTemplate)
					dispatchEvent(AddEvent.newAddSgEvent(switchTemplate, posx, posy));
			}
			else if(event.dragSource.hasFormat(VaDragDataType.VM.toString()))
			{
				var vmData:IVaElement = event.dragSource.dataForFormat(VaDragDataType.VM.toString()) as IVaElement;
				if(vmData)
					dispatchEvent(UpdateEvent.newUpdateVmEvent(vmData, posx, posy));
			}
			else if(event.dragSource.hasFormat(VaDragDataType.SG.toString()))
			{
				var sgData:IVaElement = event.dragSource.dataForFormat(VaDragDataType.SG.toString()) as IVaElement;
				if(sgData)
					dispatchEvent(UpdateEvent.newUpdateSgEvent(sgData, posx, posy));
			}
		}

		///////////////////////////////////////////////////////////////
		//
		//  Drag and Drop Connector
		//
		///////////////////////////////////////////////////////////////

		protected function beginConnectorDragHandler(event:ConnectorDragEvent):void
		{
			_connectorGuide.linkDragData = event.data;
			stage.addEventListener(MouseEvent.MOUSE_UP, connectorMouseUpHandler);
		}

		protected function changeConnectorDragHandler(event:ConnectorDragEvent):void
		{
			_connectorGuide.linkDragData = event.data;
		}

		protected function endConnectorDragHandler(event:ConnectorDragEvent):void
		{
			_connectorGuide.linkDragData = event.data;
			if(_connectorGuide.status == ConnectorGuide.ACCEPTED)
			{
				var linkDragData:VaLinkDragData = event.data;
				if(linkDragData.oldLink == null)
				{
					dispatchEvent(AddEvent.newAddLinkEvent(event.data.newLink));
				}
				else
				{
					dispatchEvent(UpdateEvent.newUpdateLinkEvent(event.data.newLink));
				}
			}
			_connectorGuide.linkDragData = null;
		}

		protected function connectorMouseUpHandler(event:MouseEvent):void
		{
			_connectorGuide.linkDragData = null;
			stage.removeEventListener(MouseEvent.MOUSE_UP, connectorMouseUpHandler);
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  スクロール処理
		//
		/////////////////////////////////////////////////////////////////////////////

		/**
		 * 水平スクロール位置
		 */
		override protected function set horizontalScrollPosition(value:Number):void
		{
			super.horizontalScrollPosition = value;
			if(connectorGuideLayer)
				connectorGuideLayer.horizontalScrollPosition = value;
		}

		/**
		 * 垂直スクロール位置
		 */
		override protected function set verticalScrollPosition(value:Number):void
		{
			super.verticalScrollPosition = value;
			if(connectorGuideLayer)
				connectorGuideLayer.verticalScrollPosition = value;
		}

	}
}