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
