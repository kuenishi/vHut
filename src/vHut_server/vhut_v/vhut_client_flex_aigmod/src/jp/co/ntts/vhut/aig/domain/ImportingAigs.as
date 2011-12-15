/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.aig.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	import flash.xml.XMLNode;

	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.entity.VhutUserStatus;
	import jp.co.ntts.vhut.util.DateUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	/**
	 * <p>
	 * <br>
	 * <p>
	 * <ul>
	 * <li>
	 * </ul>

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
	public class ImportingAigs extends EventDispatcher
	{
		public static const CHANGE_USERS:String = "changeUsers";
		public static const CHANGE_AIGS:String = "changeAigs";

		public function ImportingAigs(target:IEventDispatcher=null)
		{
			//TODO: implement function
			super(target);
		}

		[Inject]
		[Bindable]
		public var session:Session;

		[Bindable("changeUsers")]
		/** 未登録ユーザ */
//		public function set notregisteredUserList(value:IList):void
//		{
//			var user:VhutUser
//			var userMap:Dictionary = new Dictionary();
//			for each(user in _userList)
//			{
//				if(user.account != null)
//				{
//					userMap[user.account] = user;
//					user.status = VhutUserStatus.REGISTERED;
//				}
//			}
//
//			for each(user in value)
//			{
//				var localUser:VhutUser = userMap[user.account] as VhutUser;
//				if(localUser != null)
//				{
//					localUser.status = VhutUserStatus.NOTREGISTERED;
//				}
//			}
//		}
		public function get notregisteredUserList():IList
		{
			var list:ArrayCollection = new ArrayCollection();

			for each (var user:VhutUser in _userList)
			{
				if(user.account != null && !user.status.equals(VhutUserStatus.REGISTERED))
				{
					list.addItem(user);
				}
			}

			return list;
		}

		public function addRegisteredUsers(value:IList):void
		{
			var user:VhutUser

			for each(user in value)
			{
				var localUser:VhutUser = _userMap[user.account] as VhutUser;
				if(localUser != null)
				{
					localUser.id = user.id;
					localUser.status = VhutUserStatus.REGISTERED;
				}
			}

			dispatchEvent(new Event(CHANGE_USERS));
		}

		[Bindable("changeUsers")]
		/** ユーザのリスト */
		public function get userList():IList
		{
			return _userList;
		}
		private var _userList:IList;


		[Bindable("changeAigs")]
		/** アプリケーションインスタンスグループのリスト */
		public function get aigList():IList
		{
			return _aigList;
		}

		private var _aigList:IList;


		private var _userMap:Dictionary;
		/**
		 * XMLデータからインポート対象データを作成します.
		 * @param xml XMLデータ
		 */
		public function importXml(xml:XML):void
		{
			_userList = new ArrayCollection();
			_aigList = new ArrayCollection();

			_userMap = new Dictionary();

			for each(var userNode:XML in xml.users.user)
			{
				var user:VhutUser = new VhutUser();
				user.account = userNode.@account;
				user.firstName = userNode.@firstName;
				user.lastName = userNode.@lastName;
				user.email = userNode.@email;
				user.status = VhutUserStatus.NONE;
				_userMap[userNode.@id.toString()] = user;
				_userList.addItem(user);
			}

			for each(var aigNode:XML in xml.aigs.aig)
			{
				var aig:ApplicationInstanceGroup = new ApplicationInstanceGroup();
				aig.name = aigNode.@name;
				aig.startTime = DateUtil.parseDateString(aigNode.@startDate);
				aig.endTime = DateUtil.parseDateString(aigNode.@endDate);
				aig.deleteTime = DateUtil.parseDateString(aigNode.@deleteDate);
				aig.vhutUser = session.user;
				aig.vhutUserId = session.user.id;
				for each(var userRefNode:XML in aigNode.users.user)
				{
					var userRef:VhutUser = _userMap[userRefNode.@id.toString()] as VhutUser;
					if(userRef != null)
					{
						aig.addInstance(ApplicationInstance.newAiWithUser(userRef));
					}
				}
				_aigList.addItem(aig);
			}
			dispatchEvent(new Event(CHANGE_USERS));
			dispatchEvent(new Event(CHANGE_AIGS));
		}

		[Bindable("changeUsers")]
		/** すべてのユーザが登録済みです. */
		public function get isAllUsersRegistered():Boolean
		{
//			return true;
			return notregisteredUserList.length == 0;
		}

		[Bindable("changeAigs")]
		/** すべてのアプリケーションインスタンスグループにアプリケーションが割り当てられている. */
		public function get isAllAiOnAigDefined():Boolean
		{
			for each(var aig:ApplicationInstanceGroup in aigList)
			{
				if(aig.applicationId <= 0 || isNaN(aig.applicationId))
				{
					return false;
				}
			}
			return true;
		}

		[Bindable("changeAigs")]
		/** すべてのアプリケーションインスタンスグループに所有者が割り当てられている. */
		public function get isAllUserOnAigDefined():Boolean
		{
			for each(var aig:ApplicationInstanceGroup in aigList)
			{
				if(aig.vhutUserId <= 0 || isNaN(aig.vhutUserId))
				{
					return false;
				}
			}
			return true;
		}
	}
}