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
	
	[Style(name="glassColor", type="uint", format="Color", inherit="yes", theme="spark")]
	[Style(name="glassAlpha", type="Number", inherit="yes", theme="spark")]
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
	public class BasicLabel extends SkinnableContainer
	{
		public function BasicLabel()
		{
			super();
			setStyle("skinClass", BasicLabelSkin);
		}
		
		[Inspectable(category="General", dafaultValue="", format="String")]
		[Bindable]
		public function set text(value:String):void
		{
			_text = value;
			if(label)
				label.text = value;
		}
		public function get text():String
		{
			return _text;
		}
		private var _text:String;
		
		[Inspectable(category="General", dafaultValue="", format="String")]
		[Bindable]
		public function set subText(value:String):void
		{
			_subText = value;
			if(subLabel)
				subLabel.text = value;
		}
		public function get subText():String
		{
			return _subText;
		}
		private var _subText:String;
		
		[SkinPart(required="false")]
		public var label:Label;
		
		[SkinPart(required="false")]
		public var subLabel:Label;
		
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
			if(instance == label)
			{
				if(text != null)
					label.text = text;
			}
			else if(instance == subLabel)
			{
				if(subText != null)
					subLabel.text = subText;
			}
		}
	}
}