# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

from RhevHandler import *
from ReadConfigFile import *
import clr

import datetime

import logging

import xmlrpclib

import traceback
from VhutLogger import TranLog
from VhutLogger import TranDataLog
from VhutLogger import SysLog

import BaseHTTPServer
from SimpleXMLRPCServer import SimpleXMLRPCRequestHandler

clr.AddReference('System.Management.Automation')
clr.AddReference('IronPython')

from SimpleXMLRPCServer import SimpleXMLRPCServer
from os.path import *
from re import findall
from System.Management.Automation import *
from System.Management.Automation.Host import *
from System.Management.Automation.Runspaces import *


#リモートのPowerShellの場合はTrueを設定する。その場合WinRMを使って接続。
#if (get_rhev_server_remote() == 'true'):
#    _cinfo = WSManConnectionInfo(System.Uri(get_rhev_server()))
#    _runspace = RunspaceFactory.CreateRunspace(_cinfo)
#else:
#    _runspace = RunspaceFactory.CreateRunspace()
#    
#接続。runspaceがpowershellプロセスとのセッションを保持。
#_runspace.Open()


#class TraceXMLRPCRequestHandler(SimpleXMLRPCRequestHandler):
#    """Simple XML-RPC request handler class.
#
#    Handles all HTTP POST requests and attempts to decode them as
#    XML-RPC requests.
#    """
#    # Class attribute listing the accessible path components;
#    # paths not on this list will result in a 404 error.
#    rpc_paths = ('/', '/RPC2')
#
#    def do_POST(self):
#        """Handles the HTTP POST request.
#
#        Attempts to interpret all HTTP POST requests as XML-RPC calls,
#        which are forwarded to the server's _dispatch method for handling.
#        """
#
#        # Check that the path is legal
#        if not self.is_rpc_path_valid():
#            self.report_404()
#            return
#
#        try:
#            # Get arguments by reading body of request.
#            # We read this in chunks to avoid straining
#            # socket.read(); around the 10 or 15Mb mark, some platforms
#            # begin to have problems (bug #792570).
#            max_chunk_size = 10*1024*1024
#            size_remaining = int(self.headers["content-length"])
#            L = []
#            while size_remaining:
#                chunk_size = min(size_remaining, max_chunk_size)
#                L.append(self.rfile.read(chunk_size))
#                size_remaining -= len(L[-1])
#            data = ''.join(L)
#            
#            #トランザクションログ出力
#            TranDataLog("INFO", "START", "AGNT1101", "XXXXXX", None, data)
#
#            # In previous versions of SimpleXMLRPCServer, _dispatch
#            # could be overridden in this class, instead of in
#            # SimpleXMLRPCDispatcher. To maintain backwards compatibility,
#            # check to see if a subclass implements _dispatch and dispatch
#            # using that method if present.
#            response = self.server._marshaled_dispatch(
#                    data, getattr(self, '_dispatch', None)
#                )
#
#            #トランザクションログ出力
#            TranDataLog("INFO", "END", "AGNT1102", "XXXXXX", None, response)
#            
#            
#        except Exception, e: # This should only happen if the module is buggy
#            # internal error, report as HTTP server error
#            self.send_response(500)
#
#            # Send information about the exception if requested
#            if hasattr(self.server, '_send_traceback_header') and \
#                    self.server._send_traceback_header:
#                self.send_header("X-exception", str(e))
#                self.send_header("X-traceback", traceback.format_exc())
#
#            self.end_headers()
#        else:
#            # got a valid XML RPC response
#            self.send_response(200)
#            self.send_header("Content-type", "text/xml")
#            self.send_header("Content-length", str(len(response)))
#            self.end_headers()
#            self.wfile.write(response)
#
#            # shut down the connection
#            self.wfile.flush()
#            self.connection.shutdown(1)

#class KeepTransactionId:
#    self.transaction_id = "1"

class TracableXMLRPCServer(SimpleXMLRPCServer):
    '''
    classdocs
    '''

    def __init__(self, addr, requestHandler=SimpleXMLRPCRequestHandler,
                 logRequests=True, allow_none=True, encoding=None, bind_and_activate=True):
        '''
        Constructor
        '''
        #キー
        KEY_ENV_VHUT_HOME = 'VHUT_HOME'
        #ログ設定ファイル
        LOGGING_CONF = os.environ[KEY_ENV_VHUT_HOME]+"/conf/log.conf"
        #設定ファイルをセット
        logging.config.fileConfig(LOGGING_CONF)
        
