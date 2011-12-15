/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.application
{
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.util.Enum;

	/**
	 * メインコンテンツのリスト.
	 * <p>Cairngorm3 Navigation Frameworkの一部</p>
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
	public class ContentDestination extends Enum
	{
		public static const CONTENT:String = "content";
		
		public static const SEPARATOR:String = ".";
		
		public static const TOP:ContentDestination=newContentDestination(ModuleName.TOP);
		
		public static const APP:ContentDestination=newContentDestination(ModuleName.APP);
		
		public static const AIG:ContentDestination=newContentDestination(ModuleName.AIG);
		
		public static const USR:ContentDestination=newContentDestination(ModuleName.USR);
		
		public static const ROL:ContentDestination=newContentDestination(ModuleName.ROL);
		
		public static const TMP:ContentDestination=newContentDestination(ModuleName.TMP);
		
		public static const MNG:ContentDestination=newContentDestination(ModuleName.MNG);
		
		public static const CNF:ContentDestination=newContentDestination(ModuleName.CNF);
		
		private static function newContentDestination(moduleName:String):ContentDestination
		{
			return new ContentDestination(CONTENT+SEPARATOR+moduleName, _);
		}
		
		function ContentDestination(value:String = null, restrictor:* = null) {
			super((value || TOP.name), restrictor);
		}
		
		override protected function getConstants():Array {
			return constants;
		}
		
		public static function get constants():Array {
			return [TOP, APP, AIG, USR, ROL, TMP, MNG, CNF];
		}
		
		public static function valueOf(name:String):ContentDestination {
			return ContentDestination(TOP.constantOf(name));
		}
		
		public function get moduleName():String
		{
			var elements:Array = name.split(SEPARATOR);
			return elements[elements.length-1];
		}
			
	}
}