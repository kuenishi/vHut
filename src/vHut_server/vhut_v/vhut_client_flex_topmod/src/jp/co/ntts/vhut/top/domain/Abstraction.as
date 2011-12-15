/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.top.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import mx.resources.ResourceManager;

	/**
	 * 全ユーザに知らせるべき、システムの概要情報
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
	public class Abstraction extends EventDispatcher
	{

		[Bindable]
		/**
		 * パフォーマンス評価指標
		 */
		public function set performanceRank(value:Number):void
		{
			_performanceRank = value;
			_performanceString = ResourceManager.getInstance().getString("TOPUI", "performance."+value.toString());
			dispatchEvent(new Event("performanceRankChanged"));
		}
		public function get performanceRank():Number
		{
			return _performanceRank;
		}
		protected var _performanceRank:Number;

		[Bindable("performanceRankChanged")]
		public function get performanceValue():Number
		{
			return (6-_performanceRank)/5
		}

		[Bindable("performanceRankChanged")]
		public function get performanceString():String
		{
			return _performanceString;
		}
		protected var _performanceString:String;

		[Bindable]
		/**
		 * 障害有無
		 */
		public function set troubleFlag(value:Boolean):void
		{
			_troubleFlag = value;
			_troubleString = ResourceManager.getInstance().getString("TOPUI", "trouble." + (value ? 'false' : 'true'));
			dispatchEvent(new Event("troubleFlagChanged"));
		}
		public function get troubleFlag():Boolean
		{
			return _troubleFlag;
		}
		protected var _troubleFlag:Boolean;

		[Bindable("troubleFlagChanged")]
		public function get troubleString():String
		{
			return _troubleString;
		}
		protected var _troubleString:String;
	}
}