/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.layer
{
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.LinkItemRenderer;

	import mx.collections.IList;
	import mx.core.ClassFactory;
	import mx.core.mx_internal;
	import mx.events.CollectionEvent;

	use namespace mx_internal;  //ListBase and List share selection properties that are mx_internal

	/**
	 * ネットワークを描画するためのレイヤー
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
	public class VaNetworkLayer extends VaListBaseLayer
	{

		public function VaNetworkLayer()
		{
			super();
			itemRenderer = new ClassFactory(LinkItemRenderer);
			layout = new VaNetworkLayerLayout();
			layout.clipAndEnableScrolling = true;
		}

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