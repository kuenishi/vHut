/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;

	import jp.co.ntts.vhut.comp.va.domain.ConnectorGuide;
	import jp.co.ntts.vhut.comp.va.domain.VaConstant;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.comp.va.domain.VaLinkDragData;
	import jp.co.ntts.vhut.comp.va.domain.VaSide;
	import jp.co.ntts.vhut.comp.va.presentation.connector.ConnectorItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.LinkItemRenderer;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.controls.List;
	import mx.core.ClassFactory;
	import mx.core.IDataRenderer;
	import mx.core.IFactory;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;

	/**
	 * 接続等のガイドを表示するレイヤー
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
	public class VaConnectorGuideLayer extends UIComponent
	{
		/** 初期状態 */
		public static const STATUS_NONE:String = "statusNone";
		/** 描画中状態 */
		public static const STATUS_DRAWING:String = "statusDrawing";

		public static const CHANGE_INTEMRENDERER:String = "changeItemRenderer";

		public function VaConnectorGuideLayer()
		{
			super();
			connectorRenderer = new ClassFactory(ConnectorItemRenderer);
			cableRenderer = new ClassFactory(LinkItemRenderer);
		}

		/** ガイドの状態 */
		protected var _status:String = STATUS_NONE;

		/** 接続情報を保持するドメインオブジェクト */
		public function set connectorGuide(value:ConnectorGuide):void
		{
			if(_connectorGuide == value) return;

			if (_linkDragDataWatcher && _linkDragDataWatcher.isWatching())
			{
				_linkDragDataWatcher.unwatch();
			}

			_connectorGuide = value;

			_linkDragDataWatcher = BindingUtils.bindSetter(connectorGuideLinkDragDataChangeHanlder, _connectorGuide, "linkDragData");
		}
		public function get connectorGuide():ConnectorGuide
		{
			return _connectorGuide;
		}
		private var _connectorGuide:ConnectorGuide;

		///////////////////////////////////////////////////////////////
		//
		//  DrawData
		//
		///////////////////////////////////////////////////////////////

		protected var drawData:ConnectorGuideDrawData = new ConnectorGuideDrawData();

		protected function updateDrawData():void
		{
			updateDrawDataForCable();
			updateDrawDataForConnectors();
		}

		protected function updateDrawDataForCable():void
		{
			var points:Array = new Array();
			if(connectorGuide.linkDragData)
			{
				var linkDragData:VaLinkDragData = connectorGuide.linkDragData;
				switch(connectorGuide.status)
				{
					case ConnectorGuide.ACCEPTED:
						if(linkDragData.newLink)
						{
							points = linkDragData.newLink.points;
						}
						break;
					case ConnectorGuide.NOT_ACCEPTED:
						if(linkDragData.newLink)
						{
							var link:VaLink = linkDragData.newLink;
							switch (linkDragData.startNodeType)
							{
								case VaConstant.ELEMENT_TYPE_VM:
									//VM起点
									points = getPointsOnDragging(link.getVmPoint(), link.vmSide, link.cableOffset);
									break;
								case VaConstant.ELEMENT_TYPE_SG:
									points = getPointsOnDragging(link.getSgPoint(), link.sgSide, link.cableOffset);
									break;
							}
						}
						break;
					default:
				}
			}
			drawData.points = points;
		}

		protected function updateDrawDataForConnectors():void
		{
			if(connectorGuide.linkDragData)
			{
				var pos:Point;
				var linkDragData:VaLinkDragData = connectorGuide.linkDragData;
				switch(connectorGuide.status)
				{
					case ConnectorGuide.ACCEPTED:
					case ConnectorGuide.NOT_ACCEPTED:
						switch (linkDragData.startNodeType)
						{
							case VaConstant.ELEMENT_TYPE_VM:
								pos = linkDragData.newLink.getVmPoint(-linkDragData.newLink.connectorHeight);
								drawData.sPosX = pos.x;
								drawData.sPosY = pos.y;
								drawData.sRotation = linkDragData.newLink.vmRotation;
								break;
							case VaConstant.ELEMENT_TYPE_SG:
								pos = linkDragData.newLink.getSgPoint(-linkDragData.newLink.connectorHeight);
								drawData.sPosX = pos.x;
								drawData.sPosY = pos.y;
								drawData.sRotation = linkDragData.newLink.sgRotation;
								break;
						}
						break;
					default:
				}
			}
		}

		protected function getPointsOnDragging(startPos:Point, startSide:VaSide, cableOffset:Number):Array
		{
			var midPoint:Point = startPos.clone();
			switch(startSide)
			{
				case VaSide.NORTH:
					midPoint.y -= cableOffset;
					break;
				case VaSide.EAST:
					midPoint.x += cableOffset;
					break;
				case VaSide.SOUTH:
					midPoint.y += cableOffset;
					break;
				case VaSide.WEST:
					midPoint.x -= cableOffset;
					break;
			}

			var endPoint:Point = new Point();
				endPoint.x = mouseX + horizontalScrollPosition;
				endPoint.y = mouseY + verticalScrollPosition;

			return new Array(startPos, midPoint, endPoint);
		}

		///////////////////////////////////////////////////////////////
		//
		//  Override UIComponent
		//
		///////////////////////////////////////////////////////////////

		override protected function commitProperties():void
		{
			super.commitProperties();
			if (connectorRendererChanged)
			{
				rebuildConnectors();
				connectorRendererChanged = false;
			}

			if (cableRendererChanged)
			{
				rebuildCable();
				cableRendererChanged = false;
			}

			if (drawDataChanged)
			{
				updateDrawData();
				drawDataChanged = false;
			}
		}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			updateConnectors();
			updateCable();
		}

		///////////////////////////////////////////////////////
		//
		//  コネクタ
		//
		///////////////////////////////////////////////////////

		/** 開始コネクタ */
		protected var sConnector:DisplayObject;

		/** 終了コネクタ */
		protected var eConnector:DisplayObject;

		/** コネクタの再構築 */
		protected function rebuildConnectors():void
		{
			if(sConnector)
			{
				removeChild(sConnector);
			}
			sConnector = connectorRenderer.newInstance();
			addChild(sConnector);

			if(eConnector)
			{
				removeChild(eConnector);
			}
			eConnector = connectorRenderer.newInstance();
			addChild(eConnector);

			updateConnectors();
		}

		/** コネクタの状態更新 */
		protected function updateConnectors():void
		{
			switch(connectorGuide.status)
			{
				case ConnectorGuide.ACCEPTED:
				case ConnectorGuide.NOT_ACCEPTED:
					sConnector.visible = true;
					eConnector.visible = false;

					sConnector.x = drawData.sPosX - horizontalScrollPosition;
					sConnector.y = drawData.sPosY - verticalScrollPosition;
					sConnector.rotation = drawData.sRotation;

//					eConnector.x = drawData.ePosX - horizontalScrollPosition;
//					eConnector.y = drawData.ePosY - verticalScrollPosition;
//					eConnector.rotation = drawData.eRotation;
					break;
				default:
					sConnector.visible = false;
					eConnector.visible = false;
			}
		}

		/**
		 *  @private
		 *  Storage for the itemRenderer property.
		 */
		private var _connectorRenderer:IFactory;

		[Inspectable(category="Data")]

		public function get connectorRenderer():IFactory
		{
			return _connectorRenderer;
		}

		/**
		 *  @private
		 */
		public function set connectorRenderer(value:IFactory):void
		{
			_connectorRenderer = value;

			invalidateProperties();
			invalidateSize();
			invalidateDisplayList();

			//			itemsSizeChanged = true;
			//			itemsNeedMeasurement = true;
			connectorRendererChanged = true;

			dispatchEvent(new Event(CHANGE_INTEMRENDERER));
		}

		protected var connectorRendererChanged:Boolean = false;

		///////////////////////////////////////////////////////
		//
		//  ケーブル
		//
		///////////////////////////////////////////////////////

		/** ケーブル */
		protected var cable:DisplayObject;

		/** コネクタの再構築 */
		protected function rebuildCable():void
		{
			if(cable)
			{
				removeChild(cable);
			}
			cable = cableRenderer.newInstance();
			addChild(cable);
			(cable as IDataRenderer).data = drawData;
			updateCable();
		}

		/** コネクタの状態更新 */
		protected function updateCable():void
		{
			switch(connectorGuide.status)
			{
				case ConnectorGuide.ACCEPTED:
				case ConnectorGuide.NOT_ACCEPTED:
					cable.visible = true;
					cable.x = -horizontalScrollPosition;
					cable.y = -verticalScrollPosition;
					break;
				default:
					cable.visible = false;
			}
		}

		/**
		 *  @private
		 *  Storage for the itemRenderer property.
		 */
		private var _cableRenderer:IFactory;

		[Inspectable(category="Data")]

		public function get cableRenderer():IFactory
		{
			return _cableRenderer;
		}

		/**
		 *  @private
		 */
		public function set cableRenderer(value:IFactory):void
		{
			_cableRenderer = value;

			invalidateProperties();
			invalidateSize();
			invalidateDisplayList();

			//			itemsSizeChanged = true;
			//			itemsNeedMeasurement = true;
			cableRendererChanged = true;

			dispatchEvent(new Event(CHANGE_INTEMRENDERER));
		}

		protected var cableRendererChanged:Boolean = false;

		///////////////////////////////////////////////////////////////
		//
		//  Binding
		//
		///////////////////////////////////////////////////////////////

		/** ドメインモデルの状態変化を監視するハンドラ */
		protected function connectorGuideLinkDragDataChangeHanlder(linkDragData:VaLinkDragData):void
		{
			switch(connectorGuide.status)
			{
				case ConnectorGuide.NONE:
					endDraw();
					break;
				case ConnectorGuide.ACCEPTED:
					startDraw();
					break;
				case ConnectorGuide.NOT_ACCEPTED:
					startDraw();
					break;
			}
		}
		private var _linkDragDataWatcher:ChangeWatcher;

		///////////////////////////////////////////////////////////////
		//
		//  描画
		//
		///////////////////////////////////////////////////////////////

		/** 描画開始 */
		protected function startDraw():void
		{
			if (_status == STATUS_NONE)
			{
				_status = STATUS_DRAWING;
				stage.addEventListener(MouseEvent.MOUSE_MOVE, drawOnMouseMoveHandler);
				drawDataChanged = true;
				invalidateProperties();
				invalidateDisplayList();
			}
		}

		/** 描画中マウスが移動する度に起動するハンドラ */
		protected function drawOnMouseMoveHandler(event:MouseEvent):void
		{
			switch(connectorGuide.status)
			{
				case ConnectorGuide.NONE:
					break;
				case ConnectorGuide.ACCEPTED:
					drawDataChanged = true;
					invalidateProperties();
					invalidateDisplayList();
					break;
				case ConnectorGuide.NOT_ACCEPTED:
					drawDataChanged = true;
					invalidateProperties();
					invalidateDisplayList();
					break;
			}
		}

		/** 描画終了 */
		protected function endDraw():void
		{
			if (_status == STATUS_DRAWING)
			{
				stage.removeEventListener(MouseEvent.MOUSE_MOVE, drawOnMouseMoveHandler);
				_status = STATUS_NONE;
//				drawDataChanged = true;
				invalidateProperties();
				invalidateDisplayList();
			}
		}

		protected var drawDataChanged:Boolean = false;

		///////////////////////////////////////////////////////////////
		//
		//  疑似スクロール
		//
		///////////////////////////////////////////////////////////////
		[Bindable]
		[Inspectable(category="General", minValue="0.0")]
		/** 水平方向のスクロール */
		public function get horizontalScrollPosition():Number
		{
			return _horizontalScrollPosition;
		}
		/**
		 *  @private
		 */
		public function set horizontalScrollPosition(value:Number):void
		{
			if (value == _horizontalScrollPosition)
				return;

			_horizontalScrollPosition = value;
			invalidateDisplayList();
		}
		private var _horizontalScrollPosition:Number = 0;

		[Bindable]
		[Inspectable(category="General", minValue="0.0")]
		/** 垂直方向のスクロール */
		public function get verticalScrollPosition():Number
		{
			return _verticalScrollPosition;
		}
		/**
		 *  @private
		 */
		public function set verticalScrollPosition(value:Number):void
		{
			if (value == _verticalScrollPosition)
				return;

			_verticalScrollPosition = value;
			invalidateDisplayList()
		}
		private var _verticalScrollPosition:Number = 0;
	}
}


import flash.events.Event;
import flash.events.EventDispatcher;
[Bindable]
class ConnectorGuideDrawData extends EventDispatcher
{

	public var points:Array = new Array();

	public var sPosX:Number;
	public var sPosY:Number;
	public var sRotation:Number;

	public var ePosX:Number;
	public var ePosY:Number;
	public var eRotation:Number;
}