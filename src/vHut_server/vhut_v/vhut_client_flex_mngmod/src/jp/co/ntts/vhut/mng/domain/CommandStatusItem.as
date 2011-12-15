/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.domain
{
	import jp.co.ntts.vhut.entity.CommandStatus;

	import mx.collections.ArrayCollection;

	/**
	 * コマンド状態選択肢
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
	public class CommandStatusItem
	{
		public static function createItemList():ArrayCollection
		{
			var list:ArrayCollection = new ArrayCollection();
			for each (var enum:CommandStatus in CommandStatus.constants)
			{
				list.addItem(new CommandStatusItem(enum));
			}
			return list;
		}

		public var status:CommandStatus;

		public var selected:Boolean = false;

		public function CommandStatusItem(enum:CommandStatus)
		{
			status = enum;
		}
	}
}