#        self._logger_tran = logging.getLogger('tran')
#        self._logger_trandata = logging.getLogger('trandata')
        
        SimpleXMLRPCServer.__init__(self, addr, requestHandler, logRequests, allow_none, encoding, bind_and_activate)
        
        
    
    def _marshaled_dispatch(self, data, dispatch_method = None):
        """Dispatches an XML-RPC method from marshalled (XML) data.

        XML-RPC methods are dispatched from the marshalled (XML) data
        using the _dispatch method and the result is returned as
        marshalled data. For backwards compatibility, a dispatch
        function can be provided as an argument (see comment in
        SimpleXMLRPCRequestHandler.do_POST) but overriding the
        existing method through subclassing is the prefered means
        of changing method dispatch behavior.
        """

        params, method = xmlrpclib.loads(data)
        
        module_name, method_name = self._split_method(method)
        transaction_id, shaped_params = self._split_params(params)
        
#        #KeepTransactionIdクラスのアトリビュートにtransaction_idをセット
#        keepTransactionId = KeepTransactionId()
#        keepTransactionId.transaction_id = transaction_id

#        #実行速度計測Start
#        start = datetime.datetime.now()
        
        #トランザクションログ出力
#        self._logger_tran.info(("%s %s INFO %s START") % (module_name, transaction_id, method_name))
        TranLog(("%s INFO %s START") % (transaction_id, method_name))
#        self._logger_tran.info(("%s INFO %s START") % (transaction_id, method_name))
        #トランザクションデータログ出力
#        self._logger_trandata.info(("%s %s INFO %s START\n%s") % (module_name, transaction_id, method_name, data))
#        self._logger_trandata.info(("%s INFO %s START\n%s") % (transaction_id, method_name, data))
        TranDataLog(("%s INFO %s START\n%s") % (transaction_id, method_name, xmlrpclib.dumps(params)))
                    
        
        handler_fault = None
        
        # generate response
        try:
            if dispatch_method is not None:
                response = dispatch_method(method, shaped_params)
            else:
                response = self._dispatch(method, shaped_params)
            # wrap response in a singleton tuple
            response = (response,)
            response = xmlrpclib.dumps(response, methodresponse=1)
        except Fault, fault:
            handler_fault = fault;
        except:
            # report exception back to server
            
            handler_fault = xmlrpclib.Fault(1, "%s : %s" % (sys.exc_type, sys.exc_value))
                                            
        if handler_fault:
            response = xmlrpclib.dumps(handler_fault)
            #トランザクションログ出力
#            self._logger_tran.error(("%s %s ERR %s %s") % (module_name, transaction_id, method_name, handler_fault.faultString))
#            self._logger_tran.error(("%s ERR %s %s") % (transaction_id, method_name, handler_fault.faultString))
            TranLog(("%s ERROR %s %s") % (transaction_id, method_name, handler_fault.faultString))
            #トランザクションデータログ出力
#            self._logger_trandata.error(("%s %s ERR %s %s\n%s") % (module_name, transaction_id, method_name, handler_fault.faultString, format_exc()))
#            self._logger_trandata.error(("%s ERR %s %s\n%s") % (transaction_id, method_name, handler_fault.faultString, format_exc()))
#            TranDataLog(("%s ERROR %s %s\n%s") % (transaction_id, method_name, handler_fault.faultString, traceback.format_exc()))
            TranDataLog(("%s ERROR %s %s\n%s\n%s") % (transaction_id, method_name, handler_fault.faultString, traceback.format_exc(), response))

            #Windowsシステムログ出力
            SysLog("ERR" , "END", "", transaction_id, method_name)
#            SysLog("ERR", "END", "AGNT1003", "XXXXXX", None)
        
        else:    
        #トランザクションログ出力
#        self._logger_tran.info(("%s %s INFO %s END") % (module_name, transaction_id, method_name))
#        self._logger_tran.info(("%s INFO %s END") % (transaction_id, method_name))
            TranLog(("%s INFO %s END") % (transaction_id, method_name))
        #トランザクションデータログ出力
