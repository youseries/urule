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

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.parse.deserializer.FlowDeserializer;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class FlowResourceBuilder implements ResourceBuilder<FlowDefinition> {
	private FlowDeserializer flowDeserializer;
	public FlowDefinition build(Element root) {
		return flowDeserializer.deserialize(root);
	}
	public ResourceType getType() {
		return ResourceType.Flow;
	}
	public boolean support(Element root) {
		return flowDeserializer.support(root);
	}
	public void setFlowDeserializer(FlowDeserializer flowDeserializer) {
		this.flowDeserializer = flowDeserializer;
	}
}
