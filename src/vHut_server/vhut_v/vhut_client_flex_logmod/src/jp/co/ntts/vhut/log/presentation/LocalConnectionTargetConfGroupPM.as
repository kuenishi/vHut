/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.log.presentation
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import jp.co.ntts.vhut.log.infrastructure.LocalConnectionTarget;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.logging.LogEventLevel;
	import mx.logging.targets.TraceTarget;

	/**
	 * ローカルコネクションターゲットの設定を行うためのコンポーネントのプレゼンテーションモデル.
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
	public class LocalConnectionTargetConfGroupPM extends EventDispatcher
	{
		private static var logger:ILogger=Log.getLogger("jp.co.ntts.vhut.log.presentation.LocalConnectionTargetConfGroupPM");

		[Inject(id="localConnectionTarget")]
		/**
		 * ロギングターゲット.
		 */
		public var target:LocalConnectionTarget;

		[Bindable(event="isTargetEnableChanged")]
		/**
		 * トレースへのログ出力が有効/無効.
		 * @default false
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
		 * ロギングターゲットを更新する.
		 */
		protected function updateTarget():void
		{
			if (isTargetEnable)
			{
				Log.addTarget(target)
			}
			else
			{
				Log.removeTarget(target);
			}
		}
		
		/**
		 * ログビューアーを起動する.
		 * 
		 */
		public function launchViewer():void
		{
			navigateToURL(new URLRequest("LogViewer.html"), "_blank");
		}
	}
}