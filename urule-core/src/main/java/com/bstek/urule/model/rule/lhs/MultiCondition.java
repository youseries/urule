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
