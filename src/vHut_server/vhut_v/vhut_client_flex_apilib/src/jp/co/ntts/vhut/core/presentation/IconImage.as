/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.core.presentation
{
	import jp.co.ntts.vhut.config.VhutConfig;
	
	import mx.controls.Image;
	
	import spark.filters.DropShadowFilter;
	
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
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class IconImage extends Image
	{
		public function IconImage()
		{
			super();
		}
		
		[Bindable("sourceChanged")]
		[Inspectable(category="General", defaultValue="", format="File")]
		
		/**
		 *  @private
		 */
		override public function set source(value:Object):void
		{
			defaultSource = value;
			super.source = value;
		}
		private var defaultSource:Object;
		
		/** url */
		public function set url(value:String):void
		{
			_url = value;
			_isUrlChanged = true;
			invalidateProperties();
		}
		public function get url():String
		{
			return _url;
		}
		private var _url:String;
		private var _isUrlChanged:Boolean = false;
		
		/** shadow */
		public function set shadow(value:Boolean):void
		{
			if(!value || value==_shadow) return;
			_shadow = value;
			_isShadowChanged = true;
			invalidateProperties();
		}
		public function get shadow():Boolean
		{
			return _shadow;
		}
		private var _shadow:Boolean = false;
		private var _isShadowChanged:Boolean = false;
		
		override protected function commitProperties():void
		{
			super.commitProperties();
			if(_isUrlChanged)
			{
				if(_url)
				{
					load(VhutConfig.IMAGE_ROOT +"/"+ _url);
				}
				else
				{
					load(defaultSource);
				}
				_isUrlChanged = false;
			}
			if(_isShadowChanged)
			{
				var array:Array = new Array();
				if(shadow)
					array.push(new DropShadowFilter(0, 0, 0, 0.5, 5, 5));
				filters = array;
			}
		}
	}
}