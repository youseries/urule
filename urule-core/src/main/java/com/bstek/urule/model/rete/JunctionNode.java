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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public abstract class JunctionNode extends BaseReteNode{
	protected int toLineCount;
	@JsonIgnore
	protected List<Line> toConnections=new ArrayList<Line>();
	public JunctionNode(int id) {
		super(id);
	}
	public List<Line> getToConnections() {
		return toConnections;
	}
	public void addToConnection(Line connection){
		this.toConnections.add(connection);
		toLineCount=this.toConnections.size();
	}
	public int getToLineCount() {
		return toLineCount;
	}
}
