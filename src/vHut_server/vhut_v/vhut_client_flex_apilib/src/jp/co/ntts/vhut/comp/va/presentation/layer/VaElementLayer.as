/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import flash.events.MouseEvent;

	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.RappSgTemplateItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.RappTemplateItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.SgItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.VmItemRenderer;
	import jp.co.ntts.vhut.entity.ApplicationInstanceSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationInstanceVm;
	import jp.co.ntts.vhut.entity.ApplicationSecurityGroup;
	import jp.co.ntts.vhut.entity.ApplicationVm;
	import jp.co.ntts.vhut.entity.ReleasedApplicationSecurityGroupTemplate;
	import jp.co.ntts.vhut.entity.ReleasedApplicationTemplate;

	import mx.collections.IList;
	import mx.core.ClassFactory;
	import mx.core.IButton;
	import mx.core.IFactory;
	import mx.core.IVisualElement;
	import mx.core.mx_internal;
	import mx.events.CollectionEvent;

	import spark.components.Button;
	import spark.components.IItemRenderer;
	import spark.components.supportClasses.ListBase;
	import spark.events.RendererExistenceEvent;

	use namespace mx_internal;  //ListBase and List share selection properties that are mx_internal

	/**
	 * VM構成の表示コンポーネント.
	 * <p>下記の処理を行います
	 * <ul>
	 * <li>VMの選択変更
	 * <li>NWの選択変更
	 * </ul>
	 * <p>下記の要素を表示します
	 * <ul>
	 * <li>VMの構成データ
	 * </ul>
	 * </p>
	 * vaはvirtualized applicationの略
	 *
	 * @langversion 3.0
	 * @playerversion Flash 10.1
	 *
	 * @internal
	 * $Date: 2010-12-06 14:24:54 +0900 (月, 06 12 2010) $
	 * $Revision: 624 $
	 * $Author: NTT Software Corporation. $
	 */
	public class VaElementLayer extends VaListBaseLayer
	{
		/**
		 * コンストラクタ.
		 */
		public function VaElementLayer()
		{
			super();
			itemRendererFunction = itemRenderFunctionHandler;
			layout = new VaElementLayerLayout();
			layout.clipAndEnableScrolling = true;
		}

		protected function itemRenderFunctionHandler(item:Object):IFactory
		{
			var clazz:Class;
			if(item is ApplicationVm || item is ApplicationInstanceVm)
			{
				if(vmItemRenderer != null)
					return vmItemRenderer;
				clazz = VmItemRenderer;
			}
			else if(item is ReleasedApplicationTemplate)
			{
				if(vmItemRenderer != null)
					return vmItemRenderer;
				clazz = RappTemplateItemRenderer;
			}
			else if(item is ApplicationSecurityGroup || item is ApplicationInstanceSecurityGroup)
			{
				if(sgItemRenderer != null)
					return sgItemRenderer;
				clazz = SgItemRenderer;
			}
			else if(item is ReleasedApplicationSecurityGroupTemplate)
			{
				if(sgItemRenderer != null)
					return sgItemRenderer;
				clazz = RappSgTemplateItemRenderer;
			}
			return new ClassFactory(clazz);
		}

		////////////////////////////////////////////////////////////////////////////
		//
		//  アイテムレンダラー
		//
		////////////////////////////////////////////////////////////////////////////

		/**
		 * 仮想マシン描画用のアイテムレンダラー
		 */
		public var vmItemRenderer:IFactory;

		/**
		 * 仮想スイッチ描画用のアイテムレンダラー
		 */
		public var sgItemRenderer:IFactory;

		////////////////////////////////////////////////////////////////////////////
		//
		//  データプロバイダ
		//
		////////////////////////////////////////////////////////////////////////////

		override public function set dataProvider(value:IList):void
		{
			if(dataProvider)
			{
				dataProvider.removeEventListener(CollectionEvent.COLLECTION_CHANGE, dataProviderCollectionChangeHandler);
			}
			super.dataProvider = value;
			dataProvider.addEventListener(CollectionEvent.COLLECTION_CHANGE, dataProviderCollectionChangeHandler);
		}

		public function dataProviderCollectionChangeHandler(event:CollectionEvent):void
		{
			dataGroup.invalidateDisplayList();
		}

	}
}