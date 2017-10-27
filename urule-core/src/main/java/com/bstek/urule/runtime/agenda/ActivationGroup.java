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
