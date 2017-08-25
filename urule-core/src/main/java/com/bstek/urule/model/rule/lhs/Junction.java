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


/**
 * @author Jacky.gao
 * @since 2014年12月25日
 */
public abstract class Junction extends BaseCriterion {
	private List<Criterion> criterions;
	public List<Criterion> getCriterions() {
		return criterions;
	}
	
	public void addCriterion(Criterion criterion){
		if(criterions==null){
			criterions=new ArrayList<Criterion>();
		}
		criterion.setParent(this);
		criterions.add(criterion);
	}

	public void setCriterions(List<Criterion> criterions) {
		for(Criterion criterion:criterions){
			criterion.setParent(this);
		}
		this.criterions = criterions;
	}
	public abstract String getJunctionType();
}
