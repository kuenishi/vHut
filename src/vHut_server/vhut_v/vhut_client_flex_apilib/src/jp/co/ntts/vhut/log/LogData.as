/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.log
{
	/**
	 * ログレコードを表す.
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
	public class LogData
	{
		/**
		 * ログレベル.
		 */
		public var level:uint;
		/**
		 * ログメッセージ.
		 */
		public var message:String;
		
		/**
		 * コンストラクタ.
		 * @param level
		 * @param message
		 * 
		 */
		public function LogData(level:uint, message:String)
		{
			this.level = level;
			
			message = message.replace(/&/g, "&amp;");
			message = message.replace(/</g, "&lt;");
			message = message.replace(/>/g, "&gt;");
			this.message = message;
		}
		
		/**
		 * 複製する. 
		 * @return 複製されたLogData
		 * 
		 */
		public function clone():LogData
		{
			return new LogData(level, message);
		}
	}
}