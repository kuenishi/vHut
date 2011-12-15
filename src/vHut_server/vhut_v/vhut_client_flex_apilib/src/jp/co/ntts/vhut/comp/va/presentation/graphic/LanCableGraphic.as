/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.graphic
{
	import flash.display.Graphics;
	import flash.display.GraphicsStroke;
	import flash.display.IGraphicsData;
	import flash.geom.Point;
	import flash.geom.Rectangle;

	import spark.primitives.Line;
	import spark.primitives.supportClasses.StrokedElement;

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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class LanCableGraphic extends StrokedElement
	{
		public function LanCableGraphic()
		{
			super();
			Line
		}


		[Inspectable(category="General")]
		public function set points(value:Array):void
		{
			if(value != _points)
			{
				_points = value;
				invalidateSize();
				invalidateDisplayList();
			}
		}
		public function get points():Array
		{
			return _points;
		}
		private var _points:Array = new Array();

		//--------------------------------------------------------------------------
		//
		//  Overridden methods
		//
		//--------------------------------------------------------------------------

		/**
		 *  @inheritDoc
		 *
		 *  @langversion 3.0
		 *  @playerversion Flash 10
		 *  @playerversion AIR 1.5
		 *  @productversion Flex 4
		 */
		override protected function canSkipMeasurement():Boolean
		{
			// Since our measure() is quick, we prefer to call it always instead of
			// trying to detect cases where measuredX and measuredY would change.
			return false;
		}

		/**
		 *  @inheritDoc
		 *
		 *  @langversion 3.0
		 *  @playerversion Flash 10
		 *  @playerversion AIR 1.5
		 *  @productversion Flex 4
		 */
		override protected function measure():void
		{
			var point:Point;
			var top:Number = 0;
			var bottom:Number = 0;
			var left:Number = 0;
			var right:Number = 0;

			if(points && points.length > 0)
			{
				point = points[0]
				top = point.y;
				bottom = point.y;
				left = point.x;
				right = point.x;
			}

			if(points && points.length > 1)
			{
				for (var i:uint=1; points.length > i; i++)
				{
					point = points[0];
					top = Math.min(top, point.y);
					bottom = Math.max(top, point.y);
					left= Math.min(left, point.x);
					right = Math.max(right, point.x);
				}
			}

			measuredWidth = Math.abs(right - left);
			measuredHeight = Math.abs(bottom - top);
			measuredX = left;
			measuredY = top;

			measuredWidth = 0;
			measuredHeight = 0;
			measuredX = 0;
			measuredY = 0;
		}

		/**
		 * @private
		 */
		override protected function beginDraw(g:Graphics):void
		{
			var graphicsStroke:GraphicsStroke;
			if (stroke)
				graphicsStroke = GraphicsStroke(stroke.createGraphicsStroke(new
					Rectangle(drawX + measuredX, drawY + measuredY,
						Math.max(width, stroke.weight), Math.max(height, stroke.weight)),
					new Point(drawX + measuredX, drawY + measuredY)));

			// If the stroke returns a valid graphicsStroke object which is the
			// Drawing API-2 drawing commands to render this stroke, use that
			// to draw the stroke to screen
			if (graphicsStroke)
				g.drawGraphicsData(new <IGraphicsData>[graphicsStroke]);
			else
				super.beginDraw(g);
		}

		/**
		 *  @inheritDoc
		 *
		 *  @langversion 3.0
		 *  @playerversion Flash 10
		 *  @playerversion AIR 1.5
		 *  @productversion Flex 4
		 */
		override protected function draw(g:Graphics):void
		{
			if(points && points.length>0)
			{
				var point:Point = points[0] as Point;
				g.moveTo(point.x+drawX, point.y+drawY);
				for(var i:uint=1; i<points.length; i++){
					point = points[i];
					g.lineTo(point.x+drawX, point.y+drawY);
				}
			}
		}
	}
}