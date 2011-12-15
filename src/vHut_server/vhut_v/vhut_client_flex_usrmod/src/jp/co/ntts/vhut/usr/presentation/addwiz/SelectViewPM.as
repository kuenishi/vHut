/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.usr.presentation.addwiz
{
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.comp.wiz.domain.IInitiator;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;
	import jp.co.ntts.vhut.form.application.SearchEvent;
	import jp.co.ntts.vhut.usr.domain.EditingVhutUsers;
	import jp.co.ntts.vhut.usr.domain.UnregisteredVhutUsers;

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
	public class SelectViewPM extends EventDispatcher implements IValidator,IInitiator
	{
		public function SelectViewPM()
		{
		}

		[Inject]
		[Bindable]
		public var editingVhutUsers:EditingVhutUsers;

		[Inject]
		[Bindable]
		public var unregisteredVhutUsers:UnregisteredVhutUsers;

		public function initiate():void
		{
			unregisteredVhutUsers.clearUnregisteredVhutUsersFilter();
			editingVhutUsers.clearTargetVhutUsersFilter();
		}

		public function get isValid():Boolean
		{
			editingVhutUsers.clearTargetVhutUsersFilter();
			return editingVhutUsers.targetVhutUsers.length != 0;
		}

		public function searchTargetVhutUsersHandler(event:SearchEvent):void
		{
			editingVhutUsers.setTargetVhutUsersfilter(event.keywords);
		}

		public function searchUnregisteredVhutUsersHandler(event:SearchEvent):void
		{
			unregisteredVhutUsers.setUnregisteredVhutUsersfilter(event.keywords);
		}
	}
}