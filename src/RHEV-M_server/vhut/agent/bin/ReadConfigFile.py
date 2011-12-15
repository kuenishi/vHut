# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

import os
import traceback
from ConfigParser import ConfigParser
from ConfigParser import NoOptionError
from RhevAgentException import RhevAgentException
from VhutLogger import TranLog
from VhutLogger import TranDataLog
from VhutLogger import SysLog

#キー
KEY_ENV_VHUT_HOME = 'VHUT_HOME'
#KEY_ENV_RHEV_API = 'RHEV_API'

#設定ファイル
PATH_CONFIG = os.environ[KEY_ENV_VHUT_HOME]+"/conf/vhut.conf"
KEY_CONF_XMLRPC_SERVER = "XmlrpcServer"
KEY_CONF_RHEV_API = "RhevApi"
KEY_CONF_RHEV_SERVER = "RhevServer"
KEY_CONF_CLI_TASK_INSTANCE = "createCLITaskInstance"
KEY_CONF_ALIVE_MONITORING_RHEV = "aliveMonitoringRHEV"

#設定ファイル展開
config = ConfigParser()
config.read(PATH_CONFIG)

def get_xmlrpc_server_address():
    """
    外部設定ファイルからXMLRPCサーバのアドレスを取得する。
    
    引数: 
    
    返り値: address
    """
    try:
        address = config.get(KEY_CONF_XMLRPC_SERVER, 'address')
    
    except Exception, e:
        RhevAgentException(6, "get_xmlrpc_server_address Method")
        
    return address

def get_xmlrpc_server_port():
    """
    外部設定ファイルからXMLRPCサーバのポート番号を取得する。
    
    引数: 
    
    返り値: port
    """
    try:
        port = config.get(KEY_CONF_XMLRPC_SERVER, 'port')
    
    except Exception, e:
        RhevAgentException(6, "get_xmlrpc_server_port Method")
        
    return port


def get_rhev_server():
    """
    外部設定ファイルからRHEVサーバのアドレスとポート番号を取得する。
    
    引数:
    
    返り値: server
    """
    try:
        address = config.get(KEY_CONF_RHEV_SERVER, 'address')
        port = config.get(KEY_CONF_RHEV_SERVER, 'port')
        
        server = "%(address)s:%(port)s" % locals()
        
    except Exception, e:
        raise RhevAgentException(6, "get_rhev_server Method")
    
    return server

def get_pattern_data(pattern):
    """
    引数で受け取ったパターンをもとに外部設定ファイルからバリデートを行うデータ項目をリストで返す。
    
    引数: pattern
    
    返り値: datalist
    
    """

    try:
        num = int(config.get(pattern, 'num'))
        datalist = []

        #numの数だけループ
        for count in range(num):
            count_str = str(count)
            try:
                data = config.get(pattern, count_str)
            except NoOptionError, e:
                print
            if (data != None):
                datalist.append(data)

                
    except Exception, e:
        TranDataLog(("%s ERROR %s %s\n%s") % ("0000000000000/0", "get_pattern_data Method, Read Config Error", str(e), traceback.format_exc()))
        SysLog("ERR" , "END", "0000000000000/0", "get_pattern_data Method, Read Config Error", str(e))
        raise RhevAgentException(6, "get_pattern_data Method")
        
    return datalist

def get_cli_task_instance():
    """
    RhevmCmc.CLITask型のインスタンス作成用のps1ファイルのURIを返す。
    
    引数:
    
    返り値: ps1Uri
    """
    
    try:
        ps1Uri = config.get(KEY_CONF_CLI_TASK_INSTANCE, 'uri')
        
    except Exception, e:
        raise RhevAgentException(6, "get_cli_task_instance Method")
        
    return ps1Uri

    
def get_alive_monitoring_rhev_ps1():
    """
    RHEV死活監視用のps1ファイルのURIを返す。
    
        引数:
        
        返り値: ps1Uri
    """
    
    try:
        ps1Uri = config.get(KEY_CONF_ALIVE_MONITORING_RHEV, 'uri')
        
    except Exception, e:
        raise RhevAgentException(6, "get_alive_monitoring_rhev_ps1 Method")
            
    return ps1Uri


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
