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
package com.bstek.urule.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.model.decisiontree.TreeNode;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Lhs;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class DecisionTreeRulesBuilder {
	public RuleSet buildRules(DecisionTree tree) throws IOException{
		RuleSet rs=new RuleSet();
		
		List<Library> libs=tree.getLibraries();
		if(libs!=null){
			for(Library lib:libs){
				rs.addLibrary(lib);
			}
		}
		List<VariableTreeNode> nodes=new ArrayList<VariableTreeNode>();
		nodes.add(tree.getVariableTreeNode());
		List<ActionTreeNode> list=new ArrayList<ActionTreeNode>();
		fetchActionTreeNodes(nodes, list);
		List<Rule> rules=new ArrayList<Rule>();
		for(ActionTreeNode actionNode:list){
			Rule rule=new Rule();
			rule.setDebug(tree.getDebug());
			rule.setEnabled(tree.getEnabled());
			rule.setEffectiveDate(tree.getEffectiveDate());
			rule.setExpiresDate(tree.getExpiresDate());
			rule.setSalience(tree.getSalience());
			rules.add(rule);
			rule.setName("tree-rule");
			Rhs rhs=new Rhs();
			rhs.setActions(actionNode.getActions());
			rule.setRhs(rhs);
			Lhs lhs=new Lhs();
			rule.setLhs(lhs);
			And and=new And();
			lhs.setCriterion(and);
			ConditionTreeNode treeNode=(ConditionTreeNode)actionNode.getParentNode();
			buildCriterion(and,treeNode);
		}
		rs.setRules(rules);
		return rs;
	}
	
	private void buildCriterion(And and,ConditionTreeNode node){
		if(node==null)return;
		List<ConditionTreeNode> nodes=new ArrayList<ConditionTreeNode>();
		nodes.add(node);
		VariableTreeNode varNode=null;
		TreeNode parentNode=node.getParentNode();
		while(parentNode!=null){
			if(parentNode instanceof VariableTreeNode){
				varNode=(VariableTreeNode)parentNode;
				buildCriterion(and,(ConditionTreeNode)parentNode.getParentNode());
				break;
			}else if(parentNode instanceof ConditionTreeNode){
				ConditionTreeNode parentConditionTreeNode=(ConditionTreeNode)parentNode;
				nodes.add(parentConditionTreeNode);
				parentNode=parentConditionTreeNode.getParentNode();
			}
		}
		if(varNode==null){
			throw new RuleException("Decision tree is invalid.");
		}
		for(ConditionTreeNode cn:nodes){
			and.addCriterion(buildCriteria(cn,varNode));
		}
	}
	
	private Criteria buildCriteria(ConditionTreeNode cn,VariableTreeNode varNode){
		Criteria c=new Criteria();
		c.setLeft(varNode.getLeft());
		c.setOp(cn.getOp());
		c.setValue(cn.getValue());
		return c;
	}
	
	
	public void fetchActionTreeNodes(List<? extends TreeNode> nodes,List<ActionTreeNode> list){
		for(TreeNode node:nodes){
			if(node instanceof ActionTreeNode){
				list.add((ActionTreeNode)node);
			}else if(node instanceof VariableTreeNode){
				VariableTreeNode vn=(VariableTreeNode)node;
				List<ConditionTreeNode> conditionNodes=vn.getConditionTreeNodes();
				if(conditionNodes!=null){
					fetchActionTreeNodes(conditionNodes, list);
				}
			}else if(node instanceof ConditionTreeNode){
				ConditionTreeNode cn=(ConditionTreeNode)node;
				List<ActionTreeNode> actionNodes=cn.getActionTreeNodes();
				if(actionNodes!=null){
					fetchActionTreeNodes(actionNodes, list);
				}
				List<ConditionTreeNode> conditionNodes=cn.getConditionTreeNodes();
				if(conditionNodes!=null){
					fetchActionTreeNodes(conditionNodes, list);
				}
				List<VariableTreeNode> varNodes=cn.getVariableTreeNodes();
				if(varNodes!=null){
					fetchActionTreeNodes(varNodes, list);
				}
			}
			
		}
	}
}
