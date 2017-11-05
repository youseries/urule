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
package com.bstek.urule.parse.decisiontree;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.TreeNodeType;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class ConditionTreeNodeParser implements Parser<ConditionTreeNode> {
	private ValueParser valueParser;
	private VariableTreeNodeParser variableTreeNodeParser;
	private ActionTreeNodeParser actionTreeNodeParser;
	@Override
	public ConditionTreeNode parse(Element element) {
		ConditionTreeNode node=new ConditionTreeNode();
		node.setNodeType(TreeNodeType.condition);
		node.setOp(Op.valueOf(element.attributeValue("op")));
		List<ConditionTreeNode> conditionTreeNodes=new ArrayList<ConditionTreeNode>();
		List<ActionTreeNode> actionTreeNodes=new ArrayList<ActionTreeNode>();
		List<VariableTreeNode> variableTreeNodes=new ArrayList<VariableTreeNode>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(valueParser.support(name)){
				node.setValue(valueParser.parse(ele));
			}else if(support(name)){
				ConditionTreeNode cn=parse(ele);
				cn.setParentNode(node);
				conditionTreeNodes.add(cn);
			}else if(variableTreeNodeParser.support(name)){
				VariableTreeNode vn=variableTreeNodeParser.parse(ele);
				vn.setParentNode(node);
				variableTreeNodes.add(vn);
			}else if(actionTreeNodeParser.support(name)){
				ActionTreeNode an=actionTreeNodeParser.parse(ele);
				an.setParentNode(node);
				actionTreeNodes.add(an);
			}
		}
		if(conditionTreeNodes.size()>0){
			node.setConditionTreeNodes(conditionTreeNodes);
		}
		if(actionTreeNodes.size()>0){
			node.setActionTreeNodes(actionTreeNodes);
		}
		if(variableTreeNodes.size()>0){
			node.setVariableTreeNodes(variableTreeNodes);
		}
		return node;
	}
	
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	
	public void setActionTreeNodeParser(
			ActionTreeNodeParser actionTreeNodeParser) {
		this.actionTreeNodeParser = actionTreeNodeParser;
	}
	public void setVariableTreeNodeParser(
			VariableTreeNodeParser variableTreeNodeParser) {
		this.variableTreeNodeParser = variableTreeNodeParser;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("condition-tree-node");
	}
}
