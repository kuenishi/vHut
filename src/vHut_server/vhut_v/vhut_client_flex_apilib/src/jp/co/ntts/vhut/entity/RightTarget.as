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
    [RemoteClass(alias="jp.co.ntts.vhut.entity.RightTarget")]
	/**
	 * RightTarget Enum Class.
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
    public class RightTarget extends Enum {

        public static const BASIC:RightTarget = new RightTarget("BASIC", _);
        public static const APP:RightTarget = new RightTarget("APP", _);
        public static const RAPP:RightTarget = new RightTarget("RAPP", _);
        public static const AIG:RightTarget = new RightTarget("AIG", _);
        public static const AI:RightTarget = new RightTarget("AI", _);
        public static const TEMPLATE:RightTarget = new RightTarget("TEMPLATE", _);
        public static const USER:RightTarget = new RightTarget("USER", _);
        public static const ROLE:RightTarget = new RightTarget("ROLE", _);
        public static const MANAGEMENT:RightTarget = new RightTarget("MANAGEMENT", _);
        public static const CONFIGURATION:RightTarget = new RightTarget("CONFIGURATION", _);

        function RightTarget(value:String = null, restrictor:* = null) {
            super((value || BASIC.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [BASIC, APP, RAPP, AIG, AI, TEMPLATE, USER, ROLE, MANAGEMENT, CONFIGURATION];
        }

        public static function valueOf(name:String):RightTarget {
            return RightTarget(BASIC.constantOf(name));
        }
    }
}