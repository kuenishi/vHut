/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.top.presentation
{
	import flash.events.Event;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;

	import jp.co.ntts.vhut.config.VhutConfig;
	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.core.infrastructure.ModuleName;
	import jp.co.ntts.vhut.core.presentation.ModulePMBase;
	import jp.co.ntts.vhut.top.domain.Abstraction;
	import jp.co.ntts.vhut.top.domain.Content;
	import jp.co.ntts.vhut.top.domain.Manuals;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.FlexEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.skins.halo.WindowBackground;

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
	[Event(name="getTroubleAbstraction", type="jp.co.ntts.vhut.core.GetEvent")]
	[Event(name="getPerformanceAbstraction", type="jp.co.ntts.vhut.core.GetEvent")]
	[ManagedEvents(names="getTroubleAbstraction,getPerformanceAbstraction")]
	public class TopPM extends ModulePMBase
	{
		public static const CHANGE_CONTENTS:String = "changeContents";
		public static const CHANGE_TARGET_CONTENT:String = "changeTargetContent";

		[Inject]
		[Bindable]
		public var abstraction:Abstraction;

		[Inject]
		[Bindable]
		public var manuals:Manuals;

		private var _rm:IResourceManager;
		private var alertButtonLabels:Vector.<String>;

		public function TopPM():void
		{
			_rm = ResourceManager.getInstance();
			alertButtonLabels = new <String>[
				_rm.getString('APIUI', 'alert.ok'),
				_rm.getString('APIUI', 'alert.cancel'),
				]

			_contents = new ArrayCollection();
			_contents.addItem(Content.newContent(0, _rm.getString('TOPUI', 'title.abstraction')));
			/**
			_contents.addItem(Content.newContent(1, _rm.getString('TOPUI', 'title.guide')));-->
			**/
			targetContent = _contents[0];
		}

		[Bindable("changeTargetContent")]
		public function set targetContent(value:Content):void
		{
			_targetContent = value;
			dispatchEvent(new Event(CHANGE_TARGET_CONTENT));
		}
		public function get targetContent():Content
		{
			return _targetContent;
		}
		private var _targetContent:Content;

		override public function get moduleName():String
		{
			return ModuleName.TOP;
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
			refresh();
		}

		override protected function onModuleEnter():void
		{
			refresh();
		}

		override protected function onModuleExit():void
		{

		}

		/** 表示を最新化します. */
		public function refresh():void
		{
			getPerformanceAbstraction();
			getTroubleAbstraction();
			manuals.updateManualList();
		}

		/**
		 * パフォーマンス情報の概要を取得します.
		 */
		public function getPerformanceAbstraction():void
		{
			dispatchEvent(GetEvent.newGetPerformanceAbstractionEvent());
		}

		/**
		 * 障害情報の概要を取得します.
		 */
		public function getTroubleAbstraction():void
		{
			dispatchEvent(GetEvent.newGetTroubleAbstaractionEvent());
		}

		[Bindable("changeContents")]
		public function get contents():IList
		{
			return _contents;
		}
		private var _contents:ArrayCollection;

		public function launchGuide():void
		{
//			navigateToURL(new URLRequest(VhutConfig.GUIDE_URL), "_blank");
		}
	}
}