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

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rete.builder.BuildContext;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Or;

/**
 * @author Jacky.gao
 * @since 2016年9月9日
 */
public class OrBuilder extends JunctionBuilder{
	@Override
	public BaseReteNode buildCriterion(BaseCriterion c, BuildContext context) {
		Or or=(Or)c;
		List<Criterion> criterions=or.getCriterions();
		if(criterions==null || criterions.size()==0){
			throw new RuleException("Condition join node[or] need one child at least.");
		}
		List<BaseReteNode> childNodes=new ArrayList<BaseReteNode>();
		for(Criterion criterion:criterions){
			BaseReteNode node=buildCriterion(criterion, context , null);
			if(node==null){
				continue;
			}
			childNodes.add(node);
		}
		if(childNodes.size()==0){
			return null;
		}
		if(childNodes.size()==1){
			return childNodes.get(0);
		}
		OrNode orNode=new OrNode(context.nextId());
		for(BaseReteNode node:childNodes){
			node.addLine(orNode);
		}
		return orNode;
	}
	@Override
	public boolean support(Criterion criterion) {
		return criterion instanceof Or;
	}
}
