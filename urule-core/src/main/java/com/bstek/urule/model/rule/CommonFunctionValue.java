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
			id= "[函数]"+label+"("+parameter.getId()+")";
			if(arithmetic!=null){
				id=id+arithmetic.getId();
			}
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
