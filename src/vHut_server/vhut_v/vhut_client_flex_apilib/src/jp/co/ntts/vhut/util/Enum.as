/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import flash.utils.IDataInput;
	import flash.utils.getQualifiedClassName;

	/**
	 * JavaのEnumクラスを変換する際のAS側の基底クラス
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
	public class Enum {
		private var _name:String;
		private var _index:uint;

		function Enum(name:String, restrictor:*) {
			_name = (restrictor is Restrictor ? name : constantOf(name).name);
			_index = getConstants().indexOf(name);
		}

		public function set name(value:String):void
		{
			_name = constantOf(value).name
		}

		public function get name():String
		{
			return _name;
		}

		public function get index():uint
		{
			return _index;
		}

		protected function getConstants():Array {
			throw new Error("Should be overriden");
		}

		protected function constantOf(name:String):Enum {
			for each (var o:* in getConstants()) {
				var enum:Enum = Enum(o);
				if (enum.name == name)
					return enum;
			}
			throw new ArgumentError("Invalid " + getQualifiedClassName(this) + "name: " + name);
		}

		protected function indexOf(i:uint):Enum {
			if (getConstants().length <= i)
			{
				throw new ArgumentError("Invalid " + getQualifiedClassName(this) + "index: " + i);
			}
			return getConstants()[i] as Enum;
		}

		public static function readEnum(input:IDataInput):Enum {
			var tmp:Enum = input.readObject() as Enum;
			return (tmp == null ? null : tmp.constantOf(tmp.name));
		}

		public function toString():String {
			return name;
		}

		public function equals(other:Enum):Boolean {
			return other === this || (
				other != null &&
				getQualifiedClassName(this) == getQualifiedClassName(other) &&
				other.name == this.name
			);
		}

		public function isIncludedIn(...args:Array):Boolean {
			for each(var other:Enum in args)
			{
				if(equals(other))
				{
					return true;
				}
			}
			return false;
		}

		protected static function get _():Restrictor {
			return new Restrictor();
		}
	}
}
class Restrictor {}