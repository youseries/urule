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
import java.util.List;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.rete.Context;
/**
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public class ActivationGroup extends RuleGroup{
	private boolean executed;
	public ActivationGroup(String name,List<RuleInfo> executedRules) {
		super(name, executedRules);
	}
	@Override
	public List<RuleInfo> execute(Context context, AgendaFilter filter, int max,List<ActionValue> actionValues) {
		executed=true;
		Activation activation=fetchNextExecutableActivation(activations);
		if(activation==null){
			return null;
		}
		activations.clear();
		if(filter==null || filter.accept(activation)){
			RuleInfo ruleInfo=activation.execute(context, executedRules,actionValues);
			List<RuleInfo> ruleInfos=new ArrayList<RuleInfo>();
			ruleInfos.add(ruleInfo);
			return ruleInfos;
		}
		return null;
	}
	public boolean isExecuted() {
		return executed;
	}
}
