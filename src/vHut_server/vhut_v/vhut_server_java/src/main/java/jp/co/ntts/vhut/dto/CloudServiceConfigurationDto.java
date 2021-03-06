/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.dto;

//import java.util.ArrayList;
import java.util.List;

/**
 * CloudServiceConfigurationDtoクラス.
 * 
 * @author NTT Software Corporation.
 * @version 1.0.0
 * 
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 */
public class CloudServiceConfigurationDto {
    /** devResourceRateプロパティ */
    public int devResourceRate;

    /** cpuResourceWarnRateプロパティ */
    public int cpuResourceWarnRate;

    /** cpuResourceLimitRateプロパティ */
    public int cpuResourceLimitRate;

    /** memoryResourceWarnRateプロパティ */
    public int memoryResourceWarnRate;

    /** memoryResourceLimitRateプロパティ */
    public int memoryResourceLimitRate;

    /** storageResourceWarnRateプロパティ */
    public int storageResourceWarnRate;

    /** storageResourceLimitRateプロパティ */
    public int storageResourceLimitRate;

    /** vlanResourceWarnRateプロパティ */
    public int vlanResourceWarnRate;

    /** vlanResourceLimitRateプロパティ */
    public int vlanResourceLimitRate;

    /** specListプロパティ */
    //public ArrayList<Integer> specList;
    //jp.co.ntts.vhut.config.CloudConfigの仕様に合わせて修正
    public List<SpecDto> specList;
    /** diskListプロパティ */
    //public ArrayList<Integer> diskList;
    //jp.co.ntts.vhut.config.CloudConfigの仕様に合わせて修正
    public List<Integer> diskList;
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
