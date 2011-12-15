/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.domain
{
	import jp.co.ntts.vhut.entity.CommandOperation;

	import mx.collections.ArrayCollection;

	/**
	 * コマンド操作選択肢
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
	public class CommandOperationItem
	{
		public static function createItemList():ArrayCollection
		{
			var list:ArrayCollection = new ArrayCollection();
			for each (var enum:CommandOperation in CommandOperation.constants)
			{
				list.addItem(new CommandOperationItem(enum));
			}
			return list;
		}

		public var operation:CommandOperation;

		public var selected:Boolean = false;

		public function CommandOperationItem(enum:CommandOperation)
		{
			operation = enum;
		}
	}
}