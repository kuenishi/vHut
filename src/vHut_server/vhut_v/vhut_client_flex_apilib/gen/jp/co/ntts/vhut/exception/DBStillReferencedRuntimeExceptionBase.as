/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (DBStillReferencedRuntimeException.as).
 */

package jp.co.ntts.vhut.exception {


    [Bindable]
	/**
	 * DBStillReferencedRuntimeException Entity Base Class.
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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
    public class DBStillReferencedRuntimeExceptionBase extends AbstractVhutRuntimeException {

        protected var _referencingEntity:String;
        protected var _referencingEntityIds:String;
        protected var _targetEntity:String;
        protected var _targetEntityId:String;

        public function set referencingEntity(value:String):void {
            _referencingEntity = value;
        }
        public function get referencingEntity():String {
            return _referencingEntity;
        }

        public function set referencingEntityIds(value:String):void {
            _referencingEntityIds = value;
        }
        public function get referencingEntityIds():String {
            return _referencingEntityIds;
        }

        public function set targetEntity(value:String):void {
            _targetEntity = value;
        }
        public function get targetEntity():String {
            return _targetEntity;
        }

        public function set targetEntityId(value:String):void {
            _targetEntityId = value;
        }
        public function get targetEntityId():String {
            return _targetEntityId;
        }
    }
}

