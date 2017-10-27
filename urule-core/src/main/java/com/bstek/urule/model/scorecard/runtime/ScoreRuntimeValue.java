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
package com.bstek.urule.model.scorecard.runtime;
/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public class ScoreRuntimeValue {
	public static final String SCORE_VALUE="scoring_value";
	private int rowNumber;
	private String name;
	private String weight;
	private Object value;
	public ScoreRuntimeValue(int rowNumber,String name,String weight,Object value) {
		this.rowNumber=rowNumber;
		this.name = name;
		this.weight=weight;
		this.value=value;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public String getName() {
		return name;
	}
	
	public String getWeight() {
		return weight;
	}
	
	public Object getValue() {
		return value;
	}
}
