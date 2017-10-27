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

import com.bstek.urule.action.Action;
import com.bstek.urule.model.rule.Value;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class Cell {
	private int row;
	private int col;
	private int rowspan;
	private Joint joint;
	private Value value;
	private Action action;
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
	
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public Joint getJoint() {
		return joint;
	}
	public void setJoint(Joint joint) {
		this.joint = joint;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
}
