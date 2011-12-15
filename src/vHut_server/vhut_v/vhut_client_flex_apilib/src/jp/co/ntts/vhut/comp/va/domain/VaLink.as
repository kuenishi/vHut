/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.geom.Point;
	import flash.geom.Rectangle;

	import jp.co.ntts.vhut.util.CloneUtil;

	import mx.utils.ObjectUtil;

	/**
	 * ノード間の接続情報
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
	public class VaLink extends EventDispatcher
	{
		public static const CHANGE_POINTS:String = "changePoints";
		public static const CHANGE_HALF_POINTS:String = "changeHalfPoints";

		/**
		 * 座標点の配列を開始点から長さの割合で切断し、新たな配列を作ります.
		 * @param points 対象の配列
		 * @param rate 長さの割合
		 * @param reverse 反転させてから分断するか
		 * @return 新しい配列
		 *
		 */
		public static function getDividedPoints(points:Array, rate:Number, reverse:Boolean=false):Array
		{
			if(rate<0) return new Array();
			var fullPoints:Array
			if(reverse)
			{
				fullPoints = points.reverse();
			}
			else
			{
				fullPoints = points.concat();
			}
			if(rate>=1 || fullPoints.length<=1) return fullPoints;

			var i:int;
			var pointA:Point;
			var pointB:Point;
			var pointC:Point;
			var deltaPoint:Point;
			//総延長
			var fullLength:Number;
			//分割後の長さ
			var dividedLength:Number;
			//分割後の位置情報
			var dividedPoints:Array = new Array(fullPoints[0]);

			//総延長を求める。
			fullLength = 0;
			for (i=0; i<fullPoints.length-1; i++)
			{
				pointA = fullPoints[i];
				pointB = fullPoints[i+1];
				var delta:Point = pointB.subtract(pointA);
				fullLength += delta.length;
			}

			//分割後の長さを求める
			dividedLength = fullLength * rate;

			//分割後の位置情報に追加する
			var currentLength:Number = 0;
			for (i=0; i<fullPoints.length-1; i++)
			{
				pointA = fullPoints[i];
				pointB = fullPoints[i+1];
				deltaPoint = pointB.subtract(pointA);

				if(currentLength + deltaPoint.length > dividedLength)
				{
					//線分の分割
					var deltaLength:Number = dividedLength - currentLength;
					var deltaRate:Number = deltaLength / deltaPoint.length;
					pointC = pointA.add(new Point(deltaPoint.x * deltaRate, deltaPoint.y * deltaRate));
					dividedPoints.push(pointC);
					break;
				}
				else
				{
					dividedPoints.push(pointB);
				}

				currentLength += deltaPoint.length;
			}
			return dividedPoints;
		}

		public function VaLink()
		{
			_vmPoint = null;
			_sgPoint = null;
			_vmCenterPoint = null;
			_sgCenterPoint = null;
			_points = null;
		}
		private var _vmPoint:Point;
		private var _sgPoint:Point;

		/** NetworkAdapterのID */
		public var id:Number;

		/////////////////////////////////////////////
		//
		//  offset関連
		//
		/////////////////////////////////////////////

		public function get offset():Number
		{
			return cableOffset + connectorHeight;
		}

		/** コネクタとケーブルの間隔 */
		public function set cableOffset(value:uint):void
		{
			_cableOffset = value;

			invalidatePoints();
		}
		public function get cableOffset():uint
		{
			return _cableOffset;
		}
		private var _cableOffset:uint = 5;

		/** コネクタの高さ*/
		public function set connectorHeight(value:Number):void
		{
			_connectorHeight = value;

			invalidatePoints();
		}
		public function get connectorHeight():Number
		{
			return _connectorHeight;
		}
		private var _connectorHeight:Number = 10;


		/////////////////////////////////////////////
		//
		//  Vm関連
		//
		/////////////////////////////////////////////

		[Bindable]
		/** VmのプライベートID */
		public var vmId:Number;

		[Bindable]
		/** Vmの外形 */
		public function set vmRect(value:Rectangle):void
		{
			_vmRect = value;
			_vmPoint = null;
			_vmCenterPoint = null;

			invalidatePoints();
		}
		public function get vmRect():Rectangle
		{
			return _vmRect;
		}
		private var _vmRect:Rectangle;

		[Bindable]
		/** Vmの接続ポイント */
		public function set vmSide(value:VaSide):void
		{
			if(_vmSide && _vmSide.equals(value))
				return;

			_vmPoint = null;

			_vmSide= value;

			invalidatePoints();
		}
		public function get vmSide():VaSide
		{
			return _vmSide;
		}
		private var _vmSide:VaSide;

		[Bindable]
		/** Vmの接続ポイントの接続順（左から） */
		public function set vmOrder(value:Number):void
		{
			if(_vmOrder == value)
				return;

			_vmPoint = null;

			_vmOrder = value;

			invalidatePoints();
		}
		public function get vmOrder():Number
		{
			return _vmOrder;
		}
		private var _vmOrder:Number;

		/////////////////////////////////////////////
		//
		//  SecurityGroup関連
		//
		/////////////////////////////////////////////

		[Bindable]
		/** SecurityGroupのプライベートID */
		public var sgId:Number;

		/** SecurityGroupの外形 */
		public function set sgRect(value:Rectangle):void
		{
			_sgRect = value;
			_sgPoint = null;
			_sgCenterPoint = null;

			invalidatePoints();
		}
		public function get sgRect():Rectangle
		{
			return _sgRect;
		}
		private var _sgRect:Rectangle;

		[Bindable]
		/** SecurityGroupの接続ポイント */
		public function set sgSide(value:VaSide):void
		{
			if(_sgSide && _sgSide.equals(value))
				return;

			_sgPoint = null;

			_sgSide= value;

			invalidatePoints();
		}
		public function get sgSide():VaSide
		{
			return _sgSide;
		}
		private var _sgSide:VaSide;

		[Bindable]
		/** SecurityGroupの接続ポイントの接続順（左から） */
		public function set sgOrder(value:Number):void
		{
			if(_sgOrder == value)
				return;

			_sgPoint = null;

			_sgOrder = value;

			invalidatePoints();
		}
		public function get sgOrder():Number
		{
			return _sgOrder;
		}
		private var _sgOrder:Number;


		/////////////////////////////////////////////
		//
		//  描画点関連（リードオンリー）
		//
		/////////////////////////////////////////////

		protected var pointSourceChanged:Boolean = true;

		protected function invalidatePoints():void
		{
			pointSourceChanged = true;
			_halfPoints = null;
			dispatchEvent(new Event(CHANGE_POINTS));
		}

		[Bindable("changePoints")]
		public function get vmPoint():Point
		{
			if(_vmPoint == null)
			{
				_vmPoint = getVmPoint();
			}
			return _vmPoint;
		}

		public function getVmPoint(offset:Number=0):Point
		{
			offset += connectorHeight;
			var point:Point = new Point(vmRect.x, vmRect.y);
			switch(vmSide)
			{
				case VaSide.SOUTH:
					point.x += vmRect.width * vmOrder;
					point.y += vmRect.height + offset;
					break;
				case VaSide.EAST:
					point.x += vmRect.width + offset;
					point.y += vmRect.height * (1-vmOrder);
					break;
				case VaSide.NORTH:
					point.x += vmRect.width * (1-vmOrder);
					point.y += -offset;
					break;
				case VaSide.WEST:
					point.x += -offset;
					point.y += vmRect.height * vmOrder;
					break;
			}
			return point;
		}

		[Bindable("changePoints")]
		public function get sgPoint():Point
		{
			if(_sgPoint == null)
			{
				_sgPoint = getSgPoint();
			}
			return _sgPoint;
		}

		public function getSgPoint(offset:Number=0):Point
		{
			offset += connectorHeight;
			var point:Point = new Point(sgRect.x, sgRect.y);
			switch(sgSide)
			{
				case VaSide.SOUTH:
					point.x += sgRect.width * sgOrder;
					point.y += sgRect.height + offset;
					break;
				case VaSide.EAST:
					point.x += sgRect.width + offset;
					point.y += sgRect.height * (1-sgOrder);
					break;
				case VaSide.NORTH:
					point.x += sgRect.width * (1-sgOrder);
					point.y += -offset;
					break;
				case VaSide.WEST:
					point.x += -offset;
					point.y += sgRect.height * sgOrder;
					break;
			}
			return point;
		}

		[Bindable("changePoints")]
		public function get vmCenterPoint():Point
		{
			if(_vmCenterPoint == null)
			{
				_vmCenterPoint = new Point(vmRect.x+vmRect.width/2, vmRect.y+vmRect.height/2);
			}
			return _vmCenterPoint;
		}
		private var _vmCenterPoint:Point;

		[Bindable("changePoints")]
		public function get sgCenterPoint():Point
		{
			if(_sgCenterPoint == null)
			{
				_sgCenterPoint = new Point(sgRect.x+sgRect.width/2, sgRect.y+sgRect.height/2);
			}
			return _sgCenterPoint;
		}

		private var _sgCenterPoint:Point;

		[Bindable("changePoints")]
		public function get vmRotation():Number
		{
			return convertSideToRotation(vmSide)
		}

		[Bindable("changePoints")]
		public function get sgRotation():Number
		{
			return convertSideToRotation(sgSide)
		}

		protected function convertSideToRotation(side:VaSide):Number
		{
			switch(side)
			{
				case VaSide.NORTH:
					return 180;
				case VaSide.EAST:
					return 270;
				case VaSide.SOUTH:
					return 0;
				case VaSide.WEST:
					return 90;
			}
			return 0;
		}

		[Bindable("changePoints")]
		public function get points():Array
		{
			if(pointSourceChanged)
			{
				updatePoints();
				pointSourceChanged = false;
			}
			return _points;
		}

		private var _points:Array;

		private function updatePoints():void
		{
			var midX:Number;
			var midY:Number;
			var targetX:Number;
			var targetY:Number;
			var targetRight:Number;
			var targetLeft:Number;
			var targetTop:Number;
			var targetBottom:Number;

			_points = new Array();
			_points.push(sgPoint);
			if(sgSide.equals(VaSide.EAST) && vmSide.equals(VaSide.WEST)){
				//Switch側が右、VM側が左の時
				if(sgPoint.x < vmPoint.x){
					midX = (sgPoint.x + vmPoint.x)/2
					_points.push(new Point(midX, sgPoint.y))
					_points.push(new Point(midX, vmPoint.y))
				}else{
					targetRight = sgPoint.x + offset;
					targetLeft = vmPoint.x - offset;
					if(isHorisontallyOverlaped){
						midY = (sgPoint.y + vmPoint.y)/2
						if(sgCenterPoint.y < vmCenterPoint.y){
							if(midY-(sgRect.top-offset) < (vmRect.bottom+offset)-midY){
								targetY = Math.min(sgRect.top, vmRect.top) - offset;
								targetLeft = Math.min(vmPoint.x - offset, sgRect.left - offset);
							}else{
								targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
								targetRight = Math.max(sgPoint.x + offset, vmRect.right + offset);
							}
						}else{
							if(midY-(vmRect.top-offset) < (sgRect.bottom+offset)-midY){
								targetY = Math.min(sgRect.top, vmRect.top) - offset;
								targetRight = Math.max(sgPoint.x + offset, vmRect.right + offset);
							}else{
								targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
								targetLeft = Math.min(vmPoint.x - offset, sgRect.left - offset);
							}
						}
					}else{
						targetY = sgCenterPoint.y < vmCenterPoint.y ? (sgRect.bottom + vmRect.top)/2 : (vmRect.bottom + sgRect.top)/2;
					}
					_points.push(new Point(targetRight, sgPoint.y));
					_points.push(new Point(targetRight, targetY));
					_points.push(new Point(targetLeft, targetY));
					_points.push(new Point(targetLeft, vmPoint.y));
				}
			}else if(sgSide.equals(VaSide.WEST) && vmSide.equals(VaSide.EAST)){
				//Switch側が左、VM側が右の時
				if(sgPoint.x > vmPoint.x){
					midX = (sgPoint.x + vmPoint.x)/2
					_points.push(new Point(midX, sgPoint.y))
					_points.push(new Point(midX, vmPoint.y))
				}else{
					targetRight = vmPoint.x + offset;
					targetLeft = sgPoint.x - offset;
					if(isHorisontallyOverlaped){
						midY = (sgPoint.y + vmPoint.y)/2
						if(sgCenterPoint.y < vmCenterPoint.y){
							if(midY-(sgRect.top-offset) < (vmRect.bottom+offset)-midY){
								targetY = Math.min(sgRect.top, vmRect.top) - offset;
								targetRight = Math.max(vmPoint.x + offset, sgRect.right + offset);
							}else{
								targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
								targetLeft = Math.min(sgPoint.x - offset, vmRect.left - offset);
							}
						}else{
							if(midY-(vmRect.top-offset) < (sgRect.bottom+offset)-midY){
								targetY = Math.min(sgRect.top, vmRect.top) - offset;
								targetLeft = Math.min(sgPoint.x - offset, vmRect.left - offset);
							}else{
								targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
								targetRight = Math.max(vmPoint.x + offset, sgRect.right + offset);
							}
						}
					}else{
						targetY = sgCenterPoint.y < vmCenterPoint.y ? (sgRect.bottom + vmRect.top)/2 : (vmRect.bottom + sgRect.top)/2;
					}
					_points.push(new Point(targetLeft, sgPoint.y));
					_points.push(new Point(targetLeft, targetY));
					_points.push(new Point(targetRight, targetY));
					_points.push(new Point(targetRight, vmPoint.y));
				}
			}else if(sgSide.equals(VaSide.SOUTH)&& vmSide.equals(VaSide.NORTH)){
				//Switch側が下、VM側が左の上
				if(sgPoint.y < vmPoint.y){
					midY = (sgPoint.y + vmPoint.y)/2
					_points.push(new Point(sgPoint.x, midY));
					_points.push(new Point(vmPoint.x, midY));
				}else{
					targetTop = vmPoint.y - offset;
					targetBottom = sgPoint.y + offset;
					if(isVerticallyOverlaped){
						midX = (sgPoint.x + vmPoint.x)/2
						if(sgCenterPoint.x < vmCenterPoint.x){
							if(midX-(sgRect.left-offset) < (vmRect.right+offset)-midX){
								targetX = Math.min(sgRect.left, vmRect.left) - offset;
								targetTop = Math.min(vmPoint.y - offset, sgRect.top - offset);
							}else{
								targetX = Math.max(sgRect.right, vmRect.right) + offset;
								targetBottom = Math.max(sgPoint.y + offset, vmRect.bottom + offset);
							}
						}else{
							if(midX-(vmRect.left-offset) < (sgRect.right+offset)-midX){
								targetX = Math.min(sgRect.left, vmRect.left) - offset;
								targetBottom = Math.max(sgPoint.y + offset, vmRect.bottom + offset);
							}else{
								targetX = Math.max(sgRect.right, vmRect.right) + offset;
								targetTop = Math.min(vmPoint.y - offset, sgRect.top - offset);
							}
						}
					}else{
						targetX = sgCenterPoint.x < vmCenterPoint.x ? (sgRect.right + vmRect.left)/2 : (vmRect.right + sgRect.left)/2;
					}
					_points.push(new Point(sgPoint.x, targetBottom));
					_points.push(new Point(targetX, targetBottom));
					_points.push(new Point(targetX, targetTop));
					_points.push(new Point(vmPoint.x, targetTop));
				}

			}else if(sgSide.equals(VaSide.NORTH) && vmSide.equals(VaSide.SOUTH)){
				//Switch側が上、VM側が下の時
				if(sgPoint.y > vmPoint.y){
					midY = (sgPoint.y + vmPoint.y)/2
					_points.push(new Point(sgPoint.x, midY));
					_points.push(new Point(vmPoint.x, midY));
				}else{
					targetTop = sgPoint.y - offset;
					targetBottom = vmPoint.y + offset;
					if(isVerticallyOverlaped){
						midX = (sgPoint.x + vmPoint.x)/2
						if(sgCenterPoint.x < vmCenterPoint.x){
							if(midX-(sgRect.left-offset) < (vmRect.right+offset)-midX){
								targetX = Math.min(sgRect.left, vmRect.left) - offset;
								targetTop = Math.min(vmPoint.y - offset, sgRect.top - offset);
							}else{
								targetX = Math.max(sgRect.right, vmRect.right) + offset;
								targetBottom = Math.max(sgPoint.y + offset, vmRect.bottom + offset);
							}
						}else{
							if(midX-(vmRect.left-offset) < (sgRect.right+offset)-midX){
								targetX = Math.min(sgRect.left, vmRect.left) - offset;
								targetBottom = Math.max(sgPoint.y + offset, vmRect.bottom + offset);
							}else{
								targetX = Math.max(sgRect.right, vmRect.right) + offset;
								targetTop = Math.min(vmPoint.y - offset, sgRect.top - offset);
							}
						}
					}else{
						targetX = sgCenterPoint.x < vmCenterPoint.x ? (sgRect.right + vmRect.left)/2 : (vmRect.right + sgRect.left)/2;
					}
					_points.push(new Point(sgPoint.x, targetTop));
					_points.push(new Point(targetX, targetTop));
					_points.push(new Point(targetX, targetBottom));
					_points.push(new Point(vmPoint.x, targetBottom));
				}
			}else if(sgSide.equals(VaSide.NORTH)&& vmSide.equals(VaSide.NORTH)){
				//Switch側が上、VM側が上の時
				if(sgPoint.y < vmPoint.y){
					targetY = Math.min((sgRect.bottom + vmRect.top)/2, vmPoint.y-offset);
					if(overlapCheck(sgRect.left-offset, sgRect.right+offset, vmPoint.x)){
						targetX = sgPoint.x < vmPoint.x ? sgRect.right+offset : sgRect.left-offset;
						_points.push(new Point(sgPoint.x, sgPoint.y-offset));
						_points.push(new Point(targetX, sgPoint.y-offset));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x, targetY));
					}else{
						_points.push(new Point(sgPoint.x, sgPoint.y-offset));
						_points.push(new Point(vmPoint.x, sgPoint.y-offset));
					}
				}else{
					targetY = Math.min((vmRect.bottom + sgRect.top)/2, sgPoint.y-offset);
					if(overlapCheck(vmRect.left-offset, vmRect.right+offset, sgPoint.x)){
						targetX = sgPoint.x > vmPoint.x ? vmRect.right+offset : vmRect.left-offset;
						_points.push(new Point(sgPoint.x, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y-offset));
						_points.push(new Point(vmPoint.x, vmPoint.y-offset));
					}else{
						_points.push(new Point(sgPoint.x, vmPoint.y-offset));
						_points.push(new Point(vmPoint.x, vmPoint.y-offset));
					}
				}
			}else if(sgSide.equals(VaSide.SOUTH) && vmSide.equals(VaSide.SOUTH)){
				//Switch側が下、VM側が下の時
				if(sgPoint.y > vmPoint.y){
					targetY = Math.max((vmRect.bottom + sgRect.top)/2, vmPoint.y+offset);
					if(overlapCheck(sgRect.left-offset, sgRect.right+offset, vmPoint.x)){
						targetX = sgPoint.x < vmPoint.x ? sgRect.right+offset : sgRect.left-offset;
						_points.push(new Point(sgPoint.x, sgPoint.y+offset));
						_points.push(new Point(targetX, sgPoint.y+offset));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x, targetY));
					}else{
						_points.push(new Point(sgPoint.x, sgPoint.y+offset));
						_points.push(new Point(vmPoint.x, sgPoint.y+offset));
					}
				}else{
					targetY = Math.max((sgRect.bottom + vmRect.top)/2, sgPoint.y+offset);
					if(overlapCheck(vmRect.left-offset, vmRect.right+offset, sgPoint.x)){
						targetX = sgPoint.x > vmPoint.x ? vmRect.right+offset : vmRect.left-offset;
						_points.push(new Point(sgPoint.x, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y+offset));
						_points.push(new Point(vmPoint.x, vmPoint.y+offset));
					}else{
						_points.push(new Point(sgPoint.x, vmPoint.y+offset));
						_points.push(new Point(vmPoint.x, vmPoint.y+offset));
					}
				}
			}else if(sgSide.equals(VaSide.WEST) && vmSide.equals(VaSide.WEST)){
				//Switch側が左、VM側が左の時
				if(sgPoint.x < vmPoint.x){
					targetX = Math.min((sgRect.right + vmRect.left)/2, vmPoint.x-offset);
					if(overlapCheck(sgRect.top-offset, sgRect.bottom+offset, vmPoint.y)){
						targetY = sgPoint.y < vmPoint.y ? sgRect.bottom+offset : sgRect.top-offset;
						_points.push(new Point(sgPoint.x-offset, sgPoint.y));
						_points.push(new Point(sgPoint.x-offset, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y));
					}else{
						_points.push(new Point(sgPoint.x-offset, sgPoint.y));
						_points.push(new Point(sgPoint.x-offset, vmPoint.y));
					}
				}else{
					targetX = Math.min((vmRect.right + sgRect.left)/2, sgPoint.x-offset);
					if(overlapCheck(vmRect.top-offset, vmRect.bottom+offset, sgPoint.y)){
						targetY = sgPoint.y > vmPoint.y ? vmRect.bottom+offset : vmRect.top-offset;
						_points.push(new Point(targetX, sgPoint.y));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x-offset, targetY));
						_points.push(new Point(vmPoint.x-offset, vmPoint.y));
					}else{
						_points.push(new Point(vmPoint.x-offset, sgPoint.y));
						_points.push(new Point(vmPoint.x-offset, vmPoint.y));
					}
				}
			}else if(sgSide.equals(VaSide.EAST) && vmSide.equals(VaSide.EAST)){
				//Switch側が右、VM側が右の時
				if(sgPoint.x > vmPoint.x){
					targetX = Math.max((vmRect.right + sgRect.left)/2, vmPoint.x+offset);
					if(overlapCheck(sgRect.top-offset, sgRect.bottom+offset, vmPoint.y)){
						targetY = sgPoint.y < vmPoint.y ? sgRect.bottom+offset : sgRect.top-offset;
						_points.push(new Point(sgPoint.x+offset, sgPoint.y));
						_points.push(new Point(sgPoint.x+offset, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y));
					}else{
						_points.push(new Point(sgPoint.x+offset, sgPoint.y));
						_points.push(new Point(sgPoint.x+offset, vmPoint.y));
					}
				}else{
					targetX = Math.max((sgRect.right + vmRect.left)/2, sgPoint.x+offset);
					if(overlapCheck(vmRect.top-offset, vmRect.bottom+offset, sgPoint.y)){
						targetY = sgPoint.y > vmPoint.y ? vmRect.bottom+offset : vmRect.top-offset;
						_points.push(new Point(targetX, sgPoint.y));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x+offset, targetY));
						_points.push(new Point(vmPoint.x+offset, vmPoint.y));
					}else{
						_points.push(new Point(vmPoint.x+offset, sgPoint.y));
						_points.push(new Point(vmPoint.x+offset, vmPoint.y));
					}
				}
			}else if(sgSide.equals(VaSide.EAST) && vmSide.equals(VaSide.NORTH)){
				//Switch側が右、VM側が上の時
				if(sgRect.right+offset > vmPoint.x){
					if(vmRect.top-offset < sgRect.bottom+offset){
						targetX = Math.max(sgRect.right, vmRect.right) + offset;
						targetY = Math.min(sgRect.top, vmRect.top) - offset;
					}else{
						targetX = sgRect.right + offset;
						targetY = (sgRect.bottom + vmRect.top)/2;
					}
					_points.push(new Point(targetX, sgPoint.y));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(vmPoint.x, targetY));
				}else{
					if(sgPoint.y < vmPoint.y - offset){
						_points.push(new Point(vmPoint.x, sgPoint.y));
					}else{
						if(vmRect.left-offset < sgRect.right+offset){
							targetX = Math.max(sgRect.right, vmRect.right) + offset;
							targetY = Math.min(sgRect.top, vmRect.top) - offset;
						}else{
							targetX = (sgRect.right + vmRect.left)/2;
							targetY = vmPoint.y - offset;
						}
						_points.push(new Point(targetX, sgPoint.y));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x, targetY));
					}
				}
			}else if(sgSide.equals(VaSide.EAST) && vmSide.equals(VaSide.SOUTH)){
				//Switch側が右、VM側が下の時
				if(sgRect.right+offset > vmPoint.x){
					if(vmRect.bottom+offset > sgRect.top-offset){
						targetX = Math.max(sgRect.right, vmRect.right) + offset;
						targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
					}else{
						targetX = sgRect.right + offset;
						targetY = (sgRect.top + vmRect.bottom)/2;
					}
					_points.push(new Point(targetX, sgPoint.y));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(vmPoint.x, targetY));
				}else{
					if(sgPoint.y > vmPoint.y + offset){
						_points.push(new Point(vmPoint.x, sgPoint.y));
					}else{
						if(vmRect.left-offset < sgRect.right+offset){
							targetX = Math.max(sgRect.right, vmRect.right) + offset;
							targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
						}else{
							targetX = (sgRect.right + vmRect.left)/2;
							targetY = vmPoint.y + offset;
						}
						_points.push(new Point(targetX, sgPoint.y));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x, targetY));
					}
				}
			}else if(sgSide.equals(VaSide.WEST) && vmSide.equals(VaSide.NORTH)){
				//Switch側が左、VM側が上の時
				if(sgRect.left-offset < vmPoint.x){
					if(vmRect.top-offset < sgRect.bottom+offset){
						targetX = Math.min(sgRect.left, vmRect.left) - offset;
						targetY = Math.min(sgRect.top, vmRect.top) - offset;
					}else{
						targetX = sgRect.left - offset;
						targetY = (sgRect.bottom + vmRect.top)/2;
					}
					_points.push(new Point(targetX, sgPoint.y));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(vmPoint.x, targetY));
				}else{
					if(sgPoint.y < vmPoint.y - offset){
						_points.push(new Point(vmPoint.x, sgPoint.y));
					}else{
						if(vmRect.right+offset > sgRect.left-offset){
							targetX = Math.min(sgRect.left, vmRect.left) - offset;
							targetY = Math.min(sgRect.top, vmRect.top) - offset;
						}else{
							targetX = (sgRect.left + vmRect.right)/2;
							targetY = vmPoint.y - offset;
						}
						_points.push(new Point(targetX, sgPoint.y));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x, targetY));
					}
				}
			}else if(sgSide.equals(VaSide.WEST) && vmSide.equals(VaSide.SOUTH)){
				//Switch側が左、VM側が下の時
				if(sgRect.left-offset < vmPoint.x){
					if(vmRect.bottom+offset > sgRect.top-offset){
						targetX = Math.min(sgRect.left, vmRect.left) - offset;
						targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
					}else{
						targetX = sgRect.left - offset;
						targetY = (sgRect.top + vmRect.bottom)/2;
					}
					_points.push(new Point(targetX, sgPoint.y));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(vmPoint.x, targetY));
				}else{
					if(sgPoint.y > vmPoint.y + offset){
						_points.push(new Point(vmPoint.x, sgPoint.y));
					}else{
						if(vmRect.right+offset > sgRect.left-offset){
							targetX = Math.min(sgRect.left, vmRect.left) - offset;
							targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
						}else{
							targetX = (sgRect.left + vmRect.right)/2;
							targetY = vmPoint.y + offset;
						}
						_points.push(new Point(targetX, sgPoint.y));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(vmPoint.x, targetY));
					}
				}
			}else if(sgSide.equals(VaSide.NORTH) && vmSide.equals(VaSide.EAST)){
				//Switch側が上、VM側が右の時
				if(vmRect.right+offset > sgPoint.x){
					if(sgRect.top-offset < vmRect.bottom+offset){
						targetX = Math.max(sgRect.right, vmRect.right) + offset;
						targetY = Math.min(sgRect.top, vmRect.top) - offset;
					}else{
						targetX = vmRect.right + offset;
						targetY = (vmRect.bottom + sgRect.top)/2;
					}
					_points.push(new Point(sgPoint.x, targetY));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(targetX, vmPoint.y));
				}else{
					if(vmPoint.y < sgPoint.y - offset){
						_points.push(new Point(sgPoint.x, vmPoint.y));
					}else{
						if(sgRect.left-offset < vmRect.right+offset){
							targetX = Math.max(sgRect.right, vmRect.right) + offset;
							targetY = Math.min(sgRect.top, vmRect.top) - offset;
						}else{
							targetX = (vmRect.right + sgRect.left)/2;
							targetY = sgPoint.y - offset;
						}
						_points.push(new Point(sgPoint.x, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y));
					}
				}
			}else if(sgSide.equals(VaSide.SOUTH) && vmSide.equals(VaSide.EAST)){
				//Switch側が下、VM側が右の時
				if(vmRect.right+offset > sgPoint.x){
					if(sgRect.bottom+offset > vmRect.top-offset){
						targetX = Math.max(sgRect.right, vmRect.right) + offset;
						targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
					}else{
						targetX = vmRect.right + offset;
						targetY = (vmRect.top + sgRect.bottom)/2;
					}
					_points.push(new Point(sgPoint.x, targetY));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(targetX, vmPoint.y));
				}else{
					if(vmPoint.y > sgPoint.y + offset){
						_points.push(new Point(sgPoint.x, vmPoint.y));
					}else{
						if(sgRect.left-offset < vmRect.right+offset){
							targetX = Math.max(sgRect.right, vmRect.right) + offset;
							targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
						}else{
							targetX = (vmRect.right + sgRect.left)/2;
							targetY = sgPoint.y + offset;
						}
						_points.push(new Point(sgPoint.x, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y));
					}
				}
			}else if(sgSide.equals(VaSide.NORTH) && vmSide.equals(VaSide.WEST)){
				//Switch側が上、VM側が左の時
				if(vmRect.left-offset < sgPoint.x){
					if(sgRect.top-offset < vmRect.bottom+offset){
						targetX = Math.min(sgRect.left, vmRect.left) - offset;
						targetY = Math.min(sgRect.top, vmRect.top) - offset;
					}else{
						targetX = vmRect.left - offset;
						targetY = (vmRect.bottom + sgRect.top)/2;
					}
					_points.push(new Point(sgPoint.x, targetY));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(targetX, vmPoint.y));
				}else{
					if(vmPoint.y < sgPoint.y - offset){
						_points.push(new Point(sgPoint.x, vmPoint.y));
					}else{
						if(sgRect.right+offset > vmRect.left-offset){
							targetX = Math.min(sgRect.left, vmRect.left) - offset;
							targetY = Math.min(sgRect.top, vmRect.top) - offset;
						}else{
							targetX = (vmRect.left + sgRect.right)/2;
							targetY = sgPoint.y - offset;
						}
						_points.push(new Point(sgPoint.x, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y));
					}
				}
			}else if(sgSide.equals(VaSide.SOUTH) && vmSide.equals(VaSide.WEST)){
				//Switch側が下、VM側が左の時
				if(vmRect.left-offset < sgPoint.x){
					if(sgRect.bottom+offset > vmRect.top-offset){
						targetX = Math.min(sgRect.left, vmRect.left) - offset;
						targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
					}else{
						targetX = vmRect.left - offset;
						targetY = (vmRect.top + sgRect.bottom)/2;
					}
					_points.push(new Point(sgPoint.x, targetY));
					_points.push(new Point(targetX, targetY));
					_points.push(new Point(targetX, vmPoint.y));
				}else{
					if(vmPoint.y > sgPoint.y + offset){
						_points.push(new Point(sgPoint.x, vmPoint.y));
					}else{
						if(sgRect.right+offset > vmRect.left-offset){
							targetX = Math.min(sgRect.left, vmRect.left) - offset;
							targetY = Math.max(sgRect.bottom, vmRect.bottom) + offset;
						}else{
							targetX = (vmRect.left + sgRect.right)/2;
							targetY = sgPoint.y + offset;
						}
						_points.push(new Point(sgPoint.x, targetY));
						_points.push(new Point(targetX, targetY));
						_points.push(new Point(targetX, vmPoint.y));
					}
				}
			}
			_points.push(vmPoint);
		}

		[Bindable("changePoints")]
		public function get halfPoints():Array
		{
			if(_halfPoints == null)
				updateHalfPoints();
			return _halfPoints;
		}
		private var _halfPoints:Array;

		protected function updateHalfPoints():void
		{
			_halfPoints = getDividedPoints(points, 0.5);
			dispatchEvent(new Event(CHANGE_HALF_POINTS));
		}

		[Bindable("changeHalfPoints")]
		public function get halfPoint():Point
		{
			if(halfPoints.length <= 1)
			{
				return new Point((sgPoint.x + vmPoint.x)/2, (sgPoint.y + vmPoint.y)/2)
			}
			else
			{
				return halfPoints[halfPoints.length-1];
			}
		}


		/** 開始ノード と 終了ノードが重なっているか？ */
		protected function get isOverlaped():Boolean
		{
			if(sgRect && vmRect){
				var sr:Rectangle = sgRect.clone();
				var er:Rectangle = vmRect.clone();
				sr.inflate(offset, offset);
				er.inflate(offset, offset);
				if(sr.intersects(er)) return true;
				if(sr.containsRect(er)) return true;
				if(er.containsRect(sr)) return true;
				return false;
			}else{
				return false;
			}
		}

		/** 開始ノード と 終了ノードが水平方向で重なっているか？ */
		protected function get isHorisontallyOverlaped():Boolean
		{
			if(sgRect && vmRect){
				var sr:Rectangle = sgRect.clone();
				var er:Rectangle = vmRect.clone();
				sr.inflate(offset, offset);
				er.inflate(offset, offset);
				if(overlapCheck(sr.top, sr.bottom, er.top)) return true;
				if(overlapCheck(sr.top, sr.bottom, er.bottom)) return true;
				if(overlapCheck(er.top, er.bottom, sr.top)) return true;
				if(overlapCheck(er.top, er.bottom, sr.bottom)) return true;
				return false;
			}else{
				return false;
			}
		}

		/** 開始ノード と 終了ノードが垂直方向で重なっているか？ */
		protected function get isVerticallyOverlaped():Boolean
		{
			if(sgRect && vmRect){
				var sr:Rectangle = sgRect.clone();
				var er:Rectangle = vmRect.clone();
				sr.inflate(offset, offset);
				er.inflate(offset, offset);
				if(overlapCheck(sr.right, sr.left, er.right)) return true;
				if(overlapCheck(sr.right, sr.left, er.left)) return true;
				if(overlapCheck(er.right, er.left, sr.right)) return true;
				if(overlapCheck(er.right, er.left, sr.left)) return true;
				return false;
			}else{
				return false;
			}
		}

		/** 重なりを確認する（updatvm_pointsで使用） */
		protected function overlapCheck(s:Number, e:Number, t:Number):Boolean
		{
			return (t-s)*(t-e) < 0;
		}

		public function clone():VaLink
		{
			var result:VaLink = new VaLink();
			CloneUtil.updateProperties(result, this, "jp.co.ntts.vhut.comp.va.domain.VaLink");
			return result;
		}
	}
}