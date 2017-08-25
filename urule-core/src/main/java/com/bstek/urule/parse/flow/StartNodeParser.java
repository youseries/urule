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
