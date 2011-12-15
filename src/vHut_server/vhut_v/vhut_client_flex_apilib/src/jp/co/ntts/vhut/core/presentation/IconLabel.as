/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.presentation
{
	import mx.states.OverrideBase;
	import mx.states.State;
	
	import spark.components.Group;
	import spark.components.Label;
	import spark.components.SkinnableContainer;
	import spark.primitives.BitmapImage;
	
	[Style(name="source", type="Class", inherit="no")]
	/**
	 * 
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
	public class IconLabel extends BasicLabel
	{
		public function IconLabel()
		{
			super();
			setStyle("skinClass", IconLabelSkin);
		}
		
		[Inspectable(category="General", dafaultValue="", format="String")]
		public function set url(value:String):void
		{
			_url = value;
			if(image)
				image.url = value;
		}
		public function get url():String
		{
			return _url;
		}
		private var _url:String;
		
		[SkinPart(required="false")]
		public var image:IconImage;
		
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
			if(instance == image)
			{
				if(url != null)
					image.url = url;
			}
		}
	}
}