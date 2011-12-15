/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.form.application
{
	import flash.events.Event;

	/**
	 * 検索を実施した時のイベント
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
	public class SearchEvent extends Event
	{
		public static const SEARCH:String = "search";

		public static function newSearchEvent(keyword:String):SearchEvent
		{
			var event:SearchEvent = new SearchEvent(SEARCH);
			event.keyword = keyword;
			return event;
		}

		public function SearchEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		/** 検索キーワード */
		public function set keyword(value:String):void
		{
			if(_keyword == value)
				return;
			_keyword = value;
			updateKeywords();
		}
		public function get keyword():String
		{
			return _keyword;
		}
		protected var _keyword:String;

		public function get keywords():Array
		{
			return _keywords;
		}
		protected var _keywords:Array = new Array();

		protected function updateKeywords():void
		{
			if(!keyword)
			{
				_keywords = new Array();
				return;
			}

			_keywords = keyword.split(/\s+/);
		}

		override public function clone():Event
		{
			var event:SearchEvent = new SearchEvent(type, bubbles, cancelable);
			event.keyword = keyword;
			return event;
		}
	}
}