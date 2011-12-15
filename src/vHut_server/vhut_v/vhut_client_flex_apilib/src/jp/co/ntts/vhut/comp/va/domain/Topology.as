/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
	import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;

	import org.spicefactory.lib.reflect.ClassInfo;
	import org.spicefactory.lib.reflect.Property;


	/**
	 * VM及びNWの描画要素のデータ
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
	public class Topology extends EventDispatcher
	{
		public static const CHANGE_LINKLIST:String = "changeLinkList";

		public static const VM_DEFAULT_WIDTH:uint = 100;
		public static const VM_DEFAULT_HEIGHT:uint = 100;
		public static const SG_DEFAULT_WIDTH:uint = 120;
		public static const SG_DEFAULT_HEIGHT:uint = 60;

		public static const PRIVATE_ID_MAX:uint = 10000;

		public static function get vaLinkInfo():ClassInfo
		{
			if(_vaLinkInfo == null)
			{
				_vaLinkInfo = ClassInfo.forName("jp.co.ntts.vhut.comp.va.domain.VaLink");
			}
			return _vaLinkInfo;
		}
		private static var _vaLinkInfo:ClassInfo;

		/**
		 * コンストラクタ
		 * @param structure XMLの元データ
		 */
		public function Topology(elementList:IList, structure:String = null)
		{
			_elementList = elementList;
			_elementList.addEventListener(CollectionEvent.COLLECTION_CHANGE, elementListChangeHandler);

			_linkList = new ArrayCollection();
			for each(var element:IVaElement in _elementList)
			{
				element.linkList = _linkList;
			}
			updateLinkList();

			this.structure = structure;
		}

		public function get elementList():IList
		{
			return _elementList;
		}
		private var _elementList:IList;

		/** XMLの元データ */
		public function set structure(value:String):void
		{
			if(value == null || value == "")
			{
				_xml = defaultXML;
			}
			else
			{
				try
				{
					XML.ignoreComments = true;
					XML.ignoreProcessingInstructions = true;
					XML.ignoreWhitespace = true;
					_xml = new XML(value);
				}
				catch(e:Error)
				{
					_xml = defaultXML;
				}
			}
			updateLinkList();
		}
		public function get structure():String
		{
			return _xml.toString();
		}

		/**
		 * XMLデータ
		 */
		private var _xml:XML;

		/**
		 * デフォルトのXMLデータ
		 */
		private function get defaultXML():XML
		{
			var xml:XML =
				<root>
					<vms/>
					<sgs/>
					<links/>
				</root>;
//			var xml:XML =
//			<root>
//				<vms>
//					<vm id="1" posx="100" posy="100">
//						<SOUTH>
//							<link id="1"/>
//						</SOUTH>
//					</vm>
//					<vm id="2" posx="300" posy="100">
//						<SOUTH>
//							<link id="2"/>
//						</SOUTH>
//					</vm>
//				</vms>
//				<sgs>
//					<sg id="1" posx="200" posy="300">
//						<NORTH>
//							<link id="1"/>
//							<link id="2"/>
//						</NORTH>
//					</sg>
//				</sgs>
//				<links>
//					<link id="1"/>
//					<link id="2"/>
//				</links>
//			</root>;
			return xml;
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  要素の個別IDを管理します.
		//
		//////////////////////////////////////////////////////////////////////////////

		private function getUnusedVmPrivateId():Number
		{
			var map:Array = new Array();
			for each(var vmNode:XML in _xml.vms.vm)
			{
				map[Number(vmNode.@id)] = true;
			}

			for(var i:int=1; i<PRIVATE_ID_MAX; i++)
			{
				if(!map[i])
				{
					return i;
				}
			}
			throw new Error("vm id is exhausted.");
		}

		private function getUnusedSgPrivateId():Number
		{
			var map:Array = new Array();
			for each(var sgNode:XML in _xml.sgs.sg)
			{
				map[Number(sgNode.@id)] = true;
			}

			for(var i:int=1; i<PRIVATE_ID_MAX; i++)
			{
				if(!map[i])
				{
					return i;
				}
			}
			throw new Error("sg id is exhausted.");
		}

		private function getUnusedLinkPrivateId():Number
		{
			var map:Array = new Array();
			for each(var linkNode:XML in _xml.links.link)
			{
				map[Number(linkNode.@id)] = true;
			}

			for(var i:int=1; i<PRIVATE_ID_MAX; i++)
			{
				if(!map[i])
				{
					return i;
				}
			}
			throw new Error("link id is exhausted.");
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  要素を管理します.
		//
		//////////////////////////////////////////////////////////////////////////////

		public function addVm():Number
		{
			var privateId:Number = getUnusedVmPrivateId();
			var vm:XML = <vm id={privateId}/>;
			_xml.vms.appendChild(vm);
			return privateId;
		}

		public function removeVm(id:Number):void
		{
			for each(var linkNode:XML in _xml.vms.vm.(@id == id)..link)
			{
				delete _xml.sgs.sg..link.(@id == linkNode.@id)[0];
				delete _xml.links.link.(@id == linkNode.@id)[0];
			}
			delete _xml.vms.vm.(@id == id)[0];
			updateLinkList();
		}

		public function addSg():Number
		{
			var privateId:Number = getUnusedSgPrivateId();
			var sg:XML = <sg id={privateId}/>;
			_xml.sgs.appendChild(sg);
			return privateId;
		}

		public function removeSg(id:Number):void
		{
			for each(var linkNode:XML in _xml.sgs.sg.(@id == id)..link)
			{
				delete _xml.vms.vm..link.(@id == linkNode.@id)[0];
				delete _xml.links.link.(@id == linkNode.@id)[0];
			}
			delete _xml.sgs.sg.(@id == id)[0];
			updateLinkList();
		}

		protected function elementListChangeHandler(event:CollectionEvent):void
		{
			var item:IVaElement;

			switch (event.kind)
			{
				case CollectionEventKind.ADD:
					for each(item in event.items)
					{
						item.linkList = linkList;
					}
					break;

				case CollectionEventKind.REMOVE:
					for each(item in event.items)
					{
						item.linkList = null;
					}
					break;
			}
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  要素の位置情報を管理します.
		//
		////////////////////////////////////////////////////////////////////////////

		public function getElementPos(index:Number):Point
		{
			var element:Object = _elementList.getItemAt(index);
			if(element && !isNaN(element.privateId))
			{
				if(element is ApplicationVm || element is ApplicationInstanceVm || element is ReleasedApplicationTemplate)
					return getVmPos(element.privateId);
				if(element is ApplicationSecurityGroup || element is ApplicationInstanceSecurityGroup || element is ReleasedApplicationSecurityGroupTemplate)
					return getSgPos(element.privateId);
			}
			return new Point(0, 0);
		}

		/**
		 * VMのX座標を取得する。
		 * @param id VMのID
		 * @return  VMのX座標
		 */
		public function getVmPos(id:Number):Point
		{
			var vm:XML = _xml.vms.vm.(@id == id)[0];
			if(vm)
			{
				return new Point(Number(vm.@posx),Number(vm.@posy));
			}
			else
			{
				return new Point(0, 0);
			}
		}

		/**
		 * VMのX座標を指定する
		 * @param id VMのID
		 * @param pos VMのX座標
		 */
		public function setVmPos(id:Number, pos:Point):void
		{
			var vm:XML = _xml.vms.vm.(@id == id)[0];
			if(vm)
			{
				var oldPos:Point = getVmPos(id);
				vm.@posx = pos.x.toString();
				vm.@posy = pos.y.toString();
				var newPos:Point = getVmPos(id);

				for each (var element:IVaElement in elementList)
				{
					if(element.type == VaConstant.ELEMENT_TYPE_VM && element.privateId == id)
					{
						elementList.itemUpdated(element, "pos", oldPos, newPos);
					}
				}
				updateLinkList();
			}

		}

		/**
		 * VMの外形を取得する.
		 * @param id VmのID
		 * @return 外形
		 */
		public function getVmRect(id:Number):Rectangle
		{
			var result:Rectangle = new Rectangle();
			var pos:Point = getVmPos(id);
			if(pos)
			{
				result.x = pos.x;
				result.y = pos.y;
			}
			result.width = VM_DEFAULT_WIDTH;
			result.height = VM_DEFAULT_HEIGHT;

			return result;
		}

		/**
		 * SecurityGroupのX座標を取得する。
		 * @param id SecurityGroupのID
		 * @return  SecurityGroupのX座標
		 */
		public function getSgPos(id:Number):Point
		{
			var sg:XML = _xml.sgs.sg.(@id == id)[0];
			if(sg)
			{
				return new Point(Number(sg.@posx),Number(sg.@posy));
			}
			else
			{
				return new Point(0, 0);
			}
		}

		/**
		 * SecurityGroupのX座標を指定する
		 * @param id SecurityGroupのID
		 * @param pos SecurityGroupの座標
		 */
		public function setSgPos(id:Number, pos:Point):void
		{
			var sg:XML = _xml.sgs.sg.(@id == id)[0];
			if(sg)
			{
				var oldPos:Point = getSgPos(id);
				sg.@posx = pos.x.toString();
				sg.@posy = pos.y.toString();
				var newPos:Point = getSgPos(id);

				for each (var element:IVaElement in elementList)
				{
					if(element.type == VaConstant.ELEMENT_TYPE_SG && element.privateId == id)
					{
						elementList.itemUpdated(element, "pos", oldPos, newPos);
					}
				}
				updateLinkList();
			}

		}

		/**
		 * SecurityGroupの外形を取得する.
		 * @param id SecurityGroupのID
		 * @return 外形
		 */
		public function getSgRect(id:Number):Rectangle
		{
			var result:Rectangle = new Rectangle();
			var pos:Point = getSgPos(id);
			if(pos)
			{
				result.x = pos.x;
				result.y = pos.y;
			}
			result.width = SG_DEFAULT_WIDTH;
			result.height = SG_DEFAULT_HEIGHT;

			return result;
		}

		/**
		 * XMLストリングを返します.
		 * @return XMLストリング
		 */
		override public function toString():String
		{
			return _xml.toString();
		}

		/////////////////////////////////////////////////////////////////////////////
		//
		//  要素間の接続状態を管理します.
		//
		////////////////////////////////////////////////////////////////////////////

		[Bindable("changeLinkList")]
		/** ネットワーク構成を返します. */
		public function get linkList():IList
		{
			return _linkList;
		}
		private var _linkList:ArrayCollection;

		public function getLinkById(id:Number):VaLink
		{
			for each (var link:VaLink in linkList)
			{
				if(link.id == id)
					return link;
			}
			return null;
		}

		/**
		 * ネットワーク構成を更新します.
		 */
		private function updateLinkList():void
		{
			if(_xml == null) return;

			var link:VaLink;
			var i:Number;
			var length:Number;
			var updatedItems:Array = new Array();

			//create map
			var map:Dictionary = new Dictionary();
			for each(link in linkList)
			{
				map[link.id] = link
			}

			for each(var linkNode:XML in _xml.links.link)
			{

				link = new VaLink();
				link.id = Number(linkNode.@id);

				var vmLinkNode:XML = _xml.vms.vm..link.(@id==link.id)[0];
				var vmNode:XML = vmLinkNode.parent().parent() as XML;
				var vmId:Number = Number(vmNode.@id);
				link.vmId = vmId;
				link.vmRect = getVmRect(vmId); //vmRect
				var vmSideNode:XML = vmLinkNode.parent() as XML;
				link.vmSide = VaSide.valueOf(vmSideNode.name()); //vmSide
				length = vmSideNode.link.length();
				for (i=0; i<length; i++)
				{
					if(Number(vmSideNode.link[i].@id) == link.id)
					{
						link.vmOrder = (i+1)/(length+1);
						break;
					}
				}

				var sgLinkNode:XML = _xml.sgs.sg..link.(@id==link.id)[0];
				var sgNode:XML = sgLinkNode.parent().parent() as XML;
				var sgId:Number = Number(sgNode.@id);
				link.sgId = sgId;
				link.sgRect = getSgRect(sgId); //sgRect
				var sgSideNode:XML = sgLinkNode.parent() as XML;
				link.sgSide = VaSide.valueOf(sgSideNode.name()); //sgSide
				length = sgSideNode.link.length();
				for (i=0; i<length; i++)
				{
					if(Number(sgSideNode.link[i].@id) == link.id)
					{
						link.sgOrder = Number(i+1)/Number(length+1);
						break;
					}
				}


				var oldLink:VaLink = map[Number(linkNode.@id)] as VaLink;
				if(oldLink != null)
				{
					//update map
					map[Number(linkNode.@id)] = null;
					//update

					for each(var property:Property in vaLinkInfo.getProperties())
					{
						if(property.readable && property.writable)
						{
							var oldValue:Object = property.getValue(oldLink);
							var newValue:Object = property.getValue(link);
							if(oldValue != newValue)
							{
//								trace("id="+oldLink.id+", property="+property.name+", oldValue="+oldValue+", newValue="+newValue)
								property.setValue(oldLink, property.getValue(link));
								linkList.itemUpdated(oldLink, property.name, oldValue, newValue);
							}
						}
					}
				}
				else
				{
					//insert
					linkList.addItem(link);
				}
			}

			for each(link in map)
			{
				if(link)
				{
					//delete
					linkList.removeItemAt(linkList.getItemIndex(link));
				}
			}

//			if(updatedItems.length > 0)
//			{
//				linkList.dispatchEvent(createUpdateCollectionEvent(updatedItems));
//			}
			dispatchEvent(new Event(CHANGE_LINKLIST));
		}

//		protected function updateLink(oldLink:VaLink, newLink:VaLink):Boolean
//		{
//			var changed:Boolean = false;
//			for each(var property:Property in vaLinkInfo.getProperties())
//			{
//				if(property.readable && property.writable)
//				{
//					var oldValue:Object = property.getValue(oldLink);
//					var newValue:Object = property.getValue(newLink);
//					if(oldValue != newValue)
//					{
//						property.setValue(oldLink, property.getValue(newLink));
//						changed = true;
//					}
//				}
//			}
//			return changed;
//		}

		protected function createUpdateCollectionEvent(items:Array):CollectionEvent
		{
			var event:CollectionEvent = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
			event.kind = CollectionEventKind.UPDATE;
			event.items = items;
			return event;
		}

		/**
		 * 接続情報を付加します.
		 * @param link 接続の詳細情報
		 * @return 接続のプライベートID（失敗した場合は-1）
		 */
		public function addLink(link:VaLink):Number
		{
			var privateId:Number = createLink();
			if (!addLinkToVm(privateId, link.vmId, link.vmSide, link.vmOrder)
				|| !addLinkToSg(privateId, link.sgId, link.sgSide, link.sgOrder))
			{
				rollbackConnect(privateId);
				return -1;
			}
			updateLinkList();
			return privateId;
		}

		protected function createLink():Number
		{
			var privateId:Number = getUnusedLinkPrivateId();
			var linkNode:XML = <link id={privateId}/>;
			_xml.links.appendChild(linkNode);
			return privateId;
		}

		protected function addLinkToVm(linkPid:Number, vmPid:Number, side:VaSide, order:Number=0):Boolean
		{
			var vmNode:XML = _xml.vms.vm.(@id == vmPid)[0];
			if(vmNode)
			{
				var sideNode:XML = getSideNode(vmNode, side);
				addLinkToSideAt(sideNode, linkPid, order);
				return true;
			} else {
				return false;
			}
		}

		protected function addLinkToSg(linkPid:Number, sgPid:Number, side:VaSide, order:Number=0):Boolean
		{
			var sgNode:XML = _xml.sgs.sg.(@id == sgPid)[0];
			if(sgNode)
			{
				var sideNode:XML = getSideNode(sgNode, side);
				if(sideNode == null) return false;
				addLinkToSideAt(sideNode, linkPid, order);
				return true;
			} else {
				return false;
			}
		}

		protected function getSideNode (node:XML, side:VaSide):XML
		{
			var label:String = side.toString();
			switch (node.child(label).length())
			{
				case 0:
					node.appendChild(<{label}/>);
					return node.child(label)[0];
				case 1:
					return node.child(label)[0];
				default:
					return null;
			}
		}

		protected function addLinkToSideAt(sideNode:XML, linkPid:Number, order:Number=0):void
		{
			var linkNode:XML = <link id={linkPid}/>;
			var sideNodeList:XMLList = sideNode.child("link");
			var length:int = sideNodeList.length();
			var isLinkNodeAdded:Boolean = false;

			var resultSideNodeList:XMLList = new XMLList(<{sideNode.name()}/>);
			if(length == 0)
			{
				resultSideNodeList.appendChild(linkNode);
			}
			else
			{
				for (var i:uint=0; i< length; i++)
				{
					var targetOrder:Number = Number(i+1)/Number(length+1);
					if(!isLinkNodeAdded && targetOrder > order)
					{
						resultSideNodeList.appendChild(linkNode);
						isLinkNodeAdded = true;
					}
					resultSideNodeList.appendChild(sideNodeList[i]);
				}
				if(!isLinkNodeAdded)
				{
					resultSideNodeList.appendChild(linkNode);
				}

			}
			sideNode.setChildren(resultSideNodeList.children());
		}

		protected function rollbackConnect(pid:Number):void
		{
			removeLinkById(pid);
		}

		public function removeLink(link:VaLink):void
		{
			removeLinkById(link.id);
			updateLinkList();
		}

		protected function removeLinkById(pid:Number):void
		{
			var i:uint;
			for (i=0; i<_xml.links.link.(@id == pid).length(); i++)
			{
				delete _xml.links.link.(@id == pid)[i];
			}
			removeLinkFromVm(pid);
			removeLinkFromSg(pid);
		}

		protected function removeLinkFromVm(pid:Number):void
		{
			for (var i:uint=0; i<_xml.vms.vm..link.(@id == pid).length(); i++)
			{
				delete _xml.vms.vm..link.(@id == pid)[i];
			}
		}

		protected function removeLinkFromSg(pid:Number):void
		{
			for (var i:uint=0; i<_xml.sgs.sg..link.(@id == pid).length(); i++)
			{
				delete _xml.sgs.sg..link.(@id == pid)[i];
			}
		}

		public function updateLink(link:VaLink):void
		{
			removeLinkFromVm(link.id);
			addLinkToVm(link.id, link.vmId, link.vmSide, link.vmOrder);
			removeLinkFromSg(link.id);
			addLinkToSg(link.id, link.sgId, link.sgSide, link.sgOrder);
			updateLinkList();
		}
	}
}