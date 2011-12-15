/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.mng.domain
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import jp.co.ntts.vhut.core.GetWithTimeSpanEvent;
	import jp.co.ntts.vhut.dto.PredictionDto;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.formatters.DateFormatter;
	
	[Event(name="getPerformanceWithTimeSpan", type="jp.co.ntts.vhut.core.GetWithTimeSpanEvent")]
	[ManagedEvents(names="getPerformanceWithTimeSpan")]
	/**
	 * 利用予測の管理クラス.
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
	public class Predictions extends EventDispatcher
	{
		public static const CHANGE_TIME:String = "changeTime"
		public static const CHANGE_PREDICTION:String = "changePrediction"
		public static const DATE_FORMAT:String ="YYYY.MM.DD"
		public static const DAY:uint = 24*60*60*1000
		
		public function Predictions(target:IEventDispatcher=null)
		{
			super(target);
			_dateFormatter = new DateFormatter();
			_dateFormatter.formatString = DATE_FORMAT;
			var now:Date = new Date();
			_startTime = new Date(now.fullYear, now.month, now.date);
			_endTime = new Date(startTime.getTime() + 10 * DAY);
			
		}
		private var _dateFormatter:DateFormatter
		
		[Bindable("changeTime")]
		public function set startTime(value:Date):void
		{
			updatePredictions(value);
		}
		public function get startTime():Date
		{
			return _startTime;
		}
		private var _startTime:Date;
		
		[Bindable("changeTime")]
		public function set endTime(value:Date):void
		{
			updatePredictions(null, value);
		}
		public function get endTime():Date
		{
			return _endTime;
		}
		private var _endTime:Date;
		
		[Bindable("changePrediction")]
		public function get cpuList():IList
		{
			return _cpuList;
		}
		private var _cpuList:ArrayCollection = new ArrayCollection();
		
		[Bindable("changePrediction")]
		public function get memoryList():IList
		{
			return _memoryList;
		}
		private var _memoryList:ArrayCollection = new ArrayCollection();
		
		[Bindable("changePrediction")]
		public function get storageList():IList
		{
			return _storageList;
		}
		private var _storageList:ArrayCollection = new ArrayCollection();
		
		[Bindable("changePrediction")]
		public function get publicIpList():IList
		{
			return _publicIpList;
		}
		private var _publicIpList:ArrayCollection = new ArrayCollection();
		
		[Bindable("changePrediction")]
		public function get vlanList():IList
		{
			return _vlanList;
		}
		private var _vlanList:ArrayCollection = new ArrayCollection();
		
		[Bindable("changePrediction")]
		public function get complexList():IList
		{
			return _complexList;
		}
		private function updateComplexList():void
		{
			_complexList.removeAll();
			var count:uint = (_endTime.getTime() - _startTime.getTime()) / DAY + 1;
			for(var i:uint=0; i<count; i++)
			{
				var data:Object = new Object();
				data.cpu=_dto.usedCpuCore[i];
				data.memory=_dto.usedMemory[i];
				data.storage=_dto.usedStorage[i];
				data.vlan=_dto.usedVlan[i];
				data.publicIp=_dto.usedPublicIp[i];
				_complexList.addItem(data);
			}
		}
		private var _complexList:ArrayCollection = new ArrayCollection();
		
		[Bindable("changeTime")]
		public function get dateLabelList():IList
		{
			return _dateLabelList;
		}
		private function updateDateLableList():void
		{
			_dateLabelList.removeAll();
			var count:uint = (_endTime.getTime() - _startTime.getTime()) / DAY + 1;
			
			var delta:Number = 1.0;
			
			if(count > dateLabelMax)
			{
				delta = Number(dateLabelMax)/Number(count);
			}
			
			for(var i:uint=0; i<count; i++)
			{
				var sum:Number = delta*Number(i);
				var mod:Number = sum%1;
				if(mod < delta || i==count-1)
				{
					_dateLabelList.addItem(_dateFormatter.format(new Date(_startTime.getTime() + i*DAY)));
				}
				else
				{
					_dateLabelList.addItem("");
				}
			}
		}
		private var _dateLabelList:ArrayCollection = new ArrayCollection();
		
		
		public function set dateLabelMax(value:uint):void
		{
			_dateLabelMax = value;
			updateDateLableList();
			dispatchEvent(new Event(CHANGE_TIME));
		}
		public function get dateLabelMax():uint
		{
			return _dateLabelMax;
		}
		private var _dateLabelMax:uint = 10;
		
		public function updatePredictions(startTime:Date=null, endTime:Date=null):void
		{
			var tmpStartTime:Date = _startTime;
			var tmpEndTime:Date = _endTime;
			if(startTime)
				tmpStartTime = new Date(startTime.fullYear, startTime.month, startTime.date);
			if(endTime)
				tmpEndTime = new Date(endTime.fullYear, endTime.month, endTime.date);
			if(tmpEndTime.getTime() - tmpStartTime.getTime() >= DAY)
			{
				dispatchEvent(GetWithTimeSpanEvent.newGetPerformance(tmpStartTime, tmpEndTime));
			
				_startTime = tmpStartTime;
				_endTime = tmpEndTime;
			}
			updateDateLableList();
			dispatchEvent(new Event(CHANGE_TIME));
		}
		
		public function set dto(value:PredictionDto):void
		{
			_dto = value;
			_startTime = _dto.startTime;
			_endTime = _dto.endTime;
			
			_cpuList.removeAll();
			_cpuList.addAll(new ArrayCollection(_dto.usedCpuCore));
			
			_memoryList.removeAll();
			_memoryList.addAll(new ArrayCollection(_dto.usedMemory));
			
			_storageList.removeAll();
			_storageList.addAll(new ArrayCollection(_dto.usedStorage));
			
			_publicIpList.removeAll();
			_publicIpList.addAll(new ArrayCollection(_dto.usedPublicIp));
			
			_vlanList.removeAll();
			_vlanList.addAll(new ArrayCollection(_dto.usedVlan));
			
			updateComplexList();
			
			dispatchEvent(new Event(CHANGE_PREDICTION));
		}
		private var _dto:PredictionDto;
		
	}
}