/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (StorageReservationDiskTemplateMap.as).
 */

package jp.co.ntts.vhut.entity {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * StorageReservationDiskTemplateMap Entity Base Class.
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
    public class StorageReservationDiskTemplateMapBase extends EventDispatcher {

        protected var _diskTemplate:DiskTemplate;
        protected var _diskTemplateId:Number;
        protected var _id:Number;
        protected var _storageReservation:StorageReservation;
        protected var _storageReservationId:Number;
        protected var _version:Number;

        public function set diskTemplate(value:DiskTemplate):void {
            _diskTemplate = value;
        }
        public function get diskTemplate():DiskTemplate {
            return _diskTemplate;
        }

        public function set diskTemplateId(value:Number):void {
            _diskTemplateId = value;
        }
        public function get diskTemplateId():Number {
            return _diskTemplateId;
        }

        public function set id(value:Number):void {
            _id = value;
        }
        public function get id():Number {
            return _id;
        }

        public function set storageReservation(value:StorageReservation):void {
            _storageReservation = value;
        }
        public function get storageReservation():StorageReservation {
            return _storageReservation;
        }

        public function set storageReservationId(value:Number):void {
            _storageReservationId = value;
        }
        public function get storageReservationId():Number {
            return _storageReservationId;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }
    }
}

