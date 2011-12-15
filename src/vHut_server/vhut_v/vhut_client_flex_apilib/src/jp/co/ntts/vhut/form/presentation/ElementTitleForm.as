/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.form.presentation
{
	import jp.co.ntts.vhut.core.presentation.IconImage;

	import spark.components.Label;
	import spark.components.SkinnableContainer;

	/**
	 *
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
	public class ElementTitleForm extends SkinnableContainer
	{
		public function ElementTitleForm()
		{
			super();
		}

		[Bindable]
		public var localName:String;

		[Bindable]
		public var globalName:String

		[Bindable]
		public var imageUrl:String;

		[SkinPart(required="false")]
		/** Diskを格納するデータグループ */
		public var thum:IconImage;

		[SkinPart(required="false")]
		/** Diskを格納するデータグループ */
		public var localNameLabel:Label;

		[SkinPart(required="false")]
		/** Diskを格納するデータグループ */
		public var globalNameLabel:Label;
	}
}