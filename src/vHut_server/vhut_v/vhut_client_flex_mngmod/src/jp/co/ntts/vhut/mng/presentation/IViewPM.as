package jp.co.ntts.vhut.mng.presentation
{
	/**
	 * ViewとしてModulePMにインクルードされるPMのインターフェース
	 *
	 * @internal
	 * $Date$
	 * $Revision$
	 * $Author$
	 *
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IViewPM
	{
		/** 有効である */
		function set isActivate(value:Boolean):void;
		function get isActivate():Boolean;
	}
}