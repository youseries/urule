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
package com.bstek.urule.model.flow;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class JoinNode extends FlowNode {
	private FlowNodeType type=FlowNodeType.Join;
	public JoinNode() {
	}
	public JoinNode(String name) {
		super(name);
	}
	@Override
	public FlowNodeType getType() {
		return type;
	}
	@Override
	public void enterNode(FlowContext context,FlowInstance instance) {
		instance.setCurrentNode(this);
		executeNodeEvent(EventType.enter, context, instance);
		FlowInstance parentInstance=instance.getParent();
		if(parentInstance==null){
			throw new RuleException("Invalid flow instance.");
		}
		String id=parentInstance.getId();
		int arrivedChild=1;
		if(context.getVariable(id)==null){
			context.addVariable(id, arrivedChild);
		}else{
			arrivedChild=(Integer)context.getVariable(id);
			arrivedChild++;
			context.addVariable(id, arrivedChild);
		}
		executeNodeEvent(EventType.leave, context, instance);
		int childCount=instance.getParallelInstanceCount();
		if(arrivedChild>=childCount){
			context.removeVariable(id);
			leave(null,context,parentInstance);
		}
	}	
}
