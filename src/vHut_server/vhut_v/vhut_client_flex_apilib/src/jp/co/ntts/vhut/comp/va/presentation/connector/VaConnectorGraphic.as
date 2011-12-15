package jp.co.ntts.vhut.comp.va.presentation.connector
{

	import flash.display.Graphics;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.geom.Point;

	public class VaConnectorGraphic extends Sprite
	{
		public static const DIRECTION_NORTH:String = "north";
		public static const DIRECTION_EAST:String = "east";
		public static const DIRECTION_SOUTH:String = "south";
		public static const DIRECTION_WEST:String = "west";
		private static const TOTAL_WIDTH:uint = 12;
		private static const TOTAL_HEIGHT:uint = 11;
		private static const VOID_WIDTH:uint = 4;
		private static const VOID_HEIGHT:uint = 2;
		private static const LIGHT_ALPHA1:Number = 0.5;
		private static const LIGHT_ALPHA2:Number = 0.3;
		private static const SHADOW_ALPHA1:Number = 0.5;
		private static const SHADOW_ALPHA2:Number = 0.3;

		public function VaConnectorGraphic()
		{
			super();
			addEventListener(Event.ADDED_TO_STAGE, addedToStageHandler);
		}

		//////////////////////////////
		// getter setter
		private var _direction:String = DIRECTION_EAST;
		private var _color:uint = 0x252588;

		public function set direction(value:String):void
		{
			_direction = value;
			invalidateDisplay();
		}
		public function get direction():String { return _direction; }

		public function set color(value:uint):void
		{
			_color = value;
			invalidateDisplay();
		}
		public function get color():uint { return _color; }

		public function get connectorPosition():Point
		{
			var point:Point = new Point(x, y);
			switch(direction){
				case DIRECTION_EAST:
					point.x += TOTAL_WIDTH;
					break;
				case DIRECTION_WEST:
					point.x += -TOTAL_WIDTH;
					break;
				case DIRECTION_NORTH:
					point.y += -TOTAL_HEIGHT;
					break;
				case DIRECTION_SOUTH:
					point.y += TOTAL_HEIGHT;
					break;
			}
			return point;
		}
		// getter setter
		//////////////////////////////

		private function addedToStageHandler(event:Event):void
		{
			invalidateDisplay();
		}

		public function invalidateDisplay():void
		{
			addEventListener(Event.ENTER_FRAME, invalidateHandler);
		}

		private function invalidateHandler(event:Event):void
		{
			removeEventListener(Event.ENTER_FRAME, invalidateHandler);
			drawBackground();
		}

		private function drawBackground():void
		{
			var g:Graphics = graphics;
			g.clear();
			switch(direction){
				case DIRECTION_EAST:
					g.beginFill(color);
					g.drawRect(0, -TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH, TOTAL_HEIGHT);
					g.drawRect(TOTAL_WIDTH-VOID_WIDTH, (2*VOID_HEIGHT-TOTAL_HEIGHT)/2, VOID_WIDTH, TOTAL_HEIGHT-2*VOID_HEIGHT);
					g.endFill();
					g.beginFill(0xFFFFFF, LIGHT_ALPHA1);
					g.drawRect(0, -TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(TOTAL_WIDTH-VOID_WIDTH, (2*VOID_HEIGHT-TOTAL_HEIGHT)/2, VOID_WIDTH, 1);
					g.endFill();
					g.beginFill(0xFFFFFF, LIGHT_ALPHA2);
					g.drawRect(0, -TOTAL_HEIGHT/2+1, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(TOTAL_WIDTH-VOID_WIDTH, (2*VOID_HEIGHT-TOTAL_HEIGHT)/2+1, VOID_WIDTH, 1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA1);
					g.drawRect(0, TOTAL_HEIGHT/2-1, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(TOTAL_WIDTH-VOID_WIDTH, -(2*VOID_HEIGHT-TOTAL_HEIGHT)/2-1, VOID_WIDTH, 1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA2);
					g.drawRect(0, TOTAL_HEIGHT/2-2, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(TOTAL_WIDTH-VOID_WIDTH, -(2*VOID_HEIGHT-TOTAL_HEIGHT)/2-2, VOID_WIDTH, 1);
					g.endFill();
					break;
				case DIRECTION_WEST:
					g.beginFill(color);
					g.drawRect(-TOTAL_WIDTH+VOID_WIDTH, -TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH, TOTAL_HEIGHT);
					g.drawRect(-TOTAL_WIDTH, (2*VOID_HEIGHT-TOTAL_HEIGHT)/2, VOID_WIDTH, TOTAL_HEIGHT-2*VOID_HEIGHT);
					g.endFill();
					g.beginFill(0xFFFFFF, LIGHT_ALPHA1);
					g.drawRect(-TOTAL_WIDTH+VOID_WIDTH, -TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(-TOTAL_WIDTH, (2*VOID_HEIGHT-TOTAL_HEIGHT)/2, VOID_WIDTH, 1);
					g.endFill();
					g.beginFill(0xFFFFFF, LIGHT_ALPHA2);
					g.drawRect(-TOTAL_WIDTH+VOID_WIDTH, -TOTAL_HEIGHT/2+1, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(-TOTAL_WIDTH, (2*VOID_HEIGHT-TOTAL_HEIGHT)/2+1, VOID_WIDTH, 1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA1);
					g.drawRect(-TOTAL_WIDTH+VOID_WIDTH, TOTAL_HEIGHT/2-1, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(-TOTAL_WIDTH, -(2*VOID_HEIGHT-TOTAL_HEIGHT)/2-1, VOID_WIDTH, 1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA2);
					g.drawRect(-TOTAL_WIDTH+VOID_WIDTH, TOTAL_HEIGHT/2-2, TOTAL_WIDTH-VOID_WIDTH, 1);
					g.drawRect(-TOTAL_WIDTH, -(2*VOID_HEIGHT-TOTAL_HEIGHT)/2-2, VOID_WIDTH, 1);
					g.endFill();
					break;
				case DIRECTION_NORTH:
					g.beginFill(color);
					g.drawRect(-TOTAL_HEIGHT/2, VOID_WIDTH-TOTAL_WIDTH, TOTAL_HEIGHT, TOTAL_WIDTH-VOID_WIDTH);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2, -TOTAL_WIDTH, TOTAL_HEIGHT-2*VOID_HEIGHT, VOID_WIDTH);
					g.endFill();
					g.beginFill(0xFFFFFF, LIGHT_ALPHA2);
					g.drawRect(-TOTAL_HEIGHT/2+2, VOID_WIDTH-TOTAL_WIDTH, TOTAL_HEIGHT-4, 1);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2+1, -TOTAL_WIDTH, TOTAL_HEIGHT-2*VOID_HEIGHT-2, 1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA2);
					g.drawRect(-TOTAL_HEIGHT/2, VOID_WIDTH-TOTAL_WIDTH+1, 2, TOTAL_WIDTH-VOID_WIDTH-1);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2, -TOTAL_WIDTH+1, 1, VOID_WIDTH-1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA2);
					g.drawRect(TOTAL_HEIGHT/2-2, VOID_WIDTH-TOTAL_WIDTH+1, 2, TOTAL_WIDTH-VOID_WIDTH-1);
					g.drawRect(-VOID_HEIGHT+TOTAL_HEIGHT/2-1, -TOTAL_WIDTH+1, 1, VOID_WIDTH-1);
					g.endFill();
					break;
				case DIRECTION_SOUTH:
					g.beginFill(color);
					g.drawRect(-TOTAL_HEIGHT/2, 0, TOTAL_HEIGHT, TOTAL_WIDTH-VOID_WIDTH);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH, TOTAL_HEIGHT-2*VOID_HEIGHT, VOID_WIDTH);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA1);
					g.drawRect(-TOTAL_HEIGHT/2, 0, TOTAL_HEIGHT, 1);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH, TOTAL_HEIGHT-2*VOID_HEIGHT, 1);
					g.endFill();
					g.beginFill(0xFFFFFF, LIGHT_ALPHA2);
					g.drawRect(-TOTAL_HEIGHT/2+2, -VOID_WIDTH+TOTAL_WIDTH-1, TOTAL_HEIGHT-4, 1);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2+1, TOTAL_WIDTH-1, TOTAL_HEIGHT-2*VOID_HEIGHT-2, 1);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA2);
					g.drawRect(-TOTAL_HEIGHT/2, 1, 2, TOTAL_WIDTH-VOID_WIDTH-2);
					g.drawRect(VOID_HEIGHT-TOTAL_HEIGHT/2, TOTAL_WIDTH-VOID_WIDTH+1, 1, VOID_WIDTH-2);
					g.endFill();
					g.beginFill(0x0, SHADOW_ALPHA2);
					g.drawRect(TOTAL_HEIGHT/2-2, 1, 2, TOTAL_WIDTH-VOID_WIDTH-2);
					g.drawRect(-VOID_HEIGHT+TOTAL_HEIGHT/2-1, TOTAL_WIDTH-VOID_WIDTH+1, 1, VOID_WIDTH-2);
					g.endFill();
					break;
			}
		}

	}
}