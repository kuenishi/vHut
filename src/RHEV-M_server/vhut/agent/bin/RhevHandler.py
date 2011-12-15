# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

import clr
import sys
import datetime
from VhutLogger import TranLog
from VhutLogger import TranDataLog
from VhutLogger import SysLog
from DataValidator import *
from ReadConfigFile import *
import xmlrpclib
from ConfigParser import ConfigParser
import os
from RhevAgentException import RhevAgentException
from xmlrpclib import Fault
import traceback
import re

from System import AppDomain
from System import Configuration

clr.AddReference('System.Management.Automation')
clr.AddReference('IronPython')

from System.Management.Automation import *
from System.Management.Automation.Host import *
from System.Management.Automation.Runspaces import *

_conf = RunspaceConfiguration.Create()
_conf.AddPSSnapIn("RHEVMPSSnapin")
_runspace = RunspaceFactory.CreateRunspace(_conf)
_runspace.Open()

def _Invoke_command(command1, command2):
    """
    引数で受け取ったPowerSHellコマンドを実行して結果を返す。
    
    引数: command
    
    返り値: result
    """
    
#    実行速度計測Start
#    start = datetime.datetime.now()
    
    try:
        #RHEVの死活監視を行う
        #コマンドを記述した外部ファイルを実行しインスタンスを得る
        _pipe = _runspace.CreatePipeline()
        _cmd = Command(get_alive_monitoring_rhev_ps1())
        _pipe.Commands.Add(_cmd)
        #コマンドを実行
        _objects = _pipe.Invoke()
        
        #ランスペースを作成しコマンドを渡す
        _pipe = _runspace.CreatePipeline()
        _pipe.Commands.Add(command1)
        
        if command2 != None:
            _pipe.Commands.Add(command2)
        
        #トランザクションログ出力
        TranLog(("%s INFO %s %s REQUEST") % ("0000000000000/0", "RHEV API", command1))
        if command2 != None:
            TranLog(("%s INFO %s %s REQUEST") % ("0000000000000/0", "RHEV API", command2))
        #トランザクションデータログ出力
        TranDataLog(("%s INFO %s %s REQUEST\n%s") % ("0000000000000/0", "RHEV API", command1, ""))
        if command2 != None:
            TranDataLog(("%s INFO %s %s REQUEST\n%s") % ("0000000000000/0", "RHEV API", command1, ""))
        
        #コマンドを実行
        _objects = _pipe.Invoke()
        
        #トランザクションログ出力
        TranLog(("%s INFO %s %s REPLY") % ("0000000000000/0", "RHEV API", command1))
        if command2 != None:
            TranLog(("%s INFO %s %s REPLY") % ("0000000000000/0", "RHEV API", command2))
        #トランザクションデータログ出力
        TranDataLog(("%s INFO %s %s REPLY\n%s") % ("0000000000000/0", "RHEV API", command1, ""))
        if command2 != None:
            TranDataLog(("%s INFO %s %s REPLY\n%s") % ("0000000000000/0", "RHEV API", command1, ""))
            
        return _objects        
    
    #初回およびログインタイムアウトの場合にログイン処理を行う
    except Exception, e:
        
        #例外のメッセージで例外を分類
#        if str(e).startswith("Select-Cluster : ") and str(e).endswith("the system administrator.\n\n"):
        if str(e).endswith("the system administrator.\n\n"):
            _cmd = Command("Login-User")
            _cmd.Parameters.Add("Domain", "ntts.local")
            _cmd.Parameters.Add("UserName", "rhevadmin@ntts.local")
            _cmd.Parameters.Add("Password", "PASSWORD")
            
            _pipe = _runspace.CreatePipeline()
            _pipe.Commands.Add(_cmd)
            _objects = _pipe.Invoke()
            
            try:
                
                #コマンド再実行 
                _pipe = _runspace.CreatePipeline()
                _pipe.Commands.Add(command1)
                
                if command2 != None:
                    _pipe.Commands.Add(command2)
    
                _objects = _pipe.Invoke()
            
    
                #トランザクションログ出力
                TranLog(("%s INFO %s %s REPLY") % ("0000000000000/0", "RHEV API", command1))
                if command2 != None:
                    TranLog(("%s INFO %s %s REPLY") % ("0000000000000/0", "RHEV API", command2))
                #トランザクションデータログ出力
                TranDataLog(("%s INFO %s %s REPLY\n%s") % ("0000000000000/0", "RHEV API", command1, ""))
                if command2 != None:
                    TranDataLog(("%s INFO %s %s REPLY\n%s") % ("0000000000000/0", "RHEV API", command1, ""))
                        
                return _objects
                    
            except Exception, e:
                    #仮想マシン作成
                    if str(e).startswith("Add-Vm : Cannot add VM.") and str(e).endswith("VM with the same name already exists.\n\n"):
                        return e
                    #仮想マシン削除
                    elif str(e).startswith("Failed locating Vm with id"):
                        return e
                    #ネットワークアダプタ追加
                    elif str(e).endswith("Network interface is already in use\n\n"):
                        return e
                    #ユーザ追加
                    elif str(e).endswith("Attach-user : User is already attached to VM\n\n"):
                        return e
                    #ユーザ削除
                    elif str(e).startswith("Remove-User : The user is not attached to this VM."):
                        return e        
                    #仮想マシン起動
                    elif str(e).startswith("Start-Vm : Cannot run VM. VM is running."):
                        return e
                    #仮想マシン停止
                    elif str(e).startswith("Stop-Vm : Cannot stop VM. VM is not running."):
                        return e
                    #仮想マシンシャットダウン
                    elif str(e).startswith("Shutdown-Vm : Cannot shutdown VM. VM is not running."):
                        return e
                    #テンプレート作成
                    elif str(e).startswith("A template with the same name already exists."):
                        return e
                    #テンプレート削除
                    elif str(e).startswith("Cannot find Template with id"):
                        return e
                    
                    else:
                        TranDataLog(("%s ERROR %s %s\n%s") % ("0000000000000/0", "_Invoke_command Method, RHEV Connection Failure", str(e), traceback.format_exc()))
                        SysLog("ERR" , "END", "0000000000000/0", "_Invoke_command Method, RHEV Connection Failure", str(e))
                        raise RhevAgentException(3, "_Invoke_command Method")

#                print traceback.print_exc(file=sys.stdout)
#                print traceback.format_exc()
#                print traceback.print_stack()
        else:      
            
            #仮想マシン作成
            if str(e).startswith("Add-Vm : Cannot add VM.") and str(e).endswith("VM with the same name already exists.\n\n"):
                return e
            #仮想マシン削除
            elif str(e).startswith("Failed locating Vm with id"):
                return e
            #ネットワークアダプタ追加
            elif str(e).endswith("Network interface is already in use\n\n"):
                return e
            #ユーザ追加
            elif str(e).endswith("Attach-user : User is already attached to VM\n\n"):
                return e
            #ユーザ削除
            elif str(e).startswith("Remove-User : The user is not attached to this VM."):
                return e        
            #仮想マシン起動
            elif str(e).startswith("Start-Vm : Cannot run VM. VM is running."):
                return e
            #仮想マシン停止
            elif str(e).startswith("Stop-Vm : Cannot stop VM. VM is not running."):
                return e
            #仮想マシンシャットダウン
            elif str(e).startswith("Shutdown-Vm : Cannot shutdown VM. VM is not running."):
                return e
            #テンプレート作成
            elif str(e).startswith("A template with the same name already exists."):
                return e
            #テンプレート削除
            elif str(e).startswith("Cannot find Template with id"):
                return e    

            else:
                
                TranDataLog(("%s ERROR %s %s\n%s") % ("0000000000000/0", "_Invoke_command Method, RHEV Connection Failure", str(e), traceback.format_exc()))
                SysLog("ERR" , "END", "0000000000000/0", "_Invoke_command Method, RHEV Connection Failure", str(e))
                raise RhevAgentException(3, "_Invoke_command Method")
        
