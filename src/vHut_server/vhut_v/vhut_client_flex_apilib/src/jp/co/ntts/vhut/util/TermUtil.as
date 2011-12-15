/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import jp.co.ntts.vhut.core.presentation.IconLabel;
	import jp.co.ntts.vhut.entity.Term;

	import mx.collections.ArrayCollection;
	import mx.formatters.DateFormatter;

	/**
	 * 期間に関する便利クラスです.
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
	public class TermUtil
	{
		public function TermUtil()
		{
		}

		public static function arrayAndArray(termList0:Array, termList1:Array):Array
		{
			return convertVectorToArray(
				vectorAndVector(
					convertArrayToVector(termList0)
					, convertArrayToVector(termList1)
				)
			);
		}

		public static function arrayOrArray(termList0:Array, termList1:Array):Array
		{
			return convertVectorToArray(
				vectorOrVector(
					convertArrayToVector(termList0)
					, convertArrayToVector(termList1)
				)
			);
		}

		public static function arrayXorArray(termList0:Array, termList1:Array):Array
		{
			return convertVectorToArray(
				vectorXorVector(
					convertArrayToVector(termList0)
					, convertArrayToVector(termList1)
				)
			);
		}

		public static function vectorAndVector(termList0:Vector.<Term>, termList1:Vector.<Term>):Vector.<Term>
		{
			termList0 = normalize(termList0);
			termList1 = normalize(termList1);
			var resultList:Vector.<Term> = new Vector.<Term>();
			for each (var term:Term in termList1)
			{
				resultList = resultList.concat(vectorAndItem(termList0, term));
			}
			return resultList;
		}

		public static function vectorOrVector(termList0:Vector.<Term>, termList1:Vector.<Term>):Vector.<Term>
		{
			var term:Term;
			var resultList:Vector.<Term> = normalize(termList0);
			termList1 = normalize(termList1);
//			for each (term in resultList)
//			{
//				trace(termToString(term));
//			}
//			trace();
			for each (term in termList1)
			{
				resultList = vectorOrItem(resultList, term);
//				trace("term = " + termToString(term));
//				for each (term in resultList)
//				{
//					trace(termToString(term));
//				}
//				trace();
			}

			return normalize(resultList);
		}

		public static function vectorXorVector(termList0:Vector.<Term>, termList1:Vector.<Term>):Vector.<Term>
		{
			var term:Term;
			var resultList:Vector.<Term> = vectorOrVector(termList0, termList1);
//			trace("resultList");
//			for each (term in resultList)
//			{
//				trace(termToString(term));
//			}
			var andList:Vector.<Term> = vectorAndVector(termList0, termList1);
//			trace("andList");
//			for each (term in andList)
//			{
//				trace(termToString(term));
//			}
			for each (term in andList)
			{
//				trace("term = "+termToString(term));
				resultList = vectorXorItem(resultList, term);
//				trace("resultList");
//				for each (var targetTerm:Term in resultList)
//				{
//					trace(termToString(targetTerm));
//				}
			}
			return resultList;
		}

		public static function arrayAndItem(termList:Array, term:Term):Array
		{
			return convertVectorToArray(
				vectorAndItem(
					convertArrayToVector(termList)
					, term
				)
			);
		}

		public static function arrayOrItem(termList:Array, term:Term):Array
		{
			return convertVectorToArray(
				vectorOrItem(
					convertArrayToVector(termList)
					, term
				)
			);
		}

		public static function arrayXorItem(termList:Array, term:Term):Array
		{
			return convertVectorToArray(
				vectorXorItem(
					convertArrayToVector(termList)
					, term
				)
			);
		}

		public static function vectorAndItem(termList:Vector.<Term>, term:Term):Vector.<Term>
		{
			termList = normalize(termList);
			var resultList:Vector.<Term> = new Vector.<Term>();
			for each (var targetTerm:Term in termList)
			{
				resultList = resultList.concat(and(targetTerm, term));
			}
			return resultList;
		}

		public static function vectorOrItem(termList:Vector.<Term>, term:Term):Vector.<Term>
		{
			var targetTerm:Term;

			if(termList.length == 0)
			{
				return Vector.<Term>([term]);
			}
			termList = normalize(termList);
			var resultList:Vector.<Term> = new Vector.<Term>();
			for each (targetTerm in termList)
			{
				resultList = resultList.concat(or(targetTerm, term));
//				trace("targetTerm= "+termToString(targetTerm));
//				trace("term= "+termToString(term));
//				for each (targetTerm in resultList)
//				{
//					trace(termToString(targetTerm));
//				}
//				trace();
			}
			return normalize(resultList);
		}

		public static function vectorXorItem(termList:Vector.<Term>, term:Term):Vector.<Term>
		{
			var andList:Vector.<Term> = vectorAndItem(termList, term);
			var orList:Vector.<Term> = vectorOrItem(termList, term);
			var nextOrList:Vector.<Term>;
			var resultList:Vector.<Term> = new Vector.<Term>();
			for each (var andTerm:Term in andList)
			{
				nextOrList = new Vector.<Term>();
				for each (var orTerm:Term in orList)
				{
					if(hasOverlap(orTerm, andTerm))
					{
//						trace("orTerm= " + termToString(orTerm));
//						trace("andTerm= " + termToString(andTerm));
						nextOrList = nextOrList.concat(xor(orTerm, andTerm));
//						trace("nextOrList");
//						for each (var targetTerm:Term in nextOrList)
//						{
//							trace(termToString(targetTerm));
//						}
					}
					else
					{
						resultList.push(orTerm);
					}
				}
				orList = normalize(nextOrList);
			}
			return normalize(resultList.concat(nextOrList));
		}

		public static function and(term0:Term, term1:Term):Vector.<Term>
		{
			var result:Vector.<Term> = new Vector.<Term>();
			if(hasOverlap(term0, term1))
			{
				var sMax:Date = new Date(Math.max(term0.startTime.getTime(), term1.startTime.getTime()));
				var eMin:Date = new Date(Math.min(term0.endTime.getTime(), term1.endTime.getTime()));
				var term:Term;
				if(sMax < eMin)
				{
					term = term0.clone();
					term.id = NaN;
					term.startTime = sMax;
					term.endTime = eMin;
					result.push(term);
				}
			}
			return result;
		}

		public static function or(term0:Term, term1:Term):Vector.<Term>
		{
			var result:Vector.<Term> = new Vector.<Term>();
			if(hasOverlap(term0, term1))
			{
				var sMin:Date = new Date(Math.min(term0.startTime.getTime(), term1.startTime.getTime()));
				var eMax:Date = new Date(Math.max(term0.endTime.getTime(), term1.endTime.getTime()));
				var term:Term;
				if(sMin < eMax)
				{
					term = term0.clone();
					term.id = NaN;
					term.startTime = sMin;
					term.endTime = eMax;
					result.push(term);
				}
			}
			else
			{
				result.push(term0, term1);
			}
			return result;
		}

		public static function xor(term0:Term, term1:Term):Vector.<Term>
		{
			if(hasOverlap(term0, term1))
			{
				var result:Vector.<Term> = new Vector.<Term>();
				var sMin:Date = new Date(Math.min(term0.startTime.getTime(), term1.startTime.getTime()));
				var sMax:Date = new Date(Math.max(term0.startTime.getTime(), term1.startTime.getTime()));
				var eMin:Date = new Date(Math.min(term0.endTime.getTime(), term1.endTime.getTime()));
				var eMax:Date = new Date(Math.max(term0.endTime.getTime(), term1.endTime.getTime()));
				var term:Term;
				if(sMin < sMax)
				{
					term = term0.clone();
					term.id = NaN;
					term.startTime = sMin;
					term.endTime = sMax;
					result.push(term);
				}
				if(eMin < eMax)
				{
					term = term0.clone();
					term.id = NaN;
					term.startTime = eMin;
					term.endTime = eMax;
					result.push(term);
				}
				return result;
			}
			else
			{
				return Vector.<Term>([term0, term1]);
			}
		}

		public static function normalize(termList:Vector.<Term>):Vector.<Term>
		{
			if (termList.length <= 1)
				return termList;

			termList.sort(
				function(term0:Term, term1:Term):Number
				{
					var compForStartTime:Number = term0.startTime.getTime() - term1.startTime.getTime();
					switch(compForStartTime)
					{
						case 0:
							return term0.endTime.getTime() - term1.endTime.getTime();
						default:
							return compForStartTime;
					}
				}
			);

			var result:Vector.<Term> = new Vector.<Term>();
			var term:Term;
			for (var i:uint=0; i<termList.length; i++)
			{
				var targetTerm:Term = termList[i];
				if(!term || term.endTime.getTime() < targetTerm.startTime.getTime())
				{
					term = targetTerm.clone();
					result.push(term);
				}
				else if (term.endTime.getTime() < targetTerm.endTime.getTime())
				{
					term.endTime = targetTerm.endTime;
				}
			}
			return result;
		}

		public static function hasOverlap(term0:Term, term1:Term):Boolean
		{
			return ((term0.startTime.getTime() <= term1.endTime.getTime())
				&&
				(term0.endTime.getTime() >= term1.startTime.getTime()));
		}

		protected static function convertArrayToVector(value:Array):Vector.<Term>
		{
			return Vector.<Term>(value);
		}

		protected static function convertVectorToArray(value:Vector.<Term>):Array
		{
			var array:Array = new Array(value.length);
			var i:int = value.length;
			while (i--) {
				array[i] = value[i];
			}
			return array;
		}

		private static function termToString(term:Term):String
		{
			var formatter:DateFormatter = new DateFormatter();
			formatter.formatString = "YYYY.MM.DD";
			return formatter.format(term.startTime) +" - "+ formatter.format(term.endTime);
		}

	}
}