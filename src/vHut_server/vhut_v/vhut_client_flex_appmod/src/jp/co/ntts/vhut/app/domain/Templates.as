/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.app.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;

	import jp.co.ntts.vhut.dto.AdditionalDiskDto;
	import jp.co.ntts.vhut.dto.SpecDto;
	import jp.co.ntts.vhut.dto.SwitchTemplateDto;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="cahgeBaseTemplates", type="flash.events.Event")]
	[Event(name="cahgeSwitchTemplates", type="flash.events.Event")]
	[Event(name="cahgeDiskTemplates", type="flash.events.Event")]
	[Event(name="cahgeSpecTemplates", type="flash.events.Event")]
	/**
	 * テンプレートを管理するモデル.
	 * 以下が含まれます.
	 * <ul>
	 * <li>VMのテンプレート
	 * <li>スイッチのテンプレート
	 * <li>ディスクのテンプレート
	 * <li>スペックのテンプレート
	 * </ul>
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
	public class Templates extends EventDispatcher
	{
		/** ベーステンプレートのリストが変更された際のイベント. */
		public static const CHANGE_BASE_TEMPLATES:String = "cahgeBaseTemplates";
		/** スイッチテンプレートのリストが変更された際のイベント. */
		public static const CHANGE_SWITCH_TEMPLATES:String = "cahgeSwitchTemplates";
		/** ディスクテンプレートのリストが変更された際のイベント. */
		public static const CHANGE_DISK_TEMPLATES:String = "cahgeDiskTemplates";
		/** スペックテンプレートのリストが変更された際のイベント. */
		public static const CHANGE_SPEC_TEMPLATES:String = "cahgeSpecTemplates";

		/** ベーステンプレートのフィルタリングに使用するフィールドのリスト */
		public static const FIELDS_BASE_TEMPLATES:Array = ["name"];
		/** スイッチテンプレートのフィルタリングに使用するフィールドのリスト */
		public static const FIELDS_SWITCH_TEMPLATES:Array = ["name", "port"];
		/** ディスクテンプレートのフィルタリングに使用するフィールドのリスト */
		public static const FIELDS_DISK_TEMPLATES:Array = ["name", "size"];

		[Init]
		public function init():void
		{
			var baseTemplate:BaseTemplate = new BaseTemplate();
			var switchTemplate:SwitchTemplateDto = new SwitchTemplateDto();
			var diskTemplate:AdditionalDiskDto = new AdditionalDiskDto();
			var specTemplate:SpecDto = new SpecDto();
		}

		[Bindable("cahgeBaseTemplates")]
		/** ベーステンプレート */
		public function get baseTemplates():IList
		{
			return _baseTemplates;
		}
		public function set baseTemplates(value:IList):void
		{
			_baseTemplates = new ArrayCollection(value.toArray());
			dispatchEvent(new Event(CHANGE_BASE_TEMPLATES));
		}
		private var _baseTemplates:ArrayCollection = new ArrayCollection();

		/**
		 * ベーステンプレートをフィルタリングします.
		 * @param keywords キーワードのリスト
		 * @param fields 検索に使用する変数名のリスト
		 */
		public function filterBaseTemplates(keywords:Array, fields:Array=null):void
		{
			if(!fields)
			{
				fields = FIELDS_BASE_TEMPLATES;
			}
			_baseTemplates.filterFunction = function (item:Object):Boolean
			{
				return CollectionUtil.multiFilter(item, fields, keywords);
			}
			_baseTemplates.refresh();
		}

		[Bindable("cahgeSwitchTemplates")]
		/** スイッチテンプレート */
		public function get switchTemplates():IList
		{
			return _switchTemplates;
		}
		public function set switchTemplates(value:IList):void
		{
			_switchTemplates = new ArrayCollection(value.toArray());
			dispatchEvent(new Event(CHANGE_SWITCH_TEMPLATES));
		}
		private var _switchTemplates:ArrayCollection = new ArrayCollection();

		/**
		 * スイッチテンプレートをフィルタリングします.
		 * @param keywords キーワードのリスト
		 * @param fields 検索に使用する変数名のリスト
		 */
		public function filterSwitchTemplates(keywords:Array, fields:Array=null):void
		{
			if(!fields)
			{
				fields = FIELDS_SWITCH_TEMPLATES;
			}
			_switchTemplates.filterFunction = function (item:Object):Boolean
			{
				return CollectionUtil.multiFilter(item, fields, keywords);
			}
			_switchTemplates.refresh();
		}

		[Bindable("cahgeDiskTemplates")]
		/** ディスクテンプレート */
		public function get diskTemplates():IList
		{
			return _diskTemplates;
		}
		public function set diskTemplates(value:IList):void
		{
			_diskTemplates = new ArrayCollection(value.toArray());
			dispatchEvent(new Event(CHANGE_DISK_TEMPLATES));
		}
		private var _diskTemplates:ArrayCollection = new ArrayCollection();

		/**
		 * ディスクテンプレートをフィルタリングします.
		 * @param keywords キーワードのリスト
		 * @param fields 検索に使用する変数名のリスト
		 */
		public function filterDiskTemplates(keywords:Array, fields:Array=null):void
		{
			if(!fields)
			{
				fields = FIELDS_DISK_TEMPLATES;
			}
			_diskTemplates.filterFunction = function (item:Object):Boolean
			{
				return CollectionUtil.multiFilter(item, fields, keywords);
			}
			_diskTemplates.refresh();
		}

		[Bindable("cahgeSpecTemplates")]
		/** スペックテンプレート */
		public function get specTemplates():IList
		{
			return _specTemplates;
		}
		public function set specTemplates(value:IList):void
		{
			_specTemplates = value;
			dispatchEvent(new Event(CHANGE_SPEC_TEMPLATES));
		}
		private var _specTemplates:IList = new ArrayCollection();

		public function get defaultSpecTemplate():SpecDto
		{
			return _specTemplates[0];
		}


	}
}