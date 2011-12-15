/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.presentation
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import jp.co.ntts.vhut.core.application.ModuleChangeEvent;
	
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	/**
	 * 
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
	public class ModulePMBase extends EventDispatcher
	{
		public function ModulePMBase()
		{
			super();
			_resourceManager = ResourceManager.getInstance();
		}
		
		protected function get resourceManager():IResourceManager
		{
			return _resourceManager;
		}
		private var _resourceManager:IResourceManager;
		
		public function get moduleName():String
		{
			return null;
		}
		
		[MessageHandler(selector="moduleChange",scope="local")]
		/**
		 * モジュールが変更されるタイミングで呼ばれるイベントです.
		 */
		public function onModuleChange(event:ModuleChangeEvent):void
		{
			if(event.newModuleId == moduleName)
			{
				onModuleEnter();
			}
			else if(event.oldModuleId == moduleName)
			{
				onModuleExit();
			}
		}
		
		protected function onModuleEnter():void
		{
			
		}
		
		protected function onModuleExit():void
		{
			
		}
	}
}