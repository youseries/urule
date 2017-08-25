/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.model.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.urule.model.rule.Library;

/**
 * @author Jacky.gao
 * @since 2015年5月5日
 */
public class ScriptDecisionTable {
	private List<Row> rows;
	private List<Column> columns;
	private Map<String,ScriptCell> cellMap;
	private List<Library> libraries;
	public List<Row> getRows() {
		return rows;
	}
	public void addLibrary(Library library){
		if(libraries==null){
			libraries=new ArrayList<Library>();
		}
		libraries.add(library);
	}
	public void addRow(Row row){
		if(rows==null){
			rows=new ArrayList<Row>();
		}
		rows.add(row);
	}
	public void addColumn(Column col){
		if(columns==null){
			columns=new ArrayList<Column>();
		}
		columns.add(col);
	}
	public void addCell(ScriptCell cell){
		if(cellMap==null){
			cellMap=new HashMap<String,ScriptCell>();
		}
		cellMap.put(buildCellKey(cell.getRow(),cell.getCol()), cell);
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Map<String, ScriptCell> getCellMap() {
		return cellMap;
	}
	public List<Library> getLibraries() {
		return libraries;
	}
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}
	public String buildCellKey(int row,int col){
		return row+","+col;
	}
}
