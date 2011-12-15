/*
* Copyright 2011 NTT Software Corporation.
* All Rights Reserved.
*/
package jp.co.ntts.vhut.util
{
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridColumn;

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
	 * $Date$
	 * $Revision$
	 * $Author$
	 */
	public class CsvUtil
	{
		public function CsvUtil()
		{
		}

		/**
		 * DataGridからCSVを生成する
		 * @param dataGrid Datagrid
		 * @return CSV
		 *
		 */
		public static function getCsvFromDataGrid(dataGrid:DataGrid):String
		{
			var csv:String = "";

			var labels:Array = new Array();
			var fields:Array = new Array();

			for each (var column:DataGridColumn in dataGrid.columns)
			{
				labels.push(column.headerText);
				fields.push(column.dataField);
			}

			csv += labels.join(',') + "\n";

			for each (var data:Object in dataGrid.dataProvider)
			{
				for each (var field:String in fields)
				{
					csv += data[field].toString() + ',';
				}
				csv = csv.slice(0, csv.length-1);
				csv += "\n";
			}
			return csv;
		}
	}
}