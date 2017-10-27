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
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;


/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public abstract class BaseReteNode extends ReteNode {
	@JsonIgnore
	private List<ReteNode> childrenNodes=new ArrayList<ReteNode>();
	protected List<Line> lines;
	public BaseReteNode(int id) {
		super(id);
	}
	
	public List<ReteNode> getChildrenNodes() {
		return childrenNodes;
	}
	public void setChildrenNodes(List<ReteNode> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}
	protected boolean buildVariables(Context context,Value value,Map<String,Object> variableMap){
		
		return true;
	}
	
	protected Object fetchData(Object object,String property){
		try {
			return BeanUtils.getProperty(object, property);
		} catch (Exception e) {
			throw new RuleException(e);
		}
	}
	
	public Line addLine(ReteNode toNode){
		if(childrenNodes==null){
			childrenNodes=new ArrayList<ReteNode>();
		}
		childrenNodes.add(toNode);
		Line line=new Line(this,toNode);
		if(lines==null){
			lines=new ArrayList<Line>();
		}
		lines.add(line);
		if(toNode instanceof JunctionNode){
			JunctionNode junctionNode=(JunctionNode)toNode;
			junctionNode.addToConnection(line);
		}
		return line;
	}
	
	public List<Line> getLines() {
		return lines;
	}
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
}
