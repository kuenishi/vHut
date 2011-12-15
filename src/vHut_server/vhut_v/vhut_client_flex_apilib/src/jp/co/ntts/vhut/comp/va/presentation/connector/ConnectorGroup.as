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
	import jp.co.ntts.vhut.comp.va.domain.VaSide;

	import mx.collections.IList;
	import mx.core.ClassFactory;
	import mx.core.UIComponent;
	import mx.events.CollectionEvent;

	import spark.components.SkinnableDataContainer;

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
	public class ConnectorGroup extends SkinnableDataContainer
	{
		public static const CHANGE_DATA:String = "changeData";

		public function ConnectorGroup()
		{
			super();
			setStyle("skinClass", ConnectorGroupSkin);
			itemRenderer = new ClassFactory(ConnectorItemRenderer);
			layout = new ConnectorLayout();
			updateLayout();
		}

		[SkinPart(required="false")]
		public var background:UIComponent;

//		[SkinPart(required="false")]
//		public var cursor:UIComponent;

		[Bindable]
		public function set hovered(value:Boolean):void
		{
			if(_hovered == value)
				return;
			_hovered = value;
			invalidateSkinState();
		}
		public function get hovered():Boolean
		{
			return _hovered;
		}
		private var _hovered:Boolean = false;

		[Bindable]
		[Inspectable(category="General")]
		public function set data(value:IVaElement):void
		{
			if(value != _data)
			{
				_data = value;
				updateDataProvider();
				updateLayout();
				dispatchEvent(new Event(CHANGE_DATA));
			}
		}
		public function get data():IVaElement
		{
			return _data;
		}
		private var _data:IVaElement;

		[Bindable("changeData")]
		public function get type():String
		{
			return data.type;
		}

		[Bindable]
		[Inspectable(category="Other", defaultValue="NONE", enumeration="SOUTH,NORTH,WEST,EAST")]
		public function set side(value:String):void
		{
			if(value != _side)
			{
				_side = value;
				updateDataProvider();
			}
		}
		public function get side():String
		{
			return _side;
		}
		private var _side:String

		private function updateDataProvider():void
		{
			if(data)
			{
				switch(VaSide.valueOf(side))
				{
					case VaSide.NORTH:
						dataProvider = data.northLinkList;
						break;
					case VaSide.SOUTH:
						dataProvider = data.southLinkList;
						break;
					case VaSide.EAST:
						dataProvider = data.eastLinkList;
						break;
					case VaSide.WEST:
						dataProvider = data.westLinkList;
						break;
				}
			}
		}

		protected function updateLayout():void
		{
			if(layout is ConnectorLayout && data)
			{
				(layout as ConnectorLayout).type = data.type;
			}
		}

		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);

			if (instance == background)
			{
				background.addEventListener(MouseEvent.ROLL_OVER, mouseRollOverHandler);
				background.addEventListener(MouseEvent.ROLL_OUT, mouseRollOutHandler);
				background.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			}
		}

		/**
		 *  @private
		 */
		override protected function partRemoved(partName:String, instance:Object):void
		{
			if (instance == background)
			{
				background.removeEventListener(MouseEvent.ROLL_OVER, mouseRollOverHandler);
				background.removeEventListener(MouseEvent.ROLL_OUT, mouseRollOutHandler);
				background.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			}

			super.partRemoved(partName, instance);
		}

		protected function mouseRollOverHandler(event:MouseEvent):void
		{
			hovered = true;
		}

		protected function mouseRollOutHandler(event:MouseEvent):void
		{
			hovered = false;
		}

		protected function mouseUpHandler(event:MouseEvent):void
		{
			event.stopPropagation();
		}

		/**
		 *  @private
		 */
		override protected function getCurrentSkinState():String
		{
			if (!enabled)
				return "disabled";

			if (hovered)
				return "hovered";

			return "normal";
		}

		////////////////////////////////////////////////////////////////////////////
		//
		//  データプロバイダ
		//
		////////////////////////////////////////////////////////////////////////////

//		override public function set dataProvider(value:IList):void
//		{
//			if(dataProvider)
//			{
//				dataProvider.removeEventListener(CollectionEvent.COLLECTION_CHANGE, dataProviderCollectionChangeHandler);
//			}
//			super.dataProvider = value;
//			dataProvider.addEventListener(CollectionEvent.COLLECTION_CHANGE, dataProviderCollectionChangeHandler);
//		}
//
//		public function dataProviderCollectionChangeHandler(event:CollectionEvent):void
//		{
//			dataGroup.invalidateDisplayList();
//		}
	}
}