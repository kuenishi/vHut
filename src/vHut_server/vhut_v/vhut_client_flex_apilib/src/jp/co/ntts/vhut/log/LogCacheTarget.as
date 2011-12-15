/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.log
{
	import mx.core.mx_internal;
	import mx.logging.LogEvent;
	import mx.logging.targets.LineFormattedTarget;

	use namespace mx_internal;

	/**
	 * ログをキャッシュするターゲット.
	 * <p>クラッシュログをファイル出力する機能に用いる。</p>
	 * <p>Singleton</p>
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
	public class LogCacheTarget extends LineFormattedTarget
	{
		/**
		 * vHut独自ロガー.
		 */
		private static var logger:VhutLogLogger=VhutLog.getLogger("jp.co.ntts.vhut.log.LogCacheTarget");

		/**
		 * キャッシュの上限値に設定できる値の最小値.
		 */
		public static const LIMIT_MIN:uint=1;

		/**
		 * キャッシュの上限値に設定できる値の最大値.
		 */
		public static const LIMIT_MAX:uint=1000;

		/**
		 * @privates
		 * インスタンス.
		 */
		private static var _instance:LogCacheTarget;

		/**
		 * インスタンスを取得する.
		 * @return インスタンス
		 *
		 */
		public static function getInstance():LogCacheTarget
		{
			if (!_instance)
			{
				_instance=new LogCacheTarget(new LogCacheTargetInternal());
			}
			return _instance;
		}

		/**
		 * @private
		 * コンストラクタ .
		 * @param internalInstance シングルトンを実現するための仕掛け
		 *
		 */
		public function LogCacheTarget(internalInstance:LogCacheTargetInternal)
		{
			super();
			if (!internalInstance)
			{
				logger.log("EAPI0001");
				throw new Error("use getInstance");
			}
		}

		/**
		 * キャッシュの上限値
		 *
		 */
		public function get limit():int
		{
			return _limit;
		}

		/**
		 * @private
		 */
		public function set limit(value:int):void
		{
			if (value < LIMIT_MIN)
			{
				value=LIMIT_MIN;
			}
			else if (value > LIMIT_MAX)
			{
				value=LIMIT_MAX;
			}
			_limit=value;
		}
		private var _limit:int=100

		/**
		 * ログをデータクラスの配列として受け取る.
		 * データは複製される。
		 * @return ログデータの配列
		 *
		 */
		public function getLogAsLogData():Vector.<LogData>
		{
			var result:Vector.<LogData>=new Vector.<LogData>();
			for each (var data:LogData in logDataList)
			{
				result.push(data.clone());
			}
			return result;
		}


		/**
		 * ログを文字列として受け取る.
		 * @param separator 改行コード @default \n
		 * @return
		 *
		 */
		public function getLogAsString(separator:String="\n"):String
		{
			var result:String="";
			for each (var data:LogData in logDataList)
			{
				result+=data.message + separator;
			}
			return result.slice(0, result.length - 1);
		}

		/**
		 * キャッシュされたログデータの配列.
		 */
		protected var logDataList:Vector.<LogData>=new Vector.<LogData>();

		/** 一時的に保存されるログレベル. */
		protected var currLevel:uint;

		/**
		 * ログの通知を受けてレベルを設定する.
		 * @param event ロギングイベント
		 *
		 */
		override public function logEvent(event:LogEvent):void
		{
			currLevel=event.level;
			super.logEvent(event);
		}

		/**
		 *  @private
		 *  This method outputs the specified message directly to
		 *  <code>Localconnection</code>.
		 *  All output will be directed to flashlog.txt by default.
		 *
		 *  @param message String containing preprocessed log message which may
		 *  include time, date, category, etc. based on property settings,
		 *  such as <code>includeDate</code>, <code>includeCategory</code>, etc.
		 */
		override mx_internal function internalLog(message:String):void
		{
			logDataList.push(new LogData(currLevel, message));
			if (logDataList.length > limit)
			{
				logDataList = logDataList.slice(logDataList.length-limit);
			}
			else if (logDataList.length == limit + 1)
			{
				delete logDataList.shift();
			}
		}
		
		/**
		 * キャッシュ消去する. 
		 * 
		 */
		public function clear():void
		{
			logDataList = new Vector.<LogData>();
		}
	}
}

/**
 * シングルトンを実現するために必要なクラス.
 * @author NTT Software Corporation.
 *
 */
class LogCacheTargetInternal
{
	public function LogCacheTargetInternal()
	{

	}
}