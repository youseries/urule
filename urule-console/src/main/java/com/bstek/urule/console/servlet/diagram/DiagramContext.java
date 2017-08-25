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
package com.bstek.urule.console.servlet.diagram;

import java.util.List;
import java.util.Map;

import com.bstek.urule.model.Node;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class DiagramContext {
	private int id;
	private List<Edge> edges;
	private Map<Node,NodeInfo> nodeMap;
	public DiagramContext(List<Edge> edges,Map<Node, NodeInfo> nodeMap) {
		this.edges = edges;
		this.nodeMap = nodeMap;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	public Map<Node, NodeInfo> getNodeMap() {
		return nodeMap;
	}
	public void setNodeMap(Map<Node, NodeInfo> nodeMap) {
		this.nodeMap = nodeMap;
	}
	public int nextId(){
		id++;
		return id;
	}
}
