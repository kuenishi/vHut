/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.comp.va
{
	import flash.events.MouseEvent;

	import jp.co.ntts.vhut.comp.va.application.SelectEvent;
	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.Topology;
	import jp.co.ntts.vhut.comp.va.domain.VaConstant;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.comp.va.presentation.layer.VaElementLayer;
	import jp.co.ntts.vhut.comp.va.presentation.layer.VaNetworkLayer;
	import jp.co.ntts.vhut.comp.va.presentation.layer.VaStageLayer;

	import mx.collections.ArrayCollection;

	import spark.components.SkinnableContainer;
	import spark.events.IndexChangeEvent;

	[Event(name="selectVm", type="jp.co.ntts.vhut.comp.va.application.SelectEvent")]
	[Event(name="selectSg", type="jp.co.ntts.vhut.comp.va.application.SelectEvent")]
	/**
	 * VM構成の表示コンポーネント.
	 * <p>下記の処理を行います
	 * <ul>
	 * <li>VMの選択変更
	 * <li>NWの選択変更
	 * </ul>
	 * <p>下記の要素を表示します
	 * <ul>
	 * <li>VMの構成データ
	 * </ul>
	 * </p>
	 * vaはvirtualized applicationの略
	 *
	 * @langversion 3.0
	 * @playerversion Flash 10.1
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class VaViewer extends SkinnableContainer
	{
		/**
		 * コンストラクタ.
		 */
		public function VaViewer()
		{
			super();
			setStyle("skinClass", VaViewerSkin);
		}

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

			if (instance == stageLayer)
			{
				initializeStageLayer();
			}
			else if (instance == networkLayer)
			{
				initializeNetworkLayer();
			}
			else if (instance == elementLayer)
			{
				initializeElementLayer();
			}
		}

		/**
		 *  @private
		 */
		override protected function partRemoved(partName:String, instance:Object):void
		{

			if (instance == stageLayer)
			{
				finalizeStageLayer();
			}
			else if (instance == networkLayer)
			{
				finalizeNetworkLayer();
			}
			else if (instance == elementLayer)
			{
				finalizeElementLayer();
			}
			super.partRemoved(partName, instance);
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  データプロバイダ
		//
		////////////////////////////////////////////////////////////////////////////

		[Bindable]
		public function set topology(value:Topology):void
		{
			_topology = value;
			if(_topology)
			{
				if(elementLayer)
					elementLayer.dataProvider = _topology.elementList;
				if(networkLayer)
					networkLayer.dataProvider = _topology.linkList;
			}
			else
			{
				if(elementLayer)
					elementLayer.dataProvider = new ArrayCollection();
				if(networkLayer)
					networkLayer.dataProvider = new ArrayCollection();
			}
		}
		public function get topology():Topology
		{
			return _topology;
		}
		private var _topology:Topology;

		/////////////////////////////////////////////////////////////////////////////
		//
		//  スクロール処理
		//
		/////////////////////////////////////////////////////////////////////////////

		[SkinPart(required="false")]
		public var stageLayer:VaStageLayer;

		protected function initializeStageLayer():void
		{
			if(stageLayer)
			{
				stageLayer.addEventListener(MouseEvent.MOUSE_DOWN, startScrolling);
				stageLayer.addEventListener(MouseEvent.MOUSE_DOWN, unSelectHandler);
			}
		}

		protected function finalizeStageLayer():void
		{
		}

		/** スクロール開始します */
		private function startScrolling(event:MouseEvent):void
		{
			stageLayer.addEventListener(MouseEvent.MOUSE_MOVE, scrolling);
			stage.addEventListener(MouseEvent.MOUSE_UP, endScrolling);

			_sLocalX = event.localX;
			_sLocalY = event.localY;
			_sScrollH = horizontalScrollPosition;
			_sScrollV = verticalScrollPosition;
		}

		/** スクロール中です */
		private function scrolling(event:MouseEvent):void
		{
			horizontalScrollPosition = _sScrollH - event.localX + _sLocalX;
			verticalScrollPosition = _sScrollV - event.localY + _sLocalY;
		}

		/** スクロールを終了します */
		private function endScrolling(event:MouseEvent):void
		{
			stageLayer.removeEventListener(MouseEvent.MOUSE_MOVE, scrolling);
			stage.removeEventListener(MouseEvent.MOUSE_UP, endScrolling);
		}
		private var _sLocalX:Number;
		private var _sLocalY:Number;
		private var _sScrollH:Number;
		private var _sScrollV:Number;

		/**
		 * 水平スクロール位置
		 */
		protected function set horizontalScrollPosition(value:Number):void
		{
			_horizontalScrollPosition = value;
			if(elementLayer)
				elementLayer.layout.horizontalScrollPosition = _horizontalScrollPosition;
			if(networkLayer)
				networkLayer.layout.horizontalScrollPosition = _horizontalScrollPosition;
		}
		protected function get horizontalScrollPosition():Number
		{
			return _horizontalScrollPosition;
		}
		private var _horizontalScrollPosition:Number = 0;

		/**
		 * 垂直スクロール位置
		 */
		protected function set verticalScrollPosition(value:Number):void
		{
			_verticalScrollPosition = value;
			if(elementLayer)
				elementLayer.layout.verticalScrollPosition = _verticalScrollPosition;
			if(networkLayer)
				networkLayer.layout.verticalScrollPosition = _verticalScrollPosition;
		}
		protected function get verticalScrollPosition():Number
		{
			return _verticalScrollPosition;
		}
		private var _verticalScrollPosition:Number = 0;

		/////////////////////////////////////////////////////////////////////////////
		//
		//  ネットワーク描画
		//
		/////////////////////////////////////////////////////////////////////////////

		[SkinPart(required="false")]
		public var networkLayer:VaNetworkLayer;

		protected function initializeNetworkLayer():void
		{
			if(networkLayer)
			{
				if(topology)
				{
					networkLayer.dataProvider = topology.linkList;
				}
				networkLayer.addEventListener(IndexChangeEvent.CHANGE, networkIndexChangeHandler);
				networkLayer.layout.horizontalScrollPosition = horizontalScrollPosition;
				networkLayer.layout.verticalScrollPosition = verticalScrollPosition;
			}
		}

		protected function finalizeNetworkLayer():void
		{
		}

		protected function networkIndexChangeHandler(event:IndexChangeEvent):void
		{
			if(event.newIndex >= 0)
			{
				var link:VaLink = networkLayer.dataProvider.getItemAt(event.newIndex) as VaLink;
				if(link)
				{
					select(VaConstant.LINK, link);
				}
			}
			else
			{
				dispatchEvent(SelectEvent.newSelectLinkEvent());
			}
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  要素（VM, SecurityGroup）描画
		//
		/////////////////////////////////////////////////////////////////////////////

		[SkinPart(required="false")]
		public var elementLayer:VaElementLayer;

		protected function initializeElementLayer():void
		{
			if(elementLayer)
			{
				if(topology)
				{
					elementLayer.dataProvider = topology.elementList;
				}
				elementLayer.addEventListener(IndexChangeEvent.CHANGE, elementIndexChangeHandler);
				elementLayer.layout.horizontalScrollPosition = horizontalScrollPosition;
				elementLayer.layout.verticalScrollPosition = verticalScrollPosition;
			}
		}

		protected function finalizeElementLayer():void
		{
			elementLayer.removeEventListener(IndexChangeEvent.CHANGE, dispatchEvent);
		}

		protected function elementIndexChangeHandler(event:IndexChangeEvent):void
		{
			if(event.newIndex >= 0)
			{
				var element:IVaElement = elementLayer.dataProvider.getItemAt(event.newIndex) as IVaElement;
				if(element)
				{
					select(element.type, element);
				}
			}
			else
			{
				dispatchEvent(SelectEvent.newSelectSgEvent());
				dispatchEvent(SelectEvent.newSelectVmEvent());
			}
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  選択関連
		//
		/////////////////////////////////////////////////////////////////////////////

		/** 要素選択を解除します */
		protected function unSelectHandler(event:MouseEvent):void
		{
			unSelectAll();
		}

		/** 要素選択を解除します */
		protected function unSelectAll():void
		{
			elementLayer.unSelect();
			networkLayer.unSelect();
		}

		/** 排他セレクト */
		protected function select(type:String, data:Object):void
		{
			switch(type)
			{
				case VaConstant.ELEMENT_TYPE_VM:
					dispatchEvent(SelectEvent.newSelectVmEvent(data as IVaElement));
					networkLayer.unSelect();
					break;
				case VaConstant.ELEMENT_TYPE_SG:
					dispatchEvent(SelectEvent.newSelectSgEvent(data as IVaElement));
					networkLayer.unSelect();
					break;
				case VaConstant.LINK:
					dispatchEvent(SelectEvent.newSelectLinkEvent(data as VaLink));
					elementLayer.unSelect();
					break;
			}
		}


	}
}