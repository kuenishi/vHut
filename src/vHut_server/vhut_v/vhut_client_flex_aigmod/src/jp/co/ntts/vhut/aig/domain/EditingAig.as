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

	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.domain.Session;
	import jp.co.ntts.vhut.entity.ApplicationInstance;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	import jp.co.ntts.vhut.entity.VhutUser;
	import jp.co.ntts.vhut.util.CollectionUtil;
	import jp.co.ntts.vhut.util.DateUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.utils.ObjectUtil;

	[Event(name="getAllUser", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[ManagedEvents(names="getAllUser")]
	/**
	 * 編集中のアプリケーションインスタンスグループ
	 * <p>　
	 *
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
	public class EditingAig extends EventDispatcher
	{
		/**  ユーザリストを更新しました */
		public static const UPDATE_USERS:String = "updateUsers"
		/** ターゲットを変更しました */
		public static const CHANGE_TARGET:String = "changeTarget"

		public static const FIELDS_USER:Array = ["account", "fullName"]

		public function EditingAig(target:IEventDispatcher=null)
		{
			//TODO: implement function
			super(target);
		}

		[Inject]
		[Bindalbe]
		public var aigs:Aigs;

		[Inject]
		[Bindable]
		public var apps:Apps;

		[Inject]
		[Bindable]
		public var session:Session;

		/**
		 * アプリケーションを新規に作成します.
		 * <p> 新規に作成されたアプリケーションはターゲットにセットされます.
		 * @return
		 */
		public function setNewAig():ApplicationInstanceGroup
		{
			targetAig=new ApplicationInstanceGroup();
			assignCurrentUserAsOwner();
			assignDefaultStartEndDeleteTime();
			return targetAig;
		}

		/**
		 * アプリケーションを編集します.
		 * <p> 新規に作成されたアプリケーションはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setRegisteredAig(source:ApplicationInstanceGroup=null):ApplicationInstanceGroup
		{
			if (source == null && aigs.targetAig != null)
			{
				source=aigs.targetAig;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			if (source.id <= 0)
			{
				throw new Error("source is not registered.");
			}
			targetAig=ObjectUtil.copy(source) as ApplicationInstanceGroup;
			assignCurrentUserAsOwner();
			return targetAig;
		}

		/**
		 * アプリケーションを複製します.
		 * <p> 新規に作成されたアプリケーションはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setClonedAig(source:ApplicationInstanceGroup):ApplicationInstanceGroup
		{
			if (source == null && aigs.targetAig != null)
			{
				source=aigs.targetAig;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			targetAig=ObjectUtil.copy(source) as ApplicationInstanceGroup;
			assignCurrentUserAsOwner();
			assignDefaultStartEndDeleteTime();
			return targetAig;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetAig
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeTarget")]
		/** 編集対象のアプリケーションインスタンスグループ */
		public function set targetAig(value:ApplicationInstanceGroup):void
		{
			_targetAig = value;
			updateRegisteredUser();
			dispatchEvent(new Event(CHANGE_TARGET));
		}
		public function get targetAig():ApplicationInstanceGroup
		{
			return _targetAig;
		}
		private var _targetAig:ApplicationInstanceGroup;

		public function set applicationId(value:Number):void
		{
			if(targetAig) targetAig.applicationId = value;
		}
		public function get applicationId():Number
		{
			return targetAig.applicationId;
		}


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  User
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * サーバからユーザ一覧を取得します.
		 */　
		public function getAllUsers():void
		{
			dispatchEvent(GetAllEvent.newGetAllUserEvent());
		}

		[Bindable("updateUsers")]
		/** 未登録ユーザ */
		public function set userList(value:IList):void
		{
			_userList = new ArrayCollection(value.toArray());
			_userList.filterFunction = function(value:Object):Boolean
			{
				var user:VhutUser = value as VhutUser
				if(_registeredUserMap[user.id] == null)
				{
					if (notRegisteredUserKeywords && notRegisteredUserKeywords.length > 0)
					{
						return CollectionUtil.multiFilter(value, notRegisteredUserFields, notRegisteredUserKeywords);
					}
					return true;
				}
				return false;
			}
			_userList.refresh();
			dispatchEvent(new Event(UPDATE_USERS))
		}
		public function get userList():IList
		{
			return _userList;
		}
		private var _userList:ArrayCollection = new ArrayCollection();

		private var _registeredUserMap:Dictionary = new Dictionary();

		public function filterNotRegisteredUser(keywords:Array, fields:Array=null):void
		{
			notRegisteredUserKeywords = keywords;
			if(fields)
			{
				notRegisteredUserFields = fields;
			}
			else
			{
				notRegisteredUserFields = FIELDS_USER;
			}
			_userList.refresh();
		}

		protected var notRegisteredUserFields:Array = new Array();
		protected var notRegisteredUserKeywords:Array = new Array();

		[Bindable("updateUsers")]
		public function get registeredUserList():IList
		{
			return _registeredUserList;
		}

		private var _registeredUserList:ArrayCollection;

		protected function updateRegisteredUser():void
		{
			if(targetAig == null) return;
			_registeredUserMap = new Dictionary();
			_registeredUserList = new ArrayCollection();
			for each(var ai:ApplicationInstance in targetAig.applicationInstanceList)
			{
				_registeredUserMap[ai.vhutUserId] = ai;
				_registeredUserList.addItem(ai.vhutUser);
			}
			_registeredUserList.filterFunction = function(item:Object):Boolean
			{
				if(registeredUserKeywords && registeredUserKeywords.length > 0)
				{
					return CollectionUtil.multiFilter(item, registeredUserFields, registeredUserKeywords);
				}
				return true;
			}
			_registeredUserList.refresh();
			_userList.refresh();
			dispatchEvent(new Event(UPDATE_USERS));
		}

		public function registerUser(user:VhutUser):void
		{
			if(_registeredUserMap[user.id] != null) return;
			var ai:ApplicationInstance = ApplicationInstance.newAiWithUser(user);
			targetAig.addInstance(ai);
			updateRegisteredUser();
		}

		public function unregisterUser(user:VhutUser):void
		{
			if(user == null) return;
			var ai:ApplicationInstance = _registeredUserMap[user.id] as ApplicationInstance;
			targetAig.removeInstance(ai);
			updateRegisteredUser();
		}

		protected function assignCurrentUserAsOwner():Boolean
		{
			if(session == null || session.user == null)
				return false;
			if(targetAig == null)
				return false;

			targetAig.vhutUser = session.user;
			targetAig.vhutUserId = session.user.id;

			return true;
		}

		protected function assignDefaultStartEndDeleteTime():void
		{
			var now:Date = new Date();
			var endDate:Date = DateUtil.add(now, 7, DateUtil.UNIT_DATE);
			var deleteDate:Date = DateUtil.add(now, 14, DateUtil.UNIT_DATE);
			targetAig.startTime = new Date(now.fullYear, now.month, now.date);
			targetAig.endTime = new Date(endDate.fullYear, endDate.month, endDate.date);
			targetAig.deleteTime = new Date(deleteDate.fullYear, deleteDate.month, deleteDate.date);
		}

		public function filterRegisteredUser(keywords:Array, fields:Array=null):void
		{
			registeredUserKeywords = keywords;
			if(fields)
			{
				registeredUserFields = fields;
			}
			else
			{
				registeredUserFields = FIELDS_USER;
			}
			_registeredUserList.refresh();
		}
		protected var registeredUserKeywords:Array = new Array();
		protected var registeredUserFields:Array = new Array();

	}
}