#    実行速度計測End
#    end = datetime.datetime.now()

#    実行速度計測result
#    print end -start    
    

def _getErrorMessageforPowerShell():
    """
        PowerShellで発生した直前のエラーを取得する。
        引数: なし
　　    戻り値: errorMessage
    """
#    _pipe = _runspace.CreatePipeline()

    #コマンド作成
    _cmd = Command("Get-Variable")
    _cmd.Parameters.Add("Name", "Error")
#    _pipe.Commands.Add(_cmd)

    #コマンドを実行
    _objects = _Invoke_command(_cmd, None)
#    _objects = _pipe.Invoke()

    errorMessage = _objects[0].BaseObject.Value.ToArray()

    return errorMessage

#print _getErrorMessageforPowerShell()


def _convertNoneToNull(judgeData):
    """
        引数がPython独自の定数Noneだった場合、''(空文字)に変換して返却する。
        引数: judgeData
　　    戻り値: data
    """
    if judgeData == None:
        data = ''
    else:
        data = judgeData
    
    return data
    
def _addColonInMacAddress(macAddress):
    """
        引数で受け取ったMacアドレスにコロンを付与する。
        引数: macAddress
　　    戻り値: コロンを付与したMacAddress
    """
#    pattern = re.compile("([a-f][0-9]){2}")
#    pattern = re.compile("\w{2}")
#    data = pattern.sub("([a-f][0-9]){2}",  "*" ,notIncludeColonMac)
#    data = re.sub("\w{2}", ":", notIncludeColonMac)

    # とりあえず力技で
    tmp = macAddress[0:2] + ":"
    tmp = tmp + macAddress[2:4] + ":"
    tmp = tmp + macAddress[4:6] + ":"
    tmp = tmp + macAddress[6:8] + ":"
    tmp = tmp + macAddress[8:10] + ":"
    tmp = tmp + macAddress[10:12]
    
    data = tmp
    return data

#print _addColonInMacAddress("001A4A000001")


def _deleteColonInMacAddress(macAddress):
    """
        引数で受け取ったMacアドレスのコロンを削除する。
        引数: macAddress
　　    戻り値: コロンを削除したMacAddress
    """
    data = macAddress.replace(":", "")
    return data
    
#print _deleteColonInMacAddress("00:1A:4A:00:00:01")

def _get_last_commandid():
    """
    直前の非同期処理のタスクIDを提示する。
    
    引数:
    
    返り値: commandId
    
    処理概要：
    1) Get-LastCommandTasksを実行
    """        
    _cmd = Command("Get-LastCommandTasks")        
    _objects = _Invoke_command(_cmd, None)
    
    output = []
    for cnt in _objects:
        output.append(cnt.Members["TaskId"].Value)
    
    if len(output)!=0:
        return output[0]

    else:
        return ''
    
def get_all_clusters():
    """
    全てのクラスタを提示する。(同期処理)
    
    引数:
    
    返り値: clusterList クラスタのリスト
    
    処理概要:
    1) Select-Clusterを実行
    """
   
    #RHEVのAPI実行
    _objects = _Invoke_command(Command("Select-Cluster"), None)
    
    #ドライバに送るデータ　辞書型
    output = []
    clusterList = []
    output.append(dict({'commandId':''}))

    for cnt in _objects:
        #データを抜き出して辞書に格納
        clusterList.append(dict({
                    'clusterId':cnt.Members["ClusterID"].Value,
                    'name':cnt.Members["Name"].Value,
                     }))
        
    #outputデータにclusterListを追加する
    output.append(dict({'clusterList':clusterList}))
          
    #ドライバに送るデータのバリデーション
    validate_datamodel("Cluster", output , "r2v")
        
    return output
    
#print get_all_clusters()

def get_hosts_by_cluster_id(clusterId):
    """
    クラスタIDを指定して関連するホストを提示する。(同期処理)
    
    引数: clusterId クラスタID
    
    返り値: hostList ホストのリスト
    
    処理概要: 
    1. Select-Host | Where-Object -FilterScript {$_.HostClusterId -eq clusterId}を実行
    """
    
    #受け取ったデータのバリデーションを行う。
    validate_id(["cluster"], [clusterId], "v2r")

    #RHEVのAPI実行
    _cmd1 = Command("Select-Host")
    
    _cmd2 = Command("Where-Object")
    _cmd2.Parameters.Add("FilterScript", ScriptBlock.Create("$_.HostClusterId -eq %s" % (clusterId)))
