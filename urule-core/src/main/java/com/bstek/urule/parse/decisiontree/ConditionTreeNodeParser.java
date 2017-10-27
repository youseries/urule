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
