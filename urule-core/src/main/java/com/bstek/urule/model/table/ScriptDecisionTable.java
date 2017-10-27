/*******************************************************************************
 * Copyright (C) 2017 Bstek.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
