/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.tmp.domain
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;

	import jp.co.ntts.vhut.entity.BaseTemplate;
	import jp.co.ntts.vhut.entity.Template;

	import mx.binding.utils.BindingUtils;
	import mx.binding.utils.ChangeWatcher;
	import mx.utils.ObjectUtil;

	import org.spicefactory.lib.reflect.types.Void;

	/**
	 * 編集中のベーステンプレートの管理クラス.
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
	public class EditingBaseTemplate extends EventDispatcher
	{
		/** ターゲットを変更しました */
		public static const CHANGE_TARGET:String = "changeTarget"

		public function EditingBaseTemplate(target:IEventDispatcher=null)
		{
			super(target);
		}

		[Inject]
		[Bindalbe]
		public var baseTemplates:BaseTemplates;

		[Inject]
		[Bindable]
		public var unregisteredTemplates:UnregisteredTemplates;

		/**
		 * ベーステンプレートを新規に作成します.
		 * <p> 新規に作成されたベーステンプレートはターゲットにセットされます.
		 * @return
		 */
		public function setNewBaseTemplate():BaseTemplate
		{
			targetBaseTemplate=new BaseTemplate();
			return targetBaseTemplate;
		}

		/**
		 * ベーステンプレートを複製します.
		 * <p> 新規に作成されたベーステンプレートはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setRegisteredBaseTemplate(source:BaseTemplate=null):BaseTemplate
		{
			if (source == null && baseTemplates.targetBaseTemplate != null)
			{
				source=baseTemplates.targetBaseTemplate;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			if (source.id <= 0)
			{
				throw new Error("source is not registered.");
			}
			targetBaseTemplate=ObjectUtil.copy(source) as BaseTemplate;
			return targetBaseTemplate;
		}

		/**
		 * ベーステンプレートを複製します.
		 * <p> 新規に作成されたベーステンプレートはターゲットにセットされます.
		 * @param source
		 * @return
		 * @throws Error
		 */
		public function setClonedBaseTemplate(source:BaseTemplate=null):BaseTemplate
		{
			if (source == null && baseTemplates.targetBaseTemplate != null)
			{
				source=baseTemplates.targetBaseTemplate;
			}
			if (source == null)
			{
				throw new Error("source is null");
			}
			if (source.id <= 0)
			{
				throw new Error("source is not registered.");
			}
			targetBaseTemplate=ObjectUtil.copy(source) as BaseTemplate;
			return targetBaseTemplate;
		}

		///////////////////////////////////////////////////////////////////////////////////////
		//
		//  TargetBaseTemplate
		//
		///////////////////////////////////////////////////////////////////////////////////////

		[Bindable("changeTarget")]
		/** 編集対象のベーステンプレートインスタンスグループ */
		public function set targetBaseTemplate(value:BaseTemplate):void
		{
			_targetBaseTemplate = value;
			dispatchEvent(new Event(CHANGE_TARGET));
		}
		public function get targetBaseTemplate():BaseTemplate
		{
			return _targetBaseTemplate;
		}
		private var _targetBaseTemplate:BaseTemplate;

		public function set targetTemplate(value:Template):void
		{
			if(_targetBaseTemplate)
			{
				if(value)
				{
					if(_targetBaseTemplate.templateId != value.id)
					{
						_targetBaseTemplate.templateId = value.id;
						_targetBaseTemplate.template = value;
					}
				}
			}
		}
	}
}