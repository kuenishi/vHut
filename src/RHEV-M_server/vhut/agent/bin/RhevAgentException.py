# -*- coding: utf-8 -*-
'''

Copyright 2011 NTT Software Corporation.
All Rights Reserved.

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''

import xmlrpclib

#class AgentException(Exception):
#faultCodeの1はxmlrpclibのデフォルトエラー
FAULT_STRING_DICT = {1:"",
                     2:"Data Validate Error from Driver",
                     3:"Connection Error To RHEV",
                     4:"Data Validate Error from RHEV",
                     5:"Connection Error To Driver",
                     6:"ConfigFile Read Error",
                     }

    
def RhevAgentException(faultCode, faultString):
    """
        例外をスローする。
    
        引数: faultCode
        : faultString
        返り値:
    """
    fault = xmlrpclib.Fault(faultCode, "%s, %s" % (FAULT_STRING_DICT[faultCode], faultString))
    raise fault


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
