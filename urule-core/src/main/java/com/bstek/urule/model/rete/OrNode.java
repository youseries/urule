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

import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.OrActivity;


/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class OrNode extends JunctionNode {
	private NodeType nodeType=NodeType.or;
	public OrNode() {
		super(0);
	}
	public OrNode(int id) {
		super(id);
	}
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
	@Override
	public Activity newActivity(Map<Object,Object> context) {
		if(context.containsKey(this)){
			return (OrActivity)context.get(this);
		}
		OrActivity or=new OrActivity();
		for(Line line:lines){
			or.addPath(line.newPath(context));
		}
		context.put(this, or);
		return or;
	}
}
