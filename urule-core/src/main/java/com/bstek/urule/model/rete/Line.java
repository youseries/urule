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
package com.bstek.urule.model.rete;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.model.Node;
import com.bstek.urule.runtime.rete.Path;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class Line {
	private int fromNodeId;
	private int toNodeId;
	@JsonIgnore
	private ReteNode from;
	@JsonIgnore
	private ReteNode to;
	public Line() {
	}
	public Line(ReteNode from, ReteNode to) {
		this.from = from;
		this.to = to;
		this.fromNodeId=from.getId();
		this.toNodeId=to.getId();
	}
	public void setTo(ReteNode to) {
		this.to = to;
	}
	public Node getFrom() {
		return from;
	}
	public void setFrom(ReteNode from) {
		this.from = from;
	}
	public Node getTo() {
		return to;
	}
	public Path newPath(Map<Object,Object> context){
		return new Path(to.newActivity(context));
	}
	public int getFromNodeId() {
		return fromNodeId;
	}
	public void setFromNodeId(int fromNodeId) {
		this.fromNodeId = fromNodeId;
	}
	public int getToNodeId() {
		return toNodeId;
	}
	public void setToNodeId(int toNodeId) {
		this.toNodeId = toNodeId;
	}
}
