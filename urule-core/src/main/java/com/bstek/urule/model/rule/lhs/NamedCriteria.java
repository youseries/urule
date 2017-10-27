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
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		boolean result=unit.evaluate(context, obj, allMatchedObjects);
		return result;
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
