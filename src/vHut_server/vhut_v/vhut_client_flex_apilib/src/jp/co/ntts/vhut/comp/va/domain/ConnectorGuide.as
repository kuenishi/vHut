/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	/**
	 * 接続ガイドの操作に関するドメインです.
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
	public class ConnectorGuide extends EventDispatcher
	{
		public static const CHANGE_DATA:String = "changeData";

		/** 初期状態 */
		public static const NONE:String = "none";
		/** ドラック状態（ドロップ先未確定） */
		public static const NOT_ACCEPTED:String = "notAccepted";
		/** ドラッグ状態（ドロップ先確定）*/
		public static const ACCEPTED:String = "accepted";

		public function ConnectorGuide(target:IEventDispatcher=null)
		{
			super(target);
		}

		[Bindable("changeData")]
		/** 操作対象のリンク */
		public function set linkDragData(value:VaLinkDragData):void
		{
			_linkDragData = value;
			updateStatus();
			dispatchEvent(new Event(CHANGE_DATA));
		}
		public function get linkDragData():VaLinkDragData
		{
			return _linkDragData;
		}
		private var _linkDragData:VaLinkDragData;

//		/** 開始ノードのタイプ */
//		public var sElementType:String;
//
//		/** 開始ノードの描画用のID */
//		public var sElementId:Number;
//
//		/** 開始ノードのタイプ */
//		public var sElementSide:VaSide;
//
//		/** 開始ノードの順位 */
//		public var sElementOrder:Number;
//
//		/** 終了ノードのタイプ */
//		public var eElementType:String;
//
//		/** 終了ノードの描画用のID */
//		public var eElementId:Number;
//
//		/** 終了ノードのタイプ */
//		public var eElementSide:VaSide;
//
//		/** 終了ノードの順位 */
//		public var eElementOrder:Number;

		[Bindable("changeData")]
		/** 接続状態 */
		public function get status():String
		{
			return _status;
		}
		private var _status:String;

		protected function updateStatus():void
		{
			if (linkDragData == null)
			{
				_status = NONE;
			}
			else
			{
				var link:VaLink = linkDragData.newLink;
				if (!link) {
					_status = NONE;
				}
				else if (isNaN(link.vmId) && isNaN(link.sgId))
				{
					_status = NONE;
				}
				else if (!isNaN(link.vmId) && !isNaN(link.sgId))
				{
					_status = ACCEPTED;
				}
				else
				{
					_status = NOT_ACCEPTED;
				}
			}
		}
	}
}