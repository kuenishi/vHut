/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.spicefactory.lib.reflect.ClassInfo;
	import org.spicefactory.lib.reflect.Property;

	/**
	 * 複製に関連するユーティリティです.
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
	public class CloneUtil
	{
		public static var classInfoCache:Dictionary = new Dictionary();
		
		public static function cloneCollection(list:ArrayCollection, withId:Boolean = true, deep:Boolean = true, ref:Dictionary = null):ArrayCollection
		{
			if(!list) return null;
			var result:ArrayCollection = new ArrayCollection();
			for each(var element:IClonable in list)
			{
				if(element)
				{
					result.addItem(element.clone(withId, deep, ref));
				}
			}
			return result;
		}
		
		public static function updateProperties(target:Object, source:Object, className:String = null):void
		{
			if(!className)
			{
				var targetClassName:String = flash.utils.getQualifiedClassName(target);
				var sourceClassName:String = flash.utils.getQualifiedClassName(source);
				
				if(targetClassName != sourceClassName)
				{
					throw Error("Source and Target object has different class name.");
				}
				
				className = targetClassName;
			}
			
			var info:ClassInfo = getClassInfo(className);
			
			for each (var property:Property in info.getProperties())
			{
				if (property.readable && property.writable)
				{
					property.setValue(target, property.getValue(source));
				}
			}

		}
		
		private static function getClassInfo(className:String):ClassInfo
		{
			if (!classInfoCache[className])
			{
				classInfoCache[className] = ClassInfo.forName(className);
			}
			
			return classInfoCache[className] as ClassInfo;
		}
	}
}