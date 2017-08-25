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
