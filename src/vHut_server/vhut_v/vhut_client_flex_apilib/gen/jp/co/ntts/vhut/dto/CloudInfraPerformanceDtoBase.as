/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (CloudInfraPerformanceDto.as).
 */

package jp.co.ntts.vhut.dto {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * CloudInfraPerformanceDto Entity Base Class.
	 * 
	 * <p>
	 * <b>Author :</b> NTT Software Corporation.
	 * <b>Version :</b> 1.0.0
	 * </p>
	 *
	 * @langversion 3.0
	 * @playerversion Flash 10.1
	 * 
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
    public class CloudInfraPerformanceDtoBase extends EventDispatcher {

        protected var _activeCpuFreq:Number;
        protected var _activeMemory:Number;
        protected var _activeStorage:Number;
        protected var _activeVlan:Number;
        protected var _activeVm:Number;
        protected var _commitedCpuFreq:Number;
        protected var _commitedMemory:Number;
        protected var _commitedVm:Number;
        protected var _totalCpuFreq:Number;
        protected var _totalMemory:Number;
        protected var _totalStorage:Number;
        protected var _totalVlan:Number;

        public function set activeCpuFreq(value:Number):void {
            _activeCpuFreq = value;
        }
        public function get activeCpuFreq():Number {
            return _activeCpuFreq;
        }

        public function set activeMemory(value:Number):void {
            _activeMemory = value;
        }
        public function get activeMemory():Number {
            return _activeMemory;
        }

        public function set activeStorage(value:Number):void {
            _activeStorage = value;
        }
        public function get activeStorage():Number {
            return _activeStorage;
        }

        public function set activeVlan(value:Number):void {
            _activeVlan = value;
        }
        public function get activeVlan():Number {
            return _activeVlan;
        }

        public function set activeVm(value:Number):void {
            _activeVm = value;
        }
        public function get activeVm():Number {
            return _activeVm;
        }

        public function set commitedCpuFreq(value:Number):void {
            _commitedCpuFreq = value;
        }
        public function get commitedCpuFreq():Number {
            return _commitedCpuFreq;
        }

        public function set commitedMemory(value:Number):void {
            _commitedMemory = value;
        }
        public function get commitedMemory():Number {
            return _commitedMemory;
        }

        public function set commitedVm(value:Number):void {
            _commitedVm = value;
        }
        public function get commitedVm():Number {
            return _commitedVm;
        }

        public function set totalCpuFreq(value:Number):void {
            _totalCpuFreq = value;
        }
        public function get totalCpuFreq():Number {
            return _totalCpuFreq;
        }

        public function set totalMemory(value:Number):void {
            _totalMemory = value;
        }
        public function get totalMemory():Number {
            return _totalMemory;
        }

        public function set totalStorage(value:Number):void {
            _totalStorage = value;
        }
        public function get totalStorage():Number {
            return _totalStorage;
        }

        public function set totalVlan(value:Number):void {
            _totalVlan = value;
        }
        public function get totalVlan():Number {
            return _totalVlan;
        }
    }
}

