/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import flashx.textLayout.events.DamageEvent;
	import flashx.textLayout.operations.SplitParagraphOperation;

	/**
	 * <p>
	 * <br>
	 * <p>
	 * <ul>
	 * <li>
	 * </ul>

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
	public class DateUtil
	{
		public function DateUtil()
		{
		}

		public static const DAY_SUNDAY:String = "sunday";
		public static const DAY_MONDAY:String = "monday";
		public static const DAY_TUESDAY:String = "tuesday";
		public static const DAY_WEDNESDAY:String = "wednesday";
		public static const DAY_THURSDAY:String = "thursday";
		public static const DAY_FRIDAY:String = "friday";
		public static const DAY_SATURDAY:String = "saturday";

		public static const UNIT_MILLISECOND:Number = 1;
		public static const UNIT_SECOND:Number = 1000;
		public static const UNIT_MINUTE:Number = 60 * 1000;
		public static const UNIT_HOUR:Number = 60 * 60 * 1000;
		public static const UNIT_DATE:Number = 24 * 60 * 60 * 1000;
		public static const UNIT_WEEK:Number = 7 * 24 * 60 * 60 * 1000;

		public static const DEFAULT_DAY_LIST:Array = [DAY_SUNDAY, DAY_MONDAY, DAY_TUESDAY, DAY_WEDNESDAY, DAY_THURSDAY, DAY_FRIDAY, DAY_SATURDAY];

		public static function parseDateString(value:String):Date
		{
			var array:Array = value.split("/");
			return new Date(Number(array[0]), Number(array[1]) - 1, Number(array[2]));
		}

		public static function getDatesInMonth(month:Date):Vector.<Date>
		{
			var dates:Vector.<Date> = new Vector.<Date>();
			var date:Date = new Date(month.fullYear, month.month, 1, 0, 0, 0, 0);
			while (month.month == date.month)
			{
				dates.push(date);
				date = new Date(date.getTime() + 24 * 60 * 60 * 1000);
			}
			return dates;
		}

		public static function paddingFront(dates:Vector.<Date>, startDay:String="sunday"):Vector.<Date>
		{
			var startDayNumber:uint = 0;
			switch (startDay)
			{
				case DAY_SUNDAY:
					startDayNumber = 0;
					break;
				case DAY_MONDAY:
					startDayNumber = 1;
					break;
				case DAY_TUESDAY:
					startDayNumber = 2;
					break;
				case DAY_WEDNESDAY:
					startDayNumber = 3;
					break;
				case DAY_THURSDAY:
					startDayNumber = 4;
					break;
				case DAY_FRIDAY:
					startDayNumber = 5;
					break;
				case DAY_SATURDAY:
					startDayNumber = 6;
					break;
			}
			var firstDate:Date = dates[0];
			var paddingLength:uint = (7 + firstDate.day - startDayNumber) % 7;

			for (var i:uint=0; i<paddingLength; i++)
			{
				dates.unshift(null);
			}
			return dates;
		}

		public static function paddingBack(dates:Vector.<Date>, length:uint):Vector.<Date>
		{
			var paddingLength:uint = length - dates.length;
			for (var i:uint=0; i<paddingLength; i++)
			{
				dates.push(null);
			}
			return dates;
		}

		public static function hasSameMonth(date0:Date, date1:Date):Boolean
		{
			return (date0.fullYear == date1.fullYear
				&& date0.month == date1.month);
		}

		public static function add(date:Date, num:Number, unit:Number=1):Date
		{
			return new Date(date.getTime() + num * unit);
		}

		public static function sub(date:Date, num:Number, unit:Number=1):Date
		{
			return new Date(date.getTime() - num * unit);
		}

		public static function getDayList(startDay:String = "sunday"):Array
		{
			var index:uint = 0;
			switch (startDay)
			{
				case DAY_SUNDAY:
					index = 0;
					break;
				case DAY_MONDAY:
					index = 1;
					break;
				case DAY_TUESDAY:
					index = 2;
					break;
				case DAY_WEDNESDAY:
					index = 3;
					break;
				case DAY_THURSDAY:
					index = 4;
					break;
				case DAY_FRIDAY:
					index = 5;
					break;
				case DAY_SATURDAY:
					index = 6;
					break;
			}

			return DEFAULT_DAY_LIST.slice(index).concat(DEFAULT_DAY_LIST.slice(0, index));
		}

		public static function floorAsDate(value:Date):Date
		{
			return new Date(value.fullYear, value.month, value.date);
		}
	}
}