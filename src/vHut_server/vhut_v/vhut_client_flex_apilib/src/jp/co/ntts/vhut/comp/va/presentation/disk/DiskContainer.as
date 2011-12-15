/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.disk
{
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.DiskItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.itemrenderer.EditableDiskItemRenderer;
	import jp.co.ntts.vhut.comp.va.presentation.layer.VaListBaseLayer;
	import jp.co.ntts.vhut.entity.Disk;
	import jp.co.ntts.vhut.entity.DiskTemplate;
	
	import mx.core.ClassFactory;
	import mx.core.IFactory;

	/**
	 * ディスクを管理するコンテナ
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
	public class DiskContainer extends VaListBaseLayer
	{
		public function DiskContainer()
		{
			super();
			setStyle("skinClass", DiskContainerSkin);
			itemRendererFunction = itemRenderFunctionHandler;
		}

		protected function itemRenderFunctionHandler(item:Object):IFactory
		{
			var clazz:Class;
			if(item is Disk)
			{
				if(itemRenderer != null)
					return itemRenderer;
				var disk:Disk = item as Disk;
				if (disk.diskTemplateId > 0)
				{
					clazz = DiskItemRenderer;
				}
				else
				{
					clazz = EditableDiskItemRenderer;
				}
			}
			else if(item is DiskTemplate)
			{
				if(itemRenderer != null)
					return itemRenderer;
				clazz = DiskItemRenderer;
			}
			return new ClassFactory(clazz);
		}
	}
}