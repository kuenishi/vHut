/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */

package jp.co.ntts.vhut.log.presentation
{
	import flash.events.AsyncErrorEvent;
	import flash.events.EventDispatcher;
	import flash.events.SecurityErrorEvent;
	import flash.net.LocalConnection;
	
	import flashx.textLayout.elements.FlowElement;
	import flashx.textLayout.elements.ParagraphElement;
	import flashx.textLayout.elements.SpanElement;
	import flashx.textLayout.elements.TextFlow;
	
	import jp.co.ntts.vhut.log.LogData;
	import jp.co.ntts.vhut.log.infrastructure.LogViewerEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.logging.LogEventLevel;
	
	[Event(name="addind", type="jp.co.ntts.vhut.infrastructure.LogViewerEvent")]
	[Event(name="added", type="jp.co.ntts.vhut.infrastructure.LogViewerEvent")]
	[ManagedEvents("adding, added")]
	/**
	 * ログビューアーのプレゼンテーションモデル.
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
	public class LogViewerGroupPM extends EventDispatcher
	{
		
		/**
		 * @private
		 * ローカルコネクション接続失敗を表示するテキスト.
		 */
		private static const CONNECTION_ERROR:String="ERROR: The LocalConnection instance is already connected.";
		/**
		 * @private 
		 * ログ出力用のリッチテキストエレメントを作成する.
		 * 
		 * @param text メッセージ
		 * @param color 色
		 * @return リッチテキストエレメント
		 * 
		 */
		private static function createStanardElement(text:String, color:int):FlowElement
		{
			var para:ParagraphElement=new ParagraphElement();
			var span:SpanElement=new SpanElement();
			span.color=color;
			span.text=text;
			para.addChild(span);
			return para;
		}
		
		/**
		 * コンストラクタ. 
		 * 
		 */
		public function LogViewerGroupPM()
		{
			BindingUtils.bindSetter(levelChangeHandler, this, "isDebugLevelEnable");
			BindingUtils.bindSetter(levelChangeHandler, this, "isInfoLevelEnable");
			BindingUtils.bindSetter(levelChangeHandler, this, "isWarnLevelEnable");
			BindingUtils.bindSetter(levelChangeHandler, this, "isErrorLevelEnable");
			BindingUtils.bindSetter(levelChangeHandler, this, "isFatalLevelEnable");
		}

		/** @private ログを格納する配列. */
		private var logList:Vector.<LogData>=new Vector.<LogData>();

		[Bindable]
		/** DEBUG表示を有効化するフラグ. */
		public var isDebugLevelEnable:Boolean=true;

		[Bindable]
		/** INFO表示を有効化するフラグ. */
		public var isInfoLevelEnable:Boolean=true;

		[Bindable]
		/** WARN表示を有効化するフラグ. */
		public var isWarnLevelEnable:Boolean=true;

		[Bindable]
		/** ERROR表示を有効化するフラグ. */
		public var isErrorLevelEnable:Boolean=true;

		[Bindable]
		/** FATAL表示を有効化するフラグ. */
		public var isFatalLevelEnable:Boolean=true;

		[Bindable]
		/** ログを表示するリッチテキスト. */
		public var textFlow:TextFlow=new TextFlow();

		/** @private ローカルコネクション. */
		private var localConnection:LocalConnection=new LocalConnection();

		[Init]
		/**
		 * 初期化処理. 
		 */
		public function init():void
		{
			localConnection.allowDomain("*");
			localConnection.addEventListener(AsyncErrorEvent.ASYNC_ERROR, function(event:AsyncErrorEvent):void
				{
					log(LogEventLevel.ERROR, event.text);
				});
			localConnection.addEventListener(SecurityErrorEvent.SECURITY_ERROR, function(event:SecurityErrorEvent):void
				{
					log(LogEventLevel.ERROR, event.text);
				});
			localConnection.client=this;

			try
			{
				localConnection.connect("_LocalConnectionTarget");
			}
			catch (e:Error)
			{
				textFlow.addChild(createStanardElement(CONNECTION_ERROR, 0xFF0000));
			}
		}

		[Destroy]
		/**
		 * 終了処理. 
		 */
		public function destroy():void
		{
			localConnection.close();
		}

		/**
		 * ログを消去する.
		 */
		public function clear():void
		{
			logList=new Vector.<LogData>();
			flushLog();
		}
		
		/**
		 * ログを追加する。ローカルコネクションで外部から呼ばれる. 
		 * @param logLevel ログレベル @see mx.logging.LogEventLevel
		 * @param message ログメッセージ
		 * 
		 */
		public function log(logLevel:uint, message:String):void
		{
			var logData:LogData = new LogData(logLevel, message)
			logList.push(logData);
			dispatchEvent(LogViewerEvent.newAddingLogViewerEvent(logData));
			flushLog();
			dispatchEvent(LogViewerEvent.newAddedLogViewerEvent(logData));
		}
		
		/**
		 * @private
		 * レベル変更を反映する.
		 * @param level ログレベル @see mx.logging.LogEventLevel
		 * 
		 */
		private function levelChangeHandler(level:int):void
		{
			flushLog();
		}

		/**
		 * @private 
		 * ログデータをテキストの書き下す.
		 */
		private function flushLog():void
		{
			var logText:TextFlow=new TextFlow();
			var levelCol:int;
			for each (var logData:LogData in logList)
			{
				if (!((isDebugLevelEnable && logData.level == LogEventLevel.DEBUG) || (isInfoLevelEnable && logData.level == LogEventLevel.INFO) || (isWarnLevelEnable && logData.level == LogEventLevel.WARN) || (isErrorLevelEnable && logData.level == LogEventLevel.ERROR) || (isFatalLevelEnable && logData.level == LogEventLevel.FATAL)))
					continue;

				levelCol=0x000000;
				switch (logData.level)
				{
					case LogEventLevel.DEBUG:
						levelCol=0x5E885E;
						break;
					case LogEventLevel.INFO:
						levelCol=0x00689D;
						break;
					case LogEventLevel.WARN:
						levelCol=0x995522;
						break;
					case LogEventLevel.ERROR:
						levelCol=0xDD5500;
						break;
					case LogEventLevel.FATAL:
						levelCol=0xFF0000;
						break;
				}
				logText.addChild(createStanardElement(logData.message, levelCol));
			}
			textFlow=logText;
		}
	}
}