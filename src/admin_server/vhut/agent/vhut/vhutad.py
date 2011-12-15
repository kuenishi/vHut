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
import sys
import signal
import time
import vhuta
import logging.config
import atexit
import httplib

#キー
KEY_ENV_VHUT_HOME = 'VHUT_HOME'
#Proccess IDファイル
PATH_PID_FILE= os.environ[KEY_ENV_VHUT_HOME]+"/agent/log/vhutad.pid"
#秘密鍵ファイル
PATH_PRIV_KEY = os.environ[KEY_ENV_VHUT_HOME]+"/agent/cert/agentkey.pem"
#秘密鍵証明書ファイル
PATH_PRIV_CERT = os.environ[KEY_ENV_VHUT_HOME]+"/agent/cert/agentcert.pem"

#ロガー設定
logger = None

#エージェントプロセスのPID
vhuta_pid = 0

def main():
    """
    vhutad main
    """
    logger = logging.getLogger('vhuta')
    vhuta_pid = None
    # デーモン化する
    daemonize()

    #プロセスIDを書いておく
    write_pid()

    # 親プロセスのループ
    # 子プロセスを監視して、子が死んだらすぐに新しい子プロセスを作成する
    while True:
        while vhuta_pid != None:
            os.waitpid(vhuta_pid, 0)
            vhuta_pid = None

        pid = os.fork()

        if pid == 0:
            signal.signal(signal.SIGTERM, kill_child)
            break
        else:
            vhuta_pid = pid
            logger.info(u"server process id is %d." % vhuta_pid)
            #シグナルハンドラの登録
            signal.signal(signal.SIGTERM, kill_all)
            time.sleep(0.1)

    vhuta.main()

def kill_all(sig, status):
    os.kill( vhuta_pid, signal.SIGTERM )
    exit(0)

def kill_child(sig, status):
    vhuta.kill()
    exit(0)

def write_pid():
    f = open(PATH_PID_FILE, mode='w');
    f.write("%d" % os.getpid())
    f.close()

def daemonize():
    os.chdir("/")

    try:
        pid = os.fork()
        if pid > 0:
            sys.exit(0)

    except OSError, e:
        raise Exception, "%s [%d]" % (e.strerror, e.errno)

    os.setsid()
    os.umask(0)
    sys.stdin = open('/dev/null', 'r')
    sys.stdout = open('/dev/null', 'w')
    sys.stderr = open('/dev/null', 'w')

def stop_server (port):
    """send QUIT request to http server running on localhost:<port>"""
    conn = httplib.HTTPConnection("localhost:%d" % port)
    conn.request("QUIT", "/")
    conn.getresponse()

def stop_secure_server ():
    """send QUIT request to http server running on localhost:<port>"""
    conn = httplib.HTTPSConnection("localhost", 443, PATH_PRIV_KEY, PATH_PRIV_CERT)
    conn.request("QUIT", "/")
    conn.getresponse()

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
