/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.RightAction;
	import jp.co.ntts.vhut.entity.RightLevel;

	/**
	 * 権限を表示するためのデータ
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class RightItem extends EventDispatcher
	{

		public function RightItem(action:RightAction, group:RightGroup)
		{
			super(null);
			_action = action;
			_group = group;
			_order = RightAction.constants.indexOf(action);
		}

		public function get action():RightAction
		{
			return _action;
		}
		private var _action:RightAction;

		public function get group():RightGroup
		{
			return _group;
		}
		private var _group:RightGroup;

		public function get order():uint
		{
			return _order;
		}
		private var _order:uint;

		public function updateRight():void
		{
			dispatchEvent(new Event("rightUpdated"));
		}

		//-------------------------------------------------
		// MASK
		//-------------------------------------------------

		public function updateMask():void
		{
			// SYS
			if (group.getRight(action, RightLevel.SYS))
			{
				_sys = 1;
			}
			else
			{
				_sys = 0;
			}
			// OWN
			if (group.getRight(action, RightLevel.OWN))
			{
				_own = 1;
			}
			else
			{
				_own = 0;
			}
			// CHILD
			if (group.getRight(action, RightLevel.CHILD))
			{
				_child = 1;
			}
			else
			{
				_child = 0;
			}
			// ALL
			if (group.getRight(action, RightLevel.ALL))
			{
				_all = 1;
			}
			else
			{
				_all = 0;
			}
		}

		public function clearMask():void
		{
			// SYS
			if (group.getRight(action, RightLevel.SYS))
			{
				_sys = 1;
			}
			else
			{
				_sys = -1;
			}
			// OWN
			if (group.getRight(action, RightLevel.OWN))
			{
				_own = 1;
			}
			else
			{
				_own = -1;
			}
			// CHILD
			if (group.getRight(action, RightLevel.CHILD))
			{
				_child = 1;
			}
			else
			{
				_child = -1;
			}
			// ALL
			if (group.getRight(action, RightLevel.ALL))
			{
				_all = 1;
			}
			else
			{
				_all = -1;
			}
		}

		//-------------------------------------------------
		// SYS
		//-------------------------------------------------

		[Bindable("rightUpdated")]
		public function set sys(value:int):void
		{
			if (value == _sys)
				return;

			if (value > 0)
				_sys = 1;
			else if (value < 0)
				_sys = -1;
			else
				_sys = 0;

			group.setRight(action, RightLevel.SYS, _sys > 0);
		}
		public function get sys():int
		{
			if (_sys == 0)
			{
				return 0;
			}
			else
			{
				try {
					if (group.getRight(action, RightLevel.SYS))
					{
						_sys = 1;
					}
					else
					{
						_sys = -1;
					}
				} catch (e:Error) {
					_sys = 0;
				}
			}
			return _sys;
		}
		private var _sys:int = -1;
		//-------------------------------------------------
		// OWN
		//-------------------------------------------------

		[Bindable("rightUpdated")]
		public function set own(value:int):void
		{
			if (value == _own)
				return;

			if (value > 0)
				_own = 1;
			else if (value < 0)
				_own = -1;
			else
				_own = 0;

			group.setRight(action, RightLevel.OWN, _own > 0);
		}
		public function get own():int
		{
			if (_own == 0)
			{
				return 0;
			}
			else
			{
				try {
					if (group.getRight(action, RightLevel.OWN))
					{
						_own = 1;
					}
					else
					{
						_own = -1;
					}
				} catch (e:Error) {
					_own = 0;
				}
			}
			return _own;
		}
		private var _own:int = -1;
		//-------------------------------------------------
		// CHILD
		//-------------------------------------------------

		[Bindable("rightUpdated")]
		public function set child(value:int):void
		{
			if (value == _child)
				return;

			if (value > 0)
				_child = 1;
			else if (value < 0)
				_child = -1;
			else
				_child = 0;

			group.setRight(action, RightLevel.CHILD, _child > 0);
		}
		public function get child():int
		{
			if (_child == 0)
			{
				return 0;
			}
			else
			{
				try {
					if (group.getRight(action, RightLevel.CHILD))
					{
						_child = 1;
					}
					else
					{
						_child = -1;
					}
				} catch (e:Error) {
					_child = 0;
				}
			}
			return _child;
		}
		private var _child:int = -1;
		//-------------------------------------------------
		// ALL
		//-------------------------------------------------

		[Bindable("rightUpdated")]
		public function set all(value:int):void
		{
			if (value == _all)
				return;

			if (value > 0)
				_all = 1;
			else if (value < 0)
				_all = -1;
			else
				_all = 0;

			group.setRight(action, RightLevel.ALL, _all > 0);
		}
		public function get all():int
		{
			if (_all == 0)
			{
				return 0;
			}
			else
			{
				try {
					if (group.getRight(action, RightLevel.ALL))
					{
						_all = 1;
					}
					else
					{
						_all = -1;
					}
				} catch (e:Error) {
					_all = 0;
				}
			}
			return _all;
		}
		private var _all:int = -1;
	}
}