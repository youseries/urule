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
