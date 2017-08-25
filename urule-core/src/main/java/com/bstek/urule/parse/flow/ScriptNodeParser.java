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

import com.bstek.urule.model.flow.ScriptNode;

/**
 * @author Jacky.gao
 * @since 2015年4月22日
 */
public class ScriptNodeParser extends FlowNodeParser<ScriptNode> {
	@Override
	public ScriptNode parse(Element element) {
		ScriptNode node =new ScriptNode();
		node.setName(element.attributeValue("name"));
		node.setEventBean(element.attributeValue("event-bean"));
		node.setX(element.attributeValue("x"));
		node.setY(element.attributeValue("y"));
		node.setWidth(element.attributeValue("width"));
		node.setHeight(element.attributeValue("height"));
		node.setConnections(parseConnections(element));
		String script=element.getStringValue();
		node.setScript(script);
		return node;
	}
	public boolean support(String name) {
		return name.equals("script");
	};
}
