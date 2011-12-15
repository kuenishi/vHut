param($csvpath=".\createVhutNetwork.csv", $eth="eth3") 

process
{

	Write-Host $csvpath
	
	# ネットワーク情報CSVファイルの読み込み
	$nwDefinitions = Import-CSV $csvpath


	# ネットワークの作成
	foreach ( $nwDefinition in $nwDefinitions )
	{
		Write-Host "作成中:$nwDefinition.name"
		
		$clusterName = $nwDefinition.clusterName

		$cluster = Select-Cluster -SearchText name=$clusterName

		$clusterId=$cluster.ClusterID

		$network = Get-Networks -DataCenterId $cluster.DataCenterId | ? { $_.Name -eq $nwDefinition.name }

		Write-Host "network:$network"

		if ($network -eq $null)
		{		
			$network = Add-Network -Name $nwDefinition.name -DataCenterId $cluster.DataCenterId -Address $nwDefinition.address -NetMask $nwDefinition.mask -VlanId $nwDefinition.vlan -Gateway $nwDefinition.gateway
		}

		$networkInCluster = Get-Networks -ClusterId $clusterId | ? { $_.Name -eq $nwDefinition.name }
		
		if ($networkInCluster -eq $null)
		{		
			Add-NetworkToCluster -Cluster $cluster -Network $network
		}
		
		$rhevHosts = Select-Host | ? { $_.HostClusterId -eq $cluster.ClusterID }
		
		foreach ( $rhevHost in $rhevHosts )
		{
			$adapter = $rhevHost.GetNetworkAdapters() | ? { $_.name -eq $eth }
			Attach-LogicalNetworkToNetworkAdapter -HostObject $rhevHost -Network $network -NetworkAdapter $adapter
		}
		
		Write-Host "完了:$nwDefinition.name"
	}
}


 # =====================================================================
 # 
 #    Copyright 2011 NTT Sofware Corporation
 # 
 #    Licensed under the Apache License, Version 2.0 (the "License");
 #    you may not use this file except in compliance with the License.
 #    You may obtain a copy of the License at
 # 
 #        http://www.apache.org/licenses/LICENSE-2.0
 # 
 #    Unless required by applicable law or agreed to in writing, software
 #    distributed under the License is distributed on an "AS IS" BASIS,
 #    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 #    See the License for the specific language governing permissions and
 #    limitations under the License.
 # 
 # =====================================================================
