# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

import clr, socket
from System.Diagnostics import EventLog, EventLogEntryType

import os
import time
import logging
import logging.config
from logging.handlers import NTEventLogHandler

import logging
import logging.config


##メッセージコードとメッセージのひも付
MASSAGE_DICT = {}
MASSAGE_DICT.update(dict({"AGNT1001":"get_all_cluster Method",
                          "AGNT1002":"get_hosts_by_cluster_id Method",
                          "AGNT1003":"get_networks_by_cluster_id Method",
                          "AGNT1004":"get_all_data_storages Method",
                          "AGNT1005":"get_vms_by_cluster_id Method",
                          "AGNT1006":"get_templates_by_cluster_id Method",
                          "AGNT1007":"get_all_users Method",
                          "AGNT1008":"get_tasks_by_task_ids Method",
                          "AGNT1009":"create_vm Method",
                          "AGNT1010":"delete_vm Method",
                          "AGNT1011":"change_spac Method",
                          "AGNT1012":"add_network_adapter Method",
                          "AGNT1013":"remove_network_adapter Method",
                          "AGNT1014":"add_disk Method",
                          "AGNT1015":"remove_disk Method",
                          "AGNT1016":"add_user Method",
                          "AGNT1017":"remove_user Method",
                          "AGNT1018":"start_vm Method",
                          "AGNT1019":"stop_vm Method",
                          "AGNT1020":"shutdown_vm Method",
                          "AGNT1021":"create_template Method",
                          "AGNT1022":"delete_template Method",
                          
                          "AGNT1101":"Driver sent XMLRPC data to Agent ",
                          "AGNT1102":"Agent sent XMLRPC data to Driver",
                          
                          "AGNT2001":"DataValidator Method, Validation Failure",
                          "AGNT2101":"_Invoke_command Method, RHEV Login Failure",
                          "AGNT2201":"_Invoke_command Method, RHEV Connection Failure",
                        }))

#def TranLog(level, type, messageId , tranId, other):
def TranLog(massage):
    """
    トランザクションログを出力する。
    
    引数: level ログ出力レベル
        　type ログのタイプ
          messageId ログに埋め込むメッセージID
          tranId トランザクションID
          other その他データ
    返り値:
    """
    
    #キー
    KEY_ENV_VHUT_HOME = 'VHUT_HOME'
    #ログ設定ファイル
    LOGGING_CONF = os.environ[KEY_ENV_VHUT_HOME]+"/conf/log.conf"
    #設定ファイルをセット
    logging.config.fileConfig(LOGGING_CONF)
    
    logger = logging.getLogger("tran")
    
    # message加工
#    if other == None:
#        message = tranId + ' ' + type + ' ' + messageId + ' ' + MASSAGE_DICT[messageId]
#    else:
#        message = tranId + ' ' + type + ' ' + messageId + ' ' + other
        
    logger.info(massage)
    
#    if level == "DEBUG":
#        logger.debug(message) # debugレベルのログ出力を行います。
#    
#    if level == "INFO":
#            logger.info(message) # infoレベルのログ出力を行います。
#        
#    if level == "WARNNING":
#        logger.warn(message) # warnレベルのログ出力を行います。
#    
#    if level == "ERR":
#        logger.error(message) # errorレベルのログ出力を行います。
#        
#    if level == "EMG":
#        logger.critical(message)    

#def TranDataLog(level, type, messageId, tranId, other, dumpData):
def TranDataLog(message):
    """
    トランザクションデータログを出力する。
    
    引数: level ログ出力レベル
        　type ログのタイプ
        　message ログに埋め込むメッセージ
          data ログに埋め込むXMLデータの中身
    返り値:
    """
    #キー
    KEY_ENV_VHUT_HOME = 'VHUT_HOME'
    
    #ログ設定ファイル
    LOGGING_CONF = os.environ[KEY_ENV_VHUT_HOME]+"/conf/log.conf"
    
    #設定ファイルをセット
    logging.config.fileConfig(LOGGING_CONF)
    
    logger = logging.getLogger("trandata")
    
#    #ログに埋め込むmessage加工
#    if other == None:
#        message = tranId + ' ' + type + ' ' + messageId + ' ' + MASSAGE_DICT[messageId] + '\n' + dumpData
#    else:
#        message = tranId + ' ' + type + ' ' + messageId + ' ' + other + '\n' + dumpData
    
    logger.info(message)
    
    
#    if level == "DEBUG":
#        logger.debug(message) # debugレベルのログ出力を行います。
#    
#    if level == "INFO":
#        logger.info(message) # infoレベルのログ出力を行います。
#        
#    if level == "WARNNING":
#        logger.warn(message) # warnレベルのログ出力を行います。
#    
#    if level == "ERR":
#        logger.error(message) # errorレベルのログ出力を行います。
#        
#    if level == "EMG":
#        logger.critical(message)
    


#Windowsシステムログ出力用
def SysLog(level, type, messageId, tranId, other):
    """
    WindowsイベントログにErrorログを出力する。
    
    引数: level ログ出力レベル
        　message ログに埋め込むメッセージ
    返り値:
    """
    #Windowsイベントログの説明欄生成
    if other == None:
        message = 'COMPONENT = vHut_Agent\n' + 'MODULE = Hypervisor_Agent\n' + 'TRANSACTIONID = ' + tranId + '\n' + 'LEVEL = '+ level + '\n' + 'TYPE = ' + type + '\n' + 'MESSAGEID = ' + messageId + '\n' + 'MESSAGE = ' + MASSAGE_DICT[messageId]
   
    else:
        message = 'COMPONENT = vHut_Agent\n' + 'MODULE = Hypervisor_Agent\n' + 'TRANSACTIONID = ' + tranId + '\n' + 'LEVEL = '+ level + '\n' + 'TYPE = ' + type + '\n' + 'MESSAGE = ' + other
        
        
    #ログ区分
    level = EventLogEntryType.Error
    
    #ログ出力種別を「アプリケーション」に設定
    logname = "Application"
    
    #ソース名生成
    source = "vHutAgent"
    
#    EventLog.DeleteEventSource(source)
    if not EventLog.SourceExists(source):
        EventLog.CreateEventSource(source, logname)
        
    EventLog(logname, '.', source).WriteEntry(message, level)



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
