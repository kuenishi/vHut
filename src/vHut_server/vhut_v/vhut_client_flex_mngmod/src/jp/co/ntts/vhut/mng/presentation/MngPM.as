/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.mng.presentation
{
	import flash.events.Event;

	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.mng.domain.Commands;
	import jp.co.ntts.vhut.mng.domain.Content;
	import jp.co.ntts.vhut.mng.domain.Performance;
	import jp.co.ntts.vhut.mng.domain.Predictions;
	import jp.co.ntts.vhut.mng.domain.Troubles;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.FlexEvent;
	import mx.skins.halo.SwatchPanelSkin;

	/**
	 * アプリケーション管理モジュールのメインのPM.
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
	 * $Author: NTT Software Corporation. $ss
	 */
	public class MngPM extends ModulePMBase
	{
		public static const CHANGE_CONTENTS:String = "changeContents";
		public static const CHANGE_TARGET_CONTENT:String = "changeTargetContent";

		[Inject]
		public var commandViewPM:CommandViewPM;

		[Inject]
		[Bindable]
		public var performance:Performance;

		[Inject]
		[Bindable]
		public var predictions:Predictions;

		[Inject]
		[Bindable]
		public var troubles:Troubles;

		public function MngPM():void
		{

			_contents = new ArrayCollection();
			_contents.addItem(Content.newContent(0, resourceManager.getString('MNGUI', 'title.performance')));
			/**
			_contents.addItem(Content.newContent(1, resourceManager.getString('MNGUI', 'title.trouble')));
			**/
			_contents.addItem(Content.newContent(2, resourceManager.getString('MNGUI', 'title.prediction')));
			_contents.addItem(Content.newContent(3, resourceManager.getString('MNGUI', 'title.command')));

			targetContent = _contents[0];
		}

		[Bindable("changeTargetContent")]
		public function set targetContent(value:Content):void
		{
			_targetContent = value;
			updateContentsStatus();
			dispatchEvent(new Event(CHANGE_TARGET_CONTENT));
		}
		public function get targetContent():Content
		{
			return _targetContent;
		}
		private var _targetContent:Content;

		override public function get moduleName():String
		{
			return ModuleName.MNG;
		}

		[Init]
		/**
		 * フレームワークから初期化される際に呼ばれるイベントです.
		 */
		public function init():void
		{
		}

		/**
		 * メインモジュールがインスタンス完了した際に呼ばれるイベントです.
		 */
		public function onCreationComplete(event:FlexEvent):void
		{
			isActivate = true;
		}

		override protected function onModuleEnter():void
		{
			isActivate = true;
		}

		override protected function onModuleExit():void
		{
			isActivate = false;
		}

		public function set isActivate(value:Boolean):void
		{
			if (_isActivate == value) return;
			_isActivate = value;
			updateContentsStatus();
		}
		public function get isActivate():Boolean
		{
			return _isActivate;
		}
		private var _isActivate:Boolean = false;

		/** 表示を最新化します. */
		public function updateContentsStatus():void
		{
			if(commandViewPM)
				commandViewPM.isActivate = false;

			if(targetContent && isActivate)
			{
				switch(targetContent.index)
				{
					case 0:
						if(performance)
							performance.updatePerformace();
						break;
					case 1:
						if(troubles)
							troubles.updateTroubles();
						break;
					case 2:
						if(predictions)
							predictions.updatePredictions(null, null);
						break;
					case 3:
						if(commandViewPM)
							commandViewPM.isActivate = true;
						break;
				}
			}
		}

		[Bindable("changeContents")]
		public function get contents():IList
		{
			return _contents;
		}
		private var _contents:ArrayCollection;
	}
}
