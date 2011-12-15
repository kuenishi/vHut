/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.entity {

    [Bindable]
    [RemoteClass(alias="jp.co.ntts.vhut.entity.Term")]
	/**
	 * Term Entity Class.
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
    public class Term extends TermBase {

		public static function newTerm(startTime:Date, endTime:Date):Term
		{
			var term:Term = new Term();
			term.startTime = startTime;
			term.endTime = endTime;
			return term;
		}

		[SyncId]
		override public function get id():Number {
			return _id;
		}
		override public function set id(value:Number):void {
			_id = value;
		}

		public function clone():Term
		{
			var term:Term = new Term();
			term.id = id;
			term.startTime = startTime;
			term.endTime = endTime;
			term.application = application;
			term.applicationId = applicationId;
			term.reservationId = reservationId
			term.version = version;
			return term;
		}
    }
}