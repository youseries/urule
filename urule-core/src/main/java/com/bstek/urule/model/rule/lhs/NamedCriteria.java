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

import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2016年8月15日
 */
public class NamedCriteria extends BaseCriterion implements BaseCriteria{
	private String referenceName;
	private String variableCategory;
	private CriteriaUnit unit;
	private String id;
	
	@Override
	public EvaluateResponse evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		EvaluateResponse response=unit.evaluate(context, obj, allMatchedObjects);
		return response;
	}

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

	public CriteriaUnit getUnit() {
		return unit;
	}

	public void setUnit(CriteriaUnit unit) {
		this.unit = unit;
	}

	@Override
	public String getId() {
		if(id==null){
			StringBuffer sb=new StringBuffer();
			sb.append(referenceName+":"+variableCategory+"(");
			sb.append(unit.getId());
			sb.append(")");
			id=sb.toString();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