#    _cmd2.Parameters.Add("FilterScript", ("{$_.HostClusterId -eq %s}" % (clusterId)))
    
    _objects = _Invoke_command(_cmd1, _cmd2)
    
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    hostList = []
    
    for cnt in _objects:
        #データを抜き出して辞書に格納
        hostList.append(dict({
                             'hostId':cnt.Members["HostId"].Value,
                             'name':cnt.Members["Name"].Value,
                             'status':cnt.Members["Status"].Value,
                             'cpuCore':cnt.Members["GetCpuStatistics"].Invoke()[0].Cores,
                             'memory':cnt.Members["GetMemoryStatistics"].Invoke().PhysicalMemory,
                             'cpuUsage':100 - int(cnt.Members["GetCpuStatistics"].Invoke().ToArray()[0].Idle),
                             'memoryUsage':cnt.Members["GetMemoryStatistics"].Invoke().UsagePercents,          
                             'clusterId':cnt.Members["HostClusterId"].Value,
                             }))
    output.append(dict({'hostList':hostList}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("Host", output, "r2v")
    
    
    return output

#print get_hosts_by_cluster_id(0)

def get_networks_by_cluster_id(clusterId):
    """
    クラスタIDを指定して関連するネットワークを提示する。(同期処理)
    
    引数: clusterId クラスタID
    
    返り値: networkList ネットワークのリスト
    """
            
    #受け取ったデータのバリデーションを行う。
    validate_id(["cluster"], [clusterId], "v2r")
    
    #RHEVのAPI実行
    _cmd = Command("Get-Networks")
    _cmd.Parameters.Add("ClusterId", clusterId)
    _objects = _Invoke_command(_cmd, None)
       
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    networkList = []
    
    for cnt in _objects:
        
        #VLAN以外のネットワークは除外する
#        if cnt.Members["Name"].Value.ToString() != 'rhevm..' and cnt.Members["Name"].Value.ToString() != 'Management' and cnt.Members["Name"].Value.ToString() != 'Storage':
        if str(cnt.Members["Name"].Value).startswith("VN"):

            #データを抜き出して辞書に格納
            networkList.append(dict({
#                                    'networkId':cnt.Members["NetworkId"].Value.ToString(),
                                    'networkId':cnt.Members["Name"].Value.ToString(),
                                    'vlan':_convertNoneToNull(cnt.Members["VlanId"].Value),
                                    'networkAddress':_convertNoneToNull(cnt.Members["Address"].Value),
                                    'subnet':_convertNoneToNull(cnt.Members["Subnet"].Value),
                                    'gateway':_convertNoneToNull(cnt.Members["Gateway"].Value),
                                    'status':cnt.Members["Status"].Value.ToString(),
                                    }))
    output.append(dict({"networkList":networkList}))
    
    #ドライバに送るデータのバリデーション
    validate_datamodel("Network", output, "r2v")
    
    return output
    
#print get_networks_by_cluster_id(0)


def get_all_data_storages():
    """
    全ての(データ)ストレージを提示する。(同期処理)
    
    引数:
    
    返り値: storageList ストレージのリスト
    """
    
    #RHEVのAPI実行
    _objects = _Invoke_command(Command("Select-Storagedomain"), None)
       
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    storageList = []
    for cnt in _objects:
        #データを抜き出して辞書に格納
        storageList.append(dict({
                                'storageId':cnt.Members["StorageDomainId"].Value,
                                'name':cnt.Members["Name"].Value,
                                'status':_convertNoneToNull(cnt.Members["Status"].Value),
                                'availableSize':cnt.Members["AvailableDiskSize"].Value,
                                'commitedSize':cnt.Members["CommittedDiskSize"].Value,
                                'usedDiskSize':cnt.Members["UsedDiskSize"].Value,
                               }))
        
    output.append(dict({"storageList":storageList}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("Storage", output, "r2v")

    return output

#print get_all_data_storages()


def get_vms_by_cluster_id(clusterId):
    """
    クラスタIDを指定して関連する仮想マシンを提示する。(同期処理)
    
    引数: clusterId クラスタID
    
    返り値: vmList 仮想マシンのリスト
    """
    
    #受け取ったデータのバリデーションを行う。
    validate_id(["cluster"], [clusterId], "v2r")
    
    #RHEVのAPI実行
    _cmd1 = Command("Select-Vm")
    
    _cmd2 = Command("Where-Object")
    _cmd2.Parameters.Add("FilterScript", ScriptBlock.Create("$_.HostClusterId -eq %s" % clusterId))
    
    _objects = _Invoke_command(_cmd1, _cmd2)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    _networks = _Invoke_command(Command("Get-Networks"), None)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    networkDict = {}
    for obj in _networks:
        networkDict.update(dict({obj.Members["name"].Value:obj.Members["NetworkId"].Value.ToString(),
                                 }))
            
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    vmList = []
    for cnt in _objects:

        #vHut管理外のVMは除外する
        if str(cnt.Members["Name"].Value).startswith("AP") or str(cnt.Members["Name"].Value).startswith("AI"):

            #データを抜き出して辞書に格納
            vm = dict({
                        'vmId':cnt.Members["VmId"].Value,
                        'name':cnt.Members["Name"].Value,
                        'description':_convertNoneToNull(cnt.Members["Description"].Value),
                        'cpuCore':cnt.Members["NumOfSockets"].Value * cnt.Members["NumOfCpusPerSocket"].Value,
                        'memory':cnt.Members["MemorySize"].Value,
                        'cpuUsage':100 - int(cnt.Members["GetCpuStatistics"].Invoke().Idle),
                        'memoryUsage':cnt.Members["GetMemoryStatistics"].Invoke().usageMemPercent,
                        'status':cnt.Members["Status"].Value,
                        'os':cnt.Members["OperatingSystem"].Value,
                        'templateId':cnt.Members["TemplateId"].Value,
                        'hostId':cnt.Members["RunningOnHost"].Value,
                        'clusterId':cnt.Members["HostClusterId"].Value,
                        })
        
            #networkAdapterList作成
            networkAdapterList = []
            for na in cnt.Members["GetNetworkAdapters"].Invoke().ToArray():

                #networkAdapterList作成
                #network名を取得してnetworkDictの辞書からvalueを引っ張る
                networkName = na.Network
                networkId = networkDict[networkName]
            
                networkAdapter = dict({
                                       'networkAdapterId':na.Id,
                                       'name':na.Name,
                                       'macAddress':na.MacAddress,
#                                       'networkId':networkId,
#                                       'networkId':networkName,
                                       })
                networkAdapterList.append(networkAdapter)
            vm.update(dict({"networkAdapterList":networkAdapterList}))
        
            #diskList作成
            diskList = []
            for di in cnt.Members["GetDiskImages"].Invoke().ToArray():
                 disk = dict({
                             'diskId':di.SnapshotId,
                             'size':int(di.SizeInGB),
                             })
                 diskList.append(disk)
            
            #vmを更新
            vm.update(dict({"diskList":diskList}))
        
            #vmリストに追加
            vmList.append(vm)
        
        
    output.append(dict({"vmList":vmList}))
    
    #ドライバに送るデータのバリデーション
    if len(vmList) != 0:
        validate_datamodel("Vm", output, "r2v")
    
    return output

#print get_vms_by_cluster_id(0)


def get_templates_by_cluster_id(clusterId):
    """
    クラスタIDを指定して関連するテンプレートを提示する。(同期処理)
    
    引数: clusterId クラスタID
    
    返り値: templateList テンプレートのリスト
    """
    
    #受け取ったデータのバリデーションを行う。
    validate_id(["cluster"], [clusterId], "v2r")
    
    #RHEVのAPI実行
    _cmd1 = Command("Select-Template")
    
    _cmd2 = Command("Where-Object")
    _cmd2.Parameters.Add("FilterScript", ScriptBlock.Create("$_.HostClusterId -eq %s" % clusterId))        

    _objects = _Invoke_command(_cmd1, _cmd2)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    #RHEVのAPI実行
    _networks = _Invoke_command(Command("Get-Networks"), None)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    networkDict = {}
    for obj in _networks:
        networkDict.update(dict({obj.Members["name"].Value:obj.Members["NetworkId"].Value.ToString(),
                                 }))  
    
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    templateList = []
    template = ''
    for cnt in _objects:

        #RHEVが初期で持っているblankテンプレートは除外する？
        if cnt.Members["Name"].Value != "Blank............":

            #データを抜き出して辞書に格納
            template = dict({
                            'templateId':cnt.Members["TemplateId"].Value,
                            'name':cnt.Members["Name"].Value,
                            'description':cnt.Members["Description"].Value,
                            'cpuCore':cnt.Members["NumOfSockets"].Value * cnt.Members["NumOfCpusPerSocket"].Value,
                            'memory':cnt.Members["MemSizeMb"].Value,
                            'status':_convertNoneToNull(cnt.Members["Status"].Value.ToString()),
                            'os':cnt.Members["OperatingSystem"].Value,
                            'clusterId':cnt.Members["HostClusterId"].Value,
                            })
        
            #networkAdapterTemplateList作成
            networkAdapterTemplateList = []
            for na in cnt.Members["GetNetworkAdapters"].Invoke().ToArray():
            
                #network名を取得してnetworkDictの辞書からvalueを引っ張る
                networkName = na.Network
                networkId = networkDict[networkName]
        
        
                networkAdapterTemplate = dict({'networkAdapterId':na.Id,
                                       'name':na.Name,
                                       'macAddress':_convertNoneToNull(na.MacAddress),
#                                       'networkId':networkId
                                       })
                #networkAdapterTemplateListに追加
                networkAdapterTemplateList.append(networkAdapterTemplate)
            #templateを更新
            template.update(dict({"networkAdapterTemplateList":networkAdapterTemplateList}))
        
        
            #diskTemplateList作成
            diskTemplateList = []
            for di in cnt.Members["GetDiskImages"].Invoke().ToArray():
                diskTemplate = dict({
                             'diskId':di.SnapshotId,
                             'size':int(di.SizeInGB),
                             })
                diskTemplateList.append(diskTemplate)
       
            template.update(dict({"diskTemplateList":diskTemplateList}))

            #templateリストに追加
            templateList.append(template)
        
    output.append(dict({"templateList":templateList}))      
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("Template", output, "r2v")

    return output

#print get_templates_by_cluster_id(0)


def get_all_users():
    """
    全てのユーザを提示する。(同期処理)
    
    引数: 
    
    返り値: cloudUserList ネットワークのリスト
    """
    
    #RHEVのAPI実行
    _cmd = Command("Select-User")
    
    _objects = _Invoke_command(_cmd, None)

    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    cloudUserList = []
    for cnt in _objects:
        #データを抜き出して辞書に格納
        cloudUserList.append(dict({
                            'cloudUserId':_convertNoneToNull(cnt.Members["UserId"].Value),
                            'account':_convertNoneToNull(cnt.Members["UserName"].Value),
                            'lastName':_convertNoneToNull(cnt.Members["LastName"].Value),
                            'firstName':_convertNoneToNull(cnt.Members["Name"].Value),
                            'email':_convertNoneToNull(cnt.Members["Email"].Value),
                            }))
    output.append(dict({"cloudUserList":cloudUserList}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("CloudUser", output, "r2v")

    return output

#print get_all_users()

 
def get_tasks_by_task_ids(commandList):
    """
    タスクIDのリストに関連する全てのタスクを提示する。(同期処理)
    
    引数: commandList コマンドのリスト
    
    返り値: commandList コマンドのリスト
    """
    #受け取ったデータのバリデーションを行う。
    validate_data(["CommandList"], [commandList], "v2r")
    
    #RhevmCmd.CLITask型インスタンス作成
    #コマンドを記述した外部ファイルを実行しインスタンスを得る
    _cmd = Command(get_cli_task_instance())
    
    _taskInstance_ps = _Invoke_command(_cmd, None)

    _taskInstance = _taskInstance_ps[0].BaseObject

    #ドライバに送るデータ
    output = []
    output.append(dict({'commandId':''}))
    
    _commandList = []
    for command in commandList:
        
        #RHEVのAPI実行
        _taskInstance.TaskId = command
        
        _cmd = Command("Get-TasksStatus")
        _cmd.Parameters.Add("CommandTaskIdList", _taskInstance)
        _objects = _Invoke_command(_cmd, None)
    



        for cnt in _objects:
            #データを抜き出して辞書に格納
            _commandList.append(dict({
                                'commandId':command,
                                'status':cnt.Members["Status"].Value.ToString(),
                                }))
    
    output.append(dict({"commandList":_commandList}))

    
    #ドライバに送るデータのバリデーション
    validate_datamodel("Command", output, "r2v")
    
    return output


#_list = []
#_list.append("11070349-3a4a-4b58-8aae-dd2a7ead941e")
#_list.append("21070349-3a4a-4b58-8aae-dd2a7ead941a")
#
#print get_tasks_by_task_ids(_list)


def create_vm(templateId, name, description, cpuCore,
               memory, storageId, clusterId):
    """
    テンプレートから仮想マシンを作成する。(非同期処理)
    
    引数: templateId テンプレートID
          name 名前
          description 概要
          cpuCore CPUコア数
          memory メモリ容量
          storageId ストレージID
          clusterId クラスタID  
    
    返り値: commandId コマンドID
         vm 仮想マシン
            
    処理概要：
    1) テンプレートIDからテンプレートオブジェクトを取得する
    2) テンプレートオブジェクトをAdd-VMのパラメータで指定
    3) その他名前、概要等もパラメータで指定
    4) RHEVが指定のエラーを返した場合、冗送ロジックを実行する。
    """
    
    #受け取ったデータのバリデーションを行う。
    validate_id(["template", "storage", "cluster"], [templateId, storageId, clusterId], "v2r")
    validate_data(["name", "cpuCore", "memory"], [name, cpuCore, memory], "v2r")

    
    #templateオブジェクト取得
    _cmd = Command("Get-Template")
    _cmd.Parameters.Add("TemplateId", templateId)
    _templateObj = _Invoke_command(_cmd, None)
    _templateObj = _templateObj[0].BaseObject
    
    #暫定対処
    #name = "NewVm3"
    #description = "WindowsXPBaseTemplate"

    
    #RHEVのAPI実行
    _cmd = Command("Add-Vm")
    _cmd.Parameters.Add("TemplateObject", _templateObj)
    _cmd.Parameters.Add("Name", name)
    #Descriptionが空の場合はパラメータとして指定しない
    if description != '':
        _cmd.Parameters.Add("Description", description)
    _cmd.Parameters.Add("NumOfCpusPerSocket", cpuCore)
    _cmd.Parameters.Add("MemorySize", memory)
    _cmd.Parameters.Add("StorageDomainId", storageId)
    _cmd.Parameters.Add("HostClusterId", clusterId)
#    _cmd.Parameters.Add("VmType", "Server")
    _cmd.Parameters.Add("Async", True)
    
    
    _objects = _Invoke_command(_cmd, None) 
   
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Add-Vm : Cannot add VM.") and str(_objects).endswith("VM with the same name already exists.\n\n"):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _cmd = Command("Select-Vm")
        _cmd.Parameters.Add("SearchText", "Name=%s" % (name))
        _objects = _Invoke_command(_cmd, None)
            
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    _networks = _Invoke_command(Command("Get-Networks"), None)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    networkDict = {}
    for obj in _networks:
        networkDict.update(dict({obj.Members["name"].Value:obj.Members["NetworkId"].Value.ToString(),
                                 }))

    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':_get_last_commandid()}))

    for cnt in _objects:

        #データを抜き出して辞書に格納
        _vm = dict({
                    'vmId':cnt.Members["VmId"].Value,
                    'name':cnt.Members["Name"].Value,
                    'description':cnt.Members["Description"].Value,
                    'cpuCore':cnt.Members["NumOfSockets"].Value * cnt.Members["NumOfCpusPerSocket"].Value,
                    'memory':cnt.Members["MemorySize"].Value,
                    'cpuUsage':100 - int(cnt.Members["GetCpuStatistics"].Invoke().Idle),
                    'memoryUsage':cnt.Members["GetMemoryStatistics"].Invoke().usageMemPercent,
                    'status':cnt.Members["Status"].Value,
                    'os':cnt.Members["OperatingSystem"].Value,
                    'templateId':cnt.Members["TemplateId"].Value,
                    'hostId':cnt.Members["RunningOnHost"].Value,
                    'clusterId':cnt.Members["HostClusterId"].Value,
                     })
        
        #networkAdapterList作成
        networkAdapterList = []
        for na in cnt.Members["GetNetworkAdapters"].Invoke().ToArray():
        
            #network名を取得してnetworkDictの辞書からvalueを引っ張る
            networkName = na.Network
            networkId = networkDict[networkName]
        
            networkAdapter = dict({
                                   'networkAdapterId':na.Id,
                                   'name':na.Name,
                                   'macAddress':na.MacAddress,
                                   'networkId':networkId
                                   })
            #networkAdapterListに追加
            networkAdapterList.append(networkAdapter)
            
            #_vmを更新
        _vm.update(dict({"networkAdapterList":networkAdapterList}))

        #diskList作成
        diskList = []
        for di in cnt.Members["GetDiskImages"].Invoke().ToArray():
            disk = dict({
                         'diskId':di.SnapshotId,
                         'size':int(di.SizeInGB),
                         })
            #diskListに追加
            diskList.append(disk)
            
        _vm.update(dict({"diskList":diskList}))
        
    #outputに追加
    output.append(dict({"vm":_vm}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("Vm", output, "r2v")

    return output

#def create_vm(templateId, name, description, cpuCore,
#               memory, storageId, clusterId):

#print create_vm("623e4ea8-1122-432b-abc5-78404b301c1c", "testVmccc", "testdesu", 1, 512, "3260fafe-85b8-4765-9b0a-74c80b522d10", 0)
    
def delete_vm(vmId):
    """
    仮想マシンを削除する。(非同期処理)
    
    引数: vmId 仮想マシンID
    
    返り値: commandId コマンドID
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm"], [vmId], "v2r")
    
    #RHEVのAPI実行
    _cmd = Command("Remove-Vm")
    _cmd.Parameters.Add("VmId", vmId)
    _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)

    
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Failed locating Vm with id %s" % (vmId)):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        _objects = ""
            
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':_get_last_commandid()}))
    
  
    #ドライバに送るデータのバリデーション
    validate_id("command", output, "r2v")
    
    return output


#print delete_vm("60cdd77d-9a6f-4104-81c0-ec7ef166749e")
        
        
def change_spec(vmId, cpuCore, memory):
    """
    仮想マシンのスペックを変更する。(非同期処理)
    
    引数: vmId 仮想マシンID
          cpuCore CPUコア数
          memory メモリ容量  
    
    返り値: vm 仮想マシン
    
    処理概要：
    1) vmIdからVmオブジェクトを取得する
    2) update-vmでVmオブジェクト等をパラメータにして実行する
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id("vm", vmId, "v2r")
    
    #Vmオブジェクト取得
    _cmd = Command("Get-Vm")
    _cmd.Parameters.Add("VmId", vmId)
    _vmObj_ps = _Invoke_command(_cmd, None)
    _vmObj = _vmObj_ps[0].BaseObject
    _vmObj.NumOfCpusPerSocket = cpuCore
    _vmObj.MemorySize = memory
    
    
    #RHEVのAPI実行
    _cmd = Command("Update-Vm")
    _cmd.Parameters.Add("VmObject", _vmObj)
#    _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    _networks = _Invoke_command(Command("Get-Networks"), None)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    networkDict = {}
    for obj in _networks:
        networkDict.update(dict({obj.Members["name"].Value:obj.Members["NetworkId"].Value.ToString(),
                                 }))

    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))
    vm = []
    for cnt in _objects:

        #データを抜き出して辞書に格納
        _vm = dict({
                    'vmId':cnt.Members["VmId"].Value,
                    'name':cnt.Members["Name"].Value,
                    'description':cnt.Members["Description"].Value,
                    'cpuCore':cnt.Members["NumOfSockets"].Value * cnt.Members["NumOfCpusPerSocket"].Value,
                    'memory':cnt.Members["MemorySize"].Value,
                    'cpuUsage':100 - int(cnt.Members["GetCpuStatistics"].Invoke().Idle),
                    'memoryUsage':cnt.Members["GetMemoryStatistics"].Invoke().usageMemPercent,
                    'status':cnt.Members["Status"].Value,
                    'os':cnt.Members["OperatingSystem"].Value,
                    'templateId':cnt.Members["TemplateId"].Value,
                    'hostId':cnt.Members["RunningOnHost"].Value,
                    'clusterId':cnt.Members["HostClusterId"].Value,
                     })
        
        #networkAdapterList作成
        networkAdapterList = []
        for na in cnt.Members["GetNetworkAdapters"].Invoke().ToArray():
        
            #network名を取得してnetworkDictの辞書からvalueを引っ張る
            networkName = na.Network
            networkId = networkDict[networkName]
        
            networkAdapter = dict({
                                   'networkAdapterId':na.Id,
                                   'name':na.Name,
                                   'macAddress':na.MacAddress,
                                   'networkId':networkId
                                   })
            #networkAdapterListに追加
            networkAdapterList.append(networkAdapter)
            
            #_vmを更新
        _vm.update(dict({"networkAdapterList":networkAdapterList}))

        #diskList作成
        diskList = []
        for di in cnt.Members["GetDiskImages"].Invoke().ToArray():
            disk = dict({
                         'diskId':di.SnapshotId,
                         'size':int(di.SizeInGB),
                         })
            #diskListに追加
            diskList.append(disk)
            
        _vm.update(dict({"diskList":diskList}))
        
    #outputに追加
    output.append(dict({"vm":_vm}))
                
    #ドライバに送るデータのバリデーション
    validate_datamodel("Vm", output, "r2v")
    

    return output

#print change_spec("a4dd2757-05d3-4495-9957-655137891901", 2, 256)


#def add_network_adapter(vmId, name, networkId, macAddress):
def add_network_adapter(vmId, name, networkName, macAddress):
    """
    仮想マシンにネットワークアダプタを追加する。(同期処理)
    
    引数: vmId 仮想マシンID
          name 名前
          networkName ネットワーク名
          macAddress マックアドレス  
    
    返り値: networkAdapter ネットワークアダプタ
    
    処理概要：
    1) vmIdをもとにVmオブジェクトを取得する
#    2) networkIdをもとにNetworkのnameを取得する
    3) Add-NetworkAdapterのパラメータにVmオブジェクト等を指定して実行する
    """
    
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "network"], [vmId, networkName], "v2r")
    
    #Vmオブジェクト取得
    _cmd = Command("Get-Vm")
    _cmd.Parameters.Add("VmId", vmId)
    _vmObj_ps = _Invoke_command(_cmd, None)
    _vmObj = _vmObj_ps[0].BaseObject
    
    #NetworkのName取得
    _networks = _Invoke_command(Command("Get-Networks"), None)
    #networkの辞書(NetworIdとNameのセット)を作成する。
    networkDict1 = {}
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    networkDict2 = {}
    
    for obj in _networks:
        networkDict1.update(dict({obj.Members["NetworkId"].Value.ToString():obj.Members["name"].Value,
                                 }))
        networkDict2.update(dict({obj.Members["name"].Value:obj.Members["NetworkId"].Value.ToString(),
                                 }))
#    _networkName = networkDict1[networkId]


    #Add-NetworkAdapterを実行
    #RHEVのAPI実行
    _cmd = Command("Add-NetworkAdapter")
    _cmd.Parameters.Add("VmObject", _vmObj)
    _cmd.Parameters.Add("InterfaceName", name)
#    _cmd.Parameters.Add("NetworkName", _networkName)
    _cmd.Parameters.Add("NetworkName", networkName)
    _cmd.Parameters.Add("MacAddress", _addColonInMacAddress(macAddress))
    _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)
        
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
#        if str(e).startswith("Add-NetworkAdapter : Add-NetworkAdapter : Network interface is already in use"):
    if str(_objects).endswith("Network interface is already in use\n\n"):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _cmd = Command("Get-Vm")
        _cmd.Parameters.Add("VmId", vmId)
        _objects = _Invoke_command(_cmd, None)
        
        _objects = _objects[0].Members["GetNetworkAdapters"].Invoke().ToArray()
                
    output = []
    output.append(dict({'commandId':''}))
#    print _objects
    for cnt in _objects:
        try:
            networkName = cnt.Members["Network"].Value

#            networkId = networkDict2[networkName]
                    
            networkAdapter = dict({
                                'networkAdapterId':_convertNoneToNull(cnt.Members["Id"].Value),
                                'name':_convertNoneToNull(cnt.Members["Name"].Value),
                                'macAddress':_convertNoneToNull(_deleteColonInMacAddress(cnt.Members["MacAddress"].Value)),
#                                'networkId':networkId
                                'networkId':networkName

                                })
            
        #冗送対応時に発生するエラーをキャッチ
        except:
            networkName = cnt.Network
#            networkId = networkDict2[networkName]
                    
            networkAdapter = dict({
                                'networkAdapterId':_convertNoneToNull(cnt.Id),
                                'name':_convertNoneToNull(cnt.Name),
                                'macAddress':_convertNoneToNull(_deleteColonInMacAddress(cnt.MacAddress)),
#                                'networkId':networkId
                                'networkId':networkName
                                })
            
    output.append(dict({"networkAdapter":networkAdapter}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("NetworkAdapter", output, "r2v")

    return output

#add_network_adapter(vmId, name, networkId, macAddress):  
#print add_network_adapter("5f755c43-9d7e-4fcd-9bc4-aedbbc107abc", "defaultname", "VN2032", "001A4A000001")
    

def remove_network_adapter(vmId, networkAdapterId):
    """
    仮想マシンからネットワークアダプタを削除する。(同期処理)
    
    引数: vmId 仮想マシンID
          networkAdapterId ネットワークアダプタID  
    
    返り値: commandId タスクID
    
    処理概要：
    1) vmIdをもとにVmオブジェクトを取得する
    2) networkAdapterIdをもとにNetworkAdapterオブジェクトを取得する
    2) Remove_NetworkAdapterのパラメータにVmオブジェクトを指定して実行する
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "networkAdapter"], [vmId, networkAdapterId], "v2r")
    
    # Vmオブジェクトの取得
    _cmd = Command("Get-Vm")
    _cmd.Parameters.Add("VmId", vmId)
    _vmObj_ps = _Invoke_command(_cmd, None)
    _vmObj = _vmObj_ps[0].BaseObject
    
    _networkAdapters = _vmObj.GetNetworkAdapters()

    for _networkAdapter in _networkAdapters:
        if _networkAdapter.Id == networkAdapterId:
            _networkAdapterObj = _networkAdapter
            
    try:
        #RHEVのAPI実行
        _cmd = Command("Remove-NetworkAdapter")
        _cmd.Parameters.Add("VmObject", _vmObj)
        _cmd.Parameters.Add("NetworkAdapterObject", _networkAdapterObj)
    
        _objects = _Invoke_command(_cmd, None)
            
        #冗送対応
#        #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
#        if str(_objects).startswith("Remove-NetworkAdapter : 引数が null であるため、"):
#            _objects = ""
    except:
        _objects = "" 

    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))

    return output

#print remove_network_adapter(vmId, networkAdapterId):
#print remove_network_adapter("a4dd2757-05d3-4495-9957-655137891901", "59c50d69-e142-4c05-b2cc-ce5961c095b5")
    
def add_disk(vmId, size, storageId):
    """
    仮想マシンにディスクを追加する。(非同期処理)
    
    引数: vmId 仮想マシンID
          size ディスク容量
          storageId ストレージID  
    
    返り値: disk ディスク
    
    処理概要：
    1) vmIdをもとにVmオブジェクトを取得する
    2) sizeをもとにDiskオブジェクトを取得する
    3) Add-DiskのパラメータにVmオブジェクトを指定して実行する
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "storage"], [vmId, storageId], "v2r")
    validate_data(["size"] , [size] , "v2r")
    
    #vmIdをもとにVmオブジェクトを取得
    _cmd = Command("Get-Vm")
    _cmd.Parameters.Add("VmId", vmId)
    _vmObj_ps = _Invoke_command(_cmd, None)
    _vmObj = _vmObj_ps[0].BaseObject
    
    #sizeをもとにDiskオブジェクトを取得
    _cmd = Command("New-Disk")
    _cmd.Parameters.Add("DiskSize", size)
    _cmd.Parameters.Add("DiskType", "Data")
    _cmd.Parameters.Add("DiskInterface", "VirtIO")
    _diskObj_ps = _Invoke_command(_cmd, None)
    _diskObj = _diskObj_ps[0].BaseObject
  
    try:
        #RHEVのAPI実行
        _cmd = Command("Add-Disk")
        _cmd.Parameters.Add("VmObject", _vmObj)
        _cmd.Parameters.Add("DiskObject", _diskObj)
        _cmd.Parameters.Add("Async", True)
        
        _objects = _Invoke_command(_cmd, None)
    
    except Exception, e:
        #冗送対応
        #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
        if str(e).startswith("Add-Disk : There can be only one bootable disk defined."):
            #トランザクションログ出力
            TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
            #トランザクションデータログ出力
            TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
            
            _objects = _vmObj.Members["GetDiskImages"].Invoke()[0]
        else:
            raise        

    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':_get_last_commandid()}))

    for cnt in _objects:
        #データを抜き出して辞書に格納
        disk = dict({
                    'diskId':cnt.Members["SnapshotId"].Value,
                    'size':int(cnt.Members["SizeInGB"].Value),
                     })
    output.append(dict({"disk":disk}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("Disk", output, "r2v")
    
    
    return output

#def add_disk(vmId, size, storageId):
#print add_disk("a4dd2757-05d3-4495-9957-655137891901", 1, "3260fafe-85b8-4765-9b0a-74c80b522d10")

    
def remove_disk(vmId, diskId):
    """
    仮想マシンからディスクを削除する。(非同期処理)
    
    引数: vmId 仮想マシンID
          diskId ディスクID  
    
    返り値: commandId コマンドID
    
    処理概要:
    1) vmIdをパラメータにRemove-Diskを実行する
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "disk"], [vmId, diskId], "v2r")
    
    
    try:
        #RHEVのAPI実行
        _cmd = Command("Remove-Disk")
        _cmd.Parameters.Add("VmId", vmId)
        _cmd.Parameters.Add("DiskIds", diskId)
        _cmd.Parameters.Add("Async", True)
        
        _objects = _Invoke_command(_cmd, None)

    except Exception, e:
        #冗送対応
        #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
        if str(e).startswith("Remove-Disk : Remove-Disk : Cannot remove Virtual Machine Disk"):
            #トランザクションログ出力
            TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
            #トランザクションデータログ出力
            TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
            
            _objects = ""

    #ドライバに送るデータ　辞書型
    output = []
    #データを抜き出して辞書に格納
    output.append(dict({'commandId':_get_last_commandid()
                           }))
    #ドライバに送るデータのバリデーション
    validate_id("command", output, "r2v")
    
    
    return output

#remove_disk(vmId, diskId)
#print remove_disk("a4dd2757-05d3-4495-9957-655137891901", "027ca9cc-70f0-4879-bdcb-baf479e6b3c0")

def add_user(vmId, cloudUserId):
    """
    仮想マシンにユーザを追加する。(同期処理)
    
    引数: vmId 仮想マシンID
          cloudUserId ユーザID  
    
    返り値: cloudUser ユーザ
    
    処理概要：
    1) cloudUserIdからUserオブジェクトを取得する
    2) Attach-UserのパラメータにUserオブジェクトとvmIdを指定して実行する
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "cloudUser"], [vmId, cloudUserId], "v2r")
    
    #Userオブジェクトを取得する
    _cmd = Command("Get-User")
    _cmd.Parameters.Add("UserId", cloudUserId)
    _userObj_ps = _Invoke_command(_cmd, None)
    _userObj = _userObj_ps[0].BaseObject
    
    #RHEVのAPI実行
    _cmd = Command("Attach-user")
    _cmd.Parameters.Add("UserObject", _userObj)
    _cmd.Parameters.Add("VmId", vmId)
    
    _objects = _Invoke_command(_cmd, None)
    
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
#    if str(_objects).startswith("Attach-User : User is already attached to VM"):
    if str(_objects).endswith("Attach-user : User is already attached to VM\n\n"):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _cmd = Command("Get-User")
        _cmd.Parameters.Add("UserId", cloudUserId)
        _objects = _Invoke_command(_cmd, None)

    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':''}))

    for cnt in _objects:
        #正規表現でデータを抜き出して辞書に格納
        cloudUser = dict({
                    'cloudUserId':_convertNoneToNull(cnt.Members["UserId"].Value),
                    'account':_convertNoneToNull(cnt.Members["UserName"].Value),
                    'lastName':_convertNoneToNull(cnt.Members["LastName"].Value),
                    'firstName':_convertNoneToNull(cnt.Members["Name"].Value),
                    'email':_convertNoneToNull(cnt.Members["Email"].Value),
                    })
    output.append(dict({"cloudUser":cloudUser}))

    #ドライバに送るデータのバリデーション
    validate_datamodel("CloudUser", output, "r2v")
    
    return output

