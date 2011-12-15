/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.aig.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.core.ChangeTargetItemEvent;
	import jp.co.ntts.vhut.core.GetAllEvent;
	import jp.co.ntts.vhut.core.GetByIdEvent;
	import jp.co.ntts.vhut.core.UpdateTargetItemEvent;
	import jp.co.ntts.vhut.entity.ApplicationInstanceGroup;
	import jp.co.ntts.vhut.util.CollectionUtil;

	import mx.collections.ArrayCollection;
	import mx.collections.IList;

	[Event(name="getAllAig", type="jp.co.ntts.vhut.core.GetAllEvent")]
	[Event(name="getAigById", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="getIpInfoListByAigId", type="jp.co.ntts.vhut.core.GetByIdEvent")]
	[Event(name="changeTargetItem", type="jp.co.ntts.vhut.core.ChangeTargetItemEvent")]
	[Event(name="updateTargetItem", type="jp.co.ntts.vhut.core.UpdateTargetItemEvent")]
	[ManagedEvents(names="getAigById, getAllAig, getIpInfoListByAigId")]
	/**
	 * アプリケーションインスタンスグループの管理クラス
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
	public class Aigs extends EventDispatcher
	{
//		/** アプリケーションインスタンスグループのリストが変更されました. */
//		public static const CHANGE_AIGS:String="changeAigs";
		/** アプリケーションインスタンスグループが選択されました. */
		public static const SELECT_AIG:String="selectAig";
		/** アプリケーションインスタンスグループが更新されました. */
		public static const UPDATE_AIG:String="updateAig";

		/** AIGリストのフィルタリングに用いる変数名のリスト */
		public static const FILTER_FIELDS_AIG:Array = ["name"];

		public function Aigs(target:IEventDispatcher=null)
		{
			//TODO: implement function
			super(target);
		}


		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Aigs
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		/**
		 * Aiglicationのリスト
		 */
		public function get aigs():IList
		{
			return _aigs;
		}
		public function set aigs(value:IList):void
		{
			_aigs = new ArrayCollection(value.toArray());
			_aigs.filterFunction = aigsFilter;
			_aigs.refresh();
			updateTargetAig(true);
		}
		private var _aigs:ArrayCollection=new ArrayCollection();

		public function updateAigs():void
		{
			dispatchEvent(GetAllEvent.newGetAllAigEvent());
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  Filter Aigs
		//
		///////////////////////////////////////////////////////////////////////////////////////

		/**
		 * フィルタリングの設定を更新する.
		 * @param keywords 検索キーワードのリスト
		 * @param fields 検索対象の変数名のリスト
		 */
		public function setAigsfilter(keywords:Array=null, fields:Array=null):void
		{
			if(keywords)
			{
				_aigsFilterKeywords = keywords;
			}
			else
			{
				_aigsFilterKeywords = new Array();
			}

			if(fields)
			{
				_aigsFilterFields = fields;
			}
			else
			{
				_aigsFilterFields = FILTER_FIELDS_AIG;
			}

			_aigs.refresh();

			updateTargetAig();

			dispatchEvent(new Event("filterParamsChanged"));
		}

		/** フィルタリングの設定を消去する. */
		public function clearAigsFilter():void
		{
			setAigsfilter();
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索対象の変数名のリスト */
		public function get aigsFilterFields():Array
		{
			return _aigsFilterFields;
		}

		[Bindable("filterParamsChanged")]
		/** フィルタリングに用いる検索キーワードのリスト */
		public function get aigsFilterKeywords():Array
		{
			return _aigsFilterKeywords;
		}

		protected var _aigsFilterFields:Array = new Array();
		protected var _aigsFilterKeywords:Array = new Array();

		/**
		 * ベーステンプレートリストのフィルタリング関数
		 * @param item 要素
		 * @return 表示/非表示
		 */
		protected function aigsFilter(item:Object):Boolean
		{
			return CollectionUtil.multiFilter(item, aigsFilterFields, aigsFilterKeywords);
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetAig
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("targetAigChanged")]
		/**
		 * 選択中のアプリケーションデータ.
		 */
		public function get targetAig():ApplicationInstanceGroup
		{
			return _targetAig;
		}
		public function set targetAig(value:ApplicationInstanceGroup):void
		{
			if(_targetAig == value)
				return;

			_isTargetAigLast = false;
			_targetAig=value;
			targetAigIpInfos = null;
			updateTargetAig();
			dispatchEvent(ChangeTargetItemEvent.newUpdateTargetItemEvent(value));
		}
		private var _targetAig:ApplicationInstanceGroup;

		[Bindable]
		/** targetAigが最新である. */
		public function get isTargetAigLast():Boolean
		{
			return _isTargetAigLast;
		}
		public function set isTargetAigLast(value:Boolean):void
		{
			if (_isTargetAigLast == value)
				return;
			_isTargetAigLast = value;
			if (_isTargetAigLast)
				updateTargetAigIpInfos();
			dispatchEvent(new Event("targetAigChanged"));
			dispatchEvent(UpdateTargetItemEvent.newUpdateTargetItemEvent(targetAig));
		}
		private var _isTargetAigLast:Boolean = false;

		/**
		 * ターゲットのアプリケーションの詳細をサーバから取得してデータを更新します.
		 */
		public function updateTargetAig(force:Boolean = false):void
		{
			if (_targetAig)
			{
				_targetAig = getAigById(_targetAig.id);
			}

			if (_targetAig)
			{
				if(force || !isTargetAigLast)
					isTargetAigLast = false;
					dispatchEvent(GetByIdEvent.newGetAig(targetAig.id));
			}
			else
			{
				isTargetAigLast = true;
				_targetAig = null;
			}
			dispatchEvent(new Event("targetAigChanged"));
		}

		/**
		 * ID指定でベーステンプレートを取得します.
		 * @param id ID
		 * @return ベーステンプレート
		 */
		public function getAigById(id:Number):ApplicationInstanceGroup
		{
			if(aigs)
			{
				for each (var aig:ApplicationInstanceGroup in aigs)
				{
					if(aig.id == id)
						return aig;
				}
			}
			return null;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetAig Elements
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable]
		public function get targetAigIpInfos():IList
		{
			return _taragetAigIpInfos;
		}
		public function set targetAigIpInfos(value:IList):void
		{
			if (_taragetAigIpInfos == value) return;

			if (value == null)
			{
				_taragetAigIpInfos = new ArrayCollection();
			}
			else
			{
				_taragetAigIpInfos = new ArrayCollection(value.toArray());
			}
		}
		private var _taragetAigIpInfos:ArrayCollection = new ArrayCollection();

		public function updateTargetAigIpInfos():void
		{
			if (_targetAig)
			{
				dispatchEvent(GetByIdEvent.newGetIpInfoListByAig(targetAig.id));
			}
		}

	}
}