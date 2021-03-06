/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (VlanReservation.as).
 */

package jp.co.ntts.vhut.entity {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * VlanReservation Entity Base Class.
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
    public class VlanReservationBase extends EventDispatcher implements IIdentifiableEntity {

        protected var _cloudId:Number;
        protected var _id:Number;
        protected var _network:Network;
        protected var _networkId:Number;
        protected var _reservation:Reservation;
        protected var _reservationId:Number;
        protected var _securityGroup:SecurityGroup;
        protected var _securityGroupId:Number;
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

        public function set network(value:Network):void {
            _network = value;
        }
        public function get network():Network {
            return _network;
        }

        public function set networkId(value:Number):void {
            _networkId = value;
        }
        public function get networkId():Number {
            return _networkId;
        }

        public function set reservation(value:Reservation):void {
            _reservation = value;
        }
        public function get reservation():Reservation {
            return _reservation;
        }

        public function set reservationId(value:Number):void {
            _reservationId = value;
        }
        public function get reservationId():Number {
            return _reservationId;
        }

        public function set securityGroup(value:SecurityGroup):void {
            _securityGroup = value;
        }
        public function get securityGroup():SecurityGroup {
            return _securityGroup;
        }

        public function set securityGroupId(value:Number):void {
            _securityGroupId = value;
        }
        public function get securityGroupId():Number {
            return _securityGroupId;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }
    }
}