#add_user(vmId, cloudUserId)
#print add_user("a4dd2757-05d3-4495-9957-655137891901", "e669b6d2-5660-4070-9d32-fb88c4a49657")

def remove_user(vmId, cloudUserId):
    """
    仮想マシンからユーザを削除する。(同期処理)
    
    引数: vmId 仮想マシンID
          cloudUserId ユーザID  
    
    返り値: commandId タスクID
            
    処理概要：
    1) cloudUserIdからUserオブジェクトを取得する
    2) Attach-UserのパラメータにcloudUserIdとvmIdを指定して実行する
    """
    
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "user"], [vmId, cloudUserId], "v2r")
    
    #RHEVのAPIを実行
    _cmd = Command("Remove-User")
    _cmd.Parameters.Add("UserId", cloudUserId)
    _cmd.Parameters.Add("VmId", vmId)
    
    _objects = _Invoke_command(_cmd, None)
    
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Remove-User : The user is not attached to this VM."):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _objects = ""

    #ドライバに送るデータ　辞書型
    output = []
#    for cnt in _objects:
#        #データを抜き出して辞書に格納
    output.append(dict({'commandId':'',
                           }))
        
    return output

#print remove_user("a4dd2757-05d3-4495-9957-655137891901", "e669b6d2-5660-4070-9d32-fb88c4a49657")

    
def start_vm(vmId):
    """
    仮想マシンを起動する。(非同期処理)
    
    引数: vmId 仮想マシンID
    
    返り値: commandId タスクID
    
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm"], [vmId], "v2r")
    
    #RHEVのAPIを実行
    _cmd = Command("Start-Vm")
    _cmd.Parameters.Add("VmId", vmId)
#    _cmd.Parameters.Add("Async", True)

    _objects = _Invoke_command(_cmd, None)

    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Start-Vm : Cannot run VM. VM is running"):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _objects = ""

    #ドライバに送るデータ　辞書型
    output = []
        #データを抜き出して辞書に格納
#        output.update(dict({'commandId':_get_last_commandid(),
    output.append(dict({'commandId':''
                           }))
  
    return output
    
#print start_vm("a4dd2757-05d3-4495-9957-655137891901")    

def stop_vm(vmId):
    """
    仮想マシンを停止する。(非同期処理)
    
    引数: vmId 仮想マシンID
    
    返り値: commandId コマンドID
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm"], [vmId], "v2r")
    
    #RHEVのAPI実行
    _cmd = Command("Stop-Vm")
    _cmd.Parameters.Add("VmId", vmId)
    _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)

    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Stop-Vm : Cannot stop VM. VM is not running."):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _objects = ""
    
    #ドライバに送るデータ　辞書型
    output = []
    #データを抜き出して辞書に格納
