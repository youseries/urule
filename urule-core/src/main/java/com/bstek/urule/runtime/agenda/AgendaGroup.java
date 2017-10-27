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
public class AgendaGroup extends RuleGroup{
	private boolean focus;
	private boolean executed;
	public AgendaGroup(String name,List<RuleInfo> executedRules) {
		super(name, executedRules);
	}
	@Override
	public List<RuleInfo> execute(Context context,AgendaFilter filter, int max,List<ActionValue> actionValues){
		executed=true;
		List<RuleInfo> ruleInfos=new ArrayList<RuleInfo>();
		for(int i=0;i<max;i++){
			Activation activation=fetchNextExecutableActivation(activations);
			if(activation==null){
				return ruleInfos;
			}
			activations.remove(activation);
			ActivationImpl ac=(ActivationImpl)activation;
			ac.setProcessed(true);
			boolean autoFocus=false;
			if(activation.getRule().getAutoFocus()!=null){
				autoFocus=activation.getRule().getAutoFocus();
			}
			RuleInfo ruleInfo=null;
			if(focus){
				ruleInfo=activation.execute(context, executedRules,actionValues);
			}else if(autoFocus){
				ruleInfo=activation.execute(context, executedRules,actionValues);
			}
			if(ruleInfo!=null){
				ruleInfos.add(ruleInfo);
			}
			if(ruleInfos.size()>=max){
				break;
			}
		}
		return ruleInfos;
	}

	public boolean isExecuted() {
		return executed;
	}
	
	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}
}
