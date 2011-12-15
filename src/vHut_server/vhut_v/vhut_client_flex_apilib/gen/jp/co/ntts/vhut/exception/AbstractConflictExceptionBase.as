/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (AbstractConflictException.as).
 */

package jp.co.ntts.vhut.exception {

    import mx.collections.ArrayCollection;

    [Bindable]
	/**
	 * AbstractConflictException Entity Base Class.
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
    public class AbstractConflictExceptionBase extends AbstractVhutException {

        protected var _detail:String;
        protected var _entity:Object;
        protected var _entries:ArrayCollection;

        public function get detail():String {
            return _detail;
        }

        public function get entity():Object {
            return _entity;
        }
    }
}
