/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.rol.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import jp.co.ntts.vhut.entity.Role;
	
	import mx.utils.ObjectUtil;
	
	/**
	 * 編集中ロールの管理クラス.
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
	public class EditingRole extends EventDispatcher
	{
		/** ターゲットを変更しました */
		public static const CHANGE_TARGET:String = "changeTarget"
		
		public function EditingRole(target:IEventDispatcher=null)
		{
			super(target);
		}
		
		[Inject]
		[Bindalbe]
		public var roles:Roles;
		
		/**
		 * ロールを新規に作成します.
		 * <p> 新規に作成されたロールはターゲットにセットされます.
		 * @return
		 */
		public function setNewRole():Role
		{
			targetRole=new Role();
			return targetRole;
		}
		
		/**
		 * ロールを複製します.
		 * <p> 新規に作成されたロールはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setRegisteredRole(source:Role=null):Role
		{
			if (source == null && roles.targetRole != null)
			{
				source=roles.targetRole;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			if (source.id <= 0)
			{
				throw new Error("source is not registered.");
			}
			targetRole=ObjectUtil.copy(source) as Role;
			return targetRole;
		}
		
		/**
		 * ロールを複製します.
		 * <p> 新規に作成されたロールはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setClonedRole(source:Role=null):Role
		{
			if (source == null && roles.targetRole != null)
			{
				source=roles.targetRole;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			if (source.id <= 0)
			{
				throw new Error("source is not registered.");
			}
			targetRole=ObjectUtil.copy(source) as Role;
			return targetRole;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetRole
		//
		///////////////////////////////////////////////////////////////////////////////////////
		
		[Bindable("changeTarget")]
		/** 編集対象のロールインスタンスグループ */
		public function set targetRole(value:Role):void
		{
			_targetRole = value;
			dispatchEvent(new Event(CHANGE_TARGET));
		}
		public function get targetRole():Role
		{
			return _targetRole;
		}
		private var _targetRole:Role;
	}
}