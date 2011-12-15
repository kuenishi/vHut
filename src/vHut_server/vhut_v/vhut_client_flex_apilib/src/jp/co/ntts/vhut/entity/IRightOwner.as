package jp.co.ntts.vhut.entity
{
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;

	import jp.co.ntts.vhut.core.BitArray;

	import mx.collections.IList;

	/**
	 * Rightを所持するエンティティのインターフェース。
	 *
	 * @internal
	 * $Date$
	 * $Revision$
	 * $Author$
	 *
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IRightOwner
	{
		/** 権限（バイト列） */
		function get rights():ByteArray;
		/** 権限（Bit列） */
		function get rightBits():BitArray;
		/** 権限マップ（key=Right, value=Boolean） */
		function get rightsMap():Dictionary;
		/** 権限グループRightItemのリスト */
		function get rightGroups():IList;
	}
}