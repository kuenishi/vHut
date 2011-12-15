/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import flash.events.MouseEvent;
	import flash.geom.Point;

	import jp.co.ntts.vhut.comp.va.application.AddEvent;
	import jp.co.ntts.vhut.comp.va.application.RemoveEvent;
	import jp.co.ntts.vhut.comp.va.application.UpdateEvent;
	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.VaConstant;
	import jp.co.ntts.vhut.comp.va.domain.VaDragDataType;
	import jp.co.ntts.vhut.comp.va.infrastructure.SgItemRendererEvent;
	import jp.co.ntts.vhut.comp.va.infrastructure.VmItemRendererEvent;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.EditableSgItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.EditableVmItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.RappSgTemplateItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.RappTemplateItemRenderer;
	import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
	import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;

	import mx.core.ClassFactory;
	import mx.core.DragSource;
	import mx.core.IFactory;
	import mx.core.IFlexDisplayObject;
	import mx.core.IVisualElement;
	import mx.core.mx_internal;
	import mx.managers.DragManager;

	import spark.components.IItemRenderer;
	import spark.components.supportClasses.ItemRenderer;
	import spark.events.RendererExistenceEvent;

	use namespace mx_internal;  //ListBase and List share selection properties that are mx_internal

	[Event(name="addVm", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="addSg", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="addDisk", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="updateVm", type="jp.co.ntts.vhut.comp.va.application.UpdateEvent")]
	[Event(name="updateSg", type="jp.co.ntts.vhut.comp.va.application.UpdateEvent")]
	[Event(name="removeVm", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	[Event(name="removeSg", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	[Event(name="removeDisk", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class EditableVaElementLayer extends VaElementLayer
	{
		public function EditableVaElementLayer()
		{
			super();
		}

		override protected function itemRenderFunctionHandler(item:Object):IFactory
		{
			var clazz:Class;
			if(item is ApplicationVm || item is ApplicationInstanceVm)
			{
				if(vmItemRenderer != null)
					return vmItemRenderer;
				clazz = EditableVmItemRenderer;
			}
			else if(item is ReleasedApplicationTemplate)
			{
				if(vmItemRenderer != null)
					return vmItemRenderer;
				clazz = RappTemplateItemRenderer;
			}
			else if(item is ApplicationSecurityGroup || item is ApplicationInstanceSecurityGroup)
			{
				if(sgItemRenderer != null)
					return sgItemRenderer;
				clazz = EditableSgItemRenderer;
			}
			else if(item is ReleasedApplicationSecurityGroupTemplate)
			{
				if(sgItemRenderer != null)
					return sgItemRenderer;
				clazz = RappSgTemplateItemRenderer;
			}
			return new ClassFactory(clazz);
		}

		/**
		 *  @private
		 *  Called when an item has been added to this component.
		 */
		override protected function dataGroup_rendererAddHandler(event:RendererExistenceEvent):IVisualElement
		{
			var renderer:IVisualElement = super.dataGroup_rendererAddHandler(event);
			if(renderer is EditableVmItemRenderer)
			{
				(renderer as EditableVmItemRenderer).addEventListener(VmItemRendererEvent.REMOVE_VM, removeVmHandler);
			}
			else if(renderer is EditableSgItemRenderer)
			{
				(renderer as EditableSgItemRenderer).addEventListener(SgItemRendererEvent.REMOVE_SG, removeSgHandler);
			}
			return renderer;
		}

		/**
		 *  @private
		 *  Called when an item has been removed from this component.
		 */
		override protected function dataGroup_rendererRemoveHandler(event:RendererExistenceEvent):IVisualElement
		{
			var renderer:IVisualElement = super.dataGroup_rendererRemoveHandler(event);
			if(renderer is EditableVmItemRenderer)
			{
				(renderer as EditableVmItemRenderer).removeEventListener(VmItemRendererEvent.REMOVE_VM, removeVmHandler);
			}
			else if(renderer is EditableSgItemRenderer)
			{
				(renderer as EditableSgItemRenderer).removeEventListener(SgItemRendererEvent.REMOVE_SG, removeSgHandler);
			}
			return renderer;
		}

		///////////////////////////////////////////////////////////////
		//
		//  Drag & Drop Elements inside.
		//
		///////////////////////////////////////////////////////////////

		protected var draggingTriger:ItemRenderer;

		override protected function item_mouseDownHandler(event:MouseEvent):void
		{
			super.item_mouseDownHandler(event);

			if(event.currentTarget is ItemRenderer)
			{
				draggingTriger = event.currentTarget as ItemRenderer;
				stage.addEventListener(MouseEvent.MOUSE_MOVE, stage_mouseMoveHandler);
				stage.addEventListener(MouseEvent.MOUSE_UP, stage_mouseUpHandler);
			}
		}

		protected function stage_mouseMoveHandler(event:MouseEvent):void
		{
			if(stage)
			{
				stage.removeEventListener(MouseEvent.MOUSE_MOVE, stage_mouseMoveHandler);
				DragManager.doDrag(draggingTriger, createDragSource(), event, createDragImage());
			}
		}

		protected function stage_mouseUpHandler(event:MouseEvent):void
		{
			if(stage)
			{
				stage.removeEventListener(MouseEvent.MOUSE_MOVE, stage_mouseMoveHandler);
			}
		}

		protected function createDragSource():DragSource
		{
			var result:DragSource = new DragSource();
			if (draggingTriger)
			{
				var data:IVaElement = draggingTriger.data as IVaElement;
				if(data)
				{
					switch (data.type)
					{
						case VaConstant.ELEMENT_TYPE_VM:
							result.addData( data, VaDragDataType.VM.toString() );
							break;
						case VaConstant.ELEMENT_TYPE_SG:
							result.addData( data, VaDragDataType.SG.toString() );
							break;
					}
					result.addData( new Point(
						draggingTriger.mouseX
						, draggingTriger.mouseY
					)
						, VaDragDataType.OFFSET.toString() );
				}
			}
			return result;
		}

		protected function createDragImage():IFlexDisplayObject
		{
			if (draggingTriger)
			{
				var data:IVaElement = draggingTriger.data as IVaElement;
				if(data)
				{
					var factory:IFactory = itemRenderFunctionHandler(data);
					var dragImage:ItemRenderer = factory.newInstance() as ItemRenderer;
					if(dragImage)
					{
						dragImage.data = data;
						return dragImage;
					}
				}
			}
			return null;
		}

		///////////////////////////////////////////////////////////////
		//
		//  Remove Item. one by one
		//
		///////////////////////////////////////////////////////////////

		protected function removeVmHandler(event:VmItemRendererEvent):void
		{
			if(event.data is ApplicationVm)
			{
				setSelectedIndex(-1, true);
				dispatchEvent(RemoveEvent.newRemoveVmEvent(event.data));
			}
		}

		protected function removeSgHandler(event:SgItemRendererEvent):void
		{
			if(event.data is ApplicationSecurityGroup)
			{
				setSelectedIndex(-1, true);
				dispatchEvent(RemoveEvent.newRemoveSgEvent(event.data));
			}
		}
	}
}