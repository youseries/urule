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
	private boolean debug;
	private List<Library> libraries;
	private List<FlowNode> nodes;
	public FlowDefinitionWrapper(ProcessDefinition flowDefinition) {
		this.id = flowDefinition.getId();
		this.debug=flowDefinition.isDebug();
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

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
