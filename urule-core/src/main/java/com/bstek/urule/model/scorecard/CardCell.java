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
package com.bstek.urule.model.scorecard;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.table.Joint;

/**
 * @author Jacky.gao
 * @since 2016年9月20日
 */
public class CardCell implements Comparable<CardCell>{
	private String variableLabel;
	private String variableName;
	private Datatype datatype;
	private CellType type;
	private String weight;
	private Joint joint;
	private Value value;
	private int row;
	private int col;
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
	public Datatype getDatatype() {
		return datatype;
	}
	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}
	public CellType getType() {
		return type;
	}
	public void setType(CellType type) {
		this.type = type;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public Joint getJoint() {
		return joint;
	}
	public void setJoint(Joint joint) {
		this.joint = joint;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	@Override
	public int compareTo(CardCell o) {
		return row-o.getRow();
	}
}
