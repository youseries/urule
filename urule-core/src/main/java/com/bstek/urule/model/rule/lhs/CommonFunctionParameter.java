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
package com.bstek.urule.model.rule.lhs;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.model.rule.Value;

/**
 * @author Jacky.gao
 * @since 2015年7月28日
 */
public class CommonFunctionParameter {
	@JsonIgnore
	private String id;
	private Value objectParameter;
	private String name;
	private String property;
	private String propertyLabel;
	public Value getObjectParameter() {
		return objectParameter;
	}
	public String getId(){
		if(id==null){
			id=objectParameter.getId();
			if(property!=null){
				id+=","+property;
			}
		}
		return id;
	}
	public void setObjectParameter(Value objectParameter) {
		this.objectParameter = objectParameter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getPropertyLabel() {
		return propertyLabel;
	}
	public void setPropertyLabel(String propertyLabel) {
		this.propertyLabel = propertyLabel;
	}
}
