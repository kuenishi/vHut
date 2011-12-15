/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app.wiz.presentation
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.app.cal.application.TermCalendarEvent;
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent;
	import jp.co.ntts.vhut.entity.Term;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="getAvailableTermListByAppId", type="jp.co.ntts.vhut.core.GetByIdWithTimeSpanEvent")]
	[ManagedEvents(names="getAvailableTermListByAppId")]
	/**
	 * アプリケーション起動期間予約ビューのモデルクラス.
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
	public class TermViewPM extends EventDispatcher implements IValidator
	{
		public static const CHANGE_CURRENT_MONTH:String = "changeCurrentMonth";

		[Inject]
		[Bindable]
		public var applications:Applications;

		[Bindable]
		public var editingTermList:IList;

		[Bindable("changeCurrentMonth")]
		/** 表示する月 */
		public function set currentMonth(value:Date):void
		{
			_currentMonth = new Date(value.fullYear, value.month);
			if(!isPrevMonth)
				updateAvailableTermList();
			dispatchEvent(new Event(CHANGE_CURRENT_MONTH));
		}
		public function get currentMonth():Date
		{
			return _currentMonth;
		}
		private var _currentMonth:Date;

		/**
		 * ウィザードの開始時に実行される初期化メソッド
		 */
		public function onInitializedByWizard():void
		{
			editingTermList = new ArrayCollection();
			currentMonth = new Date();
		}

		public function get isValid():Boolean
		{
			return true;
		}

		public function next():Date
		{
			currentMonth = new Date(currentMonth.time + 32*24*60*60*1000)
			return currentMonth;
		}

		public function prev():Date
		{
			currentMonth = new Date(currentMonth.time - 1*24*60*60*1000)
			return currentMonth;
		}

		[Bindable("changeCurrentMonth")]
		public function get startTime():Date
		{
			if(isThisMonth)
			{
				return today;
			}
			else
			{
				return currentMonth;
			}
		}

		[Bindable("changeCurrentMonth")]
		public function get endTime():Date
		{
			var date0:Date = new Date(currentMonth.time + 32*24*60*60*1000);
			var date1:Date = new Date(date0.fullYear, date0.month);
			return new Date(date1.fullYear, date1.month, date1.date);
		}

		public function updateAvailableTermList():void
		{
			applications.updateTargetApplicationAvailableTermList(startTime, endTime);
		}
		public function reservingTermListChangedHandler(event:TermCalendarEvent):void
		{
			editingTermList = event.data;
		}

		protected function get isPrevMonth():Boolean
		{
			var now:Date = new Date();
			return new Date(_currentMonth.fullYear, _currentMonth.month).getTime() < new Date(now.fullYear, now.month).getTime();
		}

		protected function get isThisMonth():Boolean
		{
			var now:Date = new Date();
			return new Date(_currentMonth.fullYear, _currentMonth.month).getTime() == new Date(now.fullYear, now.month).getTime();
		}

		protected function get today():Date
		{
			var now:Date = new Date();
			return new Date(now.fullYear, now.month, now.date);
		}

	}
}