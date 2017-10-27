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

import java.util.List;
import java.util.Map;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public abstract class BindingNode extends FlowNode {
	private KnowledgePackageWrapper knowledgePackageWrapper;
	public BindingNode() {
	}
	public BindingNode(String name) {
		super(name);
	}
	
	protected KnowledgeSession executeKnowledgePackage(FlowContext context,ProcessInstance instance){
		KnowledgeSession parentSession=(KnowledgeSession)context.getWorkingMemory();
		List<Object> facts=parentSession.getAllFacts();
		KnowledgePackage knowledgePackage=knowledgePackageWrapper.getKnowledgePackage();
		KnowledgeSession session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
		for(Object fact:facts){
			session.insert(fact);
		}
		if(knowledgePackage.getFlowMap()==null || knowledgePackage.getFlowMap().size()==0){
			RuleExecutionResponse ruleExecutionResponse=session.fireRules(context.getVariables());
			((ExecutionResponseImpl)context.getResponse()).addRuleExecutionResponse(ruleExecutionResponse);
		}else{
			String processId=knowledgePackage.getFlowMap().values().iterator().next().getId();
			FlowExecutionResponse flowExecutionResponse=session.startProcess(processId,context.getVariables());
			((ExecutionResponseImpl)context.getResponse()).addFlowExecutionResponse(flowExecutionResponse);
		}
		Map<String,Object> parameters=session.getParameters();
		Map<String,Object> variables=context.getVariables();
		for(String key:parameters.keySet()){
			if(key.equals(DecisionItem.RETURN_VALUE_KEY)){
				continue;
			}
			variables.put(key, parameters.get(key));
		}
		return session;
	}
	public KnowledgePackageWrapper getKnowledgePackageWrapper() {
		return knowledgePackageWrapper;
	}
	public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
		this.knowledgePackageWrapper = knowledgePackageWrapper;
	}
}
