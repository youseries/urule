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
		String id="[变量对象]"+variableCategory;
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}
	public String getVariableCategory() {
		return variableCategory;
	}
}
