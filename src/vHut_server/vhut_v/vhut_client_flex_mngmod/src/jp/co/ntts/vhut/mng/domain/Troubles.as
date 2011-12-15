/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.dto.ServiceTroubleDto;
	
	import mx.collections.IList;
	
	[Event(name="getTrouble", type="jp.co.ntts.vhut.core.GetEvent")]
	[ManagedEvents(names="getTrouble")]
	/**
	 * 障害情報の管理クラス.
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
	public class Troubles extends EventDispatcher
	{
		public static const CHANGE_DTO:String = "changeDto";
		
		public function Troubles(target:IEventDispatcher=null)
		{
			super(target);
		}
		
		public function set dto(value:ServiceTroubleDto):void
		{
			_dto = value;
			dispatchEvent(new Event(CHANGE_DTO));
		}
		private var _dto:ServiceTroubleDto;
		
		[Bindable("changeDto")]
		/** 障害が起きているコンテンツのリスト */
		public function get apps():IList
		{
			return _dto.aList;
		}
		
		[Bindable("changeDto")]
		/** 障害が起きている研修のリスト */
		public function get aigs():IList
		{
			return _dto.aigList;
		}
		
		[Bindable("changeDto")]
		/** 障害が起きているホストのリスト */
		public function get hosts():IList
		{
			return _dto.hostList;
		}
		
		[Bindable("changeDto")]
		/** 障害が起きているユーザのリスト */
		public function get users():IList
		{
			return _dto.userList;
		}
		
		public function updateTroubles():void
		{
			dispatchEvent(GetEvent.newGetTroubleEvent());
		}
	}
}