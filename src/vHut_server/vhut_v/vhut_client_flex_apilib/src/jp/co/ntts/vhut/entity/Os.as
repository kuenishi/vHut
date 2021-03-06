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
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Os")]
	/**
	 * Os Enum Class.
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
    public class Os extends Enum {

        public static const OTHER:Os = new Os("OTHER", _);
        public static const OTHER_LINUX:Os = new Os("OTHER_LINUX", _);
        public static const WINDOWS_XP:Os = new Os("WINDOWS_XP", _);
        public static const WINDOWS_VISTA:Os = new Os("WINDOWS_VISTA", _);
        public static const WINDOWS_VISTA_64:Os = new Os("WINDOWS_VISTA_64", _);
        public static const WINDOWS_7:Os = new Os("WINDOWS_7", _);
        public static const WINDOWS_7_64:Os = new Os("WINDOWS_7_64", _);
        public static const WINDOWS_2000:Os = new Os("WINDOWS_2000", _);
        public static const WINDOWS_2003:Os = new Os("WINDOWS_2003", _);
        public static const WINDOWS_2003_64:Os = new Os("WINDOWS_2003_64", _);
        public static const WINDOWS_2008:Os = new Os("WINDOWS_2008", _);
        public static const WINDOWS_2008_64:Os = new Os("WINDOWS_2008_64", _);
        public static const WINDOWS_2008_R2:Os = new Os("WINDOWS_2008_R2", _);
        public static const RHEL_3:Os = new Os("RHEL_3", _);
        public static const RHEL_3_64:Os = new Os("RHEL_3_64", _);
        public static const RHEL_4:Os = new Os("RHEL_4", _);
        public static const RHEL_4_64:Os = new Os("RHEL_4_64", _);
        public static const RHEL_5:Os = new Os("RHEL_5", _);
        public static const RHEL_5_64:Os = new Os("RHEL_5_64", _);
        public static const RHEL_6:Os = new Os("RHEL_6", _);
        public static const RHEL_6_64:Os = new Os("RHEL_6_64", _);
        public static const CENTOS_3:Os = new Os("CENTOS_3", _);
        public static const CENTOS_3_64:Os = new Os("CENTOS_3_64", _);
        public static const CENTOS_4:Os = new Os("CENTOS_4", _);
        public static const CENTOS_4_64:Os = new Os("CENTOS_4_64", _);
        public static const CENTOS_5:Os = new Os("CENTOS_5", _);
        public static const CENTOS_5_64:Os = new Os("CENTOS_5_64", _);
        public static const CENTOS_6:Os = new Os("CENTOS_6", _);
        public static const CENTOS_6_64:Os = new Os("CENTOS_6_64", _);

        function Os(value:String = null, restrictor:* = null) {
            super((value || OTHER.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [OTHER, OTHER_LINUX, WINDOWS_XP, WINDOWS_VISTA, WINDOWS_VISTA_64, WINDOWS_7, WINDOWS_7_64, WINDOWS_2000, WINDOWS_2003, WINDOWS_2003_64, WINDOWS_2008, WINDOWS_2008_64, WINDOWS_2008_R2, RHEL_3, RHEL_3_64, RHEL_4, RHEL_4_64, RHEL_5, RHEL_5_64, RHEL_6, RHEL_6_64, CENTOS_3, CENTOS_3_64, CENTOS_4, CENTOS_4_64, CENTOS_5, CENTOS_5_64, CENTOS_6, CENTOS_6_64];
        }

        public static function valueOf(name:String):Os {
            return Os(OTHER.constantOf(name));
        }
    }
}