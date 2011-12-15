package jp.co.ntts.vhut.util
{
	import flash.utils.Dictionary;

	/**
	 * <p>xxxのインターフェース。
	 * <br>
	 * <p>xxxに関する以下の情報を提供する。
	 * <ul>
	 * <li>xxx
	 * </ul>
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 * 
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IClonable
	{
		function clone(withId:Boolean = true, deep:Boolean = true, ref:Dictionary = null):*;
	}
}