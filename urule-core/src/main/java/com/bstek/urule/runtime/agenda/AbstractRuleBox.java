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
import java.util.Collections;
import java.util.List;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.event.impl.ActivationCancelledEventImpl;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
/**
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public abstract class AbstractRuleBox implements RuleBox {
	protected List<RuleInfo> executedRules;
	protected Context context;
	protected List<Rule> rules;
	public AbstractRuleBox(Context context,List<RuleInfo> executedRules) {
		this.context=context;
		this.rules=new ArrayList<Rule>();
		this.executedRules=executedRules;
	}
	protected void retract(Object obj,List<Activation> activations){
		List<Activation> needRemovedList=new ArrayList<Activation>();
		for(Activation activation:activations){
			if(activation.contain(obj)){
				needRemovedList.add(activation);
			}
		}
		KnowledgeSession session = (KnowledgeSession)context.getWorkingMemory();
		for(Activation ac:needRemovedList){
			activations.remove(ac);
			session.fireEvent(new ActivationCancelledEventImpl(ac,session));
		}
	}
	
	protected boolean addActivation(Activation activation, List<Activation> list) {
		boolean result=list.add(activation);
		Collections.sort(list);
		return result;
	}
	
	protected boolean activationShouldAdd(Activation activation){
		Rule rule=activation.getRule();
		for(Rule r:rules){
			if(r.equals(rule)){
				if(r.getLoop()!=null && r.getLoop()){
					return true;
				}else{
					return false;
				}
			}
		}
		return true;
	}

	
	@Override
	public List<Rule> getRules() {
		return rules;
	}
	
	protected List<Activation> reevaluate(Object obj,List<Activation> activations,EvaluationContext context){
		List<Activation> needRemoved=new ArrayList<Activation>();
		for(Activation activation:activations){
			if(activation.isProcessed()){
				continue;
			}
			if(!activation.reevaluate(obj,context)){
				needRemoved.add(activation);
			}
		}
		KnowledgeSession session = (KnowledgeSession)context.getWorkingMemory();
		for(Activation ac:needRemoved){
			activations.remove(ac);
			session.fireEvent(new ActivationCancelledEventImpl(ac,session));
		}
		return needRemoved;
	}
}
