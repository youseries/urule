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
package com.bstek.urule.model.rule;
/**
 * @author Jacky.gao
 * @since 2015年2月28日
 */
public class VariableCategoryValue extends AbstractValue {
	private String variableCategory;
	private ValueType valueType=ValueType.VariableCategory;
	public VariableCategoryValue() {
	}
	public VariableCategoryValue(String variableCategory) {
		this.variableCategory=variableCategory;
	}
	
	@Override
	public ValueType getValueType() {
		return valueType;
	}

	public void setVariableCategory(String variableCategory) {
		this.variableCategory = variableCategory;
	}
	
	@Override
	public String getId() {
		String id="[CATEGORY]"+variableCategory;
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}
	public String getVariableCategory() {
		return variableCategory;
	}
}
