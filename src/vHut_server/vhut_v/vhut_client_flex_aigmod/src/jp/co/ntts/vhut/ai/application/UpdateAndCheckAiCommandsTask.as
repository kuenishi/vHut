/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.ai.application
{
	import flash.events.Event;
	
	import jp.co.ntts.vhut.ai.domain.Ais;
	import jp.co.ntts.vhut.config.VhutConfig;
	import jp.co.ntts.vhut.util.VhutTimer;
	
	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	
	import org.spicefactory.lib.reflect.types.Void;
	import org.spicefactory.lib.task.Task;
	
	/**
	 * 選択中のApplicationInstanceに関連するコマンドを更新し、それらが無くなることを確認するタスクです.
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
	public class UpdateAndCheckAiCommandsTask extends Task
	{
		public function UpdateAndCheckAiCommandsTask(ais:Ais)
		{
			super();
			_ais = ais;
			_timer = new VhutTimer(VhutConfig.COMMAND_INTERVAL, update, this);
		}
		
		private var _ais:Ais;
		
		private var _timer:VhutTimer;
		
		private var _watcher:ChangeWatcher;
		
		override protected function doStart():void
		{
			_watcher = BindingUtils.bindSetter(check, _ais, "isTargetAiCommandsFinished");
			_timer.start();
		}
		
		override protected function doCancel():void
		{
			finalize();
		}
		
		override protected function doSkip():void
		{
			finalize();
		}
		
		override protected function doSuspend():void
		{
			finalize();
		}
		
		override protected function doTimeout():void
		{
			finalize();
		}
		
		public function update():void
		{
			_ais.updateTargetAiCommands();
		}
		
		public function check(value:Boolean):void
		{
			if(value)
			{
				finalize();
				complete();
			}
		}
		
		private function finalize():void
		{
			_timer.stop();
			_watcher.unwatch();
		}
	}
}