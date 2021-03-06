/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package jp.co.ntts.vhut.entity {

    import jp.co.ntts.vhut.util.Enum;

    [Bindable]
    public class VhutUserStatus extends Enum {
		
		/** 初期状態 */
        public static const NONE:VhutUserStatus = new VhutUserStatus("NONE", _);
		/** 未登録 */
        public static const NOTREGISTERED:VhutUserStatus = new VhutUserStatus("NOTREGISTERED", _);
		/** 登録済み */
        public static const REGISTERED:VhutUserStatus = new VhutUserStatus("REGISTERED", _);

        function VhutUserStatus(value:String = null, restrictor:* = null) {
            super((value || NONE.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [NONE, NOTREGISTERED, REGISTERED];
        }

        public static function valueOf(name:String):VhutUserStatus {
            return VhutUserStatus(NONE.constantOf(name));
        }
    }
}