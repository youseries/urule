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

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2015年5月29日
 */
public class MultiCondition {
	private String id;
	private List<PropertyCriteria> conditions;
	private JunctionType type=JunctionType.and;
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		if(type.equals(JunctionType.and)){
			for(PropertyCriteria criteria:conditions){
				boolean value=criteria.evaluate(context, obj, allMatchedObjects);
				if(!value){
					return false;
				}
			}
		}else{
			for(PropertyCriteria criteria:conditions){
				boolean value=criteria.evaluate(context, obj, allMatchedObjects);
				if(value){
					break;
				}
			}
		}
		return true;
	}
	public void addCondition(PropertyCriteria condition){
		if(conditions==null){
			conditions=new ArrayList<PropertyCriteria>();
		}
		conditions.add(condition);
	}
	public List<PropertyCriteria> getConditions() {
		return conditions;
	}

	public void setConditions(List<PropertyCriteria> conditions) {
		this.conditions = conditions;
	}

	public JunctionType getType() {
		return type;
	}
	public void setType(JunctionType type) {
		this.type = type;
	}
	public String getId(){
		if(id==null){
			for(PropertyCriteria criteria:conditions){
				if(id==null){
					id=criteria.getId();
				}else{
					id+=type.name()+" "+criteria.getId();
				}
			}
		}
		return id;
	}
}
