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

import com.bstek.urule.model.flow.RuleNode;

/**
 * @author Jacky.gao
 * @since 2015年4月21日
 */
public class RuleNodeParser extends FlowNodeParser<RuleNode> {
	@Override
	public RuleNode parse(Element element) {
		RuleNode node=new RuleNode(element.attributeValue("name"));
		node.setFile(element.attributeValue("file"));
		node.setVersion(element.attributeValue("version"));
		node.setX(element.attributeValue("x"));
		node.setY(element.attributeValue("y"));
		node.setWidth(element.attributeValue("width"));
		node.setHeight(element.attributeValue("height"));
		node.setConnections(parseConnections(element));
		return node;
	}
	@Override
	public boolean support(String name) {
		return name.equals("rule");
	}
}
