/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.form.presentation
{
	import spark.components.Label;
	import spark.components.SkinnableContainer;
	
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
	public class KeyValueDescriptionForm extends KeyValueForm
	{
		public function KeyValueDescriptionForm()
		{
			super();
			setStyle("skinClass", KeyValueDescriptionFormSkin);
		}
		
		[SkinPart(required="false")]
		public var descriptionLabel:Label;
		
		public function set descriptionText(value:String):void
		{
			_descriptionText = value;
			if(descriptionLabel)
				descriptionLabel.text = _descriptionText +" :";
		}
		public function get descriptionText():String
		{
			return _descriptionText;
		}
		private var _descriptionText:String
		
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
			switch(instance)
			{
				case descriptionLabel:
					descriptionLabel.text = descriptionText+" :";
					break;
			}
		}
	}
}