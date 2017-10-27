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
import java.util.Collection;
import java.util.List;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.KnowledgeSessionImpl;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.FactTracker;
import com.bstek.urule.runtime.rete.ReteInstance;
/**
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public class Agenda {
	private Context context;
	private List<RuleBox> ruleBoxes=new ArrayList<RuleBox>();
	private List<RuleInfo> matchedRules=new ArrayList<RuleInfo>();
	public Agenda(WorkingMemory workingMemory,Context context){
		this.context=context;
		ruleBoxes.add(new AgendaGroupRuleBox(context,matchedRules));
		ruleBoxes.add(new ActivationGroupRuleBox(context,matchedRules));
		ruleBoxes.add(new ActivationRuleBox(context,matchedRules));
	}
	public RuleExecutionResponse execute(AgendaFilter filter,int max){
		ExecutionResponseImpl response=new ExecutionResponseImpl();
		List<ActionValue> actionValues=new ArrayList<ActionValue>();
		response.setActionValues(actionValues);
		List<RuleInfo> firedRules=new ArrayList<RuleInfo>();
		RuleBox ruleBox=nextRuleBox();
		while(ruleBox!=null){
			List<RuleInfo> ruleInfoResult=ruleBox.execute(filter, max-firedRules.size(),actionValues);
			if(ruleInfoResult!=null && ruleInfoResult.size()>0){
				firedRules.addAll(ruleInfoResult);
			}
			if(firedRules.size()>=max){
				break;
			}
			ruleBox=nextRuleBox();
		}
		
		KnowledgeSessionImpl session=(KnowledgeSessionImpl)context.getWorkingMemory();
		List<ReteInstance> reteInstanceList=session.getReteInstanceList();
		for(ReteInstance reteInstance:reteInstanceList){
			reteInstance.reset();			
		}
		session.getAllFacts().clear();
		response.setFiredRules(firedRules);
		response.setMatchedRules(matchedRules);
		return response;
	}
	
	private RuleBox nextRuleBox(){
		for(RuleBox ruleBox:ruleBoxes){
			RuleBox next=ruleBox.next();
			if(next!=null){
				return next;
			}
		}
		return null;
	}
	
	
	public void addTrackers(Collection<FactTracker> list){
		for(FactTracker tracker:list){
			Activation activation=tracker.getActivation();
			for(RuleBox ruleBox:ruleBoxes){
				boolean add=ruleBox.add(activation);
				if(add){
					break;
				}
			}
		}
	}
	
	public void retract(Object obj){
		for(RuleBox ruleBox:ruleBoxes){
			ruleBox.retract(obj);
		}
	}
	
	public void reevaluate(Object obj,EvaluationContext context){
		for(RuleBox ruleBox:ruleBoxes){
			ruleBox.reevaluate(obj, context);
		}
	}
	
	public List<RuleBox> getRuleBoxes() {
		return ruleBoxes;
	}
	
	public void clean(){
		for(RuleBox ruleBox:ruleBoxes){
			ruleBox.clean();
		}
	}
}
