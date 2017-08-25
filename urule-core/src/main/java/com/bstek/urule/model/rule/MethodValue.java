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
