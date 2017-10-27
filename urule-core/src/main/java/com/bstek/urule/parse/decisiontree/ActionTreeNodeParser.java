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
import java.util.Collection;
import java.util.List;

import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.action.Action;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.TreeNodeType;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class ActionTreeNodeParser implements Parser<ActionTreeNode>,ApplicationContextAware {
	private Collection<ActionParser> actionParsers;
	@Override
	public ActionTreeNode parse(Element element) {
		ActionTreeNode node=new ActionTreeNode();
		node.setNodeType(TreeNodeType.action);
		List<Action> actions=new ArrayList<Action>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			
			for(ActionParser actionParser:actionParsers){
				if(actionParser.support(name)){
					actions.add(actionParser.parse(ele));
					break;
				}
			}
		}
		node.setActions(actions);
		return node;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("action-tree-node");
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		actionParsers=context.getBeansOfType(ActionParser.class).values();
	}
}
