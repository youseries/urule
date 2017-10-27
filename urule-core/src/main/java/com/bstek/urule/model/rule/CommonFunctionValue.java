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

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;

/**
 * @author Jacky.gao
 * @since 2015年7月28日
 */
public class CommonFunctionValue extends AbstractValue{
	@JsonIgnore
	private String id;
	private String name;
	private String label;
	private CommonFunctionParameter parameter;
	private ValueType valueType=ValueType.CommonFunction;
	@Override
	public String getId() {
		if(id==null){
			id= "[FUN]"+label+"("+parameter.getId()+")";			
		}
		return id;
	}
	@Override
	public ValueType getValueType() {
		return valueType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}
	public CommonFunctionParameter getParameter() {
		return parameter;
	}
	public void setParameter(CommonFunctionParameter parameter) {
		this.parameter = parameter;
	}
}
