/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.tmp.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.tmp.TmpEvent;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(type="jp.co.ntts.vhut.core.GetAllEvent", name="getAllBaseTemplate")]
	[Event(type="jp.co.ntts.vhut.core.GetByIdEvent", name="getTmpById")]
	[Event(type="jp.co.ntts.vhut.tmp.TmpEvent", name="deleteTmp")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents("getAllBaseTemplate, getTmpById, deleteTmp")]
	/**
	 * ベーステンプレートの管理クラス.
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
	public class BaseTemplates extends EventDispatcher
	{
		/** フィルタリングに用いる変数のデフォルト */
		public static const FILTER_FIELD_BASETEMPLATES:Array = ["name"];

		public function BaseTemplates(target:IEventDispatcher=null)
		{
			super(target);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  BaseTemplates
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/** ベーステンプレートのリスト */
		public function set baseTemplates(value:IList):void
		{
			_baseTemplates = new ArrayCollection(value.toArray())
			_baseTemplates.filterFunction = baseTemplatesFilter;
			_baseTemplates.refresh();
			updateTargetBaseTemplate(true);
		}
		public function get baseTemplates():IList
		{
			return _baseTemplates;
		}
		private var _baseTemplates:ArrayCollection = new ArrayCollection();

		/** ベーステンプレートのリストをサーバから取得する. */
		public function updateBaseTemplates():void
		{
			dispatchEvent(GetAllEvent.newGetAllBaseTemplateEvent());
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter BaseTemplates
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setBaseTemplatesfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_baseTemplatesFilterKeywords = keywords;
			}
			else
			{
				_baseTemplatesFilterKeywords = new Array();
			}

			if(fields)
			{
				_baseTemplatesFilterFields = fields;
			}
			else
			{
				_baseTemplatesFilterFields = FILTER_FIELD_BASETEMPLATES;
			}

			_baseTemplates.refresh();

			updateTargetBaseTemplate();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearBaseTemplatesFilter():void
		{
			setBaseTemplatesfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get baseTemplatesFilterFields():Array
		{
			return _baseTemplatesFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get baseTemplatesFilterKeywords():Array
		{
			return _baseTemplatesFilterKeywords;
		}

		protected var _baseTemplatesFilterFields:Array = new Array();
		protected var _baseTemplatesFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function baseTemplatesFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, baseTemplatesFilterFields, baseTemplatesFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetBaseTemplate
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("targetBaseTemplateChanged")]
		/** 選択中ベーステンプレート */
		public function set targetBaseTemplate(value:BaseTemplate):void
		{
			if(_targetBaseTemplate == value)
				return;

			isTargetBaseTemplateLast = false;
			_targetBaseTemplate = value;
			updateTargetBaseTemplate();
			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		public function get targetBaseTemplate():BaseTemplate
		{
			return _targetBaseTemplate;
		}
		private var _targetBaseTemplate:BaseTemplate;

		[Bindable]
		/** 選択中のベーステンプレートが詳細まで取得できている. */
		public function set isTargetBaseTemplateLast(value:Boolean):void
		{
			if(_isTargetBaseTemplateLast == value)
				return;

			_isTargetBaseTemplateLast = value;
			if(_isTargetBaseTemplateLast)
			{
				dispatchEvent(new Event("targetBaseTemplateChanged"));
			}
			dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetBaseTemplate));
		}
		public function get isTargetBaseTemplateLast():Boolean
		{
			return _isTargetBaseTemplateLast;
		}
		private var _isTargetBaseTemplateLast:Boolean = false;

		/**
		 * 選択中のベーステンプレートを更新する.
		 *
		 * @param force 既に最新化していても再取得する.
		 */
		public function updateTargetBaseTemplate(force:Boolean = false):void
		{
			if(_targetBaseTemplate)
			{
				_targetBaseTemplate = getBaseTemplateById(_targetBaseTemplate.id);
			}

			if(_targetBaseTemplate)
			{
				if(force || !isTargetBaseTemplateLast)
				{
					isTargetBaseTemplateLast = false;
					dispatchEvent(GetByIdEvent.newGetTmp(_targetBaseTemplate.id));
				}
			}
			else
			{
				isTargetBaseTemplateLast = false;
			}
			dispatchEvent(new Event("targetBaseTemplateChanged"));
		}

		/**
		 * ID指定でベーステンプレートを取得します.
		 * @param id ID
		 * @return ベーステンプレート
		 */
		public function getBaseTemplateById(id:Number):BaseTemplate
		{
			if(baseTemplates)
			{
				for each (var baseTemplate:BaseTemplate in baseTemplates)
				{
					if(baseTemplate.id == id)
						return baseTemplate;
				}
			}
			return null;
		}

		/** 選択中のベーステンプレートを消去する. */
		public function deleteTargetBaseTemplate():void
		{
			dispatchEvent(TmpEvent.newDeleteTmpEvent(targetBaseTemplate));
		}

	}
}