/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.comp.va.domain.IVaElement;
	import jp.co.ntts.vhut.comp.va.domain.VaConstant;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;
	import jp.co.ntts.vhut.comp.va.domain.VaSide;
	import jp.co.ntts.vhut.util.CloneUtil;
	import jp.co.ntts.vhut.util.IClonable;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.events.CollectionEvent;
	import mx.utils.ObjectUtil;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ApplicationSecurityGroup")]
	/**
	 * ApplicationSecurityGroup Entity Class.
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
    public class ApplicationSecurityGroup extends ApplicationSecurityGroupBase implements IClonable, IVaElement
	{
		public static const UPDATE_LINK_LIST:String = "updateLinkList";

		public function ApplicationSecurityGroup()
		{
			name = "New Network"
			applicationVmSecurityGroupMapList = new ArrayCollection();
			initializeLinkList();
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set applicationVmSecurityGroupMapList(value:ArrayCollection):void {
			super.applicationVmSecurityGroupMapList = value;
			for each (var item:ApplicationVmSecurityGroupMap in value)
			{
				if(item.applicationSecurityGroupId == id)
				{
					item.applicationSecurityGroup = this;
				}
			}
		}

		/**
		 * エンティティを複製します.
		 * @param withId IDも含めて複製します.
		 * @param deep 関連要素を含めて複製します.
		 * @param ref 置換されたリファレンス
		 * @return 複製されたエンティティ.
		 */
		public function clone(withId:Boolean = true, deep:Boolean = true, ref:Dictionary = null):*
		{
			if(ref == null)
			{
				ref = new Dictionary();
			}
			else
			{
				var result:ApplicationSecurityGroup = ref[this] as ApplicationSecurityGroup;
				if(result != null)
				{
					return result;
				}
			}
			result = new ApplicationSecurityGroup();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.entity.ApplicationSecurityGroupBase");
			ref[this] = result;

			//IDの初期化
			if(!withId)
			{
				result.id = 0;
			}
			if(deep)
			{
				if(application)
					result.application = application.clone(withId, deep, ref);
				if(securityGroup)
					result.securityGroup = securityGroup.clone(withId, deep, ref);
				if(applicationVmSecurityGroupMapList)
					result.applicationVmSecurityGroupMapList = CloneUtil.cloneCollection(applicationVmSecurityGroupMapList, withId, deep, ref);
			}
			return result;
		}

		/**
		 * 削除に先だって関連をクリアします.
		 */
		public function clean():void
		{
			//networkAdapterの削除
			securityGroup.clean();
			//ApplicationVmSecurityGroupMapの削除
			removeAllApplicationVmSecurityGroupMaps();
		}

		/**
		 * すべてのApplicationVmSecurityMapを削除します.
		 * @return 削除したApplicationVmSecurityMapのリスト
		 */
		public function removeAllApplicationVmSecurityGroupMaps():Array
		{
			var result:Array = new Array();

			for each(var map:ApplicationVmSecurityGroupMap in applicationVmSecurityGroupMapList)
			{
				var resultElement:ApplicationVmSecurityGroupMap = removeApplicationVmSecurityGroupMap(map);
				if(resultElement != null) result.push(resultElement);
			}

			return result;
		}

		/**
		 * ApplicationVmSecurityMapを削除します.
		 * @param map ApplicationVmSecurityGroupMap
		 * @return 削除したApplicationVmSecurityMap
		 */
		public function removeApplicationVmSecurityGroupMap(map:ApplicationVmSecurityGroupMap):ApplicationVmSecurityGroupMap
		{
			var indexInAsg:int = applicationVmSecurityGroupMapList.getItemIndex(map);
			if(indexInAsg == -1) return null;

			var indexInAvm:int = map.applicationVm.applicationVmSecurityGroupMapList.getItemIndex(map);
			if(indexInAvm != -1) map.applicationVm.applicationVmSecurityGroupMapList.removeItemAt(indexInAvm);

			applicationVmSecurityGroupMapList.removeItemAt(indexInAsg);

			return map;
		}

		/**
		 * ApplicationVmと接続を持つApplicationVmSecurityGroupMapを取得します.
		 * @param avm ApplicationVm
		 * @return ApplicationVmSecurityGroupMap
		 */
		public function getApplicationVmSecurityGroupMapWithConnectionTo(avm:ApplicationVm):ApplicationVmSecurityGroupMap
		{
			for each(var map:ApplicationVmSecurityGroupMap in applicationVmSecurityGroupMapList)
			{
				if(map.applicationVm == avm) return map;
			}
			return null;
		}

		/**
		 * ApplicationVmSecurityGroupMapを追加します.
		 * @param map 追加されたApplicationVmSecurityGroupMap
		 * @return 追加された場所
		 */
		public function addApplicationVmSecurityGroupMap(map:ApplicationVmSecurityGroupMap):int
		{
			if(map == null) return -1;
			var index:int = applicationVmSecurityGroupMapList.getItemIndex(map);
			if(index >= 0) return index;
			applicationVmSecurityGroupMapList.addItem(map);
			return applicationVmSecurityGroupMapList.length - 1;
		}

		/**
		 * ApplicationVmとの接続を切断します.
		 * @param avm 切断するApplicationVm
		 * @return 削除されたMap
		 */
		public function disconnectTo(avm:ApplicationVm):ApplicationVmSecurityGroupMap
		{
			var map:ApplicationVmSecurityGroupMap = getApplicationVmSecurityGroupMapWithConnectionTo(avm);
			var index:int = applicationVmSecurityGroupMapList.getItemIndex(map);
			return applicationVmSecurityGroupMapList.removeItemAt(index) as ApplicationVmSecurityGroupMap;
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
			return VaConstant.ELEMENT_TYPE_SG;
		}

		[Transient]
		/**
		 * グラフィカル表示の際のX座標を取得する.
		 */
		public function get pos():Point
		{
			if(application == null) return new Point(0, 0);
			return application.topology.getSgPos(privateId);
		}
		public function set pos(value:Point):void
		{
			if(application != null)
				application.topology.setSgPos(privateId, value);
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
		protected var _width:Number = 120;

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
		protected var _height:Number = 60;

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

		protected function linkListUpdateHandler(event:CollectionEvent):void
		{
			updateConnectorList();
		}

		protected function initializeLinkList():void
		{
			_northLinkList = new ArrayCollection();
			_southLinkList = new ArrayCollection();
			_eastLinkList = new ArrayCollection();
			_westLinkList = new ArrayCollection();

			var sort:Sort = new Sort();
			sort.fields = [new SortField("sgOrder", true)];

			_northLinkList.sort = sort;
			_southLinkList.sort = sort;
			_eastLinkList.sort = sort;
			_westLinkList.sort = sort;
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
					if (link.sgId == privateId)
					{
						switch (link.sgSide)
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