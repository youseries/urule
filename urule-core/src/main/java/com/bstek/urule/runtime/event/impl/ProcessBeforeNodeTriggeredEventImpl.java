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
package com.bstek.urule.runtime.event.impl;

import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.event.ProcessBeforeNodeTriggeredEvent;

/**
 * @author Jacky.gao
 * @since 2015年7月21日
 */
public class ProcessBeforeNodeTriggeredEventImpl extends DefaultProcessEvent implements ProcessBeforeNodeTriggeredEvent{
	private FlowNode flowNode;
	public ProcessBeforeNodeTriggeredEventImpl(FlowNode flowNode,ProcessInstance processInstance,KnowledgeSession knowledgeSession) {
		super(processInstance, knowledgeSession);
		this.flowNode=flowNode;
	}
	@Override
	public FlowNode getFlowNode() {
		return flowNode;
	}
}
