/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (RightDto.as).
 */

package jp.co.ntts.vhut.dto {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * RightDto Entity Base Class.
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
    public class RightDtoBase extends EventDispatcher {

        protected var _max:int;
        protected var _now:int;
        protected var _target:String;

        public function set max(value:int):void {
            _max = value;
        }
        public function get max():int {
            return _max;
        }

        public function set now(value:int):void {
            _now = value;
        }
        public function get now():int {
            return _now;
        }

        public function set target(value:String):void {
            _target = value;
        }
        public function get target():String {
            return _target;
        }
    }
}

