/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.si
{
	import flash.events.MouseEvent;

	import mx.states.OverrideBase;

	import spark.components.Button;
	import spark.components.Label;
	import spark.components.supportClasses.SkinnableComponent;

	[Event(name="changeStatus", type="jp.co.ntts.vhut.comp.si.StatusIndicatorEvent")]
	[SkinState("none")]
	[SkinState("stable")]
	[SkinState("changing")]
	/**
	 * ステータス情報を表示、遷移させるインジケータ
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
	public class StatusIndicator extends SkinnableComponent
	{
		public function StatusIndicator()
		{
			super();
			setStyle("skinClass", StatusIndicatorSkin);
		}

		//------------------------------------------------
		// Fields
		//------------------------------------------------

		[SkinPart(required="false")]
		public var statusLabel:Label;

		[SkinPart(required="false")]
		public var actionButton:Button;

		private var _currentStatus:String;

		private var _currentStatusName:String;

		private var _nextStatus:String;

		private var _actionName:String;

		private var _buttonVisible:Boolean;

		private var _buttonEnabled:Boolean;

		//------------------------------------------------
		// Setter Getter
		//------------------------------------------------

		[Bindable]
		/** 現在の状態 */
		public function set currentStatus(value:String):void
		{
			_currentStatus = value;
			invalidateSkinState();
		}
		public function get currentStatus():String
		{
			return _currentStatus;
		}

		[Bindable]
		/** 現在の状態の名称 */
		public function set currentStatusName(value:String):void
		{
			_currentStatusName = value;
			if(statusLabel)
				statusLabel.text = value;
		}
		public function get currentStatusName():String
		{
			return _currentStatusName;
		}

		[Bindable]
		/** 次の状態 */
		public function set nextStatus(value:String):void
		{
			_nextStatus = value;
			invalidateSkinState();
		}
		public function get nextStatus():String
		{
			return _nextStatus;
		}

		[Bindable]
		/** ボタンを押した際のアクション名 */
		public function set actionName(value:String):void
		{
			_actionName = value;
			if (actionButton)
				actionButton.label = value;
		}
		public function get actionName():String
		{
			return _actionName;
		}

		[Bindable]
		/** ボタンの可視状態 */
		public function set buttonVisible(value:Boolean):void
		{
			_buttonVisible = value;
			if (actionButton)
				actionButton.visible = value;
		}
		public function get buttonVisible():Boolean
		{
			return _buttonVisible;
		}

		[Bindable]
		/** ボタンの有効化状態 */
		public function set buttonEnabled(value:Boolean):void
		{
			_buttonEnabled = value;
			if (actionButton)
				actionButton.enabled = value;
		}
		public function get buttonEnabled():Boolean
		{
			return _buttonEnabled;
		}

		//------------------------------------------------
		// Skin
		//------------------------------------------------
		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);

			if (instance == statusLabel)
			{
				statusLabel.text = currentStatusName;
			}

			if (instance == actionButton)
			{
				actionButton.label = actionName;
				actionButton.visible = buttonVisible && nextStatus != null;
				actionButton.enabled = buttonEnabled;

				actionButton.addEventListener(MouseEvent.CLICK, onActionButtonClick);
			}
		}

		//------------------------------------------------
		// State
		//------------------------------------------------
		override protected function getCurrentSkinState():String
		{
			if (!currentStatus)
			{
				return "none";
			}
			else if (nextStatus)
			{
				return "stable"
			}
			else
			{
				return "changing"
			}
		}

		//------------------------------------------------
		// Event
		//------------------------------------------------
		/**
		 * アクションの実行ボタンが押された際のハンドラ
		 * @param event
		 */
		protected function onActionButtonClick(event:MouseEvent):void {
			if(nextStatus != null)
				dispatchEvent(StatusIndicatorEvent.newStatusIndicatorChangeStatusEvent(currentStatus, nextStatus));
		}
	}
}