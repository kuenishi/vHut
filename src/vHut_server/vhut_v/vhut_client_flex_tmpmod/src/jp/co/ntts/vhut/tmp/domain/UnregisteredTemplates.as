/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.tmp.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.entity.Template;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;


	[Event(type="jp.co.ntts.vhut.core.GetAllEvent", name="getUnregisteredTemplate")]
	[ManagedEvents("getUnregisteredTemplate")]
	/**
	 * 外部テンプレートの管理クラス.
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
	public class UnregisteredTemplates extends EventDispatcher
	{
		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_UNREGISTERED_TEMPLATES:Array = ["name"];

		public function UnregisteredTemplates(target:IEventDispatcher=null)
		{
			super(target);
		}

		[Inject]
		public var editingBaseTemplate:EditingBaseTemplate;

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  UnregisteredTemplates
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/** 外部テンプレートのリスト */
		public function set unregisteredTemplates(value:IList):void
		{
			_unregisteredTemplates = new ArrayCollection(value.toArray());
			_unregisteredTemplates.filterFunction = unregisteredTemplatesFilter;
			_unregisteredTemplates.refresh();
			updateTargetUnregisteredTemplate();
		}
		public function get unregisteredTemplates():IList
		{
			return _unregisteredTemplates;
		}
		private var _unregisteredTemplates:ArrayCollection = new ArrayCollection();

		/** 外部テンプレートのリストを再取得する. */
		public function updateUnregisteredTemplates():void
		{
			dispatchEvent(GetAllEvent.newGetUnregisteredTemplateEvent());
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter UnregisteredTemplates
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setUnregisteredTemplatesFilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_unregisteredTemplatesFilterKeywords = keywords;
			}
			else
			{
				_unregisteredTemplatesFilterKeywords = new Array();
			}

			if(fields)
			{
				_unregisteredTemplatesFilterFields = fields;
			}
			else
			{
				_unregisteredTemplatesFilterFields = FILTER_FIELD_UNREGISTERED_TEMPLATES;
			}

			_unregisteredTemplates.refresh();

			updateTargetUnregisteredTemplate();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearUnregisteredTemplatesFilter():void
		{
			setUnregisteredTemplatesFilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get unregisteredTemplatesFilterFields():Array
		{
			return _unregisteredTemplatesFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get unregisteredTemplatesFilterKeywords():Array
		{
			return _unregisteredTemplatesFilterKeywords;
		}

		protected var _unregisteredTemplatesFilterFields:Array = new Array();
		protected var _unregisteredTemplatesFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function unregisteredTemplatesFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, unregisteredTemplatesFilterFields, unregisteredTemplatesFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetUnregisteredTemplate
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("targetUnregisteredTemplateChanged")]
		/** 選択中の外部テンプレート */
		public function set targetUnregisteredTemplate(value:Template):void
		{
			if(_targetUnregisteredTemplate == value)
				return;

			_targetUnregisteredTemplate = value;
			updateTargetUnregisteredTemplate();
		}
		public function get targetUnregisteredTemplate():Template
		{
			return _targetUnregisteredTemplate;
		}
		protected var _targetUnregisteredTemplate:Template;

		private function updateTargetUnregisteredTemplate():void
		{
			if(_targetUnregisteredTemplate
				&& unregisteredTemplates.getItemIndex(_targetUnregisteredTemplate) < 0)
			{
				_targetUnregisteredTemplate = null;
			}

			if( !_targetUnregisteredTemplate
				&& editingBaseTemplate
				&& editingBaseTemplate.targetBaseTemplate
				&& editingBaseTemplate.targetBaseTemplate.templateId > 0)
			{
				for each(var template:Template in unregisteredTemplates)
				{
					if(template.id == editingBaseTemplate.targetBaseTemplate.templateId)
					{
						_targetUnregisteredTemplate = template;
						break;
					}
				}
			}

			dispatchEvent(new Event("targetUnregisteredTemplateChanged"));
		}
	}
}