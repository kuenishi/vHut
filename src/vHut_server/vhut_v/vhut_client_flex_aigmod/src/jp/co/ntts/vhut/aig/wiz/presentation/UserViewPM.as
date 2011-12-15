/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.aig.domain.EditingAig;
	import jp.co.ntts.vhut.comp.wiz.domain.IInitiator;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.form.application.SearchEvent;

	/**
	 * アプリケーションインスタンスグループ追加・複製・編集ウィザードの
 	 *	ユーザを選択する画面のPM
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
	public class UserViewPM extends EventDispatcher implements IValidator,IInitiator
	{
		public function UserViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var editingAig:EditingAig;


		public function initiate():void
		{
			editingAig.getAllUsers();
		}

		public function get isValid():Boolean
		{
			return true;
		}

		public function searchNotRegisteredUserHandler(event:SearchEvent):void
		{
			editingAig.filterNotRegisteredUser(event.keywords);
		}

		public function searchRegisteredUserHandler(event:SearchEvent):void
		{
			editingAig.filterRegisteredUser(event.keywords);
		}
	}
}