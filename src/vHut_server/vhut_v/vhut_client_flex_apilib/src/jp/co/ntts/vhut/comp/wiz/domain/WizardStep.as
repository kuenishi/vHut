/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.wiz.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import mx.binding.utils.BindingUtils;

	/**
	 * Wizardにセットするプロセスを表現します.
	 * <br>WizardStepsの要素です.
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
	public class WizardStep extends EventDispatcher
	{
		/** 検証結果の変更があります. */
		public static const CHANGE_VALIDATION:String = "changeValidation";

		/** Navigationに使用するマーカーです. */
		[Inspectable(category="General", dafaultValue="", format="String")]
		public var destination:String;
		/** ステップ名称. */
		[Inspectable(category="General", dafaultValue="", format="String")]
		public var name:String;
		/** 次に遷移するためのvalidatorです. */
		[Inspectable(category="General", dafaultValue="", format="Class")]
		public function set validator(value:IValidator):void
		{
			if(!value || value==_validator) return;
			_validator = value;
			BindingUtils.bindSetter(
				function(value:Object):void
				{
					dispatchEvent(new Event(CHANGE_VALIDATION));
				}
				, _validator, "isValid"
			);
		}
		public function get validator():IValidator
		{
			return _validator;
		}
		private var _validator:IValidator;

		/** 現在のステップが完了している. */
		[Inspectable(category="General", dafaultValue="", format="Class")]
		public var initiator:IInitiator;
		/** 初期化する. */
		public function initiate():void
		{
			if(initiator != null) {
				initiator.initiate();
			}
		}
		[Bindable("changeValidation")]
		/** 現在のステップが完了している. */
		public function get isValid():Boolean
		{
			if(validator != null) {
				return validator.isValid;
			} else {
				return true;
			}
		}

		[Bindable]
		public var isCurrent:Boolean = false;
	}
}