/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	import org.osmf.traits.SwitchableTrait;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Command")]
	/**
	 * Command Entity Class.
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
    public class Command extends CommandBase {
		/** ステータスの変更 */
		public static const CHANGE_STATUS:String = "changeStatus";
		/** ターゲットの変更 */
		public static const CHANGE_TARGET:String = "changeTarget";
		
		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}
		
		/////////////////////////////////////////////////////////////////////////////////
		//
		//  STATUS
		//
		/////////////////////////////////////////////////////////////////////////////////
		
		[Bindable("changeStatus")]
		/**
		 * @return リトライ可能です.
		 */
		public function get canRetry():Boolean
		{
			return !status.isIncludedIn(CommandStatus.WAITING, 
				CommandStatus.SUCCESS);
		}
		
		[Bindable("changeStatus")]
		/**
		 * @return キャンセル可能です.
		 */
		public function get canCancel():Boolean
		{
			return !status.isIncludedIn(CommandStatus.WAITING, 
				CommandStatus.SUCCESS,
				CommandStatus.CANCELED);
		}
		
		override public function set status(value:CommandStatus):void
		{
			super.status = value;
			dispatchEvent(new Event(CHANGE_STATUS));
		}
		
		/////////////////////////////////////////////////////////////////////////////////
		//
		//  TARGET
		//
		/////////////////////////////////////////////////////////////////////////////////
		
		override public function set commandVmMapList(value:ArrayCollection):void {
			super.commandVmMapList = value;
			updateTarget();
		}
		
		override public function set commandTemplateMapList(value:ArrayCollection):void {
			super.commandTemplateMapList = value;
			updateTarget();
		}
		
		private function updateTarget():void
		{
			if(commandVmMapList && commandVmMapList.length)
			{
				_target = (commandVmMapList[0] as CommandVmMap).vm;
			}
			else if(commandTemplateMapList && commandTemplateMapList.length)
			{
				_target = (commandTemplateMapList[0] as CommandTemplateMap).template;
			}
			else
			{
				_target = null;
			}
			dispatchEvent(new Event(CHANGE_TARGET));
		}
		
		[Bindable("changeTarget")]
		public function get target():Object
		{
			return _target;
		}
		private var _target:Object;
		
    }
}