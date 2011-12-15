/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;

	/**
	 * タイマーの便利クラス.
	 * <p>実行すると最初に登録したメソッドを実行します。
	 *
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
	public class VhutTimer
	{
		/**
		 * コンストラクタ.
		 * @param delay 実行間隔
		 * @param func 実行メソッド
		 * @param parent 親オブジェクト（実行の主体）
		 */
		public function VhutTimer(delay:int, func:Function, parent:*=null)
		{
			this._func = func;
			this._parent = parent;
			timer = new Timer(delay);
			timer.addEventListener(TimerEvent.TIMER, onTimerTime);
		}

		private var timer:Timer;
		private var _func:Function;
		private var _parent:*;

		private function onTimerTime(event:TimerEvent):void
		{
			_func.call(_parent);
		}

		public function start():void
		{
			if(timer.running) return;
			_func.call(_parent);
			timer.start();
		}

		public function stop():void
		{
			timer.stop();
		}

	}
}