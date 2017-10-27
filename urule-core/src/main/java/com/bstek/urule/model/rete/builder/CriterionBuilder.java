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

import com.bstek.urule.model.Node;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.ConditionNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.NamedCriteriaNode;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.ReteNode;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.NamedCriteria;

public abstract class CriterionBuilder{
	public abstract BaseReteNode buildCriterion(BaseCriterion c,BuildContext context);
	public abstract boolean support(Criterion criterion);
	
	protected BaseReteNode buildCriteria(Criteria criteria,ConditionNode prevNode,BuildContext context) {
		String objectType=context.getObjectType(criteria);
		if(prevNode!=null && !(prevNode instanceof NamedCriteriaNode)){
			CriteriaNode targetNode=null;
			String prevObjectType=context.getObjectType(prevNode.getCriteria());
			if(objectType.equals(prevObjectType)){
				List<ReteNode> prevChildrenNodes=prevNode.getChildrenNodes();
				targetNode = fetchSameCriteriaNode(criteria, prevChildrenNodes);
				if(targetNode==null){
					targetNode=new CriteriaNode((Criteria)criteria,context.nextId());
					prevNode.addLine(targetNode);
				}
			}else{
				targetNode=buildCriteriaNode(criteria,context,objectType);
			}
			return targetNode;
		}else{
			CriteriaNode criteriaNode=buildCriteriaNode(criteria,context,objectType);
			return criteriaNode;
		}
	}
	
	/**
	 * 带reference name的条件比较特殊，它不需要判断是否有父节点，需要将所有节点都直接挂在ObjectTypeNode下
	 * @param criteria 命名条件对象
	 * @param prevNode 上一节点对象
	 * @param context 上下文对象
	 * @return 返回命名条件节点对象
	 */
	protected NamedCriteriaNode buildNamedCriteria(NamedCriteria criteria,ConditionNode prevNode,BuildContext context){
		/*if(prevNode!=null){
			NamedCriteriaNode targetNode=null;
			String objectType=context.getObjectType(criteria);
			String prevObjectType=context.getObjectType(criteria);
			if(objectType.equals(prevObjectType)){
				List<ReteNode> prevChildrenNodes=prevNode.getChildrenNodes();
				targetNode = fetchExistNamedCriteriaNode(criteria, prevChildrenNodes);
				if(targetNode==null){
					targetNode=new NamedCriteriaNode(criteria,context.nextId());
					prevNode.addLine(targetNode);
				}
			}else{
				targetNode=buildNewTypeNamedCriteria(criteria,context);
			}
			return targetNode;
		}else{
			NamedCriteriaNode node=buildNewTypeNamedCriteria(criteria,context);
			return node;
		}*/
		NamedCriteriaNode node=buildNewTypeNamedCriteria(criteria,context);
		return node;
	}
	
	
	private CriteriaNode buildCriteriaNode(BaseCriteria criteria,BuildContext context,String objectType) {
		ObjectTypeNode targetObjectTypeNode=context.buildObjectTypeNode(objectType);
		List<ReteNode> childrenNodes=targetObjectTypeNode.getChildrenNodes();
		CriteriaNode targetNode = fetchSameCriteriaNode(criteria, childrenNodes);
		if(targetNode==null){
			targetNode=new CriteriaNode((Criteria)criteria,context.nextId());
			targetObjectTypeNode.addLine(targetNode);
		}
		return targetNode;
	}

	private NamedCriteriaNode buildNewTypeNamedCriteria(NamedCriteria criteria,BuildContext context) {
		VariableCategory category=context.getResourceLibrary().getVariableCategory(criteria.getVariableCategory());
		String objectType=category.getClazz();
		ObjectTypeNode targetObjectTypeNode=context.buildObjectTypeNode(objectType);
		List<ReteNode> childrenNodes=targetObjectTypeNode.getChildrenNodes();
		NamedCriteriaNode targetNode = fetchExistNamedCriteriaNode(criteria,childrenNodes);
		if(targetNode==null){
			targetNode=new NamedCriteriaNode(criteria,context.nextId());	
			targetObjectTypeNode.addLine(targetNode);
		}
		return targetNode;
	}
	
	private CriteriaNode fetchSameCriteriaNode(BaseCriteria criteria,List<ReteNode> childrenNodes) {
		String conditionId=criteria.getId();
		CriteriaNode targetNode=null;
		for(Node node:childrenNodes){
			if(!(node instanceof ConditionNode)){
				continue;
			}
			if(criteria instanceof Criteria){
				if(!(node instanceof CriteriaNode)){
					continue;
				}
			}
			if(criteria instanceof NamedCriteria){
				if(!(node instanceof NamedCriteriaNode)){
					continue;
				}
			}
			ConditionNode baseNode=(ConditionNode)node;
			String nodeId=baseNode.getCriteriaInfo();
			if(nodeId.equals(conditionId)){
				targetNode=(CriteriaNode)baseNode;
				break;
			}
		}
		return targetNode;
	}
	
	
	private NamedCriteriaNode fetchExistNamedCriteriaNode(NamedCriteria criteria,List<ReteNode> childrenNodes) {
		String conditionId=criteria.getId();
		NamedCriteriaNode targetNode=null;
		for(Node node:childrenNodes){
			if(!(node instanceof NamedCriteriaNode)){
				continue;
			}
			NamedCriteriaNode baseNode=(NamedCriteriaNode)node;
			String nodeId=baseNode.getCriteriaInfo();
			if(nodeId.equals(conditionId)){
				targetNode=baseNode;
				break;
			}
		}
		return targetNode;
	}
}
