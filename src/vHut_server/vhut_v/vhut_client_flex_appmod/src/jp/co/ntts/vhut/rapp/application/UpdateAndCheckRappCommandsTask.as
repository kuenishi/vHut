/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.rapp.application
{
	import flash.events.Event;

	import jp.co.ntts.vhut.config.VhutConfig;
	import jp.co.ntts.vhut.app.domain.Applications;
	import jp.co.ntts.vhut.rapp.domain.ReleasedApplications;
	import jp.co.ntts.vhut.util.VhutTimer;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;

	import org.spicefactory.lib.reflect.types.Void;
	import org.spicefactory.lib.task.Task;

	/**
	 * 選択中のApplicationに関連するコマンドを更新し、それらが無くなることを確認するタスクです.
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
	public class UpdateAndCheckRappCommandsTask extends Task
	{
		public function UpdateAndCheckRappCommandsTask(rapps:ReleasedApplications)
		{
			super();
			_rapps = rapps;
			_timer = new VhutTimer(VhutConfig.COMMAND_INTERVAL, update, this);
		}

		private var _rapps:ReleasedApplications;

		private var _timer:VhutTimer;

		private var _watcher:ChangeWatcher;

		override protected function doStart():void
		{
			_watcher = BindingUtils.bindSetter(check, _rapps, "isTargetReleasedApplicationCommandsFinished");
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
			_rapps.updateTargetReleasedApplicationCommands();
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
			if (_watcher)
				_watcher.unwatch();
		}
	}
}