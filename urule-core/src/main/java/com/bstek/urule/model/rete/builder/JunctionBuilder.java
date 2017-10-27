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
