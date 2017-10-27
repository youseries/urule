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
package com.bstek.urule.parse.flow;

import org.dom4j.Element;

import com.bstek.urule.model.flow.ActionNode;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ActionNodeParser extends FlowNodeParser<ActionNode> {
	public ActionNode parse(Element element) {
		ActionNode action=new ActionNode(element.attributeValue("name"));
		action.setActionBean(element.attributeValue("action-bean"));
		action.setEventBean(element.attributeValue("event-bean"));
		action.setX(element.attributeValue("x"));
		action.setY(element.attributeValue("y"));
		action.setWidth(element.attributeValue("width"));
		action.setHeight(element.attributeValue("height"));
		action.setConnections(parseConnections(element));
		return action;
	}
	public boolean support(String name) {
		return name.equals("action");
	}
}
