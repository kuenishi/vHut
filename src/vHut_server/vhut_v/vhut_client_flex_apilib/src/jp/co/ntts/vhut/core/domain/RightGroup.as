/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.core.BitArray;
	import jp.co.ntts.vhut.entity.Right;
	import jp.co.ntts.vhut.entity.RightAction;
	import jp.co.ntts.vhut.entity.RightLevel;
	import jp.co.ntts.vhut.entity.RightTarget;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;

	[Event(type="flash.events.Event", name="change")]
	/**
	 * 権限をグループ化します.
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
	public class RightGroup extends EventDispatcher
	{

		//------------------------------------------------
		// Static Methods
		//------------------------------------------------

		/**
		 * Right(Enum)を参照してRightGroupのリストを作ります.
		 * @param rightBits 権利ビットの初期値 nullの時はすべての権利なしで作成
		 * @return RightGroupのリスト
		 */
		public static function createRightGroups(rightBits:BitArray=null):IList
		{
			var action:RightAction = null;
			var level:RightLevel = null;
			var target:RightTarget = null;
			var rightGroup:RightGroup = null;

			var rightGroups:ArrayCollection = new ArrayCollection();
			var rightGroupMap:Dictionary = new Dictionary();

			if (rightBits == null)
			{
				rightBits = new BitArray(Right.constants.length);
			}

			for each (var right:Right in Right.constants)
			{
				//Rightをパースして action level targetを生成
				var elements:Array = right.name.split("_");
				action = RightAction.valueOf(elements[0]);
				level = RightLevel.valueOf(elements[1]);
				target = RightTarget.valueOf(elements[2]);
				//RightGroupの取得、ない時は作る
				rightGroup = rightGroupMap[target] as RightGroup;
				if (!rightGroup)
					rightGroup = rightGroupMap[target] = new RightGroup(target, rightBits);
				//action, level, rightBits の追加
				rightGroup.addAction(action);
				rightGroup.addLevel(level);
			}

			for each (target in RightTarget.constants)
			{
				rightGroup = rightGroupMap[target] as RightGroup;
				if (rightGroup)
				{
					rightGroup.rightBits = rightBits;
//					rightGroup.updateMask();
					rightGroups.addItem(rightGroup);
				}
			}

			return rightGroups;
		}

		/**
		 * 権利ビットを更新します.
		 * @param rightGroups RightGroupのリスト
		 * @param rightBits 権利ビット
		 */
		public static function updateRightGroup(rightGroups:IList, rightBits:BitArray):void
		{
			for each(var rightGroup:RightGroup in rightGroups)
			{
				rightGroup.rightBits = rightBits;
			}
		}

		//------------------------------------------------
		// RightGroup
		//------------------------------------------------

		/**
		 * コンストラクタ.
		 * @param target 操作対象
		 */
		public function RightGroup(target:RightTarget, rightBits:BitArray=null)
		{
			super(null);
			_target = target;
			_rightBits = rightBits;

			var sortItem:Sort = new Sort();
			sortItem.fields = [new SortField("order")];
			_items = new ArrayCollection();
			_items.sort = sortItem;
		}

		//------------------------------------------------
		// items: DataProvider for DataGrid
		//------------------------------------------------

		[Bindable("changeRightList")]
		/** RightItemのリスト（DataGrid等のdataProvider） */
		public function get items():IList
		{
			return _items;
		}
		private var _items:ArrayCollection;
		/** RightItemのリストの権限状態を更新します. */
		private function updateItems():void
		{
			if(!rightBits) return;
			var event:CollectionEvent = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
			event.kind = CollectionEventKind.RESET;
			event.items = [];
			items.dispatchEvent(event);
		}

		//------------------------------------------------
		// target
		//------------------------------------------------
		/** 操作対象 */
		public function get target():RightTarget
		{
			return _target;
		}
		private var _target:RightTarget;

		//------------------------------------------------
		// actions
		//------------------------------------------------
		/** 対象に設定されている操作のリスト */
		public function get actions():Array
		{
			if (!_actions)
				updateActions();
			return _actions;
		}
		private var _actions:Array;

		/** actionと権限要素のマップ(key=RightAction, value=RightItem) */
		private var _actionRightItemMap:Dictionary = new Dictionary();

		/** actionsを更新します. */
		private function updateActions():void
		{
			_actions = new Array();
			for each (var action:RightAction in RightAction.constants)
			{
				if (_actionRightItemMap[action])
					_actions.push(action);
			}
		}

		/**
		 * 操作を追加します.
		 * @param action 操作
		 * @return 対応するRightItem
		 *
		 */
		private function addAction(action:RightAction):RightItem
		{
			var item:RightItem = _actionRightItemMap[action] as RightItem;

			if (item)
				return item;

			item = new RightItem(action, this);
			_actionRightItemMap[action] = item;
			_items.addItem(item);
			_items.refresh();
			dispatchEvent(new Event("changeRightList"));

			_actions = null;

			return item;
		}

		//------------------------------------------------
		// levels
		//------------------------------------------------

		/** 対象に設定されているレベルのリスト */
		public function get levels():Array
		{
			if (!_levels)
				updateLevels();
			return _levels;
		}
		private var _levels:Array = new Array();

		/** levelとBoolのマップ(key=RightLevel, value=Boolean) */
		private var _levelBooleanMap:Dictionary = new Dictionary();

		/** levelsを更新します. */
		private function updateLevels():void
		{
			_levels = new Array();
			for each (var level:RightLevel in RightLevel.constants)
			{
				if (_levelBooleanMap[level])
					_levels.push(level);
			}
		}

		/**
		 * レベルを追加します.
		 * @param  レベル
		 */
		private function addLevel(level:RightLevel):void
		{
			if (!_levelBooleanMap[level])
			{
				_levelBooleanMap[level] = true;
				_levels = null;
			}
		}

		//------------------------------------------------
		// rightBits
		//------------------------------------------------

		/** 権限ビット */
		public function set rightBits(value:BitArray):void
		{
			_rightBits = value;
			updateItems();
		}
		public function get rightBits():BitArray
		{
			return _rightBits;
		}
		private var _rightBits:BitArray;

		/**
		 * 権限ビットから値を取得します
		 * @param action 操作
		 * @param level レベル
		 * @return bool
		 */
		public function getRight(action:RightAction, level:RightLevel):Boolean
		{
			if (!rightBits)
				return false;
			var right:Right = null;
			right = Right.valueOf(action.name + "_" + level.name + "_" + target.name);
			if (!right)
				return false;
			var order:uint = Right.constants.indexOf(right);
			if (order < 0)
				return false;
			return rightBits.getAt(order);
		}

		/**
		 * 権限ビットを更新します.
		 * @param action 操作
		 * @param level レベル
		 * @param value 値
		 */
		public function setRight(action:RightAction, level:RightLevel, value:Boolean):void
		{
			var i:uint;
			var j:uint;
			var a:RightAction;
			var l:RightLevel;
			var right:Right;
			var order:uint;

			var actionIndex:int = actions.indexOf(action);
			var levelIndex:int = levels.indexOf(level);

			if (actionIndex < 0 || levelIndex < 0)
				return;

			if (value)
			{
				//value = true
				for (i=0; i<=actionIndex; i++)
				{
					a = actions[i] as RightAction;
					if (!a) continue;
					for (j=0; j<=levelIndex; j++)
					{
						l = levels[j] as RightLevel;
						if (!l) continue;
						try {
							right = Right.valueOf(a.name + "_" + l.name + "_" + target.name);
						} catch (e:Error) {
						}
						if (!right) continue;
						order = Right.constants.indexOf(right);
						if (order < 0) continue;
						rightBits.setAt(order, value);
					}
				}
			}
			else
			{
				//value = false
				for (i=actionIndex; i<actions.length; i++)
				{
					a = actions[i] as RightAction;
					if (!a) continue;
					for (j=levelIndex; j<levels.length; j++)
					{
						l = levels[j] as RightLevel;
						if (!l) continue;
						try {
							right = Right.valueOf(a.name + "_" + l.name + "_" + target.name);
						} catch (e:Error) {
						}
						if (!right) continue;
						order = Right.constants.indexOf(right);
						if (order < 0) continue;
						rightBits.setAt(order, value);
					}
				}

			}
			updateItems();
			dispatchEvent(new Event(Event.CHANGE));
		}

		//-------------------------------------------------
		// MASK
		//-------------------------------------------------

		public function updateMask():void
		{
			for each (var item:RightItem in items)
			{
				item.updateMask();
			}
		}

		public function clearMask():void
		{
			for each (var item:RightItem in items)
			{
				item.clearMask();
			}
		}
	}
}