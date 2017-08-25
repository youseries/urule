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

import com.bstek.urule.model.flow.JoinNode;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class JoinNodeParser extends FlowNodeParser<JoinNode> {
	public JoinNode parse(Element element) {
		JoinNode join=new JoinNode(element.attributeValue("name"));
		join.setConnections(parseConnections(element));
		join.setEventBean(element.attributeValue("event-bean"));
		join.setX(element.attributeValue("x"));
		join.setY(element.attributeValue("y"));
		join.setWidth(element.attributeValue("width"));
		join.setHeight(element.attributeValue("height"));
		return join;
	}
	public boolean support(String name) {
		return name.equals("join");
	}
}
