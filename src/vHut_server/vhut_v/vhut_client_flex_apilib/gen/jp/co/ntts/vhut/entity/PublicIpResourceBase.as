/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (PublicIpResource.as).
 */

package jp.co.ntts.vhut.entity {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * PublicIpResource Entity Base Class.
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
    public class PublicIpResourceBase extends EventDispatcher {

        protected var _cloudId:Number;
        protected var _id:Number;
        protected var _publicIpMax:Number;
        protected var _publicIpTerminablyUsed:Number;
        protected var _time:Date;
        protected var _version:Number;

        public function set cloudId(value:Number):void {
            _cloudId = value;
        }
        public function get cloudId():Number {
            return _cloudId;
        }

        public function set id(value:Number):void {
            _id = value;
        }
        public function get id():Number {
            return _id;
        }

        public function set publicIpMax(value:Number):void {
            _publicIpMax = value;
        }
        public function get publicIpMax():Number {
            return _publicIpMax;
        }

        public function set publicIpTerminablyUsed(value:Number):void {
            _publicIpTerminablyUsed = value;
        }
        public function get publicIpTerminablyUsed():Number {
            return _publicIpTerminablyUsed;
        }

        public function set time(value:Date):void {
            _time = value;
        }
        public function get time():Date {
            return _time;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }
    }
}

