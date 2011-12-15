/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.infrastructure
{
	import flash.events.Event;

	/**
	 * SecurityGroupの描画クラスのイベント
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
	 * $Date: 2010-10-26 19:15:48 +0900 (火, 26 10 2010) $
	 * $Revision: 540 $
	 * $Author: NTT Software Corporation. $
	 */
	public class SgItemRendererEvent extends Event
	{
		/** SecurityGroupを削除します */
		public static const REMOVE_SG:String = "removeSg";

		public static function newRemoveSgSgitemRendererEvent(data:Object):SgItemRendererEvent
		{
			var event:SgItemRendererEvent = new SgItemRendererEvent(REMOVE_SG);
			event.data = data;
			return event;
		}

		public function SgItemRendererEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		public var data:Object;

		override public function clone():Event
		{
			var result:SgItemRendererEvent = new SgItemRendererEvent(type, bubbles, cancelable);
			result.data = data
			return result;
		}
	}
}