#        self._logger_trandata.info(("%s %s INFO %s END %s") % (module_name, transaction_id, method_name, response))
#        self._logger_trandata.info(("%s INFO %s END\n%s") % (transaction_id, method_name, response))
            TranDataLog(("%s INFO %s END\n%s") % (transaction_id, method_name, response))


#        #実行速度計測End
#        end = time.hour + time.minute + time.second
#        end = datetime.datetime.now()
#        

#        #実行速度計測result
##        result = end - start
#        print "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
#        print "start: " + start
#        print "end: " + end 
#        print "result: " + result
#        print end -start
#        print "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

        return response
    
            
    def _split_method(self, method):
        '''
        XMLRPCのmethod名をモジュール名と内部関数名に分割します.
        
        method名は「モジュール名.内部関数名」という形式であると想定していますが、
        分割できない時はすべて内部関数として理解し、モジュール名はundefinedとします。
        '''
        module_name = "undefined"
        method_name = method
        
        elements = method.split(".")
        if len(elements) > 1:
            module_name = elements[0]
            method_name = ".".join(elements[1:])
        
        return module_name, method_name
    
    def _split_params(self, params):
        '''
        XMLRPCのパラメータをトランザクションIDと内部関数の引数に分割します.
        
        params配列の第一番目にString型のトランザクションIDが存在するという想定で処理をします。
        paramsのサイズが2以上（N）の時は、第一要素をトランザクションID、内部関数の引数はサイズN-1の配列として処理します。
        paramsのサイズが1の時は、トランザクションIDのみ存在し、内部関数の引数はサイズ0の配列として処理します。
        paramsのサイズが0の時は、トランザクションIDをundefined、内部関数の引数はサイズ0の配列として処理します。
        '''
        transaction_id = "undefined"
        shaped_params = params
        
        if len(params) > 0:
            transaction_id = params[0]
            shaped_params = []
        if len(params) > 1:
            shaped_params = params[1:]
        
        return transaction_id, shaped_params


if __name__ == '__main__':
    
#    # RHEV API読み込み
#    _cmd = Command(get_rhev_api_uri())
#    _pipe = _runspace.CreatePipeline()
#    _pipe.Commands.Add(_cmd)
#    _pipe.Invoke()
    
    #SimpleXMLRPCServerに入れる引数は自身のIPアドレスと待ち受けポート番号
    _address = get_xmlrpc_server_address()
    _port = int(get_xmlrpc_server_port())
    server = TracableXMLRPCServer((_address, _port), allow_none=True)

    server.register_introspection_functions()
    
    ################### 関数登録が必要 22個
    #関数を登録
    server.register_function(get_all_clusters, "get_all_clusters")
    server.register_function(get_hosts_by_cluster_id, "get_hosts_by_cluster_id")
    server.register_function(get_networks_by_cluster_id, "get_networks_by_cluster_id")
    server.register_function(get_all_data_storages, "get_all_data_storages")
    server.register_function(get_vms_by_cluster_id, "get_vms_by_cluster_id")
    server.register_function(get_templates_by_cluster_id, "get_templates_by_cluster_id")
    server.register_function(get_all_users, "get_all_users")
    server.register_function(get_tasks_by_task_ids, "get_tasks_by_task_ids")
    server.register_function(create_vm, "create_vm")
    server.register_function(delete_vm, "delete_vm")
    server.register_function(change_spec, "change_spec")
    server.register_function(add_network_adapter, "add_network_adapter")
    server.register_function(remove_network_adapter, "remove_network_adapter")
    server.register_function(add_disk, "add_disk")
    server.register_function(remove_disk, "remove_disk")
    server.register_function(add_user, "add_user")
    server.register_function(remove_user, "remove_user")
    server.register_function(start_vm, "start_vm")
    server.register_function(stop_vm, "stop_vm")
    server.register_function(shutdown_vm, "shutdown_vm")
    server.register_function(create_template, "create_template")
    server.register_function(delete_template, "delete_template")
    server.register_function(change_users_password, "change_users_password")                             
    
    print "server is successfully up."
    
    #サーバ待ち受け
    server.serve_forever()
    


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
