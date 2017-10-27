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
package com.bstek.urule.console.servlet.flow;

import java.util.List;

import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ProcessDefinition;
import com.bstek.urule.model.rule.Library;

/**
 * @author Jacky.gao
 * @since 2016年7月27日
 */
public class FlowDefinitionWrapper {

	private String id;
	private List<Library> libraries;
	private List<FlowNode> nodes;
	public FlowDefinitionWrapper(ProcessDefinition flowDefinition) {
		this.id = flowDefinition.getId();
		this.libraries = flowDefinition.getLibraries();
		this.nodes = flowDefinition.getNodes();
	}
	
	public List<FlowNode> getNodes() {
		return nodes;
	}


	public void setNodes(List<FlowNode> nodes) {
		this.nodes = nodes;
	}


	public List<Library> getLibraries() {
		return libraries;
	}
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
