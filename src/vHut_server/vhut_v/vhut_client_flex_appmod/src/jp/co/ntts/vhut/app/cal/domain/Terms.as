/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.cal.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;

	import flashx.textLayout.events.DamageEvent;

	import jp.co.ntts.vhut.entity.Term;
	import jp.co.ntts.vhut.util.DateUtil;
	import jp.co.ntts.vhut.util.TermUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	/**
	 * 起動予約カレンダーのドメインクラス.
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
	public class Terms extends EventDispatcher
	{
		public function Terms(target:IEventDispatcher=null)
		{
			super(target);
		}

		//////////////////////////////////////////////////////
		//
		//  public interfaces
		//
		//////////////////////////////////////////////////////

		public function reverseCalSlot(start:uint, end:uint):void
		{
			reverseAvailableAndReserving(start, end);
			invalidateCalSlotList();
			invalidateReservingTermList();
		}

		//////////////////////////////////////////////////////////
		//
		//  startDay
		//
		//////////////////////////////////////////////////////////

		public function set startDay(value:String):void
		{
			_startDay = value;
			invalidateCalSlotList();
			invalidateCalHeaderList();
		}
		public function get startDay():String
		{
			return _startDay;
		}

		protected var _startDay:String = DateUtil.DAY_MONDAY;


		//////////////////////////////////////////////////////////
		//
		//  currentMonth
		//
		//////////////////////////////////////////////////////////

		public function set currentMonth(value:Date):void
		{
			_currentMonth = value;
			invalidateCalSlotList();
		}
		public function get currentMonth():Date
		{
			return _currentMonth;
		}

		protected var _currentMonth:Date;


		//////////////////////////////////////////////////////////
		//
		//  calSlotList
		//
		//////////////////////////////////////////////////////////

		[Bindable("calSlotListChanged")]
		public function get calSlotList():IList
		{
			if(!_calSlotList)
			{
				createCalSlotList();
			}
			if(isInvalidateCalSlotList)
			{
				updateCalSlotList();
			}
			return _calSlotList;
		}
		protected var _calSlotList:ArrayCollection;

		protected var _dateCalSlotMap:Dictionary;

		protected var _dateStateMap:Dictionary;

		protected function createCalSlotList():void
		{
			_calSlotList = new ArrayCollection();
			for (var i:uint=0; i<42; i++)
			{
				_calSlotList.addItem(new CalSlot());
			}
		}

		protected var isInvalidateCalSlotList:Boolean = false;

		protected function invalidateCalSlotList():void
		{
			isInvalidateCalSlotList = true;
			dispatchEvent(new Event("calSlotListChanged"));
		}

		protected function updateCalSlotList():void
		{
			if(_currentMonth)
			{
				setLabelAndInitialize();
				setAvailable();
//				setReserved();
				setReserving();
				updateCalSlotState();
			}

			isInvalidateCalSlotList = false;
		}

		/**
		 * 表示日付の更新、StatusはすべてnoneかnotAvailable
		 */
		protected function setLabelAndInitialize():void
		{
			var dates:Vector.<Date> = DateUtil.getDatesInMonth(_currentMonth);
			dates = DateUtil.paddingFront(dates, startDay);
			dates = DateUtil.paddingBack(dates, 42);

			_dateCalSlotMap = new Dictionary();
			_dateStateMap = new Dictionary();

			for (var i:uint=0; i<42; i++)
			{
				var calSlot:CalSlot = _calSlotList.getItemAt(i) as CalSlot;
				var date:Date = dates[i];
				if(calSlot)
				{
					if(date)
					{
						calSlot.date = date;
						_dateStateMap[date.date] = CalSlot.STATUS_NOT_AVAILABLE;
//						calSlot.status = CalSlot.STATUS_NOT_AVAILABLE;

						//updateCalSlotDateMap
						_dateCalSlotMap[date.date] = calSlot;
					}
					else
					{
						calSlot.date = null;
						calSlot.status = CalSlot.STATUS_NONE;
					}
				}
			}
		}

		protected function setAvailable():void
		{
//			var targetCalSlotList:Vector.<CalSlot> = getTargetCalSlotListByTermList(_availableTermList);
//			for each (var calSlot:CalSlot in targetCalSlotList)
//			{
//				calSlot.status = CalSlot.STATUS_AVAILABLE;
//			}
			setState(_availableTermList, CalSlot.STATUS_AVAILABLE);
		}

