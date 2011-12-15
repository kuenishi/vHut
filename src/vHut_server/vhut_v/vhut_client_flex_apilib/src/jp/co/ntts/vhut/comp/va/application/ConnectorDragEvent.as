/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.comp.va.domain.VaLinkDragData;

	/**
	 * 接続コネクターをドラッグ中に発生するイベント
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
	public class ConnectorDragEvent extends Event
	{
		/** 開始 */
		public static const BEGIN_DRAG:String = "beginDrag";
		/** 状態変更 */
		public static const CHANGE_DRAG:String = "changeDrag";
		/** 終了 */
		public static const END_DRAG:String = "endDrag";

		/** 開始イベントを作成 */
		public static function newBeginConnectorDragEvent(data:VaLinkDragData):ConnectorDragEvent
		{
			var event:ConnectorDragEvent = new ConnectorDragEvent(BEGIN_DRAG, true);
			event.data = data;
			return event;
		}

		/** 状態変更イベントを作成 */
		public static function newChangeConnectorDragEvent(data:VaLinkDragData):ConnectorDragEvent
		{
			var event:ConnectorDragEvent = new ConnectorDragEvent(CHANGE_DRAG, true);
			event.data = data;
			return event;
		}

		/** 終了イベントを作成 */
		public static function newEndConnectorDragEvent(data:VaLinkDragData):ConnectorDragEvent
		{
			var event:ConnectorDragEvent = new ConnectorDragEvent(END_DRAG, true);
			event.data = data;
			return event;
		}

		public function ConnectorDragEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		/** 接続状態 */
		public var data:VaLinkDragData;

		override public function clone():Event
		{
			var event:ConnectorDragEvent = new ConnectorDragEvent(type, bubbles, cancelable);
			event.data = data;
			return event;
		}
	}
}