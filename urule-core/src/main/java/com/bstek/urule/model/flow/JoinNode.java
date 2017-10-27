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
