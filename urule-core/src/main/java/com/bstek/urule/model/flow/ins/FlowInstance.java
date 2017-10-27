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
package com.bstek.urule.model.flow.ins;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ProcessDefinition;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class FlowInstance implements ProcessInstance{
	private String id;
	private int parallelInstanceCount;
	private ProcessDefinition flowDefinition;
	private FlowInstance parent;
	private List<ProcessInstance> children=new ArrayList<ProcessInstance>();
	private FlowNode currentNode;
	public FlowInstance(ProcessDefinition flowDefinition) {
		this.flowDefinition=flowDefinition;
		id=UUID.randomUUID().toString();
	}
	
	@Override
	public ProcessDefinition getProcessDefinition() {
		return flowDefinition;
	}
	
	@Override
	public List<ProcessInstance> getChildren() {
		return children;
	}
	
	public void addChild(FlowInstance child){
		children.add(child);
	}
	@Override
	public int getParallelInstanceCount() {
		return parallelInstanceCount;
	}

	public void setParallelInstanceCount(int parallelInstanceCount) {
		this.parallelInstanceCount = parallelInstanceCount;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public FlowNode getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(FlowNode currentNode) {
		this.currentNode = currentNode;
	}
	public void setParent(FlowInstance parent) {
		this.parent = parent;
	}
	@Override
	public FlowInstance getParent() {
		return parent;
	}
	public FlowInstance newChildInstance(int childCount){
		FlowInstance instance=new FlowInstance(flowDefinition);
		instance.setParallelInstanceCount(childCount);
		instance.setParent(this);
		addChild(instance);
		return instance;
	}
}
