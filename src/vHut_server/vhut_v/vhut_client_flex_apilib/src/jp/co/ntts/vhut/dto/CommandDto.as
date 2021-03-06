/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package jp.co.ntts.vhut.dto {
	import flash.events.Event;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.dto.CommandDto")]
	/**
	 * CommandDto Dto Class.
	 * <p></p>
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
    public class CommandDto extends CommandDtoBase {

		override public function set aigName(value:String):void
		{
			super.aigName = value;
			_serviceTargetName = value;
			dispatchEvent(new Event("serviceTargetNameChanged"));
		}

		override public function set appName(value:String):void
		{
			super.appName = value;
			_serviceTargetName = value;
			dispatchEvent(new Event("serviceTargetNameChanged"));
		}

		[Bindable("serviceTargetNameChanged")]
		public function get serviceTargetName():String
		{
			return _serviceTargetName;
		}
		private var _serviceTargetName:String;

		override public function set vmName(value:String):void {
			super.vmName = value;
			_cloudTargetName = value;
			dispatchEvent(new Event("cloudTargetNameChanged"));
		}

		override public function set templateName(value:String):void {
			super.templateName = value;
			_cloudTargetName = value;
			dispatchEvent(new Event("cloudTargetNameChanged"));
		}

		[Bindable("cloudTargetNameChanged")]
		public function get cloudTargetName():String
		{
			return _cloudTargetName;
		}
		private var _cloudTargetName:String;
    }
}