/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.logic;

import java.util.Map;

/**
 * <p>//
 * <br>
 * <p>//
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 */
interface IClusterResource {
    /* (non-Javadoc)
     * 
     */
    public Map<Long, Integer> getCpuMax();

    /* (non-Javadoc)
     * 
     */
    public Map<Long, Integer> getCpuUsed();

    /* (non-Javadoc)
     * 
     */
    public Map<Long, Integer> getMemoryMax();

    /* (non-Javadoc)
     * 
     */
    public Map<Long, Integer> getMemoryUsed();
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
