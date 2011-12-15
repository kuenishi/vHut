/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.log
{
	import jp.co.ntts.vhut.util.MessageUtil;
	
	import mx.logging.ILogger;
	import mx.logging.LogEventLevel;
	import mx.logging.LogLogger;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	/**
	 * vHut独自ロガー.
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
	public class VhutLogLogger
	{
		/**
		 * コンストラクター.
		 * @param logger
		 *
		 */
		public function VhutLogLogger(logger:ILogger)
		{
			_logger=logger;
		}

		/**
		 * @private
		 * 標準ロガー
		 */
		private var _logger:ILogger;


		/**
		 * ログを書き込む,
		 *
		 * @param msgcode 設定ファイル上に定義されたメッセージID
		 * @param rest そのほかのパラメータ
		 * <p>LogLoggerと同様に{x}を置換する。</p>
		 *
		 */
		public function log(msgcode:String, ... rest):void
		{
			rest.unshift(msgcode);
			var message:String = MessageUtil.getMessage.apply(null, rest);
			var level:int = MessageUtil.getLogEventLevel(msgcode);
			_logger.log(level, message);
		}
		
//		public function request(msgcode:String, ... rest):void
//		{
//			var level:int = MessageUtil.getLogEventLevel(msgcode);
//		}
//		
//		public function reply(msgcode:String, ... rest):void
//		{
//			var level:int = MessageUtil.getLogEventLevel(msgcode);
//		}
	}
}