/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {
	import mx.collections.ArrayCollection;

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.SecurityGroupTemplate")]
	/**
	 * SecurityGroupTemplate Entity Class.
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
    public class SecurityGroupTemplate extends SecurityGroupTemplateBase {
		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		override public function set networkAdapterTemplateList(value:ArrayCollection):void
		{
			super.networkAdapterTemplateList = value;
			for each (var item:NetworkAdapterTemplate in value)
			{
				if(item.securityGroupTemplateId == id)
				{
					item.securityGroupTemplate = this;
				}
			}
		}
    }
}