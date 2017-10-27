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
 * @since 2015年3月10日
 */
public class ParameterValue extends AbstractValue {
	private String variableName;
	private String variableLabel;
	private ValueType valueType=ValueType.Parameter;
	@Override
	public ValueType getValueType() {
		return valueType;
	}

	public String getVariableName() {
		return variableName;
	}


	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}


	public String getVariableLabel() {
		return variableLabel;
	}


	public void setVariableLabel(String variableLabel) {
		this.variableLabel = variableLabel;
	}


	@Override
	public String getId() {
		String id="[P]参数."+variableLabel;
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}
}
