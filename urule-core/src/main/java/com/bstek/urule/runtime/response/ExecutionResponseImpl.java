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
package com.bstek.urule.runtime.response;

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.RuleInfo;

/**
 * @author Jacky.gao
 * @since 2015年1月27日
 */
public class ExecutionResponseImpl implements RuleExecutionResponse,FlowExecutionResponse {
	private long duration;
	private String flowId;
	private List<String> nodeNames=new ArrayList<String>();
	private List<RuleExecutionResponse> ruleExecutionResponses=new ArrayList<RuleExecutionResponse>();
	private List<FlowExecutionResponse> flowExecutionResponses=new ArrayList<FlowExecutionResponse>();
	private List<RuleInfo> rulesFired;
	private List<RuleInfo> matchedRules=new ArrayList<RuleInfo>();
	private List<ActionValue> actionValues;
	
	@Override
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	
	public void addFlowExecutionResponse(FlowExecutionResponse response) {
		flowExecutionResponses.add(response);
	}
	
	@Override
	public List<FlowExecutionResponse> getFlowExecutionResponses() {
		return flowExecutionResponses;
	}
	
	@Override
	public List<RuleExecutionResponse> getRuleExecutionResponses() {
		return ruleExecutionResponses;
	}

	public void addRuleExecutionResponse(RuleExecutionResponse response) {
		ruleExecutionResponses.add(response);
	}
	
	@Override
	public List<String> getNodeNames() {
		return nodeNames;
	}
	
	public void addNodeName(String nodeName) {
		nodeNames.add(nodeName);
	}
	
	@Override
	public List<ActionValue> getActionValues() {
		return actionValues;
	}
	public void setActionValues(List<ActionValue> actionValues) {
		this.actionValues = actionValues;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public List<RuleInfo> getMatchedRules() {
		return matchedRules;
	}
	public void addMatchedRules(List<RuleInfo> matchedRules){
		this.matchedRules.addAll(matchedRules);
	}
	public List<RuleInfo> getFiredRules() {
		return rulesFired;
	}
	public void setFiredRules(List<RuleInfo> rulesFired) {
		this.rulesFired = rulesFired;
	}
}
