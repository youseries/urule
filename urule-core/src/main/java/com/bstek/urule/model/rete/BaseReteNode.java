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
