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
package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.Utils;
import com.bstek.urule.debug.MsgType;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;
import com.bstek.urule.model.rule.lhs.NamedCriteria;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class NamedCriteriaActivity  extends AbstractActivity{
	protected NamedCriteria criteria;
	private boolean pass;
	private boolean debug;
	public NamedCriteriaActivity(NamedCriteria criteria,boolean debug){
		this.criteria=criteria;
		this.debug=debug;
	}
	public List<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		String referenceName=criteria.getReferenceName();
		if(pass){
			if(StringUtils.isBlank(referenceName) || variableMap.containsKey(referenceName)){
				return null;
			}
		}
		List<Object> allMatchedObjects=new ArrayList<Object>();
		EvaluateResponse response=criteria.evaluate(context,obj,allMatchedObjects);
		boolean result=response.getResult();
		doDebug(response,context);
		if(result){
			if(StringUtils.isNotBlank(referenceName)){
				variableMap.put(referenceName,obj);				
			}
			context.setPrevActivity(this);
			pass=true;
			tracker.addObjectCriteria(obj, criteria);
			for(Object object:allMatchedObjects){
				tracker.addObjectCriteria(object, criteria);
			}
			return visitPahs(context,obj,tracker,variableMap);
		}
		return null;
	}
	
	private void doDebug(EvaluateResponse response,Context context){
		if(!debug || !Utils.isDebug()){
			return;
		}
		String id=criteria.getId();
		StringBuffer sb=new StringBuffer();
		sb.append("^^^命名条件："+id);
		String result=response.getResult() ? "满足" : "不满足";
		sb.append(" =>"+result);
		System.out.println(sb.toString());
		context.debugMsg(sb.toString(), MsgType.Condition, debug);
	}
	
	@Override
	public boolean orNodeIsPassed() {
		return false;
	}
	@Override
	public void reset() {
		pass=false;
	}
}
