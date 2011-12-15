# -*- coding: utf-8 -*-
'''
Copyright 2011 NTT Software Corporation.
All Rights Reserved.

$Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
$Revision: 949 $
$Author: NTT Software Corporation. $
'''
import os
import types
import traceback

import RhevAgentException

from VhutLogger import TranLog
from VhutLogger import TranDataLog
from VhutLogger import SysLog


from ReadConfigFile import *


def validate_id(idTypes, Ids, vector):
    #####################################################################
    """
    各種IDのバリデートを行う。
    バリデート対象のIDが単数の場合でもリスト型でデータを受け取る。
    
    引数: idType ID種別のリスト(Idは不要)
          Id 各種IDのリスト
          vector バリデートを行うデータの種別
                  (Driverからのデータ: v2r
                   RHEVからのデータ: r2v)
    
    返り値:
    
    例外: RhevAgentException
    """
    for i, Id in enumerate(Ids):
        if Id == '' and vector == "v2r":
            TranLog("%s ERR INPUT %sId Validate Error" % ("0000000000000/0", idTypes[i]))
            SysLog("ERR", "INPUT", "0000000000000/0", "%sId Validate Error" % (idTypes[i]), "")
            raise RhevAgentException(2, "%sId Validate Error" % idTypes[i]);

        if Id == '' and vector == "r2v":
            TranLog("%s ERR OUTPUT %sId Validate Error" % ("0000000000000/0", idTypes[i]))
            SysLog("ERR", "OUTPUT", "0000000000000/0", "%sId Validate Error" % (idTypes[i]), "")
            
            raise RhevAgentException(4, "%sId Validate Error" % idTypes[i])
        
    
def validate_data(names, data, vector):
    """
    String型またはInt型データのバリデートを行う。
        バリデート対象データは配列にて受け取る。
    
        引数: names 各データ名称の配列
          data 各種データの配列
          vector バリデートを行うデータの種別
                  (Driverからのデータ: v2r
                   RHEVからのデータ: r2v)
                   
        返り値:
    
        例外: RhevAgentException
    """
    
    for i, one in enumerate(data):
                
        if one == '' and vector == "v2r":
            
            #ログ出力
            TranDataLog(("%s ERROR Data Validate Error from Driver, %s\n%s") % ("0000000000000/0", names[i], data))
            SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error from Driver", "%s %s" % (names[i], data))

            #例外をスロー
            raise RhevAgentException(2, "%s" % (names[i]))
            

        if one == '' and vector == "r2v":

            #ログ出力
            TranDataLog(("%s ERROR Data Validate Error from Driver, %s\n%s") % ("0000000000000/0", names[i], data))
            SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error from Driver", "%s %s" % (names[i], data))
        
            #例外をスロー
            raise RhevAgentException(2, "%s" % (names))
        
    
def validate_datamodel(pattern, inputData, vector):
    """
        各種データおよびそのリストのバリデートを行う。
        バリデートするデータ項目については引数のpatternをもとに外部ファイルから取得する。
        
        引数: pattern データモデル種別
    　　　        inputData 各種データモデルの実態データのリスト
        　　　    vector r2v(Rhev to vHut) or v2r(vHut to Rhev)
        返り値: 
        
        例外: RhevAgentException
    """
        
    try: 
        #データパターンを取得しリストに格納
        allConfData = get_pattern_data(pattern)
            
        #commandId以外のデータ群を変数dataに格納
        #valuesメソッドの返り値はリストで返却されるので頭のみを取り出す
        inputData_value = inputData[1].values()[0]
        
        #ログ出力用にkeyを取得する
        #keysメソッドの返り値はリストで返却されるので頭のみを取り出す
        inputData_key = inputData[1].keys()[0]
        
        if len(inputData_value) == 0 and vector == "v2r":
            TranDataLog(("%s ERROR Data Validate Error from Driver, %s is null\n%s") % ("0000000000000/0", inputData_key, inputData))
            SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error from Driver", "%s is null" % (inputData_key))
            raise RhevAgentException(2, "%s is null" % (inputData_key))
        
        elif len(inputData_value) == 0 and vector == "r2v":
            TranDataLog(("%s ERROR Data Validate Error from RHEV, %s is null\n%s") % ("0000000000000/0", inputData_key, inputData))
            SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error from RHEV", "%s is null" % (inputData_key))
            raise RhevAgentException(4, "%s is null" % (inputData_key))
        
        if inputData_key.endswith("List"):
            #上記inputDataのリストをひとつずつ取り出して変数dataに格納
            for data in inputData_value:
                
                #confファイルから取得したデータパターンを元にdataのnullチェックを行う
                for confData in allConfData:
                    #data(辞書)から指定したデータパターンで値を取得しnullであればエラーログと例外を発生させる
                    if data[confData] == '' and vector == "v2r":
                        
                        #ログ出力
                        TranDataLog(("%s ERROR Data Validate Error from Driver, %s is null in %s\n%s") % ("0000000000000/0", confData, pattern, inputData))
                        SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error from Driver", "%s is null in %s" % (confData, confData))
        
                        #例外をスロー
                        raise RhevAgentException(2, "%s is null in %s" % (confData, pattern))
                    
                    #RHEVから受け取ったデータのバリデート
                    elif data[confData] == '' and vector == "r2v":
                    
                        #ログ出力
                        TranDataLog(("%s ERROR Data Validate Error From RHEV, %s is null in %s\n%s") % ("0000000000000/0", confData, pattern, inputData))
                        SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error From RHEV,", "%s is null in %s" % (confData, pattern))
                       
                        #例外をスロー
                        raise RhevAgentException(4, "%s is null in %s" % (confData, pattern))
                    
        else:
            data = inputData_value
            #confファイルから取得したデータパターンを元にdataのnullチェックを行う
            for confData in allConfData:
                #data(辞書)から指定したデータパターンで値を取得しnullであればエラーログと例外を発生させる
                if data[confData] == '' and vector == "v2r":
                    
                    #ログ出力
                    TranDataLog(("%s ERROR Data Validate Error from Driver, %s's %s\n%s") % ("0000000000000/0", inputData_key, confData, inputData))
                    SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error from Driver", "%s's %s" % (inputData_key, confData))
    
                    #例外をスロー
                    raise RhevAgentException(2, "%s is null in %s" % (confData, pattern))
                
                #RHEVから受け取ったデータのバリデート
                elif data[confData] == '' and vector == "r2v":
                
                    #ログ出力
                    TranDataLog(("%s ERROR Data Validate Error From RHEV, %s is null in %s\n%s") % ("0000000000000/0", confData, pattern, inputData))
                    SysLog("ERR" , "END", "0000000000000/0", "Data Validate Error From RHEV,", "%s is null in %s" % (confData, pattern))
                   
                    #例外をスロー
                    raise RhevAgentException(4, "%s is null in %s" % (confData, pattern))
                    
    except:
        print traceback.format_exc()
        raise

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
