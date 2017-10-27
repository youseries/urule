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
 * 规则以activation-group属性进行分组，该属性相同且满足所有条件的规则都会划到此组中,<br>
 * 这样，在执行时这个组中规则只要有一个执行，那么其它的规则将不再执行，因此也叫互斥组。
 * activation-group级别要低于agenda-group级别。
 * @author Jacky.gao
 * @since 2015年1月2日
 * */
public class ActivationGroupRuleBox extends AbstractRuleBox {
	private Map<String,ActivationGroup> activationGroupMap=new HashMap<String,ActivationGroup>();
	public ActivationGroupRuleBox(Context context,List<RuleInfo> executedRules) {
		super(context, executedRules);
	}

	public List<RuleInfo> execute(AgendaFilter filter, int max,List<ActionValue> actionValues) {
		List<RuleInfo> ruleInfos=new ArrayList<RuleInfo>();
		ActivationGroup group=findNextActivationGroup();
		while(group!=null){
			if(ruleInfos.size()>=max){
				break;
			}
			List<RuleInfo> ruleInfoResult=group.execute(context, filter,1,actionValues);
			if(ruleInfoResult!=null){
				ruleInfos.addAll(ruleInfoResult);
			}
			if(ruleInfos.size()>=max){
				break;
			}
			group=findNextActivationGroup();
		}
		return ruleInfos;
	}
	
	public ActivationGroup findNextActivationGroup(){
		for(ActivationGroup group:activationGroupMap.values()){
			if(!group.isExecuted()){
				return group;
			}
		}
		return null;
	}
	
	@Override
	public RuleBox next() {
		ActivationGroup group= findNextActivationGroup();
		if(group!=null){
			return this;
		}
		return null;
	}
	
	@Override
	public void clean() {
		executedRules.clear();
		activationGroupMap.clear();
		rules.clear();
	}
	
	@Override
	public void retract(Object obj) {
		for(ActivationGroup group:activationGroupMap.values()){
			List<Activation> activations=group.getActivations();
			super.retract(obj, activations);
		}
	}
	
	@Override
	public void reevaluate(Object obj,EvaluationContext context) {
		for(ActivationGroup group:activationGroupMap.values()){
			List<Activation> activations=group.getActivations();
			super.reevaluate(obj, activations, context);			
		}
	}

	public boolean add(Activation activation) {
		boolean shouldAdd=this.activationShouldAdd(activation);
		if(!shouldAdd){
			return false;
		}
		Rule rule=activation.getRule();
		String activationGroup=rule.getActivationGroup();
		if(StringUtils.isEmpty(activationGroup)){
			return false;
		}
		ActivationGroup group=activationGroupMap.get(activationGroup);
		if(group==null){
			group=new ActivationGroup(activationGroup,executedRules);
			activationGroupMap.put(activationGroup, group);
		}
		List<Activation> list=group.getActivations();
		addActivation(activation, list);
		rules.add(activation.getRule());
		return true;
	}
}
