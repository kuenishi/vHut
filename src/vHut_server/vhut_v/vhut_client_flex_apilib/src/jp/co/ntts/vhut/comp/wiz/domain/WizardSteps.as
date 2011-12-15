/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.wiz.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;

	import spark.components.SkinnableContainer;
	import spark.events.IndexChangeEvent;

	/**
	 * <p>ウィザードのステップを管理します.
	 * <br>
	 * Cairgorm:Navigationのwaypointを格納する想定で作成しました.
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
	[Event(name="change", type="spark.events.IndexChangeEvent")]
	[Event(name="complete", type="flash.events.Event")]
	[DefaultProperty(stepsArray)]
	public class WizardSteps extends EventDispatcher
	{

		/**
		 * コンストラクタ
		 * @param args ステップ文字列（Cairngorm:Navigationのwaypoint）（可変長）
		 */
		public function WizardSteps()
		{
			_steps = new ArrayCollection();
			_currentNumber = 0;
		}

		[ArrayElementType("jp.co.ntts.vhut.comp.wiz.domain.WizardStep")]
		public function set stepsArray(value:Array):void
		{
			_steps = new ArrayCollection(value);
			currentNumber = 0;
			dispatchEvent(new Event("stepsChanged"));
		}

		[Bindable("stepsChanged")]
		/** ステップ文字列のリスト. */
		public function get steps():IList
		{
			return _steps;
		}
		public function set steps(value:IList):void
		{
			_steps = new ArrayCollection(value.toArray());
			currentNumber = 0;
		}
		private var _steps:ArrayCollection = new ArrayCollection();

		[Bindable("change")]
		/** 現在のステップ文字列 */
		public function get currentStep():WizardStep
		{
			return _steps.getItemAt(_currentNumber) as WizardStep;
		}
		private var _currentNumber:int = 0;

		protected function set currentNumber(value:int):void
		{
			var event:CollectionEvent;

			var oldIndex:uint = _currentNumber;
			var newIndex:uint = value;
			var oldStep:WizardStep = steps.getItemAt(oldIndex) as WizardStep;
			var newStep:WizardStep = steps.getItemAt(newIndex) as WizardStep;

			if(oldStep)
			{
				oldStep.isCurrent = false;
				event = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
				event.kind = CollectionEventKind.UPDATE;
				event.items = [PropertyChangeEvent.createUpdateEvent(oldStep, "isCurrent", true, false)];
				steps.dispatchEvent(event);
			}

			if(newStep)
			{
				newStep.isCurrent = true;
				event = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
				event.kind = CollectionEventKind.UPDATE;
				event.items = [PropertyChangeEvent.createUpdateEvent(newStep, "isCurrent", false, true)];
				steps.dispatchEvent(event);
			}

			_currentNumber = value;
			dispatchIndexChangedEvent(oldIndex, newIndex);
		}

		public function init():WizardStep
		{
			currentNumber = 0;
			currentStep.initiate();
			return currentStep;
		}

		/**
		 * 次のステップに遷移します.
		 * <ul>
		 * <li>ステップがない時はnullを返します.
		 * <li>次のステップがない時は現在のステップを返します.
		 * </ul>
		 * 上記のケースでは遷移のEvent（currentChange）は発生しません。
		 */
		public function next():WizardStep
		{
			if (_steps.length == 0)
			{
				return null;
			}
			else if (currentStep.isValid)
			{
				if (hasNext)
				{
					currentNumber = _currentNumber + 1;
					currentStep.initiate();
				}
				else
				{
					dispatchEvent(new Event(Event.COMPLETE));
				}
			}
			return currentStep;
			SkinnableContainer
		}

		/**
		 * 前のステップに遷移します.
		 * <ul>
		 * <li>ステップがない時はnullを返します.
		 * <li>前のステップがない時は現在のステップを返します.
		 * </ul>
		 * 上記のケースでは遷移のEvent（currentChange）は発生しません。
		 */
		public function prev():WizardStep
		{
			if (_steps.length == 0)
			{
				return null;
			}
			else if (hasPrev)
			{
				currentNumber = _currentNumber - 1;
				currentStep.initiate();
			}
			return currentStep;
		}

		[Bindable("change")]
		/** 次のステップが存在します. */
		public function get hasNext():Boolean
		{
			if (_steps.length > 0 && _currentNumber < _steps.length-1)
			{
				return true;
			}
			return false;
		}

		/** 前のステップが存在します. */
		[Bindable("change")]
		public function get hasPrev():Boolean
		{
			if (_steps.length > 0 && _currentNumber > 0)
			{
				return true;
			}
			return false;
		}

		protected function dispatchIndexChangedEvent(oldIndex:uint, newIndex:uint):void
		{
			var event:IndexChangeEvent = new IndexChangeEvent(IndexChangeEvent.CHANGE);
			event.oldIndex = oldIndex;
			event.newIndex = newIndex;
			dispatchEvent(event);
		}
	}
}