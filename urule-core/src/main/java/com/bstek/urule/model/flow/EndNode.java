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

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class EndNode extends FlowNode{
	private FlowNodeType type=FlowNodeType.End;
	public EndNode() {
	}
	@Override
	public FlowNodeType getType() {
		return type;
	}
	public EndNode(String name) {
		super(name);
	}
	@Override
	public void enterNode(FlowContext context, FlowInstance instance) {
		executeNodeEvent(EventType.enter, context, instance);
		instance.setCurrentNode(this);
		executeNodeEvent(EventType.leave, context, instance);
	}
}
