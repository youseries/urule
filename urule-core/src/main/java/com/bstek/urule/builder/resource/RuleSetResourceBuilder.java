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
package com.bstek.urule.builder.resource;

import org.dom4j.Element;

import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class RuleSetResourceBuilder implements ResourceBuilder<RuleSet> {
	private RuleSetDeserializer ruleSetDeserializer;
	public RuleSet build(Element root) {
		return ruleSetDeserializer.deserialize(root);
	}
	public boolean support(Element root) {
		return ruleSetDeserializer.support(root);
	}
	public ResourceType getType() {
		return ResourceType.RuleSet;
	}
	public void setRuleSetDeserializer(RuleSetDeserializer ruleSetDeserializer) {
		this.ruleSetDeserializer = ruleSetDeserializer;
	}
}
