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
	private List<RuleInfo> matchedRules;
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
	public void setMatchedRules(List<RuleInfo> matchedRules) {
		this.matchedRules = matchedRules;
	}
	public List<RuleInfo> getFiredRules() {
		return rulesFired;
	}
	public void setFiredRules(List<RuleInfo> rulesFired) {
		this.rulesFired = rulesFired;
	}
}
