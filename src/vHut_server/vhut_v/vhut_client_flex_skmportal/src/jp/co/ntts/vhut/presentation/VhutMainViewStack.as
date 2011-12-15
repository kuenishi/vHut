/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.presentation
{
	import flash.utils.Dictionary;

	import mx.collections.IList;
	import mx.containers.ViewStack;
	import mx.core.INavigatorContent;

	/**
	 * vHutポータルのメインの画面スタックです.
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
	public class VhutMainViewStack extends ViewStack
	{

		public function VhutMainViewStack()
		{
			super();
			updateChildren();
		}

		//----------------------------------------------
		// contents
		//----------------------------------------------
		public function set contents(value:IList):void
		{
			_contents = value;
			updateChildren();
		}
		public function get contents():IList
		{
			return _contents;
		}
		private var _contents:IList;

		//----------------------------------------------
		// rightsMap
		//----------------------------------------------
		public function set rightsMap(value:Dictionary):void
		{
			_rightsMap = value;
			updateChildren();
		}
		public function get rightsMap():Dictionary
		{
			return _rightsMap;
		}
		private var _rightsMap:Dictionary;

		//----------------------------------------------
		// children
		//----------------------------------------------
		protected function updateChildren():void
		{
			var index:uint;
			removeAllChildren();
			if (rightsMap)
			{
				for each (var content:VhutMainNavigatorContent in contents)
				{
//					index = getElementIndex(content);
					if(rightsMap[content.requiredRight])
					{
						addChild(content);
					}
				}
			}
		}
	}
}