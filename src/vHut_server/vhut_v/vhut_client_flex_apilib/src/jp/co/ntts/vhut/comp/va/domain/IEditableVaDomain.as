package jp.co.ntts.vhut.comp.va.domain
{
	import jp.co.ntts.vhut.dto.AdditionalDiskDto;
	import jp.co.ntts.vhut.dto.SwitchTemplateDto;
	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.entity.Disk;
	import jp.co.ntts.vhut.entity.NetworkAdapter;
	import jp.co.ntts.vhut.entity.SecurityGroup;
	import jp.co.ntts.vhut.entity.Template;
	import jp.co.ntts.vhut.entity.Vm;
	import jp.co.ntts.vhut.entity.VmStatus;

	/**
	 * 編集可能なVirtualized Applicationのドメインモデルが実装すべきインターフェースです.
	 * <br>
	 * <p>VaEditorが参照するドメインです.
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 * 
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface IEditableVaDomain extends IControllableVaDomain
	{
		/**
		 * Vmを追加します.
		 * @param template 参照するテンプレート
		 * @return Vm
		 */
		function addVm(template:Template):Vm
			
		/**
		 * Vmを削除します.
		 * @param vm VM
		 * @return Vm
		 */
		function removeVm(vm:Vm):Vm
			
		/**
		 * SecurityGroupを追加します.
		 * @param template 参照するスイッチテンプレート
		 * @return セキュリティグループ
		 */
		function addSecurityGroup(template:SwitchTemplateDto):SecurityGroup
		
		/**
		 * SecurityGroupを削除します.
		 * @param vm VM
		 * @return セキュリティグループ
		 */
		function removeSecurityGroup(sg:SecurityGroup):SecurityGroup
		
		/**
		 * NetworkAdapterを追加します.
		 * @param vm 接続するVm
		 * @param securityGroup 接続するSecurityGroup
		 * @return ネットワークアダプター
		 */
		function addNetworkAdapter(vm:Vm, sg:SecurityGroup):NetworkAdapter
		
		/**
		 * NetworkAdapterを削除します.
		 * @param networkAdapter 切断するネットワークアダプター
		 * @return ネットワークアダプター
		 */
		function removeNetworkAdapter(networkAdapter:NetworkAdapter):NetworkAdapter
		
		/**
		 * Diskを追加します.
		 * @param vm ディスクを追加するVm
		 * @param template 追加ディスクテンプレート
		 * @return ディスク
		 */
		function addDisk(vm:Vm, template:AdditionalDiskDto):Disk
		
		/**
		 * Diskを削除します.
		 * @param disk 削除するディスク
		 * @return ディスク
		 */
		function removeDisk(disk:Disk):Disk
	}
}