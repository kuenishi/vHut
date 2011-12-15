package jp.co.ntts.vhut.comp.va.domain
{
	import flash.geom.Point;
	import flash.geom.Rectangle;

	import mx.collections.IList;

	/**
	 * <p>VM表示の要素が実装すべきインターフェース.
	 *
	 * @internal
	 * $Date$
	 * $Revision$
	 * $Author$
	 *
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IVaElement
	{
		/** 種別（"vm" or "sg"） */
		function get type():String;

		/** 描画用要素ID */
		function set privateId(value:Number):void;
		function get privateId():Number;

		/** 描画用要素の位置 */
		function set pos(value:Point):void;
		function get pos():Point;

		/** 描画用要素の幅 */
		function set width(value:Number):void;
		function get width():Number;

		/** 描画用要素の高さ */
		function set height(value:Number):void;
		function get height():Number;

		/** 描画用要素の外形 */
		function get rect():Rectangle;

		/** 接続情報リスト */
		function set linkList(value:IList):void;
		function get linkList():IList;

		/** 北側の接続情報リスト */
		function get northLinkList():IList;
		/** 南側の接続情報リスト */
		function get southLinkList():IList;
		/** 西側の接続情報リスト */
		function get westLinkList():IList;
		/** 東側の接続情報リスト */
		function get eastLinkList():IList;
	}
}