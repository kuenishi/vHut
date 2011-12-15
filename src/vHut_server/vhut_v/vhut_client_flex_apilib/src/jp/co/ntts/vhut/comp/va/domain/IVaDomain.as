package jp.co.ntts.vhut.comp.va.domain
{
	import jp.co.ntts.vhut.entity.SecurityGroup;
	import jp.co.ntts.vhut.entity.Vm;
	
	import mx.collections.IList;

	/**
	 * Virtualized Applicationのドメインモデルが実装すべきインターフェースです.
	 * <br>
	 * <p>VaViewerが参照するドメインです.
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 * 
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IVaDomain
	{
		/** 描画データ */
		function get topology():Topology;
		/** 描画要素 */
		function get elements():IList;
		/**
		 * 描画要素を選択します.
		 * @param vm
		 */
		function selectElement(element:Object):void
		/**
		 * 選択を解除します.
		 */
		function unselectElement():void
	}
}