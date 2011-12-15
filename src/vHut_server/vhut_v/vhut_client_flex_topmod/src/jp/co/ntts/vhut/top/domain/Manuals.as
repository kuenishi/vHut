/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.top.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;

	import jp.co.ntts.vhut.config.VhutConfig;

	import mx.collections.IList;
	import mx.collections.XMLListCollection;

	/**
	 * 使い方情報を管理するクラス.
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
	public class Manuals extends EventDispatcher
	{
		public function Manuals(target:IEventDispatcher=null)
		{
			super(target);
		}

		[Bindable("manualListChanged")]
		public function get manualList():IList
		{
			return _manualList;
		}
		protected var _manualList:XMLListCollection;

		public function updateManualList():void
		{
			if (!VhutConfig.MANUAL_URL)
				return;

			var urlRequest:URLRequest = new URLRequest(VhutConfig.MANUAL_URL);
			var urlLoader:URLLoader = new URLLoader();
			urlLoader.dataFormat = URLLoaderDataFormat.TEXT;
			urlLoader.addEventListener(Event.COMPLETE, updateManualListLoadComplateHandler);
			urlLoader.load(urlRequest);
		}

		protected function updateManualListLoadComplateHandler(event:Event):void
		{
			var xml:XML = new XML(event.target.data);
			if(xml)
			{
				_manualList = new XMLListCollection(new XMLList(xml.category));
				dispatchEvent(new Event("manualListChanged"));
			}
		}
	}
}