/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * <p>Network情報をCSV書き出しするための構造体.
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
@CSVEntity(header = true, headerCheck = false, columnCountCheck = false)
public class NetworkCsv {

    @CSVColumn(columnIndex = 0, columnName = "name")
    public String name;

    @CSVColumn(columnIndex = 1, columnName = "vlan")
    public int vlan;

    @CSVColumn(columnIndex = 2, columnName = "address")
    public String address;

    @CSVColumn(columnIndex = 3, columnName = "mask")
    public String mask;

    @CSVColumn(columnIndex = 4, columnName = "broadcast")
    public String broadcast;

    @CSVColumn(columnIndex = 5, columnName = "gateway")
    public String gateway;

    @CSVColumn(columnIndex = 6, columnName = "dhcp")
    public String dhcp;

    @CSVColumn(columnIndex = 7, columnName = "dns")
    public String dns;

    @CSVColumn(columnIndex = 8, columnName = "clusterName")
    public String clusterName;

}


/**
 * =====================================================================
 * 
 *    Copyright 2011 NTT Sofware Corporation
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * =====================================================================
 */
