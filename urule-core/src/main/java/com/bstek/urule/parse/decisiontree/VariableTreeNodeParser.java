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

import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.TreeNodeType;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.parse.LeftParser;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class VariableTreeNodeParser implements Parser<VariableTreeNode> {
	private LeftParser leftParser;
	private ConditionTreeNodeParser conditionTreeNodeParser;
	@Override
	public VariableTreeNode parse(Element element) {
		VariableTreeNode node=new VariableTreeNode();
		node.setNodeType(TreeNodeType.variable);
		List<ConditionTreeNode> conditionTreeNodes=new ArrayList<ConditionTreeNode>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(name.equals("left")){
				node.setLeft(leftParser.parse(ele));
			}else if(conditionTreeNodeParser.support(name)){
				ConditionTreeNode cn=conditionTreeNodeParser.parse(ele);
				cn.setParentNode(node);
				conditionTreeNodes.add(cn);
			}
		}
		if(conditionTreeNodes.size()>0){
			node.setConditionTreeNodes(conditionTreeNodes);
		}
		return node;
	}
	
	public void setConditionTreeNodeParser(
			ConditionTreeNodeParser conditionTreeNodeParser) {
		this.conditionTreeNodeParser = conditionTreeNodeParser;
	}
	
	public void setLeftParser(LeftParser leftParser) {
		this.leftParser = leftParser;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("variable-tree-node");
	}
}
