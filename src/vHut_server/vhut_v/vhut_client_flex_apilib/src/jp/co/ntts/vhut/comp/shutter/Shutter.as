/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.shutter
{
	import flash.events.Event;

	import spark.components.Button;
	import spark.components.Label;
	import spark.components.SkinnableContainer;

	/**
	 * 開閉可能なコンテナ.
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
	[SkinState("open")]
	[Event(name="change", type="flash.events.Event")]
	public class Shutter extends SkinnableContainer
	{
		/** コンストラクタ. */
		public function Shutter()
		{
			super();
			setStyle("skinClass", ShutterSkin);
		}

		/** タイトルを表示するラベル */
		[SkinPart(required="false")]
		public var titleLabel:Label;

		/** タイトルを開閉するボタン */
		[SkinPart(required="false")]
		public var iconButton:Button;

		[Bindable("change")]
		/** 開閉状態 */
		public function get isOpen():Boolean
		{
			return _isOpen;
		}
		private var _isOpen:Boolean = false;

		[Bindable]
		/** タイトル */
		public function set title(value:String):void
		{
			if (_title == value)
				return;

			_title = value;

			if (titleLabel)
				titleLabel.text = value;
		}
		public function get title():String
		{
			return _title;
		}
		private var _title:String;

		/** 開く */
		public function open():void
		{
			if (isOpen) return;
			_isOpen = true;
			dispatchEvent(new Event(Event.CHANGE));
			invalidateSkinState();
		}

		/** 閉じる */
		public function close():void
		{
			if (!isOpen) return;
			_isOpen = false;
			dispatchEvent(new Event(Event.CHANGE));
			invalidateSkinState();
		}

		/** 開閉状態を変更する */
		public function toggle():void
		{
			if (isOpen)
			{
				close();
			}
			else
			{
				open();
			}
		}

		//--------------------------------------------------------------------------
		//
		//  Overridden methods
		//
		//--------------------------------------------------------------------------


		//----------------------------------
		//  enabled
		//----------------------------------

		/**
		 *  @private
		 */
		override public function set enabled(value:Boolean):void
		{
			super.enabled = value;
			close();
		}

		/** 現在のスキンの状態を取得する */
		override protected function getCurrentSkinState():String
		{

			if (!enabled)
			{
				return "disabled" //無効状態
			}
			else if (isOpen)
			{
				return "open" //開いている
			}
			else
			{
				return "normal" //閉じている
			}
		}

		/** スキンパーツが追加された時の処理 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
			if (instance == titleLabel)
			{
				titleLabel.text = title;
			}
		}

	}
}