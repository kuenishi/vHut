/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (ApplicationInstanceVmSecurityGroupMap.as).
 */

package jp.co.ntts.vhut.entity {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * ApplicationInstanceVmSecurityGroupMap Entity Base Class.
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
    public class ApplicationInstanceVmSecurityGroupMapBase extends EventDispatcher {

        protected var _applicationInstanceSecurityGroup:ApplicationInstanceSecurityGroup;
        protected var _applicationInstanceSecurityGroupId:Number;
        protected var _applicationInstanceVm:ApplicationInstanceVm;
        protected var _applicationInstanceVmId:Number;
        protected var _id:Number;
        protected var _version:Number;

        public function set applicationInstanceSecurityGroup(value:ApplicationInstanceSecurityGroup):void {
            _applicationInstanceSecurityGroup = value;
        }
        public function get applicationInstanceSecurityGroup():ApplicationInstanceSecurityGroup {
            return _applicationInstanceSecurityGroup;
        }

        public function set applicationInstanceSecurityGroupId(value:Number):void {
            _applicationInstanceSecurityGroupId = value;
        }
        public function get applicationInstanceSecurityGroupId():Number {
            return _applicationInstanceSecurityGroupId;
        }

        public function set applicationInstanceVm(value:ApplicationInstanceVm):void {
            _applicationInstanceVm = value;
        }
        public function get applicationInstanceVm():ApplicationInstanceVm {
            return _applicationInstanceVm;
        }

        public function set applicationInstanceVmId(value:Number):void {
            _applicationInstanceVmId = value;
        }
        public function get applicationInstanceVmId():Number {
            return _applicationInstanceVmId;
        }

        public function set id(value:Number):void {
            _id = value;
        }
        public function get id():Number {
            return _id;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }
    }
}
