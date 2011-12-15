/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.aig.wiz.presentation
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.ProgressEvent;
	import flash.net.FileFilter;
	import flash.net.FileReference;

	import jp.co.ntts.vhut.aig.domain.ImportingAigs;
	import jp.co.ntts.vhut.comp.wiz.domain.IValidator;

	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	import org.spicefactory.lib.reflect.types.Void;

	/**
	 * アプリケーションインスタンスグループ一括ウィザードの
	 * インポートファイル選択時のViewのPMクラス.
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
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class FileViewPM extends EventDispatcher implements IValidator
	{
		public static const CHANGE_STATUS:String = "changeStatus";

		public static const STATUS_INIT:String = "statusInit";
		public static const STATUS_BROWS:String = "statusBrows";
		public static const STATUS_SELECT:String = "statusSelect";
		public static const STATUS_LOAD:String = "statusLoad";
		public static const STATUS_COMPLETE:String = "statusComplete";

		[Inject]
		[Bindable]
		public  var importingAigs:ImportingAigs;

		private var _rm:IResourceManager;

		public function FileViewPM()
		{
			_rm = ResourceManager.getInstance();
		}

		public function onInitializedByWizard():void
		{
			_fileReference = new FileReference();
			_fileReference.addEventListener(Event.SELECT, onSelect);
			status = STATUS_INIT;
		}

		protected var _fileReference:FileReference;

		[Bindable("changeStatus")]
		public function get status():String
		{
			return _status;
		}
		public function set status(value:String):void
		{
			_status = value;
			dispatchEvent(new Event(CHANGE_STATUS));
		}
		private var _status:String;

		public function get isValid():Boolean
		{
			return status == STATUS_COMPLETE;
		}

		public function brows():void
		{
			_fileReference.browse([new FileFilter("xml document", "*.xml;")]);
			status = STATUS_BROWS;
		}

		public function onSelect(event:Event):void
		{
			status = STATUS_SELECT;
			_fileReference.addEventListener(ProgressEvent.PROGRESS, onProgress);
			_fileReference.addEventListener(Event.COMPLETE, onComplete);
			_fileReference.load();
		}

		public function onProgress(event:ProgressEvent):void
		{
			status = STATUS_LOAD;
		}

		public function onComplete(event:Event):void
		{
			importingAigs.importXml(new XML(event.target.data));
			status = STATUS_COMPLETE;
		}

		[Bindable("changeStatus")]
		public function get fileName():String
		{
			switch(status)
			{
				case STATUS_SELECT:
				case STATUS_LOAD:
				case STATUS_COMPLETE:
					return _fileReference.name;
				default:
					return "";
			}
		}

	}
}