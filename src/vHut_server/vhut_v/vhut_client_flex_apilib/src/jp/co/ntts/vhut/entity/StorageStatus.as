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
    [RemoteClass(alias="jp.co.ntts.vhut.entity.StorageStatus")]
	/**
	 * StorageStatus Enum Class.
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
    public class StorageStatus extends Enum {

        public static const DOWN:StorageStatus = new StorageStatus("DOWN", _);
        public static const UP:StorageStatus = new StorageStatus("UP", _);
        public static const MAINTENANCE:StorageStatus = new StorageStatus("MAINTENANCE", _);
        public static const ERROR:StorageStatus = new StorageStatus("ERROR", _);
        public static const UNKNOWN:StorageStatus = new StorageStatus("UNKNOWN", _);

        function StorageStatus(value:String = null, restrictor:* = null) {
            super((value || DOWN.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [DOWN, UP, MAINTENANCE, ERROR, UNKNOWN];
        }

        public static function valueOf(name:String):StorageStatus {
            return StorageStatus(DOWN.constantOf(name));
        }
    }
}