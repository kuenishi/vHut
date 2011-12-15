/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.usr.presentation.wiz
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.comp.wiz.domain.IInitiator;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.usr.domain.EditingVhutUser;

	/**
	 * <p>ユーザ編集ウィザードの
	 * プロパティを入力する画面のPM
	 * <p></p>
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
	public class PropViewPM extends EventDispatcher implements IValidator,IInitiator
	{
		public function PropViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var editingVhutUser:EditingVhutUser;

		public function initiate():void
		{
		}

		public function get isValid():Boolean
		{
			return true;
		}
	}
}