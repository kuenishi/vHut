/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.app
{
	import flash.events.Event;
	
	import mx.collections.IList;

	/**
	 * 利用期間関連の更新系サーバアクセスイベント.
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
	public class TermEvent extends Event
	{
		private static const SET_TERM:String="setTerm";
		
		/**
		 * 利用期間を変更します.
		 * @param applicationId
		 * @param items
		 * @return 
		 */
		public static function newSetTermEvent(applicationId:Number, items:IList):TermEvent
		{
			var event:TermEvent = new TermEvent(SET_TERM);
			event._applicationId = applicationId;
			event._terms = items;
			return event;
		}

		public function TermEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		private var _applicationId:Number;
		
		public function get applicationId():Number
		{
			return _applicationId;
		}
		
		private var _terms:IList;
		
		public function get terms():IList
		{
			return _terms;
		}
	}
}