package jp.co.ntts.vhut.log.presentation
{
	import flash.events.EventDispatcher;
	
	import jp.co.ntts.vhut.log.LogConfPanelEvent;
	
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.logging.targets.TraceTarget;
	
	[Event(name="launch", type="jp.co.ntts.vhut.log.LogConfPanelEvent")]
	[ManagedEvents("launch")]
	/**
	 * ログ設定用モジュールのメインクラスのプレゼンテーションモデル.
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
	public class LogPM extends EventDispatcher
	{
		private static var logger:ILogger = Log.getLogger("jp.co.ntts.vhut.log.presentation.LogPM");
		
		[Bindable]
		/**
		 * パネルの開閉状態.
		 * この値はポップアップマネージャからリッスンされているため、
		 * 変更は直にポップアップに反映される。
		 */
		public var isPanelOpen:Boolean = false;
		
		[MessageHandler(selector="launch",scope="local")]
		/**
		 * ポップアップを起動するメッセージを受け取る.
		 * @param event ポップアップを起動するメッセージ
		 * 
		 */
		public function handleMessage(event:LogConfPanelEvent):void
		{
			logger.debug("handleMessage")
			isPanelOpen = true;
		}
	}
}