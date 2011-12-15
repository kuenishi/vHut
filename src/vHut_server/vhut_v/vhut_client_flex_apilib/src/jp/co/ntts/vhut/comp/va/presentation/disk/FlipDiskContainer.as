/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.disk
{
	import jp.co.ntts.vhut.comp.va.application.AddEvent;
	import jp.co.ntts.vhut.comp.va.domain.VaDragDataType;
	import jp.co.ntts.vhut.dto.AdditionalDiskDto;
	import jp.co.ntts.vhut.entity.Disk;
	import jp.co.ntts.vhut.entity.Vm;

	import mx.collections.IList;
	import mx.controls.Image;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	import mx.states.State;

	import spark.components.DataGroup;
	import spark.components.List;
	import spark.components.SkinnableContainer;
	import spark.components.supportClasses.ItemRenderer;
	import jp.co.ntts.vhut.core.presentation.ZSortLayout;

	[SkinState("normal")] //表
	[SkinState("back")] //裏
	[SkinState("disabled")] //無効
	[Event(name="addDisk", type="jp.co.ntts.vhut.comp.va.application.AddEvent")]
	[Event(name="removeDisk", type="jp.co.ntts.vhut.comp.va.application.RemoveEvent")]
	/**
	 * 反転するコンテナ
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
	public class FlipDiskContainer extends SkinnableContainer
	{
		public static const FLIP_SIDE_FRONT:String = "front";
		public static const FLIP_SIDE_BACK:String = "back";

		public function FlipDiskContainer()
		{
			super();
			setStyle("skinClass", FlipDiskContainerSkin);
			layout = new ZSortLayout();
			addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
			addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
			addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
		}

		public override function initialize():void
		{
			super.initialize();
			states.push(new State({name:"normal"}));
			states.push(new State({name:"back"}));
			states.push(new State({name:"disabled"}));
		}

		override protected function stateChanged(oldState:String, newState:String, recursive:Boolean):void
		{
			super.stateChanged(oldState, newState, recursive);
			invalidateSkinState();
		}

		override protected function getCurrentSkinState():String
		{
			if (flipSide == FLIP_SIDE_FRONT)
			{
				return "normal";
			}
			else if (flipSide == FLIP_SIDE_BACK)
			{
				return "back";
			}
			return "disabled";
		}

		///////////////////////////////////////////////////////////////////
		//
		//  Data
		//
		///////////////////////////////////////////////////////////////////

		[Bindable]
		public function set vm(value:Vm):void
		{
			_vm = value;
		}
		public function get vm():Vm
		{
			return _vm;
		}
		protected var _vm:Vm;

		[Bindable]
		public function set imageUrl(value:String):void
		{
			_imageUrl = value;
		}
		public function get imageUrl():String
		{
			return _imageUrl;
		}
		protected var _imageUrl:String;

		[Bindable]
		public function set diskList(value:IList):void
		{
			_diskList = value;
		}
		public function get diskList():IList
		{
			return _diskList;
		}
		protected var _diskList:IList;

		[Bindable]
		public function set diskNumMax(value:uint):void
		{
			_diskNumMax = value;
		}
		public function get diskNumMax():uint
		{
			return _diskNumMax;
		}
		protected var _diskNumMax:uint = 4;

		[Bindable]
		public function set flipSide(value:String):void
		{
			if(value == FLIP_SIDE_BACK)
			{
				_isFlipBackDefinedFromOutSide = true;
			}
			else if (value == FLIP_SIDE_FRONT)
			{
				_isFlipBackDefinedFromOutSide = false;
			}
			_flipSide = value;
			if (value == FLIP_SIDE_FRONT)
			{
				diskContainer.unSelect();
			}
			invalidateSkinState();
		}
		public function get flipSide():String
		{
			return _flipSide;
		}

		protected function set flipSideInside(value:String):void
		{
			if(_isFlipBackDefinedFromOutSide)
				return;
			_flipSide = value;
			if (value == FLIP_SIDE_FRONT)
			{
				diskContainer.unSelect();
			}
			invalidateSkinState();
		}
		protected function get flipSideInside():String
		{
			return _flipSide;
		}

		protected var _flipSide:String = FLIP_SIDE_FRONT;

		protected var _isFlipBackDefinedFromOutSide:Boolean = false;

		///////////////////////////////////////////////////////////////////
		//
		//  SkinParts
		//
		///////////////////////////////////////////////////////////////////

		/** VMの内容を表すイメージ */
		[SkinPart(required="false")]
		public var image:Image;

		/** Diskを格納するデータグループ */
		[SkinPart(required="false")]
		public var diskContainer:DiskContainer;

		/**
		 *  @private
		 */
		override protected function partAdded(partName:String, instance:Object):void
		{
			super.partAdded(partName, instance);
		}

		/**
		 *  @private
		 */
		override protected function partRemoved(partName:String, instance:Object):void
		{
			super.partRemoved(partName, instance);
		}

		///////////////////////////////////////////////////////////////
		//
		// Drop from outside
		//
		////////////////////////////////////////////////////////////////

		protected function dragEnterHandler(event:DragEvent):void
		{
			if(event.dragSource.hasFormat(VaDragDataType.DISK_TEMPLATE.toString()))
			{
				flipSideInside = FLIP_SIDE_BACK;
				DragManager.acceptDragDrop(event.target as UIComponent);
			}
		}

		protected function dragExitHandler(event:DragEvent):void
		{

			flipSideInside = FLIP_SIDE_FRONT;
		}

		protected function dragDropHandler(event:DragEvent):void
		{
			flipSideInside = FLIP_SIDE_FRONT;
			if(event.dragSource.hasFormat(VaDragDataType.DISK_TEMPLATE.toString())
			&& diskList.length < diskNumMax)
			{
				var additionalDisk:AdditionalDiskDto = event.dragSource.dataForFormat(VaDragDataType.DISK_TEMPLATE.toString()) as AdditionalDiskDto;
				if(additionalDisk)
					dispatchEvent(AddEvent.newAddDiskEvent(createNewDiskData(additionalDisk)));
			}
		}

		protected function createNewDiskData(additionalDisk:AdditionalDiskDto):Disk
		{
			var disk:Disk = Disk.newDisk(additionalDisk);
			disk.vm = vm;
			return disk;
		}

	}
}