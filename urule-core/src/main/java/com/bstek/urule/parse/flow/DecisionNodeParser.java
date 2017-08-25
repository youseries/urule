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
