/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.comp.wiz.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;

	import flash.events.Event;
	import flash.events.EventDispatcher;

	import flashx.textLayout.elements.DivElement;
	import flashx.textLayout.elements.ParagraphElement;
	import flashx.textLayout.elements.SpanElement;
	import flashx.textLayout.elements.TextFlow;

	import jp.co.ntts.vhut.comp.wiz.domain.WizardStep;
	import jp.co.ntts.vhut.comp.wiz.domain.WizardSteps;
	import jp.co.ntts.vhut.core.BaseCommand;
	import jp.co.ntts.vhut.core.CommandWatcher;
	import jp.co.ntts.vhut.core.ICommandOwner;

	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;

	import spark.events.IndexChangeEvent;

	[Event(name="changeFlow", type="flash.events.Event")]
	[Event(name="changeMode", type="flash.events.Event")]
	[Event(name="navigateTo", type="com.adobe.cairngorm.navigation.NavigationEvent")]
	[ManagedEvents("navigateTo")]
	/**
	 * <p>
	 * UIComponentの配列を登録してPopupさせて使用します.
	 * CairngormのNavigation Frameworkを使用し、
	 * Wizard型（シーケンス型）の実行を行います。
	 * このクラスはSKMPortal関連モジュールで共用されます.
	 *
	 * </p>
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
	public class WizardPM extends EventDispatcher implements ICommandOwner
	{
		/** フローが変更された際のイベント */
		public static const CHNAGE_FLOW:String = "changeFlow";
		/** フローが変更された際のイベント */
		public static const CHNAGE_MODE:String = "changeMode";

		/** フローを表示するためのリソースバンドル */
		protected var _bundle:String

		[MessageDispatcher]
		/** Spiceのイベントディスパッチャー */
		public var dispatcher:Function;

		[Bindable]
		[Inject]
		public var commandWatcher:CommandWatcher;

		/**
		 * コンストラクタ
		 * @param bundle フローを表示するためのリソースバンドル
		 */
		public function WizardPM(bundle:String)
		{
			_bundle = bundle;
			_resournceManager = ResourceManager.getInstance();
			updateFlow();
		}

		public function get resourceManager():IResourceManager
		{
			return _resournceManager;
		}
		private var _resournceManager:IResourceManager;

		public function initByComponent():void
		{
			steps.init();
			dispatchEvent(new Event(CHNAGE_FLOW));
		}

		[Bindable("changeMode")]
		/** ウィザードの起動モード */
		public function get mode():String
		{
			return _mode;
		}
		public function set mode(value:String):void
		{
			_mode = value;
			dispatchEvent(new Event(CHNAGE_MODE));
		}
		private var _mode:String;

		[Bindable]
		/** ウィザードの画面ステップ */
		public function get steps():WizardSteps
		{
			return _steps;
		}
		public function set steps(value:WizardSteps):void
		{
			if(_steps != null) {
				_steps.removeEventListener(Event.COMPLETE, onWizardStepsComplete);
				_steps.removeEventListener(IndexChangeEvent.CHANGE, onWizardIndexChange);
			}
			_steps = value;
			_steps.addEventListener(Event.COMPLETE, onWizardStepsComplete);
			_steps.addEventListener(IndexChangeEvent.CHANGE, onWizardIndexChange);
			updateFlow();
			goToStep(steps.currentStep);
		}
		private var _steps:WizardSteps;

		/**
		 * 次のプロセスに移動します.
		 */
		public function next():void
		{
			goToStep(steps.next());
		}

		/**
		 * 前のプロセスに移動します.
		 */
		public function prev():void
		{
			goToStep(steps.prev());
		}

		[Bindable("changeFlow")]
		/** フローの表示文字列 */
		public function get flow():TextFlow
		{
			return _flow;
		}
		private var _flow:TextFlow;

		/** フローの表示文字列を更新する */
		public function updateFlow():void
		{
			_flow = new TextFlow();
			var rootElement:ParagraphElement = new ParagraphElement();
			_flow.addChild(rootElement);
			if(steps)
				for each (var step:WizardStep in steps.steps) {
					var stepSpan:SpanElement = new SpanElement();
					stepSpan.text = step.name;
					if (step == steps.currentStep) {
						stepSpan.id = "current";
					}
					rootElement.addChild(stepSpan);
				}

			dispatchEvent(new Event(CHNAGE_FLOW));
		}

		private function goToStep(step:WizardStep):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(step.destination));
		}

		/**
		 * ウィザードが終了した際に実行されます.
		 */
		protected function onWizardStepsComplete(event:Event):void
		{
			//継承して実装します.
		}

		/**
		 * ウィザードの現在ステップ位置が変更された際に実行されます.
		 */
		protected function onWizardIndexChange(event:IndexChangeEvent):void
		{
			updateFlow();
		}

		[Bindable("changeMode")]
		/** ウィザードタイトル */
		public function get title():String
		{
			return "not defined";
		}

		public function result(command:BaseCommand, object:Object):Boolean
		{
			return true;
		}

		public function error(command:BaseCommand, fault:FaultEvent):Boolean
		{
			return true;
		}
	}
}