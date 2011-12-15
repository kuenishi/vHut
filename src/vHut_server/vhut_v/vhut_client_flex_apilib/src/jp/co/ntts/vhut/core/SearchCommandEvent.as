/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core
{
	import flash.events.Event;

	/**
	 * コマンドを検索するイベント
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
	public class SearchCommandEvent extends Event
	{
		/** すべてのコマンドについて検索します. */
		public static const ALL:String = "searchAllCommands";
		/** Applicationに関連があるコマンドについて検索します. */
		public static const APP:String = "searchAppCommands";
		/** ReleasedApplicationに関連があるコマンドについて検索します. */
		public static const RAPP:String = "searchRappCommands";
		/** ApplicationInstanceに関連があるコマンドについて検索します. */
		public static const AI:String = "searchAICommands";

		/**
		 * コマンド検索（全対象）イベントを作成します.
		 * @param keyword 検索キー
		 * @param startTime 検索範囲（開始）
		 * @param endTime 検索範囲（終了）
		 * @param operations  対象オペレーションリスト
		 * @param statuses 対象状態リスト
		 * @return イベント
		 *
		 */
		public static function newSearchAllCommandEvent(keyword:String=null, startTime:Date=null, endTime:Date=null, operations:Array=null, statuses:Array=null):SearchCommandEvent
		{
			return newSearchCommandEvent(ALL, keyword, startTime, endTime, operations, statuses);
		}

		/**
		 * コマンド検索（Application関連）イベントを作成します.
		 * @param keyword 検索キー
		 * @param startTime 検索範囲（開始）
		 * @param endTime 検索範囲（終了）
		 * @param operations  対象オペレーションリスト
		 * @param statuses 対象状態リスト
		 * @return イベント
		 *
		 */
		public static function newSearchAppCommandEvent(keyword:String=null, startTime:Date=null, endTime:Date=null, operations:Array=null, statuses:Array=null):SearchCommandEvent
		{
			return newSearchCommandEvent(APP, keyword, startTime, endTime, operations, statuses);
		}

		/**
		 * コマンド検索（ReleasedApplication関連）イベントを作成します.
		 * @param keyword 検索キー
		 * @param startTime 検索範囲（開始）
		 * @param endTime 検索範囲（終了）
		 * @param operations  対象オペレーションリスト
		 * @param statuses 対象状態リスト
		 * @return イベント
		 *
		 */
		public static function newSearchRappCommandEvent(keyword:String=null, startTime:Date=null, endTime:Date=null, operations:Array=null, statuses:Array=null):SearchCommandEvent
		{
			return newSearchCommandEvent(RAPP, keyword, startTime, endTime, operations, statuses);
		}

		/**
		 * コマンド検索（ApplicaitonInstance関連）イベントを作成します.
		 * @param keyword 検索キー
		 * @param startTime 検索範囲（開始）
		 * @param endTime 検索範囲（終了）
		 * @param operations  対象オペレーションリスト
		 * @param statuses 対象状態リスト
		 * @return イベント
		 *
		 */
		public static function newSearchAiCommandEvent(type:String, keyword:String=null, startTime:Date=null, endTime:Date=null, operations:Array=null, statuses:Array=null):SearchCommandEvent
		{
			return newSearchCommandEvent(AI, keyword, startTime, endTime, operations, statuses);
		}

		/**
		 * コマンド検索イベントを作成します.
		 * @param type コマンドタイプ
		 * @param keyword 検索キー
		 * @param startTime 検索範囲（開始）
		 * @param endTime 検索範囲（終了）
		 * @param operations  対象オペレーションリスト
		 * @param statuses 対象状態リスト
		 * @return イベント
		 *
		 */
		public static function newSearchCommandEvent(type:String, keyword:String=null, startTime:Date=null, endTime:Date=null, operations:Array=null, statuses:Array=null):SearchCommandEvent
		{
			var event:SearchCommandEvent = new SearchCommandEvent(type);
			event.keyword = keyword;
			event.startTime = startTime;
			event.endTime = endTime;
			event.operations = operations;
			event.statuses = statuses;
			return event;
		}

		public function SearchCommandEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

		/** 検索キー */
		public var keyword:String;

		/** 検索範囲（開始） */
		public var startTime:Date;

		/** 検索範囲（終了） */
		public var endTime:Date;

		/** 対象オペレーションリスト */
		public var operations:Array;

		/** 対象状態リスト */
		public var statuses:Array;

		override public function clone():Event
		{
			var event:SearchCommandEvent = new SearchCommandEvent(type, bubbles, cancelable);
			event.keyword = keyword;
			event.startTime = startTime;
			event.endTime = endTime;
			event.operations = operations;
			event.statuses = statuses;
			return event;
		}

	}
}