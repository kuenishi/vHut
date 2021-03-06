/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (ReleasedApplicationTemplateSecurityGroupMap.as).
 */

package jp.co.ntts.vhut.entity {

    import flash.events.EventDispatcher;

    [Bindable]
	/**
	 * ReleasedApplicationTemplateSecurityGroupMap Entity Base Class.
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
    public class ReleasedApplicationTemplateSecurityGroupMapBase extends EventDispatcher {

        protected var _id:Number;
        protected var _releasedApplicationSecurityGroupTemplate:ReleasedApplicationSecurityGroupTemplate;
        protected var _releasedApplicationSecurityGroupTemplateId:Number;
        protected var _releasedApplicationTemplate:ReleasedApplicationTemplate;
        protected var _releasedApplicationTemplateId:Number;
        protected var _version:Number;

        public function set id(value:Number):void {
            _id = value;
        }
        public function get id():Number {
            return _id;
        }

        public function set releasedApplicationSecurityGroupTemplate(value:ReleasedApplicationSecurityGroupTemplate):void {
            _releasedApplicationSecurityGroupTemplate = value;
        }
        public function get releasedApplicationSecurityGroupTemplate():ReleasedApplicationSecurityGroupTemplate {
            return _releasedApplicationSecurityGroupTemplate;
        }

        public function set releasedApplicationSecurityGroupTemplateId(value:Number):void {
            _releasedApplicationSecurityGroupTemplateId = value;
        }
        public function get releasedApplicationSecurityGroupTemplateId():Number {
            return _releasedApplicationSecurityGroupTemplateId;
        }

        public function set releasedApplicationTemplate(value:ReleasedApplicationTemplate):void {
            _releasedApplicationTemplate = value;
        }
        public function get releasedApplicationTemplate():ReleasedApplicationTemplate {
            return _releasedApplicationTemplate;
        }

        public function set releasedApplicationTemplateId(value:Number):void {
            _releasedApplicationTemplateId = value;
        }
        public function get releasedApplicationTemplateId():Number {
            return _releasedApplicationTemplateId;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }
    }
}

