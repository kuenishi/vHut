/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import jp.co.ntts.vhut.comp.va.domain.Topology;
	import jp.co.ntts.vhut.util.CollectionUtil;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ReleasedApplication")]
	/**
	 * ReleasedApplication Entity Class.
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
    public class ReleasedApplication extends ReleasedApplicationBase {
		public static const CHANGE_TOPOLOGY:String = "changeTopology"
		
		public function ReleasedApplication()
		{
			_releasedApplicationTemplateList = new ArrayCollection();
			_releasedApplicationSecurityGroupTemplateList = new ArrayCollection();
			_elements = CollectionUtil.fusion(_releasedApplicationTemplateList, _releasedApplicationSecurityGroupTemplateList);
			_topology = new Topology(_elements);
		}
		
		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}
		
		override public function set releasedApplicationSecurityGroupTemplateList(value:ArrayCollection):void {
			_releasedApplicationSecurityGroupTemplateList.removeAll();
			_releasedApplicationSecurityGroupTemplateList.addAll(value);
		}
		
		override public function set releasedApplicationTemplateList(value:ArrayCollection):void {
			_releasedApplicationTemplateList.removeAll();
			_releasedApplicationTemplateList.addAll(value);
		}
		
		///////////////////////////////////////////////////////////////////////////
		//  Graphic Parameters, do not upload
		///////////////////////////////////////////////////////////////////////////
		
		[Transient]
		/** 描画要素 */
		public function get elements():IList
		{
			if(_elements == null)
			{
				updateElements();
			}
			return _elements;
		}
		private var _elements:ArrayCollection;
		
		/** 描画要素を更新します. */
		private function updateElements():void
		{
			_elements = CollectionUtil.fusion(releasedApplicationTemplateList, releasedApplicationSecurityGroupTemplateList);
		}
		
		override public function set structure(value:String):void {
			_structure = value;
			_topology.structure = _structure;
			dispatchEvent(new Event(CHANGE_TOPOLOGY));
		}
		override public function get structure():String {
			if(_topology == null)
				return _structure;
			return _topology.toString();
		}
		
		[Transient]
		[Bindable("changeTopology")]
		public function get topology():Topology
		{
			return _topology;
		}
		private var _topology:Topology;
    }
}