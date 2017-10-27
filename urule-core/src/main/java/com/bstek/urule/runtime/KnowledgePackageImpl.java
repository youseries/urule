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
package com.bstek.urule.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.bstek.urule.model.Node;
import com.bstek.urule.model.RuleJsonDeserializer;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.TerminalNode;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.rete.ReteInstance;

/**
 * @author Jacky.gao
 * @since 2015年1月20日
 */
public class KnowledgePackageImpl implements KnowledgePackage {
	private Rete rete;
	private Map<String,String> variableCategoryMap=new HashMap<String,String>();
	private Map<String,FlowDefinition> flowMap;
	private Map<String, String> parameters;
	
	@JsonDeserialize(using=RuleJsonDeserializer.class)
	private List<Rule> noLhsRules;
	
	@JsonIgnore
	private Map<Rule,Rule> elseRulesMap=new HashMap<Rule,Rule>();
	
	@JsonIgnore
	private List<Rule> withElseRules=new ArrayList<Rule>();
	
	private long timestamp;
	private String id=UUID.randomUUID().toString();
	public KnowledgePackageImpl() {
		timestamp=System.currentTimeMillis();
	}
	
	public void buildWithElseRules(){
		List<ObjectTypeNode> typeNodes=rete.getObjectTypeNodes();
		if(typeNodes!=null){
			for(ObjectTypeNode typeNode:typeNodes){
				buildReteLinesForElseRules(typeNode.getLines());
			}			
		}
	}
	
	private void buildReteLinesForElseRules(List<Line> lines){
		if(lines==null)return;
		for(Line line:lines){
			Node toNode=line.getTo();
			if(toNode==null)continue;
			if(toNode instanceof TerminalNode){
				TerminalNode terminalNode=(TerminalNode)toNode;
				Rule rule=terminalNode.getRule();
				if(!withElseRules.contains(rule)){
					Other other=rule.getOther();
					if(other!=null && other.getActions()!=null && other.getActions().size()>0){
						withElseRules.add(rule);
						
						Rule elseRule=new Rule();
						elseRule.setName(rule.getName()+"else");
						elseRule.setActivationGroup(rule.getActivationGroup());
						elseRule.setAgendaGroup(rule.getAgendaGroup());
						elseRule.setAutoFocus(rule.getAutoFocus());
						elseRule.setEffectiveDate(rule.getEffectiveDate());
						elseRule.setExpiresDate(rule.getExpiresDate());
						elseRule.setEnabled(rule.getEnabled());
						elseRule.setRuleflowGroup(rule.getRuleflowGroup());
						elseRule.setSalience(rule.getSalience());
						Rhs rhs=new Rhs();
						rhs.setActions(other.getActions());
						elseRule.setRhs(rhs);

						elseRulesMap.put(rule, elseRule);
					}
				}
			}else if(toNode instanceof BaseReteNode){
				BaseReteNode reteNode=(BaseReteNode)toNode;
				buildReteLinesForElseRules(reteNode.getLines());
			}
		}
	}
	
	public Rule getElseRule(Rule rule){
		return elseRulesMap.get(rule);
	}
	
	public String getId() {
		return id;
	}
	
	public Rete getRete() {
		return rete;
	}
	public void setRete(Rete rete) {
		this.rete = rete;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void resetTimestamp() {
		timestamp=System.currentTimeMillis();
	}
	public Map<String, String> getVariableCateogoryMap() {
		return variableCategoryMap;
	}
	public void setNoLhsRules(List<Rule> noLhsRules) {
		this.noLhsRules = noLhsRules;
	}
	@Override
	public List<Rule> getNoLhsRules() {
		return noLhsRules;
	}
	
	@Override
	public List<Rule> getWithElseRules() {
		return this.withElseRules;
	}
	
	
	public void setVariableCategoryMap(Map<String, String> variableCategoryMap) {
		this.variableCategoryMap = variableCategoryMap;
	}
	public Map<String, FlowDefinition> getFlowMap() {
		return flowMap;
	}
	public void setFlowMap(Map<String, FlowDefinition> flowMap) {
		this.flowMap = flowMap;
	}
	public ReteInstance newReteInstance() {
		return rete.newReteInstance();
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	@Override
	public Map<String, String> getParameters() {
		return this.parameters;
	}
}
