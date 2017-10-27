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

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年2月28日
 */
public class MethodValue extends AbstractValue {
	private String beanId;
	private String beanLabel;
	private String methodLabel;
	private String methodName;
	private List<Parameter> parameters;
	private ValueType valueType=ValueType.Method;
	@Override
	public ValueType getValueType() {
		return valueType;
	}

	@Override
	public String getId() {
		String id="[BEAN]["+beanId+"."+methodName+"]";
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getBeanLabel() {
		return beanLabel;
	}

	public void setBeanLabel(String beanLabel) {
		this.beanLabel = beanLabel;
	}

	public String getMethodLabel() {
		return methodLabel;
	}

	public void setMethodLabel(String methodLabel) {
		this.methodLabel = methodLabel;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}
