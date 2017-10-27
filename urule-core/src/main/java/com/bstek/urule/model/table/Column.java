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

import com.bstek.urule.model.library.Datatype;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class Column implements Comparable<Column>{
	private int num;
	private int width;
	private String variableCategory;
	private String variableLabel;
	private String variableName;
	private Datatype datatype;
	private ColumnType type;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getVariableCategory() {
		return variableCategory;
	}
	public void setVariableCategory(String variableCategory) {
		this.variableCategory = variableCategory;
	}
	public String getVariableLabel() {
		return variableLabel;
	}
	public void setVariableLabel(String variableLabel) {
		this.variableLabel = variableLabel;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public ColumnType getType() {
		return type;
	}
	public void setType(ColumnType type) {
		this.type = type;
	}
	public Datatype getDatatype() {
		return datatype;
	}
	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}
	public int compareTo(Column o) {
		return o.getNum()-num;
	}
}
