/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package jp.co.ntts.vhut.entity {

    import jp.co.ntts.vhut.util.Enum;

    [Bindable]
    public class ApplicationInstanceGroupResourceStatus extends Enum {
		
		/** 初期状態 */
        public static const NONE:ApplicationInstanceGroupResourceStatus = new ApplicationInstanceGroupResourceStatus("NONE", _);
		/** 不十分 */
        public static const NOTENOUGH:ApplicationInstanceGroupResourceStatus = new ApplicationInstanceGroupResourceStatus("NOTENOUGH", _);
		/** 十分 */
        public static const ENOUGH:ApplicationInstanceGroupResourceStatus = new ApplicationInstanceGroupResourceStatus("ENOUGH", _);

        function ApplicationInstanceGroupResourceStatus(value:String = null, restrictor:* = null) {
            super((value || NONE.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [NONE, NOTENOUGH, ENOUGH];
        }

        public static function valueOf(name:String):ApplicationInstanceGroupResourceStatus {
            return ApplicationInstanceGroupResourceStatus(NONE.constantOf(name));
        }
    }
}