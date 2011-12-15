# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

@author NTT Software Corporation.
@version 1.0.0

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

import os
import logging.config
from xmlrpc import SecureXMLRPCServer
from nw import NwHandler
from ConfigParser import ConfigParser
import signal
import atexit

#キー
KEY_ENV_VHUT_HOME = 'VHUT_HOME'
KEY_CONF_VHUT = 'vhut'
#KEY_CONF_KVM = 'kvm'
#KEY_CONF_HV = 'hv'
KEY_CONF_NW = 'nw'
#KEY_CONF_ZFS = 'zfs'
#KEY_CONF_LVM = 'lvm'
#設定ファイル
PATH_CONFIG = os.environ[KEY_ENV_VHUT_HOME]+"/agent/conf/vhuta.conf"
#ログ設定ファイル
PATH_LOG_CONFIG = os.environ[KEY_ENV_VHUT_HOME]+"/agent/conf/log.conf"
#秘密鍵ファイル
PATH_PRIV_KEY = os.environ[KEY_ENV_VHUT_HOME]+"/agent/cert/agentkey.pem"
#秘密鍵証明書ファイル
PATH_PRIV_CERT = os.environ[KEY_ENV_VHUT_HOME]+"/agent/cert/agentcert.pem"
#CA秘密鍵証明書ファイル
PATH_TRUST_STORE = os.environ[KEY_ENV_VHUT_HOME]+"/agent/cert/cacert.pem"
#NW設定DBファイル
PATH_DATA_NW = os.environ[KEY_ENV_VHUT_HOME]+"/agent/data/nw.db"

#ロガー設定
logger = None

#ネットワークエージェント
nwa = None

server = None

def main():
    """
    vhuta main
    """
    logger = logging.getLogger('vhuta')
    #設定ファイル展開
    default = {'role' : 'manager'
               ,'public_if' : 'eth0'
               ,'public_netmask' : '24'
               ,'private_if' : 'eth1'
               ,'private_network' : '192.168.0.1'
               ,'private_netmask' : '24'}
    config = ConfigParser(default)
    config.read(PATH_CONFIG)
    #終了時のKillコマンド登録
    atexit.register(kill)
    #共通部分の設定読み込み
    address = config.get(KEY_CONF_VHUT, 'address')
    port = config.getint(KEY_CONF_VHUT, 'port')
    #サーバ準備
    server_address = (address, port)
    private_key = (PATH_PRIV_KEY, PATH_PRIV_CERT)
    server = SecureXMLRPCServer(server_address, private_key, PATH_TRUST_STORE)
    #インターフェース定義
#    if config.get(KEY_CONF_KVM, 'enable'):
#        kvm = KvmHandler(config)
#        server.register_function(kvm.startVM, 'kvm.startVM')
    if config.getboolean(KEY_CONF_NW, 'enable'):
        nwa = NwHandler(PATH_CONFIG, PATH_DATA_NW)
        nwa.init()
        server.register_function(nwa.init, 'network_agent.init')
        server.register_function(nwa.add_ip, 'network_agent.add_ip')
        server.register_function(nwa.del_ip, 'network_agent.del_ip')
        server.register_function(nwa.add_network, 'network_agent.add_network')
        server.register_function(nwa.del_network, 'network_agent.del_network')
        server.register_function(nwa.add_nat, 'network_agent.add_nat')
        server.register_function(nwa.del_nat, 'network_agent.del_nat')
        server.register_function(nwa.set_filter, 'network_agent.set_filter')
        server.register_function(nwa.get_config, 'network_agent.get_config')
        server.register_quit_handler(kill)
    #サーバスタート
    sa = server.socket.getsockname()
    logger.info(u"Serving HTTPS on %s port %d" % sa)
    server.serve_forever()

def kill():
    logger.debug(u"kill:called" )
    server.quit()
    nwa = NwHandler(PATH_CONFIG, PATH_DATA_NW)
    nwa.end()
    logger.info(u"Child proccess is terminated." )

if __name__ == '__main__':
    logging.config.fileConfig(os.environ[KEY_ENV_VHUT_HOME]+"/agent/conf/log.conf")
    main()


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
