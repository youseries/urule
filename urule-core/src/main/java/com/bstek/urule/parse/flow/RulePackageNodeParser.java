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

import com.bstek.urule.model.flow.RulePackageNode;

/**
 * @author Jacky.gao
 * @since 2015年4月21日
 */
public class RulePackageNodeParser extends FlowNodeParser<RulePackageNode> {
	@Override
	public RulePackageNode parse(Element element) {
		RulePackageNode node=new RulePackageNode(element.attributeValue("name"));
		node.setConnections(parseConnections(element));
		node.setProject(element.attributeValue("project"));
		node.setPackageId(element.attributeValue("package-id"));
		node.setX(element.attributeValue("x"));
		node.setY(element.attributeValue("y"));
		node.setWidth(element.attributeValue("width"));
		node.setHeight(element.attributeValue("height"));
		return node;
	}
	@Override
	public boolean support(String name) {
		return name.equals("rule-package");
	}
}
