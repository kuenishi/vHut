/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	/**
	 * コマンドの状況確認クラス
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
	public class CommandWatcher extends EventDispatcher
	{
		/** タイムリミット */
		private static const TIME_LIMIT:Number = 5000;
		/** チェックするコマンドの最大件数 */
		private static const CACHE_LENGTH:uint = 200;
		/** コマンドリスト */
		private var items:Array = new Array();
		/** ステータス確認タイマー */
		private var updateTimer:Timer;

		/**
		 * コンストラクタ
		 * @param target
		 */
		public function CommandWatcher(target:IEventDispatcher=null)
		{
			super(target);
			createUpdateTimer();
		}

		/** ステータス確認タイマーの作成 */
		private function createUpdateTimer():void
		{
			updateTimer = new Timer(TIME_LIMIT/2);
			updateTimer.addEventListener(TimerEvent.TIMER, function (event:TimerEvent):void
			{
				updateCommands();
			}
				);
			updateTimer.start();
		}


		/**
		 * コマンドの開始を報告する
		 * @param command コマンド
		 */
		public function tellExecute(command:BaseCommand):void
		{
			items.unshift(new CommandItem(command));
			updateCommands();
		}

		/**
		 * コマンドの成功を報告する
		 * @param command コマンド
		 */
		public function tellSuccess(command:BaseCommand):void
		{
			var item:CommandItem = getCommandItem(command);
			if (item)
			{
				item.success();
				updateCommands();
			}
		}

		/**
		 * コマンドのエラーを報告する
		 * @param command コマンド
		 */
		public function tellError(command:BaseCommand):void
		{
			var item:CommandItem = getCommandItem(command);
			if (item)
			{
				item.error();
				updateCommands();
			}
		}

		/**
		 *  コマンドが関連するデータをスタックから取得する
		 * @param command コマンド
		 * @return データ要素
		 */
		private function getCommandItem(command:BaseCommand):CommandItem
		{
			for each (var item:CommandItem in items)
			{
				if (item.command == command)
				{
					return item;
				}
			}
			return null;
		}

		/** スタックあり */
		[Bindable("commandsChanged")]
		public function get isStacked():Boolean
		{
			return _isStacked;
		}
		private var _isStacked:Boolean = false;

		/** コマンドの状態を更新 */
		private function updateCommands():void
		{
			var nowTime:Number = new Date().time
			_isStacked = false;
			for each (var item:CommandItem in items)
			{
				if (item.state == CommandItem.STATE_EXECUTING) {
					if (nowTime - item.startDate.time > TIME_LIMIT) {
						item.timeOut();
					}
					else
					{
						_isStacked = true;
					}
				}
			}
			items = items.slice(0, CACHE_LENGTH);
			dispatchEvent(new Event("commandsChanged"));
		}
	}
}
import jp.co.ntts.vhut.core.BaseCommand;

/**
 * コマンド情報を定義するメタデータ
 * @author NTT Software Corporation.
 *
 */
class CommandItem
{
	/** 実行中 */
	public static const STATE_EXECUTING:String = "stateExecuting";
	/** 成功 */
	public static const STATE_SUCCESS:String = "stateSuccess";
	/** エラー */
	public static const STATE_ERROR:String = "stateError";
	/** タイムアウト */
	public static const STATE_TIMEOUT:String = "stateTimeOut";

	public var command:BaseCommand;
	public var startDate:Date;
	public var endDate:Date;
	public var state:String;
	/**
	 * コンストラクタ.
	 * @param command コマンド
	 * @param state 状態
	 * @param startDate 開始時刻
	 */
	public function CommandItem(command:BaseCommand, state:String="stateExecuting", startDate:Date=null)
	{
		this.command = command;
		this.state = state;
		if (!startDate)
		{
			this.startDate = new Date();
		}
		else
		{
			this.startDate = startDate;
		}
	}

	/** 終了を成功として記録する */
	public function success():void
	{
		state = STATE_SUCCESS;
		endDate = new Date();
	}

	/** 終了をエラーとして記録する */
	public function error():void
	{
		state = STATE_ERROR;
		endDate = new Date();
	}

	/** 終了をタイムアウトとして記録する */
	public function timeOut():void
	{
		state = STATE_TIMEOUT;
		endDate = new Date();
	}
}