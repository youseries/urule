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
