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
	private boolean debug;
	public FlowInstance(ProcessDefinition flowDefinition,boolean debug) {
		this.flowDefinition=flowDefinition;
		id=UUID.randomUUID().toString();
		this.debug=debug;
	}
	
	public boolean isDebug() {
		return debug;
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
		FlowInstance instance=new FlowInstance(flowDefinition,debug);
		instance.setParallelInstanceCount(childCount);
		instance.setParent(this);
		addChild(instance);
		return instance;
	}
}
