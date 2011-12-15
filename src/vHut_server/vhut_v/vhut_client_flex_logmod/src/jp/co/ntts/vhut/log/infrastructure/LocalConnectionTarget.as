/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.log.infrastructure
{

	import mx.core.mx_internal;
	use namespace mx_internal

	import flash.net.LocalConnection;

	import mx.logging.LogEvent;
	import mx.logging.targets.LineFormattedTarget;
	import flash.events.StatusEvent;

	/**
	 * ローカルコネクションでログを飛ばすロギングターゲット.
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
	public class LocalConnectionTarget extends LineFormattedTarget
	{

		/**
		 *  コンストラクタ.
		 *
		 *  <p>ローカルコネクションでログを飛ばすロギングターゲットを生成する</p>
		 *
		 */
		public function LocalConnectionTarget()
		{
			super();
			localConnection=new LocalConnection();
			localConnection.addEventListener(StatusEvent.STATUS, function lc_statusHandler(event:StatusEvent):void
				{
				});
		}

		/** ローカルコネクション. */
		protected var localConnection:LocalConnection;
		/** 一時的に保存されるログレベル. */
		protected var currLevel:uint;

		/**
		 * ログの通知を受けてレベルを設定する。 
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
			localConnection.send("_LocalConnectionTarget", "log", currLevel, message);
		}
	}
}