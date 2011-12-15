/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.cnf.domain
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import jp.co.ntts.vhut.config.CloudConfig;
	import jp.co.ntts.vhut.config.ServiceConfig;
	import jp.co.ntts.vhut.core.GetEvent;
	import jp.co.ntts.vhut.dto.PerformanceDto;
	
	[Event(name="getServiceConfig", type="jp.co.ntts.vhut.core.GetEvent")]
	[Event(name="getCloudConfig", type="jp.co.ntts.vhut.core.GetEvent")]
	[ManagedEvents(names="getServiceConfig, getCloudConfig")]
	/**
	 * パフォーマンス情報の管理クラス.
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
	public class Configs extends EventDispatcher
	{
		public function Configs(target:IEventDispatcher=null)
		{
			super(target);
		}
		
		[Bindable]
		public var serviceConfig:ServiceConfig;
		
		[Bindable]
		public var cloudConfig:CloudConfig;
		
		public function updateServiceConfig():void
		{
			dispatchEvent(GetEvent.newGetServiceConfigEvent());
		}
		
		public function updateCloudConfig():void
		{
			dispatchEvent(GetEvent.newGetCloudConfigEvent());
		}
	}
}