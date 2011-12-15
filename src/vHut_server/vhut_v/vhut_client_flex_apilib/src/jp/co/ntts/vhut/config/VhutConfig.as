/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.config
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[ResourceBundle("vhut")]
	/**
	 * アプリケーションで使用する設定値をvhut.propertiesから取得するクラスです.
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
	public class VhutConfig extends EventDispatcher
	{
		public static const BUNDLE_NAME:String = "vhut";

		public function VhutConfig()
		{
			rm = ResourceManager.getInstance();
			load();
		}

		private var rm:IResourceManager;

		public function load():void
		{
			COMMAND_INTERVAL = getInt("command.interval", 10000, 1000, 20000);
			APP_INTERVAL = getInt("app.interval", 10000, 1000, 20000);
			AI_INTERVAL = getInt("api.interval", 10000, 1000, 20000);
			CORE_VERSION = getString("core.version");
			IMAGE_ROOT = getString("image.root");
			MANUAL_URL = getString("manual.url");
			dispatchEvent(new Event(Event.CHANGE));
		}

		[Bindable]
		/** 実行中コマンドの状態更新の周期 */
		public static var COMMAND_INTERVAL:int;

		[Bindable]
		/** アプリケーションの状態更新の周期*/
		public static var APP_INTERVAL:int;

		[Bindable]
		/** アプリケーションインスタンスの状態更新の周期*/
		public static var AI_INTERVAL:int;

		[Bindable]
		/** バージョン番号 */
		public static var CORE_VERSION:String;

		[Bindable]
		/** 画像のディレクトリ */
		public static var IMAGE_ROOT:String;

		[Bindable]
		/** マニュアルのディレクトリ */
		public static var MANUAL_URL:String;

		private function getString(resource:String):String
		{
			return rm.getString(BUNDLE_NAME, resource);
		}

		private function getInt(resource:String, defaultValue:int=0, min:int = int.MIN_VALUE, max:int = int.MAX_VALUE):int
		{
			var value:Object = rm.getObject(BUNDLE_NAME, resource);
			if(value)
			{
				var result:int = int(Number(value));
				result = Math.max(Math.min(result, max), min);
				return result;
			}
			else{
				return defaultValue;
			}
		}
	}
}