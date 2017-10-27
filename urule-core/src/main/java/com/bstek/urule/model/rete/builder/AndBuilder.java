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

import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.rete.AndNode;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.ConditionNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.JunctionNode;
import com.bstek.urule.model.rete.NamedCriteriaNode;
import com.bstek.urule.model.rete.ReteNode;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;

/**
 * @author Jacky.gao
 * @since 2016年9月9日
 */
public class AndBuilder extends JunctionBuilder{
	@Override
	public BaseReteNode buildCriterion(BaseCriterion c, BuildContext context) {
		And and=(And)c;
		AndNode andNode=null;
		ConditionNode currentCriteriaNode=null;
		List<Criterion> criterions=and.getCriterions();
		if(criterions==null || criterions.size()==0){
			throw new RuleException("Condition join node[and] need one child at least.");
		}
		for(Criterion criterion:criterions){
			ReteNode node=buildCriterion(criterion, context, currentCriteriaNode);
			if(node==null){
				continue;
			}
			if(node instanceof CriteriaNode){
				if(currentCriteriaNode!=null){
					List<ReteNode> childrenNodes=currentCriteriaNode.getChildrenNodes();
					if(!childrenNodes.contains(node)){
						if(andNode==null){
							andNode=new AndNode(context.nextId());
						}
						currentCriteriaNode.addLine(andNode);
					}
				}
				currentCriteriaNode=(ConditionNode)node;
			}else if(node instanceof JunctionNode){
				if(andNode==null){
					andNode=new AndNode(context.nextId());
				}
				((JunctionNode) node).addLine(andNode);
			}else if(node instanceof NamedCriteriaNode){
				if(currentCriteriaNode!=null){
					List<ReteNode> childrenNodes=currentCriteriaNode.getChildrenNodes();
					if(!childrenNodes.contains(node)){
						if(andNode==null){
							andNode=new AndNode(context.nextId());
						}
						currentCriteriaNode.addLine(andNode);
					}
				}
				currentCriteriaNode=(ConditionNode)node;
			}
		}
		if(criterions.size()==1 && currentCriteriaNode!=null){
			return (BaseReteNode)currentCriteriaNode;
		}
		if(andNode==null){
			return (BaseReteNode)currentCriteriaNode;
		}
		if(andNode!=null && currentCriteriaNode!=null){
			currentCriteriaNode.addLine(andNode);
		}
		return andNode;
	}
	
	@Override
	public boolean support(Criterion criterion) {
		return criterion instanceof And;
	}
}
