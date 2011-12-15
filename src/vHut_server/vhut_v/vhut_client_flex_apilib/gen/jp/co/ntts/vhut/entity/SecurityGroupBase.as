/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (SecurityGroup.as).
 */

package jp.co.ntts.vhut.entity {

    import flash.events.EventDispatcher;
    import mx.collections.ArrayCollection;

    [Bindable]
	/**
	 * SecurityGroup Entity Base Class.
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
    public class SecurityGroupBase extends EventDispatcher implements IIdentifiableEntity {

        protected var _cloudId:Number;
        protected var _id:Number;
        protected var _name:String;
        protected var _network:Network;
        protected var _networkAdapterList:ArrayCollection;
        protected var _networkId:Number;
        protected var _version:Number;
        protected var _vlanReservationList:ArrayCollection;

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

        public function set name(value:String):void {
            _name = value;
        }
        public function get name():String {
            return _name;
        }

        public function set network(value:Network):void {
            _network = value;
        }
        public function get network():Network {
            return _network;
        }

        public function set networkAdapterList(value:ArrayCollection):void {
            _networkAdapterList = value;
        }
        public function get networkAdapterList():ArrayCollection {
            return _networkAdapterList;
        }

        public function set networkId(value:Number):void {
            _networkId = value;
        }
        public function get networkId():Number {
            return _networkId;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }

        public function set vlanReservationList(value:ArrayCollection):void {
            _vlanReservationList = value;
        }
        public function get vlanReservationList():ArrayCollection {
            return _vlanReservationList;
        }
    }
}
