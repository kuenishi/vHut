/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.comp.va.presentation.connector
{
	import flash.geom.Point;

	import jp.co.ntts.vhut.comp.va.domain.Topology;
	import jp.co.ntts.vhut.comp.va.domain.VaLink;

	import mx.collections.IList;
	import mx.core.ILayoutElement;

	import spark.components.DataGroup;
	import spark.components.supportClasses.GroupBase;
	import spark.components.supportClasses.Skin;
	import spark.layouts.supportClasses.LayoutBase;

	/**
	 * LANケーブルのコネクタを配置します.
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
	 * $Date: 2010-12-02 12:13:44 +0900 (木, 02 12 2010) $
	 * $Revision: 618 $
	 * $Author: NTT Software Corporation. $
	 */
	public class ConnectorLayout extends LayoutBase
	{
		static public const TYPE_VM:String = "vm";
		static public const TYPE_SG:String = "sg";

		public function ConnectorLayout()
		{
			super();
		}

		[Bindable]
		[Inspectable(category="Other", defaultValue="vm", enumeration="vm,sg")]
		public function set type(value:String):void
		{
			if(_type != value)
			{
				_type = value;
			}
		}
		public function get type():String
		{
			return _type;
		}
		private var _type:String = TYPE_VM;

		override public function updateDisplayList(width:Number, height:Number):void
		{
			super.updateDisplayList(width, height);

			var container:DataGroup = target as DataGroup;

			for(var i:int=0; i<target.numElements; i++)
			{
				var element:ILayoutElement = target.getElementAt(i);
				var xpos:Number = 0;
				var ypos:Number = 0;

				if(container)
				{
					var link:VaLink = container.dataProvider.getItemAt(i) as VaLink;
					if(link)
					{
						var order:Number = 0;
						switch(type)
						{
							case TYPE_VM:
								order = link.vmOrder;
								break;
							case TYPE_SG:
								order = link.sgOrder;
								break;
						}
//						xpos = width * order - element.getLayoutBoundsWidth()/2;
						xpos = width * order;
					}
				}

				element.setLayoutBoundsSize(NaN, NaN);
				element.setLayoutBoundsPosition(xpos, ypos);
			}
		}
	}
}