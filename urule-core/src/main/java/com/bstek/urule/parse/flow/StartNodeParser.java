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

import com.bstek.urule.model.flow.StartNode;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class StartNodeParser extends FlowNodeParser<StartNode> {
	public StartNode parse(Element element) {
		StartNode start=new StartNode(element.attributeValue("name"));
		start.setConnections(parseConnections(element));
		start.setEventBean(element.attributeValue("event-bean"));
		start.setX(element.attributeValue("x"));
		start.setY(element.attributeValue("y"));
		start.setWidth(element.attributeValue("width"));
		start.setHeight(element.attributeValue("height"));
		return start;
	}
	public boolean support(String name) {
		return name.equals("start");
	}
}
