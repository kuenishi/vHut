/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.infrastructure
{
	import flash.utils.getQualifiedClassName;

	import jp.co.ntts.vhut.util.Enum;

	import mx.rpc.events.FaultEvent;

	/**
	 * サーバに実装されている例外のクラス名を管理します.
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
	public class ServerException extends Enum
	{
		public static const NONE:ServerException = new ServerException(
			"NONE"
			,""
			,_);

		/** 認証エラー */
		public static const AUTHENTICATION:ServerException = new ServerException(
			"AUTHENTICATION"
			,"jp.co.ntts.vhut.exception.AuthenticationException"
			,_);

		public static function valueFromFaultEvent(event:FaultEvent):ServerException
		{
			var clazz:String = event.fault.faultString.split(" : ")[0];
			return ServerException(NONE.serverClassOf(clazz));
		}

		/** サーバ上の例外クラス */
		public function get serverClass():String
		{
			return _serverClass;
		}
		private var _serverClass:String;

		function ServerException(value:String = null, serverClass:String=null ,restrictor:* = null) {
			super((value || NONE.name), restrictor);
			_serverClass = serverClass;
		}

		override protected function getConstants():Array {
			return constants;
		}

		public static function get constants():Array {
			return [NONE, AUTHENTICATION];
		}

		public static function valueOf(name:String):ServerException {
			return ServerException(NONE.constantOf(name));
		}

		protected function serverClassOf(clazz:String):ServerException {
			for each (var o:* in getConstants()) {
				var serverException:ServerException = ServerException(o);
				if (serverException.serverClass == clazz)
					return serverException;
			}
			throw new ArgumentError("Invalid " + getQualifiedClassName(this) + "serverException: " + clazz);
		}
	}
}