#    output.append(dict({'commandId':_get_last_commandid(),
    output.append(dict({'commandId':''
                           }))
        
    return output

#print stop_vm("a4dd2757-05d3-4495-9957-655137891901")    
    
    
def shutdown_vm(vmId):
    """
    仮想マシンをシャットダウンする。(非同期処理)
    
    引数: vmId 仮想マシンID
    
    返り値: commandId コマンドID 
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id("vm", vmId, "v2r")
    
    #RHEVのAPI実行
    _cmd = Command("Shutdown-Vm")
    _cmd.Parameters.Add("VmId", vmId)
#        _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)
    
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Shutdown-Vm : Cannot shutdown VM. VM is not running."):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _objects = ""
    
    #ドライバに送るデータ　辞書型
    output = []
    #データを抜き出して辞書に格納
#    output.update(dict({'commandId':_get_last_commandid(),
    output.append(dict({'commandId':''}))
                        
  
    return output

#print shutdown_vm("a4dd2757-05d3-4495-9957-655137891901")    


def create_template(vmId, name, description, storageId, clusterId):
    """
    仮想マシンからテンプレートを作成する。(非同期処理)
    
    引数: vmId 仮想マシンID
          name 名前
          description 概要
          storageId ストレージID
          clusterId クラスタID  
    
    返り値: commandId コマンドID
            template テンプレート
            
    処理概要：
    1) vmIdをもとにVmオブジェクトを取得する
    2) Add-TemplateのパラメータにVmオブジェクト等を指定して実行する
    """
    #暫定対処
    #name = "NewVm3_tmp"
    #description = "dummy_chicket192"
      
    #受け取ったデータのバリデーションを行う。
    validate_id(["vm", "storageId", "clusterId"], [vmId, storageId, clusterId], "v2r")
    validate_data(["name", "description"], [name, description], "v2r")
    
    #Vmオブジェクト取得
    _cmd = Command("Get-Vm")
    _cmd.Parameters.Add("VmId", vmId) 
    _vmObj_ps = _Invoke_command(_cmd, None)
    _vmObj = _vmObj_ps[0].BaseObject

    #RHEVのAPI実行
    _cmd = Command("Add-Template")
    _cmd.Parameters.Add("MasterVmObject", _vmObj)
    _cmd.Parameters.Add("Name", name)
    _cmd.Parameters.Add("Description", description)
    _cmd.Parameters.Add("ClusterId", clusterId)
    _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)
        
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("A template with the same name already exists."):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _cmd = Command("Select-Template")
        _cmd.Parameters.Add("SearchText", "Name=%s" % (name))
        
        _objects = _Invoke_command(_cmd, None)

    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    _networks = _Invoke_command(Command("Get-Networks"), None)
    
    #networkAdapterList作成時に使用するnetworkの辞書(NameとNetworIdのセット)を作成する。
    networkDict = {}
    for obj in _networks:
        networkDict.update(dict({obj.Members["name"].Value:obj.Members["NetworkId"].Value.ToString(),
                                 }))
    #ドライバに送るデータ　辞書型
    output = []
    output.append(dict({'commandId':_get_last_commandid()}))
    
    for cnt in _objects:
        #正規表現でデータを抜き出して辞書に格納
        _template = dict({
                        'templateId':_convertNoneToNull(cnt.Members["TemplateId"].Value),
                        'name':_convertNoneToNull(cnt.Members["Name"].Value),
                        'description':_convertNoneToNull(cnt.Members["Description"].Value),
                        'cpuCore':_convertNoneToNull(cnt.Members["NumOfSockets"].Value * cnt.Members["NumOfCpusPerSocket"].Value),
                        'memory':_convertNoneToNull(cnt.Members["MemSizeMb"].Value),
                        'status':_convertNoneToNull(cnt.Members["Status"].Value.ToString()),
                        'os':_convertNoneToNull(cnt.Members["OperatingSystem"].Value),
                        'clusterId':_convertNoneToNull(cnt.Members["HostClusterId"].Value),
                        })
        
        #networkAdapterTemplateList作成
        networkAdapterTemplateList = []
        for na in cnt.Members["GetNetworkAdapters"].Invoke().ToArray():

            #networkAdapterList作成
            #network名を取得してnetworkDictの辞書からvalueを引っ張る
            networkName = na.Network
            networkId = networkDict[networkName]
            
            networkAdapterTemplate = dict({
                                   'networkAdapterId':_convertNoneToNull(na.Id),
                                   'name':_convertNoneToNull(na.Name),
                                   'macAddress':_convertNoneToNull(na.MacAddress),
                                   'networkId':_convertNoneToNull(networkId),
                                   })
            networkAdapterTemplateList.append(networkAdapterTemplate)
        _template.update(dict({"networkAdapterTemplateList":networkAdapterTemplateList}))
        
        #diskTemplateList作成
        diskTemplateList = []
        for di in cnt.Members["GetDiskImages"].Invoke().ToArray():
            diskTemplate = dict({
                         'diskId':_convertNoneToNull(di.SnapshotId),
                         'size':_convertNoneToNull(int(di.SizeInGB)),
                         })
            diskTemplateList.append(diskTemplate)
            
        #_templateを更新
        _template.update(dict({"diskTemplateList":diskTemplateList}))
        
    #辞書に追加
    output.append(dict({"template":_template}))
        
    #ドライバに送るデータのバリデーション
    validate_datamodel("Template", output, "r2v")
    
    return output

#create_template(vmId, name, description, storageId, clusterId):
#print create_template("a4dd2757-05d3-4495-9957-655137891901", "testTemplate77", "testDesu", "3260fafe-85b8-4765-9b0a-74c80b522d10", 0)

    
def delete_template(templateId):
    """
    テンプレートを削除する。(非同期処理)
    
    引数: templateId テンプレートID
    
    返り値: commandId コマンドID  
    """
        
    #受け取ったデータのバリデーションを行う。
    validate_id("template", templateId, "v2r")
    
    #RHEVのAPI実行
    _cmd = Command("Remove-Template")
    _cmd.Parameters.Add("TemplateId", templateId)
    _cmd.Parameters.Add("Async", True)
    
    _objects = _Invoke_command(_cmd, None)
        
    #冗送対応
    #例外メッセージの先頭数文字と最終数文字をインプットに例外を分類
    if str(_objects).startswith("Cannot find Template with id %s" % (templateId)):
        #トランザクションログ出力
        TranLog(("%s WARN %s %s Verbose REQUEST") % ("0000000000000/0", "RHEV API", _cmd))
        #トランザクションデータログ出力
        TranDataLog(("%s WARN %s %s Verbose REQUEST\n%s") % ("0000000000000/0", "RHEV API", _cmd, ""))
        
        _objects = ""
        
    #ドライバに送るデータ　辞書型
    output = []
    #データを抜き出して辞書に格納
    output.append(dict({'commandId':_get_last_commandid(),
                           }))
        
    #ドライバに送るデータのバリデーション
    validate_id(["command"], [output[0]["commandId"]], "r2v")
    
    return output
    
#print delete_template("a495dd3e-a742-4ee6-99c9-69dd8b78658c")

def change_users_password(accountList, password):
    """
    受け取った複数ユーザのパスワードを変更する。
    
    引数: accountList アカウントのリスト
          password パスワード

    返り値: commandId コマンドID
    """
    
    #受け取ったデータのバリデーションを行う。
#    validate_
    
    #ActiveDirectoryのモジュールをインポート
    _cmd = Command("Import-Module")
    _cmd.Parameters.Add("Name", "ActiveDirectory")
    
    _Invoke_command(_cmd, None)
    
    #パスワードをセキュア化
#    _cmd = Command("ConvertTo-SecureString")
#    _cmd.Parameters.Add("String", password)
#    _cmd.Parameters.Add("AsPlainText", password)
#    _cmd.Parameters.Add("Force", "true")

    _cmd = Command("New-Object")
    _cmd.Parameters.Add("TypeName", "Security.SecureString")
    
    secureStringObject_ps = _Invoke_command(_cmd, None)
    secureString = secureStringObject_ps[0].BaseObject
    
    #パスワードを一文字づつ追加
    for i in password:
        secureString.AppendChar(i)
  
    for account in accountList:
        #パスワード変更を実行
        _cmd = Command("Set-ADAccountPassword")
        _cmd.Parameters.Add("Identity", account)
        _cmd.Parameters.Add("NewPassword", secureString)
    
        _Invoke_command(_cmd, None)

    #ドライバに送るデータ　辞書型
    output = []
    #データを抜き出して辞書に格納
    output.append(dict({'commandId':_get_last_commandid(),
                           }))
    
    return output

#_list = []
#_list.append("3110")
#print change_users_password(_list, "12345678")

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