//		protected function setReserved():void
//		{
//			var targetCalSlotList:Vector.<CalSlot> = getTargetCalSlotListByTermList(_reservedTermList);
//			for each (var calSlot:CalSlot in targetCalSlotList)
//			{
//				calSlot.status = CalSlot.STATUS_RESERVED;
//			}
//		}

		protected function setReserving():void
		{
//			var targetCalSlotList:Vector.<CalSlot> = getTargetCalSlotListByTermList(reservingTermList);
//			for each (var calSlot:CalSlot in targetCalSlotList)
//			{
//				if(calSlot.status == CalSlot.STATUS_AVAILABLE)
//					calSlot.status = CalSlot.STATUS_RESERVING;
//			}
			setState(reservingTermList, CalSlot.STATUS_RESERVING);
		}

		protected function getTargetCalSlotListByTermList(termList:IList):Vector.<CalSlot>
		{
			var calSlotList:Vector.<CalSlot> = new Vector.<CalSlot>();

			for each (var term:Term in termList)
			{
//				if(
//					DateUtil.hasSameMonth(term.startTime, _currentMonth)
//					||
//					DateUtil.hasSameMonth(DateUtil.sub(term.endTime, 1, DateUtil.UNIT_DATE), _currentMonth)
//				)
//				{
					var date:Date = new Date(term.startTime.fullYear, term.startTime.month, term.startTime.date);
					var endDate:Date = new Date(term.endTime.fullYear, term.endTime.month, term.endTime.date);
					while (date.getTime() < endDate.getTime())
					{
						if(DateUtil.hasSameMonth(date, _currentMonth))
						{
							var calSlot:CalSlot = _dateCalSlotMap[date.date] as CalSlot;
							if(calSlot)
							{
								calSlotList.push(calSlot);
							}
						}
						date = DateUtil.add(date, 1, DateUtil.UNIT_DATE);
					}
//				}
			}

			return calSlotList;
		}

		protected function setState(termList:IList, state:String):void
		{
			for each (var term:Term in termList)
			{
				var date:Date = new Date(term.startTime.fullYear, term.startTime.month, term.startTime.date);
				var endDate:Date = new Date(term.endTime.fullYear, term.endTime.month, term.endTime.date);
				while (date.getTime() < endDate.getTime())
				{
					if(DateUtil.hasSameMonth(date, _currentMonth))
					{
						_dateStateMap[date.date] = state;
//						var calSlot:CalSlot = _dateCalSlotMap[date.date] as CalSlot;
//						if(calSlot)
//						{
//							calSlotList.push(calSlot);
//						}
					}
					date = DateUtil.add(date, 1, DateUtil.UNIT_DATE);
				}
			}
		}

		protected function updateCalSlotState():void
		{
			for (var key:String in _dateStateMap)
			{
				var date:Number = Number(key);
				var calSlot:CalSlot = _dateCalSlotMap[date] as CalSlot;
				if (calSlot)
				{
					calSlot.status = _dateStateMap[date];
				}
			}
		}

		//////////////////////////////////////////////////////////
		//
		//  reservingTermList
		//
		//////////////////////////////////////////////////////////

		[Bindable("reservingTermListChanged")]
		public function get reservingTermList():IList
		{
			updateReservingTermList();
			return _availableReservingTermList;
		}

		protected var _availableReservingTermList:ArrayCollection = new ArrayCollection();
		protected var _reservingTermList:ArrayCollection = new ArrayCollection();

		/**
		 * 予約可能期間と予約申請期間を反転する.
		 * @param start 開始日のインデックス
		 * @param end 終了日のインデックス
		 */
		protected function reverseAvailableAndReserving(start:uint, end:uint):void
		{
			var targetTermList:Array = new Array();
			var term:Term;

			for (var i:uint = start; i<=end && i<_calSlotList.length; i++)
			{
				var calSlot:CalSlot = calSlotList.getItemAt(i) as CalSlot;

				if(calSlot)
				{
					switch (calSlot.status)
					{
						case CalSlot.STATUS_AVAILABLE:
						case CalSlot.STATUS_RESERVING:
//						case CalSlot.STATUS_RESERVED:
							if(term)
							{
								term.endTime = DateUtil.add(calSlot.date, 1, DateUtil.UNIT_DATE);
							}
							else
							{
								term = new Term();
								term.startTime = calSlot.date;
								term.endTime = DateUtil.add(calSlot.date, 1, DateUtil.UNIT_DATE);
								targetTermList.push(term);
							}
							break;
						default:
							term = null;
							break;
					}
				}
			}

			_reservingTermList = new ArrayCollection(TermUtil.arrayXorArray(_reservingTermList.toArray(), targetTermList));

			invalidateCalSlotList();
			invalidateReservingTermList();
		}

		protected var isInvalidateReservingTermList:Boolean = false;

		protected function invalidateReservingTermList():void
		{
			isInvalidateReservingTermList = true;
			dispatchEvent(new Event("reservingTermListChanged"));
		}

		protected function updateReservingTermList():void
		{
			//他の日付リストと矛盾がないようにreservaing期間を削る。
			_availableReservingTermList = new ArrayCollection(TermUtil.arrayAndArray(_reservingTermList.toArray(), _availableTermList.toArray()));
			isInvalidateReservingTermList = false;
		}

		//////////////////////////////////////////////////////////
		//
		//  reservedTermList
		//
		//////////////////////////////////////////////////////////

		public function set reservedTermList(value:IList):void
		{
			_reservedTermList = value;
			_reservingTermList = new ArrayCollection(TermUtil.arrayOrArray(_reservingTermList.toArray(), _reservedTermList.toArray()));
			invalidateReservingTermList();
			invalidateCalSlotList();
		}
		public function get reservedTermList():IList
		{
			return _reservedTermList;
		}
		protected var _reservedTermList:IList;

		//////////////////////////////////////////////////////////
		//
		//  availableTermList
		//
		//////////////////////////////////////////////////////////

		public function set availableTermList(value:IList):void
		{
			_availableTermList = value;
			invalidateReservingTermList();
			invalidateCalSlotList();
		}
		public function get availableTermList():IList
		{
			return _availableTermList;
		}
		protected var _availableTermList:IList;

		//////////////////////////////////////////////////////////
		//
		//  calHeaderList
		//
		//////////////////////////////////////////////////////////

		[Bindable("calHeaderListChanged")]
		public function get calHeaderList():IList
		{
			if(_isInvalidateCalHeaderList)
				updateCalHeaderList();

			return _calHeaderList;
		}
		protected var _calHeaderList:ArrayCollection = new ArrayCollection;

		public function invalidateCalHeaderList():void
		{
			_isInvalidateCalHeaderList = true;
			dispatchEvent(new Event("calHeaderListChanged"));
		}
		protected var _isInvalidateCalHeaderList:Boolean = true;

		protected function updateCalHeaderList():void
		{
			_calHeaderList = new ArrayCollection();

			var dayList:Array = DateUtil.getDayList(startDay);
			var resourceManager:IResourceManager =  ResourceManager.getInstance();
			for each (var day:String in dayList)
			{
				_calHeaderList.addItem(resourceManager.getString("APPUI", "calendar."+day));
			}
			_isInvalidateCalHeaderList = false;
		}

	}
}