/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import flash.events.Event;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.core.BitArray;
	import jp.co.ntts.vhut.core.domain.RightGroup;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Role")]
	/**
	 * Role Entity Class.
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
    public class Role extends RoleBase implements IRightOwner
	{

		public function Role()
		{
			_rights = new ByteArray();
			_rightBits = BitArray.newFromByteArray(_rights);
			_rightGroups = RightGroup.createRightGroups(_rightBits);
			for each (var rightGroup:RightGroup in _rightGroups)
			{
				if(rightGroup)
					rightGroup.addEventListener(Event.CHANGE, rightGroupChangeHandler);
			}
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		////////////////////////////////////////////////////////////////////
		//
		//  Rights.
		//
		////////////////////////////////////////////////////////////////////

		[Bindable("rightsChanged")]
		override public function get rights():ByteArray {
			return _rights;
		}
		override public function set rights(value:ByteArray):void {
//			var fingerPrint:String = "";
//			for (var i:uint=0; i<value.length; i++)
//			{
//				fingerPrint+=value[i] + ", ";
//			}
//			trace (fingerPrint);
			_rights = value;
			updateRights();
		}

		[Transient]
		public function get rightBits():BitArray
		{
			return _rightBits;
		}
		private var _rightBits:BitArray;

		[Transient]
		public function get rightsMap():Dictionary
		{
			return _rightsMap;
		}
		private var _rightsMap:Dictionary;

		[Transient]
		public function get rightGroups():IList
		{
			return _rightGroups;
		}
		private var _rightGroups:IList;

		private function updateRights():void
		{
			_rightBits = BitArray.newFromByteArray(_rights);
			_rightsMap = createRightsMap(_rightBits);
			RightGroup.updateRightGroup(_rightGroups, _rightBits);
		}

		private function createRightsMap(bits:BitArray):Dictionary
		{
			var rightsMap:Dictionary = new Dictionary();
			for (var i:uint=0; i<Right.constants.length; i++)
			{
				var right:Right = Right.constants[i] as Right;
				var value:Boolean = bits.getAt(i);
				if (value)
					rightsMap[right] = value;
				else
					rightsMap[right] = false;
			}
			return rightsMap;
		}

		private function rightGroupChangeHandler(event:Event):void
		{
			_rights = rightBits.toByteArray();
			dispatchEvent(new Event("rightsChanged"));
		}

    }
}