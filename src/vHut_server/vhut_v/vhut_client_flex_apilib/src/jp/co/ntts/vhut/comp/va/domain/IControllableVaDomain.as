package jp.co.ntts.vhut.comp.va.domain
{
	import jp.co.ntts.vhut.entity.Vm;

	/**
	 * 制御可能なVirtualized Applicationのドメインモデルが実装すべきインターフェースです.
	 * <br>
	 * <p>VaControllerが参照するドメインです.
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 * 
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IControllableVaDomain extends IVaDomain
	{
		/**
		 * Vmを起動します.
		 * @param vm Vm
		 * @return Vm
		 */
		function startVm(vm:Vm):Vm
		
		/**
		 * Vmを停止します.
		 * @param vm Vm
		 * @return Vm
		 */
		function stopVm(vm:Vm):Vm
	}
}