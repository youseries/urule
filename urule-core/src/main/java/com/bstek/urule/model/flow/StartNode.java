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
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.event.impl.ProcessAfterStartedEventImpl;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class StartNode extends FlowNode {
	private FlowNodeType type=FlowNodeType.Start;
	public StartNode() {
	}
	public StartNode(String name) {
		super(name);
	}
	@Override
	public FlowNodeType getType() {
		return type;
	}
	@Override
	public void enterNode(FlowContext context,FlowInstance instance) {
		KnowledgeSession session=(KnowledgeSession)context.getWorkingMemory();
		session.fireEvent(new ProcessAfterStartedEventImpl(instance,session));
		executeNodeEvent(EventType.enter,context,instance);
		executeNodeEvent(EventType.leave,context,instance);
		leave(null,context, instance);
	}
}
