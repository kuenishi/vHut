/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.log.presentation
{
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.logging.LogEventLevel;
	import mx.logging.targets.TraceTarget;

	/**
	 * トレースターゲットの設定を行うためのコンポーネントのプレゼンテーションモデル.
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
	public class TraceTargetConfGroupPM extends EventDispatcher
	{
		/** @private ロガー */
		private static var logger:ILogger=Log.getLogger("jp.co.ntts.vhut.log.presentation.TraceTargetConfGroupPM");

		function TraceTargetConfGroupPM()
		{
			logger.debug("constructor start")
			logLevelList=new ArrayCollection();
			logLevelList.addItem({label: "DEBUG", data: LogEventLevel.DEBUG});
			logLevelList.addItem({label: "INFO", data: LogEventLevel.INFO});
			logLevelList.addItem({label: "WARN", data: LogEventLevel.WARN});
		}

		[Inject]
		/**
		 * ロギングターゲット. 
		 */
		public var target:TraceTarget;

		[Bindable]
		/**
		 * 選択中のログレベルのインデックス.
		 * @default 2
		 */
		public var selectedLogLevelIndex:int=2;

		[Bindable]
		/**
		 * ログレベルの選択肢.
		 * @return ログレベルの選択肢
		 *
		 */
		public var logLevelList:IList;

		[Bindable(event="isTargetEnableChanged")]
		/**
		 * トレースへのログ出力が有効.
		 * @default false
		 *
		 */
		public function get isTargetEnable():Boolean
		{
			return _isTargetEnable;
		}
		/**
		 * @private
		 */
		public function set isTargetEnable(value:Boolean):void
		{
			_isTargetEnable=value;
			updateTarget()
			dispatchEvent(new Event("isTargetEnableChanged"));
		}
		private var _isTargetEnable:Boolean=false;

		/**
		 * ログレベル.
		 * @default LogEventLevel.WARN
		 */
		public function get logLevel():int
		{
			return _logLevel
		}
		/**
		 * @private
		 */
		public function set logLevel(value:int):void
		{
			_logLevel=value;
			updateTarget();
		}
		private var _logLevel:int=LogEventLevel.WARN;

		[Init]
		/**
		 * 初期化処理.
		 */
		public function init():void
		{
			updateTarget()
		}

		/**
		 * @private
		 * ログターゲットを更新する.
		 */
		protected function updateTarget():void
		{
			if (isTargetEnable)
			{
				Log.addTarget(target);
				target.level=logLevel;
				switch (target.level)
				{
					case LogEventLevel.ALL:
					case LogEventLevel.DEBUG:
						selectedLogLevelIndex=0;
						break;
					case LogEventLevel.INFO:
						selectedLogLevelIndex=1;
						break;
					default:
						selectedLogLevelIndex=2;
				}
			}
			else
			{
				Log.removeTarget(target);
			}
		}
	}
}