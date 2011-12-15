/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.util
{
	import mx.logging.LogEventLevel;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	/**
	 * xxxクラス.
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
	public class MessageUtil
	{
		/** @private DEBUG用ログコードのヘッダー. */
		public static const H_DEBUG:String="D";
		/** @private INFO用ログコードのヘッダー. */
		public static const H_INFO:String="I";
		/** @private WARN用ログコードのヘッダー. */
		public static const H_WARN:String="W";
		/** @private ERROR用ログコードのヘッダー. */
		public static const H_ERROR:String="E";
		/** @private FATAL用ログコードのヘッダー. */
		public static const H_FATAL:String="F";
		
		/**
		 *  @private
		 *  Storage for the resourceManager getter.
		 *  This gets initialized on first access,
		 *  not at static initialization time, in order to ensure
		 *  that the Singleton registry has already been initialized.
		 */
		private static var _resourceManager:IResourceManager;
		
		/**
		 *  @private
		 *  A reference to the object which manages
		 *  all of the application's localized resources.
		 *  This is a singleton instance which implements
		 *  the IResourceManager interface.
		 */
		private static function get resourceManager():IResourceManager
		{
			if (!_resourceManager)
				_resourceManager=ResourceManager.getInstance();
			return _resourceManager;
		}
		
		public static function getMessageTemplate(msgcode:String):String
		{
			var module:String=msgcode.slice(1, msgcode.length - 4);
			return resourceManager.getString(module + "Messages", msgcode);
		}
		
		public static function getMessage(msgcode:String, ... rest):String
		{
			var msg:String = getMessageTemplate(msgcode);
			for (var i:int = 0; i < rest.length; i++)
			{
				msg = msg.replace(new RegExp("\\{"+i+"\\}", "g"), rest[i]);
			}
			return msg;
		}
		
		public static function getLogEventLevel(msgcode:String):int
		{
			var msgLevel:String=msgcode.slice(0, 1);
			var level:int=LogEventLevel.DEBUG;
			switch (msgLevel)
			{
				case H_DEBUG:
					level=LogEventLevel.DEBUG;
					break;
				case H_INFO:
					level=LogEventLevel.INFO;
					break;
				case H_WARN:
					level=LogEventLevel.WARN;
					break;
				case H_ERROR:
					level=LogEventLevel.ERROR;
					break;
				case H_FATAL:
					level=LogEventLevel.FATAL;
					break;
			}
			return level;
		}
		
	}
}