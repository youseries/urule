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
