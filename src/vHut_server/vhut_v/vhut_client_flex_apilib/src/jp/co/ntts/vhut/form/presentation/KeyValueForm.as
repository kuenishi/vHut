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
	public class KeyValueForm extends SkinnableContainer
	{
		public function KeyValueForm()
		{
			super();
			setStyle("skinClass", KeyValueFormSkin);
		}

		[SkinPart(required="false")]
		public var keyLabel:Label;

		[SkinPart(required="false")]
		public var valueLabel:Label;

		/** 変数名のテキスト */
		public function set keyText(value:String):void
		{
			_keyText = value;
			if(keyLabel)
				keyLabel.text = _keyText +" :";
		}
		public function get keyText():String
		{
			return _keyText;
		}
		private var _keyText:String

		/** 値名のテキスト */
		public function set valueText(value:String):void
		{
			_valueText = value;
			_isValueLavelChanged = true;
			invalidateProperties();
		}
		public function get valueText():String
		{
			return _valueText;
		}
		private var _valueText:String

		/** 単位名のテキスト */
		public function set unitText(value:String):void
		{
			_unitText = value;
			_isValueLavelChanged = true;
			invalidateProperties();
		}
		public function get unitText():String
		{
			return _unitText;
		}
		private var _unitText:String

		protected var _isValueLavelChanged:Boolean = false;

		protected function updateValueTextLabel():void
		{
			if(valueLabel)
			{
				var text:String = valueText;
				if(unitText)
					text += " " + unitText;
				valueLabel.text = text;
			}
		}

		override protected function commitProperties():void
		{
			super.commitProperties();
			if(_isValueLavelChanged)
			{
				updateValueTextLabel();
				_isValueLavelChanged = false;
			}
		}

		override protected function partAdded(partName:String, instance:Object):void
		{
			switch(instance)
			{
				case keyLabel:
					keyLabel.text = keyText+" :";
					break;
				case valueLabel:
					updateValueTextLabel();
					break;
			}
		}
	}
}