/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.domain
{
	import org.spicefactory.lib.reflect.mapping.ValidationError;

	/**
	 * 接続情報の伝達に使うデータ.
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
	public class VaLinkDragData
	{
		public function VaLinkDragData()
		{
		}

		public var oldLink:VaLink;
		public var newLink:VaLink;

		public var startNodeType:String;

		public function get isUpdate():Boolean
		{
			return oldLink != null;
		}

	}
}