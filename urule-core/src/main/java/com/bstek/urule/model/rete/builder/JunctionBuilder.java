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
package com.bstek.urule.model.rete.builder;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.ConditionNode;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.NamedCriteria;

public abstract class JunctionBuilder extends CriterionBuilder{
	protected BaseReteNode buildCriterion(Criterion criterion,BuildContext context,ConditionNode prevCriteriaNode){
		if(criterion instanceof Junction){
			Junction junction=(Junction)criterion;
			return ReteBuilder.buildCriterion(context, junction);
		}else if(criterion instanceof Criteria){
			Criteria criteria=(Criteria)criterion;
			return buildCriteria(criteria,prevCriteriaNode,context);
		}else if(criterion instanceof NamedCriteria){
			NamedCriteria criteria=(NamedCriteria)criterion;
			return buildNamedCriteria(criteria, prevCriteriaNode, context);
		}
		return null;
	}
}
