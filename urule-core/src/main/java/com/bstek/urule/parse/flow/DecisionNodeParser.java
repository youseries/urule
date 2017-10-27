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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.model.flow.DecisionItem;
import com.bstek.urule.model.flow.DecisionNode;
import com.bstek.urule.model.flow.DecisionType;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class DecisionNodeParser extends FlowNodeParser<DecisionNode> {
	public DecisionNode parse(Element element) {
		DecisionNode decision=new DecisionNode(element.attributeValue("name"));
		decision.setEventBean(element.attributeValue("event-bean"));
		String decitionType=element.attributeValue("decision-type");
		if(StringUtils.isNotEmpty(decitionType)){
			decision.setDecisionType(DecisionType.valueOf(decitionType));
		}
		decision.setX(element.attributeValue("x"));
		decision.setY(element.attributeValue("y"));
		decision.setWidth(element.attributeValue("width"));
		decision.setHeight(element.attributeValue("height"));
		decision.setConnections(parseConnections(element));
		List<DecisionItem> items=new ArrayList<DecisionItem>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("item")){
				continue;
			}
			DecisionItem item=parseDecisionItem(ele);
			items.add(item);
		}
		decision.setItems(items);
		return decision;
	}
	
	private DecisionItem parseDecisionItem(Element element){
		DecisionItem item=new DecisionItem();
		item.setTo(element.attributeValue("connection"));
		String script=element.getStringValue();
		item.setScript(script);
		String percent=element.attributeValue("percent");
		if(StringUtils.isNotEmpty(percent)){
			item.setPercent(Integer.valueOf(percent));			
		}
		return item;
	}
	
	public boolean support(String name) {
		return name.equals("decision");
	}
}
