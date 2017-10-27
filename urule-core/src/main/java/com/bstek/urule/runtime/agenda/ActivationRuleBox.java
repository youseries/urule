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
import java.util.List;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
/**
 * 所有未定义activation-group及agenda-group的满足条件的规则都放在此处
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public class ActivationRuleBox extends AbstractRuleBox{
	private List<Activation> activations=new ArrayList<Activation>();
	public ActivationRuleBox(Context context,List<RuleInfo> executedRules) {
		super(context,executedRules);
	}
	public List<RuleInfo> execute(AgendaFilter filter, int max,List<ActionValue> actionValues) {
		Activation activation=findNextActivation();
		List<RuleInfo> ruleInfos=new ArrayList<RuleInfo>();
		while(activation!=null){
			ActivationImpl ac=(ActivationImpl)activation;
			ac.setProcessed(true);
			if(filter!=null && !filter.accept(activation)){
				continue;
			}
			if(ruleInfos.size()>=max){
				break;
			}
			RuleInfo ruleInfo=activation.execute(context, executedRules,actionValues);
			if(ruleInfo!=null){
				ruleInfos.add(ruleInfo);
			}
			activation=RuleGroup.fetchNextExecutableActivation(activations);
		}
		return ruleInfos;
	}
	
	public Activation findNextActivation(){
		return RuleGroup.fetchNextExecutableActivation(activations);
	}
	
	@Override
	public RuleBox next() {
		Activation activation = findNextActivation();
		if(activation!=null){
			return this;
		}
		return null;
	}
	
	@Override
	public void clean() {
		executedRules.clear();
		activations.clear();
		rules.clear();
	}
	
	public void retract(Object obj){
		super.retract(obj, activations);
	}
	
	@Override
	public void reevaluate(Object obj,EvaluationContext context) {
		super.reevaluate(obj, activations, context);			
	}
	
	@Override
	public boolean add(Activation activation) {
		boolean shouldAdd=this.activationShouldAdd(activation);
		if(!shouldAdd){
			return false;
		}
		rules.add(activation.getRule());
		return addActivation(activation, activations);
	}
}
