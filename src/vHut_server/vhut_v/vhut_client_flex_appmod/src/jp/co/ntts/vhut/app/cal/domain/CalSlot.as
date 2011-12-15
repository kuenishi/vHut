/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.cal.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	/**
	 * カレンダーのスロットを表すデータ
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
	public class CalSlot extends EventDispatcher
	{
		//予約申請可能
		public static const STATUS_AVAILABLE:String = "available";
		//予約申請不可能
		public static const STATUS_NOT_AVAILABLE:String = "notAvailable";
		//予約申請予定
		public static const STATUS_RESERVING:String = "reserving";
		//日付なし
		public static const STATUS_NONE:String = "none";

		public function CalSlot(target:IEventDispatcher=null)
		{
			super(target);
		}

		[Bindable]
		/** 状態 */
		public function set date(value:Date):void
		{
			_date = value;
			dispatchEvent(new Event("updateDate"));
		}
		public function get date():Date
		{
			return _date;
		}
		private var _date:Date = null;

		[Bindable("updateDate")]
		/** 表示用日付 */
		public function get displayDate():String
		{
			if(date == null)
				return "";
			return date.date.toString();
		}

		[Bindable]
		/** 表示日付 */
		public function set status(value:String):void
		{
			_status = value;
		}
		public function get status():String
		{
			return _status;
		}
		private var _status:String = STATUS_NONE;

		[Bindable]
		public var hovered:Boolean = false;

	}
}