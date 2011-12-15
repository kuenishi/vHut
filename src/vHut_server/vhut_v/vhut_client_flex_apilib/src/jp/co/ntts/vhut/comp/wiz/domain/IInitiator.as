package jp.co.ntts.vhut.comp.wiz.domain
{
	/**
	 * ステップの初期化処理を行います.
	 * <br>PresentationModelに実装されることを想定しています.
	 * このインターフェースを実装したPMをWizardStep にセットすると遷移の後に実行されます。
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 * 
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IInitiator
	{
		function initiate():void
	}
}