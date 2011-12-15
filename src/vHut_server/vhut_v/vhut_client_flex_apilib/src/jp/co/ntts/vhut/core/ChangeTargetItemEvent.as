package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	import jp.co.ntts.vhut.entity.Application;

	/**
	 * 編集中のオブジェクトを変更した際に発生するイベント.
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
	public class ChangeTargetItemEvent extends Event
	{
		/** 編集中のオブジェクトを変更した */
		public static const CHANGE_TARGET_ITEM:String = "changeTargetItem";

		/**
		 * イベントを作成する。
		 * @param item 選択された要素
		 * @return イベント.
		 */
		public static function newUpdateTargetItemEvent(item:Object):ChangeTargetItemEvent
		{
			var event:ChangeTargetItemEvent = new ChangeTargetItemEvent(CHANGE_TARGET_ITEM);
			event._targetItem = item;
			return event;
		}

		/** デフォルトコンストラクタ. */
		public function ChangeTargetItemEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
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
			var event:ChangeTargetItemEvent = new ChangeTargetItemEvent(type, bubbles, cancelable);
			event._targetItem = _targetItem;
			return event;
		}
	}
}