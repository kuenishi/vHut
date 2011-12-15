/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/

package jp.co.ntts.vhut.core
{
	import jp.co.ntts.vhut.dialog.presentation.VhutAlert;
	import jp.co.ntts.vhut.entity.ResourceType;
	import jp.co.ntts.vhut.exception.AigDeployedException;
	import jp.co.ntts.vhut.exception.AigHasNoRappException;
	import jp.co.ntts.vhut.exception.ApplicationInstanceStatusException;
	import jp.co.ntts.vhut.exception.ApplicationStatusException;
	import jp.co.ntts.vhut.exception.CloudReservationException;
	import jp.co.ntts.vhut.exception.CloudReservationPeriodException;
	import jp.co.ntts.vhut.exception.CloudResourceException;
	import jp.co.ntts.vhut.exception.CloudUserNotFoundException;
	import jp.co.ntts.vhut.exception.NoAvailableReservationException;
	import jp.co.ntts.vhut.exception.ReleasedApplicationStatusException;
	import jp.co.ntts.vhut.log.VhutLog;
	import jp.co.ntts.vhut.log.VhutLogLogger;

	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;

	/**
	 * すべてのコマンドクラスの基本となるクラス.
	 * <p>SPICEのDynamicCommandのライフタイムがstateful=falseであることを前提としています。
	 * ログ出力用のトランザクションIDを生成します。</p>
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
	public class BaseCommand
	{
		private static const TRANSACTION_ID_BASE:String = new Date().getTime().toString();

		private static var nextTransactionId:Number = 1;

		protected var _logger:VhutLogLogger;

		private var _javaClazz:String;

		private var _component:String;

		private var _module:String;

		private var _msgcodeRequest:String;

		private var _msgcodeResponse:String;

		private var _msgcodeResponseError:String;

		private var _owner:ICommandOwner;

		private var _resourceManager:IResourceManager

		public function BaseCommand(component:String, clazz:String, javaClazz:String)
		{
			_logger = VhutLog.getLogger(clazz);
			_module = clazz.split(".").pop();
			_javaClazz = javaClazz;
			_component = component;
			_msgcodeRequest = "I"+_component+"0011";
			_msgcodeResponse = "I"+_component+"0012";
			_msgcodeResponseError = "E"+_component+"0012";
			_transactionId = TRANSACTION_ID_BASE +"/"+ (nextTransactionId++).toString();
			_resourceManager = ResourceManager.getInstance();
		}

		[MessageDispatcher]
		public var dispatcher:Function;

		[Inject]
		public var commandWatcher:CommandWatcher;

		private var _transactionId:String;

		public function get transactionId():String
		{
			return _transactionId;
		}

		/**
		 * Spice Frameworkからメソッド実行時に実行されます.
		 * @param event
		 * @return
		 */
		protected function _request(params:Object = ""):void
		{
			_logger.log(_msgcodeRequest, _module, _javaClazz, _transactionId, params.toString());
			commandWatcher.tellExecute(this);
		}

		protected function set owner(value:ICommandOwner):void
		{
			_owner = value;
		}

		protected function get owner():ICommandOwner
		{
			return _owner;
		}

		/**
		 * Spice Frameworkからメソッド正常終了時に実行されます.
		 * @param event
		 * @return
		 */
		protected function _response(result:Object = ""):void
		{
			_logger.log(_msgcodeResponse, _module, _javaClazz, _transactionId, result.toString());
			commandWatcher.tellSuccess(this);
			if (_owner)
			{
				if(!_owner.result(this, result)) {
					return;
				}
			}
		}

		/**
		 * Spice Frameworkからメソッド異常終了時に実行されます.
		 * @param event
		 * @return
		 */
		protected function _error(fault:FaultEvent):void
		{
			_logger.log(_msgcodeResponseError, _module, _javaClazz, _transactionId, fault.message);
			commandWatcher.tellError(this);
			if (_owner)
			{
				if(!_owner.error(this, fault))
				{
					return;
				}
			}

			if (fault.fault.rootCause is CloudResourceException)
			{
				showAlert(createMessageWithCloudResourceException(fault.fault.rootCause as CloudResourceException));
			}
			else if (fault.fault.rootCause is CloudReservationException)
			{
				showAlert(createMessageWithCloudReservationException(fault.fault.rootCause as CloudReservationException));
			}
			else if (fault.fault.rootCause is CloudReservationPeriodException)
			{
				showAlert(createMessageWithCloudReservationPeriodException(fault.fault.rootCause as CloudReservationPeriodException));
			}
			else if (fault.fault.rootCause is ApplicationStatusException)
			{
				showAlert(createMessageWithApplicationStatusException(fault.fault.rootCause as ApplicationStatusException));
			}
			else if (fault.fault.rootCause is ApplicationInstanceStatusException)
			{
				showAlert(createMessageWithApplicationInstanceStatusException(fault.fault.rootCause as ApplicationInstanceStatusException));
			}
			else if (fault.fault.rootCause is ReleasedApplicationStatusException)
			{
				showAlert(createMessageWithReleasedApplicationStatusException(fault.fault.rootCause as ReleasedApplicationStatusException));
			}
			else if (fault.fault.rootCause is NoAvailableReservationException)
			{
				showAlert(createMessageWithNoAvailableReservationException(fault.fault.rootCause as NoAvailableReservationException));
			}
			else if (fault.fault.rootCause is AigHasNoRappException)
			{
				showAlert(createMessageWithAigHasNoRappException(fault.fault.rootCause as AigHasNoRappException));
			}
			else if (fault.fault.rootCause is AigDeployedException)
			{
				showAlert(createMessageWithAigDeployedException(fault.fault.rootCause as AigDeployedException));
			}
			else if (fault.fault.rootCause is CloudUserNotFoundException)
			{
				showAlert(createMessageWithCloudUserNotFoundException(fault.fault.rootCause as CloudUserNotFoundException));
			}
			else if (fault.fault.rootCause && fault.fault.rootCause.messageCode)
			{
				//その他の共通エラー処理
				var message:String = _resourceManager.getString('APIUI', 'alert.message.error');
				var messageCode:String = fault.fault.rootCause.messageCode;
				if (messageCode)
				{
					message += messageCode;
				}
				showError(message);
			}
		}

		protected function showAlert(message:String):void
		{
			VhutAlert.show(message
				, _resourceManager.getString('APIUI', 'alert.title.alert')
				, VhutAlert.LABELS_CLOSE
			);
		}

		protected function showError(message:String):void
		{
			VhutAlert.show(message
				, _resourceManager.getString('APIUI', 'alert.title.error')
				, VhutAlert.LABELS_CLOSE
			);
		}

		protected function createMessageWithCloudResourceException(exception:CloudResourceException):String
		{
			switch(exception.type.name)
			{
				case ResourceType.CLUSTER.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.resource.cluster');
				case ResourceType.STORAGE.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.resource.storage');
				case ResourceType.VLAN.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.resource.vlan');
				case ResourceType.PUBLIC_IP.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.resource.publicIp');
			}
			return null;
		}

		protected function createMessageWithCloudReservationException(exception:CloudReservationException):String
		{
			switch(exception.type.name)
			{
				case ResourceType.CLUSTER.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.reservation.cluster');
				case ResourceType.STORAGE.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.reservation.storage');
				case ResourceType.VLAN.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.reservation.vlan');
				case ResourceType.PUBLIC_IP.name:
					return _resourceManager.getString('APIUI', 'alert.message.cloud.reservation.publicIp');
			}
			return null;
		}

		protected function createMessageWithCloudReservationPeriodException(exception:CloudReservationPeriodException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.cloud.reservation.period');
		}

		protected function createMessageWithApplicationStatusException(exception:ApplicationStatusException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.status');
		}

		protected function createMessageWithApplicationInstanceStatusException(exception:ApplicationInstanceStatusException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.status');
		}

		protected function createMessageWithReleasedApplicationStatusException(exception:ReleasedApplicationStatusException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.status');
		}

		protected function createMessageWithNoAvailableReservationException(exception:NoAvailableReservationException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.cloud.reservation.notAvailable');
		}

		protected function createMessageWithAigHasNoRappException(exception:AigHasNoRappException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.aig.noRapp');
		}

		protected function createMessageWithAigDeployedException(exception:AigDeployedException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.aig.deployed');
		}

		protected function createMessageWithCloudUserNotFoundException(exception:CloudUserNotFoundException):String
		{
			return _resourceManager.getString('APIUI', 'alert.message.user.notFound');
		}


	}
}