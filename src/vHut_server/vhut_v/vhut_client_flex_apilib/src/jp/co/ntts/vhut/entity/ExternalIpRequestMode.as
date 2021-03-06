/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package jp.co.ntts.vhut.entity {

    import jp.co.ntts.vhut.util.Enum;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.ExternalIpRequestMode")]
	/**
	 * ExternalIpRequestMode Enum Class.
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
    public class ExternalIpRequestMode extends Enum {

        public static const AUTO:ExternalIpRequestMode = new ExternalIpRequestMode("AUTO", _);
        public static const PERVM:ExternalIpRequestMode = new ExternalIpRequestMode("PERVM", _);
        public static const PERAPP:ExternalIpRequestMode = new ExternalIpRequestMode("PERAPP", _);
        public static const MANUAL:ExternalIpRequestMode = new ExternalIpRequestMode("MANUAL", _);

        function ExternalIpRequestMode(value:String = null, restrictor:* = null) {
            super((value || AUTO.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [AUTO, PERVM, PERAPP, MANUAL];
        }

        public static function valueOf(name:String):ExternalIpRequestMode {
            return ExternalIpRequestMode(AUTO.constantOf(name));
        }
    }
}