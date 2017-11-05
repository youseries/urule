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

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class Diagram {
	private List<Edge> edges;
	private NodeInfo rootNode;
	private int width;
	private int height;
	public Diagram(List<Edge> edges, NodeInfo rootNode) {
		this.edges = edges;
		this.rootNode = rootNode;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public NodeInfo getRootNode() {
		return rootNode;
	}
	public void setRootNode(NodeInfo rootNode) {
		this.rootNode = rootNode;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
