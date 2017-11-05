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
