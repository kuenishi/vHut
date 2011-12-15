/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import mx.collections.ArrayCollection;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ApplicationInstanceGroup")]
	/**
	 * ApplicationInstanceGroup Entity Class.
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
    /**
     *
     * @author NTT Software Corporation.
     */
    public class ApplicationInstanceGroup extends ApplicationInstanceGroupBase {
		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set vhutUser(value:VhutUser):void
		{
			super.vhutUser = value;
			if (value)
			{
				vhutUserId = value.id;
			}
			else
			{
				vhutUserId = 0;
			}
		}

		/**
		 * アプリケーションインスタンスを追加する.
		 * @param ai
		 */
		public function addInstance(ai:ApplicationInstance):void
		{
			if(applicationInstanceList == null)
			{
				applicationInstanceList = new ArrayCollection();
			}
			applicationInstanceList.addItem(ai);
		}

		/**
		 * アプリケーションインスタンスを削除する.
		 * @param ai
		 */
		public function removeInstance(ai:ApplicationInstance):void
		{
			if(applicationInstanceList == null)
			{
				applicationInstanceList = new ArrayCollection();
			}
			var index:int = applicationInstanceList.getItemIndex(ai);
			if(index >= 0)
			{
				applicationInstanceList.removeItemAt(index);
			}
		}

		[Transient]
		/** ユーザの数 */
		public function get userCount():int
		{
			var result:int = 0;
			if(applicationInstanceList != null)
			{
				result = applicationInstanceList.length;
			}
			return result;
		}

		[Transient]
		/** アプリケーションの名称 */
		public function get applicationName():String
		{
			var result:String = "NotDefined";
			if(application != null)
			{
				result = application.name;
			}
			return result;
		}

		[Transient]
		/** リソースの状況 */
		public function get resourceStatus():ApplicationInstanceGroupResourceStatus
		{
			return _resourveStatus;
		}
		public function set resourceStatus(value:ApplicationInstanceGroupResourceStatus):void
		{
			_resourveStatus = value;
		}
		private var _resourveStatus:ApplicationInstanceGroupResourceStatus = ApplicationInstanceGroupResourceStatus.NONE;


    }
}