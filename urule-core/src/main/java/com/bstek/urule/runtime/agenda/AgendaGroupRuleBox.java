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
package com.bstek.urule.runtime.agenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
/**
 * 以agenda-group属性划分的组，如果该属性相同，则划到这个组当中，<br>
 * 默认情况下，引擎在执行这些有agenda-group属性的规则时，需要某个组得到了焦点，<br>
 * 或者当前组中要执行的规则设置了auto-focus=true，否则该组下所有规则不会被执行。<br>
 * agenda-group级别最高，高于activation-group级别。
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public class AgendaGroupRuleBox extends AbstractRuleBox {
	private Map<String,AgendaGroup> agendaGroupMap=new HashMap<String,AgendaGroup>();
	public AgendaGroupRuleBox(Context context,List<RuleInfo> executedRules) {
		super(context,executedRules);
	}
	public List<RuleInfo> execute(AgendaFilter filter, int max,List<ActionValue> actionValues) {
		List<RuleInfo> ruleInfos=new ArrayList<RuleInfo>();
		AgendaGroup ag=findNextAgendaGroup();
		while(ag!=null){
			List<RuleInfo> ruleInfoResult=ag.execute(context, filter,max,actionValues);
			ruleInfos.addAll(ruleInfoResult);
			if(ruleInfos.size()>=max){
				break;
			}
			ag=findNextAgendaGroup();
		}
		return ruleInfos;
	}
	
	public AgendaGroup findNextAgendaGroup(){
		for(AgendaGroup ag:agendaGroupMap.values()){
			if(!ag.isExecuted()){
				return ag;
			}
		}
		return null;
	}
	
	@Override
	public RuleBox next() {
		AgendaGroup group= findNextAgendaGroup();
		if(group!=null){
			return this;
		}
		return null;
	}
	
	public AgendaGroup getAgendaGroup(String agendaGroupName){
		return agendaGroupMap.get(agendaGroupName);
	}
	
	@Override
	public void clean() {
		executedRules.clear();
		agendaGroupMap.clear();
		rules.clear();
	}
	
	@Override
	public void retract(Object obj) {
		for(AgendaGroup group:agendaGroupMap.values()){
			List<Activation> activations=group.getActivations();
			super.retract(obj, activations);			
		}
	}
	
	@Override
	public void reevaluate(Object obj,EvaluationContext context) {
		for(AgendaGroup group:agendaGroupMap.values()){
			List<Activation> activations=group.getActivations();
			super.reevaluate(obj, activations, context);			
		}
	}
	
	@Override
	public boolean add(Activation activation) {
		boolean shouldAdd=this.activationShouldAdd(activation);
		if(!shouldAdd){
			return false;
		}
		Rule rule=activation.getRule();
		String agendaGroup=rule.getAgendaGroup();
		if(StringUtils.isEmpty(agendaGroup)){
			return false;
		}
		AgendaGroup group=agendaGroupMap.get(agendaGroup);
		if(group==null){
			group=new AgendaGroup(agendaGroup,executedRules);
			agendaGroupMap.put(agendaGroup, group);
		}
		List<Activation> list=group.getActivations();
		addActivation(activation, list);
		rules.add(activation.getRule());	
		return true;
	}
}
