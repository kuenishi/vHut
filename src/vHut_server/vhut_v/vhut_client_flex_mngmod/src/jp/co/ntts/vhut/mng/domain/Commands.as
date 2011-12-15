/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.domain
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.SearchCommandEvent;
	import jp.co.ntts.vhut.util.DateUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="searchAllCommands", type="jp.co.ntts.vhut.core.SearchCommandEvent")]
	[ManagedEvents(names="searchAllCommands")]
	/**
	 * コマンドの管理クラス.
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
	public class Commands extends EventDispatcher
	{
		public function Commands(target:IEventDispatcher=null)
		{
			super(target);
		}

		[Bindable]
		public function get commandDtos():IList
		{
			return _commandDtos;
		}
		public function set commandDtos(value:IList):void
		{
			_commandDtos.removeAll();
			_commandDtos.addAll(value);
		}
		private var _commandDtos:ArrayCollection = new ArrayCollection();

		[Bindable]
		/** 検索キー */
		public function set keyword(value:String):void
		{
			_keyword = value;
		}
		public function get keyword():String
		{
			return _keyword;
		}
		private var _keyword:String

		[Bindable]
		/** 対象日時 */
		public function set targetDate(value:Date):void
		{
			_targetDate = DateUtil.floorAsDate(value);
		}
		public function get targetDate():Date
		{
			return _targetDate;
		}
		private var _targetDate:Date = new Date();

		[Bindable]
		/** 検索対象期間（日） */
		public function set dateSpan(value:Number):void
		{
			_dateSpan = value;
		}
		public function get dateSpan():Number
		{
			return _dateSpan;
		}
		private var _dateSpan:Number = 1;

		public function get startTime():Date
		{
			return DateUtil.sub(targetDate, dateSpan-1, DateUtil.UNIT_DATE);
		}

		public function get endTime():Date
		{
			return DateUtil.add(targetDate, 1, DateUtil.UNIT_DATE);
		}

		[Bindable]
		/** 対象操作リスト */
		public function set operations(value:Array):void
		{
			_operations = value;
		}
		public function get operations():Array
		{
			return _operations;
		}
		private var _operations:Array;

		[Bindable]
		/** 対象状態リスト */
		public function set statuses(value:Array):void
		{
			_statuses = value;
		}
		public function get statuses():Array
		{
			return _statuses;
		}
		private var _statuses:Array;

		public function updateCommandDtos():void
		{
			dispatchEvent(SearchCommandEvent.newSearchAllCommandEvent(
				keyword
				, startTime
				, endTime
				, operations
				, statuses
			));
		}
	}
}