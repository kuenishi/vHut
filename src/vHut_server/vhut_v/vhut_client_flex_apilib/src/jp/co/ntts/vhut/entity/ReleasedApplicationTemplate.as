/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.geom.Point;
	import flash.geom.Rectangle;

	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.VaConstant;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.comp.va.domain.VaSide;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.events.CollectionEvent;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ReleasedApplicationTemplate")]
	/**
	 * ReleasedApplicationTemplate Entity Class.
	 * <p></p>
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
    public class ReleasedApplicationTemplate extends ReleasedApplicationTemplateBase implements IVaElement
	{
		public static const UPDATE_LINK_LIST:String = "updateLinkList";

		public function ReleasedApplicationTemplate()
		{
			super();
			initializeLinkList();
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set releasedApplicationTemplateSecurityGroupMapList(value:ArrayCollection):void {
			super.releasedApplicationTemplateSecurityGroupMapList = value;
			for each (var item:ReleasedApplicationTemplateSecurityGroupMap in value)
			{
				if(item.releasedApplicationTemplateId == id)
				{
					item.releasedApplicationTemplate = this;
				}
			}
		}

		///////////////////////////////////////////////////////////////////////////
		//
		//  IVaElement I/F
		//
		///////////////////////////////////////////////////////////////////////////

		[Transient]
		/**
		 * 種別.
		 */
		public function get type():String
		{
			return VaConstant.ELEMENT_TYPE_VM;
		}

		[Transient]
		/**
		 * グラフィカル表示の際のX座標を取得する.
		 */
		public function get pos():Point
		{
			if(releasedApplication == null) return new Point(0, 0);
			return releasedApplication.topology.getVmPos(privateId);
		}
		public function set pos(value:Point):void
		{
			if(releasedApplication != null)
				releasedApplication.topology.setVmPos(privateId, value);
		}

		[Transient]
		/**
		 * グラフィカル表示の際の幅を取得する.
		 */
		public function get width():Number
		{
			return _width;
		}
		public function set width(value:Number):void
		{
			_width = value;
		}
		protected var _width:Number = 100;

		[Transient]
		/**
		 * グラフィカル表示の際の高さを取得する.
		 */
		public function get height():Number
		{
			return _height;
		}
		public function set height(value:Number):void
		{
			_height = value;
		}
		protected var _height:Number = 100;

		[Transient]
		/**
		 * グラフィカル表示の際の外形を取得する.
		 */
		public function get rect():Rectangle
		{
			return new Rectangle(pos.x, pos.y, width, height);
		}

		[Transient]
		public function get linkList():IList
		{
			return _linkList;
		}
		public function set linkList(list:IList):void
		{
			if(_linkList)
			{
				_linkList.removeEventListener(CollectionEvent.COLLECTION_CHANGE, linkListUpdateHandler);
			}
			_linkList = list;
			if(_linkList)
			{
				_linkList.addEventListener(CollectionEvent.COLLECTION_CHANGE, linkListUpdateHandler);
			}
			updateConnectorList();
		}

		[Transient]
		private var _linkList:IList;

		protected function initializeLinkList():void
		{
			_northLinkList = new ArrayCollection();
			_southLinkList = new ArrayCollection();
			_eastLinkList = new ArrayCollection();
			_westLinkList = new ArrayCollection();

			var sort:Sort = new Sort();
			sort.fields = [new SortField("vmOrder", true)];

			_northLinkList.sort = sort;
			_southLinkList.sort = sort;
			_eastLinkList.sort = sort;
			_westLinkList.sort = sort;
		}

		protected function linkListUpdateHandler(event:CollectionEvent):void
		{
			updateConnectorList();
		}

		protected function updateConnectorList():void
		{
			_northLinkList.removeAll();
			_southLinkList.removeAll();
			_eastLinkList.removeAll();
			_westLinkList.removeAll();
			if (_linkList != null)
			{
				for each (var link:VaLink in _linkList)
				{
					if (link.vmId == privateId)
					{
						switch (link.vmSide)
						{
							case VaSide.NORTH:
								_northLinkList.addItem(link);
								break;
							case VaSide.SOUTH:
								_southLinkList.addItem(link);
								break;
							case VaSide.EAST:
								_eastLinkList.addItem(link);
								break;
							case VaSide.WEST:
								_westLinkList.addItem(link);
								break;
						}
					}
				}
			}
			_northLinkList.refresh();
			_southLinkList.refresh();
			_eastLinkList.refresh();
			_westLinkList.refresh();
			dispatchEvent(new Event(UPDATE_LINK_LIST));
		}

		[Transient]
		[Bindable("updateLinkList")]
		public function get northLinkList():IList
		{
			return _northLinkList;
		}
		private var _northLinkList:ArrayCollection = new ArrayCollection();

		[Transient]
		[Bindable("updateLinkList")]
		public function get southLinkList():IList
		{
			return _southLinkList;
		}
		private var _southLinkList:ArrayCollection = new ArrayCollection();

		[Transient]
		[Bindable("updateLinkList")]
		public function get eastLinkList():IList
		{
			return _eastLinkList;
		}
		private var _eastLinkList:ArrayCollection = new ArrayCollection();

		[Transient]
		[Bindable("updateLinkList")]
		public function get westLinkList():IList
		{
			return _westLinkList;
		}
		private var _westLinkList:ArrayCollection = new ArrayCollection();
    }
}