package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	import jp.co.ntts.vhut.entity.Application;

	/**
	 * 編集中のオブジェクトを更新した際に発生するイベント.
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
	 * $Date: 2010-10-01 14:49:29 +0900 (金, 01 10 2010) $
	 * $Revision: 504 $
	 * $Author: NTT Software Corporation. $
	 */
	public class UpdateTargetItemEvent extends Event
	{
		/** 編集中のオブジェクトを更新した */
		public static const UPDATE_TARGET_ITEM:String = "updateTargetItem";

		/**
		 * イベントを作成する。
		 * @param item 選択された要素
		 * @return イベント.
		 */
		public static function newUpdateTargetItemEvent(item:Object):UpdateTargetItemEvent
		{
			var event:UpdateTargetItemEvent = new UpdateTargetItemEvent(UPDATE_TARGET_ITEM);
			event._targetItem = item;
			return event;
		}

		/** デフォルトコンストラクタ. */
		public function UpdateTargetItemEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		protected var _targetItem:Object;

		/** ターゲットのオブジェクト */
		public function get targetItem():Object
		{
			return _targetItem;
		}

		override public function clone():Event
		{
			var event:UpdateTargetItemEvent = new UpdateTargetItemEvent(type, bubbles, cancelable);
			event._targetItem = _targetItem;
			return event;
		}
	}
}