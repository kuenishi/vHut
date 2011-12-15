/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.cal.presentation
{
	import flash.events.MouseEvent;

	import jp.co.ntts.vhut.app.cal.application.TermCalendarEvent;
	import jp.co.ntts.vhut.app.cal.domain.CalSlot;
	import jp.co.ntts.vhut.app.cal.domain.Terms;
	import jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent;

	import mx.binding.utils.BindingUtils;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	import mx.core.IFactory;
	import mx.core.IVisualElement;
	import mx.events.FlexEvent;

	import spark.components.DataGroup;
	import spark.components.IItemRenderer;
	import spark.components.SkinnableDataContainer;
	import spark.events.RendererExistenceEvent;

	[Event(name="reservingTermChanged", type="jp.co.ntts.vhut.app.cal.application.TermCalendarEvent")]
	/**
	 * 期間予約カレンダー
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
	public class TermCalendar extends SkinnableDataContainer
	{
		public function TermCalendar()
		{
			super();
			terms = new Terms();
			setStyle("skinClass", TermCalendarSkin);
			itemRenderer = new ClassFactory(CalSlotItemRenderer);
			headerItemRenderer = new ClassFactory(CalHeaderItemRenderer);
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		protected var terms:Terms;

		protected var isInvalidatedReservingTermList:Boolean = false;

		public function set currentMonth(value:Date):void
		{
			terms.currentMonth = value;
		}

		public function set reservedTermList(value:IList):void
		{
			terms.reservedTermList = value;

			isInvalidatedReservingTermList = true;
			invalidateProperties();
		}

		public function set availableTermList(value:IList):void
		{
			terms.availableTermList = value;

			isInvalidatedReservingTermList = true;
			invalidateProperties();
		}

		public function set startDay(value:String):void
		{
			terms.startDay = value;
			if(headerDataGroup)
				headerDataGroup.dataProvider = terms.calHeaderList;
		}

		public function get reservingTermList():IList
		{
			return terms.reservingTermList;
		}

		override protected function commitProperties():void
		{
			super.commitProperties();

			if(isInvalidatedReservingTermList)
			{
				dispatchEvent(TermCalendarEvent.newReservingTermChangedEvent(reservingTermList))
				isInvalidatedReservingTermList = false;
			}

		}

		/**
		 *  @private
		 */
		protected function creationCompleteHandler(event:FlexEvent):void
		{
			BindingUtils.bindProperty(this.dataGroup, "dataProvider", terms, "calSlotList");
		}

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
			else if (instance == headerDataGroup)
			{
				if (terms.calHeaderList)
				{
					headerDataGroup.dataProvider = terms.calHeaderList;
				}

				if (_headerItemRenderer)
				{
					headerDataGroup.itemRenderer = _headerItemRenderer;
				}
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
			renderer.addEventListener(MouseEvent.MOUSE_OVER, item_mouseOverHandler);
			renderer.addEventListener(MouseEvent.MOUSE_OUT, item_mouseOutHandler);
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
			renderer.removeEventListener(MouseEvent.MOUSE_OVER, item_mouseOverHandler);
			renderer.removeEventListener(MouseEvent.MOUSE_OUT, item_mouseOutHandler);
			return renderer;
		}

		protected function item_mouseDownHandler(event:MouseEvent):void
		{
			var index:int = getIndex(event.currentTarget);
			//selection
			onStartSelection(index);
			//hover
			onStartDragging(index);
		}

		protected function item_mouseUpHandler(event:MouseEvent):void
		{
			var index:int = getIndex(event.currentTarget);
			//selection
			onEndSelection(index);
			//hover
			onEndDragging();
		}

		protected function item_mouseOverHandler(event:MouseEvent):void
		{
			var index:int = getIndex(event.currentTarget);
			//hover
			onDragging(index);
		}

		protected function item_mouseOutHandler(event:MouseEvent):void
		{
			var index:int = getIndex(event.currentTarget);
			//hover
			onDragging(-1);
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

		protected function getIndex(target:Object):int
		{
			var index:int = -1;
			if (target is IItemRenderer)
				index = IItemRenderer(target).itemIndex;
			else
				index = dataGroup.getElementIndex(target as IVisualElement);
			return index;
		}

		///////////////////////////////////////////////////////////////
		//
		// selection
		//
		///////////////////////////////////////////////////////////////

		/** 選択の開始インデックス(-1は非選択) */
		protected var selectedIndexStart:int = -1;

		protected function onStartSelection(index:int):void
		{
			selectedIndexStart = index;
		}

		protected function onEndSelection(index:int):void
		{
			reverseCalSlot(selectedIndexStart, index);
			selectedIndexStart = -1;
		}

		protected function reverseCalSlot(start:int, end:int):void
		{
			if(start < 0 || end < 0)
				return;
			var reqStart:int = Math.floor(Math.min(start, end));
			var reqEnd:int = Math.floor(Math.max(start, end));

			terms.reverseCalSlot(reqStart, reqEnd);

			isInvalidatedReservingTermList = true;
			invalidateProperties();
		}

		///////////////////////////////////////////////////////////////
		//
		// hover
		//
		///////////////////////////////////////////////////////////////

		/** の開始インデックス(-1は非選択) */
		protected var hoveredIndexStart:int = -1;

		protected function onStartDragging(index:int):void
		{
			hoveredIndexStart = index;
		}

		protected function onDragging(index:int):void
		{
			updateHovered(hoveredIndexStart > 0 ? hoveredIndexStart : index, index);
		}

		protected function onEndDragging():void
		{
			hoveredIndexStart = -1;
			updateHovered(-1, -1);
		}

		protected function updateHovered(start:int, end:int):void
		{
			if(start < 0 || end < 0)
			{
				clearHovered();
			}
			else
			{
				var reqStart:int = Math.floor(Math.min(start, end));
				var reqEnd:int = Math.floor(Math.max(start, end));
				setHovered(reqStart, reqEnd);
			}
		}

		protected function clearHovered():void
		{
			for each (var calSlot:CalSlot in dataProvider)
			{
				if (calSlot.hovered)
				{
					calSlot.hovered = false;
					dataProvider.itemUpdated(calSlot, "hovered", true, false);
				}
			}
		}

		protected function setHovered(start:uint, end:uint):void
		{
			for (var i:uint=0; i<dataProvider.length; i++)
			{
				var calSlot:CalSlot = dataProvider.getItemAt(i) as CalSlot;
				if (i >= start && i <= end && !calSlot.hovered)
				{
					calSlot.hovered = true;
					dataProvider.itemUpdated(calSlot, "hovered", false, true);
				}
				else if(calSlot.hovered)
				{
					calSlot.hovered = false;
					dataProvider.itemUpdated(calSlot, "hovered", true, false);
				}
			}
		}

		///////////////////////////////////////////////////////////////
		//
		// header
		//
		///////////////////////////////////////////////////////////////

		[SkinPart(required="false")]
		/** カレンダーの曜日部分を表示する */
		public var headerDataGroup:DataGroup;

		public function get headerItemRenderer():IFactory
		{
			return (headerDataGroup)
			? headerDataGroup.itemRenderer
				: _headerItemRenderer;
		}

		/**
		 *  @private
		 */
		public function set headerItemRenderer(value:IFactory):void
		{
			if (headerDataGroup)
			{
				headerDataGroup.itemRenderer = value;
			}
			else
				_headerItemRenderer = value;
		}

		protected var _headerItemRenderer:IFactory;


	}
}