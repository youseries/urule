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
package com.bstek.urule.model.rule.loop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.builtinaction.LoopAction;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.Context;

/**
 * @author Jacky.gao
 * @since 2016年5月31日
 */
public class LoopRule extends Rule{
	private LoopStart loopStart;
	private LoopEnd loopEnd;
	private LoopTarget loopTarget;
	private KnowledgePackageWrapper knowledgePackageWrapper;
	private Log log=LogFactory.getLog(getClass());
	public LoopRule() {
		this.setLoopRule(true);
	}
	
	public List<ActionValue> execute(Context context,Object matchedObject,List<Object> allMatchedObjects,Map<String,Object> variableMap){
		Object loopTargetObj=context.getValueCompute().complexValueCompute(loopTarget.getValue(), matchedObject, context, allMatchedObjects,variableMap);
		if(loopTargetObj==null){
			log.warn("Loop rule ["+getName()+"] target value is null,cannot be executed.");
			return null;
		}
		
		List<ActionValue> values=new ArrayList<ActionValue>();
		KnowledgeSession parentSession=(KnowledgeSession)context.getWorkingMemory();
		List<Object> facts=parentSession.getAllFacts();
		Map<String,Object> parameters=parentSession.getParameters();
		
		if(loopStart!=null){			
			List<Action> startActions=loopStart.getActions();
			if(startActions!=null){
				for(Action action:startActions){
					ActionValue value=action.execute(context, matchedObject, allMatchedObjects,variableMap);
					if(value!=null){
						values.add(value);
					}
				}
			}
		}

		KnowledgeSession session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackageWrapper.getKnowledgePackage());
		if(loopTargetObj instanceof Collection){
			Collection<?> collections=(Collection<?>)loopTargetObj;
			String loopClazz=null;
			for(Object object:collections){
				if(loopClazz==null){
					if(object instanceof GeneralEntity){
						loopClazz=((GeneralEntity)object).getTargetClass();
					}else{
						loopClazz=object.getClass().getName();
					}
				}
				for(Object fact:facts){
					String clazz=null;
					if(fact instanceof GeneralEntity){
						clazz=((GeneralEntity)fact).getTargetClass();
					}else{
						clazz=fact.getClass().getName();
					}
					if(!loopClazz.equals(clazz)){
						session.insert(fact);						
					}
				}
				session.insert(object);
				RuleExecutionResponse response=session.fireRules(parameters);
				List<ActionValue> list=response.getActionValues();
				boolean needBreak=false;
				if(list!=null){
					for(ActionValue av:list){
						if(av.getActionId().equals(LoopAction.BREAK_LOOP_ACTION_ID)){
							needBreak=true;
						}else{
							values.add(av);
						}
					}
				}
				parameters=new HashMap<String,Object>();
				parameters.putAll(session.getParameters());
				if(needBreak){
					break;
				}
			}
		}else if(loopTargetObj instanceof Object[]){
			Object[] objs=(Object[])loopTargetObj;
			for(Object object:objs){
				for(Object fact:facts){
					session.insert(fact);
				}
				session.insert(object);
				RuleExecutionResponse response=session.fireRules();
				List<ActionValue> list=response.getActionValues();
				boolean needBreak=false;
				if(list!=null){
					for(ActionValue av:list){
						if(av.getActionId().equals(LoopAction.BREAK_LOOP_ACTION_ID)){
							needBreak=true;
						}else{
							values.add(av);
						}
					}
				}
				parameters=new HashMap<String,Object>();
				parameters.putAll(session.getParameters());
				if(needBreak){
					break;
				}
			}
		}else{
			throw new RuntimeException("Loop rule target variable must be Collection or Object array type.");
		}
		parentSession.getParameters().putAll(parameters);
		if(loopEnd!=null){			
			List<Action> endActions=loopEnd.getActions();
			if(endActions!=null){
				for(Action action:endActions){
					ActionValue value=action.execute(context, matchedObject, allMatchedObjects,variableMap);
					if(value!=null){
						values.add(value);
					}
				}
			}
		}
		
		return values;
	}
		
	public LoopStart getLoopStart() {
		return loopStart;
	}
	public void setLoopStart(LoopStart loopStart) {
		this.loopStart = loopStart;
	}
	public LoopEnd getLoopEnd() {
		return loopEnd;
	}
	public void setLoopEnd(LoopEnd loopEnd) {
		this.loopEnd = loopEnd;
	}
	public LoopTarget getLoopTarget() {
		return loopTarget;
	}
	public void setLoopTarget(LoopTarget loopTarget) {
		this.loopTarget = loopTarget;
	}
	public KnowledgePackageWrapper getKnowledgePackageWrapper() {
		return knowledgePackageWrapper;
	}
	public void setKnowledgePackageWrapper(
			KnowledgePackageWrapper knowledgePackageWrapper) {
		this.knowledgePackageWrapper = knowledgePackageWrapper;
	}
}
