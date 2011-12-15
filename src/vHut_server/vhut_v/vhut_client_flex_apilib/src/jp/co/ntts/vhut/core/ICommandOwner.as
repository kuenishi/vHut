package jp.co.ntts.vhut.core
{
	import mx.rpc.events.FaultEvent;

	/**
	 * <p>コマンドの発信源が持つインターフェース。
	 *
	 * @internal
	 * $Date$
	 * $Revision$
	 * $Author$
	 *
	 * author NTT Software Corporation.
	 * version 1.0.0
	 */
	public interface ICommandOwner
	{
		function result(command:BaseCommand, object:Object):Boolean;
		function error(command:BaseCommand, fault:FaultEvent):Boolean;
	}
}