/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.presentation
{
	import mx.controls.Image;
	
	import spark.components.Button;
	
	[Style(name="icon", type="Class", inherit="no")]
	[Style(name="glassColor", type="uint", format="Color", inherit="yes", theme="spark")]
	/**
	 * アイコン付きボタン
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
	public class IconButton extends Button
	{
		
		public function IconButton()
		{
			super();
			setStyle("skinClass", IconButtonDefaultSkin);
		}
	}
}