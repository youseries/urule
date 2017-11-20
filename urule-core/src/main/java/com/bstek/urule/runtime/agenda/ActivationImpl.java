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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.event.impl.ActivationAfterFiredEventImpl;
import com.bstek.urule.runtime.event.impl.ActivationBeforeFiredEventImpl;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public class ActivationImpl implements Activation{
	private boolean processed;
	private Rule rule;
	//当前WorkingMemory当中命名变量,为then或else部分可能存在的根据变量引用进行的计算操作作为准备
	private Map<String,Object> variableMap;
	private Map<Object,List<BaseCriteria>> objectCriteriaMap=new HashMap<Object,List<BaseCriteria>>();
	public ActivationImpl(Rule rule,Map<String,Object> variableMap) {
		this.rule=rule;
		this.variableMap=variableMap;
	}
	public RuleInfo execute(Context context,List<RuleInfo> executedRules,List<ActionValue> actionValues) {
		KnowledgeSession session = (KnowledgeSession)context.getWorkingMemory();
		session.fireEvent(new ActivationBeforeFiredEventImpl(this,session));
		executedRules.add(rule);
		boolean enabled=true;
		if(rule.getEnabled()!=null){
			enabled=rule.getEnabled();
		}
		if(!enabled){
			return null;
		}
		Date now=new Date();
		Date effectiveDate=rule.getEffectiveDate();
		if(effectiveDate!=null){
			if(effectiveDate.getTime()>now.getTime()){
				return null;
			}			
		}
		Date expiresDate=rule.getExpiresDate();
		if(expiresDate!=null){
			if(expiresDate.getTime()<now.getTime()){
				return null;
			}
		}
		
		List<Object> matchedObjects=new ArrayList<Object>();
		matchedObjects.addAll(objectCriteriaMap.keySet());
		if(rule instanceof LoopRule){
			LoopRule loopRule=(LoopRule)rule;
			List<ActionValue> values=loopRule.execute(context, objectCriteriaMap.keySet(), matchedObjects,variableMap);
			if(values!=null){
				actionValues.addAll(values);
			}
		}else if(rule instanceof ScoreRule){
			ScoreRule scoreRule=(ScoreRule)rule;
			scoreRule.execute(context, objectCriteriaMap.keySet(), matchedObjects,variableMap);
		}else{	
			Rhs rhs=rule.getRhs();
			if(rhs!=null){
				List<Action> actions=rhs.getActions();
				if(actions!=null){
					for(Action action:actions){
						if(rule.getDebug()!=null){
							action.setDebug(rule.getDebug());							
						}
						ActionValue actionValue=action.execute(context,objectCriteriaMap.keySet(),matchedObjects,variableMap);
						if(actionValue!=null){
							actionValues.add(actionValue);
						}
					}
				}
			}
		}
		session.fireEvent(new ActivationAfterFiredEventImpl(this,session));
		processed=true;
		return rule;
	}
	public void setObjectCriteriaMap(Map<Object, List<BaseCriteria>> objectCriteriaMap) {
		this.objectCriteriaMap = objectCriteriaMap;
	}
	
	@Override
	public boolean contain(Object obj) {
		return objectCriteriaMap.containsKey(obj);
	}
	
	@Override
	public boolean reevaluate(Object obj,EvaluationContext context) {
		Object key=obj;
		if((obj instanceof HashMap) && !(obj instanceof GeneralEntity)){
			key=HashMap.class.getName();
		}
		if(!objectCriteriaMap.containsKey(key)){
			return true;
		}
		List<BaseCriteria> list=objectCriteriaMap.get(key);			
		boolean result=false;
		for(BaseCriteria criteria:list){
			List<Object> allMatchedObjects=new ArrayList<Object>();
			EvaluateResponse response=criteria.evaluate(context,obj,allMatchedObjects);
			result=response.getResult();
			if(result){
				for(Object object:allMatchedObjects){
					addObjectCriteria(object, criteria);
				}
			}else{
				break;
			}
		}
		return result;
	}
	private void addObjectCriteria(Object obj, BaseCriteria criteria) {
		if(obj instanceof HashMap && !(obj instanceof GeneralEntity)){
			obj=HashMap.class.getName();
		}
		if(objectCriteriaMap.containsKey(obj)){
			List<BaseCriteria> list=objectCriteriaMap.get(obj);
			if(!list.contains(criteria)){
				list.add(criteria);
			}
		}else{
			List<BaseCriteria> list=new ArrayList<BaseCriteria>();
			list.add(criteria);
			objectCriteriaMap.put(obj, list);
		}
	}
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	public int compareTo(Activation o) {
		Integer o1=o.getRule().getSalience();
		Integer o2=rule.getSalience();
		if(o1!=null && o2!=null){
			return o1-o2;			
		}else if(o1!=null){
			return 1;
		}else if(o2!=null){
			return -1;
		}
		return 0;
	}
}
