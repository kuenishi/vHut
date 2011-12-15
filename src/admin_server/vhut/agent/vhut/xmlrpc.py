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

import SocketServer
import BaseHTTPServer
import SimpleXMLRPCServer
from OpenSSL import SSL
import socket
import logging
import xmlrpclib
from xmlrpclib import Fault
import sys
from traceback import format_exc

class SecureXMLRpcRequestHandler(SimpleXMLRPCServer.SimpleXMLRPCRequestHandler):
    """Secure XML-RPC request handler class.

    It it very similar to SimpleXMLRPCRequestHandler but it uses HTTPS for transporting XML data.
    """
    def setup(self):
        self.connection = self.request
        self.rfile = socket._fileobject(self.request, "rb", self.rbufsize)
        self.wfile = socket._fileobject(self.request, "wb", self.wbufsize)
        #トランザクションログ・トランザクションデータログ用のロガー
        self._logger = logging.getLogger('vhut.tran')
        self._logger_data = logging.getLogger('vhut.trandata')

    def do_POST(self):
        """Handles the HTTPS POST request.

        It was copied out from SimpleXMLRPCServer.py and modified to shutdown the socket cleanly.
        """

        try:
            # get arguments
            data = self.rfile.read(int(self.headers["content-length"]))
            # In previous versions of SimpleXMLRPCServer, _dispatch
            # could be overridden in this class, instead of in
            # SimpleXMLRPCDispatcher. To maintain backwards compatibility,
            # check to see if a subclass implements _dispatch and dispatch
            # using that method if present.

            response = self.server._marshaled_dispatch(
                    data, getattr(self, '_dispatch', None)
                )
#            params, method = xmlrpclib.loads(data)
#            module_name, method_name = self._split_method(method)
#            transaction_id, shaped_params = self._split_params(params)
#            self._logger.info(("%s %s INFO %s START") % (module_name, transaction_id, method_name))
#            self._logger_data.info(("%s %s INFO %s START %s") % (module_name, transaction_id, method_name, data))
#            response = self.server._marshaled_dispatch(method, shaped_params)
#            self._logger.info(("%s %s INFO %s END") % (module_name, transaction_id, method_name))
#            self._logger_data.info(("%s %s INFO %s END %s") % (module_name, transaction_id, method_name, response))
        except: # This should only happen if the module is buggy
            # internal error, report as HTTP server error
            self.send_response(500)
            self.end_headers()
        else:
            # got a valid XML RPC response
            self.send_response(200)
            self.send_header("Content-type", "text/xml")
            self.send_header("Content-length", str(len(response)))
            self.end_headers()
            self.wfile.write(response)

            # shut down the connection
            self.wfile.flush()
            self.connection.shutdown() # Modified here!

    def do_QUIT (self):
        """send 200 OK response, and set server.stop to True"""
        self.server.quit()
        self.send_response(200)
        self.end_headers()
        self.server.stop = True


class SecureXMLRPCServer(BaseHTTPServer.HTTPServer, SimpleXMLRPCServer.SimpleXMLRPCDispatcher):
    '''
    classdocs
    '''

    def __init__(self, server_address, privkey, trust_store, HandlerClass=SecureXMLRpcRequestHandler, logRequests=True):
        '''
        Constructor
        '''
        self._logger_tran = logging.getLogger('vhuta.tran')
        self._logger_trandata = logging.getLogger('vhuta.trandata')

        self.logRequests = logRequests

        SimpleXMLRPCServer.SimpleXMLRPCDispatcher.__init__(self)
        SocketServer.BaseServer.__init__(self, server_address, HandlerClass)
        ctx = SSL.Context(SSL.SSLv23_METHOD)

        ctx.set_verify(SSL.VERIFY_PEER
                       | SSL.VERIFY_FAIL_IF_NO_PEER_CERT
                       | SSL.VERIFY_CLIENT_ONCE, self.verify_callback)

        ctx.use_privatekey_file (privkey[0])
        ctx.use_certificate_file(privkey[1])
        ctx.load_verify_locations (trust_store)

        ctx.set_session_id('VHUT')

        self.socket = SSL.Connection(ctx, socket.socket(self.address_family,
                                                        self.socket_type))
        self.server_bind()
        self.server_activate()

    def verify_callback(self, conn, cert, errnum, depth, ok):
        """
        Verify callback. If we get here then the certificate is ok.
        """
        result = ok
        return result

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

        self._logger_tran.info(("%s %s INFO %s START") % (module_name, transaction_id, method_name))
        self._logger_trandata.info(("%s %s INFO %s START %s") % (module_name, transaction_id, method_name, data))

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
            handler_fault = xmlrpclib.Fault(1, "%s:%s" % (sys.exc_type, sys.exc_value))

        if handler_fault:
            response = xmlrpclib.dumps(handler_fault)
            self._logger_tran.error(("%s %s ERR %s %s") % (module_name, transaction_id, method_name, handler_fault.faultString))
            self._logger_trandata.error(("%s %s ERR %s %s\n$%s") % (module_name, transaction_id, method_name, handler_fault.faultString, format_exc()))

        self._logger_tran.info(("%s %s INFO %s END") % (module_name, transaction_id, method_name))
        self._logger_trandata.info(("%s %s INFO %s END %s") % (module_name, transaction_id, method_name, response))

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

    def serve_forever (self):
        """Handle one request at a time until stopped."""
        self.stop = False
        while not self.stop:
            self.handle_request()

    def quit (self):
        self._quit_handler()

    def register_quit_handler(self, handler):
        self._quit_handler = handler

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
