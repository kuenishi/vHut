package jp.co.ntts.vhut.log
{
	import flash.events.Event;

	/**
	 * ログ設定パネル関連のイベント.
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
	public class LogConfPanelEvent extends Event
	{

		/**
		 * ログ設定パネルを開くためのラベル.
		 */
		private static const LAUNCH:String="launch";

		/**
		 * ログ設定パネルを開くためのイベントを作成する.
		 * @return ログ設定パネルを開くためのイベント
		 *
		 */
		public static function newLaunchMessage():LogConfPanelEvent
		{
			var event:LogConfPanelEvent=new LogConfPanelEvent(LAUNCH);
			return event;
		}
		
		/**
		 * @private
		 * コンストラクタ.
		 * @param type
		 * @param bubbles
		 * @param cancelable
		 *
		 */
		public function LogConfPanelEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		/**
		 * イベントを複製する. 
		 * @return 
		 * 
		 */
		override public function clone():Event
		{
			var event:LogConfPanelEvent=new LogConfPanelEvent(type);
			return event;
		}
	}
}