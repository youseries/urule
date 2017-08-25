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

import java.util.List;

public class NamedJunction extends BaseCriterion{
	private String referenceName;
	private String variableCategory;
	private JunctionType junctionType;
	private List<NamedItem> items;
	public String getReferenceName() {
		return referenceName;
	}
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}
	public String getVariableCategory() {
		return variableCategory;
	}
	public void setVariableCategory(String variableCategory) {
		this.variableCategory = variableCategory;
	}
	public JunctionType getJunctionType() {
		return junctionType;
	}
	public void setJunctionType(JunctionType junctionType) {
		this.junctionType = junctionType;
	}
	public List<NamedItem> getItems() {
		return items;
	}
	public void setItems(List<NamedItem> items) {
		this.items = items;
	}
}
